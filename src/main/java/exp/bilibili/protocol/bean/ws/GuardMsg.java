package exp.bilibili.protocol.bean.ws;

import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.verify.RegexUtils;
import net.sf.json.JSONObject;

/**
 * 
 * <PRE>
 * 
 	(全频道)登船消息
	{
	  "cmd": "GUARD_MSG",
	  "msg": "用户 :?阿斗金:? 在主播 吃不饱的小黄瓜 的直播间开通了总督"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GuardMsg extends _Msg {

	private String msg;
	
	private String liveup;
	
	private int roomId;
	
	private String url;
	
	public GuardMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.GUARD_MSG;
		this.roomId = 0;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
		this.liveup = RegexUtils.findFirst(msg, "在主播 (\\S+) 的直播间");
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.url);
	}

	public String getMsg() {
		return msg;
	}
	
	public String getLiveup() {
		return liveup;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getUrl() {
		return url;
	}
	
}
