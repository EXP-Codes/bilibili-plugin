package exp.bilibili.plugin.bean.ldm;

import java.util.Date;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.plugin.envm.Danmu;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.BoolUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.cookie.HttpCookie;

/**
 * <PRE>
 * B站账号的cookie集
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BiliCookie extends HttpCookie {

	/** NULL-cookie对象 */
	public final static BiliCookie NULL = new BiliCookie();
	
	/** B站CSRF标识 */
	private final static String CSRF_KEY = "bili_jct";
	
	/** B站用户ID标识 */
	private final static String UID_KEY = "DedeUserID";
	
	/** 自动投喂开关 */
	private final static String FEED_KEY = "AutoFeed";
	
	/** 自动投喂房间号标识 */
	private final static String RID_KEY = "RoomID";
	
	/** 登陆类型 */
	private CookieType type;
	
	/** 从cookies提取的有效期 */
	private Date expires;
	
	/** 从cookies提取的csrf token */
	private String csrf;
	
	/** 从cookies提取的用户ID */
	private String uid;
	
	/** 该cookie对应的用户昵称 */
	private String nickName;
	
	/** 是否已绑定手机 */
	private boolean isBindTel;
	
	/** 是否已实名认证 */
	private boolean isRealName;
	
	/** 是否为房管 */
	private boolean isRoomAdmin;
	
	/** 是否为老爷/年费老爷 */
	private boolean isVip;
	
	/** 是否为提督/总督 */
	private boolean isGuard;
	
	/** 自动投喂 */
	private boolean autoFeed;
	
	/** 投喂房间号 */
	private int feedRoomId;

	/** 标识日常任务的执行状态 */
	private TaskStatus taskStatus;
	
	/** 累计参与抽奖计数 */
	private int lotteryCnt;
	
	/** 下次抽奖时间 */
	private long nextLotteryTime;
	
	/** 是否被冻结抽奖 */
	private boolean freeze;
	
	public BiliCookie() {
		super();
	}
	
	public BiliCookie(String headerCookies) {
		super(headerCookies);
	}
	
	protected void init() {
		this.type = CookieType.UNKNOW;
		this.expires = new Date();
		this.csrf = "";
		this.uid = "";
		this.nickName = "";
		this.isBindTel = false;
		this.isRealName = false;
		this.isRoomAdmin = false;
		this.isVip = false;
		this.isGuard = false;
		this.autoFeed = false;
		this.feedRoomId = Config.getInstn().SIGN_ROOM_ID();
		this.taskStatus = new TaskStatus();
		this.lotteryCnt = 0;
		this.nextLotteryTime = 0L;
		this.freeze = false;
	}
	
	@Override
	protected boolean takeCookieNVE(String name, String value, Date expires) {
		boolean isKeep = true;
		if(CSRF_KEY.equalsIgnoreCase(name)) {
			this.csrf = value;
			
		} else if(UID_KEY.equalsIgnoreCase(name)) {
			this.uid = value;
			this.expires = expires;
			
		} else if(FEED_KEY.equals(name)) {
			this.autoFeed = BoolUtils.toBool(value, false);
			isKeep = false;	// 属于自定义的cookie属性, 不保持到cookie会话中(即不会发送到服务器)
			
		} else if(RID_KEY.equals(name)) {
			this.feedRoomId = NumUtils.toInt(value, 0);
			isKeep = false;	// 属于自定义的cookie属性, 不保持到cookie会话中(即不会发送到服务器)
		}
		return isKeep;
	}
	
	/**
	 * cookies是否有效
	 * @return true:有效; false:无效
	 */
	public boolean isVaild() {
		return (super.isVaild() && StrUtils.isNotEmpty(uid, nickName));
	}
	
	public CookieType TYPE() {
		return type;
	}
	
	public void setType(CookieType type) {
		this.type = type;
	}
	
	public Date EXPIRES() {
		return expires;
	}
	
	public String CSRF() {
		return csrf;
	}
	
	public String UID() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String NICKNAME() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public boolean isBindTel() {
		return isBindTel;
	}
	
	public void setBindTel(boolean isBindTel) {
		this.isBindTel = isBindTel;
	}

	public boolean isRealName() {
		return isRealName;
	}

	public void setRealName(boolean isRealName) {
		this.isRealName = isRealName;
	}

	public boolean isRoomAdmin() {
		return isRoomAdmin;
	}

	public void setRoomAdmin(boolean isRoomAdmin) {
		this.isRoomAdmin = isRoomAdmin;
	}
	
	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}
	
	public boolean isGuard() {
		return isGuard;
	}

	public void setGuard(boolean isGuard) {
		this.isGuard = isGuard;
	}
	
	public int DANMU_LEN() {
		return (isGuard ? Danmu.LEN_GUARD : (isVip ? Danmu.LEN_VIP : Danmu.LEN));
	}

	public boolean isAutoFeed() {
		return autoFeed;
	}

	public void setAutoFeed(boolean autoFeed) {
		this.autoFeed = autoFeed;
	}
	
	public int getFeedRoomId() {
		return feedRoomId;
	}

	public void setFeedRoomId(int feedRoomId) {
		this.feedRoomId = feedRoomId;
	}
	
	public TaskStatus TASK_STATUS() {
		return taskStatus;
	}
	
	/**
	 * 是否允许抽奖
	 * @return
	 */
	public boolean allowLottery() {
		boolean isOk = random();
		isOk &= frequency();
		isOk &= continuity();
		return isOk;
	}
	
	/**
	 * 以随机方式控制抽奖
	 * @return
	 */
	private boolean random() {
		int percent = UIUtils.getLotteryProbability();
		return BoolUtils.hit(percent);
	}

	/**
	 * 通过抽奖间隔控制高频抽奖
	 * @return
	 */
	private boolean frequency() {
		boolean isOk = false;
		long now = System.currentTimeMillis();
		if(now >= nextLotteryTime) {
			nextLotteryTime = now + UIUtils.getIntervalTime();
			freeze = false;
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 控制连续抽奖.
	 * 限制未实名账号连续抽奖 (B站严查未实名账号)
	 * @return
	 */
	private boolean continuity() {
		boolean isOk = true;
		if(isRealName() == false) {
			if(lotteryCnt >= Config.LOTTERY_LIMIT) {
				lotteryCnt = 0;
				isOk = false;
				
			} else {
				lotteryCnt++;
			}
		}
		return isOk;
	}
	
	/**
	 * 主动冻结抽奖一小时.
	 * 
	 * B站抓到脚本抽奖后，会突然在某个时间点突然解除冻结，
	 * 若这个时候马上去抽奖，就会马上被重新冻结（试探机制）
	 * 
	 * 因此当章后被B站冻结抽奖后，这边也主动冻结抽奖2小时，以回避这个试探机制。
	 */
	public void freeze() {
		// FIXME: 可配置
		nextLotteryTime = System.currentTimeMillis() + 7200000L;
		freeze = true;
	}
	
	public boolean isFreeze() {
		return freeze;
	}
	
	@Override
	public String toHeaderCookie() {
		return StrUtils.concat(super.toHeaderCookie(), 
				LFCR, FEED_KEY, "=", (isAutoFeed() ? "true" : "false"), 
				LFCR, RID_KEY, "=", getFeedRoomId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof BiliCookie)) {
			return false;
		}
		
		BiliCookie other = (BiliCookie) obj;
		return this.uid.equals(other.uid);
	}
	
	@Override
	public int hashCode() {
		return uid.hashCode();
	}
	
}
