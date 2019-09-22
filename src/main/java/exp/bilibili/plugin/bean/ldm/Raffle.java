package exp.bilibili.plugin.bean.ldm;

import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.ObjUtils;
import exp.libs.utils.other.StrUtils;
import net.sf.json.JSONObject;

/**
 *  抽奖信息
{
  "code": 0,
  "message": "0",
  "ttl": 1,
  "data": {
    "pk": [
      
    ],
    "guard": [
      
    ],
    "gift": [
      {
        "raffleId": 427802,
        "type": "GIFT_30035",
        "from_user": {
          "uid": 0,
          "uname": "sy阿四",
          "face": "http://i2.hdslb.com/bfs/face/7b37c75d6467210cdd211a60c061b3b605156d1b.jpg"
        },
        "time_wait": 19,
        "time": 79,
        "max_time": 180,
        "status": 1,
        "sender_type": 1,
        "asset_icon": "http://s1.hdslb.com/bfs/live/28c2f3dd68170391d173ca2efd02bdabc917df26.png",
        "asset_animation_pic": "http://i0.hdslb.com/bfs/live/d7303a91bf00446b2bc53b8726844b4ad813b9ed.gif",
        "thank_text": "感谢\u003c%sy阿四%\u003e 赠送的任意门",
        "weight": 0,
        "gift_id": 30035
      }
    ]
  }
}
 *
 */
public class Raffle {

	private String raffleId;
	
	private String giftId;
	
	private String type;
	
	/** 等待抽奖时间(s) */
	private int timeWait;
	
	/** 可以抽奖时间 (millis) */
	private long dotime;
	
	public Raffle() {
		this.raffleId = "";
		this.giftId = "";
		this.type = "";
		this.timeWait = 0;
		this.dotime = System.currentTimeMillis();
	}
	
	public Raffle(JSONObject json) {
		this();
		
		if(json != null) {
			this.timeWait = JsonUtils.getInt(json, BiliCmdAtrbt.time_wait, 0);
			this.dotime = System.currentTimeMillis() + (timeWait + 5) * 1000;
			
			this.type = JsonUtils.getStr(json, BiliCmdAtrbt.type);
			if(StrUtils.isTrimEmpty(this.type)) {
				this.raffleId = JsonUtils.getStr(json, BiliCmdAtrbt.id);
				this.type = "guard";
				
			} else {
				this.raffleId = JsonUtils.getStr(json, BiliCmdAtrbt.raffleId);
				this.giftId = JsonUtils.getStr(json, BiliCmdAtrbt.gift_id);
			}
		}
	}
	
	public String getRaffleId() {
		return raffleId;
	}

	public void setRaffleId(String raffleId) {
		this.raffleId = raffleId;
	}

	public String getGiftId() {
		return giftId;
	}

	public String getType() {
		return type;
	}

	public int getTimeWait() {
		return timeWait;
	}

	public long getDotime() {
		return dotime;
	}

	@Override
	public int hashCode() {
		return raffleId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isEquals = false;
		if(obj != null) {
			Raffle other = (Raffle) obj;
			isEquals = this.raffleId.equals(other.getRaffleId());
		}
		return isEquals;
	}
	
	@Override
	public String toString() {
		return ObjUtils.toBeanInfo(this);
	}
	
}
