package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	高能礼物公告
	{
		"cmd": "SYS_GIFT",
		"msg": "茕茕茕茕孑立丶:?  在裕刺Fy的:?直播间447:?内赠送:?105:?共367个",
		"rnd": "0",
		"uid": 8277884,
		"msg_text": "茕茕茕茕孑立丶在裕刺Fy的直播间447内赠送火力票共367个"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SysGift extends _Msg {

	protected String msg;
	
	protected String msgText;
	
	private String rnd;
	
	private String uid;
	
	public SysGift(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.SYS_GIFT;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
		this.msgText = JsonUtils.getStr(json, BiliCmdAtrbt.msg_text);
		this.rnd = JsonUtils.getStr(json, BiliCmdAtrbt.rnd);
		this.uid = JsonUtils.getStr(json, BiliCmdAtrbt.uid);
	}

	public String getMsg() {
		return msg;
	}

	public String getMsgText() {
		return msgText;
	}

	public String getRnd() {
		return rnd;
	}

	public String getUid() {
		return uid;
	}

}
