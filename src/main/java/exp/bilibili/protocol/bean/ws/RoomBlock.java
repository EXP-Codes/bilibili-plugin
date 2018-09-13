package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	(直播间内)关小黑屋通知消息:
	{
	  "cmd": "ROOM_BLOCK_MSG",
	  "uid": "247056833",
	  "uname": "hghi7432",
	  "roomid": 390480
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RoomBlock extends _Msg {

	private int roomId;
	
	private String uid;
	
	private String uname;
	
	public RoomBlock(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ROOM_BLOCK_MSG;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		this.uid = JsonUtils.getStr(json, BiliCmdAtrbt.uid);
		this.uname = JsonUtils.getStr(json, BiliCmdAtrbt.uname);
	}

	public int getRoomId() {
		return roomId;
	}

	public String getUid() {
		return uid;
	}

	public String getUname() {
		return uname;
	}

}
