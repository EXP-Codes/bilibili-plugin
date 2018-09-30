package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.GuardType;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	提督/总督进入直播间欢迎消息：
	{
	  "cmd": "WELCOME_GUARD",
	  "data": {
	    "uid": 1650868,
	    "username": "M-亚絲娜",
	    "guard_level": "2"
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WelcomeGuard extends _Msg {

	protected String uid;
	
	protected String username;
	
	protected GuardType guardType;
	
	public WelcomeGuard(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.WELCOME_GUARD;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uid = JsonUtils.getStr(data, BiliCmdAtrbt.uid);
			this.username = JsonUtils.getStr(data, BiliCmdAtrbt.username);
			int guardLevel = JsonUtils.getInt(data, BiliCmdAtrbt.guard_level, 0);
			this.guardType = GuardType.toGuardType(guardLevel);
		}
	}

	public String getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}

	public GuardType getGuardType() {
		return guardType;
	}

}
