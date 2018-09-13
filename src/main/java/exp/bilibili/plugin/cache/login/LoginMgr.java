package exp.bilibili.plugin.cache.login;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.HttpCookies;
import exp.bilibili.plugin.core.back.MsgSender;
import exp.bilibili.plugin.envm.LoginType;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.libs.envm.Charset;
import exp.libs.utils.encode.CryptoUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.PathUtils;
import exp.libs.utils.other.StrUtils;

public class LoginMgr {

	/** cookies保存目录 */
	private final static String COOKIE_DIR = Config.getInstn().COOKIE_DIR();
	
	/**  文件名后缀 */
	private final static String SUFFIX = ".dat";
	
	/** 主号cookie文件路径 */
	private final static String COOKIE_MAIN_PATH = PathUtils.combine(COOKIE_DIR, 
			StrUtils.concat("cookie-main", SUFFIX));
	
	/** 马甲号cookie文件路径 */
	private final static String COOKIE_VEST_PATH = PathUtils.combine(COOKIE_DIR, 
			StrUtils.concat("cookie-vest", SUFFIX));
	
	/** 临时cookie文件路径 */
	private final static String COOKIE_TEMP_PATH = PathUtils.combine(COOKIE_DIR, 
			StrUtils.concat("cookie-temp", SUFFIX));
	
	/** 小号cookie文件名前缀 */
	private final static String COOKIE_MINI_PREFIX = "cookie-mini-";
	
	/** 主号cookie */
	private HttpCookies mainCookies;
	
	/** 马甲号cookie */
	private HttpCookies vestCookies;
	
	/** 临时cookie */
	private HttpCookies tempCookies;
	
	/** 小号cookie集 */
	private List<HttpCookies> miniCookies;
	
	private static volatile LoginMgr instance;
	
	private LoginMgr() {
		this.mainCookies = HttpCookies.NULL;
		this.vestCookies = HttpCookies.NULL;
		this.tempCookies = HttpCookies.NULL;
		this.miniCookies = new LinkedList<HttpCookies>();
	}
	
	public static LoginMgr INSTN() {
		if(instance == null) {
			synchronized (LoginMgr.class) {
				if(instance == null) {
					instance = new LoginMgr();
				}
			}
		}
		return instance;
	}
	
	public boolean add(HttpCookies cookies, LoginType type) {
		boolean isOk = false;
		if(cookies == null || cookies == HttpCookies.NULL || !cookies.isVaild()) {
			return isOk;
		}
		
		if(LoginType.MAIN == type) {
			this.mainCookies = cookies;
			isOk = save(cookies, COOKIE_MAIN_PATH);
			
		} else if(LoginType.VEST == type) {
			this.vestCookies = cookies;
			isOk = save(cookies, COOKIE_VEST_PATH);
			
		} else if(LoginType.TEMP == type) {
			this.tempCookies = cookies;
			isOk = save(cookies, COOKIE_TEMP_PATH);
			
		} else {
			this.miniCookies.add(cookies);
			isOk = save(cookies, PathUtils.combine(COOKIE_DIR, StrUtils.concat(
					COOKIE_MINI_PREFIX, TimeUtils.getSysDate("yyyyMMddHHmmSS"), SUFFIX)));
		}
		return isOk;
	}
	
	private boolean save(HttpCookies cookies, String cookiePath) {
		String data = CryptoUtils.toDES(cookies.toString());
		return FileUtils.write(cookiePath, data, Charset.ISO, false);
	}
	
	public boolean load(LoginType type) {
		boolean isOk = true;
		if(LoginType.MAIN == type) {
			mainCookies = load(COOKIE_MAIN_PATH);
			isOk = (mainCookies != HttpCookies.NULL);
			
		} else if(LoginType.VEST == type) {
			vestCookies = load(COOKIE_VEST_PATH);
			isOk = (vestCookies != HttpCookies.NULL);

		} else if(LoginType.TEMP == type) {
			tempCookies = load(COOKIE_TEMP_PATH);
			isOk = (tempCookies != HttpCookies.NULL);
			
		} else {
			File dir = new File(COOKIE_DIR);
			String[] fileNames = dir.list();
			for(String fileName : fileNames) {
				if(fileName.contains(COOKIE_MINI_PREFIX)) {
					HttpCookies miniCookie = load(PathUtils.combine(dir.getPath(), fileName));
					if(miniCookie != HttpCookies.NULL) {
						miniCookies.add(miniCookie);
						isOk &= true;
						
					} else {
						isOk &= false;
					}
				}
			}
		}
		return isOk;
	}
	
	private HttpCookies load(String cookiePath) {
		HttpCookies cookies = HttpCookies.NULL;
		if(FileUtils.exists(cookiePath)) {
			String data = CryptoUtils.deDES(FileUtils.read(cookiePath, Charset.ISO));
			if(StrUtils.isNotEmpty(data)) {
				cookies = new HttpCookies(data);
			}
		}
		return cookies;
	}

	public HttpCookies getMainCookies() {
		return mainCookies;
	}

	public HttpCookies getVestCookies() {
		return vestCookies;
	}
	
	public HttpCookies getTempCookies() {
		return tempCookies;
	}

	public Iterator<HttpCookies> getMiniCookies() {
		return miniCookies.iterator();
	}
	
	/**
	 * 检查cookie是否可以登陆成功
	 *  若成功则把昵称也更新到cookie中
	 * @param cookies
	 * @return
	 */
	protected static boolean checkLogined(HttpCookies cookies) {
		String nickName = MsgSender.queryUsername(cookies.toNVCookies());
		cookies.setNickName(nickName);
		return !cookies.isExpire();
	}
	
}
