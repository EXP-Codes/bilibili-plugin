package exp.bilibili.protocol.test;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.protocol.xhr.Other;

/**
 * <PRE>
 * 测试提取当前B站的服务器配置信息
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestGetServerConfig {

	public static void main(String[] args) {
		CookiesMgr.getInstn().load(CookieType.MAIN);
		BiliCookie cookie = CookiesMgr.MAIN();
		String json = Other.queryServerConfig(cookie, 269706);
		System.out.println(json);
	}
}
