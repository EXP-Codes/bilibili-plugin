package exp.bilibili.plugin.cache;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.Switch;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.XHRSender;
import exp.bilibili.protocol.bean.other.LotteryRoom;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.thread.LoopThread;

/**
 * <PRE>
 * Web行为模拟器（仿真机器人）
 * 
 * 	主要功能:
 *   1.全平台礼物抽奖管理器（小电视/高能礼物/节奏风暴）
 *   2.日常任务(签到/友爱社/小学数学)
 *   3.自动扭蛋、投喂
 *   4.自动领取成就奖励
 *   5.自动领取日常/周常礼包
 *   6.自动领取活动心跳礼物
 *   7.检查cookie有效期
 *   8.打印版权信息
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WebBot extends LoopThread {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(WebBot.class);
	
	/** 单位时间：天 */
	private final static long DAY_UNIT = TimeUtils.DAY_UNIT;
	
	/** 单位时间：小时 */
	private final static long HOUR_UNIT = TimeUtils.HOUR_UNIT;
	
	/** 北京时间时差 */
	private final static int HOUR_OFFSET = TimeUtils.PEKING_HOUR_OFFSET;
	
	/** 延迟时间 */
	private final static long DELAY_TIME = 120000L;
	
	/** 轮询间隔 */
	private final static long LOOP_TIME = 1000L;
	
	/** 定时触发事件的间隔 */
	private final static long EVENT_TIME = 3600000L;
	
	/** 定时触发事件的周期 */
	private final static int EVENT_LIMIT = (int) (EVENT_TIME / LOOP_TIME);
	
	/** 轮询次数 */
	private int loopCnt;
	
	/** 最近一次添加过cookie的时间点 */
	private long lastAddCookieTime;
	
	/** 执行下次日常任务的时间点 */
	private long nextTaskTime;
	
	/** 上次重置每日任务的时间点 */
	private long resetTaskTime;
	
	/** 下次在线心跳时间 */
	private long nextHBTime;
	
	/** 机器人睡眠开始时间点 */
	private int sleepBgnHour;
	
	/** 机器人睡眠结束时间点 */
	private int sleepEndHour;
	
	/** 单例 */
	private static volatile WebBot instance;
	
	/**
	 * 构造函数
	 */
	private WebBot() {
		super("Web行为模拟器");
		this.loopCnt = 0;
		this.lastAddCookieTime = System.currentTimeMillis();
		this.nextTaskTime = System.currentTimeMillis() + DELAY_TIME;	// 首次打开软件时, 延迟一点时间再执行任务
		this.resetTaskTime = TimeUtils.getZeroPointMillis(HOUR_OFFSET) + DELAY_TIME;	// 避免临界点时差, 后延一点时间
		this.nextHBTime = 0;
		
		String robotSleepTime = Config.getInstn().ROBOT_SLEEP_TIME();
		String[] hours = robotSleepTime.split("-");
		this.sleepBgnHour = NumUtils.toInt(hours[0], 1);
		this.sleepEndHour = NumUtils.toInt(hours[1], 6);
	}
	
	/**
	 * 获取单例
	 * @return
	 */
	public static WebBot getInstn() {
		if(instance == null) {
			synchronized (WebBot.class) {
				if(instance == null) {
					instance = new WebBot();
				}
			}
		}
		return instance;
	}

	@Override
	protected void _before() {
		log.info("{} 已启动", getName());
	}

	@Override
	protected void _loopRun() {
		try {
			if(!toSleep()) {
				toDo();
			}
		} catch(Exception e) {
			log.error("模拟Web行为异常", e);
		}
		_sleep(LOOP_TIME);
	}

	@Override
	protected void _after() {
		log.info("{} 已停止", getName());
	}
	
	private boolean toSleep() {
		boolean isSleep = false;
		// 凌晨3点~4点是B站判定机器人的固定时间，在这段时间的前后2小时不执行任何操作
		
		int hour = TimeUtils.getCurHour();
		if(hour >= sleepBgnHour && hour < sleepEndHour) {
			isSleep = true;
			UIUtils.log("[机器人休眠中] : 高危时间期间暂停一切行为");
		}
		return isSleep;
	}
	
	private void toDo() {
		
		// 优先参与抽奖
		LotteryRoom room = RoomMgr.getInstn().getGiftRoom();
		if(room != null) {
			toLottery(room);
			
		// 无抽奖操作则做其他事情
		} else {
			doDailyTasks();	// 执行每日任务
			doEvent();		// 定时触发事件
			doOnlineHB();	// 在线心跳（模拟观看直播）
		}
	}
	
	/**
	 * 通过后端注入服务器参与抽奖
	 * @param room
	 */
	private void toLottery(LotteryRoom room) {
		final int roomId = room.getRoomId();
		final String raffleId = room.getRaffleId();
		final String url = room.getUrl();
		
		// 小电视抽奖
		if(room.TYPE() == LotteryType.TV && Switch.isJoinLottery()) {
			_waitReactionTime(room);
			XHRSender.entryRoom(roomId, url);
			XHRSender.toTvLottery(roomId, raffleId);
			
		// 节奏风暴抽奖
		} else if(room.TYPE() == LotteryType.STORM && Switch.isJoinStorm()) {
//			_waitReactionTime(room);	// 节奏风暴无需等待, 当前环境太多机器人, 很难抢到前几名导致被捉
			XHRSender.toStormLottery(roomId, raffleId);
			
		// 总督登船领奖
		} else if(room.TYPE() == LotteryType.GUARD && Switch.isJoinLottery()) {
			_waitReactionTime(room);
			XHRSender.entryRoom(roomId, url);
			XHRSender.getGuardGift(roomId);
			
		// 高能抽奖
		} else if(Switch.isJoinLottery()) {
			_waitReactionTime(room);
			XHRSender.toEgLottery(roomId);
		}
	}
	
	/**
	 * 等待满足抽奖反应时间（过快反应会被冻结抽奖）
	 * @param room 抽奖房间
	 */
	private void _waitReactionTime(LotteryRoom room) {
		long waitTime = UIUtils.getReactionTime() - 
				(System.currentTimeMillis() - room.getStartTime());
		if(waitTime > 0) {
			ThreadUtils.tSleep(waitTime);
		}
	}
	
	/**
	 * 执行每日任务
	 */
	private void doDailyTasks() {
		resetDailyTasks();	// 满足某个条件则重置每日任务
		
		if(nextTaskTime > 0 && nextTaskTime <= System.currentTimeMillis()) {
			Set<BiliCookie> cookies = CookiesMgr.ALL(true);
			for(BiliCookie cookie : cookies) {
				if(cookie.TASK_STATUS().isAllFinish()) {
					continue;
				}
				
				long max = -1;
				max = NumUtils.max(XHRSender.toSign(cookie), max);				// 每日签到
				max = NumUtils.max(XHRSender.receiveDailyGift(cookie), max);	// 每日/每周礼包
				if(cookie.isBindTel()) {	// 仅绑定了手机的账号才能参与
					max = NumUtils.max(XHRSender.receiveHolidayGift(cookie), max);	// 活动心跳礼物
					max = NumUtils.max(XHRSender.toAssn(cookie), max);			// 友爱社
					
					if(!cookie.isFreeze()) {
						max = NumUtils.max(XHRSender.doMathTask(cookie), max);		// 小学数学
					}
				}
				nextTaskTime = NumUtils.max(nextTaskTime, max);
				ThreadUtils.tSleep(50);
			}
		}
	}
	
	/**
	 * 当cookies发生变化时, 重置每日任务
	 */
	private void resetDailyTasks() {
		
		// 当跨天时, 重置任务时间, 且清空完成任务的cookie标记
		long now = System.currentTimeMillis();
		if(now - resetTaskTime > DAY_UNIT) {
			resetTaskTime = now;
			nextTaskTime = now;
			CookiesMgr.getInstn().resetTaskStatus();
			log.info("日常任务已重置");
			
		// 当cookie发生变化时, 仅重置任务时间
		} else if(nextTaskTime <= 0 && 
				lastAddCookieTime != CookiesMgr.getInstn().getLastAddCookieTime()) {
			lastAddCookieTime = CookiesMgr.getInstn().getLastAddCookieTime();
			nextTaskTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * 触发事件
	 */
	private void doEvent() {
		if(loopCnt++ >= EVENT_LIMIT) {
			loopCnt = 0;
			
			// 零点错峰时不执行事件
			if(TimeUtils.inZeroPointRange() == false) {
				toCapsule();	// 自动扭蛋
				toAutoFeed();	// 自动投喂
				takeFinishAchieve();	// 领取成就奖励
			}
			
			reflashActivity();		// 刷新活跃值到数据库
			checkCookieExpires();	// 检查Cookie有效期
			
			// 打印心跳
			log.info("{} 活动中...", getName());
			UIUtils.printVersionInfo();
		}
	}
	
	/**
	 * 自动扭蛋机（仅小号）
	 */
	private void toCapsule() {
		if(Switch.isAutoFeed() == false) {
			return;
		}
		
		Set<BiliCookie> cookies = CookiesMgr.MINIs();
		for(BiliCookie cookie : cookies) {
			if(cookie.isAutoFeed()) {
				if(cookie.isRealName() || 
						(cookie.isBindTel() && Config.getInstn().PROTECT_FEED())) {
					// Undo 已经实名、 或绑了手机且开了投喂保护的， 不触发扭蛋
				} else {
					XHRSender.toCapsule(cookie);
				}
			}
		}
	}
	
	/**
	 * 自动投喂（仅小号）
	 */
	private void toAutoFeed() {
		if(Switch.isAutoFeed() == false) {
			return;	// 总开关
		}
		
		Set<BiliCookie> cookies = CookiesMgr.MINIs();
		for(BiliCookie cookie : cookies) {
			if(cookie.isAutoFeed()) {
				int roomId = cookie.getFeedRoomId();
				XHRSender.toFeed(cookie, roomId);
				ThreadUtils.tSleep(50);
			}
		}
	}
	
	/**
	 * 领取已完成的任务奖励
	 */
	private void takeFinishAchieve() {
		Set<BiliCookie> cookies = CookiesMgr.ALL();
		for(BiliCookie cookie : cookies) {
			XHRSender.toAchieve(cookie);
		}
	}
	
	/**
	 * 刷新活跃值到数据库(每天凌晨刷新一次)
	 */
	private void reflashActivity() {
		ActivityMgr.getInstn().reflash();
	}
	
	/**
	 * 检查Cookie有效期
	 */
	private void checkCookieExpires() {
		final long WARN_MILLIS = 48 * HOUR_UNIT;	// 有效期到期前48小时开始警告
		final long now = System.currentTimeMillis();
		
		// 检查小号的登陆有效期
		Set<BiliCookie> cookies = CookiesMgr.MINIs();
		for(BiliCookie cookie : cookies) {
			long expires = TimeUtils.toMillis(cookie.EXPIRES());
			long diff = expires - now;
			if(diff <= WARN_MILLIS) {
				if(diff > HOUR_UNIT) {
					UIUtils.log("小号 [", cookie.NICKNAME(), "] 剩余的登陆有效期: ", 
							(diff / HOUR_UNIT), "小时 (到期自动注销)");
				} else {
					CookiesMgr.getInstn().del(cookie);
					UIUtils.log("小号 [", cookie.NICKNAME(), "] 登陆已过期: 请重新登陆");
				}
			}
		}
		
		// 检查主号和马甲号的登陆有效期(取两者最小值作为共同有效期)
		long mainExpires = TimeUtils.toMillis(CookiesMgr.MAIN().EXPIRES());
		if(CookiesMgr.VEST() != BiliCookie.NULL) {
			long vestExpires = TimeUtils.toMillis(CookiesMgr.VEST().EXPIRES());
			mainExpires = (mainExpires < vestExpires ? mainExpires : vestExpires);
		}
		long diff = mainExpires - now;
		if(diff <= WARN_MILLIS) {
			if(diff > HOUR_UNIT) {
				UIUtils.log("主号 [", CookiesMgr.MAIN().NICKNAME(), "] 剩余的登陆有效期: ", 
						(diff / HOUR_UNIT), "小时 (到期自动注销并退出程序)");
				
			} else {
				String msg = StrUtils.concat("主号 [", CookiesMgr.MAIN().NICKNAME(), "] 登陆已过期: 重启后请重新登陆");
				CookiesMgr.getInstn().del(CookiesMgr.MAIN());
				CookiesMgr.getInstn().del(CookiesMgr.VEST());
				
				UIUtils.log(msg);
				UIUtils.notityExit(msg);
			}
		}
	}
	
	/**
	 * 在线心跳（模拟保持直播间在线状态）
	 */
	private void doOnlineHB() {
		if(nextHBTime <= System.currentTimeMillis()) {
			Set<BiliCookie> cookies = CookiesMgr.ALL(true);
			for(BiliCookie cookie : cookies) {
				if(!cookie.isBindTel()) {	// 仅绑定了手机的账号才能参与
					continue;
				}
				
				XHRSender.toWatchLive(cookie);	// PC端
				nextHBTime = XHRSender.onlineHeartbeat(cookie);
				ThreadUtils.tSleep(50);
			}
			nextHBTime += System.currentTimeMillis();
		}
	}
	
}
