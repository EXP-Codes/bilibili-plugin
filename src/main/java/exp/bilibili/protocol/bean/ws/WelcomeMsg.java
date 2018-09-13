package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	管理员/老爷进入直播间欢迎消息:
	{
	  "cmd": "WELCOME",
	  "data": {
	    "uid": 210940623,
	    "uname": "\u4e1d\u4e1d-",
	    "isadmin": 0,
	    "svip": 1		// 年费 （年费和月费只会出现一个）
	    "vip":1			// 月费（年费和月费只会出现一个）
	  },
	  "roomid": 269706
	}
	
	{
	  "cmd": "WELCOME",
	  "data": {
	    "uid": 56395161,
	    "uname": "肉圆滚滚艹",
	    "is_admin": false,
	    "svip": 1
	    "vip":1			// 月费（年费和月费只会出现一个）
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WelcomeMsg extends _Msg {

	private final static String[] VIPS = {
		"老爷", "月费老爷", "年费老爷", "房管", 
	};
	
	private String uid;
	
	private String username;
	
	private boolean isAdmin;
	
	private int vipLevel;
	
	private String vipDesc;
	
	public WelcomeMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.WELCOME;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uid = JsonUtils.getStr(data, BiliCmdAtrbt.uid);
			this.username = JsonUtils.getStr(data, BiliCmdAtrbt.uname);
			
			this.isAdmin = JsonUtils.getBool(data, BiliCmdAtrbt.is_admin, false);
			if(isAdmin == false) {
				isAdmin = (JsonUtils.getInt(data, BiliCmdAtrbt.isadmin, 0) != 0);
				if(isAdmin == true) {
					vipLevel = 3;	// 房管
				}
			} else {
				vipLevel = 3;	// 房管
			}
			
			if(isAdmin == false) {
				vipLevel = JsonUtils.getInt(data, BiliCmdAtrbt.svip, 0);
				if(vipLevel > 0) {
					vipLevel = 2;	// 年费
					
				} else {
					vipLevel = JsonUtils.getInt(data, BiliCmdAtrbt.vip, 0);
				}
			}
			this.vipDesc = VIPS[vipLevel];
		}
	}

	public String getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public String getVipDesc() {
		return vipDesc;
	}

}
