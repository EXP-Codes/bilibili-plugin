package exp.bilibili.plugin.bean.ldm;

import java.util.Date;

import org.openqa.selenium.Cookie;

import exp.bilibili.plugin.utils.TimeUtils;
import exp.libs.utils.other.StrUtils;

final class HttpCookie {

	private final static String DOMAIN = "Domain";
	
	private final static String PATH = "Path";
	
	private final static String EXPIRE = "Expires";
	
	private final static String SECURE = "Secure";
	
	private final static String HTTPONLY = "HttpOnly";

	private String name;
	
	private String value;
	
	private String domain;
	
	private String path;
	
	private Date expiry;
	
	private boolean isSecure;
	
	private boolean isHttpOnly;
	
	protected HttpCookie() {
		this.name = "";
		this.value = "";
		this.domain = "";
		this.path = "";
		this.expiry = new Date();
		this.isSecure = false;
		this.isHttpOnly = false;
	}
	
	protected HttpCookie(Cookie cookie) {
		this();
		init(cookie);
	}
	
	private void init(Cookie cookie) {
		if(cookie == null) {
			return;
		}
		
		this.name = cookie.getName();
		this.value = cookie.getValue();
		this.domain = cookie.getDomain();
		this.path = cookie.getPath();
		this.expiry = (cookie.getExpiry() == null ? new Date() : cookie.getExpiry());
		this.isSecure = cookie.isSecure();
		this.isHttpOnly = cookie.isHttpOnly();
	}
	
	/**
	 * 
	 * @param headerCookie HTTP响应头中的 Set-Cookie
	 */
	protected HttpCookie(String headerCookie) {
		this();
		init(headerCookie);
	}
	
	private void init(String headerCookie) {
		String[] vals = headerCookie.split(";");
		for(int i = 0; i < vals.length; i++) {
			String[] kv = vals[i].split("=");
			if(kv.length == 2) {
				String key = kv[0].trim();
				String val = kv[1].trim();
				
				if(i == 0) {
					this.name = key;
					this.value = val;
					
				} else {
					if("Domain".equalsIgnoreCase(key)) {
						this.domain = val;
						
					} else if("Path".equalsIgnoreCase(key)) {
						this.path = val;
						
					} else if("Expires".equalsIgnoreCase(key)) {
						this.expiry = TimeUtils.toDate(val);
					}
				}
				
			} else if(kv.length == 1){
				String key = kv[0].trim();
				if("Secure".equalsIgnoreCase(key)) {
					this.isSecure = true;
					
				} else if("HttpOnly".equalsIgnoreCase(key)) {
					this.isHttpOnly = true;
				}
			}
		}
	}
	
	protected boolean isVaild() {
		return StrUtils.isNotEmpty(name);
	}
	
	protected String toNV() {
		return (!isVaild() ? "" : StrUtils.concat(name, "=", value));
	}

	protected String toHeaderCookie() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=").append(value).append(" ; ");
		sb.append(DOMAIN).append("=").append(domain).append(" ; ");
		sb.append(PATH).append("=").append(path).append(" ; ");
		sb.append(EXPIRE).append("=").append(TimeUtils.toExpires(expiry)).append(" ; ");
		if(isSecure == true) { sb.append(SECURE).append(" ; "); }
		if(isHttpOnly == true) { sb.append(HTTPONLY).append(" ; "); }
		return sb.toString();
	}
	
	protected Cookie toSeleniumCookie() {
		return new Cookie(name, value, domain, path, expiry, isSecure, isHttpOnly);
	}
	
	protected String getName() {
		return name;
	}

	protected String getValue() {
		return value;
	}

	protected String getDomain() {
		return domain;
	}

	protected String getPath() {
		return path;
	}

	protected Date getExpiry() {
		return expiry;
	}

	protected boolean isSecure() {
		return isSecure;
	}

	protected boolean isHttpOnly() {
		return isHttpOnly;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	protected void setDomain(String domain) {
		this.domain = domain;
	}

	protected void setPath(String path) {
		this.path = path;
	}

	protected void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	protected void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	protected void setHttpOnly(boolean isHttpOnly) {
		this.isHttpOnly = isHttpOnly;
	}
	
	@Override
	public String toString() {
		return toHeaderCookie();
	}
	
}
