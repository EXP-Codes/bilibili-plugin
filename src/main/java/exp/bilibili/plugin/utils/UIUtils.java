package exp.bilibili.plugin.utils;

import java.awt.Toolkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.plugin.ui.AppUI;
import exp.bilibili.plugin.ui._NoticeUI;
import exp.libs.envm.Colors;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.SwingUtils;

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
		AppUI.getInstn().toConsole(msg);
	}
	
	public static void chat(Object... msgs) {
		chat(StrUtils.concat(msgs));
	}
	
	public static void chat(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		AppUI.getInstn().toChat(msg);
	}
	
	public static void notify(Object... msgs) {
		notify(StrUtils.concat(msgs));
	}
	
	public static void notify(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		AppUI.getInstn().toNotify(msg);
	}
	
	public static void statistics(Object... msgs) {
		statistics(StrUtils.concat(msgs));
	}
	
	public static void statistics(String msg) {
		msg = StrUtils.concat(TimeUtils.getCurTime(), msg);
		AppUI.getInstn().toStatistics(msg);
	}
	
	public static void updateLotteryCnt() {
		AppUI.getInstn().updateLotteryCnt(1);
	}
	
	public static void updateLotteryCnt(int num) {
		AppUI.getInstn().updateLotteryCnt(num);
	}
	
	public static void markLogin(String username) {
		AppUI.getInstn().markLogin(username);
	}
	
	public static boolean isLogined() {
		return AppUI.getInstn().isLogined();
	}
	
	public static void updateAppTitle(String certificateTime) {
		AppUI.getInstn().updateTitle(certificateTime);
	}
	
	public static void printVersionInfo() {
		AppUI.getInstn().printVersionInfo();
	}
	
	public static String getCurLiveURL() {
		return AppUI.getInstn().getLiveUrl();
	}
	
	public static int getLiveRoomId() {
		return AppUI.getInstn().getLiveRoomId();
	}
	
	public static boolean isOnlyFreeze() {
		return AppUI.getInstn().isOnlyFreeze();
	}
	
	public static HotLiveRange getHotLiveRange() {
		return AppUI.getInstn().getHotLiveRange();
	}
	
	public static int getLotteryProbability() {
		return AppUI.getInstn().getLotteryProbability();
	}
	
	public static long getReactionTime() {
		return AppUI.getInstn().getReactionTime();
	}
	
	public static long getIntervalTime() {
		return AppUI.getInstn().getIntervalTime();
	}
	
	public static Colors getCurChatColor() {
		return AppUI.getInstn().getCurChatColor();
	}
	
	public static void notityLive(int roomId) {
		new _NoticeUI(roomId)._view();		// 右下角通知提示
		Toolkit.getDefaultToolkit().beep();	// 蜂鸣音提示
	}
	
	public static void notityExit(String msg) {
		SwingUtils.warn(msg);
		System.exit(0);
	}
	
}
