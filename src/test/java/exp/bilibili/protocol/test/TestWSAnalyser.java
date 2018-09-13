package exp.bilibili.protocol.test;

import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.other.StrUtils;

public class TestWSAnalyser {

	public static void main(String[] args) {
		String hex = "0000007E0010000000000005000000007B22636D64223A2241435449564954595F4556454E54222C2264617461223A7B226B6579776F7264223A226E6577737072696E675F32303138222C2274797065223A22637261636B6572222C226C696D6974223A3330303030302C2270726F6772657373223A3238303239347D7D0000003F0010000000000005000000007B22636D64223A22505245504152494E47222C22726F756E64223A312C22726F6F6D6964223A22333930343830227D000000450010000000000005000000007B22636D64223A22524F4F4D5F53494C454E545F4F4646222C2264617461223A5B5D2C22726F6F6D6964223A22333930343830227D";
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
