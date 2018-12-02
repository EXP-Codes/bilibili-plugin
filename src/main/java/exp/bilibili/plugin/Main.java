package exp.bilibili.plugin;

import exp.bilibili.plugin.ui.AppUI;
import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.LogUtils;
import exp.libs.utils.time.TimeUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;

/**
 * <PRE>
 * 程序入口
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Main {
	
	/**
	 * 程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		LogUtils.loadLogBackConfig();
		BeautyEyeUtils.init();
		Config.getInstn();
		
		if(OSUtils.getStartlock(2333)) {
			if(TimeUtils.isCalibrated(3600000)) {
				AppUI.createInstn(args);
				
			} else {
				System.err.println("本地时间异常，请校准北京时间再启动");
				SwingUtils.warn("本地时间异常，请校准北京时间再启动");
			}
		} else {
			System.err.println("禁止重复启动");
			SwingUtils.warn("禁止重复启动");
		}
	}
	
}
