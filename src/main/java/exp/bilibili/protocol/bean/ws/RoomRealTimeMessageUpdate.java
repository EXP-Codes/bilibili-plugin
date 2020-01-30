package exp.bilibili.protocol.bean.ws;

import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import net.sf.json.JSONObject;

/**
 * 
 * <PRE>
 * 
 	直播间实时信息更新（粉丝人数）:
	{
	  "cmd": "ROOM_REAL_TIME_MESSAGE_UPDATE",
	  "data": {
	    "roomid": 5440,
	    "fans": 1211519,
	    "red_notice": -1
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RoomRealTimeMessageUpdate extends _Msg {

	private int roomId;
	
	private int fans;
	
	public RoomRealTimeMessageUpdate(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ROOM_REAL_TIME_MESSAGE_UPDATE;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.roomId = JsonUtils.getInt(data, BiliCmdAtrbt.roomid, 0);
			this.fans = JsonUtils.getInt(data, BiliCmdAtrbt.fans, 0);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public int getFans() {
		return fans;
	}

}
