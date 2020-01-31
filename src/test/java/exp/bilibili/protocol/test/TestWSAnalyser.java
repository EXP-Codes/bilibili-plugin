package exp.bilibili.protocol.test;

import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 测试WebSocket的接收报文
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestWSAnalyser {

	public static void main(String[] args) {
		String hex = "0000005B0010000000000005000000007B22636D64223A22504B5F424154544C455F454E5452414E4345222C2274696D657374616D70223A31353830343";
		alalyseMsg(hex);
	}
	
	private static void alalyseMsg(String hexMsg) {
		byte[] bytes = BODHUtils.toBytes(hexMsg);
		String msg = new String(bytes);
		System.out.println(StrUtils.view(msg));
		System.out.println("====");
		
		int len = 0;
		do {
			len = getLen(hexMsg);
			if(len <= 32) {
				break;
			}
			
			String subHexMsg = hexMsg.substring(32, len);
			msg = new String(BODHUtils.toBytes(subHexMsg));
			System.out.println(StrUtils.view(msg));

			hexMsg = hexMsg.substring(len);
		} while(StrUtils.isNotEmpty(hexMsg));
	}
	
	private static int getLen(String hexMsg) {
		String hexLen = hexMsg.substring(0, 8);	// 消息的前8位是本条消息长度
		long len = BODHUtils.hexToDec(hexLen);
		return (int) (len * 2);
	}
	
}
