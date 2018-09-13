package exp.bilibili.protocol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.Area;
import exp.bilibili.plugin.envm.Gift;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.bean.other.User;
import exp.bilibili.protocol.bean.xhr.Achieve;
import exp.bilibili.protocol.bean.xhr.BagGift;
import exp.bilibili.protocol.bean.xhr.Medal;
import exp.bilibili.protocol.xhr.Chat;
import exp.bilibili.protocol.xhr.DailyTasks;
import exp.bilibili.protocol.xhr.Gifts;
import exp.bilibili.protocol.xhr.Guard;
import exp.bilibili.protocol.xhr.LiveArea;
import exp.bilibili.protocol.xhr.Login;
import exp.bilibili.protocol.xhr.LotteryEnergy;
import exp.bilibili.protocol.xhr.LotteryStorm;
import exp.bilibili.protocol.xhr.LotteryTV;
import exp.bilibili.protocol.xhr.Other;
import exp.bilibili.protocol.xhr.WatchLive;
import exp.libs.envm.Colors;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.os.ThreadUtils;

/**
 * <PRE>
 * XHR请求发送器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class XHRSender {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(XHRSender.class);
	
	/**
	 * 获取管理员在B站link中心针对本插件的授权校验标签
	 * @return
	 */
	public static String queryCertTags() {
		return Other.queryCertificateTags();
	}
	
	/**
	 * 查询直播间的房管(含主播)
	 * @param roomId 直播间ID
	 * @return 房管列表
	 */
	public static Set<User> queryManagers() {
		int roomId = UIUtils.getLiveRoomId();
		return Other.queryManagers(roomId);
	}
	
	/**
	 * 临时把用户关小黑屋1小时
	 * @param username
	 * @return
	 */
	public static boolean blockUser(String username) {
		BiliCookie cookie = CookiesMgr.MAIN();
		int roomId = UIUtils.getLiveRoomId();
		final int hour = 1;
		return Other.blockUser(cookie, roomId, username, hour);
	}
	
	/**
	 * 获取二维码登陆信息(用于在本地生成二维码图片)
	 * @return
	 */
	public static String getQrcodeInfo() {
		return Login.getQrcodeInfo();
	}
	
	/**
	 * 检测二维码是否扫码登陆成功
	 * @param oauthKey 二维码登陆信息中提取的oauthKey
	 * @return 若扫码登陆成功, 则返回有效Cookie
	 */
	public static BiliCookie toLogin(String oauthKey) {
		return Login.toLogin(oauthKey);
	}
	
	/**
	 * 下载登陆用的验证码图片
	 * @param imgPath 图片保存路径
	 * @return 与该验证码配套的cookies
	 */
	public static String downloadVccode(String imgPath) {
		return Login.downloadVccode(imgPath);
	}
	
	/**
	 * 通过帐密+验证码方式登陆
	 * @param username 账号
	 * @param password 密码
	 * @param vccode 验证码
	 * @param vcCookies 与验证码配套的登陆用cookie
	 * @return 
	 */
	public static BiliCookie toLogin(String username, String password, 
			String vccode, String vcCookies) {
		return Login.toLogin(username, password, vccode, vcCookies);
	}
	
	/**
	 * 查询账号信息(并写入cookie内)
	 * @param cookie
	 * @return username
	 */
	public static boolean queryUserInfo(BiliCookie cookie) {
		boolean isOk = Other.queryUserInfo(cookie);	// 普通信息: 用户ID+昵称
		isOk &= Other.queryUserSafeInfo(cookie);	// 安全信息: 是否绑定手机号
		return isOk;
	}
	
	/**
	 * 查询账号在当前直播间的授权信息(并写入cookie内)
	 * @param cookie
	 * @return
	 */
	public static boolean queryUserAuthorityInfo(BiliCookie cookie) {
		int roomId = UIUtils.getLiveRoomId();
		return Other.queryUserAuthorityInfo(cookie, roomId);
	}

	/**
	 * 每日签到
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long toSign(BiliCookie cookie) {
		long nextTaskTime = (cookie.TASK_STATUS().isFinSign() ? 
				-1 : DailyTasks.toSign(cookie));
		if(nextTaskTime <= 0) {
			cookie.TASK_STATUS().markSign();
		}
		return nextTaskTime;
	}
	
	/**
	 * 友爱社签到
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long toAssn(BiliCookie cookie) {
		long nextTaskTime = (cookie.TASK_STATUS().isFinAssn() ? 
				-1 : DailyTasks.toAssn(cookie));
		
		// 若有爱社签到失败, 则模拟双端观看直播
		if(nextTaskTime > 0) {
			int roomId = UIUtils.getLiveRoomId();
			WatchLive.toWatchPCLive(cookie, roomId);	// PC端
//			WatchLive.toWatchAppLive(cookie, roomId);	// 手机端 (FIXME: 暂时无效)\
			
		} else {
			cookie.TASK_STATUS().markAssn();
		}
		return nextTaskTime;
	}
	
	/**
	 * 领取日常/周常的勋章/友爱社礼物
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long receiveDailyGift(BiliCookie cookie) {
		long nextTaskTime = (cookie.TASK_STATUS().isFinDailyGift() ? 
				-1 : DailyTasks.receiveDailyGift(cookie));
		if(nextTaskTime <= 0) {
			cookie.TASK_STATUS().markDailyGift();
		}
		return nextTaskTime;
	}
	
	/**
	 * 领取活动心跳礼物（每在线10分钟领取一个xxx）
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long receiveHolidayGift(BiliCookie cookie) {
		long nextTaskTime = (cookie.TASK_STATUS().isFinHoliday() ? 
				-1 : DailyTasks.receiveHolidayGift(cookie));
		if(nextTaskTime <= 0) {
			cookie.TASK_STATUS().markHolidayGift();
		}
		return nextTaskTime;
	}
	
	/**
	 * 执行小学数学日常任务
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long doMathTask(BiliCookie cookie) {
		long nextTaskTime = (cookie.TASK_STATUS().isFinMath() ? 
				-1 : DailyTasks.doMathTask(cookie));
		if(nextTaskTime <= 0) {
			cookie.TASK_STATUS().markMath();
		}
		return nextTaskTime;
	}
	
	/**
	 * 检索主播的房间号
	 * @param liveupName 主播名称
	 * @return 主播的房间号(长号)
	 */
	public static int searchRoomId(String liveupName) {
		return Other.searchRoomId(CookiesMgr.MAIN(), liveupName);
	}
	
	/**
	 * 提取直播间内的总督ID列表.
	 * 	(已经领取过某个总督奖励的用户, 不会再查询到相关的总督id)
	 * @param cookie
	 * @param roomId 直播间号
	 * @return 可以领取奖励总督ID列表
	 */
	public static List<String> checkGuardIds(BiliCookie cookie, int roomId) {
		return Guard.checkGuardIds(cookie, roomId);
	}
	
	/**
	 * 领取总督亲密度奖励
	 * @param cookie
	 * @param roomId 总督所在房间
	 * @param guardId 总督编号
	 * @return
	 */
	public static boolean getGuardGift(BiliCookie cookie, int roomId, String guardId) {
		return Guard.getGuardGift(cookie, roomId, guardId);
	}
	
	/**
	 * 领取总督亲密度奖励
	 * @param cookie
	 * @param roomId 总督所在房间
	 * @return 补领个数
	 */
	public static int getGuardGift(int roomId) {
		return Guard.getGuardGift(roomId);
	}
	
	/**
	 * 为所有登陆用户补领取热门直播间的总督亲密奖励
	 */
	public static int getGuardGift() {
		
		// 查询当前热梦直播间
		HotLiveRange range = UIUtils.getHotLiveRange();
		List<Integer> roomIds = queryTopLiveRoomIds(range);
		
		int cnt = 0;
		for(Integer roomId : roomIds) {
			cnt += getGuardGift(roomId);
			ThreadUtils.tSleep(50);
		}
		return cnt;
	}
	
	/**
	 * 查询每个直播分区的榜首房间号
	 * @return 分区 -> 榜首房间号
	 */
	public static Map<Area, Integer> getAreaTopOnes() {
		return LiveArea.getAreaTopOnes();
	}
	
	/**
	 * 扫描当前的人气直播间房号列表
	 * @param range 扫描页码范围
	 * @return
	 */
	public static List<Integer> queryTopLiveRoomIds(HotLiveRange range) {
		return LotteryStorm.queryHotLiveRoomIds(range);
	}
	
	/**
	 * 扫描并加入节奏风暴
	 * @param hotRoomIds 热门房间列表
	 * @param scanInterval 扫描房间间隔
	 */
	public static void scanAndJoinStorms(List<Integer> hotRoomIds, long scanInterval) {
		LotteryStorm.toLottery(hotRoomIds, scanInterval);
	}
	
	/**
	 * 节奏风暴抽奖
	 * @param roomId
	 * @return
	 */
	public static void toStormLottery(int roomId, String raffleId) {
		LotteryStorm.toLottery(roomId, raffleId);
	}
	
	/**
	 * 小电视抽奖
	 * @param roomId
	 * @param raffleId
	 * @return
	 */
	public static void toTvLottery(int roomId, String raffleId) {
		if(NumUtils.toInt(raffleId, -1) <= 0) {
			LotteryTV.toLottery(roomId);
			
		} else {
			LotteryTV.toLottery(roomId, raffleId);
		}
	}
	
	/**
	 * 高能礼物抽奖
	 * @param roomId
	 * @return
	 */
	public static void toEgLottery(int roomId) {
		LotteryEnergy.toLottery(roomId);
	}
	
	/**
	 * 投喂主播
	 * @param cookie 投喂用户cookie
	 * @param roomId 房间号
	 */
	public static void toFeed(BiliCookie cookie, int roomId) {
		if(cookie.TASK_STATUS().isFinFeed()) {
			return;
		}
		
		// 查询持有的所有礼物（包括银瓜子可以兑换的辣条数）
		List<BagGift> allGifts = Gifts.queryBagList(cookie, roomId);
		int silver = Gifts.querySilver(cookie);
		int giftNum = silver / Gift.HOT_STRIP.COST();
		if(giftNum > 0) {	// 银瓜子转换为虚拟的永久辣条
			BagGift bagGift = new BagGift(
					Gift.HOT_STRIP.ID(), Gift.HOT_STRIP.NAME(), giftNum);
			allGifts.add(bagGift);
		}
		
		// 查询用户当前持有的勋章
		Map<Integer, Medal> medals = Gifts.queryMedals(cookie);
		Medal medal = medals.get(RoomMgr.getInstn().getRealRoomId(roomId));
		
		// 筛选可以投喂的礼物列表
		List<BagGift> feedGifts = filterGifts(cookie, allGifts, medal);
		
		// 投喂主播
		User up = Other.queryUpInfo(roomId);
		Gifts.feed(cookie, roomId, up.ID(), feedGifts);
	}
	
	/**
	 * 过滤可投喂的礼物
	 * @param cookie 
	 * @param allGifts 当前持有的所有礼物
	 * @param medal 当前房间的勋章
	 * @return 可投喂的礼物列表
	 */
	private static List<BagGift> filterGifts(BiliCookie cookie, 
			List<BagGift> allGifts, Medal medal) {
		List<BagGift> feedGifts = new LinkedList<BagGift>();
		final long TOMORROW = TimeUtils.getZeroPointMillis() + TimeUtils.DAY_UNIT; // 今天24点之前
		
		// 对于已绑定手机或实名的账号，移除受保护礼物（即不投喂）
		if(cookie.isRealName() || 
				(cookie.isBindTel() && Config.getInstn().PROTECT_FEED())) {
			Iterator<BagGift> giftIts = allGifts.iterator();
			while(giftIts.hasNext()) {
				BagGift gift = giftIts.next();
				if(gift.getExpire() <= 0) {
					giftIts.remove(); 	// 永久礼物
					
				} else if(gift.getIntimacy() <= 0) {
					giftIts.remove(); 	// 亲密度<=0 的礼物(可能是某些活动礼物)
					
				} else if(Gift.B_CLOD.ID().equals(gift.getGiftId()) && 
						gift.getExpire() > TOMORROW) {
					giftIts.remove();	// 未过期的B坷垃
				}
			}
		}
		
		// 用户没有持有当前投喂的房间的勋章
		if(medal == null) {
			
			// 检查是否持有B坷垃
			BagGift bClod = null;
			for(BagGift gift : allGifts) {
				if(Gift.B_CLOD.ID().equals(gift.getGiftId())) {
					bClod = gift;
					bClod.setGiftNum(1);
					break;
					
				} else {
					feedGifts.add(gift);	// 当没有B坷垃时, 默认所有礼物均可投喂
				}
			}
			
			// 若持有B坷垃，则先只投喂1个B坷垃(下一轮自动投喂再根据亲密度选择礼物)
			if(bClod != null) {
				feedGifts.clear();
				feedGifts.add(bClod);
			}
			
		// 用户持有当前投喂的房间的勋章, 则需筛选礼物
		} else {
			int todayIntimacy = medal.getDayLimit() - medal.getTodayFeed();	// 今天可用亲密度
			for(BagGift gift : allGifts) {
				
				if(gift.getIntimacy() <= 0) {
					log.error("未登记的礼物, 不投喂: {}", gift.getGiftName());
					continue;
				}
				
				// 今天内到期的礼物, 全部选择(无视亲密度和实名)
				if(gift.getExpire() > 0 && gift.getExpire() <= TOMORROW) {
					feedGifts.add(gift);
					todayIntimacy -= gift.getIntimacy() * gift.getGiftNum();
					
				// 在不溢出亲密度的前提下选择礼物
				} else {
					int num = todayIntimacy / gift.getIntimacy();
					num = (num > gift.getGiftNum() ? gift.getGiftNum() : num);
					if(num > 0) {
						gift.setGiftNum(num);
						feedGifts.add(gift);
						todayIntimacy -= gift.getIntimacy() * num;
					}
				}
			}
			
			if(todayIntimacy <= 0) {
				cookie.TASK_STATUS().markFeed();
			}
		}
		return feedGifts;
	}
	
	/**
	 * 扭蛋
	 * @param cookie
	 */
	public static void toCapsule(BiliCookie cookie) {
		final int MAX_COIN = 100; // 每次打开扭蛋上限
		int coin = Gifts.queryCapsuleCoin(cookie);
		
		// 满100个扭蛋币才执行, 可提高奖品质量
		while(coin >= MAX_COIN) {
			boolean isOk = Gifts.openCapsuleCoin(cookie, MAX_COIN);
			if(isOk == false) {
				break;
			}
			coin -= MAX_COIN;
		}
	}
	
	/**
	 * 领取已完成的成就奖励
	 * @param cookie
	 */
	public static void toAchieve(BiliCookie cookie) {
		List<Achieve> achieves = Other.queryAchieve(cookie);
		Other.doAchieve(cookie, achieves);
	}
	
	/**
	 * 发送弹幕消息到当前监听的直播间
	 * @param msg 弹幕消息
	 * @return
	 */
	public static boolean sendDanmu(String msg) {
		Colors color = Colors.RANDOM();
		return sendDanmu(msg, color);
	}
	
	/**
	 * 发送弹幕消息到当前监听的直播间
	 * @param msg 弹幕消息
	 * @param color 弹幕颜色
	 * @return
	 */
	public static boolean sendDanmu(String msg, Colors color) {
		BiliCookie cookie = CookiesMgr.MAIN();
		int roomId = UIUtils.getLiveRoomId();
		return Chat.sendDanmu(cookie, roomId, msg, color);
	}
	
	/**
	 * 使用指定账号发送弹幕消息到当前监听的直播间
	 * @param cookie 发送弹幕的账号
	 * @param msg 弹幕消息
	 * @param color 弹幕颜色
	 * @return
	 */
	public static boolean sendDanmu(BiliCookie cookie, String msg) {
		int roomId = UIUtils.getLiveRoomId();
		Colors color = Colors.RANDOM();
		return Chat.sendDanmu(cookie, roomId, msg, color);
	}
	
	/**
	 * 发送弹幕消息
	 * @param msg 弹幕消息
	 * @param roomId 接收弹幕的直播间
	 * @return
	 */
	public static boolean sendDanmu(String msg, int roomId) {
		BiliCookie cookie = CookiesMgr.MAIN();
		Colors color = Colors.RANDOM();
		return Chat.sendDanmu(cookie, roomId, msg, color);
	}
	
	/**
	 * 发送私信
	 * @param recvId 接收账号的用户ID
	 * @param msg 私信消息
	 * @return
	 */
	public static boolean sendPM(String recvId, String msg) {
		BiliCookie cookie = CookiesMgr.MAIN();
		return Chat.sendPM(cookie, recvId, msg);
	}
	
}
