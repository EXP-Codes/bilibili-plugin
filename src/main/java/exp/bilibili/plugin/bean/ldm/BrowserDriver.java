package exp.bilibili.plugin.bean.ldm;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.envm.WebDriverType;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.os.CmdUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 浏览器驱动
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final public class BrowserDriver {

	private final static long DEFAULT_WAIT_ELEMENT_TIME = 5;
	
	private WebDriverType type;
	
	private WebDriver webDriver;
	
	/** 针对页面事件进行操作的行为对象 */
	private Actions action;
	
	public BrowserDriver(WebDriverType type) {
		this(type, false, DEFAULT_WAIT_ELEMENT_TIME);
	}
	
	public BrowserDriver(WebDriverType type, boolean loadImages, long waitElementSecond) {
		this.type = type;
		waitElementSecond = (waitElementSecond <= 0 ? 
				DEFAULT_WAIT_ELEMENT_TIME : waitElementSecond);
		
		initProperty();
		initWebDriver(loadImages);
		setWaitElementTime(waitElementSecond);
	}
	
	private void initProperty() {
		String property = "";
		if(WebDriverType.PHANTOMJS == type) {
			property = PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY;
			
		} else if(WebDriverType.CHROME == type) {
			property = ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
			
		} else {
			// Undo
		}
		
		if(StrUtils.isNotEmpty(property)) {
			System.setProperty(property, type.DRIVER_PATH());
		}
	}
	
	public void initWebDriver(boolean loadImages) {
		if(WebDriverType.PHANTOMJS == type) {
			this.webDriver = new PhantomJSDriver(getCapabilities(loadImages));
			
		} else if(WebDriverType.CHROME == type) {
			this.webDriver = new ChromeDriver(getCapabilities(loadImages));
			
		} else {
			this.webDriver = new HtmlUnitDriver(getCapabilities(loadImages));
		}
		this.action = new Actions(webDriver);
	}
	
	private DesiredCapabilities getCapabilities(boolean loadImages) {
		DesiredCapabilities capabilities = null;
		
		if(WebDriverType.PHANTOMJS == type) {
			capabilities = DesiredCapabilities.phantomjs();
			capabilities.setJavascriptEnabled(true);	// 执行页面js脚本
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);	// SSL证书支持
			
			final String PAGE_SETTINGS = PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX;
			capabilities.setCapability(PAGE_SETTINGS.concat("loadImages"), loadImages);		// 加载图片
			capabilities.setCapability(PAGE_SETTINGS.concat("XSSAuditingEnabled"), false);	// 跨域请求监控
			capabilities.setCapability(PAGE_SETTINGS.concat("localToRemoteUrlAccessEnabled"), false);	// 本地资源是否可以访问远程URL
			capabilities.setCapability(PAGE_SETTINGS.concat("userAgent"), Config.USER_AGENT);	// 伪装浏览器
			
			
//			final String HERDER = PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX;
//			capabilities.setCapability(HERDER.concat("Accept"), "application/json, text/javascript, */*; q=0.01");
//			capabilities.setCapability(HERDER.concat("Content-Type"), "application/x-www-form-urlencoded; charset=UTF-8");
//			capabilities.setCapability(HERDER.concat("Accept-Encoding"), "gzip, deflate, br");
//			capabilities.setCapability(HERDER.concat("Accept-Language"), "zh-CN,zh;q=0.8");
			
		} else if(WebDriverType.CHROME == type) {
			capabilities = DesiredCapabilities.chrome();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("loadImages", loadImages);
			
		} else {
			capabilities = DesiredCapabilities.htmlUnit();
			capabilities.setJavascriptEnabled(true);
		}
		return capabilities;
	}
	
	public WebDriver getDriver() {
		return webDriver;
	}
	
	public void open(String url) {
		webDriver.navigate().to(url);
	}
	
	/**
	 * 隐式等待期望的元素出现
	 * @param second 最长的等待秒数
	 */
	public void setWaitElementTime(long second) {
		webDriver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);	
	}
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		webDriver.navigate().refresh();
	}
	
	/**
	 * 关闭当前页面
	 */
	public void close() {
		try {
			webDriver.close();
		} catch(Throwable e) {}
	}
	
	/**
	 * 关闭浏览器
	 */
	public void quit() {
		try {
			webDriver.quit();	// 大部分浏览器驱动结束进程的方法
		} catch(Throwable e) {}
		
		// 以防万一, 使用系统命令杀掉驱动进程 （Chrome只能通过此方法）
		CmdUtils.kill(type.DRIVER_NAME());
	}
	
	public String getCurURL() {
		String url = "";
		try {
			url = webDriver.getCurrentUrl();
		} catch(Throwable e) {}
		return url;
	}
	
	public String getPageSource() {
		String ps = "";
		try {
			ps = webDriver.getPageSource();
		} catch(Throwable e) {}
		return ps;
	}
	
	public void clearCookies() {
		try {
			webDriver.manage().deleteAllCookies();
		} catch(Throwable e) {}
	}
	
	public boolean addCookie(Cookie cookie) {
		boolean isOk = true;
		try {
			webDriver.manage().addCookie(cookie);
		} catch(Exception e) {
			isOk = false;
		}
		return isOk;
	}
	
	public boolean addCookies(Set<Cookie> cookies) {
		boolean isOk = true;
		if(cookies == null) {
			isOk = false;
			
		} else {
			for(Cookie cookie : cookies) {
				isOk &= addCookie(cookie);
			}
		}
		return isOk;
	}
	
	public Set<Cookie> getCookies() {
		return webDriver.manage().getCookies();
	}
	
	public WebElement findElement(By by) {
		WebElement element = null;
		try {
			element = webDriver.findElement(by);
		} catch(Throwable e) {}
		return element;
	}

	public void click(WebElement button) {
		action.click(button).perform();	// 点击并提交
	}
	
	public void send(WebElement form, String msg) {
		action.sendKeys(form, msg, Keys.ENTER, Keys.NULL).perform();
	}
	
	/**
	 * 对浏览器的当前页面截图
	 * @param imgPath 图片保存路径
	 */
	public void screenshot(String imgPath) {
		webDriver.manage().window().maximize(); //浏览器窗口最大化
		File srcFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);  
        FileUtils.copyFile(srcFile, new File(imgPath));
	}
	
}
