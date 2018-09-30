package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	(直播间内)新船员上船抽奖消息
	{
	  "cmd": "GUARD_LOTTERY_START",
	  "data": {
	    "id": 473918,
	    "roomid": 280446,
	    "message": "睡不够的茜茜茜 在【280446】购买了舰长，请前往抽奖",
	    "type": "guard",
	    "privilege_type": 3,
	    "link": "https:\\/\\/live.bilibili.com\\/280446",
	    "payflow_id": "web_3e76de8043394a0908_201809",
	    "lottery": {
	      "id": 473918,
	      "sender": {
	        "uid": 9377231,
	        "uname": "睡不够的茜茜茜",
	        "face": "http:\\/\\/i2.hdslb.com\\/bfs\\/face\\/2d0a4ea2b7258e14289b8e8022fa8e7958e605c8.jpg"
	      },
	      "keyword": "guard",
	      "time": 1200,
	      "status": 1,
	      "mobile_display_mode": 2,
	      "mobile_static_asset": "",
	      "mobile_animation_asset": ""
	    }
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GuardLotteryStart extends _Msg {

	private String msg;
	
	private int roomId;
	
	public GuardLotteryStart(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.GUARD_LOTTERY_START;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.msg = JsonUtils.getStr(data, BiliCmdAtrbt.message);
			this.roomId = JsonUtils.getInt(data, BiliCmdAtrbt.roomid, 0);
		}
	}

	public String getMsg() {
		return msg;
	}

	public int getRoomId() {
		return roomId;
	}
	
}