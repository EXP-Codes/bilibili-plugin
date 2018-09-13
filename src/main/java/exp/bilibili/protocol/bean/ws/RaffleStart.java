package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	(直播间内)高能抽奖开始消息
	{
	  "cmd": "RAFFLE_START",
	  "roomid": 390480,
	  "data": {
	    "raffleId": 61825,
	    "type": "openfire",
	    "from": "鼠二三三",
	    "time": 60
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RaffleStart extends _Msg {

	protected int roomId;
	
	protected String raffleId;
	
	protected String from;
	
	public RaffleStart(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.RAFFLE_START;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.raffleId = JsonUtils.getStr(data, BiliCmdAtrbt.raffleId);
			this.from = JsonUtils.getStr(data, BiliCmdAtrbt.from);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRaffleId() {
		return raffleId;
	}

	public String getFrom() {
		return from;
	}

}
