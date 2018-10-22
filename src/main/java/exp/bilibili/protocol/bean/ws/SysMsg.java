package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	系统公告
	{
		"cmd": "SYS_MSG",
		"msg": "\u7cfb\u7edf\u516c\u544a\uff1a\u76f4\u64ad2017\u5e74\u5ea6\u56de\u9988\uff0c\u505a\u4efb\u52a1\u9886\u5956\u52b1\uff01",
		"rep": 1,
		"url": "https:\/\/live.bilibili.com\/pages\/1703\/2017livethx.html"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SysMsg extends _Msg {

	protected String msg;
	
	protected String rep;
	
	protected String url;
	
	public SysMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.SYS_MSG;
	}

	@Override
	protected void analyse(JSONObject json) {
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
		this.rep = JsonUtils.getStr(json, BiliCmdAtrbt.rep);
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.url);
	}
	
	public String getMsg() {
		return msg;
	}

	public String getRep() {
		return rep;
	}

	public String getUrl() {
		return url;
	}

}
