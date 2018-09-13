package exp.bilibili.protocol.bean.ws;

import java.util.Iterator;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	特殊礼物：(房间内)节奏风暴消息
	{
	  "cmd": "SPECIAL_GIFT",
	  "roomid": 390480			// 这是此程序擅自加的
	  "data": {
	    "39": {
	      "id": 152125,
	      "time": 90,
	      "hadJoin": 0,
	      "num": 1,
	      "content": "皈依小乔~钵钵鸡~藤藤菜~啦啦啦",
	      "action": "start"
	    }
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SpecialGift extends _Msg {

	private int roomId;
	
	private String raffleId;
	
	public SpecialGift(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.SPECIAL_GIFT;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void analyse(JSONObject json) {
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
		Iterator keys = data.keys();
		if(keys.hasNext()) {
			JSONObject obj = JsonUtils.getObject(data, keys.next().toString());
			this.raffleId = JsonUtils.getStr(obj, BiliCmdAtrbt.id);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRaffleId() {
		return raffleId;
	}

}
