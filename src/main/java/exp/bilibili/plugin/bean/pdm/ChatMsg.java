package exp.bilibili.plugin.bean.pdm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.BiliCmd;
import exp.bilibili.plugin.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;

/**
 * 
 * <PRE>
 * 
	直播间弹幕消息：
	{
	  "info": [
	    [
	      0,
	      1,
	      25,
	      16772431,
	      1513270027,
	      "1513257498",
	      0,
	      "53d7c7bc",
	      0
	    ],
	    "弹幕消息内容",
	    [
	      1650868,
	      "M-亚絲娜",
	      0,
	      1,
	      1,
	      10000,
	      1,
	      ""		// 这是后面版本突然多出来的一个元素
	    ],
	    [
	      16,
	      "高达",
	      "M斯文败类",
	      "51108",
	      16746162,
	      ""
	    ],
	    [
	      43,
	      0,
	      16746162,
	      4967
	    ],
	    [
	      "task-year",
	      "title-29-1"
	    ],
	    2,
	    2
	  ],
	  "cmd": "DANMU_MSG"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ChatMsg extends _Msg {

	private String uid;
	
	private String username;
	
	/** 头衔 (老爷+船员)*/
	private String title;
	
	/** 勋章（含等级） */
	private String medal;
	
	/** 用户等级 */
	private String level;
	
	private String msg;
	
	public ChatMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.DANMU_MSG;
	}

	@Override
	protected void analyse(JSONObject json) {
		JSONArray infos = JsonUtils.getArray(json, BiliCmdAtrbt.info);
		for(int i = 0; i < infos.size(); i++) {
			Object info = infos.get(i);
			
			if(info instanceof JSONArray) {
				JSONArray array = (JSONArray) info;
				
				// 未知信息
				if(array.size() == 9) {
					// Undo 
					
				// 发起聊天的用户信息
				} else if(array.size() == 7 || array.size() == 8) {
					this.uid = array.getString(0);
					this.username = array.getString(1);
					
				// 发起聊天的用户所佩戴的勋章信息
				} else if(array.size() == 6) {
					String lv = array.getString(0);
					String desc = array.getString(1);
					this.medal = StrUtils.concat(desc, "(", lv, ")");
					
				// 发起聊天的用户的等级信息
				} else if(array.size() == 4) {
					this.level = array.getString(0);
					
				// 发起聊天的用户所拥有的头衔（老爷/船员）
				} else if(array.size() <= 2) {
					// Undo
				}
				
			} else if(info instanceof String) {
				this.msg = (String) info;
				
			} else {
				// Undo
			}
		}
	}

	public String getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}

	public String getTitle() {
		return title;
	}

	public String getMedal() {
		return medal;
	}

	public String getLevel() {
		return level;
	}

	public String getMsg() {
		return msg;
	}

}
