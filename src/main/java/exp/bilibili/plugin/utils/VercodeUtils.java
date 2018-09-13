package exp.bilibili.plugin.utils;

import exp.bilibili.plugin.cache.VercodeRecognition;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.RandomUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ocr.OCR;

/**
 * <PRE>
 * 校验码识别工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class VercodeUtils {
	
	/** OCR组件目录 */
	public final static String OCR_DIR = "./conf/ocr/tesseract";
	
	/** OCR处理对象 (用于识别旧版无干扰的验证码) */
	private final static OCR OCR = new OCR(OCR_DIR);
	
	/** B站小学数学验证码识别器 (用于识别新版有干扰的验证码) */
	private final static VercodeRecognition RECOGNITION = VercodeRecognition.getInstn();
	
	/** 私有化构造函数 */
	protected VercodeUtils() {}
	
	/**
	 * 从小学数学验证码的图片中析取表达式.
	 * ------------------------------------
	 *   验证码表达式的特点:
	 *    1. 仅有 a+b 与 a-b 两种形式的验证码 (其中a为2位数, b为1位数)
	 *    2. a的取值范围是 [10, 99]
	 *    3. b的取值范围是 [1, 9]
	 *    4. 验证码结果的取值范围是 [1, 108]
	 * 
	 * @param imgPath 小学数学验证码图片路径
	 * @return 数学表达式
	 */
	public static int calculateImageExpression(String imgPath) {
		String expression = RECOGNITION.analyse(imgPath); // 新版图像识别(有干扰)
		int rst = calculate(expression);
		
		// 即使识别失败, 还是碰碰运气
		// 而目前识别是在仅当表达式存在数字1的时候 (因识别略低, 算法直接跳过不识别)
		// 而当表达式至少存在一个1时, 取值范围为 [1, 100] 而非 [1, 108] 
		if(rst <= 0) {	
			rst = RandomUtils.genInt(1, 100);
		}
		return rst;
	}
	
	/**
	 * 从小学数学验证码的图片中析取表达式 (通过OCR工具识别).
	 * ------------------------------------
	 *   验证码表达式的特点:
	 *    1. 仅有 a+b 与 a-b 两种形式的验证码 (其中a为2位数, b为1位数)
	 *    2. a的取值范围是 [10, 99]
	 *    3. b的取值范围是 [1, 9]
	 *    4. 验证码结果的取值范围是 [1, 108]
	 * 
	 * @param imgPath 小学数学验证码图片路径
	 * @return 数学表达式
	 */
	public static int calculateImageExpressionByOCR(String imgPath) {
		String expression = OCR.recognizeText(imgPath);	// 旧版图像识别(无干扰)
		expression = reviseByOCR(expression);	// 修正表达式
		return calculate(expression);
	}
	
	/**
	 * 目前小学数学验证码图片由于字体问题，某些数字会被OCR固定识别错误, 
	 *  此方法用于修正常见的识别错误的数字/符号, 提高识别率
	 * @param txt
	 * @return
	 */
	private static String reviseByOCR(String expression) {
		String revise = expression;
		
		revise = revise.replace("[1", "0");
		revise = revise.replace("[|", "0");
		
		revise = revise.replace("'I", "7");
		
		revise = revise.replace("l•", "4");
		revise = revise.replace("l»", "4");
		revise = revise.replace("b", "4");
		revise = revise.replace("h", "4");
		
		revise = revise.replace("i", "1");
		revise = revise.replace("I", "1");
		revise = revise.replace("]", "1");
		revise = revise.replace("|", "1");
		
		revise = revise.replace("E", "6");
		
		revise = revise.replace("B", "8");
		
		revise = revise.replace("H", "9");
		revise = revise.replace("Q", "9");
		
		revise = revise.replace("·", "-");
		return revise;
	}
	
	/**
	 * 计算表达式
	 * @param expression 表达式, 目前仅有 a+b 与 a-b 两种形式
	 * @return
	 */
	private static int calculate(String expression) {
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
	
	/**
	 * 节奏风暴校验码辨识
	 * @param imgPath 节奏风暴校验码图片路径, 含4~5个变形字符
	 * @return 文字形式字符
	 */
	public static String recognizeStormImage(String imgPath) {
		return StrUtils.isTrimEmpty(imgPath) ? "" : 
			TensorFlowUtils.imgToTxt(imgPath);
	}
	
}
