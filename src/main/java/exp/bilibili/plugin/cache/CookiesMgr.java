package exp.bilibili.plugin.cache;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.envm.Identity;
import exp.bilibili.protocol.XHRSender;
import exp.libs.envm.Charset;
import exp.libs.utils.encode.CryptoUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.PathUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 账号cookie管理器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class CookiesMgr {

	/** cookie保存目录 */
	private final static String COOKIE_DIR = Config.getInstn().COOKIE_DIR();
	
	/**  文件名后缀 */
	private final static String SUFFIX = ".dat";
	
	/** 主号cookie文件路径 */
	private final static String COOKIE_MAIN_PATH = PathUtils.combine(COOKIE_DIR, 
			StrUtils.concat("cookie-main", SUFFIX));
	
	/** 马甲号cookie文件路径 */
	private final static String COOKIE_VEST_PATH = PathUtils.combine(COOKIE_DIR, 
			StrUtils.concat("cookie-vest", SUFFIX));
	
	/** 小号cookie文件名前缀 */
	private final static String COOKIE_MINI_PREFIX = "cookie-mini-";
	
	/** 上限保存的小号Cookie个数 */
	public final static int MAX_NUM = !Identity.less(Identity.ADMIN) ? 99 : 
		(!Identity.less(Identity.UPLIVE) ? 8 : 3);
	
	/** 主号cookie */
	private BiliCookie mainCookie;
	
	/** 马甲号cookie */
	private BiliCookie vestCookie;
	
	/** 小号cookie集 */
	private Set<BiliCookie> miniCookies;
	
	/** 小号cookie保存路径 */
	private Map<BiliCookie, String> miniPaths;
	
	/** 最近一次添加过cookie的时间点 */
	private long lastAddCookieTime;
	
	/** 单例 */
	private static volatile CookiesMgr instance;
	
	/**
	 * 构造函数
	 */
	private CookiesMgr() {
		this.mainCookie = BiliCookie.NULL;
		this.vestCookie = BiliCookie.NULL;
		this.miniCookies = new HashSet<BiliCookie>();
		this.miniPaths = new HashMap<BiliCookie, String>();
		this.lastAddCookieTime = System.currentTimeMillis();
	}
	
	public static CookiesMgr getInstn() {
		if(instance == null) {
			synchronized (CookiesMgr.class) {
				if(instance == null) {
					instance = new CookiesMgr();
				}
			}
		}
		return instance;
	}
	
	public boolean add(BiliCookie cookie, CookieType type) {
		boolean isOk = false;
		if(cookie == null || cookie == BiliCookie.NULL) {
			return isOk;
		}
		
		cookie.setType(type);
		if(CookieType.MAIN == type) {
			this.mainCookie = cookie;
			isOk = save(cookie, COOKIE_MAIN_PATH);
			
		} else if(CookieType.VEST == type) {
			this.vestCookie = cookie;
			isOk = save(cookie, COOKIE_VEST_PATH);
			
		} else {
			if(miniCookies.size() < MAX_NUM) {
				String cookiePath = miniPaths.get(cookie);
				if(cookiePath == null) {
					cookiePath = PathUtils.combine(COOKIE_DIR, StrUtils.concat(
							COOKIE_MINI_PREFIX, cookie.UID(), SUFFIX));
				}
				
				this.miniCookies.add(cookie);
				isOk = save(cookie, cookiePath);
			}
		}
		return isOk;
	}
	
	private boolean save(BiliCookie cookie, String cookiePath) {
		if(cookie.TYPE() == CookieType.MINI) {
			miniPaths.put(cookie, cookiePath);
		}
		
		String data = CryptoUtils.toDES(cookie.toString());
		boolean isOk = FileUtils.write(cookiePath, data, Charset.ISO, false);
		if(isOk == true) {
			lastAddCookieTime = System.currentTimeMillis();
		}
		return isOk;
	}
	
	public boolean load(CookieType type) {
		boolean isOk = false;
		if(CookieType.MAIN == type) {
			mainCookie = load(COOKIE_MAIN_PATH, type);
			isOk = (mainCookie != BiliCookie.NULL);
			
		} else if(CookieType.VEST == type) {
			vestCookie = load(COOKIE_VEST_PATH, type);
			isOk = (vestCookie != BiliCookie.NULL);
			
		} else {
			File dir = new File(COOKIE_DIR);
			String[] fileNames = dir.list();
			for(String fileName : fileNames) {
				if(fileName.contains(COOKIE_MINI_PREFIX) && miniCookies.size() < MAX_NUM) {
					String cookiePath = PathUtils.combine(dir.getPath(), fileName);
					BiliCookie miniCookie = load(cookiePath, type);
					if(BiliCookie.NULL != miniCookie) {
						miniCookies.add(miniCookie);
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	private BiliCookie load(String cookiePath, CookieType type) {
		BiliCookie cookie = BiliCookie.NULL;
		if(FileUtils.exists(cookiePath)) {
			String data = CryptoUtils.deDES(FileUtils.read(cookiePath, Charset.ISO));
			if(StrUtils.isNotEmpty(data)) {
				cookie = new BiliCookie(data);
				cookie.setType(type);
				
				if(checkLogined(cookie) == true) {
					if(cookie.TYPE() == CookieType.MINI && !miniPaths.containsKey(cookie)) {
						miniPaths.put(cookie, cookiePath);
					}
					lastAddCookieTime = System.currentTimeMillis();
					
				} else {
					cookie = BiliCookie.NULL;
					FileUtils.delete(cookiePath);
				}
			}
		}
		return cookie;
	}
	
	public boolean del(BiliCookie cookie) {
		boolean isOk = false;
		if(cookie == null || cookie == BiliCookie.NULL) {
			return isOk;
		}
		
		String cookiePath = "";
		if(CookieType.MAIN == cookie.TYPE()) {
			this.mainCookie = BiliCookie.NULL;
			cookiePath = COOKIE_MAIN_PATH;
			
		} else if(CookieType.VEST == cookie.TYPE()) {
			this.vestCookie = BiliCookie.NULL;
			cookiePath = COOKIE_VEST_PATH;
			
		} else {
			this.miniCookies.remove(cookie);
			cookiePath = miniPaths.remove(cookie);
		}
		
		return FileUtils.delete(cookiePath);
	}

	public static BiliCookie MAIN() {
		return getInstn().mainCookie;
	}

	public static BiliCookie VEST() {
		return getInstn().vestCookie;
	}
	
	public static Set<BiliCookie> MINIs() {
		Set<BiliCookie> cookies = new LinkedHashSet<BiliCookie>();
		Iterator<BiliCookie> minis = getInstn().miniCookies.iterator();
		for(int i = 0; i < MAX_NUM; i++) {
			if(minis.hasNext()) {
				cookies.add(minis.next());
			}
		}
		return cookies;
	}
	
	public static Set<BiliCookie> ALL() {
		Set<BiliCookie> cookies = new LinkedHashSet<BiliCookie>();
		if(BiliCookie.NULL != MAIN()) { cookies.add(MAIN()); }
		if(BiliCookie.NULL != VEST()) { cookies.add(VEST()); }
		cookies.addAll(MINIs());
		return cookies;
	}
	
	/**
	 * 持有cookie数
	 * @return
	 */
	public static int SIZE() {
		int size = 0;
		size += (MAIN() != BiliCookie.NULL ? 1 : 0);
		size += (VEST() != BiliCookie.NULL ? 1 : 0);
		size += MINI_SIZE();
		return size;
	}
	
	/**
	 * 持有小号的cookie数
	 * @return
	 */
	public static int MINI_SIZE() {
		return getInstn().miniCookies.size();
	}
	
	/**
	 * 获取最近一次添加cookie的时间
	 * @return
	 */
	public long getLastAddCookieTime() {
		return lastAddCookieTime;
	}
	
	/**
	 * 清除主号和马甲号的cookies
	 * @return
	 */
	public static boolean clearMainAndVestCookies() {
		boolean isOk = FileUtils.delete(COOKIE_MAIN_PATH);
		isOk &= FileUtils.delete(COOKIE_VEST_PATH);
		return isOk;
	}
	
	/**
	 * 清除所有cookies
	 * @return
	 */
	public static boolean clearAllCookies() {
		boolean isOk = FileUtils.delete(COOKIE_DIR);
		isOk &= (FileUtils.createDir(COOKIE_DIR) != null);
		return isOk;
	}
	
	/**
	 * 检查cookie是否可以登陆成功
	 *  若成功则把账号ID和昵称也更新到cookie中
	 * @param cookie
	 * @return
	 */
	public static boolean checkLogined(BiliCookie cookie) {
		return (BiliCookie.NULL != cookie && XHRSender.queryUserInfo(cookie));
	}
	
}
