package exp.bilibili.plugin.ui;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.WebBot;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.envm.Identity;
import exp.bilibili.plugin.utils.SwingUtils;
import exp.bilibili.protocol.XHRSender;
import exp.bilibili.protocol.ws.BiliWebSocketMgr;

/**
 * <PRE>
 * 主应用程序（无窗口）
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class App {

	/**
	 * 创建实例
	 * @param args main入参
	 */
	public static void createInstn(String[] args) {
		
		// 登陆所有账号
		loginAll();
		
		
		// 授权并连接到版聊直播间
		Identity.set(Identity.ADMIN);
		BiliWebSocketMgr wsMgr = new BiliWebSocketMgr();
		wsMgr.relinkLive(Config.getInstn().SIGN_ROOM_ID());
		wsMgr._start();	// 启动分区监听
		
		
		// 启动仿真机器人
		WebBot.getInstn()._start();
	}
	
	/**
	 * 登陆所有账号
	 */
	private static void loginAll() {
		if(CookiesMgr.getInstn().load(CookieType.MAIN)) {
			SwingUtils.info("已登陆主号: ".concat(CookiesMgr.MAIN().NICKNAME()));
			XHRSender.queryUserAuthorityInfo(CookiesMgr.MAIN());
		}
		
		if(CookiesMgr.getInstn().load(CookieType.MINI)) {
			for(BiliCookie mini : CookiesMgr.MINIs()) {
				SwingUtils.info("已登陆小号: ".concat(mini.NICKNAME()));
				XHRSender.queryUserAuthorityInfo(mini);
			}
		}
		
		if(CookiesMgr.getInstn().load(CookieType.VEST)) {
			SwingUtils.info("已登陆马甲号: ".concat(CookiesMgr.VEST().NICKNAME()));
			XHRSender.queryUserAuthorityInfo(CookiesMgr.VEST());
		}
	}
	
}
