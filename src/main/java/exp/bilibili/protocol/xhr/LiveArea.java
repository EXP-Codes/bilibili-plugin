package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.envm.Area;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 直播分区查询
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LiveArea extends __XHR {

	/** 游戏区/手游区房间列表URL */
	private final static String GAME_URL = Config.getInstn().GAME_URL();

	/** 娱乐区房间列表URL */
	private final static String AMUSE_URL = Config.getInstn().AMUSE_URL();
	
	/** 绘画区房间列表URL */
	private final static String DRAW_URL = Config.getInstn().DRAW_URL();
	
	/**
	 * 获取每个直播分区的TOP1房间号
	 * @return TOP1房间号列表
	 */
	public static Map<Area, Integer> getAreaTopOnes() {
		Map<Area, Integer> roomIds = new HashMap<Area, Integer>();
		roomIds.put(Area.PC_GAME, getGameTopOne(false));
		roomIds.put(Area.APP_GAME, getGameTopOne(true));
		roomIds.put(Area.AMUSE, getAmuseTopOne());
		roomIds.put(Area.DRAW, getDrawTopOne());
		return roomIds;
	}
	
	/**
	 * 获取游戏区top1房间号
	 * @param isApp true:手机平台（对应手游区）; false:PC平台（对应游戏区）
	 * @return top1房间号
	 */
	private static int getGameTopOne(boolean isApp) {
		final Area AREA = isApp ? Area.APP_GAME : Area.PC_GAME;
		final String SUB_AREA_ID = "0";	// 子分区号(0表示所有子分区)
		final String URI = StrUtils.concat("/p/eden/area-tags?parentAreaId=", 
				AREA.ID(), "&areaId=", SUB_AREA_ID, "&visit_id=", getVisitId());
		Map<String, String> header = GET_HEADER("", URI);
		Map<String, String> request = getRequest(AREA.ID(), SUB_AREA_ID);
		String response = HttpURLUtils.doGet(GAME_URL, header, request);
		
		int roomId = 0;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONArray array = JsonUtils.getArray(json, BiliCmdAtrbt.data);
				roomId = JsonUtils.getInt(array.getJSONObject(0), BiliCmdAtrbt.roomid, 0);
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取 {} Top1 房间失败: {}", AREA.DESC(), reason);
			}
		} catch(Exception e) {
			log.error("获取 {} Top1 房间异常: {}", AREA.DESC(), response, e);
		}
		return roomId;
	}
	
	/**
	 * 获取游戏分区TOP1房间号的请求参数
	 * @param pAreaId
	 * @param areaId
	 * @return
	 */
	private static Map<String, String> getRequest(String pAreaId, String areaId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.platform, "web");
		request.put(BiliCmdAtrbt.parent_area_id, pAreaId);
		request.put(BiliCmdAtrbt.cate_id, "0");
		request.put(BiliCmdAtrbt.area_id, areaId);
		request.put(BiliCmdAtrbt.sort_type, "online");
		request.put(BiliCmdAtrbt.page, "1");		// 取首页
		request.put(BiliCmdAtrbt.page_size, "1");	// 只取1个房间
		return request;
	}
	         
 	/**
	 * 获取娱乐区top1房间号
	 * @return top1房间号
	 */
	private static int getAmuseTopOne() {
		Map<String, String> header = GET_HEADER("", "/pages/area/ent");
		String response = HttpURLUtils.doGet(AMUSE_URL, header, null);
		
		int roomId = 0;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray array = JsonUtils.getArray(data, BiliCmdAtrbt.top_recommend);
				roomId = JsonUtils.getInt(array.getJSONObject(0), BiliCmdAtrbt.roomid, 0);
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取 {} Top1 房间失败: {}", Area.AMUSE.DESC(), reason);
			}
		} catch(Exception e) {
			log.error("获取 {} Top1 房间异常: {}", Area.AMUSE.DESC(), response, e);
		}
		return roomId;
	}
	
	/**
	 * 获取绘画区top1房间号
	 * @return top1房间号
	 */
	private static int getDrawTopOne() {
		Map<String, String> header = GET_HEADER("", "/pages/area/draw");
		Map<String, String> request = getRequest();
		String response = HttpURLUtils.doGet(DRAW_URL, header, request);
		
		int roomId = 0;
		try {
 			JSONObject json = JSONObject.fromObject(response);
 			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
 			if(code == 0) {
 				JSONArray array = JsonUtils.getArray(json, BiliCmdAtrbt.data);
 				roomId = JsonUtils.getInt(array.getJSONObject(0), BiliCmdAtrbt.roomid, 0);
 				
 			} else {
 				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
 				log.warn("获取 {} Top1 房间失败: {}", Area.DRAW.DESC(), reason);
 			}
 		} catch(Exception e) {
 			log.error("获取 {} Top1 房间异常: {}", Area.DRAW.DESC(), response, e);
 		}
		return roomId;
	}
	
	/**
	 * 获取绘画分区TOP1房间号的请求参数
	 * @param pAreaId
	 * @param areaId
	 * @return
	 */
	private static Map<String, String> getRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.area, "draw");
		request.put(BiliCmdAtrbt.order, "live_time");
		request.put(BiliCmdAtrbt.page, "1");		// 取首页
		return request;
	}
	
}
