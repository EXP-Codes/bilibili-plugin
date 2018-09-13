package exp.bilibili.plugin.bean.pdm;

import exp.bilibili.plugin.envm.BiliCmd;
import exp.bilibili.plugin.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import net.sf.json.JSONObject;

/**
 * 
 * <PRE>
 * 
 	开播通知
	{
		"cmd": "LIVE",
		"roomid": "390480",
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LiveMsg extends _Msg {

	private int roomId;
	
	public LiveMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.LIVE;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
	}

	public int getRoomId() {
		return roomId;
	}

}
