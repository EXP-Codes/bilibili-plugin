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
	private final static String AREA_URL = Config.getInstn().AREA_URL();

	/**
	 * 获取每个直播分区的TOP1房间号
	 * @return TOP1房间号列表
	 */
	public static Map<Area, Integer> getAreaTopOnes() {
		Map<Area, Integer> roomIds = new HashMap<Area, Integer>();
		roomIds.put(Area.PC_GAME, getAreaTopOne(Area.PC_GAME));
		roomIds.put(Area.APP_GAME, getAreaTopOne(Area.APP_GAME));
		roomIds.put(Area.AMUSE, getAreaTopOne(Area.AMUSE));
		roomIds.put(Area.DRAW, getAreaTopOne(Area.DRAW));
		roomIds.put(Area.RADIO, getAreaTopOne(Area.RADIO));
		return roomIds;
	}
	
	/**
	 * 获取分区top1房间号
	 * @param Area true:手机平台（对应手游区）; false:PC平台（对应游戏区）
	 * @return top1房间号
	 */
	private static int getAreaTopOne(Area area) {
		final String SUB_AREA_ID = "0";	// 子分区号(0表示所有子分区)
		final String URI = StrUtils.concat("/p/eden/area-tags?parentAreaId=", 
				area.ID(), "&areaId=", SUB_AREA_ID, "&visit_id=", getVisitId());
		Map<String, String> header = GET_HEADER("", URI);
		Map<String, String> request = getRequest(area.ID(), SUB_AREA_ID);
		String response = HttpURLUtils.doGet(AREA_URL, header, request);

		int roomId = 0;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray list = JsonUtils.getArray(data, BiliCmdAtrbt.list);
				roomId = JsonUtils.getInt(list.getJSONObject(0), BiliCmdAtrbt.roomid, 0);
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取 {} Top1 房间失败: {}", area.DESC(), reason);
			}
		} catch(Exception e) {
			log.error("获取 {} Top1 房间异常: {}", area.DESC(), response, e);
		}
		return roomId;
	}
	
	/**
	 * 获取分区TOP1房间号的请求参数
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
	         
}
