package com.sun.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * 图像文字识别工具
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class OCR {
	
	private final static String CHARSET = "UTF-8";
	
	private final static String EOL = System.getProperty("line.separator");
	
	private final static String TMP_FILENAME = "tmp";
	
	private final static String TMP_SUFFIX = ".txt";
	
	private final static String LANG_OPTION = "-l";
	
	private final static String ENG = "eng";
	
	public final static String TESSERACT = "tesseract";
	
	public final static String IMG_FORMAT_JPG = "jpg";
	
	public final static String IMG_FORMAT_PNG = "png";
	
	private String tesseractPath;

	public OCR(String tesseractPath) {
		this.tesseractPath = new File(tesseractPath).getAbsolutePath();
	}
	
	public String recognizeText(String imgPath, String imgFormat) throws Exception {
		File imgFile = new File(imgPath);
		File tmpImg = ImageIOHelper.createImage(imgFile, imgFormat);
		File tmpTxt = new File(imgFile.getParentFile(), TMP_FILENAME);
		String tmpTxtPath = tmpTxt.getAbsolutePath().concat(TMP_SUFFIX);
		
		int status = analyseImg(imgFile, tmpImg, tmpTxt);
		String rst = (status == 0 ? readFile(tmpTxtPath) : toErrDesc(status));
		
		tmpImg.delete();
		new File(tmpTxtPath).delete();
		
		if(status != 0) {
			throw new RuntimeException(rst);
		}
		return rst;
	}
	
	private int analyseImg(File imgFile, File tmpImg, File tmpTxt) throws Exception {
		List<String> cmd = new ArrayList<String>();
		cmd.add(tesseractPath.concat("/").concat(TESSERACT));
		cmd.add(tmpImg.getName());
		cmd.add(tmpTxt.getName());
		cmd.add(LANG_OPTION);
		cmd.add(ENG);
		
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imgFile.getParentFile());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		return process.waitFor();
	}
	
	private String readFile(String filePath) throws Exception {
		StringBuilder rst = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), CHARSET));
		String tmp = "";
		while ((tmp = in.readLine()) != null) {
			rst.append(tmp).append(EOL);
		}
		in.close();
		return rst.toString();
	}
	
	private String toErrDesc(int status) {
		String errDesc;
		switch (status) {
			case 0: {
				errDesc = "";
				break;
			}
			case 1: {
				errDesc = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			}
			case 29: {
				errDesc = "Cannot recognize the image or its selected region.";
				break;
			}
			case 31: {
				errDesc = "Unsupported image format.";
				break;
			}
			default: {
				errDesc = "Errors occurred.";
			}
		}
		return errDesc;
	}
	
}
