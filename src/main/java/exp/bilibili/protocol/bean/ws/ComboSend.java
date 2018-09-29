package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	(直播间内)礼物combo连击消息:
	{
	  "cmd": "COMBO_SEND",
	  "data": {
	    "uid": 298674880,
	    "uname": "霞光舍不得臭猫呀",
	    "combo_num": 5,
	    "gift_name": "吃瓜",
	    "gift_id": 20004,
	    "action": "赠送",
	    "combo_id": "gift:combo_id:298674880:27020889:20004:1538239401.4661"
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ComboSend extends _Msg {

	private String uname;
	
	private int comboNum;
	
	private String giftId;
	
	private String comboIds;
	
	public ComboSend(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.COMBO_SEND;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uname = JsonUtils.getStr(data, BiliCmdAtrbt.uname);
			this.comboIds = JsonUtils.getStr(data, BiliCmdAtrbt.combo_id);
			this.comboNum = JsonUtils.getInt(data, BiliCmdAtrbt.combo_num, 0);
			this.giftId = JsonUtils.getStr(data, BiliCmdAtrbt.gift_id);
		}
	}

	public String getUname() {
		return uname;
	}

	public String getComboIds() {
		return comboIds;
	}

	public int getComboNum() {
		return comboNum;
	}

	public String getGiftId() {
		return giftId;
	}

}
