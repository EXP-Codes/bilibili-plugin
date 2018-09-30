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
		String hex = "000001770010000000000005000000007B22636D64223A2247554152445F4D5347222C226D7367223A22E794A8E688B7203A3FE38282E38284E38197E38391E383AFE383BC3A3F20E59CA8E4B8BBE692AD20E998BFE6A293E4BB8EE5B08FE5B0B1E5BE88E58FAFE788B120E79A84E79BB4E692ADE997B4E5BC80E9809AE4BA86E680BBE79DA3222C226D73675F6E6577223A223C25E38282E38284E38197E38391E383AFE383BC253E20E59CA8203C25E998BFE6A293E4BB8EE5B08FE5B0B1E5BE88E58FAFE788B1253E20E79A84E688BFE997B4E5BC80E9809AE4BA86E680BBE79DA3E5B9B6E8A7A6E58F91E4BA86E68ABDE5A596EFBC8CE782B9E587BBE5898DE5BE805441E79A84E688BFE997B4E58EBBE68ABDE5A596E590A7222C2275726C223A2268747470733A5C2F5C2F6C6976652E62696C6962696C692E636F6D5C2F3830333937222C22726F6F6D6964223A38303339372C226275795F74797065223A312C2262726F6164636173745F74797065223A307D";
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
