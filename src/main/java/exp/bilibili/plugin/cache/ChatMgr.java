package exp.bilibili.plugin.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.envm.Identity;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.XHRSender;
import exp.bilibili.protocol.bean.ws.ChatMsg;
import exp.bilibili.protocol.bean.ws.SendGift;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.RandomUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.thread.LoopThread;

/**
 * <PRE>
 * 在线聊天管理器:
 *  1.自动晚安
 *  2.自动感谢投喂
 *  3.定时公告
 *  4.举报/禁言等命令检测
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ChatMgr extends LoopThread {

	private final static Logger log = LoggerFactory.getLogger(ChatMgr.class);
	
	/** 被其他人联名举报上限: 超过上限则临时关小黑屋1小时 */
	private final static int COMPLAINT_LIMIT = 3;
	
	/** 禁言关键字 */
	private final static String BAN_KEY = "#禁言";
	
	/** 举报关键字 */
	private final static String COMPLAINT_KEY = "#举报";
	
	/** 同屏可以显示的最大发言数 */
	private final static int SCREEN_CHAT_LIMT = 10;
	
	private final static String WARN_KEY = "【警告】";
	
	private final static String NOTICE_KEY = "【公告】";
	
	private final static String NIGHT_KEY = "晚安(´▽`)ﾉ  ";
	
	/** 同一时间可以感谢的最大用户数（避免刷屏） */
	private final static int THX_USER_LIMIT = 2;
	
	/** 发送消息间隔 */
	private final static long SEND_TIME = 500;
	
	/** 自动感谢周期 */
	private final static long THX_TIME = 30000;
	
	/** 滚屏公告周期 */
	private final static long NOTICE_TIME = 300000;
	
	/** 检测待发送消息间隔 */
	private final static long SLEEP_TIME = 1000;
	
	private final static int THX_LIMIT = (int) (THX_TIME / SLEEP_TIME);
	
	private final static int NOTICE_LIMIT = (int) (NOTICE_TIME / SLEEP_TIME);
	
	private int thxCnt;
	
	private int noticeCnt;
	
	/** 自动答谢 */
	private boolean autoThankYou;
	
	/** 自动公告 */
	private boolean autoNotice;
	
	/** 自动晚安 */
	private boolean autoGoodNight;
	
	/** 已经被晚安过的用户 */
	private Set<String> nightedUsers;
	
	/**
	 * 一段时间内，每个用户赠送的礼物清单.
	 *  username -> giftName -> giftName
	 */
	private Map<String, Map<String, Integer>> userGifts;
	
	/**
	 * 发言计数器(主要针对定时公告和自动打call)
	 * 	当同屏存在自己的发言时，则取消本次自动发言，避免刷屏.
	 */
	private int chatCnt;
	
	private static volatile ChatMgr instance;
	
	private ChatMgr() {
		super("自动发言姬");
		this.thxCnt = 0;
		this.noticeCnt = 0;
		this.chatCnt = SCREEN_CHAT_LIMT;
		this.autoThankYou = false;
		this.autoNotice = false;
		this.autoGoodNight = false;
		this.nightedUsers = new HashSet<String>();
		this.userGifts = new LinkedHashMap<String, Map<String, Integer>>();
	}
	
	public static ChatMgr getInstn() {
		if(instance == null) {
			synchronized (ChatMgr.class) {
				if(instance == null) {
					instance = new ChatMgr();
				}
			}
		}
		return instance;
	}
	
	private void clear() {
		nightedUsers.clear();
		userGifts.clear();
	}
	
	@Override
	protected void _before() {
		log.info("{} 已启动", getName());
	}

	@Override
	protected void _loopRun() {
		
		// 自动感谢礼物投喂
		if(thxCnt++ >= THX_LIMIT) {
			thxCnt = 0;
			toThxGift();
		}
		
		// 定时公告
		if(noticeCnt++ >= NOTICE_LIMIT && allowAutoChat()) {
			noticeCnt = 0;
			toNotice();
		}
		
		_sleep(SLEEP_TIME);
	}

	@Override
	protected void _after() {
		clear();
		log.info("{} 已停止", getName());
	}
	
	/**
	 * 开播打招呼
	 * @param roomId
	 */
	public void helloLive(int roomId) {
		if(UIUtils.isLogined() == false) {
			return;
		}
		
		String card = RandomUtils.genElement(MsgKwMgr.getCards());
		String msg = "滴~".concat(card);
		
		int hour = TimeUtils.getCurHour(8);	// 中国8小时时差
		if(hour >= 6 && hour < 12) {
			msg = msg.concat("早上好");
			
		} else if(hour >= 12 && hour < 18) {
			msg = msg.concat("下午好");
			
		} else if(hour >= 18 && hour < 24) {
			msg = msg.concat("晚上好");
			
		} else {
			msg = msg.concat("还在浪吗?");
		}
		XHRSender.sendDanmu(msg, roomId);
	}
	
	/**
	 * 房间内高能礼物感谢与中奖祝贺
	 * @param msg
	 * @return
	 */
	public boolean sendThxEnergy(String msg) {
		boolean isOk = false;
		if(isAutoThankYou()) {
			isOk = XHRSender.sendDanmu(StrUtils.concat(NOTICE_KEY, msg), 
					UIUtils.getCurChatColor());
		}
		return isOk;
	}
	
	/**
	 * 感谢上船
	 * @param msg
	 */
	public void sendThxGuard(String msg) {
		if(!isAutoThankYou()) {
			return;
		}
		
		XHRSender.sendDanmu(StrUtils.concat(NOTICE_KEY, "感谢", msg), 
				UIUtils.getCurChatColor());
	}
	
	/**
	 * 添加到投喂感谢列表
	 * @param msgBean
	 */
	public void addThxGift(SendGift msgBean) {
		if(!isAutoThankYou() || msgBean.getNum() <= 0) {
			return;
		}
		
		String username = msgBean.getUname();
		String giftName = msgBean.getGiftName();
		
		synchronized (userGifts) {
			Map<String, Integer> gifts = userGifts.get(username);
			if(gifts == null) {
				gifts = new HashMap<String, Integer>();
				userGifts.put(username, gifts);
			}
			
			Integer sum = gifts.get(giftName);
			sum = (sum == null ? 0 : sum);
			gifts.put(giftName, (sum + msgBean.getNum()));
		}
	}
	
	/**
	 * 感谢一段时间内所有用户的投喂
	 */
	private void toThxGift() {
		Map<String, Map<String, Integer>> tmp = 
				new LinkedHashMap<String, Map<String,Integer>>();
		synchronized (userGifts) {
			tmp.putAll(userGifts);
			userGifts.clear();
		}
		
		// 若短时间内投喂用户过多, 则不逐一感谢, 避免刷屏
		int userNum = tmp.keySet().size();
		if(userNum > THX_USER_LIMIT) {
			String msg = StrUtils.concat(NOTICE_KEY, "感谢前面[", userNum, 
					"]个大佬的投喂d(´ω｀*)");
			XHRSender.sendDanmu(msg);
			
		// 分别合并每个用户的投喂礼物再感谢
		} else {
			Iterator<String> userIts = tmp.keySet().iterator();
			while(userIts.hasNext()) {
				String username = userIts.next();
				Map<String, Integer> gifts = tmp.get(username);
				
				toThxGift(username, gifts);
				_sleep(SEND_TIME);
				
				userIts.remove();
			}
			tmp.clear();
		}
	}
	
	/**
	 * 感谢某个用户的投喂
	 * @param username
	 * @param gifts
	 */
	private void toThxGift(String username, Map<String, Integer> gifts) {
		if(gifts.size() <= 0) {
			return;
			
		// 1个礼物多份
		} else if(gifts.size() == 1) {
			Iterator<String> giftIts = gifts.keySet().iterator();
			if(giftIts.hasNext()) {
				String giftName = giftIts.next();
				Integer num = gifts.get(giftName);
				if(num != null && num > 0) {
					int cost = ActivityMgr.showCost(giftName, num);
					String msg = getThxMsg(username, giftName, num, cost);
					XHRSender.sendDanmu(msg);
				}
			}
			
		// 多个礼物多份
		} else {
			int cost = 0;
			StringBuilder sb = new StringBuilder();
			Iterator<String> giftIts = gifts.keySet().iterator();
			while(giftIts.hasNext()) {
				String giftName = giftIts.next();
				sb.append(giftName).append(",");
				cost += ActivityMgr.showCost(giftName, gifts.get(giftName));
			}
			sb.setLength(sb.length() - 1);
			
			String msg = getThxMsg(username, sb.toString(), -1, cost);
			XHRSender.sendDanmu(msg);
		}
		
		gifts.clear();
	}
	
	private String getThxMsg(String username, String gift, int num, int cost) {
		String head = StrUtils.concat(NOTICE_KEY, "感谢[", username, "]");
		String tail = "";
		if(num > 0) {
			tail = StrUtils.concat("投喂", gift, "x", num);
		} else {
			tail = StrUtils.concat("投喂[", gift, "]");
		}
		
		String adj = "";
		int len = CookiesMgr.MAIN().DANMU_LEN() - head.length() - tail.length();
		for(int retry = 0; retry < 3; retry++) {
			adj = MsgKwMgr.getAdv();
			if(len >= adj.length()) {
				break;
			}
		}
		return StrUtils.concat(head, adj, tail);
	}
	
	/**
	 * 定时公告
	 */
	private void toNotice() {
		if(!isAutoNotice() || ListUtils.isEmpty(MsgKwMgr.getNotices())) {
			return;
		}
		
		String msg = NOTICE_KEY.concat(
				RandomUtils.genElement(MsgKwMgr.getNotices()));
		XHRSender.sendDanmu(msg);
	}
	
	/**
	 * 分析弹幕内容, 触发不同的响应机制
	 * @param chatMsg
	 */
	public void analyseDanmu(ChatMsg chatMsg) {
		if(UIUtils.isLogined() == false) {
			return;
		}
		
		countChatCnt(chatMsg.getUsername());	// 登陆用户发言计数器
		toNight(chatMsg.getUsername(), chatMsg.getMsg());	// 自动晚安
		complaint(chatMsg.getUsername(), chatMsg.getMsg());	// 举报处理
		ban(chatMsg.getUsername(), chatMsg.getMsg());	// 禁言处理
	}
	
	/**
	 * 计算登陆用户的发言次数
	 * @param username 当前发言用户
	 */
	private void countChatCnt(String username) {
		
		// 当是登陆用户发言时, 清空计数器
		if(CookiesMgr.MAIN().NICKNAME().equals(username)) {
			chatCnt = 0;
			
		// 当是其他用户发言时, 计数器+1
		} else {
			chatCnt++;
		}
	}
	
	/**
	 * 自动晚安
	 * @param username
	 * @param msg
	 */
	private void toNight(String username, String msg) {
		if(!isAutoGoodNight() || 
				msg.startsWith(NIGHT_KEY) ||		// 避免跟机器人对话
				nightedUsers.contains(username)) { 	// 避免重复晚安
			return;
		}
		
		if(MsgKwMgr.containsNight(msg)) {
			String chatMsg = StrUtils.concat(NIGHT_KEY, ", ", username);
			XHRSender.sendDanmu(chatMsg, UIUtils.getCurChatColor());
			nightedUsers.add(username);
		}
	}
	
	/**
	 * 弹幕举报.
	 * 	借登陆用户的权限执法, 登陆用户必须是当前直播间的主播或房管.
	 * @param username 举报人
	 * @param msg 弹幕（消息含被举报人）
	 */
	private void complaint(String username, String msg) {
		if(Identity.less(Identity.ADMIN) || 
				!CookiesMgr.MAIN().isRoomAdmin() || 
				!msg.trim().startsWith(COMPLAINT_KEY)) {
			return;
		}
		
		String accuser = username;
		String unameKey = RegexUtils.findFirst(msg, COMPLAINT_KEY.concat("\\s*(.+)")).trim();
		List<String> accuseds = OnlineUserMgr.getInstn().findOnlineUser(unameKey);
		if(accuseds.size() <= 0) {
			log.warn("用户 [{}] 举报失败: 不存在关键字为 [{}] 的账号", accuser, unameKey);
			
		} else if(accuseds.size() > 1) {
			log.warn("用户 [{}] 举报失败: 关键字为 [{}] 的账号有多个", accuser, unameKey);
			
		} else {
			String accused = accuseds.get(0);
			int cnt = OnlineUserMgr.getInstn().complaint(accuser, accused);
			if(cnt > 0) {
				if(cnt < COMPLAINT_LIMIT) {
					msg = StrUtils.concat(WARN_KEY, "x", cnt, ":请[", accused, "]注意弹幕礼仪");
					
				} else if(XHRSender.blockUser(accused)) {
					OnlineUserMgr.getInstn().cancel(accused);
					msg = StrUtils.concat(WARN_KEY, "[", accused, "]被", cnt, "人举报,暂时禁言");
				}
				XHRSender.sendDanmu(msg);
				
			} else {
				log.warn("用户 [{}] 举报失败: 请勿重复举报 [{}]", accuser, accused);
			}
		}
	}
	
	/**
	 * 把指定用户关小黑屋.
	 *  借登陆用户的权限执法, 登陆用户必须是当前直播间的主播或房管.
	 * @param username 举报人名称（只能是房管）
	 * @param msg 弹幕（消息含被禁闭人）
	 */
	private void ban(String username, String msg) {
		if(Identity.less(Identity.ADMIN) || 
				!CookiesMgr.MAIN().isRoomAdmin() || 
				!OnlineUserMgr.getInstn().isManager(username) || 
				!msg.trim().startsWith(BAN_KEY)) {
			return;
		}
		
		String managerId = OnlineUserMgr.getInstn().getManagerID(username);
		String unameKey = RegexUtils.findFirst(msg, BAN_KEY.concat("\\s*(.+)")).trim();
		List<String> accuseds = OnlineUserMgr.getInstn().findOnlineUser(unameKey);
		
		if(accuseds.size() <= 0) {
			msg = StrUtils.concat("【禁言失败】 不存在关键字为 [", unameKey, "] 的用户");
			
		} else if(accuseds.size() > 1) {
			msg = StrUtils.concat("【禁言失败】 关键字为 [", unameKey, "] 的用户有 [", accuseds.size(), 
					"] 个, 请确认其中一个用户再执行禁言: ");
			for(String accused : accuseds) {
				msg = StrUtils.concat(msg, "[", accused, "] ");
			}
		} else {
			String accused = accuseds.get(0);
			if(OnlineUserMgr.getInstn().isManager(accused)) {
				msg = StrUtils.concat("【禁言失败】 用户 [", accused, "] 是主播/管理员");
				
			} else if(XHRSender.blockUser(accused)) {
				msg = StrUtils.concat("【禁言成功】 用户 [", accused, "] 已暂时关到小黑屋1小时");
				
			} else {
				msg = StrUtils.concat("【禁言失败】 用户 [", accused, "] 已被其他房管拖到小黑屋不可描述了");
			}
		}
		XHRSender.sendPM(managerId, msg);
	}
	
	public void setAutoThankYou() {
		autoThankYou = !autoThankYou;
		userGifts.clear();	// 切换状态时, 清空已投喂的礼物列表
	}
	
	public boolean isAutoThankYou() {
		return autoThankYou;
	}
	
	public void setAutoNotice() {
		autoNotice = !autoNotice;
		chatCnt = SCREEN_CHAT_LIMT;
	}
	
	public boolean isAutoNotice() {
		return autoNotice;
	}
	
	public void setAutoGoodNight() {
		autoGoodNight = !autoGoodNight;
		nightedUsers.clear();	// 切换状态时, 清空已晚安的用户列表
	}
	
	public boolean isAutoGoodNight() {
		return autoGoodNight;
	}
	
	/**
	 * 是否允许自动发言:
	 * 	当距离上一次发言超过同屏显示限制时，则允许自动发言
	 * @return
	 */
	private boolean allowAutoChat() {
		return chatCnt >= SCREEN_CHAT_LIMT;
	}
	
}
