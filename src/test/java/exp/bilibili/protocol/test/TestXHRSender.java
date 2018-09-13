package exp.bilibili.protocol.test;

import java.util.Set;

import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.protocol.XHRSender;
import exp.libs.utils.os.ThreadUtils;

/**
 * <PRE>
 * 测试XHR请求
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestXHRSender {

	public static void main(String[] args) {
		CookiesMgr.getInstn().load(CookieType.MAIN);
		CookiesMgr.getInstn().load(CookieType.VEST);
		CookiesMgr.getInstn().load(CookieType.MINI);
		
		testGetGuardGift();
	}
	
	/**
	 * 测试补领所有房间的总督奖励
	 */
	private static void testGetGuardGift() {
		Set<Integer> roomIds = RoomMgr.getInstn().getRealRoomIds();
		for(Integer roomId : roomIds) {
			System.out.println(roomId);
			XHRSender.getGuardGift(roomId);
			ThreadUtils.tSleep(10);
		}
	}
	
}
