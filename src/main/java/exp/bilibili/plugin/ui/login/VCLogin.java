package exp.bilibili.plugin.ui.login;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.protocol.XHRSender;

/**
 * <PRE>
 * 帐密登陆.
 *  可用于登陆主号、小号、马甲号
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class VCLogin {

	protected VCLogin() {}
	
	/**
	 * 下载登陆用的验证码图片
	 * @param imgPath 图片保存路径
	 * @return 与该验证码配套的cookies
	 */
	protected static String downloadVccode(String imgPath) {
		return XHRSender.downloadVccode(imgPath);
	}
	
	/**
	 * 使用帐密+验证码的方式登录
	 * @param username 账号
	 * @param password 密码
	 * @param vccode 验证码
	 * @param vcCookies 与验证码配套的cookies
	 * @return
	 */
	protected static BiliCookie toLogin(String username, String password, 
			String vccode, String vcCookies) {
		BiliCookie cookie = XHRSender.toLogin(username, password, vccode, vcCookies);
		if(cookie != BiliCookie.NULL) {
			XHRSender.queryUserInfo(cookie);
		}
		return cookie;
	}
	
}
