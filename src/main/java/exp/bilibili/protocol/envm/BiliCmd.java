package exp.bilibili.protocol.envm;


/**
 * <PRE>
 * B站Json交互报文命令类型
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BiliCmd {
	
	/** 未知消息 */
	public final static BiliCmd UNKNOW = new BiliCmd("UNKNOW");
	
	/** 通知消息（这种消息根据不同的类型可转换成不同的消息） */
	private final static String _NOTICE_MSG = "NOTICE_MSG";
	public final static BiliCmd NOTICE_MSG = new BiliCmd(_NOTICE_MSG);
	
	/** 欢迎老爷消息 */
	private final static String _WELCOME = "WELCOME";
	public final static BiliCmd WELCOME = new BiliCmd(_WELCOME);
	
	/** 欢迎船员消息 */
	private final static String _WELCOME_GUARD = "WELCOME_GUARD";
	public final static BiliCmd WELCOME_GUARD = new BiliCmd(_WELCOME_GUARD);
	
	/** 船员进入直播间特效消息 */
	private final static String _ENTRY_EFFECT = "ENTRY_EFFECT";
	public final static BiliCmd ENTRY_EFFECT = new BiliCmd(_ENTRY_EFFECT);
	
	/** (直播间内)新船员上船消息 */
	private final static String _GUARD_BUY = "GUARD_BUY";
	public final static BiliCmd GUARD_BUY = new BiliCmd(_GUARD_BUY);
	
	/** (直播间内)新船员上船抽奖通知 */
	private final static String _GUARD_LOTTERY_START = "GUARD_LOTTERY_START";
	public final static BiliCmd GUARD_LOTTERY_START = new BiliCmd(_GUARD_LOTTERY_START);
	
	/** (全频道)总督登船消息 */
	private final static String _GUARD_MSG = "GUARD_MSG";
	public final static BiliCmd GUARD_MSG = new BiliCmd(_GUARD_MSG);
	
	/** 系统消息：小电视抽奖、全频道公告等 */
	private final static String _SYS_MSG = "SYS_MSG";
	public final static BiliCmd SYS_MSG = new BiliCmd(_SYS_MSG);
	
	/** 系统礼物: 高能抽奖、高能公告等 */
	private final static String _SYS_GIFT = "SYS_GIFT";
	public final static BiliCmd SYS_GIFT = new BiliCmd(_SYS_GIFT);
	
	/** 投喂消息 */
	private final static String _SEND_GIFT = "SEND_GIFT";
	public final static BiliCmd SEND_GIFT = new BiliCmd(_SEND_GIFT);
	
	/** 弹幕消息 */
	private final static String _DANMU_MSG = "DANMU_MSG";
	public final static BiliCmd DANMU_MSG = new BiliCmd(_DANMU_MSG);
	
	/** 开播通知消息 */
	private final static String _LIVE = "LIVE";
	public final static BiliCmd LIVE = new BiliCmd(_LIVE);
	
	/** 关播通知消息 */
	private final static String _PREPARING = "PREPARING";
	public final static BiliCmd PREPARING = new BiliCmd(_PREPARING);
	
	/** 关播通知消息 */
	private final static String _ROOM_SILENT_OFF = "ROOM_SILENT_OFF";
	public final static BiliCmd ROOM_SILENT_OFF = new BiliCmd(_ROOM_SILENT_OFF);
	
	/** 特殊礼物：(直播间内)节奏风暴消息 */
	private final static String _SPECIAL_GIFT = "SPECIAL_GIFT";
	public final static BiliCmd SPECIAL_GIFT = new BiliCmd(_SPECIAL_GIFT);
	
	/** (直播间内)高能抽奖开始消息 */
	private final static String _RAFFLE_START = "RAFFLE_START";
	public final static BiliCmd RAFFLE_START = new BiliCmd(_RAFFLE_START);
	
	/** (直播间内)高能抽奖结束消息 */
	private final static String _RAFFLE_END = "RAFFLE_END";
	public final static BiliCmd RAFFLE_END = new BiliCmd(_RAFFLE_END);
	
	/** (直播间内)许愿瓶实现进度消息 */
	private final static String _WISH_BOTTLE = "WISH_BOTTLE";
	public final static BiliCmd WISH_BOTTLE = new BiliCmd(_WISH_BOTTLE);
	
	/** (直播间内)关小黑屋通知消息 */
	private final static String _ROOM_BLOCK_MSG = "ROOM_BLOCK_MSG";
	public final static BiliCmd ROOM_BLOCK_MSG = new BiliCmd(_ROOM_BLOCK_MSG);
	
	/** 2018春节活动-红灯笼高能通知事件 */
	private final static String _ACTIVITY_EVENT = "ACTIVITY_EVENT";
	public final static BiliCmd ACTIVITY_EVENT = new BiliCmd(_ACTIVITY_EVENT);
	
	/** 直播间小时榜排名通知消息 */
	private final static String _ROOM_RANK = "ROOM_RANK";
	public final static BiliCmd ROOM_RANK = new BiliCmd(_ROOM_RANK);
	
	/** 礼物combo连击消息 */
	private final static String _COMBO_SEND = "COMBO_SEND";
	public final static BiliCmd COMBO_SEND = new BiliCmd(_COMBO_SEND);
	
	/** 礼物combo连击结束消息 */
	private final static String _COMBO_END = "COMBO_END";
	public final static BiliCmd COMBO_END = new BiliCmd(_COMBO_END);
	
	/** 小电视中奖消息 */
	private final static String _TV_END = "TV_END";
	public final static BiliCmd TV_END = new BiliCmd(_TV_END);
	
	/** 凌晨 00:00 新一天的通知事件 */
	private final static String _DAILY_QUEST_NEWDAY = "DAILY_QUEST_NEWDAY";
	public final static BiliCmd DAILY_QUEST_NEWDAY = new BiliCmd(_DAILY_QUEST_NEWDAY);
	
	/** 红色活动横幅通知事件 */
	private final static String _ACTIVITY_BANNER_RED_NOTICE_CLOSE = "ACTIVITY_BANNER_RED_NOTICE_CLOSE";
	public final static BiliCmd ACTIVITY_BANNER_RED_NOTICE_CLOSE = new BiliCmd(_ACTIVITY_BANNER_RED_NOTICE_CLOSE);
	
	private String cmd;
	
	private BiliCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String CMD() {
		return cmd;
	}
	
	public static BiliCmd toCmd(String cmd) {
		BiliCmd biliCMD = UNKNOW;
		if(_NOTICE_MSG.equals(cmd)) {
			biliCMD = NOTICE_MSG;
			
		} else if(_WELCOME.equals(cmd)) {
			biliCMD = WELCOME;
			
		} else if(_WELCOME_GUARD.equals(cmd)) {
			biliCMD = WELCOME_GUARD;
			
		} else if(_ENTRY_EFFECT.equals(cmd)) {
			biliCMD = ENTRY_EFFECT;
			
		} else if(_GUARD_BUY.equals(cmd)) {
			biliCMD = GUARD_BUY;
			
		} else if(_GUARD_LOTTERY_START.equals(cmd)) {
			biliCMD = GUARD_LOTTERY_START;
			
		} else if(_GUARD_MSG.equals(cmd)) {
			biliCMD = GUARD_MSG;
			
		} else if(_SYS_MSG.equals(cmd)) {
			biliCMD = SYS_MSG;
			
		} else if(_SYS_GIFT.equals(cmd)) {
			biliCMD = SYS_GIFT;
			
		} else if(_SEND_GIFT.equals(cmd)) {
			biliCMD = SEND_GIFT;
			
		} else if(_DANMU_MSG.equals(cmd)) {
			biliCMD = DANMU_MSG;
		
		} else if(_LIVE.equals(cmd)) {
			biliCMD = LIVE;
			
		} else if(_PREPARING.equals(cmd)) {
			biliCMD = PREPARING;
			
		} else if(_ROOM_SILENT_OFF.equals(cmd)) {
			biliCMD = ROOM_SILENT_OFF;
			
		} else if(_SPECIAL_GIFT.equals(cmd)) {
			biliCMD = SPECIAL_GIFT;
			
		} else if(_RAFFLE_START.equals(cmd)) {
			biliCMD = RAFFLE_START;
			
		} else if(_RAFFLE_END.equals(cmd)) {
			biliCMD = RAFFLE_END;
			
		} else if(_WISH_BOTTLE.equals(cmd)) {
			biliCMD = WISH_BOTTLE;
			
		} else if(_ROOM_BLOCK_MSG.equals(cmd)) {
			biliCMD = ROOM_BLOCK_MSG;
			
		} else if(_ACTIVITY_EVENT.equals(cmd)) {
			biliCMD = ACTIVITY_EVENT;
			
		} else if(_ROOM_RANK.equals(cmd)) {
			biliCMD = ROOM_RANK;
			
		} else if(_COMBO_SEND.equals(cmd)) {
			biliCMD = COMBO_SEND;
			
		} else if(_COMBO_END.equals(cmd)) {
			biliCMD = COMBO_END;
		
		} else if(_TV_END.equals(cmd)) {
			biliCMD = TV_END;
		
		} else if(_DAILY_QUEST_NEWDAY.equals(cmd)) {
			biliCMD = DAILY_QUEST_NEWDAY;
			
		} else if(_ACTIVITY_BANNER_RED_NOTICE_CLOSE.equals(cmd)) {
			biliCMD = ACTIVITY_BANNER_RED_NOTICE_CLOSE;
			
		}
		return biliCMD;
	}
}
