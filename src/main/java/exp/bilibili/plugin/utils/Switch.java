package exp.bilibili.plugin.utils;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.StormScanner;
import exp.bilibili.plugin.ui.AppUI;

/**
 * <PRE>
 * 全局开关类
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Switch {

	protected Switch() {}
	
	/**
	 * 是否参加全平台抽奖(含小电视、高能、摩天大楼)
	 * @return
	 */
	public static boolean isJoinLottery() {
		boolean isJoin = true;
		if(Config.USE_UI()) {
			isJoin = AppUI.getInstn().isJoinLottery();
		}
		return isJoin;
	}
	
	/**
	 * 是否参加节奏风暴抽奖
	 * @return
	 */
	public static boolean isJoinStorm() {
		boolean isJoin = true;
		if(Config.USE_UI()) {
			isJoin = StormScanner.getInstn().isScan();
		}
		return isJoin;
	}
	
	/**
	 * 是否参加舰队抽奖
	 * @return
	 */
	public static boolean isJoinGuard() {
		boolean isJoin = true;
		if(Config.USE_UI()) {
			isJoin = StormScanner.getInstn().isScan();
		}
		return isJoin;
	}
	
	/**
	 * 是否激活自动投喂
	 * @return
	 */
	public static boolean isAutoFeed() {
		boolean isAuto = false;
		if(Config.USE_UI()) {
			isAuto = AppUI.getInstn().isAutoFeed();
		}
		return isAuto;
	}
	
}
