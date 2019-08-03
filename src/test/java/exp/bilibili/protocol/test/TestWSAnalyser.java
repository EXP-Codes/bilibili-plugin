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
		String hex = "0000057F0010000000000005000000007B22636D64223A224E4F544943455F4D5347222C2266756C6C223A7B22686561645F69636F6E223A22687474703A5C2F5C2F69302E6864736C622E636F6D5C2F6266735C2F6C6976655C2F613365313634353133626430333066313131336138666265313136353435343838306333656534632E77656270222C227461696C5F69636F6E223A22687474703A5C2F5C2F69302E6864736C622E636F6D5C2F6266735C2F6C6976655C2F383232646134383166646162613938366437333864623564386664343639666661393561386661312E77656270222C22686561645F69636F6E5F6661223A22687474703A5C2F5C2F69302E6864736C622E636F6D5C2F6266735C2F6C6976655C2F323863326633646436383137303339316431373363613265666430326264616263393137646632362E706E67222C227461696C5F69636F6E5F6661223A22687474703A5C2F5C2F69302E6864736C622E636F6D5C2F6266735C2F6C6976655C2F333863623261396631323039623136633066313531363262306235353365336232386439663136662E706E67222C22686561645F69636F6E5F66616E223A312C227461696C5F69636F6E5F66616E223A342C226261636B67726F756E64223A22233836373546354646222C22636F6C6F72223A22234646464646464646222C22686967686C69676874223A22234644464632464646222C2274696D65223A32307D2C2268616C66223A7B22686561645F69636F6E223A22687474703A5C2F5C2F69302E6864736C622E636F6D5C2F6266735C2F6C6976655C2F646630306265356565383863346364353335313430393061353866383730323336386239613332342E706E67222C227461696C5F69636F6E223A22222C226261636B67726F756E64223A22233932393946324646222C22636F6C6F72223A22234646464646464646222C22686967686C69676874223A22234644464632464646222C2274696D65223A31357D2C2273696465223A7B22686561645F69636F6E223A22222C226261636B67726F756E64223A22222C22636F6C6F72223A22222C22686967686C69676874223A22222C22626F72646572223A22227D2C22726F6F6D6964223A3134372C227265616C5F726F6F6D6964223A32343534312C226D73675F636F6D6D6F6E223A225C75353136385C75353333615C75356537665C75363461645C75666631615C75346533625C75363461643C255C75346636305C75373638345C75383263665C7536363936253E5C75356630305C75353432665C75346538365C75323031635C75353136385C75353333615C75346566625C75363130665C75393565385C75323031645C75666630635C75373062395C75353166625C75353337335C75353365665C75346632305C75393030315C75353233305C75373666345C75363461645C75393566345C75363262645C75353935365C7566663031222C226D73675F73656C66223A225C75353136385C75353333615C75356537665C75363461645C75666631615C75346533625C75363461643C255C75346636305C75373638345C75383263665C7536363936253E5C75356630305C75353432665C75346538365C75323031635C75353136385C75353333615C75346566625C75363130665C75393565385C75323031645C75666630635C75356665625C75363736355C75363262645C75353935365C75353432375C7566663031222C226C696E6B5F75726C223A22687474703A5C2F5C2F6C6976652E62696C6962696C692E636F6D5C2F3134373F6C6976655F6C6F74746572795F747970653D312662726F6164636173745F747970653D302666726F6D3D32383030332665787472615F6A756D705F66726F6D3D3238303033222C226D73675F74797065223A382C22736869656C645F756964223A2D317D";
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
