package exp.bilibili.protocol.bean.ws;

import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import net.sf.json.JSONObject;

/**
 * 
 * <PRE>
 * 
 	(全频道)登船消息
	{
	  "cmd": "GUARD_MSG",
	  "msg": "乘客 :?期货大佬:? 成功购买5311231房间总督船票1张，欢迎登船！"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GuardMsg extends _Msg {

	private String msg;
	
	public GuardMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.GUARD_MSG;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
	}

	public String getMsg() {
		return msg;
	}

}
