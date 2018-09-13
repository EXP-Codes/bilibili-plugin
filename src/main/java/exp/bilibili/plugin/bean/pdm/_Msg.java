package exp.bilibili.plugin.bean.pdm;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.BiliCmd;

abstract class _Msg {

	protected BiliCmd cmd;
	
	protected JSONObject json;
	
	protected _Msg(JSONObject json) {
		this.cmd = BiliCmd.UNKNOW;
		this.json = (json == null ? new JSONObject() : json);
		analyse(this.json);
	}

	protected abstract void analyse(JSONObject json);
	
	public BiliCmd getCmd() {
		return cmd;
	}

	public JSONObject getJson() {
		return json;
	}
	
}
