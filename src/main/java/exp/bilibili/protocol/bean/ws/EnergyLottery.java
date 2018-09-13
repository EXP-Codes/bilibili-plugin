package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	高能礼物抽奖：
	{
		"cmd": "SYS_GIFT",
		"msg": "00\u515c\u515c00\u5728\u76f4\u64ad\u95f45279\u706b\u529b\u5168\u5f00\uff0c\u55e8\u7ffb\u5168\u573a\uff0c\u901f\u53bb\u56f4\u89c2\uff0c\u8fd8\u80fd\u514d\u8d39\u9886\u53d6\u706b\u529b\u7968\uff01",
		"msg_text": "00\u515c\u515c00\u5728\u76f4\u64ad\u95f45279\u706b\u529b\u5168\u5f00\uff0c\u55e8\u7ffb\u5168\u573a\uff0c\u901f\u53bb\u56f4\u89c2\uff0c\u8fd8\u80fd\u514d\u8d39\u9886\u53d6\u706b\u529b\u7968\uff01",
		"tips": "00\u515c\u515c00\u5728\u76f4\u64ad\u95f45279\u706b\u529b\u5168\u5f00\uff0c\u55e8\u7ffb\u5168\u573a\uff0c\u901f\u53bb\u56f4\u89c2\uff0c\u8fd8\u80fd\u514d\u8d39\u9886\u53d6\u706b\u529b\u7968\uff01",
		"url": "http:\/\/live.bilibili.com\/5279",
		"roomid": 5279,
		"real_roomid": 5279,
		"giftId": 106,
		"msgTips": 0
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class EnergyLottery extends SysGift {

	private String tips;
	
	private String url;
	
	private int roomId;
	
	private int realRoomId;
	
	private String giftId;
	
	private String msgTips;
	
	public EnergyLottery(JSONObject json) {
		super(json);
	}

	public int ROOM_ID() {
		int id = getRealRoomId();
		return (id <= 0 ? getRoomId() : id);
	}
	
	@Override
	protected void analyse(JSONObject json) {
		super.analyse(json);
		this.tips = JsonUtils.getStr(json, BiliCmdAtrbt.tips);
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.url);
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		this.realRoomId = JsonUtils.getInt(json, BiliCmdAtrbt.real_roomid, 0);
		this.giftId = JsonUtils.getStr(json, BiliCmdAtrbt.giftId);
		this.msgTips = JsonUtils.getStr(json, BiliCmdAtrbt.msgTips);
	}
	
	public String getTips() {
		return tips;
	}

	public String getUrl() {
		return url;
	}

	public int getRoomId() {
		return roomId;
	}

	public int getRealRoomId() {
		return realRoomId;
	}

	public String getGiftId() {
		return giftId;
	}

	public String getMsgTips() {
		return msgTips;
	}

}
