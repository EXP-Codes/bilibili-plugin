package exp.bilibili.protocol.bean.xhr;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.ObjUtils;

/**
 * 勋章信息
{
  "code": 0,
  "msg": "获取成功",
  "data": {
    "medalCount": 20,
    "count": 12,
    "fansMedalList": [
      {
        "id": "571612",
        "uid": 1650868,
        "target_id": 14266048,
        "medal_id": 204,
        "score": 210,
        "level": 2,
        "intimacy": 9,
        "next_intimacy": 300,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-05-03 18:49:23",
        "mtime": "2017-07-30 17:42:20",
        "ctime": "2017-05-03 18:49:23",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "小可梨",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": ">10万",
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "小可梨",
        "roomid": "46716",
        "anchorInfo": {
          "uid": 14266048,
          "uname": "语梨",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 6,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "105454",
        "uid": 1650868,
        "target_id": 13173681,
        "medal_id": 470,
        "score": 129642,
        "level": 16,
        "intimacy": 29741,
        "next_intimacy": 50000,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2016-05-19 18:55:56",
        "mtime": "2018-03-08 00:06:41",
        "ctime": "2016-05-19 18:55:56",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "高达",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 1,
        "medal_color": 16746162,
        "today_feed": "49",
        "day_limit": 2000,
        "todayFeed": "49",
        "dayLimit": 2000,
        "medalName": "高达",
        "roomid": "51108",
        "anchorInfo": {
          "uid": 13173681,
          "uname": "M斯文败类",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 4,
          "official_verify": {
            "type": -1,
            "desc": ""
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "1096726",
        "uid": 1650868,
        "target_id": 803870,
        "medal_id": 1918,
        "score": 109,
        "level": 1,
        "intimacy": 109,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2018-01-06 22:41:04",
        "mtime": "2018-01-06 22:41:06",
        "ctime": "2018-01-06 22:41:04",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "狐宝",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": ">10万",
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "狐宝",
        "roomid": "70270",
        "anchorInfo": {
          "uid": 803870,
          "uname": "爱吃橘子の狐妖",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 5,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "571614",
        "uid": 1650868,
        "target_id": 116683,
        "medal_id": 2361,
        "score": 30,
        "level": 1,
        "intimacy": 30,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-05-03 18:50:26",
        "mtime": "2017-06-30 05:04:00",
        "ctime": "2017-05-03 18:50:26",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "猫酱",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 5170,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "猫酱",
        "roomid": "5294",
        "anchorInfo": {
          "uid": 116683,
          "uname": "=咬人猫=",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 6,
          "official_verify": {
            "type": 0,
            "desc": "bilibili 知名舞见"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "572148",
        "uid": 1650868,
        "target_id": 733055,
        "medal_id": 3239,
        "score": 1492,
        "level": 4,
        "intimacy": 491,
        "next_intimacy": 700,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-05-04 00:15:29",
        "mtime": "2017-08-20 22:15:23",
        "ctime": "2017-05-04 00:15:29",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "璇咕咕",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 1059,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "璇咕咕",
        "roomid": "482156",
        "anchorInfo": {
          "uid": 733055,
          "uname": "璇咩",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 5,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "755777",
        "uid": 1650868,
        "target_id": 10278125,
        "medal_id": 3365,
        "score": 99,
        "level": 1,
        "intimacy": 99,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-08-05 16:27:40",
        "mtime": "2017-08-05 16:27:41",
        "ctime": "2017-08-05 16:27:40",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "猫饼",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 3549,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "猫饼",
        "roomid": "149608",
        "anchorInfo": {
          "uid": 10278125,
          "uname": "香菜猫饼",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 6,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "1096677",
        "uid": 1650868,
        "target_id": 36330559,
        "medal_id": 3742,
        "score": 2549,
        "level": 5,
        "intimacy": 848,
        "next_intimacy": 1000,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2018-01-06 22:32:29",
        "mtime": "2018-02-15 17:45:45",
        "ctime": "2018-01-06 22:32:29",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "消嘤器",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 192,
        "medal_color": 5805790,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "消嘤器",
        "roomid": "847617",
        "anchorInfo": {
          "uid": 36330559,
          "uname": "鼠二三三",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 4,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播\r\n"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "1055596",
        "uid": 1650868,
        "target_id": 915804,
        "medal_id": 3835,
        "score": 131,
        "level": 1,
        "intimacy": 131,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-12-22 19:40:50",
        "mtime": "2017-12-22 19:42:59",
        "ctime": "2017-12-22 19:40:50",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "亚丝娜",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 719,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "亚丝娜",
        "roomid": "521525",
        "anchorInfo": {
          "uid": 915804,
          "uname": "艾米莉亚EMT",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 5,
          "official_verify": {
            "type": -1,
            "desc": ""
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "571993",
        "uid": 1650868,
        "target_id": 6970675,
        "medal_id": 6415,
        "score": 1915,
        "level": 5,
        "intimacy": 214,
        "next_intimacy": 1000,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-05-03 22:40:09",
        "mtime": "2017-06-30 05:04:05",
        "ctime": "2017-05-03 22:40:09",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "喵侍",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 342,
        "medal_color": 5805790,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "喵侍",
        "roomid": "423227",
        "anchorInfo": {
          "uid": 6970675,
          "uname": "Yuri_喵四",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 4,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播\r\n"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "571606",
        "uid": 1650868,
        "target_id": 20872515,
        "medal_id": 8922,
        "score": 154634,
        "level": 17,
        "intimacy": 4733,
        "next_intimacy": 100000,
        "status": 1,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-05-03 18:43:13",
        "mtime": "2018-03-07 23:42:19",
        "ctime": "2017-05-03 18:43:13",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "翘李吗",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 3,
        "medal_color": 16752445,
        "today_feed": 0,
        "day_limit": 3000,
        "todayFeed": 0,
        "dayLimit": 3000,
        "medalName": "翘李吗",
        "roomid": "269706",
        "anchorInfo": {
          "uid": 20872515,
          "uname": "苏乔o_o",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 4,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 3,
        "guard_medal_title": "舰长buff：上限提升至150%"
      },
      {
        "id": "1051969",
        "uid": 1650868,
        "target_id": 56465669,
        "medal_id": 36374,
        "score": 99,
        "level": 1,
        "intimacy": 99,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2017-12-20 21:35:10",
        "mtime": "2017-12-20 21:35:55",
        "ctime": "2017-12-20 21:35:10",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "小雏菊",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 593,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "小雏菊",
        "roomid": "1942272",
        "anchorInfo": {
          "uid": 56465669,
          "uname": "宝贝乔w",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 3,
          "official_verify": {
            "type": -1,
            "desc": ""
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      },
      {
        "id": "1153657",
        "uid": 1650868,
        "target_id": 23658843,
        "medal_id": 43934,
        "score": 99,
        "level": 1,
        "intimacy": 99,
        "next_intimacy": 201,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2018-02-07 20:07:13",
        "mtime": "2018-02-07 20:07:14",
        "ctime": "2018-02-07 20:07:13",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "小丶琪",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 439,
        "medal_color": 6406234,
        "today_feed": 0,
        "day_limit": 500,
        "todayFeed": 0,
        "dayLimit": 500,
        "medalName": "小丶琪",
        "roomid": "5450114",
        "anchorInfo": {
          "uid": 23658843,
          "uname": "小丶琪w",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 3,
          "official_verify": {
            "type": 0,
            "desc": "bilibili直播签约主播"
          }
        },
        "guard_level": 0,
        "guard_medal_title": "未开启加成"
      }
    ],
    "pageinfo": {
      "totalpages": 1,
      "curPage": 1
    }
  }
}
 */
public class Medal {

	/** 勋章ID */
	private int id;
	
	/** 勋章名称 */
	private String name;
	
	/** 所属房间ID */
	private int roomId;
	
	/** 是否佩戴中 */
	private boolean wear;
	
	/** 每日投喂上限 */
	private int dayLimit;
	
	/** 今天已投喂 */
	private int todayFeed;
	
	private Medal() {
		this.id = 0;
		this.name = "";
		this.roomId = 0;
		this.wear = false;
		this.dayLimit = 0;
		this.todayFeed = 0;
	}
	
	/**
	 * {
        "id": "105454",
        "uid": 1650868,
        "target_id": 13173681,
        "medal_id": 470,
        "score": 129642,
        "level": 16,
        "intimacy": 29741,
        "next_intimacy": 50000,
        "status": 0,
        "source": 1,
        "receive_channel": 1,
        "is_receive": 1,
        "master_status": 0,
        "receive_time": "2016-05-19 18:55:56",
        "mtime": "2018-03-08 00:06:41",
        "ctime": "2016-05-19 18:55:56",
        "reserve1": "0",
        "reserve2": "",
        "medal_name": "高达",
        "master_available": "1",
        "target_name": "",
        "target_face": "",
        "rank": 1,
        "medal_color": 16746162,
        "today_feed": "49",
        "day_limit": 2000,
        "todayFeed": "49",
        "dayLimit": 2000,
        "medalName": "高达",
        "roomid": "51108",
        "anchorInfo": {
          "uid": 13173681,
          "uname": "M斯文败类",
          "rank": 10000,
          "mobile_verify": 1,
          "platform_user_level": 4,
          "official_verify": {
            "type": -1,
            "desc": ""
          }
        }
	 * @param json
	 */
	public Medal(JSONObject json) {
		this();
		
		if(json != null) {
			this.id = JsonUtils.getInt(json, BiliCmdAtrbt.medal_id, 0);
			this.name = JsonUtils.getStr(json, BiliCmdAtrbt.medal_name);
			this.roomId = RoomMgr.getInstn().getRealRoomId(
					JsonUtils.getInt(json, BiliCmdAtrbt.roomid, 0));
			this.wear = (JsonUtils.getInt(json, BiliCmdAtrbt.status, 0) > 0);
			this.dayLimit = JsonUtils.getInt(json, BiliCmdAtrbt.day_limit, 0);
			this.todayFeed = JsonUtils.getInt(json, BiliCmdAtrbt.todayFeed, 0);
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRoomId() {
		return roomId;
	}

	public boolean isWear() {
		return wear;
	}

	public int getDayLimit() {
		return dayLimit;
	}

	public int getTodayFeed() {
		return todayFeed;
	}

	@Override
	public String toString() {
		return ObjUtils.toBeanInfo(this);
	}
	
}
