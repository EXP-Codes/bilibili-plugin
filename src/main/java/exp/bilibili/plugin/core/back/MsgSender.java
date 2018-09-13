package exp.bilibili.plugin.core.back;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.DailyTask;
import exp.bilibili.plugin.bean.ldm.HttpCookies;
import exp.bilibili.plugin.cache.Browser;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.BiliCmdAtrbt;
import exp.bilibili.plugin.envm.ChatColor;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.RSAUtils;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.plugin.utils.VercodeUtils;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpClient;
import exp.libs.warp.net.http.HttpURLUtils;
import exp.libs.warp.net.http.HttpUtils;

/**
 * <PRE>
 * B站直播版聊消息发送器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class MsgSender {

	private final static Logger log = LoggerFactory.getLogger(MsgSender.class);
	
	private final static long SLEEP_TIME = 1000;
	
	private final static String HOME_URL = Config.getInstn().HOME_URL();
	
	private final static String SSL_HOST = Config.getInstn().SSL_HOST();
	
	private final static String LIVE_URL = Config.getInstn().LIVE_URL();
	
	private final static String LOGIN_HOST = Config.getInstn().LOGIN_HOST();
	
	private final static String MINI_LOGIN_URL = Config.getInstn().MINI_LOGIN_URL();
	
	private final static String RSA_KEY_URL = Config.getInstn().RSA_URL();
	
	private final static String ACCOUNT_URL = Config.getInstn().ACCOUNT_URL();
	
	private final static String SIGN_URL = Config.getInstn().SIGN_URL();
	
	private final static String ASSN_URL = Config.getInstn().ASSN_URL();
	
	private final static String LINK_HOST = Config.getInstn().LINK_HOST();
	
	private final static String LINK_URL = Config.getInstn().LINK_URL();
	
	private final static String MSG_HOST = Config.getInstn().MSG_HOST();
	
	private final static String MSG_URL = Config.getInstn().MSG_URL();
	
	private final static String CHAT_URL = Config.getInstn().CHAT_URL();
	
	private final static String LIVE_LIST_URL = Config.getInstn().LIVE_LIST_URL();
	
	private final static String STORM_CHECK_URL = Config.getInstn().STORM_CHECK_URL();
	
	private final static String STORM_JOIN_URL = Config.getInstn().STORM_JOIN_URL();
	
	private final static String EG_CHECK_URL = Config.getInstn().EG_CHECK_URL();
	
	private final static String EG_JOIN_URL = Config.getInstn().EG_JOIN_URL();
	
	private final static String TV_JOIN_URL = Config.getInstn().TV_JOIN_URL();
	
	private final static String CHECK_TASK_URL = Config.getInstn().CHECK_TASK_URL();
	
	private final static String VERCODE_URL = Config.getInstn().VERCODE_URL();
	
	private final static String DO_TASK_URL = Config.getInstn().DO_TASK_URL();
	
	private final static String GET_REDBAG_URL = Config.getInstn().GET_REDBAG_URL();
	
	private final static String EX_REDBAG_URL = Config.getInstn().EX_REDBAG_URL();
	
	private final static String VERCODE_PATH = Config.getInstn().IMG_DIR().concat("/vercode.jpg");
	
	/** 最上一次抽奖过的礼物编号(礼物编号是递增的) */
	private static int LAST_RAFFLEID = 0;
	
	/** 最上一次抽奖过的节奏风暴编号(礼物编号是递增的) */
	private static int LAST_STORMID = 0;
	
	/** 私有化构造函数 */
	protected MsgSender() {}
	
	/**
	 * 生成POST方法的请求头参数
	 * @param cookies
	 * @param realRoomId
	 * @return
	 */
	private static Map<String, String> toPostHeadParams(String cookies, String realRoomId) {
		Map<String, String> params = toPostHeadParams(cookies);
		params.put(HttpUtils.HEAD.KEY.HOST, SSL_HOST);
		params.put(HttpUtils.HEAD.KEY.ORIGIN, LIVE_URL);
		params.put(HttpUtils.HEAD.KEY.REFERER, LIVE_URL.concat(realRoomId));	// 发送/接收消息的直播间地址
		return params;
	}
	
	/**
	 * 生成POST方法的请求头参数
	 * @param cookies
	 * @return
	 */
	private static Map<String, String> toPostHeadParams(String cookies) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUtils.HEAD.KEY.ACCEPT, "application/json, text/javascript, */*; q=0.01");
		params.put(HttpUtils.HEAD.KEY.ACCEPT_ENCODING, "gzip, deflate, br");
		params.put(HttpUtils.HEAD.KEY.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6");
		params.put(HttpUtils.HEAD.KEY.CONNECTION, "keep-alive");
		params.put(HttpUtils.HEAD.KEY.CONTENT_TYPE, // POST的是表单
				HttpUtils.HEAD.VAL.POST_FORM.concat(Config.DEFAULT_CHARSET));
		params.put(HttpUtils.HEAD.KEY.COOKIE, cookies);
		params.put(HttpUtils.HEAD.KEY.USER_AGENT, Config.USER_AGENT);
		return params;
	}
	
	/**
	 * 生成GET方法的请求头参数
	 * @param cookies
	 * @param realRoomId
	 * @return
	 */
	private static Map<String, String> toGetHeadParams(String cookies, String realRoomId) {
		Map<String, String> params = toGetHeadParams(cookies);
		params.put(HttpUtils.HEAD.KEY.HOST, SSL_HOST);
		params.put(HttpUtils.HEAD.KEY.ORIGIN, LIVE_URL);
		params.put(HttpUtils.HEAD.KEY.REFERER, LIVE_URL.concat(String.valueOf(realRoomId)));	// 发送/接收消息的直播间地址
		return params;
	}
	
	/**
	 * 生成GET方法的请求头参数
	 * @param cookies
	 * @return
	 */
	private static Map<String, String> toGetHeadParams(String cookies) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUtils.HEAD.KEY.ACCEPT, "application/json, text/plain, */*");
		params.put(HttpUtils.HEAD.KEY.ACCEPT_ENCODING, "gzip, deflate, sdch");
		params.put(HttpUtils.HEAD.KEY.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6");
		params.put(HttpUtils.HEAD.KEY.CONNECTION, "keep-alive");
		params.put(HttpUtils.HEAD.KEY.COOKIE, cookies);
		params.put(HttpUtils.HEAD.KEY.USER_AGENT, Config.USER_AGENT);
		return params;
	}
	
	/**
	 * 从后台秘密通道登陆B站
	 * @param username 账号
	 * @param password 密码
	 * @param vccode 验证码
	 * @param vcCookies 与验证码配套的登陆用cookies
	 * @return Cookie集合
	 */
	public static HttpCookies toLogin(String username, String password, 
			String vccode, String vcCookies) {
		HttpCookies cookies = new HttpCookies();
		HttpClient client = new HttpClient();
		
		try {
			// 从服务器获取RSA公钥(公钥是固定的)和随机hash码, 然后使用公钥对密码进行RSA加密
			String sJson = client.doGet(RSA_KEY_URL, _toLoginHeadParams(""), null);
			JSONObject json = JSONObject.fromObject(sJson);
			String hash = JsonUtils.getStr(json, BiliCmdAtrbt.hash);
			String pubKey = JsonUtils.getStr(json, BiliCmdAtrbt.key);
			password = RSAUtils.encrypt(hash.concat(password), pubKey);
			
			// 把验证码、验证码配套的cookies、账号、RSA加密后的密码 提交到登陆服务器
			Map<String, String> headers = _toLoginHeadParams(vcCookies);
			Map<String, String> requests = _toLoginRequestParams(username, password, vccode);
			sJson = client.doPost(MINI_LOGIN_URL, headers, requests);
			
			// 若登陆成功，则提取返回的登陆cookies, 以便下次使用
			json = JSONObject.fromObject(sJson);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {	
				HttpMethod method = client.getHttpMethod();
				if(method != null) {
					Header[] outHeaders = method.getResponseHeaders();
					for(Header outHeader : outHeaders) {
						if(HttpUtils.HEAD.KEY.SET_COOKIE.equals(outHeader.getName())) {
							cookies.add(outHeader.getValue());
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("登陆失败", e);
		}
		client.close();
		return cookies;
	}
	
	/**
	 * 生成登陆用的请求头参数
	 * @param cookies
	 * @return
	 */
	private static Map<String, String> _toLoginHeadParams(String cookies) {
		Map<String, String> params = toGetHeadParams(cookies);
		params.put(HttpUtils.HEAD.KEY.HOST, LOGIN_HOST);
		return params;
	}
	
	/**
	 * 生成登陆用的请求参数
	 * @param username 账号
	 * @param password 密码（RSA公钥加密密文）
	 * @param vccode 图片验证码
	 * @return
	 */
	private static Map<String, String> _toLoginRequestParams(
			String username, String password, String vccode) {
		Map<String, String> requests = new HashMap<String, String>();
		requests.put("cType", "2");
		requests.put("vcType", "1");		// 1:验证码校验方式;  2:二维码校验方式
		requests.put("captcha", vccode);	// 图片验证码
		requests.put("user", username);	// 账号（明文）
		requests.put("pwd", password);	// 密码（RSA公钥加密密文）
		requests.put("keep", "true");
		requests.put("gourl", HOME_URL);	// 登录后的跳转页面
		return requests;
	}
	
	/**
	 * 查询账号信息
	 * {"code":0,"status":true,"data":{"level_info":{"current_level":4,"current_min":4500,"current_exp":7480,"next_exp":10800},"bCoins":0,"coins":464,"face":"http:\/\/i2.hdslb.com\/bfs\/face\/bbfd1b5cafe4719e3a57154ac1ff16a9e4d9c6b3.jpg","nameplate_current":"http:\/\/i1.hdslb.com\/bfs\/face\/54f4c31ab9b1f1fa2c29dbbc967f66535699337e.png","pendant_current":"","uname":"M-\u4e9a\u7d72\u5a1c","userStatus":"","vipType":1,"vipStatus":1,"official_verify":-1,"pointBalance":0}}
	 * @param cookies
	 * @return username
	 */
	public static String queryUsername(String cookies) {
		Map<String, String> headers = toGetHeadParams(cookies);
		String response = HttpURLUtils.doGet(ACCOUNT_URL, headers, null, Config.DEFAULT_CHARSET);
		
		String username = "";
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				username = JsonUtils.getStr(data, BiliCmdAtrbt.uname);
			}
		} catch(Exception e) {
			log.error("查询账号信息失败: {}", response, e);
		}
		return username;
	}
	
	/**
	 * 每日签到
	 * @return
	 */
	public static void toSign() {
		final String cookies = Browser.COOKIES();
		int roomId = Config.getInstn().SIGN_ROOM_ID();
		roomId = (roomId <= 0 ? UIUtils.getCurRoomId() : roomId);
		int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		if(realRoomId > 0) {
			String sRoomId = String.valueOf(realRoomId);
			Map<String, String> headers = toGetHeadParams(cookies, sRoomId);
			String response = HttpURLUtils.doGet(SIGN_URL, headers, null, Config.DEFAULT_CHARSET);
			_analyseSignResponse(response);
			
		} else {
			log.warn("自动签到失败: 无效的房间号 [{}]", roomId);
		}
	}
	
	private static void _analyseSignResponse(String response) {
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				UIUtils.log("每日签到成功");
				
			} else {
				String errDesc = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				UIUtils.log("每日签到失败: ", errDesc);
			}
		} catch(Exception e) {
			log.error("每日签到失败: {}", response, e);
		}
	}
	
	/**
	 * 友爱社签到
	 * @return 是否需要持续测试签到
	 */
	public static boolean toAssn() {
		Map<String, String> headers = toPostHeadParams(Browser.COOKIES());
		headers.put(HttpUtils.HEAD.KEY.HOST, SSL_HOST);
		headers.put(HttpUtils.HEAD.KEY.ORIGIN, LINK_URL);
		headers.put(HttpUtils.HEAD.KEY.REFERER, LINK_URL.concat("/p/center/index"));
		
		Map<String, String> requests = new HashMap<String, String>();
		requests.put("task_id", "double_watch_task");
		requests.put("csrf_token", Browser.CSRF());
		
		String response = HttpURLUtils.doPost(ASSN_URL, headers, requests);
		return _analyseAssnResponse(response);
	}
	
	/**
	 * 
	 * @param response  {"code":0,"msg":"","message":"","data":[]}
	 * @return
	 */
	private static boolean _analyseAssnResponse(String response) {
		boolean goOn = true;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				goOn = false;	// 已签到成功，不需要继续签到
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				if(reason.contains("已领取")) {
					goOn = false;	// 已签到过，不需要继续签到
					
				} else {
					log.debug("友爱社签到失败: {}", reason);
				}
			}
		} catch(Exception e) {
			log.error("友爱社签到失败: {}", response, e);
		}
		return goOn;
	}
	
	/**
	 * 发送弹幕消息
	 * @param msg 弹幕消息
	 * @return
	 */
	public static boolean sendChat(String msg) {
		final int roomId = UIUtils.getCurRoomId();
		return sendChat(msg, roomId);
	}
	
	/**
	 * 
	 * @param msg
	 * @param color
	 * @return
	 */
	public static boolean sendChat(String msg, ChatColor color) {
		final int roomId = UIUtils.getCurRoomId();
		return sendChat(msg, color, roomId);
	}
	
	/**
	 * 发送弹幕消息
	 * @param msg 弹幕消息
	 * @param roomId 目标直播间
	 * @return
	 */
	public static boolean sendChat(String msg, int roomId) {
		return sendChat(msg, ChatColor.WHITE, roomId);
	}
	
	/**
	 * 发送弹幕消息
	 * @param msg 弹幕消息
	 * @param color 弹幕颜色
	 * @param roomId 目标直播间
	 * @return
	 */
	public static boolean sendChat(String msg, ChatColor color, int roomId) {
		return sendChat(msg, color, roomId, Browser.COOKIES());
	}

	/**
	 * 发送弹幕消息
	 * @param msg 弹幕消息
	 * @param color 弹幕颜色
	 * @param roomId 目标直播间房号
	 * @param cookies 发送用户的cookies
	 * @return
	 */
	public static boolean sendChat(String msg, ChatColor color, 
			int roomId, String cookies) {
		boolean isOk = false;
		int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		if(realRoomId > 0) {
			String sRoomId = String.valueOf(realRoomId);
			Map<String, String> headers = toPostHeadParams(cookies, sRoomId);
			Map<String, String> requests = _toChatRequestParams(msg, sRoomId, color.CODE());
			String response = HttpURLUtils.doPost(CHAT_URL, headers, requests, Config.DEFAULT_CHARSET);
			isOk = _analyseChatResponse(response);
			
		} else {
			log.warn("发送弹幕失败: 无效的房间号 [{}]", roomId);
		}
		return isOk;
	}
	
	/**
	 * 
	 * @param msg
	 * @param realRoomId
	 * @param chatColor
	 * @return
	 */
	private static Map<String, String> _toChatRequestParams(
			String msg, String realRoomId, String color) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("rnd", String.valueOf(System.currentTimeMillis() / 1000));	// 时间戳
		params.put("msg", msg);		// 弹幕内容
		params.put("color", color);	// 弹幕颜色
		params.put("roomid", realRoomId);	// 接收消息的房间号
		params.put("fontsize", "25");
		params.put("mode", "1");
		return params;
	}
	
	/**
	 * 
	 * @param response  {"code":-101,"msg":"请先登录","data":[]}
	 * @return
	 */
	private static boolean _analyseChatResponse(String response) {
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				isOk = true;
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				UIUtils.log("发送弹幕失败: ", reason);
			}
		} catch(Exception e) {
			UIUtils.log("发送弹幕失败: 服务器无响应");
			log.error("发送弹幕失败: {}", response, e);
		}
		return isOk;
	}
	
	/**
	 * 扫描当前的人气直播间房号列表
	 * @param cookies 扫描用的cookies
	 * @param MAX_PAGES 最大的查询分页(每页最多30个房间)
	 * @param MIN_ONLINE 要求房间最小人数(达标才扫描)
	 * @return
	 */
	public static List<Integer> queryTopLiveRoomIds(String cookies, 
			final int MAX_PAGES, final int MIN_ONLINE) {
		Map<String, String> header = toGetHeadParams(cookies, "all");
		Map<String, String> request = new HashMap<String, String>();
		request.put("area", "all");
		request.put("order", "online");
		
		List<Integer> roomIds = new LinkedList<Integer>();
		HttpClient client = new HttpClient();
		int pageOffset = TimeUtils.isNight() ? 1 : 0;	// 当为晚上时, 不选择首页房间(抢风暴成功率太低)
		for(int page = 1 + pageOffset; page <= MAX_PAGES + pageOffset; page++) {
			request.put("page", String.valueOf(page));
			String response = client.doGet(LIVE_LIST_URL, header, request);
			roomIds.addAll(_analyseTopLiveResponse(response, MIN_ONLINE));
		}
		client.close();
		return roomIds;
	}
	
	/**
	 * 
	 * @param response {"code":0,"msg":"ok","data":[{"roomid":99783,"short_id":828,"uid":7005369,"uname":"\u5251\u7f513\u5b98\u65b9\u89c6\u9891","face":"http:\/\/i1.hdslb.com\/bfs\/face\/85bd12a028ea4c4fa66448acc4ddc0609e824e01.jpg","title":"\u5251\u7f513\u5168\u6c11\u5403\u9e21\u2014\u2014\u51b3\u6218\u9f99\u95e8\uff01\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/99783.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","online":645731,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/828","stream_id":90886,"area_v2_id":82,"area_v2_name":"\u5251\u7f513","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":271744,"short_id":0,"uid":1577804,"uname":"\u67d0\u5e7b\u541b","face":"http:\/\/i1.hdslb.com\/bfs\/face\/9ed5ebf1e3694d9cd2b4fcd1d353759ee83b3dfe.jpg","title":"\u4e2d\u6587\u516b\u7ea7","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/271744.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","online":194894,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/271744","stream_id":262803,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1011,"short_id":0,"uid":4162287,"uname":"\u6e17\u900f\u4e4bC\u541b","face":"http:\/\/i0.hdslb.com\/bfs\/face\/623ccce0ab28b721edb61dd64749d91de18fb384.jpg","title":"\u6210\u5e74\u4eba\u7684\u6e38\u620f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1011.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","online":171792,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/1011","stream_id":479,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5441,"short_id":528,"uid":322892,"uname":"\u75d2\u5c40\u957f","face":"http:\/\/i1.hdslb.com\/bfs\/face\/bcdf640faa16ebaacea1d4c930baabaec9087a80.jpg","title":"\u4eba\u7c7b\u4e00\u8d25\u6d82\u5730","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5441.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","online":133722,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/528","stream_id":930,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":12722,"short_id":1040,"uid":352577,"uname":"\u6df3\u8272","face":"http:\/\/i2.hdslb.com\/bfs\/face\/39f3f9d4f1a0679a3409ee8b76ea8737307ba6b4.jpg","title":"\u8363\u8000\u4e00\u5439\uff1a\u8363\u8000\u5c40\u4e0a51\u661f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/12722.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","online":119625,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/1040","stream_id":3658,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":5279,"short_id":102,"uid":110631,"uname":"\u5bab\u672c\u72d7\u96e8","face":"http:\/\/i1.hdslb.com\/bfs\/face\/8c49a758216f9bd14b0046afe48a3514f44126f0.jpg","title":"\u7edd\u5730\u5927\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5279.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","online":117283,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/102","stream_id":774,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1029,"short_id":139,"uid":43536,"uname":"\u9ed1\u6850\u8c37\u6b4c","face":"http:\/\/i1.hdslb.com\/bfs\/face\/e2dae77b01436e8e9c99a392caeb58dff0415cf4.jpg","title":"\u4ece\u96f6\u5f00\u59cb\u7684\u4e9a\u6960\u751f\u6d3b","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1029.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","online":100075,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/139","stream_id":497,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":66688,"short_id":0,"uid":20848957,"uname":"\u98ce\u7af9\u6559\u4e3b\u89e3\u8bf4","face":"http:\/\/i0.hdslb.com\/bfs\/face\/288f13d1f589a3d6386d022044fbc10b705cab4f.jpg","title":"\u573a\u5747\u4e0d\u80fd12\u6740\uff0c\u5403\u4ec0\u4e48\u9e21\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/66688.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","online":89878,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/66688","stream_id":57731,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5313,"short_id":0,"uid":6043533,"uname":"\u9b45\u84dd\u624b\u673a","face":"http:\/\/i0.hdslb.com\/bfs\/face\/797907168d743cf24b347d8d329bd4ae3ce0ff11.jpg","title":"\u674e\u6960\u80fd\u5426\u5403\u9e21\uff1f0117\u9b45\u84dd\u65b0\u54c1\u60e8\u906d\u66dd\u5149\uff1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5313.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","online":89675,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/5313","stream_id":806,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5096,"short_id":388,"uid":183430,"uname":"\u4e24\u4eea\u6eda","face":"http:\/\/i0.hdslb.com\/bfs\/face\/10542620e3225773e0a3848888ccc4bf93d12488.jpg","title":"\u3010\u6eda\u3011\u6253\u724c\u665a\u4e0a\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5096.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","online":82395,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/388","stream_id":594,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":83264,"short_id":1125,"uid":1864366,"uname":"\u9ed1\u54f2\u541b","face":"http:\/\/i2.hdslb.com\/bfs\/face\/0053321a0ec1e53824b8fc0ab8297469c9ce2816.jpg","title":"(\uff40\uff65\u0434\uff65\u00b4\uff09\u611f\u53d7\u7edd\u671b\u5427\u4f60","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/83264.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","online":68605,"area":4,"areaName":"\u7535\u5b50\u7ade\u6280","link":"\/1125","stream_id":74307,"area_v2_id":86,"area_v2_name":"\u82f1\u96c4\u8054\u76df","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5067,"short_id":1000,"uid":227933,"uname":"\u5742\u672c\u53d4","face":"http:\/\/i1.hdslb.com\/bfs\/face\/1e31ac069058528e26b9be60b26d86c9c9a99f62.jpg","title":"\u3010\u5742\u672c\u3011\u4e0d\u770b\u4eac\u7d2b\u5168\u5c01\u4e86\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5067.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","online":61137,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/1000","stream_id":565,"area_v2_id":65,"area_v2_name":"\u5f69\u8679\u516d\u53f7","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":92613,"short_id":0,"uid":13046,"uname":"\u5c11\u5e74Pi","face":"http:\/\/i0.hdslb.com\/bfs\/face\/d851f48a579778b06249bf3debaa62d353694e91.jpg","title":"\u73a9\u706b\u67f4\u4eba\u6253\u67b6","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","online":66560,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/92613","stream_id":83716,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1175880,"short_id":367,"uid":1872628,"uname":"\u67ab\u8a00w","face":"http:\/\/i1.hdslb.com\/bfs\/face\/6590e763ec8ad1a3ba7ed5237949c048be91a7c3.jpg","title":"\u65b0\u7248\u672c\u65e5\u5e38","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1175880.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","online":50619,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/367","stream_id":1167310,"area_v2_id":40,"area_v2_name":"\u5d29\u574f3","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":175412,"short_id":553,"uid":14308645,"uname":"37\u4e0d\u662f37","face":"http:\/\/i2.hdslb.com\/bfs\/face\/d7b5e23b8ad7140240fd1ed132c180420ae5b54a.jpg","title":"\u301037\u3011\u9e21\u901f\u4e16\u754c!","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/175412.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","online":51185,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/553","stream_id":166470,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":56948,"short_id":446,"uid":1767542,"uname":"\u7b28\u5c3c\u65af\u7279","face":"http:\/\/i2.hdslb.com\/bfs\/face\/f7611e57f90efa247f58422634f628dcf07aafb0.jpg","title":"\u5413\u6b7b\u4eba\u4e86\u5413\u6b7b\u4eba\u4e86","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56948.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","online":42991,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/446","stream_id":47991,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":63727,"short_id":48,"uid":2832224,"uname":"SNH48\u5b98\u65b9\u8d26\u53f7","face":"http:\/\/i1.hdslb.com\/bfs\/face\/0bf6a963fbdfa98f5e243bb2e4d152ace4544592.jpg","title":"SNH48 XII\u961f\u300a\u4ee3\u53f7XII\u300b\u516c\u6f14","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63727.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","online":41509,"area":10,"areaName":"\u5531\u89c1\u821e\u89c1","link":"\/48","stream_id":54770,"area_v2_id":22,"area_v2_name":"\u821e\u89c1","area_v2_parent_id":1,"area_v2_parent_name":"\u5a31\u4e50","is_tv":0,"is_bn":""},{"roomid":544893,"short_id":0,"uid":13705279,"uname":"\u7761\u4e0d\u9192\u7684\u67d0\u67d0\u9633","face":"http:\/\/i2.hdslb.com\/bfs\/face\/a198ab1e6fc7e33480af5b4c989fd4092d154e7e.jpg","title":"\u8f6f\u7ef5\u7ef58\u4eba\u8054\u673a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/544893.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","online":40825,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/544893","stream_id":535954,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":544941,"short_id":594,"uid":6501408,"uname":"\u86c7\u8db3","face":"http:\/\/i2.hdslb.com\/bfs\/face\/54f174a503c7830b52c02faa5732520b3e2986bf.jpg","title":"\u3010\u86c7\u8db3\u3011\u4eca\u5929\u662f\u5e05\u6c14\u7684\u773c\u955c\u54e6","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/544941.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","online":36547,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/594","stream_id":536002,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":90713,"short_id":469,"uid":7946235,"uname":"\u987e\u4e8e\u6d6e\u751f\u5982\u68a6","face":"http:\/\/i1.hdslb.com\/bfs\/face\/7bba9f90ba3ab44b8e77c54ee300ac1c43b158b3.jpg","title":"\u738b\u8005\u8363\u8000 \u5fae\u4fe1\u533a\u4e0a\u738b\u8005","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/90713.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","online":38818,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/469","stream_id":81816,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":933508,"short_id":512,"uid":4705522,"uname":"\u6c99\u62c9Azusa","face":"http:\/\/i1.hdslb.com\/bfs\/face\/67f01127a411016e2b20cfd3d2e088d651856f31.jpg","title":"\u5973\u88c5up\u5728\u7537\u5bdd\u5ba4\u5973\u88c5\u662f\u4ec0\u4e48\u611f\u89c9\uff1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/933508.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","online":39282,"area":6,"areaName":"\u751f\u6d3b\u5a31\u4e50","link":"\/512","stream_id":924597,"area_v2_id":26,"area_v2_name":"\u65e5\u5e38","area_v2_parent_id":1,"area_v2_parent_name":"\u5a31\u4e50","is_tv":0,"is_bn":""},{"roomid":79558,"short_id":305,"uid":6810019,"uname":"AnKe-Poi","face":"http:\/\/i2.hdslb.com\/bfs\/face\/ae8aea930b21e86a83313dd3ad12cd8192e8bf49.jpg","title":"\u3010\u5b89\u53ef\u3011\u4ed9\u5883\u5403\u9e21\u5927\u9003\u6740","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/79558.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","online":36426,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/305","stream_id":70601,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":37338,"short_id":465,"uid":5907649,"uname":"\u5343\u8449\u578bDJ","face":"http:\/\/i0.hdslb.com\/bfs\/face\/8e1e4196cbcf5e5d9144c82bca70b134f324dabf.jpg","title":"ServantPara \u5fa9\u523b \u8d0b\u4f5c\u82f1\u970a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/37338.jpg?01112019","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","online":36424,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/465","stream_id":28306,"area_v2_id":37,"area_v2_name":"Fate\/GO","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":1374115,"short_id":0,"uid":7774837,"uname":"\u8fd9\u4e2a\u9ed1\u5ca9\u4e0d\u592a\u51b7","face":"http:\/\/i1.hdslb.com\/bfs\/face\/96bf6348d02dc0985bda0042eab2922da3643b67.jpg","title":"\u55d3\u5b50\u53d1\u708e\uff0c\u8bdd\u5c11...","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1374115.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","online":34620,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/1374115","stream_id":1365946,"area_v2_id":84,"area_v2_name":"300\u82f1\u96c4","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":21133,"short_id":0,"uid":7450650,"uname":"\u8d85\u679c\u679cmc","face":"http:\/\/i1.hdslb.com\/bfs\/face\/f6deabfcc901bb31e5ab42e8ec23067c1332b9ef.jpg","title":"[\u7edd\u5730\u6c42\u751f]\u5403\u9e21\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/21133.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","online":34571,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/21133","stream_id":12073,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5123,"short_id":0,"uid":372418,"uname":"\u6bd2\u5976\u6cd3\u5e0c","face":"http:\/\/i2.hdslb.com\/bfs\/face\/db00f2d1f6557c4a4e3e5e4eacbd48908b4f1498.jpg","title":"\u3010\u6cd3\u5e0c\u7089\u77f3\u3011\u65b0\u53d1\u578b.\u597d\u5eb7\u7684","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5123.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","online":32644,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/5123","stream_id":621,"area_v2_id":91,"area_v2_name":"\u7089\u77f3\u4f20\u8bf4","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":73014,"short_id":0,"uid":3405965,"uname":"\u5416\u9b3c123","face":"http:\/\/i1.hdslb.com\/bfs\/face\/b244b4c1fae1e776737d72652ad51d7ff087aa9d.jpg","title":"\u3010\u5416\u9b3c\u3011\u6700\u5f3a\u6cd5\u5e08\u6559\u5b66\uff0c\u592a\u9633\u51fa\u6765\u9e1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/73014.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","online":24730,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/73014","stream_id":64057,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":5031,"short_id":112,"uid":228342,"uname":"\u7a7a\u8033YAYA","face":"http:\/\/i1.hdslb.com\/bfs\/face\/12324212a2736ea1ee045a95add648531a395555.jpg","title":"\u6700\u7ec8\u5e7b\u60f3\u7eb7\u4e89NT \u5267\u60c5\u8d70\u8d77","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5031.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","online":24663,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/112","stream_id":530,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":24065,"short_id":0,"uid":193584,"uname":"\u95fb\u9999\u8bc6","face":"http:\/\/i1.hdslb.com\/bfs\/face\/4d417312a927ea3f895904afeb349bafbaeb45ca.jpg","title":"\u3010\u95fb\u9999\u8bc6\u3011\u6218\u6597","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/24065.jpg?01112019","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","online":25039,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/24065","stream_id":15005,"area_v2_id":65,"area_v2_name":"\u5f69\u8679\u516d\u53f7","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":50583,"short_id":154,"uid":201293,"uname":"\u4e03\u4e03\u89c1\u5948\u6ce2\u4e36","face":"http:\/\/i0.hdslb.com\/bfs\/face\/e7473106f5435023444dc259719b4b38312bf1b0.jpg","title":"\u5403\u9e21\u9047\u5230\u59b9 \u4e24\u773c\u6cea\u6c6a\u6c6a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/50583.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","online":25783,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/154","stream_id":41561,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""}]}
	 * @return
	 */
	private static List<Integer> _analyseTopLiveResponse(
			String response, final int MIN_ONLINE) {
		List<Integer> roomIds = new LinkedList<Integer>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONArray data = JsonUtils.getArray(json, BiliCmdAtrbt.data);
			for(int i = 0 ; i < data.size(); i++) {
				JSONObject room = data.getJSONObject(i);
				int realRoomId = JsonUtils.getInt(room, BiliCmdAtrbt.roomid, 0);
				int online = JsonUtils.getInt(room, BiliCmdAtrbt.online, 0);
				if(online > MIN_ONLINE) {
					roomIds.add(realRoomId);
				}
				
				// 顺便关联房间号(短号)与真实房号(长号)
				int shortId = JsonUtils.getInt(room, BiliCmdAtrbt.short_id, -1);
				int roomId = (shortId > 0 ? shortId : realRoomId);
				RoomMgr.getInstn().relate(roomId, realRoomId);
			}
		} catch(Exception e) {
			log.error("提取人气直播房间号失败: {}", response, e);
		}
		return roomIds;
	}
	
	/**
	 * 扫描节奏风暴
	 */
	public static int scanStorms(String cookies, 
			List<Integer> roomIds, long scanInterval) {
		int sum = 0;
		HttpClient client = new HttpClient();
		Map<String, String> requests = new HashMap<String, String>();
		for(Integer roomId : roomIds) {
			String sRoomId = String.valueOf(roomId);
			requests.put("roomid", sRoomId);
			Map<String, String> headers = toGetHeadParams(cookies, sRoomId);
			
			int cnt = 0;
			do {
				cnt = 0;
				String response = client.doGet(STORM_CHECK_URL, headers, requests);
				List<String> raffleIds = _getStormIds(roomId, response);
				cnt = _joinStorms(roomId, raffleIds);
				sum += cnt;
			} while(cnt > 0);	// 对于存在节奏风暴的房间, 再扫描一次(可能有人连续送节奏风暴)
			ThreadUtils.tSleep(scanInterval);
		}
		client.close();
		return sum;
	}
	
	/**
	 * 获取节奏风暴的礼物ID
	 * @param roomId
	 * @param response {"code":0,"msg":"","message":"","data":{"id":157283,"roomid":2717660,"num":100,"time":50,"content":"康康胖胖哒……！","hasJoin":0}}
	 * @return
	 */
	private static List<String> _getStormIds(int roomId, String response) {
		List<String> raffleIds = new LinkedList<String>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			Object data = json.get(BiliCmdAtrbt.data);
			if(data instanceof JSONObject) {
				JSONObject room = (JSONObject) data;
				int raffleId = JsonUtils.getInt(room, BiliCmdAtrbt.id, 0);
				if(raffleId > LAST_STORMID) {
					LAST_STORMID = raffleId;
					raffleIds.add(String.valueOf(raffleId));
				}
						
			} else if(data instanceof JSONArray) {
				JSONArray array = (JSONArray) data;
				for(int i = 0 ; i < array.size(); i++) {
					JSONObject room = array.getJSONObject(i);
					int raffleId = JsonUtils.getInt(room, BiliCmdAtrbt.id, 0);
					if(raffleId > LAST_STORMID) {
						LAST_STORMID = raffleId;
						raffleIds.add(String.valueOf(raffleId));
					}
				}
			}
		} catch(Exception e) {
			log.error("提取直播间 [{}] 的节奏风暴信息失败: {}", roomId, response, e);
		}
		return raffleIds;
	}
	
	/**
	 * 加入节奏风暴抽奖
	 * @param roomId
	 * @param raffleIds
	 * @return
	 */
	private static int _joinStorms(int roomId, List<String> raffleIds) {
		int cnt = 0;
		if(raffleIds.size() > 0) {
			String msg = StrUtils.concat("直播间 [", roomId, 
					"] 开启了节奏风暴 [x", raffleIds.size(), "] !!!");
			UIUtils.notify(msg);
			
			for(String raffleId : raffleIds) {
				cnt += (MsgSender.toStormLottery(roomId, raffleId) ? 1 : 0);
			}
		}
		return cnt;
	}
	
	/**
	 * 节奏风暴抽奖
	 * @param roomId
	 * @return
	 */
	public static boolean toStormLottery(int roomId, String raffleId) {
		boolean isOk = true;
		String errDesc = joinLottery(STORM_JOIN_URL, roomId, raffleId, Browser.COOKIES(), LotteryType.STORM);
		if(StrUtils.isEmpty(errDesc)) {
			log.info("参与直播间 [{}] 抽奖成功", roomId);
			UIUtils.statistics("成功(节奏风暴): 抽奖直播间 [", roomId, "]");
			UIUtils.updateLotteryCnt();
			
		} else {
			log.info("参与直播间 [{}] 抽奖失败: {}", roomId, errDesc);
			UIUtils.statistics("失败(", errDesc, "): 抽奖直播间 [", roomId, "]");
			isOk = false;
		}
		return isOk;
	}
	
	/**
	 * 小电视抽奖
	 * @param roomId
	 * @param raffleId
	 * @return
	 */
	public static String toTvLottery(int roomId, String raffleId) {
		return joinLottery(TV_JOIN_URL, roomId, raffleId, Browser.COOKIES(), LotteryType.TV);
	}
	
	/**
	 * 高能礼物抽奖
	 * @param roomId
	 * @return
	 */
	public static int toEgLottery(int roomId) {
		return toLottery(roomId, EG_CHECK_URL, EG_JOIN_URL);
	}
	
	/**
	 * 高能礼物抽奖
	 * @param roomId
	 * @param checkUrl
	 * @param joinUrl
	 * @return
	 */
	private static int toLottery(int roomId, String checkUrl, String joinUrl) {
		int cnt = 0;
		final String cookies = Browser.COOKIES();
		List<String> raffleIds = checkLottery(checkUrl, roomId, cookies);
		
		if(ListUtils.isNotEmpty(raffleIds)) {
			for(String raffleId : raffleIds) {
				int id = NumUtils.toInt(raffleId, 0);
				if(id > LAST_RAFFLEID) {	// 礼物编号是递增
					LAST_RAFFLEID = id;
					String errDesc = joinLottery(joinUrl, roomId, raffleId, cookies, LotteryType.OTHER);
					if(StrUtils.isEmpty(errDesc)) {
						cnt++;
					} else {
						if(!errDesc.contains("你已加入抽奖")) {
							UIUtils.statistics("失败(", errDesc, "): 抽奖直播间 [", roomId, "]");
						}
						log.info("参与直播间 [{}] 抽奖失败: {}", roomId, errDesc);
					}
				}
			}
		}
		return cnt;
	}
	
	/**
	 * 检查是否存在抽奖
	 * @param url
	 * @param roomId
	 * @param cookies
	 * @return
	 */
	private static List<String> checkLottery(String url, int roomId, String cookies) {
		List<String> raffleIds = new LinkedList<String>();
		int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		if(realRoomId > 0) {
			String sRoomId = String.valueOf(realRoomId);
			Map<String, String> headers = toGetHeadParams(cookies, sRoomId);
			Map<String, String> requests = _toLotteryRequestParams(sRoomId);
			String response = HttpURLUtils.doGet(url, headers, requests, Config.DEFAULT_CHARSET);
			raffleIds = _getRaffleId(response);
		} else {
			log.warn("获取礼物编号失败: 无效的房间号 [{}]", roomId);
		}
		return raffleIds;
	}
	
	/**
	 * 加入抽奖
	 * @param url
	 * @param roomId
	 * @param raffleId
	 * @param cookies
	 * @param type
	 * @return
	 */
	private static String joinLottery(String url, int roomId, String raffleId, 
			String cookies, LotteryType type) {
		String errDesc = "";
		int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		if(realRoomId > 0) {
			String sRoomId = String.valueOf(realRoomId);
			Map<String, String> headers = toGetHeadParams(cookies, sRoomId);
			Map<String, String> requests = (LotteryType.STORM == type ? 
					_toStormRequestParams(sRoomId, raffleId) : 
					_toLotteryRequestParams(sRoomId, raffleId));
			
			String response = HttpURLUtils.doPost(url, headers, requests, Config.DEFAULT_CHARSET);
			errDesc = _analyseLotteryResponse(response);
			
			// 系统繁忙哟，请再试一下吧
			if(errDesc.contains("系统繁忙")) {
				ThreadUtils.tSleep(1000);
				response = HttpURLUtils.doPost(url, headers, requests, Config.DEFAULT_CHARSET);
				errDesc = _analyseLotteryResponse(response);
			}
		} else {
			log.warn("参加抽奖失败: 无效的房间号 [{}]", roomId);
		}
		return errDesc;
	}
	
	/**
	 * 获取礼物编号
	 * @param response {"code":0,"msg":"success","message":"success","data":[{"raffleId":46506,"type":"openfire","from":"喵熊°","from_user":{"uname":"喵熊°","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg"},"time":60,"status":1},{"raffleId":46507,"type":"openfire","from":"喵熊°","from_user":{"uname":"喵熊°","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg"},"time":60,"status":1},{"raffleId":46508,"type":"openfire","from":"喵熊°","from_user":{"uname":"喵熊°","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg"},"time":60,"status":1},{"raffleId":46509,"type":"openfire","from":"喵熊°","from_user":{"uname":"喵熊°","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg"},"time":60,"status":1}]}
	 * @return
	 */
	private static List<String> _getRaffleId(String response) {
		List<String> raffleIds = new LinkedList<String>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONArray array = JsonUtils.getArray(json, BiliCmdAtrbt.data);
				for(int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					int raffleId = JsonUtils.getInt(obj, BiliCmdAtrbt.raffleId, 0);
					if(raffleId > 0) {
						raffleIds.add(String.valueOf(raffleId));
					}
				}
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取礼物编号失败: {}", reason);
			}
		} catch(Exception e) {
			log.error("获取礼物编号异常: {}", response, e);
		}
		return raffleIds;
	}
	
	private static Map<String, String> _toLotteryRequestParams(String realRoomId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("roomid", realRoomId);	// 正在抽奖的房间号
		return params;
	}
	
	private static Map<String, String> _toLotteryRequestParams(String realRoomId, String raffleId) {
		Map<String, String> params = _toLotteryRequestParams(realRoomId);
		params.put("raffleId", raffleId);	// 礼物编号
		return params;
	}
	
	private static Map<String, String> _toStormRequestParams(String realRoomId, String raffleId) {
		Map<String, String> params = _toLotteryRequestParams(realRoomId);
		params.put("id", raffleId);	// 礼物编号
		params.put("color", ChatColor.WHITE.CODE());
		params.put("captcha_token", "");
		params.put("captcha_phrase", "");
		params.put("token", "");
		params.put("csrf_token", Browser.CSRF());
		return params;
	}
	
	/**
	 * 
	 * @param response 
	 *   小电视     {"code":0,"msg":"加入成功","message":"加入成功","data":{"3392133":"small","511589":"small","8536920":"small","raffleId":"46506","1275939":"small","20177919":"small","12768615":"small","1698233":"small","4986301":"small","102015208":"small","40573511":"small","4799261":"small","from":"喵熊°","time":59,"30430088":"small","558038":"small","5599305":"small","8068250":"small","16293951":"small","7294374":"small","type":"openfire","7384826":"small","2229668":"small","7828145":"small","2322836":"small","915804":"small","86845000":"small","3076423":"small","roomid":"97835","5979210":"small","16345975":"small","7151219":"small","1479304":"small","19123719":"small","29129155":"small","7913373":"small","17049098":"small","9008673":"small","23406718":"small","141718":"small","27880394":"small","942837":"small","107844643":"small","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg","31437943":"small","34810599":"small","102994056":"small","31470791":"small","26643554":"small","29080508":"small","14709391":"small","14530810":"small","46520094":"small","2142310":"small","status":2,"77959868":"small","76979807":"small"}}
	 *   节奏风暴 {"code":0,"msg":"","message":"","data":{"gift_id":39,"title":"节奏风暴","content":"<p>你是前 35 位跟风大师<br />恭喜你获得一个亿圆(7天有效期)</p>","mobile_content":"你是前 35 位跟风大师","gift_img":"http://static.hdslb.com/live-static/live-room/images/gift-section/gift-39.png?2017011901","gift_num":1,"gift_name":"亿圆"}}
	 * @return
	 */
	private static String _analyseLotteryResponse(String response) {
		String errDesc = "";
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code != 0) {
				errDesc = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("参加抽奖失败: {}", errDesc);
			}
		} catch(Exception e) {
			log.error("参加抽奖失败: {}", response, e);
		}
		return errDesc;
	}
	
	
	/**
	 * 执行小学数学日常任务
	 * @param roomId
	 * @return 返回下次任务的时间点
	 */
	public static long doDailyTasks() {
		long nextTaskTime = -1;
		final int roomId = UIUtils.getCurRoomId();
		final int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		if(realRoomId <= 0) {
			return nextTaskTime;
		}
		final Map<String, String> header = toGetHeadParams(
				Browser.COOKIES(), String.valueOf(realRoomId));
		
		DailyTask task = checkTask(header);
		if(task != DailyTask.NULL) {
			nextTaskTime = task.getEndTime() * 1000;
			
			// 已到达任务执行时间
			if(nextTaskTime <= System.currentTimeMillis()) {
				if(!_doDailyTasks(header, task)) {
					nextTaskTime = -1;	// 标记不存在下一轮任务
				}
			}
		}
		return nextTaskTime;
	}
	
	/**
	 * 执行小学数学日常任务
	 * @param header
	 * @param task
	 * @return 是否存在下一轮任务
	 */
	private static boolean _doDailyTasks(Map<String, String> header, DailyTask task) {
		boolean isDone = false;
		do {
			int answer = 0;
			do {
				ThreadUtils.tSleep(SLEEP_TIME);
				answer = getAnswer(header);
			} while(answer <= 0);	// 若解析二维码图片失败, 则重新解析
			
			ThreadUtils.tSleep(SLEEP_TIME);
			isDone = doTask(header, task, answer);
		} while(!isDone);	// 若计算二维码结果错误, 则重新计算
		
		return task.existNext();
	}
	
	/**
	 * 提取当前的小学数学日常任务
	 * 
	 * {"code":0,"msg":"","data":{"minute":6,"silver":80,"time_start":1514015075,"time_end":1514015435,"times":3,"max_times":5}}
	 * {"code":0,"msg":"","data":{"minute":9,"silver":190,"time_start":1514036545,"time_end":1514037085,"times":3,"max_times":5}}
	 * @param header
	 * @return
	 */
	private static DailyTask checkTask(Map<String, String> header) {
		DailyTask task = DailyTask.NULL;
		String response = HttpURLUtils.doGet(CHECK_TASK_URL, header, null);
		
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				task = new DailyTask(json);
			}
		} catch(Exception e) {
			log.error("获取日常任务失败: {}", response, e);
		}
		return task;
	}
	
	/**
	 * 计算验证码图片的小学数学
	 * @param header
	 * @return
	 */
	private static int getAnswer(Map<String, String> header) {
		Map<String, String> request = new HashMap<String, String>();
		request.put("ts", String.valueOf(System.currentTimeMillis()));
		
		boolean isOk = HttpURLUtils.downloadByGet(VERCODE_PATH, VERCODE_URL, header, request);
		int answer = (isOk ? VercodeUtils.calculateImage(VERCODE_PATH) : 0);
		return answer;
	}
	
	/**
	 * 提交小学数学日常任务
	 * 
	 * {"code":0,"msg":"ok","data":{"silver":7266,"awardSilver":80,"isEnd":0}}
	 * {"code":-902,"msg":"验证码错误","data":[]}
	 * {"code":-903,"msg":"已经领取过这个宝箱","data":{"surplus":-25234082.15}}
	 * 
	 * @param header
	 * @param task
	 * @param answer
	 * @return
	 */
	private static boolean doTask(Map<String, String> header, DailyTask task, int answer) {
		Map<String, String> request = new HashMap<String, String>();
		request.put("time_start", String.valueOf(task.getBgnTime()));
		request.put("end_time", String.valueOf(task.getEndTime()));
		request.put("captcha", String.valueOf(answer));
		String response = HttpURLUtils.doGet(DO_TASK_URL, header, request);
		
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			isOk = (code == 0);
			if(isOk == true) {
				UIUtils.log("已完成小学数学任务: ", task.getCurRound(), "/", 
						task.getMaxRound(), "轮-", task.getStep(), "分钟");
			}
		} catch(Exception e) {
			log.error("执行日常任务失败: {}", response, e);
		}
		return isOk;
	}
	
	/**
	 * 获取B站link中心针对本插件的授权校验标签
	 * @param BILIBILI_URL B站link中心
	 * @return {"code":0,"msg":"OK","message":"OK","data":["W:M-亚絲娜","B:","T:20180301","V:2.0"]}
	 */
	public static String queryCertTags(final String BILIBILI_URL) {
		Map<String, String> headers = toGetHeadParams("");
		headers.put(HttpUtils.HEAD.KEY.HOST, LINK_HOST);
		headers.put(HttpUtils.HEAD.KEY.ORIGIN, LINK_URL);
		headers.put(HttpUtils.HEAD.KEY.REFERER, LINK_URL.concat("/p/world/index"));
		
		return HttpURLUtils.doGet(BILIBILI_URL, headers, null);
	}
	
	/**
	 * 发送私信
	 * @param sendId 发送账号的用户ID
	 * @param recvId 接收账号的用户ID
	 * @param msg 发送消息
	 * @return
	 */
	public static boolean sendPrivateMsg(String sendId, String recvId, String msg) {
		Map<String, String> headers = toPostHeadParams(Browser.COOKIES());
		headers.put(HttpUtils.HEAD.KEY.HOST, LINK_HOST);
		headers.put(HttpUtils.HEAD.KEY.ORIGIN, MSG_HOST);
		headers.put(HttpUtils.HEAD.KEY.REFERER, MSG_HOST);
		
		Map<String, String> requests = new HashMap<String, String>();
		requests.put("csrf_token", Browser.CSRF());
		requests.put("platform", "pc");
		requests.put("msg[sender_uid]", sendId);
		requests.put("msg[receiver_id]", recvId);
		requests.put("msg[receiver_type]", "1");
		requests.put("msg[msg_type]", "1");
		requests.put("msg[content]", StrUtils.concat("{\"content\":\"", msg, "\"}"));
		requests.put("msg[timestamp]", String.valueOf(System.currentTimeMillis() / 1000));
		
		String response = HttpURLUtils.doPost(MSG_URL, headers, requests);
		return _analyseMsgResponse(response);
	}
	
	/**
	 * 
	 * @param response  {"code":0,"msg":"ok","message":"ok","data":{"msg_key":6510413634042085687,"_gt_":0}}
	 * @return
	 */
	private static boolean _analyseMsgResponse(String response) {
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				isOk = true;
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("发送私信失败: {}", reason);
			}
		} catch(Exception e) {
			log.error("发送私信失败: {}", response, e);
		}
		return isOk;
	}
	
	/**
	 * 2018春节活动：查询当前红包奖池
	 * @return {"code":0,"msg":"success","message":"success","data":{"red_bag_num":2290,"round":70,"pool_list":[{"award_id":"guard-3","award_name":"舰长体验券（1个月）","stock_num":0,"exchange_limit":5,"user_exchange_count":5,"price":6699},{"award_id":"gift-113","award_name":"新春抽奖","stock_num":2,"exchange_limit":0,"user_exchange_count":0,"price":23333},{"award_id":"danmu-gold","award_name":"金色弹幕特权（1天）","stock_num":19,"exchange_limit":42,"user_exchange_count":42,"price":2233},{"award_id":"uname-gold","award_name":"金色昵称特权（1天）","stock_num":20,"exchange_limit":42,"user_exchange_count":42,"price":8888},{"award_id":"stuff-2","award_name":"经验曜石","stock_num":0,"exchange_limit":10,"user_exchange_count":10,"price":233},{"award_id":"title-89","award_name":"爆竹头衔","stock_num":0,"exchange_limit":10,"user_exchange_count":10,"price":888},{"award_id":"gift-3","award_name":"B坷垃","stock_num":0,"exchange_limit":1,"user_exchange_count":1,"price":450},{"award_id":"gift-109","award_name":"红灯笼","stock_num":0,"exchange_limit":500,"user_exchange_count":500,"price":15}],"pool":{"award_id":"award-pool","award_name":"刷新兑换池","stock_num":99999,"exchange_limit":0,"price":6666}}}
	 */
	public static String queryRedbagPool() {
		Map<String, String> headers = toGetHeadParams(Browser.COOKIES(), "pages/1703/spring-2018.html");
		Map<String, String> requests = new HashMap<String, String>();
		requests.put("_", String.valueOf(System.currentTimeMillis()));
		String response = HttpURLUtils.doGet(GET_REDBAG_URL, headers, requests);
		return response;
	}
	
	/**
	 * 2018春节活动：兑换红包
	 * @param id 奖品编号
	 * @param num 兑换数量
	 * @return 
	 * 	{"code":0,"msg":"OK","message":"OK","data":{"award_id":"stuff-3","red_bag_num":1695}}
	 * 	{"code":-404,"msg":"这个奖品已经兑换完啦，下次再来吧","message":"这个奖品已经兑换完啦，下次再来吧","data":[]}
	 */
	public static String exchangeRedbag(String id, int num) {
		Map<String, String> headers = toPostHeadParams(Browser.COOKIES(), "pages/1703/spring-2018.html");
		Map<String, String> requests = new HashMap<String, String>();
		requests.put("award_id", id);
		requests.put("exchange_num", String.valueOf(num));
		String response = HttpURLUtils.doPost(EX_REDBAG_URL, headers, requests);
		return response;
	}
	
}
