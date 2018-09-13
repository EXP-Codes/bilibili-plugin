package exp.bilibili.plugin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.OCR;


/**
 * <PRE>
 * 图像识别工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class OCRUtils {
	
	private final static Logger log = LoggerFactory.getLogger(UIUtils.class);
	
	private final static String OCR_DIR = "./conf/ocr/tesseract";
	
	private final static OCR _OCR = new OCR(OCR_DIR);
	
	protected OCRUtils() {}

	/**
	 * 把JPG图像识别成文本内容
	 * @param jpgPath
	 * @return
	 */
	public static String jpgToTxt(String jpgPath) {
		String txt = "";
		try {
			txt = _OCR.recognizeText(jpgPath, OCR.IMG_FORMAT_JPG);
		} catch (Exception e) {
			log.error("识别图片文字失败: {}", jpgPath, e);
		}
		return revise(txt.trim());
	}
	
	/**
	 * 目前验证码图片只有 a+b 与 a-b 两种形式, 由于字体问题，某些数字会被固定识别错误, 
	 *  此方法用于修正常见的识别错误的数字/符号, 提高识别率
	 * @param txt
	 * @return
	 */
	private static String revise(String txt) {
		String revise = txt;
		
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
	 * 把PNG图像识别成文本内容
	 * @param pngPath
	 * @return
	 */
	public static String pngToTxt(String pngPath) {
		String txt = "";
		try {
			txt = _OCR.recognizeText(pngPath, OCR.IMG_FORMAT_PNG);
		} catch (Exception e) {
			log.error("识别图片文字失败: {}", pngPath, e);
		}
		return txt.trim();
	}
	
}
