package exp.bilibili.plugin.utils;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.os.OSUtils;

public class SwingUtils extends exp.libs.warp.ui.SwingUtils {

	private final static Logger log = LoggerFactory.getLogger(SwingUtils.class);
	
	/**
	 * 信息弹窗
	 * @param msg 普通消息
	 */
	public static void info(String msg) {
		if(OSUtils.isWin()) {
			JOptionPane.showMessageDialog(
				    null, msg, "INFO", JOptionPane.INFORMATION_MESSAGE);
		} else {
			log.info(msg);
		}
	}
	
	/**
	 * 警告弹窗
	 * @param msg 警告消息
	 */
	public static void warn(String msg) {
		if(OSUtils.isWin()) {
			JOptionPane.showMessageDialog(
				    null, msg, "WARN", JOptionPane.WARNING_MESSAGE);
		} else {
			log.warn(msg);
		}
	}
	
	/**
	 * 异常弹窗
	 * @param e 异常
	 * @param msg 异常消息
	 */
	public static void error(Throwable e, String msg) {
		if(OSUtils.isWin()) {
			JOptionPane.showMessageDialog(
				    null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
			if(e != null) {
				e.printStackTrace();
			}
		} else {
			log.error(msg, e);
		}
	}
	
}
