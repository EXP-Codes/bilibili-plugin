package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.envm.Danmu;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.bean.other.User;
import exp.bilibili.protocol.bean.xhr.Achieve;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.envm.HttpHead;
import exp.libs.utils.encode.CryptoUtils;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 其他协议
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Other extends __XHR {

	/** 查询服务器配置信息的URL */
	private final static String SERVER_URL = Config.getInstn().SERVER_URL();
	
	/**
	 * 软件授权页(Bilibili-备用)
	 * 	实则上是管理员的个人LINK中心
	 */
	private final static String ADMIN_URL = CryptoUtils.deDES(
			"EECD1D519FEBFDE5EF68693278F5849E8068123647103E9D1644539B452D8DE870DD36BBCFE2C2A8E5A16D58A0CA752D3D715AF120F89F10990A854A386B95631E7C60D1CFD77605");
	
	/** 查询账号信息URL */
	private final static String ACCOUNT_URL = Config.getInstn().ACCOUNT_URL();
	
	/** 查询账号安全信息URL */
	private final static String SAFE_URL = Config.getInstn().SAFE_URL();
	
	/** 查询房间信息URL */
	private final static String ROOM_URL = Config.getInstn().ROOM_URL();
	
	/** 查询账号在特定房间内的信息URL */
	private final static String PLAYER_URL = Config.getInstn().PLAYER_URL();
	
	/** 查询弹幕配置URL */
	private final static String DANMU_URL = Config.getInstn().DANMU_URL();
	
	/** 查询房管列表URL */
	private final static String MANAGE_URL = Config.getInstn().MANAGE_URL();
	
	/** 小黑屋URL */
	private final static String BLACK_URL = Config.getInstn().BLACK_URL();
	
	/** 查询成就列表URL */
	private final static String GET_ACHIEVE_URL = Config.getInstn().GET_ACHIEVE_URL();
	
	/** 领取成就奖励URL */
	private final static String DO_ACHIEVE_URL = Config.getInstn().DO_ACHIEVE_URL();
	
	/** 检索主播的直播间URL */
	private final static String SEARCH_URL = Config.getInstn().SEARCH_URL();
	
	/** 进入直播间URL */
	private final static String ENTRY_ROOM_URL = Config.getInstn().ENTRY_ROOM_URL();
	
	/** 私有化构造函数 */
	protected Other() {}
	
	/**
	 * 获取管理员在B站link中心针对本插件的授权校验标签
	 * @return {"code":0,"msg":"OK","message":"OK","data":["W:M-亚絲娜","B:","T:20180301","V:2.0"]}
	 */
	public static String queryCertificateTags() {
		Map<String, String> header = getHeader();
		return HttpURLUtils.doGet(ADMIN_URL, header, null);
	}
	
	/**
	 * 查询校验标签的请求头
	 * @return
	 */
	private static Map<String, String> getHeader() {
		Map<String, String> header = GET_HEADER("");
		header.put(HttpHead.KEY.HOST, LINK_HOST);
		header.put(HttpHead.KEY.ORIGIN, LINK_HOME);
		header.put(HttpHead.KEY.REFERER, LINK_HOME.concat("/p/world/index"));
		return header;
	}
	
	/**
	 * 获取当前服务器的配置信息：
	 * 包括B站的服务器集群、WebSocket通知服务器、聊天服务器、直播服务器等.
	 * 
	 * 目前此接口仅用于测试
	 */
	public static String queryServerConfig(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.room_id, sRoomId);
		request.put(BiliCmdAtrbt.platform, "pc");
		request.put(BiliCmdAtrbt.player, "web");
		return HttpURLUtils.doGet(SERVER_URL, header, request);
	}
	
	/**
	 * 查询账号信息(并写入cookie内)
	 *	 主要用于检测账号id、昵称、是否为老爷
	 * {"code":0,"message":"0","ttl":1,"data":{"user_level":{"level":50,"next_level":51,"color":16746162,"level_rank":"4170"},"vip":{"vip":0,"vip_time":"2020-01-13 00:02:04","svip":0,"svip_time":"2018-12-13 21:56:04"},"title":{"title":"title-88-1"},"badge":{"is_room_admin":false},"privilege":{"target_id":0,"privilege_type":0,"sub_level":0,"notice_status":1,"broadcast":null,"buy_guard_notice":null},"info":{"uid":1650868,"uname":"M-亚絲娜","uface":"http://i2.hdslb.com/bfs/face/bbfd1b5cafe4719e3a57154ac1ff16a9e4d9c6b3.jpg","main_rank":10000,"bili_vip":0,"mobile_verify":1,"identification":1},"property":{"uname_color":"","bubble":0,"danmu":{"mode":1,"color":16772431,"length":30,"room_id":51108}},"recharge":{"status":1,"type":1,"value":"首充有礼","color":"fb7299","config_id":1001},"relation":{"is_followed":true},"wallet":{"gold":90,"silver":1008269},"medal":{"is_weared":true,"curr_weared":{"target_id":13173681,"target_name":"M斯文败类","medal_name":"高达","target_roomid":51108,"level":19,"intimacy":215788,"next_intimacy":500000,"day_limit":2000,"today_feed":2000,"is_union":0}},"extra_config":{"show_bag":true,"show_vip_broadcast":true},"mailbox":{"switch_status":1,"red_notice":0},"user_reward":{"entry_effect":null},"shield_info":{"shield_user_list":[{"uid":258768116,"uname":"星星躺在云朵上"}],"keyword_list":[],"shield_rules":{"rank":0,"verify":0,"level":0}},"lpl_info":{"lpl":0}}}
	 * @param cookie
	 * @return 
	 */
	public static boolean queryUserInfo(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = getHeader(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.room_id, sRoomId);
		String response = HttpURLUtils.doGet(ACCOUNT_URL, header, request);

		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONObject info = JsonUtils.getObject(data, BiliCmdAtrbt.info);
				JSONObject _vip = JsonUtils.getObject(data, BiliCmdAtrbt.vip);
				String uid = JsonUtils.getStr(info, BiliCmdAtrbt.uid);
				String username = JsonUtils.getStr(info, BiliCmdAtrbt.uname);
				int vip = JsonUtils.getInt(_vip, BiliCmdAtrbt.vip, 0);	// 月费老爷
				int svip = JsonUtils.getInt(_vip, BiliCmdAtrbt.svip, 0);	// 年费老爷
				
				cookie.setUid(uid);
				cookie.setNickName(username);
				cookie.setVip(vip + svip > 0);
				
			} else {
				String errDesc = JsonUtils.getStr(json, BiliCmdAtrbt.message);
				log.error("查询账号 [{}] 的个人信息失败: {}", cookie.toNVCookie(), errDesc);
			}
		} catch(Exception e) {
			log.error("查询账号信息异常: {}", response, e);
		}
		return cookie.isVaild();
	}
	
	/**
	 * 生成查询用户信息的请求头
	 * @param cookie
	 * @return
	 */
	private static Map<String, String> getHeader(String cookie, String roomId) {
		Map<String, String> header = POST_HEADER(cookie);
		header.put(HttpHead.KEY.HOST, LIVE_HOST);
		header.put(HttpHead.KEY.ORIGIN, LIVE_HOME);
		header.put(HttpHead.KEY.REFERER, LIVE_HOME.concat("/").concat(roomId));
		return header;
	}
	
	/**
	 * 查询账号安全信息(并写入cookie内)
	 *  主要用于检测账号是否: 绑定手机, 实名认证
	 *  {"code":0,"data":{"safe_question":0,"hide_email":"272****@qq.com","hide_tel_phone":"139*****412","safe_rank":{"score":80,"level":2,"bind_tel":1,"bind_email":1,"email_veri":1,"tel_veri":1,"real_name":1,"pwd_level":3},"aso_account_sns":{"sina_bind":2,"qq_bind":2},"skipVerify":false}}
	 * @param cookie
	 * @return
	 */
	public static boolean queryUserSafeInfo(BiliCookie cookie) {
		Map<String, String> header = GET_HEADER(cookie.toNVCookie());
		String response = HttpURLUtils.doGet(SAFE_URL, header, null);
		
		boolean isOk = true;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONObject safeRank = JsonUtils.getObject(data, BiliCmdAtrbt.safe_rank);
				int bindTel = JsonUtils.getInt(safeRank, BiliCmdAtrbt.bind_tel, -1);
				int realName = JsonUtils.getInt(safeRank, BiliCmdAtrbt.real_name, -1);
				
				cookie.setBindTel(bindTel > 0);
				cookie.setRealName(realName > 0);
			}
		} catch(Exception e) {
			isOk = false;
			log.error("查询账号 [{}] 的安全信息异常: {}", cookie.NICKNAME(), response, e);
		}
		return isOk;
	}
	
	/**
	 * 查询用户在指定房间内的授权信息(并写入cookie内)
	 * 	主要检测是否为房管、是否为老爷（弹幕长度上限临时+10）、是否为提督/总督老爷（弹幕长度上限临时+10）
	 * @param cookie
	 * @param roomId
	 * @return
	 */
	public static boolean queryUserAuthorityInfo(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = getRequest(sRoomId);
		String rsp1 = HttpURLUtils.doGet(PLAYER_URL, header, request);
		String rsp2 = HttpURLUtils.doGet(DANMU_URL, header, request);
		
		boolean isOk = true;
		try {
			JSONObject json = JSONObject.fromObject(rsp1);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				int isAdmin = JsonUtils.getInt(JsonUtils.getObject(data, BiliCmdAtrbt.room_admin), BiliCmdAtrbt.is_admin, 0); // 房管
				int vip = JsonUtils.getInt(JsonUtils.getObject(data, BiliCmdAtrbt.level), BiliCmdAtrbt.vip, 0); 	// 老爷
				int svip = JsonUtils.getInt(JsonUtils.getObject(data, BiliCmdAtrbt.level), BiliCmdAtrbt.svip, 0); 	// 大会员
				
				cookie.setRoomAdmin(isAdmin > 0);
				cookie.setVip(vip > 0);
				cookie.setSVip(svip > 0);
			}
			
			json = JSONObject.fromObject(rsp2);
			code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				int danmuLen = JsonUtils.getInt(data, BiliCmdAtrbt.length, 0);
				cookie.setGuard(danmuLen >= Danmu.LEN_GUARD);
			}
			
		} catch(Exception e) {
			isOk = false;
			log.error("查询账号授权信息异常", e);
		}
		return isOk;
	}
	
	/**
	 * 查询用户在指定房间内的授权信息的请求参数
	 * @param roomId
	 * @return
	 */
	private static Map<String, String> getRequest(String roomId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, roomId);
		request.put(BiliCmdAtrbt.t, String.valueOf(System.currentTimeMillis()));
		return request;
	}
	
	/**
	 * 查询直播间主播的用户信息
	 * @param roomId
	 * @return 主播的用户信息
	 */
	public static User queryUpInfo(int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER("", sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, sRoomId);
		
		String response = HttpURLUtils.doGet(ROOM_URL, header, request);
		User up = User.NULL;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONObject info = JsonUtils.getObject(data, BiliCmdAtrbt.info);
				String uid = JsonUtils.getStr(info, BiliCmdAtrbt.uid);
				String uname = JsonUtils.getStr(info, BiliCmdAtrbt.uname);
				JSONObject level = JsonUtils.getObject(data, BiliCmdAtrbt.level);
				JSONObject masterLevel = JsonUtils.getObject(level, BiliCmdAtrbt.master_level);
				int lv = JsonUtils.getInt(masterLevel, BiliCmdAtrbt.level, 0);
				up = new User(uid, uname, lv);
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取直播间 [{}] 的主播ID失败: {}", sRoomId, reason);
			}
		} catch(Exception e) {
			log.error("获取直播间 [{}] 的主播ID异常: {}", sRoomId, response, e);
		}
		return up;
	}
	
	/**
	 * 查询直播间的房管（含主播）
	 * @param roomId 直播间ID
	 * @return 房管列表
	 */
	public static Set<User> queryManagers(int roomId) {
		Set<User> managers = new HashSet<User>();
		
		// 查询主播昵称
		User up = queryUpInfo(roomId);
		if(up != User.NULL) {
			managers.add(up);
		}
		
		// 查询房管列表
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER("", sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.anchor_id, up.ID());
		String response = HttpURLUtils.doGet(MANAGE_URL, header, request);
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONArray data = JsonUtils.getArray(json, BiliCmdAtrbt.data);
				for(int i = 0; i < data.size(); i++) {
					JSONObject userinfo = JsonUtils.getObject(data.getJSONObject(i), BiliCmdAtrbt.userinfo);
					String uid = JsonUtils.getStr(userinfo, BiliCmdAtrbt.uid);
					String uname = JsonUtils.getStr(userinfo, BiliCmdAtrbt.uname);
					if(StrUtils.isNotEmpty(uid, uname)) {
						managers.add(new User(uid, uname));
					}
				}
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("查询直播间 [{}] 的房管列表失败: {}", sRoomId, reason);
			}
		} catch(Exception e) {
			log.error("查询直播间 [{}] 的房管列表失败: {}", sRoomId, response, e);
		}
		return managers;
	}
	
	/**
	 * 把用户关小黑屋
	 * @param cookie 房管cookie
	 * @param roomId 
	 * @param username 被关用户的用户名
	 * @param hour
	 * @return
	 */
	public static boolean blockUser(BiliCookie cookie, int roomId, String username, int hour) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = getRequest(cookie.CSRF(), sRoomId, username, hour);
		String response = HttpURLUtils.doPost(BLACK_URL, header, request);
		
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				isOk = true;
				log.info("直播间 [{}]: [{}] 被关小黑屋 [{}] 小时", sRoomId, username, hour);
			} else {
				log.warn("直播间 [{}]: 把 [{}] 关小黑屋失败", sRoomId, username);
			}
		} catch(Exception e) {
			log.error("直播间 [{}]: 把 [{}] 关小黑屋异常", sRoomId, username, e);
		}
		return isOk;
	}
	
	/**
	 * 关小黑屋的请求参数
	 * @param csrf
	 * @param roomId
	 * @param username
	 * @param hour
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf, String roomId, String username, int hour) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, roomId);
		request.put(BiliCmdAtrbt.type, "1");
		request.put(BiliCmdAtrbt.content, username);
		request.put(BiliCmdAtrbt.hour, String.valueOf(hour));
		request.put(BiliCmdAtrbt.token, "");
		request.put(BiliCmdAtrbt.csrf_token, csrf);
		return request;
	}
	
	/**
	 * 查询可领取奖励的成就列表
	 * @param cookie
	 * @return 可领取奖励的成就列表
	 */
	public static List<Achieve> queryAchieve(BiliCookie cookie) {
		Map<String, String> header = getHeader(cookie.toNVCookie(), "");
		Map<String, String> request = getRequest();
		String response = HttpURLUtils.doGet(GET_ACHIEVE_URL, header, request);
		
		List<Achieve> achieves = new LinkedList<Achieve>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray info = JsonUtils.getArray(data, BiliCmdAtrbt.info);
				for(int i = 0; i < info.size(); i++) {
					JSONObject obj = info.getJSONObject(i);
					boolean status = JsonUtils.getBool(obj, BiliCmdAtrbt.status, false);
					boolean finished = JsonUtils.getBool(obj, BiliCmdAtrbt.finished, false);
					if(status && !finished) {
						String tid = JsonUtils.getStr(obj, BiliCmdAtrbt.tid);
						String title = JsonUtils.getStr(obj, BiliCmdAtrbt.title);
						achieves.add(new Achieve(tid, title));
					}
				}
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.info("查询 [{}] 的成就列表失败: {}", cookie.NICKNAME(), reason);
			}
		} catch(Exception e) {
			log.error("查询 [{}] 的成就列表失败: {}", cookie.NICKNAME(), response, e);
		}
		return achieves;
	}
	
	/**
	 * 查询成就的请求参数
	 * @return
	 */
	private static Map<String, String> getRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.type, "normal");	// 普通成就
		request.put(BiliCmdAtrbt.category, "all");	// 普通成就的分类
		request.put(BiliCmdAtrbt.status, "1");		// 0:所有成就;  1：已完成成就（包括未领取）;  -1：未完成成就
		request.put(BiliCmdAtrbt.keywords, "");
		request.put(BiliCmdAtrbt.page, "1");
		request.put(BiliCmdAtrbt.pageSize, "100");	// 每页显示的成就数（B站目前最多48个成就）
		return request;
	}
	
	/**
	 * 领取成就奖励
	 * @param cookie
	 * @param achieves 可领取奖励的成就列表
	 */
	public static void doAchieve(BiliCookie cookie, List<Achieve> achieves) {
		Map<String, String> header = getHeader(cookie.toNVCookie(), "");
		Map<String, String> request = new HashMap<String, String>();
		
		for(Achieve achieve : achieves) {
			request.put(BiliCmdAtrbt.id, achieve.getId());
			String response = HttpURLUtils.doGet(DO_ACHIEVE_URL, header, request);
			
			try {
				JSONObject json = JSONObject.fromObject(response);
				int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
				if(code == 0) {
					UIUtils.log("[", cookie.NICKNAME(), "] 已领取成就奖励 [", achieve.getName(),"]");
					
				} else {
					String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
					log.info("[{}] 领取成就 [{}] 的奖励失败: {}", cookie.NICKNAME(), achieve.getName(), reason);
				}
			} catch(Exception e) {
				log.error("[{}] 领取成就 [{}] 的奖励失败: {}", cookie.NICKNAME(), achieve.getName(), response, e);
			}
		}
	}
	
	/**
	 * 检索主播的房间号
	 * @param cookie
	 * @param liveupName 主播名称
	 * @return 主播的房间号(长号)
	 */
	public static int searchRoomId(BiliCookie cookie, String liveupName) {
		Map<String, String> header = getHeader(cookie.toNVCookie(), "");
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.jsonp, "jsonp");
		request.put(BiliCmdAtrbt.search_type, "live");
		request.put(BiliCmdAtrbt.highlight, "1");
		request.put(BiliCmdAtrbt.keyword, liveupName);
		request.put(BiliCmdAtrbt.callback, "__jp1");
		
		int roomId = -1;
		String response = HttpURLUtils.doGet(SEARCH_URL, header, request);
		response = RegexUtils.findFirst(response, "^__jp1\\((.*)\\)$");
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
			JSONObject result = JsonUtils.getObject(data, BiliCmdAtrbt.result);
			JSONArray liveRooms = JsonUtils.getArray(result, BiliCmdAtrbt.live_room);
			if(liveRooms.size() > 0) {
				JSONObject liveRoom = liveRooms.getJSONObject(0);
				roomId = JsonUtils.getInt(liveRoom, BiliCmdAtrbt.roomid, -1);
				if(roomId < 0) {
					roomId = JsonUtils.getInt(liveRoom, BiliCmdAtrbt.short_id, -1);
				}
			}
			
		} catch(Exception e) {
			log.error("搜索主播 [{}] 的房间号失败: {}", liveupName, response, e);
		}
		return roomId;
	}
	
	/**
	 * 模拟进入直播房间
	 * @param cookie
	 * @param roomId
	 * @return
	 */
	public static void entryRoom(BiliCookie cookie, int roomId) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.room_id, sRoomId);
		request.put(BiliCmdAtrbt.platform, "pc");
		request.put(BiliCmdAtrbt.csrf_token, cookie.CSRF());
		request.put(BiliCmdAtrbt.visit_id, getVisitId());
		HttpURLUtils.doPost(ENTRY_ROOM_URL, header, request);
	}
	
	/**
	 * 模拟进入直播房间
	 * @param cookie
	 * @param roomId
	 * @param url
	 * @return
	 */
	public static void entryRoom(BiliCookie cookie, int roomId, String url) {
		Map<String, String> header = GET_HEADER(cookie.toNVCookie());
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.visit_id, getVisitId());
		HttpURLUtils.doGet(url, header, request);
	}
	
}
