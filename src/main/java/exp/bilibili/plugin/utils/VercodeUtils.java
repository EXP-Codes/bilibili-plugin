package exp.bilibili.plugin.utils;

import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 校验码计算工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class VercodeUtils {

	protected VercodeUtils() {}
	
	/**
	 * 计算验证码图片中的表达式
	 * @param vercodeImgPath 验证码图片路径, 目前仅有 a+b 与 a-b 两种形式的验证码
	 * @return
	 */
	public static int calculateImage(String vercodeImgPath) {
		return calculate(OCRUtils.jpgToTxt(vercodeImgPath));
	}
	
	/**
	 * 计算表达式
	 * @param expression 表达式, 目前仅有 a+b 与 a-b 两种形式
	 * @return
	 */
	public static int calculate(String expression) {
		int rst = 0;
		if(StrUtils.isNotEmpty(expression)) {
			String[] nums = expression.split("\\+|\\-");
			if(nums.length == 2) {
				int a = NumUtils.toInt(nums[0], 0);
				int b = NumUtils.toInt(nums[1], 0);
				rst = (expression.contains("+") ? (a + b) : (a - b));
			}
		}
		return rst;
	}
	
}
