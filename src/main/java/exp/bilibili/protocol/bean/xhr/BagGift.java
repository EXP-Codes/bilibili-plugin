package exp.bilibili.protocol.bean.xhr;

import net.sf.json.JSONObject;
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

	protected String bagId;
	
	protected String giftId;
	
	protected String giftName;
	
	protected int giftNum;
	
	public BagGift(String giftId, String giftName, int giftNum) {
		this.bagId = "0";
		this.giftId = (giftId == null ? "" : giftId);
		this.giftName = (giftName == null ? "" : giftName);
		this.giftNum = (giftNum < 0  ? 0 : giftNum);
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
	
	@Override
	public String toString() {
		return StrUtils.concat(getGiftName(), "x", getGiftNum());
	}
	
}
