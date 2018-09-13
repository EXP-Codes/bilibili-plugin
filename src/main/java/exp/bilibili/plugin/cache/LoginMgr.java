package exp.bilibili.plugin.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.HttpCookies;
import exp.bilibili.plugin.core.back.MsgSender;
import exp.bilibili.plugin.core.front.AppUI;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.envm.Charset;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.num.RandomUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.net.http.HttpClient;
import exp.libs.warp.net.http.HttpUtils;
import exp.libs.warp.thread.LoopThread;

/**
 * <PRE>
 * 登陆管理器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LoginMgr extends LoopThread {

	private final static Logger log = LoggerFactory.getLogger(LoginMgr.class);
	
	/** B站登陆页面 */
	private final static String LOGIN_URL = Config.getInstn().LOGIN_URL();
	
	private final static String VCCODE_URL = Config.getInstn().VCCODE_URL();
	
	public final static String IMG_DIR = Config.getInstn().IMG_DIR();
	
	private final static String VCCODE_PATH = IMG_DIR.concat("/vccode.jpg");
	
	public final static String QRIMG_NAME = "qrcode";
	
	private final static String COOKIE_DIR = Config.getInstn().COOKIE_DIR();
	
	public final static String MINI_COOKIE_PATH = "./data/cookie-mini.dat";
	
	private final static String SID = "sid";
	
	private final static String JSESSIONID = "JSESSIONID";
	
	/** B站二维码有效时间是180s, 这里设置120s, 避免边界问题 */
	private final static long UPDATE_TIME = 120000;
	
	private final static long LOOP_TIME = 1000;
	
	private final static int LOOP_LIMIT = (int) (UPDATE_TIME / LOOP_TIME);
	
	private int loopCnt;
	
	private boolean isLogined;
	
	private String loginUser;
	
	private static volatile LoginMgr instance;
	
	private LoginMgr() {
		super("登陆二维码刷新器");
		this.loopCnt = LOOP_LIMIT;
		this.isLogined = false;
		this.loginUser = "";
	}
	
	public static LoginMgr getInstn() {
		if(instance == null) {
			synchronized (LoginMgr.class) {
				if(instance == null) {
					instance = new LoginMgr();
				}
			}
		}
		return instance;
	}
	
	public String getLoginUser() {
		return loginUser;
	}
	
	@Override
	protected void _before() {
		log.info("{} 已启动", getName());
		autoLogin();	// 尝试使用上一次登陆的cookies自动登陆
	}

	@Override
	protected void _loopRun() {
		if(isLogined == true) {
			_stop();	// 若登陆成功则退出轮询
			
		} else {
			
			// 在二维码失效前更新
			if(loopCnt >= LOOP_LIMIT) {
				if(downloadQrcode()) {
					loopCnt = 0;
					AppUI.getInstn().updateQrcode();
				}
			}
			
			// 若当前页面不再是登陆页（扫码成功会跳转到主页）, 说明登陆成功
			isLogined = isSwitch();
			if(isLogined == true) {
				skipUpdradeTips();	// 跳过B站的升级教程（该教程若不屏蔽会妨碍点击抽奖）
			}
		}
		
		AppUI.getInstn().updateQrcodeTime(LOOP_LIMIT - (loopCnt++));
		_sleep(LOOP_TIME);
	}

	@Override
	protected void _after() {
		saveLoginInfo();	// 备份cookies
		log.info("{} 已停止", getName());
	}
	
	/**
	 * 尝试使用cookies自动登陆
	 */
	public boolean autoLogin() {
		UIUtils.log("正在尝试使用cookies自动登陆...");
		Browser.init(true);				// 使用加载图片的浏览器（首次登陆需要扫描二维码图片/验证码图片）
		Browser.open(LOGIN_URL);		// 打开登陆页面
		isLogined = loginByCookies();	// 先尝试cookies登陆
		if(isLogined == false) {
			clearCookies();
		}
		return isLogined;
	}
	
	public boolean clearAllCookies() {
		boolean isOk = true;
		isOk &= clearCookies();
		isOk &= clearMiniCookie();
		return isOk;
	}
	
	public boolean clearCookies() {
		boolean isOk = true;
		isOk &= FileUtils.delete(COOKIE_DIR);
		isOk &= (FileUtils.createDir(COOKIE_DIR) != null);
		return isOk;
	}
	
	public boolean clearMiniCookie() {
		return FileUtils.delete(MINI_COOKIE_PATH);
	}
	
	/**
	 * 从外存读取上次登陆成功的cookies
	 * @return
	 */
	private boolean loginByCookies() {
		if(FileUtils.isEmpty(COOKIE_DIR)) {
			return false;
		}
		Browser.clearCookies();
		Browser.recoveryCookies();
		return checkIsLogin();
	}
	
	/**
	 * 下载登陆二维码
	 * @return
	 */
	private boolean downloadQrcode() {
		boolean isOk = false;
		UIUtils.log("正在下载登陆二维码, 请打开 [哔哩哔哩手机客户端] 扫码登陆...");
		log.info("正在更新登陆二维码...");
		Browser.open(LOGIN_URL);
		WebElement img = Browser.findElement(By.xpath("//div[@class='qrcode-img'][1]/img"));
		if(img != null) {
			String imgUrl = img.getAttribute("src");
			isOk = HttpUtils.convertBase64Img(imgUrl, IMG_DIR, QRIMG_NAME);
			log.info("更新登陆二维码{}", (isOk ? "成功, 请打开 [哔哩哔哩手机客户端] 扫码登陆..." : "失败"));
		}
		return isOk;
	}
	
	/**
	 * 通过再次打开登陆页面，根据是否会发生跳转判断是否登陆成功.
	 * 	若已登陆成功,会自动跳转到首页; 否则会停留在登陆页面
	 * @return true: 登陆成功; false:登陆失败
	 */
	private boolean checkIsLogin() {
		Browser.open(LOGIN_URL);
		ThreadUtils.tSleep(LOOP_TIME);	// 等待以确认是否会发生跳转
		return isSwitch();
	}
	
	/**
	 * 检查页面是否发生了跳转
	 * @return
	 */
	private boolean isSwitch() {
		String curURL = Browser.getCurURL();
		return (StrUtils.isNotEmpty(curURL) && !curURL.startsWith(LOGIN_URL));
	}
	
	/**
	 * 切到当前直播间, 把第一次打开直播室时的升级教程提示屏蔽掉
	 */
	private void skipUpdradeTips() {
		UIUtils.log("首次登陆成功, 正在屏蔽B站拦截脚本...");
		Browser.open(AppUI.getInstn().getLiveUrl());
		By upgrade = By.className("upgrade-intro-component");
		if(Browser.existElement(upgrade)) {
			WebElement upgrapTips = Browser.findElement(upgrade);
			WebElement skipBtn = upgrapTips.findElement(By.className("skip"));
			skipBtn.click();
		}
	}
	
	/**
	 * 保存登陆信息
	 */
	public void saveLoginInfo() {
		UIUtils.log("正在保存cookies(用于下次自动登陆)");
		Browser.backupCookies();	// 保存登录成功的cookies到外存, 以备下次使用
		Browser.quit();	// 退出浏览器(此浏览器是加载图片的, 不加载图片的浏览器后面再延迟启动)
		
		loginUser = MsgSender.queryUsername(Browser.COOKIES());	// 获取当前登陆的用户名
		AppUI.getInstn().markLogin(loginUser);	// 在界面标记已登陆
	}
	
	/**
	 * 下载登陆用的验证码
	 * @return 与该验证码配套的cookies
	 */
	public String downloadVccode() {
		final String sid = StrUtils.concat(SID, "=", randomSID());
		HttpClient client = new HttpClient();
		
		// 下载验证码图片（该验证码图片需要使用一个随机sid去请求）
		Map<String, String> inHeaders = new HashMap<String, String>();
		inHeaders.put(HttpUtils.HEAD.KEY.COOKIE, sid);
		boolean isOk = client.downloadByGet(VCCODE_PATH, VCCODE_URL, inHeaders, null);
		
		// 服务端返回验证码的同时，会返回一个与之绑定的JSESSIONID
		String jsessionId = "";
		HttpMethod method = client.getHttpMethod();
		if(isOk && method != null) {
			Header outHeader = method.getResponseHeader(HttpUtils.HEAD.KEY.SET_COOKIE);
			if(outHeader != null) {
				jsessionId = RegexUtils.findFirst(outHeader.getValue(), 
						StrUtils.concat("(", JSESSIONID, "=[^;]+)"));
			}
		}
		
		// SID与JSESSIONID绑定了该二维码图片, 在登陆时需要把这个信息一起POST
		final String cookies = StrUtils.concat(sid, "; ", jsessionId);
		client.close();
		return cookies;
	}
	
	/**
	 * 生成随机SID (sid是由长度为8的由a-z0-9字符组成的字符串)
	 * @return 随机SID
	 */
	private String randomSID() {
		StringBuilder sid = new StringBuilder();
		for(int i = 0; i < 8; i++) {	// sid长度为8
			int n = RandomUtils.randomInt(36);	// a-z, 0-9
			if(n < 26) {	// a-z
				sid.append((char) (n + 'a'));
				
			} else {	// 0-9
				n = n - 26;
				sid.append((char) (n + '0'));
			}
		}
		return sid.toString();
	}
	
	/**
	 * 使用帐密+验证码的方式登录(用于登录主号, 即获取收益的账号)
	 * 	并把登录cookies同时转存到selenium浏览器
	 * @param username
	 * @param password
	 * @param vccode
	 * @param vcCookies
	 * @return
	 */
	public boolean toLogin(String username, String password, 
			String vccode, String vcCookies) {
		boolean isOk = false;
		HttpCookies httpCookies = MsgSender.toLogin(username, password, vccode, vcCookies);
		Set<Cookie> cookies = httpCookies.toSeleniumCookies();
		isOk = ListUtils.isNotEmpty(cookies);
		
		if(isOk == true) {
			Browser.open(LOGIN_URL);		// 打开登陆页面
			for(Cookie cookie : cookies) {	// 把后台返回的coookie转移到前端浏览器
				Browser.addCookie(cookie);
			}
			skipUpdradeTips();	// 跳过B站的升级教程（该教程若不屏蔽会妨碍点击抽奖）
			saveLoginInfo();	// 备份cookies
		}
		return isOk;
	}
	
	/**
	 * 使用帐密+验证码的方式登录(用于登录小号, 即用于扫描等行为的账号)
	 *  并把登录cookies同时转存到文件以备用
	 * @param username
	 * @param password
	 * @param vccode
	 * @param vcCookies
	 * @return
	 */
	public String toLoginMini(String username, String password, 
			String vccode, String vcCookies) {
		String miniCookie = "";
		HttpCookies httpCookies = MsgSender.toLogin(username, password, vccode, vcCookies);
		
		// 转存外存
		if(httpCookies.isVaild()) {
			miniCookie = httpCookies.toNVCookies();
			FileUtils.write(MINI_COOKIE_PATH, miniCookie, Charset.ISO, false);
		}
		return miniCookie;
	}
	
}
