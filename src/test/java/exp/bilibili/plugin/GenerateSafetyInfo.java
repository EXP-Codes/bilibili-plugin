package exp.bilibili.plugin;

import exp.bilibili.plugin.utils.SafetyUtils;

/**
 * <PRE>
 * 生成授权信息
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GenerateSafetyInfo {

	public static void main(String[] args) {
		updatePrivateTime();
	}
	
	/**
	 * 更新对私授权时间
	 */
	public static void updatePrivateTime() {
		int day = 35;	// 授权时间(从当前开始往后推N天)
		String code = SafetyUtils.certificateToFile(day);	// 授权码
		System.out.println(code);
	}
	
}
