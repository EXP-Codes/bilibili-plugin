package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	提督/总督/舰长进入直播间特效：
	{
	  "cmd": "ENTRY_EFFECT",
	  "data": {
	    "id": 4,
	    "uid": 6442532,
	    "target_id": 20440696,
	    "show_avatar": 1,
	    "copy_writing": "欢迎舰长 <%李猫咪%> 进入直播间",
	    "highlight_color": "#E6FF00",
	    "basemap_url": "http:\\/\\/i0.hdslb.com\\/bfs\\/live\\/1fa3cc06258e16c0ac4c209e2645fda3c2791894.png",
	    "effective_time": 2,
	    "priority": 70,
	    "privilege_type": 3,
	    "face": "http:\\/\\/i0.hdslb.com\\/bfs\\/face\\/2760e2af67538fa2f243a111fa64eac74227635e.jpg"
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class EntryEffect extends _Msg {

	protected final static String[] GUARD = {
		"平民", "总督", "提督", "舰长"
	};
	
	protected String uid;
	
	protected String msg;
	
	protected int guardLevel;
	
	protected String guardDesc;
	
	public EntryEffect(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ENTRY_EFFECT;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uid = JsonUtils.getStr(data, BiliCmdAtrbt.uid);
			this.msg = JsonUtils.getStr(data, BiliCmdAtrbt.copy_writing);
			this.guardLevel = JsonUtils.getInt(data, BiliCmdAtrbt.guard_level, 0);
			guardLevel = (guardLevel >= GUARD.length ? 0 : guardLevel);
			this.guardDesc = GUARD[guardLevel];
		}
	}

	public String getUid() {
		return uid;
	}

	public String getMsg() {
		return msg;
	}

	public int getGuardLevel() {
		return guardLevel;
	}

	public String getGuardDesc() {
		return guardDesc;
	}

}
