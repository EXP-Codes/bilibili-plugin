package exp.bilibili.plugin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.WebBot;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.envm.Identity;
import exp.bilibili.protocol.XHRSender;
import exp.bilibili.protocol.ws.BiliWebSocketMgr;

/**
 * <PRE>
 * 主应用程序（无窗口）
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2019-08-04
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class App {

	private final static Logger log = LoggerFactory.getLogger(App.class);
	
	/**
	 * 创建实例
	 * @param args main入参
	 */
	public static void createInstn(String[] args) {
		
		// 登陆所有账号
		loadAllCookies();
		
		
		// 授权并连接到版聊直播间
		Identity.set(Identity.ADMIN);
		final BiliWebSocketMgr wsMgr = new BiliWebSocketMgr();
		wsMgr.relinkLive(Config.getInstn().SIGN_ROOM_ID());
		wsMgr._start();	// 启动分区监听
		
		
		// 启动仿真机器人
		WebBot.getInstn()._start();
		
		
		// 释放资源
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				WebBot.getInstn()._stop();
				wsMgr._stop();
			}
		});
	}
	
	/**
	 * 加载所有账号的 cookies
	 */
	private static void loadAllCookies() {
		if(CookiesMgr.getInstn().load(CookieType.MAIN)) {
			log.info("已登陆主号: {}", CookiesMgr.MAIN().NICKNAME());
			XHRSender.queryUserAuthorityInfo(CookiesMgr.MAIN());
		}
		
		if(CookiesMgr.getInstn().load(CookieType.MINI)) {
			for(BiliCookie mini : CookiesMgr.MINIs()) {
				log.info("已登陆小号: {}", mini.NICKNAME());
				XHRSender.queryUserAuthorityInfo(mini);
			}
		}
		
		if(CookiesMgr.getInstn().load(CookieType.VEST)) {
			log.info("已登陆马甲号: {}", CookiesMgr.VEST().NICKNAME());
			XHRSender.queryUserAuthorityInfo(CookiesMgr.VEST());
		}
	}
	
	
}
