package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.GuardType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 船队奖励协议
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Guard extends __XHR {

	/** 检查直播间船上是否有可领奖船员URL */
	private final static String GUARD_CHECK_URL = Config.getInstn().GUARD_CHECK_URL();
	
	/** 领取船员亲密度奖励URL */
	private final static String GUARD_JOIN_URL = Config.getInstn().GUARD_JOIN_URL();
	
	/**
	 * 提取直播间内的船员ID列表.
	 * 	(已经领取过某个船员奖励的用户, 不会再查询到相关的船员id)
	 * @param cookie
	 * @param roomId 直播间号
	 * @return 可以领取奖励船员ID列表: 船员ID->船员类型
	 */
	public static Map<String, GuardType> checkGuardIds(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, sRoomId);
		
		Map<String, GuardType> guardIds = new HashMap<String, GuardType>();
		String response = HttpURLUtils.doGet(GUARD_CHECK_URL, header, request);
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONArray data = JsonUtils.getArray(json, BiliCmdAtrbt.data);
			for(int i = 0; i < data.size(); i++) {
				JSONObject guard = data.getJSONObject(i);
				String guardId = JsonUtils.getStr(guard, BiliCmdAtrbt.id);
				int type = JsonUtils.getInt(guard, BiliCmdAtrbt.privilege_type, GuardType.CIVILIAN.ID());
				GuardType guardType = GuardType.toGuardType(type);
				if(StrUtils.isNotTrimEmpty(guardId)) {
					guardIds.put(guardId, guardType);
				}
			}
		} catch(Exception e) {
			log.error("提取直播间 [{}] 的船员列表失败: {}", roomId, response, e);
		}
		return guardIds;
	}
	
	/**
	 * 领取船员亲密度奖励
	 * @param roomId 船员所在房间
	 * @param guardId 船员编号
	 * @return
	 */
	public static int getGuardGift(int roomId) {
		int cnt = 0;
		Set<BiliCookie> cookies = CookiesMgr.ALL();
		for(BiliCookie cookie : cookies) {
			if(!cookie.isBindTel()) {
				continue;
			}
			
			Map<String, GuardType> guardIds = checkGuardIds(cookie, roomId);
			Iterator<String> keys = guardIds.keySet().iterator();
			while(keys.hasNext()) {
				String guardId = keys.next();
				GuardType guardType = guardIds.get(guardId);
				cnt += getGuardGift(cookie, roomId, guardId, guardType) ? 1 : 0;
			}
		}
		return cnt;
	}
	
	/**
	 * 领取船员亲密度奖励
	 * @param cookie
	 * @param roomId 船员所在房间
	 * @param guardId 船员编号
	 * @param guardType 船员类型
	 * @return
	 */
	public static boolean getGuardGift(BiliCookie cookie, 
			int roomId, String guardId, GuardType guardType) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = getRequest(cookie.CSRF(), sRoomId, guardId);
		String response = HttpURLUtils.doPost(GUARD_JOIN_URL, header, request);
		
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				UIUtils.log("[", cookie.NICKNAME(), "] 领取了直播间 [", 
						roomId, "] 的", guardType.DESC(), "奖励");
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				if(!reason.contains("已经领取")) {
					log.warn("[{}] 领取了直播间 [{}] 的{}奖励失败: {}", 
							cookie.NICKNAME(), roomId, guardType.DESC(), reason);
				}
			}
		} catch(Exception e) {
			log.error("[{}] 领取直播间 [{}] 的{}奖励失败: {}", 
					cookie.NICKNAME(), roomId, guardType.DESC(), response, e);
		}
		return true;
	}

	/**
	 * 领取船员亲密度奖励的请求参数
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
