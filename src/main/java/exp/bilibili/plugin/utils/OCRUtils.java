package exp.bilibili.plugin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.OCR;


/**
 * <PRE>
 * 图像字符识别工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class OCRUtils {
	
	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(OCRUtils.class);
	
	/** OCR组件目录 */
	private final static String OCR_DIR = "./conf/ocr/tesseract";
	
	/** OCR处理对象 */
	private final static OCR _OCR = new OCR(OCR_DIR);
	
	/** 私有化构造函数 */
	protected OCRUtils() {}

	/**
	 * 把图像识别成文本内容
	 * @param imgPath
	 * @return
	 */
	public static String imgToTxt(String imgPath) {
		String txt = "";
		try {
			txt = _OCR.recognizeText(imgPath);
		} catch (Exception e) {
			log.error("识别图片文字失败: {}", imgPath, e);
		}
		return txt.trim();
	}
	
}
