package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 总督登船奖励协议
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Guard extends __XHR {

	/** 检查直播间船上是否有总督URL */
	private final static String GUARD_CHECK_URL = Config.getInstn().GUARD_CHECK_URL();
	
	/** 领取总督亲密度奖励URL */
	private final static String GUARD_JOIN_URL = Config.getInstn().GUARD_JOIN_URL();
	
	/**
	 * 提取直播间内的总督ID列表.
	 * 	(已经领取过某个总督奖励的用户, 不会再查询到相关的总督id)
	 * @param cookie
	 * @param roomId 直播间号
	 * @return 可以领取奖励总督ID列表
	 */
	public static List<String> checkGuardIds(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, sRoomId);
		
		List<String> guardIds = new LinkedList<String>();
		String response = HttpURLUtils.doGet(GUARD_CHECK_URL, header, request);
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
			JSONArray guards = JsonUtils.getArray(data, BiliCmdAtrbt.guard);
			for(int i = 0; i < guards.size(); i++) {
				JSONObject guard = guards.getJSONObject(i);
				String guardId = JsonUtils.getStr(guard, BiliCmdAtrbt.id);
				if(StrUtils.isNotTrimEmpty(guardId)) {
					guardIds.add(guardId);
				}
			}
		} catch(Exception e) {
			log.error("提取直播间 [{}] 的总督列表失败: {}", roomId, response, e);
		}
		return guardIds;
	}
	
	/**
	 * 领取总督亲密度奖励
	 * @param roomId 总督所在房间
	 * @param guardId 总督编号
	 * @return
	 */
	public static int getGuardGift(int roomId) {
		int cnt = 0;
		Set<BiliCookie> cookies = CookiesMgr.ALL();
		for(BiliCookie cookie : cookies) {
			if(!cookie.isBindTel()) {
				continue;
			}
			
			List<String> guardIds = checkGuardIds(cookie, roomId);
			for(String guardId : guardIds) {
				cnt += getGuardGift(cookie, roomId, guardId) ? 1 : 0;
			}
		}
		return cnt;
	}
	
	/**
	 * 领取总督亲密度奖励
	 * @param cookie
	 * @param roomId 总督所在房间
	 * @param guardId 总督编号
	 * @return
	 */
	public static boolean getGuardGift(BiliCookie cookie, int roomId, String guardId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = getRequest(cookie.CSRF(), sRoomId, guardId);
		String response = HttpURLUtils.doPost(GUARD_JOIN_URL, header, request);
		
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				UIUtils.log("[", cookie.NICKNAME(), "] 领取了直播间 [", roomId, "] 总督奖励(当前勋章亲密+20)");
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				if(!reason.contains("已经领取")) {
					log.warn("[{}] 领取了直播间 [{}] 总督奖励失败: {}", cookie.NICKNAME(), roomId, reason);
				}
			}
		} catch(Exception e) {
			log.error("[{}] 领取直播间 [{}] 的总督奖励失败: {}", cookie.NICKNAME(), roomId, response, e);
		}
		return true;
	}

	/**
	 * 领取总督亲密度奖励的请求参数
	 * @param csrf
	 * @param roomId
	 * @param guardId
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf, String roomId, String guardId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, roomId);
		request.put(BiliCmdAtrbt.id, guardId);
		request.put(BiliCmdAtrbt.type, "guard");
		request.put(BiliCmdAtrbt.csrf_token, csrf);
		request.put(BiliCmdAtrbt.visit_id, getVisitId());
		return request;
	}
	
}
