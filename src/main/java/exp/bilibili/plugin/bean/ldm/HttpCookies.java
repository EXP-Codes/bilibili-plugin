package exp.bilibili.plugin.bean.ldm;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;

import exp.libs.utils.other.StrUtils;

public class HttpCookies {
	
	public final static HttpCookies NULL = new HttpCookies();
	
	private final static String LFCR = "\r\n";
	
	/** B站CSRF标识 */
	private final static String CSRF_KEY = "bili_jct";
	
	/** 该cookie对应的用户昵称 */
	private String nickName;
	
	private List<HttpCookie> cookies;
	
	/** 多个cookies组合而成的NV串 */
	private String nvCookies;
	
	/** 从cookies提取的csrf token */
	private String csrf;
	
	/** cookies是否发生变化 */
	private boolean isChanged;
	
	public HttpCookies() {
		this.nickName = "";
		this.cookies = new LinkedList<HttpCookie>();
		this.nvCookies = "";
		this.csrf = "";
		isChanged = false;
	}
	
	public HttpCookies(String headerCookies) {
		this();
		
		if(StrUtils.isNotEmpty(headerCookies)) {
			String[] lines = headerCookies.split(LFCR);
			for(int i = 0; i < lines.length; i++) {
				add(lines[i]);
			}
		}
	}
	
	public HttpCookies(Collection<Cookie> cookies) {
		this();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				add(cookie);
			}
		}
	}
	
	/**
	 * cookies是否过期
	 * @return true:已过期; false未过期
	 */
	public boolean isExpire() {
		return StrUtils.isEmpty(nickName);
	}
	
	/**
	 * cookies是否有效
	 * @return true:有效; false:无效
	 */
	public boolean isVaild() {
		return cookies.size() > 0;
	}
	
	public void add(Cookie cookie) {
		add(new HttpCookie(cookie));
	}
	
	public void add(String headerCookie) {
		add(new HttpCookie(headerCookie));
	}
	
	public void add(HttpCookie cookie) {
		if(cookie != null && cookie.isVaild()) {
			isChanged = true;
			cookies.add(cookie);
			
			if(CSRF_KEY.equals(cookie.getName())) {
				csrf = cookie.getValue();
			}
		}
	}
	
	public String CSRF() {
		return csrf;
	}
	
	public String toNVCookies() {
		if(isChanged == true) {
			
			StringBuilder kvs = new StringBuilder();
			for(HttpCookie cookie : cookies) {
				kvs.append(cookie.toNV()).append("; ");
			}
			nvCookies = kvs.toString();
		}
		return nvCookies;
	}
	
	public String toHeaderCookies() {
		StringBuilder sb = new StringBuilder();
		for(HttpCookie cookie : cookies) {
			sb.append(cookie.toString()).append(LFCR);
		}
		return sb.toString();
	}
	
	public Set<Cookie> toSeleniumCookies() {
		Set<Cookie> seleniumCookies = new HashSet<Cookie>();
		for(HttpCookie cookie : cookies) {
			seleniumCookies.add(cookie.toSeleniumCookie());
		}
		return seleniumCookies;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return toHeaderCookies();
	}
	
}
