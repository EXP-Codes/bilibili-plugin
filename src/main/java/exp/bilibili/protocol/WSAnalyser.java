package exp.bilibili.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.cache.ActivityMgr;
import exp.bilibili.plugin.cache.ChatMgr;
import exp.bilibili.plugin.cache.MsgKwMgr;
import exp.bilibili.plugin.cache.OnlineUserMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.utils.Switch;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.bean.ws.ActivityBannerRedNoticeClose;
import exp.bilibili.protocol.bean.ws.ActivityEvent;
import exp.bilibili.protocol.bean.ws.ChatMsg;
import exp.bilibili.protocol.bean.ws.ComboEnd;
import exp.bilibili.protocol.bean.ws.ComboSend;
import exp.bilibili.protocol.bean.ws.DailyQuestNewday;
import exp.bilibili.protocol.bean.ws.EnergyLottery;
import exp.bilibili.protocol.bean.ws.EntryEffect;
import exp.bilibili.protocol.bean.ws.GuardBuy;
import exp.bilibili.protocol.bean.ws.GuardLotteryStart;
import exp.bilibili.protocol.bean.ws.GuardMsg;
import exp.bilibili.protocol.bean.ws.LiveMsg;
import exp.bilibili.protocol.bean.ws.NoticeMsg;
import exp.bilibili.protocol.bean.ws.Preparing;
import exp.bilibili.protocol.bean.ws.RaffleEnd;
import exp.bilibili.protocol.bean.ws.RaffleStart;
import exp.bilibili.protocol.bean.ws.RoomBlock;
import exp.bilibili.protocol.bean.ws.RoomRank;
import exp.bilibili.protocol.bean.ws.RoomRealTimeMessageUpdate;
import exp.bilibili.protocol.bean.ws.RoomSilentOff;
import exp.bilibili.protocol.bean.ws.SendGift;
import exp.bilibili.protocol.bean.ws.SpecialGift;
import exp.bilibili.protocol.bean.ws.SysGift;
import exp.bilibili.protocol.bean.ws.SysMsg;
import exp.bilibili.protocol.bean.ws.TvEnd;
import exp.bilibili.protocol.bean.ws.TvLottery;
import exp.bilibili.protocol.bean.ws.WelcomeGuard;
import exp.bilibili.protocol.bean.ws.WelcomeMsg;
import exp.bilibili.protocol.bean.ws.WishBottle;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * WebSocket接收的JSON报文解析器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WSAnalyser {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(WSAnalyser.class);
	
	/** 上次开播时间 */
	private static long lastOpenLive = 0;
	
	/** 私有化构造函数 */
	protected WSAnalyser() {}
	
	/**
	 * 把从ws接收到到的json消息转换为Bean对象并处理
	 * @param json Json格式消息
	 * @param roomId 被监听的房间号
	 * @param onlyListen 是否只监听礼物通知消息
	 * @return 是否处理成功
	 */
	public static boolean toMsgBean(JSONObject json, int roomId, boolean onlyListen) {
		boolean isOk = true;
		BiliCmd biliCmd = BiliCmd.toCmd(JsonUtils.getStr(json, BiliCmdAtrbt.cmd));
		
		// 根据通知消息的类型转换成真正的消息对象
		if(biliCmd == BiliCmd.NOTICE_MSG) {
			NoticeMsg msg = new NoticeMsg(json);
			json = msg.toJson();
			biliCmd = BiliCmd.toCmd(JsonUtils.getStr(json, BiliCmdAtrbt.cmd));
		}
		
		// 把不同的消息对象分发到对应的处理器
		if(!onlyListen && biliCmd == BiliCmd.DANMU_MSG) {
			toDo(new ChatMsg(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.SEND_GIFT) {
			toDo(new SendGift(json));
			
		} else if(biliCmd == BiliCmd.SYS_MSG) {
			if(StrUtils.isNotEmpty(_getRoomId(json))) {
				toDo(new TvLottery(json), !onlyListen);
				
			} else if(!onlyListen) {
				toDo(new SysMsg(json));
			}
			
		} else if(biliCmd == BiliCmd.SYS_GIFT) {
			if(StrUtils.isNotEmpty(_getRoomId(json))) {
				toDo(new EnergyLottery(json), onlyListen);
				
			} else if(!onlyListen) {
				toDo(new SysGift(json));
			}
			
		} else if(biliCmd == BiliCmd.SPECIAL_GIFT) {
			toDo(new SpecialGift(json), roomId);
			
		} else if(biliCmd == BiliCmd.RAFFLE_START) {
			toDo(new RaffleStart(json), onlyListen);
			
		} else if(!onlyListen && biliCmd == BiliCmd.RAFFLE_END) {
			toDo(new RaffleEnd(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.WELCOME) {
			toDo(new WelcomeMsg(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.WELCOME_GUARD) {
			toDo(new WelcomeGuard(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.GUARD_BUY) {
			toDo(new GuardBuy(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.GUARD_MSG) {
			toDo(new GuardMsg(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.GUARD_LOTTERY_START) {
			toDo(new GuardLotteryStart(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ENTRY_EFFECT) {
			toDo(new EntryEffect(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.LIVE) {
			toDo(new LiveMsg(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.PREPARING) {
			toDo(new Preparing(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ROOM_SILENT_OFF) {
			toDo(new RoomSilentOff(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.WISH_BOTTLE) {
			toDo(new WishBottle(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ROOM_BLOCK_MSG) {
			toDo(new RoomBlock(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ACTIVITY_EVENT) {
			toDo(new ActivityEvent(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ROOM_RANK) {
			toDo(new RoomRank(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.COMBO_SEND) {
			toDo(new ComboSend(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.COMBO_END) {
			toDo(new ComboEnd(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.TV_END) {
			toDo(new TvEnd(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.DAILY_QUEST_NEWDAY) {
			toDo(new DailyQuestNewday(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ACTIVITY_BANNER_RED_NOTICE_CLOSE) {
			toDo(new ActivityBannerRedNoticeClose(json));
			
		} else if(!onlyListen && biliCmd == BiliCmd.ROOM_REAL_TIME_MESSAGE_UPDATE) {
			toDo(new RoomRealTimeMessageUpdate(json));
			
		} else {
			isOk = onlyListen;
		}
		return isOk;
	}
	
	/**
	 * 获取抽奖房间号
	 * @param json
	 * @return
	 */
	private static String _getRoomId(JSONObject json) {
		String roomId = JsonUtils.getStr(json, BiliCmdAtrbt.real_roomid);
		if(StrUtils.isEmpty(roomId)) {
			roomId = JsonUtils.getStr(json, BiliCmdAtrbt.roomid);
		}
		return roomId;
	}
	
	/**
	 * 用户发言消息
	 * @param msgBean
	 */
	private static void toDo(ChatMsg msgBean) {
		String msg = StrUtils.concat(
				"[", msgBean.getMedal(), "][LV", msgBean.getLevel(), "][",
				msgBean.getUsername(), "]: ", msgBean.getMsg()
		);
		UIUtils.chat(msg);
		log.info(msg);
		
		OnlineUserMgr.getInstn().addOnlineUser(msgBean.getUsername());
		ActivityMgr.getInstn().add(msgBean);
		ChatMgr.getInstn().analyseDanmu(msgBean);
	}
	
	/**
	 * 礼物投喂消息
	 * @param msgBean
	 */
	private static void toDo(SendGift msgBean) {
		String msg = StrUtils.concat(
				"[", msgBean.getUname(), "] ", msgBean.getAction(), 
				" [", msgBean.getGiftName(), "] x", msgBean.getNum()
		);
		UIUtils.chat(msg);
		log.info(msg);
		
		ChatMgr.getInstn().addThxGift(msgBean);
		OnlineUserMgr.getInstn().addOnlineUser(msgBean.getUname());
		ActivityMgr.getInstn().add(msgBean);
	}
	
	/**
	 * 系统消息
	 * @param msgBean
	 */
	private static void toDo(SysMsg msgBean) {
		UIUtils.notify(msgBean.getMsg());	// 系统公告的消息体里面自带了 [系统公告: ]
		log.info(msgBean.getMsg());
	}
	
	/**
	 * 小电视/大楼通知
	 * @param msgBean
	 * @param chatSession 版聊的会话
	 */
	private static void toDo(TvLottery msgBean, boolean chatSession) {
		if(chatSession && msgBean.getMsg().contains("全区")) {
			String msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 正在全区抽奖中!!!");
			UIUtils.notify(msg);
			log.info(msg);
			RoomMgr.getInstn().addTvRoom(msgBean.ROOM_ID(), msgBean.getUrl());
			
		} else if(!chatSession && !msgBean.getMsg().contains("全区")) {
			String msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 正在分区抽奖中!!!");
			UIUtils.notify(msg);
			log.info(msg);
			RoomMgr.getInstn().addGiftRoom(msgBean.ROOM_ID());
				
		} else {
			// Undo: 避免其他监听会话重复打印 全区抽奖公告
			RoomMgr.getInstn().addTvRoom(msgBean.ROOM_ID(), msgBean.getUrl());
		}
		
		RoomMgr.getInstn().relate(msgBean.getRoomId(), msgBean.getRealRoomId());
	}
	
	/**
	 * 全频道礼物通知
	 * @param msgBean
	 */
	private static void toDo(SysGift msgBean) {
		String msg = StrUtils.concat("礼物公告：", msgBean.getMsgText());
		UIUtils.notify(msg);
		log.info(msg);
	}
	
	/**
	 * 高能礼物抽奖消息
	 * @param msgBean
	 */
	private static void toDo(EnergyLottery msgBean, boolean onlyListen) {
		String msg = "";
		if(msgBean.getMsg().contains("20倍节奏风暴")) {
			msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 开启了20倍节奏风暴!!!");
			// TODO 极少人一次送20个节奏风暴, 暂没必要参加抽奖
			
		} else {
			msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 正在高能抽奖中!!!");
			RoomMgr.getInstn().addGiftRoom(msgBean.ROOM_ID());
			RoomMgr.getInstn().relate(msgBean.getRoomId(), msgBean.getRealRoomId());
		}
		
		if(onlyListen == false) {
			UIUtils.notify(msg);
			log.info(msg);
			
		} else {
			// Undo: 高能礼物是全平台公告, 此处可避免重复打印高能公告
		}
	}
	
	/**
	 * 特殊礼物：(直播间内)节奏风暴消息
	 * @param msgBean
	 */
	private static void toDo(SpecialGift msgBean, int roomId) {
		String msg = StrUtils.concat("直播间 [", roomId, "] 开启了节奏风暴!!!");
		UIUtils.notify(msg);
		log.info(msg);
		
		if(Switch.isJoinStorm()) {
			RoomMgr.getInstn().addStormRoom(roomId, msgBean.getRaffleId());
		}
	}

	/**
	 * (直播间内)高能抽奖开始消息
	 * @param msgBean
	 */
	private static void toDo(RaffleStart msgBean, boolean onlyListen) {
		if(onlyListen == false) {
			String msg = StrUtils.concat("感谢[", msgBean.getFrom(), "]的高能!!!");
			ChatMgr.getInstn().sendThxEnergy(msg);
			log.info(msg);
		} else {
			// Undo: 避免把其他直播间的高能礼物在当前直播间进行感谢
		}
		
		RoomMgr.getInstn().addGiftRoom(msgBean.getRoomId());
	}

	/**
	 * (直播间内)高能抽奖结束消息
	 * @param msgBean
	 */
	private static void toDo(RaffleEnd msgBean) {
		String msg = StrUtils.concat("恭喜非酉[", msgBean.getWinner(), 
				"]竟然抽到了[", msgBean.getGiftName(), "]x", msgBean.getGiftNum());
		log.info(msg);
		
		ChatMgr.getInstn().sendThxEnergy(msg);
	}
	
	/**
	 * 欢迎老爷/房管消息
	 * @param msgBean
	 */
	private static void toDo(WelcomeMsg msgBean) {
		String msg = StrUtils.concat("[", msgBean.getVipDesc(), "][", 
				msgBean.getUsername(), "] ", MsgKwMgr.getAdv(), "溜进了直播间"
		);
		UIUtils.chat(msg);
		log.info(msg);
	}
	
	/**
	 * 欢迎船员消息
	 * @param msgBean
	 */
	private static void toDo(WelcomeGuard msgBean) {
		String msg = StrUtils.concat("[", msgBean.getGuardType().DESC(), "][", 
				msgBean.getUsername(), "] ", MsgKwMgr.getAdv(), "溜进了直播间"
		);
		UIUtils.chat(msg);
		log.info(msg);
	}
	
	/**
	 * (直播间内)新船员上船消息
	 * @param msgBean
	 */
	private static void toDo(GuardBuy msgBean) {
		String msg = StrUtils.concat("[", msgBean.getUsername(), "] ", 
				MsgKwMgr.getAdv(), "上了", msgBean.getGuardType().DESC(), ":活跃+",
				ActivityMgr.showCost(msgBean.getGuardType().DESC(), 1)
		);
		UIUtils.chat(msg);
		log.info(msg);
			
		ChatMgr.getInstn().sendThxGuard(msg);
		RoomMgr.getInstn().addGuardRoom(msgBean.getRoomId(), "");
		
		OnlineUserMgr.getInstn().addOnlineUser(msgBean.getUsername());
		ActivityMgr.getInstn().add(msgBean);
	}

	/**
	 * (全频道)总督登船消息
	 * @param msgBean
	 */
	private static void toDo(GuardMsg msgBean) {
		UIUtils.chat(msgBean.getMsg());
		log.info(msgBean.getMsg());
		
		if(msgBean.getRoomId() <= 0) {
			msgBean.setRoomId(XHRSender.searchRoomId(msgBean.getLiveup()));;
		}
		RoomMgr.getInstn().addGuardRoom(msgBean.getRoomId(), msgBean.getUrl());
	}
	
	/**
	 * (直播间内)新船员上船抽奖消息
	 * @param msgBean
	 */
	private static void toDo(GuardLotteryStart msgBean) {
		UIUtils.chat(msgBean.getMsg());
		log.info(msgBean.getMsg());
		RoomMgr.getInstn().addGuardRoom(msgBean.getRoomId(), "");
	}
	
	/**
	 * 船员进入直播间特效
	 * @param msgBean
	 */
	private static void toDo(EntryEffect msgBean) {
		// UNDO
	}
	
	/**
	 * 开播通知
	 * @param msgBean
	 */
	private static void toDo(LiveMsg msgBean) {
		String msg = StrUtils.concat("您关注的直播间 [", msgBean.getRoomId(), "] 开播啦!!!");
		UIUtils.chat(msg);
		log.info(msg);
		
		// 一小时内的重复开播, 认为是房间信号调整, 不重复提示
		long curTime = System.currentTimeMillis();
		if(curTime - lastOpenLive > 3600000L) {
			ChatMgr.getInstn().helloLive(msgBean.getRoomId());
			UIUtils.notityLive(msgBean.getRoomId());
		}
		lastOpenLive = curTime;
	}
	
	/**
	 * 关播通知
	 * @param msgBean
	 */
	private static void toDo(Preparing msgBean) {
		String msg = StrUtils.concat("直播间 [", msgBean.getRoomId(), "] 主播已下线.");
		UIUtils.chat(msg);
		log.info(msg);
	}
	
	/**
	 * 关播通知
	 * @param msgBean
	 */
	private static void toDo(RoomSilentOff msgBean) {
		String msg = StrUtils.concat("直播间 [", msgBean.getRoomId(), "] 串流已停止.");
		UIUtils.chat(msg);
		log.info(msg);
	}
	
	/**
	 * (直播间内)许愿瓶实现进度消息
	 * @param msgBean
	 */
	private static void toDo(WishBottle msgBean) {
		// Undo
	}
	
	/**
	 * (直播间内)关小黑屋通知消息
	 * @param msgBean
	 */
	private static void toDo(RoomBlock msgBean) {
		log.info("直播间 [{}] 的用户 [{}] 被关小黑屋了!!!", 
				msgBean.getRoomId(), msgBean.getUname());
	}
	
	/**
	 * 2018春节活动(新春榜)触发事件
	 * @param msgBean
	 */
	private static void toDo(ActivityEvent msgBean) {
		// Undo
	}
	
	/**
	 * 直播间小时榜排名通知消息
	 * @param msgBean
	 */
	private static void toDo(RoomRank msgBean) {
		// Undo
	}
	
	/**
	 * (直播间内)礼物combo连击消息
	 * @param msgBean
	 */
	private static void toDo(ComboSend msgBean) {
		// Undo
	}
	
	/**
	 * (直播间内)礼物combo连击结束消息
	 * @param msgBean
	 */
	private static void toDo(ComboEnd msgBean) {
		// Undo
	}
	
	/**
	 * 小电视中奖消息
	 * @param msgBean
	 */
	private static void toDo(TvEnd msgBean) {
		// Undo
	}
	
	/**
	 * 凌晨 00:00 新一天的通知消息
	 * @param msgBean
	 */
	private static void toDo(DailyQuestNewday msgBean) {
		// Undo
	}
	
	/**
	 * 红色活动横幅通知事件
	 * @param msgBean
	 */
	private static void toDo(ActivityBannerRedNoticeClose msgBean) {
		// Undo
	}
	
	/**
	 * 直播间实时信息更新（粉丝人数）
	 * @param msgBean
	 */
	private static void toDo(RoomRealTimeMessageUpdate msgBean) {
		// Undo
	}
	
}
