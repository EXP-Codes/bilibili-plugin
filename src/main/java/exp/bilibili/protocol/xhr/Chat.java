package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.envm.Colors;
import exp.libs.envm.HttpHead;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 版聊弹幕/私信消息
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Chat extends __XHR {

	/** 弹幕版聊URL */
	private final static String CHAT_URL = Config.getInstn().CHAT_URL();
	
	/** 私信首页 */
	private final static String MSG_HOME = Config.getInstn().MSG_HOME();
	
	/** 私信URL */
	private final static String MSG_URL = Config.getInstn().MSG_URL();
	
	/** 私有化构造函数 */
	protected Chat() {}
	
	/**
	 * 发送弹幕消息
	 * @param cookie 发送弹幕的账号cookie
	 * @param roomId 目标直播间房号
	 * @param msg 弹幕消息
	 * @param color 弹幕颜色
	 * @return
	 */
	public static boolean sendDanmu(BiliCookie cookie, int roomId, String msg, Colors color) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = getRequest(cookie.CSRF(), msg, sRoomId, color);
		String response = HttpURLUtils.doPost(CHAT_URL, header, request);
		return analyse(response, msg);
	}
	
	/**
	 * 弹幕请求参数
	 * @param msg
	 * @param realRoomId
	 * @param chatColor
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf, 
			String msg, String roomId, Colors color) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BiliCmdAtrbt.rnd, String.valueOf(System.currentTimeMillis() / 1000));	// 时间戳
		params.put(BiliCmdAtrbt.msg, msg);			// 弹幕内容
		params.put(BiliCmdAtrbt.color, color.RGB());// 弹幕颜色
		params.put(BiliCmdAtrbt.roomid, roomId);	// 接收消息的房间号
		params.put(BiliCmdAtrbt.fontsize, "25");
		params.put(BiliCmdAtrbt.mode, "1");
		params.put(BiliCmdAtrbt.csrf_token, csrf);
		return params;
	}
	
	/**
	 * 发送私信
	 * @param cookie 发送账号的cookie
	 * @param sendId 发送账号的用户ID
	 * @param recvId 接收账号的用户ID
	 * @param msg 发送消息
	 * @return
	 */
	public static boolean sendPM(BiliCookie cookie, String recvId, String msg) {
		Map<String, String> header = getHeader(cookie.toNVCookie());
		Map<String, String> request = getRequest(cookie.CSRF(), cookie.UID(), recvId, msg);
		String response = HttpURLUtils.doPost(MSG_URL, header, request);
		return analyse(response, msg);
	}
	
	/**
	 * 私信头参数
	 * @param cookie
	 * @return
	 */
	private static Map<String, String> getHeader(String cookie) {
		Map<String, String> header = POST_HEADER(cookie);
		header.put(HttpHead.KEY.HOST, LINK_HOST);
		header.put(HttpHead.KEY.ORIGIN, MSG_HOME);
		header.put(HttpHead.KEY.REFERER, MSG_HOME);
		return header;
	}
	
	/**
	 * 私信请求参数
	 * @param csrf
	 * @param sendId
	 * @param recvId
	 * @param msg
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf, 
			String sendId, String recvId, String msg) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.csrf_token, csrf);
		request.put(BiliCmdAtrbt.platform, "pc");
		request.put(BiliCmdAtrbt.msg$sender_uid, sendId);
		request.put(BiliCmdAtrbt.msg$receiver_id, recvId);
		request.put(BiliCmdAtrbt.msg$receiver_type, "1");
		request.put(BiliCmdAtrbt.msg$msg_type, "1");
		request.put(BiliCmdAtrbt.msg$timestamp, String.valueOf(System.currentTimeMillis() / 1000));
		
		JSONObject json = new JSONObject();
		json.put(BiliCmdAtrbt.content, msg);
		request.put(BiliCmdAtrbt.msg$content, json.toString());
		return request;
	}
	
	/**
	 * 弹幕/消息发送结果解析
	 * @param response  
	 * 		弹幕: {"code":-101,"msg":"请先登录","data":[]}
	 * 		私信: {"code":0,"msg":"ok","message":"ok","data":{"msg_key":6510413634042085687,"_gt_":0}}
	 * @return
	 */
	private static boolean analyse(String response, String msg) {
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				isOk = true;
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				reason = (StrUtils.isEmpty(reason) ? String.valueOf(code) : reason);
				UIUtils.log("发送消息失败(", reason, "): ", msg);
			}
		} catch(Exception e) {
			log.error("发送消息失败: {}", msg, e);
		}
		return isOk;
	}
	
}
