package exp.bilibili.plugin;

import exp.bilibili.plugin.ui.AppUI;
import exp.libs.utils.other.LogUtils;
import exp.libs.warp.ui.BeautyEyeUtils;


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
public class BPMain {
	
	public static void main(String[] args) {
		LogUtils.loadLogBackConfig();
		Config.getInstn();
		
		BeautyEyeUtils.init();
		AppUI.createInstn(args);
	}
	
}
