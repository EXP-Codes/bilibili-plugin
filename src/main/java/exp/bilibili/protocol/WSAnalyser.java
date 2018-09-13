package exp.bilibili.protocol;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.cache.ActivityMgr;
import exp.bilibili.plugin.cache.ChatMgr;
import exp.bilibili.plugin.cache.MsgKwMgr;
import exp.bilibili.plugin.cache.OnlineUserMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.bean.ws.ActivityEvent;
import exp.bilibili.protocol.bean.ws.ChatMsg;
import exp.bilibili.protocol.bean.ws.EnergyLottery;
import exp.bilibili.protocol.bean.ws.GuardBuy;
import exp.bilibili.protocol.bean.ws.GuardMsg;
import exp.bilibili.protocol.bean.ws.LiveMsg;
import exp.bilibili.protocol.bean.ws.Preparing;
import exp.bilibili.protocol.bean.ws.RaffleEnd;
import exp.bilibili.protocol.bean.ws.RaffleStart;
import exp.bilibili.protocol.bean.ws.RoomBlock;
import exp.bilibili.protocol.bean.ws.RoomSilentOff;
import exp.bilibili.protocol.bean.ws.SendGift;
import exp.bilibili.protocol.bean.ws.SpecialGift;
import exp.bilibili.protocol.bean.ws.SysGift;
import exp.bilibili.protocol.bean.ws.SysMsg;
import exp.bilibili.protocol.bean.ws.TvLottery;
import exp.bilibili.protocol.bean.ws.WelcomeGuard;
import exp.bilibili.protocol.bean.ws.WelcomeMsg;
import exp.bilibili.protocol.bean.ws.WishBottle;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * WS接收的JSON报文解析器
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
	 * @param json
	 * @return
	 */
	public static boolean toMsgBean(JSONObject json) {
		boolean isOk = true;
		String cmd = JsonUtils.getStr(json, BiliCmdAtrbt.cmd);
		BiliCmd biliCmd = BiliCmd.toCmd(cmd);
		
		if(biliCmd == BiliCmd.DANMU_MSG) {
			toDo(new ChatMsg(json));
			
		} else if(biliCmd == BiliCmd.SEND_GIFT) {
			toDo(new SendGift(json));
			
		} else if(biliCmd == BiliCmd.SYS_MSG) {
			if(StrUtils.isNotEmpty(_getRoomId(json))) {
				toDo(new TvLottery(json));
				
			} else {
				toDo(new SysMsg(json));
			}
			
		} else if(biliCmd == BiliCmd.SYS_GIFT) {
			if(StrUtils.isNotEmpty(_getRoomId(json))) {
				toDo(new EnergyLottery(json));
				
			} else {
				toDo(new SysGift(json));
			}
			
		} else if(biliCmd == BiliCmd.SPECIAL_GIFT) {
			toDo(new SpecialGift(json));
			
		} else if(biliCmd == BiliCmd.RAFFLE_START) {
			toDo(new RaffleStart(json));
			
		} else if(biliCmd == BiliCmd.RAFFLE_END) {
			toDo(new RaffleEnd(json));
			
		} else if(biliCmd == BiliCmd.WELCOME) {
			toDo(new WelcomeMsg(json));
			
		} else if(biliCmd == BiliCmd.WELCOME_GUARD) {
			toDo(new WelcomeGuard(json));
			
		} else if(biliCmd == BiliCmd.GUARD_BUY) {
			toDo(new GuardBuy(json));
			
		} else if(biliCmd == BiliCmd.GUARD_MSG) {
			toDo(new GuardMsg(json));
			
		} else if(biliCmd == BiliCmd.LIVE) {
			toDo(new LiveMsg(json));
			
		} else if(biliCmd == BiliCmd.PREPARING) {
			toDo(new Preparing(json));
			
		} else if(biliCmd == BiliCmd.ROOM_SILENT_OFF) {
			toDo(new RoomSilentOff(json));
			
		} else if(biliCmd == BiliCmd.WISH_BOTTLE) {
			toDo(new WishBottle(json));
			
		} else if(biliCmd == BiliCmd.ROOM_BLOCK_MSG) {
			toDo(new RoomBlock(json));
			
		} else if(biliCmd == BiliCmd.ACTIVITY_EVENT) {
			toDo(new ActivityEvent(json));
			
		} else {
			isOk = false;
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
	 * 小电视通知
	 * @param msgBean
	 */
	private static void toDo(TvLottery msgBean) {
		String msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 正在小电视抽奖中!!!");
		UIUtils.notify(msg);
		log.info(msg);
		
		RoomMgr.getInstn().addTvRoom(msgBean.ROOM_ID(), msgBean.getTvId());
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
	private static void toDo(EnergyLottery msgBean) {
		String msg = StrUtils.concat("直播间 [", msgBean.ROOM_ID(), "] 正在高能抽奖中!!!");
		UIUtils.notify(msg);
		log.info(msg);
		
		RoomMgr.getInstn().addGiftRoom(msgBean.ROOM_ID());
		RoomMgr.getInstn().relate(msgBean.getRoomId(), msgBean.getRealRoomId());
	}
	
	/**
	 * 特殊礼物：(直播间内)节奏风暴消息
	 * @param msgBean
	 */
	private static void toDo(SpecialGift msgBean) {
		String msg = StrUtils.concat("直播间 [", msgBean.getRoomId(), "] 开启了节奏风暴!!!");
		UIUtils.notify(msg);
		log.info(msg);
		
		RoomMgr.getInstn().addStormRoom(msgBean.getRoomId(), msgBean.getRaffleId());
	}

	/**
	 * (直播间内)高能抽奖开始消息
	 * @param msgBean
	 */
	private static void toDo(RaffleStart msgBean) {
		String msg = StrUtils.concat("感谢[", msgBean.getFrom(), "]的高能!!!");
		log.info(msg);
		
		ChatMgr.getInstn().sendThxEnergy(msg);
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
		String msg = StrUtils.concat("[", msgBean.getGuardDesc(), "][", 
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
				MsgKwMgr.getAdv(), "上了", msgBean.getGuardDesc(), ":活跃+",
				ActivityMgr.showCost(msgBean.getGuardDesc(), 1)
		);
		UIUtils.chat(msg);
		log.info(msg);
			
		ChatMgr.getInstn().sendThxGuard(msg);
		OnlineUserMgr.getInstn().addOnlineUser(msgBean.getUsername());
		ActivityMgr.getInstn().add(msgBean);
	}

	/**
	 * (全频道)登船消息
	 * @param msgBean
	 */
	private static void toDo(GuardMsg msgBean) {
		UIUtils.chat(msgBean.getMsg());
		log.info(msgBean.getMsg());
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
	
}
