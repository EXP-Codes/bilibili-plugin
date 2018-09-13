package exp.bilibili.plugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.envm.FileType;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.img.ImageUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.num.IDUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import exp.libs.warp.net.http.HttpUtils;

/**
 * <PRE>
 * 生成TensorFlow训练数据（节奏风暴验证码）
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class DownloadTensorFlowTrainDatas {

	/** 直播服务器主机 */
	protected final static String LIVE_HOST = Config.getInstn().LIVE_HOST();
	
	/** 直播首页 */
	private final static String LIVE_HOME = Config.getInstn().LIVE_HOME();
	
	/** 获取节奏风暴验证码URL */
	private final static String STORM_CODE_URL = Config.getInstn().STORM_CODE_URL();
	
	/** 图片缓存目录 */
	private final static String IMG_DIR = "./src/test/resources/exp/bilibili/plugin/utils/test/storm/download/";
	
	/** 节奏风暴验证码图片宽度 */
	private final static int IMG_WIDTH = 112;
	
	/** 节奏风暴验证码图片高度 */
	private final static int IMG_HEIGHT = 32;
	
	/**
	 * 下载节奏风暴验证码图片并将其二值化
	 *  可用于深度学习训练
	 * @param args
	 */
	@Test
	public void testDownloadStormVccodeImage() {
		CookiesMgr.getInstn().load(CookieType.VEST);
		BiliCookie cookie = CookiesMgr.VEST();
		
		for(int i = 0; i < 1000; i++) {
			String imgPath = getStormCaptcha(cookie);
			BufferedImage image = ImageUtils.read(imgPath);
			
			FileUtils.delete(imgPath);	// 先删除原图
			image = ImageUtils.toBinary(image);	// 二值化图片
			if(isVaild(image)) {	// 检查是否为有效图片（容易辨析，可用于深度训练）
				String savePath = StrUtils.concat(IMG_DIR, IDUtils.getMillisID(), FileType.PNG.EXT);
				ImageUtils.write(image, savePath, FileType.PNG);
				System.out.println(savePath);
				
			} else {
				i--;
			}
			ThreadUtils.tSleep(100);
		}
	}
	
	/**
	 * 下载节奏风暴验证码图片
	 * @param cookie
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getStormCaptcha(BiliCookie cookie) {
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), "");
		Map<String, String> request = _getRequest();
		String response = HttpURLUtils.doGet(STORM_CODE_URL, header, request);
		
		String savePath = "";
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				String token = JsonUtils.getStr(data, BiliCmdAtrbt.token);
				String image = JsonUtils.getStr(data, BiliCmdAtrbt.image);
				savePath = HttpUtils.convertBase64Img(image, IMG_DIR, "storm");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return savePath;
	}
	
	/**
	 * 生成GET方法的请求头参数
	 * @param cookie
	 * @return
	 */
	protected final static Map<String, String> GET_HEADER(String cookie) {
		Map<String, String> header = new HashMap<String, String>();
		header.put(HttpUtils.HEAD.KEY.ACCEPT, "application/json, text/plain, */*");
		header.put(HttpUtils.HEAD.KEY.ACCEPT_ENCODING, "gzip, deflate, sdch");
		header.put(HttpUtils.HEAD.KEY.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6");
		header.put(HttpUtils.HEAD.KEY.CONNECTION, "keep-alive");
		header.put(HttpUtils.HEAD.KEY.COOKIE, cookie);
		header.put(HttpUtils.HEAD.KEY.USER_AGENT, HttpUtils.HEAD.VAL.USER_AGENT);
		return header;
	}
	
	/**
	 * 生成GET方法的请求头参数
	 * @param cookie
	 * @param uri
	 * @return
	 */
	protected final static Map<String, String> GET_HEADER(String cookie, String uri) {
		Map<String, String> header = GET_HEADER(cookie);
		header.put(HttpUtils.HEAD.KEY.HOST, LIVE_HOST);
		header.put(HttpUtils.HEAD.KEY.ORIGIN, LIVE_HOME);
		header.put(HttpUtils.HEAD.KEY.REFERER, LIVE_HOME.concat(uri));
		return header;
	}
	
	/**
	 * 获取节奏风暴验证码参数
	 * @return
	 */
	private static Map<String, String> _getRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.underline, String.valueOf(System.currentTimeMillis()));
		request.put(BiliCmdAtrbt.width, String.valueOf(IMG_WIDTH));
		request.put(BiliCmdAtrbt.height, String.valueOf(IMG_HEIGHT));
		return request;
	}
	
	/**
	 * 检查是否为有效图像（此方法仅仅是粗判）
	 *   前景色（黑色）像素的个数在一定范围内时，认为是有效图片
	 * @param image
	 * @return
	 */
	private boolean isVaild(BufferedImage image) {
		final int W = image.getWidth();
		final int H = image.getHeight();
		
		int blackCnt = 0;
		for(int w = 0; w < W; w++) {
			for(int h = 0; h < H; h++) {
				int RGB = image.getRGB(w, h);
				blackCnt += (RGB == ImageUtils.RGB_BLACK ? 1 : 0);
			}
		}
		
		// 前景色在图像占比为 5%~25% 之间认为是有效图像
		// 过少认为二值化后图像过浅，难以辨认
		// 过多认为二值化后图像噪点过多，干扰太多
		boolean isVaild = false;
		double percent = ((double) blackCnt) / (W * H);
		if(percent >= 0.05 && percent <= 0.25) {
			isVaild = true;
		}
		return isVaild;
	}
	
	/**
	 * 把指定目录下的所有图片二值化
	 */
	@Test
	public void testConvertImageToBinary() {
		File dir = new File(IMG_DIR);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(FileType.PNG == FileUtils.getFileType(file)) {
				BufferedImage img = ImageUtils.read(file.getAbsolutePath());
				img = ImageUtils.toBinary(img);	// 单通道图像
				boolean isOk = ImageUtils.write(img, file.getAbsolutePath(), FileType.PNG);
				
				System.out.println(isOk + " : " + file.getPath());
			}
		}
	}
	
}

