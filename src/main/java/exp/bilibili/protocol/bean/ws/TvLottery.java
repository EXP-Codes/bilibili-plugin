package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
	小电视抽奖：
	{
	  "cmd": "SYS_MSG",
	  "msg": "叶娃娃没有名字:? 送给:? PASSさん:? 1个小电视飞船，点击前往TA的房间去抽奖吧",
	  "msg_text": "叶娃娃没有名字:? 送给:? PASSさん:? 1个小电视飞船，点击前往TA的房间去抽奖吧",
	  "msg_common": "全区广播：<%叶娃娃没有名字%> 送给<% PASSさん%> 1个小电视飞船，点击前往TA的房间去抽奖吧",
	  "msg_self": "全区广播：<%叶娃娃没有名字%> 送给<% PASSさん%> 1个小电视飞船，快来抽奖吧",
	  "rep": 1,
	  "styleType": 2,
	  "url": "http:\/\/live.bilibili.com\/2265100",
	  "roomid": 2265100,
	  "real_roomid": 2265100,
	  "rnd": 0,
	  "broadcast_type": 0
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TvLottery extends SysMsg {

	private String styleType;
	
	private int roomId;
	
	private int realRoomId;
	
	private String rnd;
	
	private String tvId;
	
	private String url;
	
	public TvLottery(JSONObject json) {
		super(json);
	}

	public int ROOM_ID() {
		int id = getRealRoomId();
		return (id <= 0 ? getRoomId() : id);
	}
	
	@Override
	protected void analyse(JSONObject json) {
		super.analyse(json);
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg_common);
		this.styleType = JsonUtils.getStr(json, BiliCmdAtrbt.styleType);
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		this.realRoomId = JsonUtils.getInt(json, BiliCmdAtrbt.real_roomid, 0);
		this.rnd = JsonUtils.getStr(json, BiliCmdAtrbt.rnd);
		this.tvId = JsonUtils.getStr(json, BiliCmdAtrbt.tv_id);
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.url);
	}

	public String getStyleType() {
		return styleType;
	}

	public int getRoomId() {
		return roomId;
	}

	public int getRealRoomId() {
		return realRoomId;
	}

	public String getRnd() {
		return rnd;
	}

	public String getTvId() {
		return tvId;
	}

	public String getUrl() {
		return url;
	}

}
