package exp.bilibili.protocol.xhr.test;

import org.junit.Before;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;

public class _Init {

	protected BiliCookie cookie;
	
	protected int roomId;
	
	@Before
	public void before() {
		System.setProperty("os.name", "Linux");		// 声明非 win 系统，避免测试弹窗
		CookiesMgr.getInstn().load(CookieType.MAIN);
		this.cookie = CookiesMgr.MAIN();
		this.roomId = 1;
	}
	
}
