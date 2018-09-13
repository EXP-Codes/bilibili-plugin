package exp.bilibili.protocol.bean.xhr;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.Gift;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;

/**
 * 
{
  "code": 0,
  "msg": "success",
  "message": "success",
  "data": {
    "list": [
      {
        "bag_id": 60039893,
        "gift_id": 1,
        "gift_name": "辣条",
        "gift_num": 1,
        "gift_type": 0,
        "expire_at": 1517328000
      },
      {
        "bag_id": 60032044,
        "gift_id": 1,
        "gift_name": "辣条",
        "gift_num": 6,
        "gift_type": 0,
        "expire_at": 1519747200
      },
      {
        "bag_id": 60044508,
        "gift_id": 1,
        "gift_name": "辣条",
        "gift_num": 2,
        "gift_type": 0,
        "expire_at": 1519833600
      }
    ],
    "time": 1517276975
  }
}
 *
 */
public class BagGift {

	private String bagId;
	
	private String giftId;
	
	private String giftName;
	
	private int giftNum;
	
	/** 有效期(<=0表示永久有效) */
	private long expire;
	
	/** 可增加亲密值 */
	private int intimacy;
	
	public BagGift(String giftId, String giftName, int giftNum) {
		this.bagId = "0";
		this.giftId = (giftId == null ? "" : giftId);
		this.giftName = (giftName == null ? "" : giftName);
		this.giftNum = (giftNum < 0  ? 0 : giftNum);
		this.expire = 0;
		this.intimacy = Gift.getIntimacy(this.giftId);
	}
	
	/**
	 * {
	        "bag_id": 60039893,
	        "gift_id": 1,
	        "gift_name": "辣条",
	        "gift_num": 1,
	        "gift_type": 0,
	        "expire_at": 1517328000
	      }
	 * @param json
	 */
	public BagGift(JSONObject json) {
		this("", "", 0);
		
		if(json != null) {
			this.bagId = JsonUtils.getStr(json, BiliCmdAtrbt.bag_id);
			this.giftId = JsonUtils.getStr(json, BiliCmdAtrbt.gift_id);
			this.giftName = JsonUtils.getStr(json, BiliCmdAtrbt.gift_name);
			this.giftNum = JsonUtils.getInt(json, BiliCmdAtrbt.gift_num, 0);
			this.expire = JsonUtils.getLong(json, BiliCmdAtrbt.expire_at, 0) * 1000;
			this.intimacy = Gift.getIntimacy(giftId);
		}
	}

	public String getBagId() {
		return bagId;
	}

	public String getGiftId() {
		return giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public int getGiftNum() {
		return giftNum;
	}
	
	public void setGiftNum(int giftNum) {
		this.giftNum = giftNum;
	}

	public long getExpire() {
		return expire;
	}

	public int getIntimacy() {
		return intimacy;
	}

	@Override
	public String toString() {
		return StrUtils.concat(getGiftName(), "x", getGiftNum());
	}
	
}
