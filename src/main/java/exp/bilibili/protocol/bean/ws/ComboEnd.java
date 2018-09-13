package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	(直播间内)礼物combo连击结束消息:
	{
	  "cmd": "COMBO_END",
	  "data": {
	    "uname": "东方晟",
	    "r_uname": "叶落莫言",
	    "combo_num": 4,
	    "price": 233,
	    "gift_name": "233",
	    "gift_id": 8,
	    "start_time": 1529550034,
	    "end_time": 1529550035
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ComboEnd extends _Msg {

	private String uname;
	
	private String upName;
	
	private int comboNum;
	
	private String giftId;
	
	public ComboEnd(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ROOM_BLOCK_MSG;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uname = JsonUtils.getStr(data, BiliCmdAtrbt.uname);
			this.upName = JsonUtils.getStr(data, BiliCmdAtrbt.r_uname);
			this.comboNum = JsonUtils.getInt(data, BiliCmdAtrbt.combo_num, 0);
			this.giftId = JsonUtils.getStr(data, BiliCmdAtrbt.gift_id);
		}
	}

	public String getUname() {
		return uname;
	}

	public String getUpName() {
		return upName;
	}

	public int getComboNum() {
		return comboNum;
	}

	public String getGiftId() {
		return giftId;
	}

}
