package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	系统通知.
 	这种通知消息有几种类型，目前已知的类型是：
 	msg_type=1 : 普通通知
 	msg_type=2 : 小电视抽奖
 	msg_type=3 : 总督抽奖通知
 	msg_type=6 : 礼物通知
 	
=======================================================================================================

	{
	  "full": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/b049ac07021f3e4269d22a79ca53e6e7815af9ba.png",
	    "tail_icon": "http://i0.hdslb.com/bfs/live/822da481fdaba986d738db5d8fd469ffa95a8fa1.webp",
	    "head_icon_fa": "http://i0.hdslb.com/bfs/live/b049ac07021f3e4269d22a79ca53e6e7815af9ba.png",
	    "tail_icon_fa": "http://i0.hdslb.com/bfs/live/38cb2a9f1209b16c0f15162b0b553e3b28d9f16f.png",
	    "head_icon_fan": 1,
	    "tail_icon_fan": 4,
	    "background": "#FFE6BDFF",
	    "color": "#9D5412FF",
	    "highlight": "#FF6933FF",
	    "time": 10
	  },
	  "half": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/4db5bf9efcac5d5928b6040038831ffe85a91883.png",
	    "tail_icon": "",
	    "background": "#FFE6BDFF",
	    "color": "#9D5412FF",
	    "highlight": "#FF6933FF",
	    "time": 8
	  },
	  "side": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/fa323d24f448d670bcc3dc59996d17463860a6b3.png",
	    "background": "#F5EBDDFF",
	    "color": "#DA9F77FF",
	    "highlight": "#C67137FF",
	    "border": "#ECDDC0FF"
	  },
	  "msg_type": 1,
	  "msg_common": "英雄联盟助威S8！峡谷征战主播招募~",
	  "msg_self": "英雄联盟助威S8！峡谷征战主播招募~",
	  "real_roomid": 0,
	  "link_url": "https://www.bilibili.com/blackboard/activity-LOLxgzz.html?from=28003&extra_jump_from=28003",
	  "shield_uid": -1,
	  "cmd": "NOTICE_MSG",
	  "roomid": 0
	}
	
=======================================================================================================

	{
	  "cmd": "NOTICE_MSG",
	  "full": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/b29add66421580c3e680d784a827202e512a40a0.webp",
	    "tail_icon": "http://i0.hdslb.com/bfs/live/822da481fdaba986d738db5d8fd469ffa95a8fa1.webp",
	    "head_icon_fa": "http://i0.hdslb.com/bfs/live/49869a52d6225a3e70bbf1f4da63f199a95384b2.png",
	    "tail_icon_fa": "http://i0.hdslb.com/bfs/live/38cb2a9f1209b16c0f15162b0b553e3b28d9f16f.png",
	    "head_icon_fan": 24,
	    "tail_icon_fan": 4,
	    "background": "#66A74EFF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FDFF2FFF",
	    "time": 20
	  },
	  "half": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/ec9b374caec5bd84898f3780a10189be96b86d4e.png",
	    "tail_icon": "",
	    "background": "#85B971FF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FDFF2FFF",
	    "time": 15
	  },
	  "side": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/e41c7e12b1e08724d2ab2f369515132d30fe1ef7.png",
	    "background": "#F4FDE8FF",
	    "color": "#79B48EFF",
	    "highlight": "#388726FF",
	    "border": "#A9DA9FFF"
	  },
	  "roomid": 10910018,
	  "real_roomid": 10910018,
	  "msg_common": "全区广播：<%孤标云间%> 送给<% 叫我民民%> 1个小电视飞船，点击前往TA的房间去抽奖吧",
	  "msg_self": "全区广播：<%孤标云间%> 送给<% 叫我民民%> 1个小电视飞船，快来抽奖吧",
	  "link_url": "http://live.bilibili.com/10910018?live_lottery_type=1&broadcast_type=0&from=28003&extra_jump_from=28003",
	  "msg_type": 2,
	  "shield_uid": -1
	}
	
=======================================================================================================

	{
	  "cmd": "NOTICE_MSG",
	  "full": {
	    "head_icon": "http:\/\/i0.hdslb.com\/bfs\/live\/d63e78ade2319108390b1d6a59a81b2abe46925d.png",
	    "tail_icon": "http:\/\/i0.hdslb.com\/bfs\/live\/822da481fdaba986d738db5d8fd469ffa95a8fa1.webp",
	    "head_icon_fa": "http:\/\/i0.hdslb.com\/bfs\/live\/d63e78ade2319108390b1d6a59a81b2abe46925d.png",
	    "tail_icon_fa": "http:\/\/i0.hdslb.com\/bfs\/live\/38cb2a9f1209b16c0f15162b0b553e3b28d9f16f.png",
	    "head_icon_fan": 1,
	    "tail_icon_fan": 4,
	    "background": "#FFB03CFF",
	    "color": "#FFFFFFFF",
	    "highlight": "#B25AC1FF",
	    "time": 10
	  },
	  "half": {
	    "head_icon": "",
	    "tail_icon": "",
	    "background": "",
	    "color": "",
	    "highlight": "",
	    "time": 8
	  },
	  "side": {
	    "head_icon": "http:\/\/i0.hdslb.com\/bfs\/live\/17c5353140045345f31c7475432920df08351837.png",
	    "background": "#FFE9C8FF",
	    "color": "#EF903AFF",
	    "highlight": "#D54900FF",
	    "border": "#FFCFA4FF"
	  },
	  "roomid": 489,
	  "real_roomid": 3495920,
	  "msg_common": "<%晚安亲亲亲%> 在 <%绝不早到小吱吱%> 的房间开通了总督并触发了抽奖，点击前往TA的房间去抽奖吧",
	  "msg_self": "<%晚安亲亲亲%> 在本房间开通了总督并触发了抽奖，快来抽奖吧",
	  "link_url": "https:\/\/live.bilibili.com\/3495920?live_lottery_type=2&broadcast_type=0&from=28003&extra_jump_from=28003",
	  "msg_type": 3,
	  "shield_uid": -1
	}

=======================================================================================================

	{
	  "cmd": "NOTICE_MSG",
	  "full": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/e483b2dd2bf03e0c98542b5e1c395bc3e460ab3c.webp",
	    "tail_icon": "",
	    "head_icon_fa": "http://i0.hdslb.com/bfs/live/4407b3ab37730ede965e85e176924b1d2fbc49a0.png",
	    "tail_icon_fa": "",
	    "head_icon_fan": 12,
	    "tail_icon_fan": 0,
	    "background": "#A6714AFF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FDFF2FFF",
	    "time": 10
	  },
	  "half": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/ad30c259cbc5cefb482585421d650a0288cbe03c.png",
	    "tail_icon": "",
	    "background": "#DE9C72FF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FDFF2FFF",
	    "time": 8
	  },
	  "side": {
	    "head_icon": "",
	    "background": "",
	    "color": "",
	    "highlight": "",
	    "border": ""
	  },
	  "roomid": 1557206,
	  "real_roomid": 0,
	  "msg_common": "恭喜 <%你像风停了又起丶%> 获得大奖 <%23333x银瓜子%>, 感谢 <%shannonlxl%> 的赠送",
	  "msg_self": "恭喜 <%你像风停了又起丶%> 获得大奖 <%23333x银瓜子%>, 感谢 <%shannonlxl%> 的赠送",
	  "link_url": "",
	  "msg_type": 5,
	  "shield_uid": -1
	}
	
=======================================================================================================

	{
	  "full": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/6fb61c0b149b46571b7945ba4e7561b92929bd04.webp",
	    "tail_icon": "http://i0.hdslb.com/bfs/live/822da481fdaba986d738db5d8fd469ffa95a8fa1.webp",
	    "head_icon_fa": "http://i0.hdslb.com/bfs/live/88b6e816855fdaedb5664359d8b5a5ae7de39807.png",
	    "tail_icon_fa": "http://i0.hdslb.com/bfs/live/38cb2a9f1209b16c0f15162b0b553e3b28d9f16f.png",
	    "head_icon_fan": 24,
	    "tail_icon_fan": 4,
	    "background": "#5E769FFF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FFF77FFF",
	    "time": 10
	  },
	  "half": {
	    "head_icon": "http://i0.hdslb.com/bfs/live/4db5bf9efcac5d5928b6040038831ffe85a91883.png",
	    "tail_icon": "",
	    "background": "#8DA3C7FF",
	    "color": "#FFFFFFFF",
	    "highlight": "#FFF77FFF",
	    "time": 8
	  },
	  "msg_type": 6,
	  "roomid": 154,
	  "real_roomid": 50583,
	  "msg_common": "原味鸡OvO在七七见奈波丶的直播间154内赠送B坷垃共46个",
	  "msg_self": "原味鸡OvO在七七见奈波丶的直播间154内赠送B坷垃共46个",
	  "link_url": "http://live.bilibili.com/154?broadcast_type=-1&from=28003&extra_jump_from=28003",
	  "cmd": "NOTICE_MSG",
	  "side": {
	    "head_icon": "",
	    "background": "",
	    "color": "",
	    "highlight": "",
	    "border": ""
	  },
	  "shield_uid": -1
	}

 * </PRE>
 * @version   2018-09-29
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class NoticeMsg extends _Msg {

	protected String msg;
	
	protected int type;
	
	private int roomId;
	
	private int realRoomId;
	
	private String url;
	
	public NoticeMsg(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.SYS_MSG;
	}

	@Override
	protected void analyse(JSONObject json) {
		this.msg = JsonUtils.getStr(json, BiliCmdAtrbt.msg_common);
		this.type = JsonUtils.getInt(json, BiliCmdAtrbt.msg_type, -1);
		this.roomId = JsonUtils.getInt(json, BiliCmdAtrbt.roomid, -1);
		this.realRoomId = JsonUtils.getInt(json, BiliCmdAtrbt.real_roomid, -1);
		this.url = JsonUtils.getStr(json, BiliCmdAtrbt.link_url);
	}
	
	public String getMsg() {
		return msg;
	}

	public int getType() {
		return type;
	}

	public int getRoomId() {
		return roomId;
	}

	public int getRealRoomId() {
		return realRoomId;
	}


	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		switch(type) {
			case 1 : case 2 : case 5 : { 
				json.put(BiliCmdAtrbt.cmd, BiliCmd.SYS_MSG.CMD());
				json.put(BiliCmdAtrbt.msg, msg);
				break; 
			}
			// 与 SYS_MSG 的抽奖通知重复，不转换成抽奖消息
//			case 2 : { // 小电视抽奖
//				json.put(BiliCmdAtrbt.cmd, BiliCmd.SYS_MSG.CMD());
//				json.put(BiliCmdAtrbt.msg, msg);
//				json.put(BiliCmdAtrbt.roomid, roomId);
//				json.put(BiliCmdAtrbt.real_roomid, realRoomId);
//				json.put(BiliCmdAtrbt.url, url);
//				break; 
//			}
			case 3 : { // 舰长抽奖
				json.put(BiliCmdAtrbt.cmd, BiliCmd.GUARD_MSG.CMD());
				json.put(BiliCmdAtrbt.msg, msg);
				json.put(BiliCmdAtrbt.roomid, realRoomId);
				json.put(BiliCmdAtrbt.url, url);
				break; 
			}
			case 6 : { 
				json.put(BiliCmdAtrbt.cmd, BiliCmd.SYS_GIFT.CMD());
				json.put(BiliCmdAtrbt.msg, msg);
				json.put(BiliCmdAtrbt.msg_text, msg);
				break; 
			}
		}
		return json;
	}

}
