package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.protocol.bean.other.AppVideo;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.num.IDUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import exp.libs.warp.net.http.HttpUtils;

/**
 * <PRE>
 * 模拟在线观看直播(定时发送在线心跳)
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WatchLive extends __XHR {

	/** 手机端浏览器头 */
	private final static String APP_USER_AGENT = "Mozilla/5.0 BiliDroid/5.22.1 (bbcallen@gmail.com)";
	
	/** 模拟PC端在线观看直播的心跳URL */
	private final static String PC_WATCH_URL = Config.getInstn().PC_WATCH_URL();
	
	/** 取手机端直播视频地址URL(每次获取有效期为半小时) */
	private final static String APP_VIDEO_URL = Config.getInstn().APP_VIDEO_URL();
	
	/** 模拟手机端在线观看直播的心跳URL */
	private final static String APP_WATCH_URL = Config.getInstn().APP_WATCH_URL();
	
	/** 当前手机直播视频的对象信息 */
	private final static AppVideo APP_VIDEO = new AppVideo();
	
	protected WatchLive() {}
	
	/**
	 * 模拟PC端在线观看直播 (需每5分钟执行一次)
	 * @param cookie
	 * @param roomId
	 */
	public static void toWatchPCLive(BiliCookie cookie, int roomId) {
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), getRealRoomId(roomId));
		String response = HttpURLUtils.doGet(PC_WATCH_URL, header, null);
		
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				log.info("[{}] 正在模拟PC端在线观看直播...", cookie.NICKNAME());
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.error("[{}] 模拟PC端在线观看直播失败: {}", cookie.NICKNAME(), reason);
			}
		} catch(Exception e) {
			log.error("[{}] 模拟PC端在线观看直播失败: {}", cookie.NICKNAME(), response, e);
		}
	}
	
	/**
	 * 模拟手机端在线观看直播 (需每15秒执行一次)
	 * @param cookie
	 * @param roomId
	 * @param detailSecond 距离上次请求的时间间隔（单位:s）, 默认频率是15秒一次
	 * @return
	 */
	public static void toWatchAppLive(BiliCookie cookie, int roomId) {
		if(APP_VIDEO.isVaild() == false) {
			APP_VIDEO.update(cookie, roomId);
		}
		
		Map<String, String> header = getHeader(cookie, APP_VIDEO.getCreateTime());
		Map<String, String> request = getRequest(cookie.UID(), APP_VIDEO);
		String response = HttpURLUtils.doPost(APP_WATCH_URL, header, request);
		if("ok".equals(response)) {
			log.info("[{}] 正在模拟手机端在线观看直播...", cookie.NICKNAME());
			
		} else {
			log.error("[{}] 模拟手机端在线观看直播失败: {}", cookie.NICKNAME(), response);
		}
	}
	
	/**
	 * 手机端观看直播请求头
	 * @param cookie
	 * @param watchTime 开始观看的时间点
	 * @return
	 */
	private static Map<String, String> getHeader(BiliCookie cookie, long watchTime) {
		Map<String, String> header = POST_HEADER(cookie.toNVCookie());
		header.put(BiliCmdAtrbt.DisplayID, StrUtils.concat(cookie.UID(), "-", watchTime));
		header.put(HttpUtils.HEAD.KEY.USER_AGENT, APP_USER_AGENT);
		header.put(HttpUtils.HEAD.KEY.HOST, "live-trace.bilibili.com");
		
		// 观看直播的手机是设备参数(相对固定)
		header.put(BiliCmdAtrbt.DeviceID, "SilMKRkvHSwfe04rVyseKxNxSH4aKWkLbAJmVSdbOghiVjUEMgMyAzMDMQE2Ag");
		header.put(BiliCmdAtrbt.Buvid, "52EBE497-0DEC-4056-8FD3-FDB2F69690877229infoc");
		return header;
	}
	
	/**
	 * 手机端观看直播请求参数
	 * @param uid 观看直播的用户ID
	 * @param appVideo 所观看的直播信息
	 * @param detailSecond 距离上次请求的时间间隔（单位:s）
	 * @return
	 */
	private static Map<String, String> getRequest(String uid, AppVideo appVideo) {
		long now = System.currentTimeMillis();
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.c_time, String.valueOf(now));		// 当前时间(ms)
		request.put(BiliCmdAtrbt.ts, String.valueOf(now / 1000));	// 当前时间(s)
		request.put(BiliCmdAtrbt.delta_ts, String.valueOf(appVideo.getDeltaSecond()));	// 距离上次请求的时间间隔
		request.put(BiliCmdAtrbt.mid, uid);		// 观看直播的用户ID
		request.put(BiliCmdAtrbt.room_id, appVideo.getRoomId());	// 房间号
		request.put(BiliCmdAtrbt.up_id, appVideo.getUpId());		// 主播ID
		request.put(BiliCmdAtrbt.up_level, String.valueOf(appVideo.getUpLv()));	// 主播等级
		request.put(BiliCmdAtrbt.playurl, appVideo.getUrl());		// 所观看的直播视频地址
		request.put(BiliCmdAtrbt.guid, appVideo.getGuid());	// FIXME： 随直播视频地址变化, 生成规则未知
		request.put(BiliCmdAtrbt.area, "21");		// FIXME: 每个直播间固定, 生成规则未知, 应该是直播子分区
		request.put(BiliCmdAtrbt.parent_area, "1");	// FIXME: 每个直播间固定, 生成规则未知, 应该是直播分区
		request.put(BiliCmdAtrbt.sign, IDUtils.getUUID().replace("-", ""));	// FIXME: 每次请求都会变化, 生成规则未知
		request.put(BiliCmdAtrbt.jumpfrom, "24000");	// FIXME: 跳转到当前直播间的入口, 如从"我的关注"进入是21000, 从平台进入是24000, 具体生成规则未知
		
		// 观看直播的手机是设备参数(相对固定值)
		request.put(BiliCmdAtrbt.appkey, "1d8b6e7d45233436");	// 设备唯一标识
		request.put(BiliCmdAtrbt.version, "5.22.1");	// Bilibili-APP版本号
		request.put(BiliCmdAtrbt.build, "5220001");		// Bilibili-APP版本号
		request.put(BiliCmdAtrbt.platform, "android");
		request.put(BiliCmdAtrbt.mobi_app, "android");
		request.put(BiliCmdAtrbt.pid, "13");
		request.put(BiliCmdAtrbt.play_type, "1");
		return request;
	}
	
	/**
	 * 获取手机端直播视频地址 (地址有效期是半小时)
	 * @param cookie
	 * @param roomId
	 * @return http://qn.live-play.acgvideo.com/live-qn/710856/live_14931184_9763491.flv?wsSecret=11a51740bd1e56c46ff172cbf4318b8f&wsTime=1518314300
	 */
	public static String getAppVideoURL(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId); 
		Map<String, String> header = getHeader();
		Map<String, String> request = getRequest(cookie.UID(), sRoomId);
		String response = HttpURLUtils.doGet(APP_VIDEO_URL, header, request);
		
		String videoUrl = "";
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONArray durl = JsonUtils.getArray(json, BiliCmdAtrbt.durl);
			if(durl.size() > 0) {
				JSONObject obj = durl.getJSONObject(0);
				videoUrl = JsonUtils.getStr(obj, BiliCmdAtrbt.url);
			}
		} catch(Exception e) {
			log.error("获取房间 [{}] 的手机端直播地址失败: {}", sRoomId, response, e);
		}
		return videoUrl;
	}
	
	/**
	 * 手机端直播视频地址的请求头
	 * @return
	 */
	private static Map<String, String> getHeader() {
		Map<String, String> header = GET_HEADER("");
		header.put(HttpUtils.HEAD.KEY.USER_AGENT, APP_USER_AGENT);
		header.put(HttpUtils.HEAD.KEY.HOST, "live.bilibili.com");
		return header;
	}
	
	/**
	 * 手机端直播视频地址的请求参数
	 * @param uid
	 * @param roomId
	 * @return
	 */
	private static Map<String, String> getRequest(String uid, String roomId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.mid, uid);
		request.put(BiliCmdAtrbt.cid, roomId);
		request.put(BiliCmdAtrbt.otype, "json");
//		request.put("access_key", "bab2f86e84fa21994d3bf3ce98a90462");
//		request.put("sign", "9573b67aa27643c8b6e0c49bf87d6497");
//		request.put("expire", "1520902763");
//		request.put("qn", "1");
//		request.put("npcybs", "0");
		
//		request.put("appkey", "iVGUTjsxvpLeuDCf");
		request.put(BiliCmdAtrbt.platform, "android");
		request.put(BiliCmdAtrbt.device, "android");
//		request.put("build", "5220001");
//		request.put("buvid", "52EBE497-0DEC-4056-8FD3-FDB2F69690877229infoc");
		return request;
	}
	
}
