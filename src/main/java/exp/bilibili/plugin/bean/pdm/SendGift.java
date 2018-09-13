package exp.bilibili.plugin.bean.pdm;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.BiliCmd;
import exp.bilibili.plugin.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	直播间投喂消息：
	{
	  "cmd": "SEND_GIFT",
	  "data": {
	    "giftName": "\u4ebf\u5706",
	    "num": 1,
	    "uname": "\u5403\u571f\u5c11\u5973\u59cc",
	    "rcost": 2509036,
	    "uid": 94601280,
	    "timestamp": 1513155211,
	    "giftId": 6,
	    "giftType": 0,
	    "action": "\u8d60\u9001",
	    "super": 0,
	    "price": 1000,
	    "rnd": "1513155151",
	    "newMedal": 0,
	    "newTitle": 0,
	    "medal": [
	      
	    ],
	    "title": "",
	    "beatId": "0",
	    "biz_source": "live",
	    "metadata": "",
	    "remain": 0,
	    "gold": 0,
	    "silver": 1410,
	    "eventScore": 0,
	    "eventNum": 0,
	    "smalltv_msg": [
	      
	    ],
	    "specialGift": null,
	    "notice_msg": [
	      
	    ],
	    "capsule": {
	      "normal": {
	        "coin": 6,
	        "change": 0,
	        "progress": {
	          "now": 8972,
	          "max": 10000
	        }
	      },
	      "colorful": {
	        "coin": 0,
	        "change": 0,
	        "progress": {
	          "now": 1000,
	          "max": 5000
	        }
	      }
	    },
	    "addFollow": 0,
	    "top_list": [
	      {
	        "uid": 82514842,
	        "uname": "\u6e21\u52ab\u4e2d\u7684\u4e0a\u5343\u4e07\u4e2a\u57fa\u4f6c",
	        "face": "http://i2.hdslb.com/bfs/face/82fd93862c31e746d79b2cf62098ec809f6bfa78.jpg",
	        "rank": 1,
	        "score": 1001999,
	        "guard_level": 3,
	        "isSelf": 0
	      },
	      {
	        "uid": 1650868,
	        "uname": "M-\u4e9a\u7d72\u5a1c",
	        "face": "http://i2.hdslb.com/bfs/face/bbfd1b5cafe4719e3a57154ac1ff16a9e4d9c6b3.jpg",
	        "rank": 2,
	        "score": 740600,
	        "guard_level": 2,
	        "isSelf": 0
	      },
	      {
	        "uid": 26045412,
	        "uname": "\u8d85\u7ba1\u4e3d\u6bd4\u4e3d",
	        "face": "http://i1.hdslb.com/bfs/face/e58c3b9ce474c4bc00962351ac9cff108aef39c0.jpg",
	        "rank": 3,
	        "score": 568200,
	        "guard_level": 0,
	        "isSelf": 0
	      }
	    ]
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SendGift extends _Msg {

	private String uid;
	
	private String uname;
	
	private String timestamp;
	
	private String action;
	
	private String giftName;
	
	private int num;
	
	public SendGift(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.SEND_GIFT;
	}

	@Override
	protected void analyse(JSONObject json) {
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			this.uid = JsonUtils.getStr(data, BiliCmdAtrbt.uid);
			this.uname = JsonUtils.getStr(data, BiliCmdAtrbt.uname);
			this.timestamp = JsonUtils.getStr(data, BiliCmdAtrbt.timestamp);
			this.action = JsonUtils.getStr(data, BiliCmdAtrbt.action);
			this.giftName = JsonUtils.getStr(data, BiliCmdAtrbt.giftName);
			this.num = JsonUtils.getInt(data, BiliCmdAtrbt.num, 0);
		}
	}
	
	public String getUid() {
		return uid;
	}

	public String getUname() {
		return uname;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getAction() {
		return action;
	}

	public String getGiftName() {
		return giftName;
	}

	public int getNum() {
		return num;
	}
	
}
