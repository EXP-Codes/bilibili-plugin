package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	直播间小时榜排名通知消息:
	{
	  "cmd": "ROOM_RANK",
	  "data": {
	    "roomid": 280446,
	    "rank_desc": "小时榜 12",
	    "color": "#FB7299",
	    "h5_url": "https://live.bilibili.com/p/eden/rank-h5-current?anchor_uid=8192168",
	    "web_url": "https://live.bilibili.com/blackboard/room-current-rank.html",
	    "timestamp": 1529549460
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RoomRank extends _Msg {

	private int roomId;
	
	private String rankDesc;
	
	public RoomRank(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ROOM_RANK;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.roomId = JsonUtils.getInt(data, BiliCmdAtrbt.roomid, 0);
			this.rankDesc = JsonUtils.getStr(data, BiliCmdAtrbt.rank_desc);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRankDesc() {
		return rankDesc;
	}

}
