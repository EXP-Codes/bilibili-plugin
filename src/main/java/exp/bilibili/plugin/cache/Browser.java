package exp.bilibili.plugin.cache;

import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BrowserDriver;
import exp.bilibili.plugin.bean.ldm.HttpCookies;
import exp.bilibili.plugin.cache.login.LoginMgr;
import exp.bilibili.plugin.envm.LoginType;
import exp.bilibili.plugin.envm.WebDriverType;

/**
 * <PRE>
 * 浏览器驱动管理器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Browser {
	
	private final static int WAIT_ELEMENT_TIME = Config.getInstn().WAIT_ELEMENT_TIME();
	
	private BrowserDriver browser;
	
	private HttpCookies cookies;
	
	private static volatile Browser instance;
	
	private Browser() {
		this.cookies = new HttpCookies();
	}
	
	private static Browser INSTN() {
		if(instance == null) {
			synchronized (Browser.class) {
				if(instance == null) {
					instance = new Browser();
				}
			}
		}
		return instance;
	}
	
	public static String COOKIES() {
		return INSTN()._COOKIES();
	}
	
	private String _COOKIES() {
		return cookies.toNVCookies();
	}
	
	public static String CSRF() {
		return INSTN()._CSRF();
	}
	
	private String _CSRF() {
		return cookies.CSRF();
	}
	
	public static void init(boolean loadImages) {
		INSTN()._reset(loadImages);
	}
	
	public static void reset(boolean loadImages) {
		INSTN()._reset(loadImages);
	}
	
	/**
	 * 重置浏览器驱动
	 * @param loadImages
	 * @return
	 */
	private void _reset(boolean loadImages) {
		backupCookies();
		quit();
		browser = new BrowserDriver(WebDriverType.PHANTOMJS, 
				loadImages, WAIT_ELEMENT_TIME);
		recoveryCookies();
	}
	
	public static void open(String url) {
		INSTN()._open(url);
	}
	
	private void _open(String url) {
		if(browser == null){
			_reset(false);
		}
		browser.open(url);
	}
	
	public static void refresh() {
		INSTN()._refresh();
	}
	
	private void _refresh() {
		if(browser != null){
			browser.refresh();
		}
	}
	
//	public static void close() {
//		INSTN()._close();
//	}
//	
//	/**
//	 * 关闭当前页面(若是最后一个页面, 则会关闭浏览器)
//	 */
//	private void _close() {
//		if(browser != null) {
//			browser.close();
//		}
//	}
	
	public static void quit() {
		INSTN()._quit();
	}
	
	/**
	 * 退出浏览器
	 */
	private void _quit() {
		if(browser != null) {
			browser.quit();
			browser = null;
		}
	}
	
	public static String getCurURL() {
		return INSTN()._getCurURL();
	}
	
	private String _getCurURL() {
		return (browser == null ? "" : browser.getCurURL());
	}
	
	public static String getPageSource() {
		return INSTN()._getPageSource();
	}
	
	private String _getPageSource() {
		return (browser == null ? "" : browser.getPageSource());
	}
	
	public static void clearCookies() {
		INSTN()._clearCookies();
	}
	
	private void _clearCookies() {
		if(browser != null) {
			browser.clearCookies();
		}
	}
	
	public static boolean addCookie(Cookie cookie) {
		return INSTN()._addCookie(cookie);
	}
	
	private boolean _addCookie(Cookie cookie) {
		boolean isOk = false;
		if(browser != null && cookie != null) {
			browser.addCookie(cookie);
			cookies.add(cookie);
			isOk = true;
		}
		return isOk;
	}
	
	public static Set<Cookie> getCookies() {
		return INSTN()._getCookies();
	}
	
	private Set<Cookie> _getCookies() {
		return (browser == null ? new HashSet<Cookie>() : browser.getCookies());
	}
	
	public static void backupCookies() {
		INSTN()._backupCookies();
	}
	
	private void _backupCookies() {
		if(browser != null) {
			LoginMgr.INSTN().add(cookies, LoginType.TEMP);
		}
	}
	
	public static int recoveryCookies() {
		return INSTN()._recoveryCookies();
	}
	
	private int _recoveryCookies() {
		int cnt = 0;
		if(browser != null) {
//			LoginMgr.INSTN().load(LoginType.TEMP);
			cookies = LoginMgr.INSTN().getTempCookies();
			browser.addCookies(cookies.toSeleniumCookies());
		}
		return cnt;
	}
	
	public static boolean existElement(By by) {
		return INSTN()._existElement(by);
	}
	
	private boolean _existElement(By by) {
		return (_findElement(by) != null);
	}
	
	public static WebElement findElement(By by) {
		return INSTN()._findElement(by);
	}
	
	private WebElement _findElement(By by) {
		return (browser == null ? null : browser.findElement(by));
	}
	
	public static void click(WebElement element) {
		INSTN()._click(element);
	}
	
	private void _click(WebElement element) {
		if(browser != null) {
			browser.click(element);
		}
	}
	
	/**
	 * 使浏览器跳转到指定页面后截图
	 * @param driver 浏览器驱动
	 * @param url 跳转页面
	 * @param imgPath 图片保存路径
	 */
	public static void screenshot(String imgPath) {
		INSTN()._screenshot(imgPath);
	}
	
	/**
	 * 对浏览器的当前页面截图
	 * @param imgPath 图片保存路径
	 */
	private void _screenshot(String imgPath) {
		if(browser != null) {
			browser.screenshot(imgPath);
		}
	}

}
