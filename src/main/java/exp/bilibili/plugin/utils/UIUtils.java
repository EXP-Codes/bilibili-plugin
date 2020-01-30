package exp.bilibili.plugin.utils;

import java.awt.Toolkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.plugin.ui.AppUI;
import exp.bilibili.plugin.ui._NoticeUI;
import exp.libs.envm.Colors;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 界面工具类
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class UIUtils {

	private final static Logger log = LoggerFactory.getLogger(UIUtils.class);
	
	protected UIUtils() {}
	
	public static void log(Object... msgs) {
		log(StrUtils.concat(msgs));
	}
	
	public static void log(String msg) {
		log.info(msg);
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		if(Config.USE_UI()) {
			AppUI.getInstn().toConsole(msg);
		}
	}
	
	public static void chat(Object... msgs) {
		chat(StrUtils.concat(msgs));
	}
	
	public static void chat(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		if(Config.USE_UI()) {
			AppUI.getInstn().toChat(msg);
		}
	}
	
	public static void notify(Object... msgs) {
		notify(StrUtils.concat(msgs));
	}
	
	public static void notify(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		if(Config.USE_UI()) {
			AppUI.getInstn().toNotify(msg);
		}
	}
	
	public static void statistics(Object... msgs) {
		statistics(StrUtils.concat(msgs));
	}
	
	public static void statistics(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		if(Config.USE_UI()) {
			AppUI.getInstn().toStatistics(msg);
		}
	}
	
	public static void updateLotteryCnt() {
		updateLotteryCnt(1);
	}
	
	public static void updateLotteryCnt(int num) {
		if(Config.USE_UI()) {
			AppUI.getInstn().updateLotteryCnt(num);
		}
	}
	
	public static void markLogin(String username) {
		if(Config.USE_UI()) {
			AppUI.getInstn().markLogin(username);
		}
	}
	
	public static boolean isLogined() {
		boolean isLogined = false;
		if(Config.USE_UI()) {
			isLogined = AppUI.getInstn().isLogined();
		}
		return isLogined;
	}
	
	public static void updateAppTitle(String certificateTime) {
		if(Config.USE_UI()) {
			AppUI.getInstn().updateTitle(certificateTime);
		}
	}
	
	public static void printVersionInfo() {
		if(Config.USE_UI()) {
			AppUI.getInstn().printVersionInfo();
		}
	}
	
	public static String getCurLiveURL() {
		String url = StrUtils.concat(Config.getInstn().LIVE_HOME(), Config.getInstn().SIGN_ROOM_ID());
		if(Config.USE_UI()) {
			url = AppUI.getInstn().getLiveUrl();
		}
		return url;
	}
	
	public static int getLiveRoomId() {
		int roomId = Config.getInstn().SIGN_ROOM_ID();
		if(Config.USE_UI()) {
			roomId = AppUI.getInstn().getLiveRoomId();
		}
		return roomId;
	}
	
	public static boolean isOnlyFreeze() {
		boolean isOnlyFreeze = true;
		if(Config.USE_UI()) {
			isOnlyFreeze = AppUI.getInstn().isOnlyFreeze();
		}
		return isOnlyFreeze;
	}
	
	public static HotLiveRange getHotLiveRange() {
		HotLiveRange range = new HotLiveRange(2, 3);
		if(Config.USE_UI()) {
			range = AppUI.getInstn().getHotLiveRange();
		}
		return range;
	}
	
	public static int getLotteryProbability() {
		int probability = 100;
		if(Config.USE_UI()) {
			probability = AppUI.getInstn().getLotteryProbability();
		}
		return probability;
	}
	
	public static long getReactionTime() {
		long reactionTime = Config.getInstn().REACTION_TIME();
		if(Config.USE_UI()) {
			reactionTime = AppUI.getInstn().getReactionTime();
		}
		return reactionTime;
	}
	
	public static long getIntervalTime() {
		long intervalTime = Config.getInstn().INTERVAL_TIME();
		if(Config.USE_UI()) {
			intervalTime = AppUI.getInstn().getIntervalTime();
		}
		return intervalTime;
	}
	
	public static Colors getCurChatColor() {
		Colors color = Colors.WHITE;
		if(Config.USE_UI()) {
			color = AppUI.getInstn().getCurChatColor();
		}
		return color;
	}
	
	public static void notityLive(int roomId) {
		if(Config.USE_UI()) {
			new _NoticeUI(roomId)._view();		// 右下角通知提示
			Toolkit.getDefaultToolkit().beep();	// 蜂鸣音提示
		}
	}
	
	public static void notityExit(String msg) {
		if(Config.USE_UI()) {
			SwingUtils.warn(msg);
			System.exit(0);
		}
	}
	
}
