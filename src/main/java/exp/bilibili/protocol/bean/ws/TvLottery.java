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
		"msg": "\u3010\u745f\u60c5\u7684\u74f6\u5b50\u83cc\u3011:?\u5728\u76f4\u64ad\u95f4:?\u30103779462\u3011:?\u8d60\u9001 \u5c0f\u7535\u89c6\u4e00\u4e2a\uff0c\u8bf7\u524d\u5f80\u62bd\u5956",
		"msg_text": "\u3010\u745f\u60c5\u7684\u74f6\u5b50\u83cc\u3011:?\u5728\u76f4\u64ad\u95f4:?\u30103779462\u3011:?\u8d60\u9001 \u5c0f\u7535\u89c6\u4e00\u4e2a\uff0c\u8bf7\u524d\u5f80\u62bd\u5956",
		"rep": 1,
		"styleType": 2,
		"url": "http:\/\/live.bilibili.com\/3779462",
		"roomid": 3779462,
		"real_roomid": 3779462,
		"rnd": 1822599641,
		"tv_id": "31572"
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TvLottery extends SysMsg {

	private String msgText;
	
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
		this.msgText = JsonUtils.getStr(json, BiliCmdAtrbt.msg_text);
		this.styleType = JsonUtils.getStr(json, BiliCmdAtrbt.styleType);
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0);
		this.realRoomId = JsonUtils.getInt(json, BiliCmdAtrbt.real_roomid, 0);
		this.rnd = JsonUtils.getStr(json, BiliCmdAtrbt.rnd);
		this.tvId = JsonUtils.getStr(json, BiliCmdAtrbt.tv_id);
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.url);
	}

	public String getMsgText() {
		return msgText;
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
