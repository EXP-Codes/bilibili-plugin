package exp.bilibili.plugin.utils;

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
		return AppUI.getInstn().isJoinLottery();
	}
	
	/**
	 * 是否参加节奏风暴抽奖
	 * @return
	 */
	public static boolean isJoinStorm() {
		return StormScanner.getInstn().isScan();
	}
	
	/**
	 * 是否参加舰队抽奖
	 * @return
	 */
	public static boolean isJoinGuard() {
		return StormScanner.getInstn().isScan();
	}
	
	/**
	 * 是否激活自动投喂
	 * @return
	 */
	public static boolean isAutoFeed() {
		return AppUI.getInstn().isAutoFeed();
	}
	
}
