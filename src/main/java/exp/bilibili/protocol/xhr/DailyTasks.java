package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.plugin.utils.VercodeUtils;
import exp.bilibili.protocol.bean.xhr.MathTask;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.envm.HttpHead;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import exp.libs.warp.net.http.HttpUtils;

/**
 * <PRE>
 * 日常任务
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class DailyTasks extends __XHR {

	/** 日常签到URL */
	private final static String SIGN_URL = Config.getInstn().SIGN_URL();
	
	/** 友爱社签到URL */
	private final static String ASSN_URL = Config.getInstn().ASSN_URL();
	
	/** 领取日常/周常礼物URL */
	private final static String GIFT_URL = Config.getInstn().GIFT_URL();
	
	/** PING心跳URL */
	private final static String PING_HB_URL = Config.getInstn().PING_HB_URL();
	
	/** PONG心跳URL */
	private final static String PONG_HB_URL = Config.getInstn().PONG_HB_URL();
	
	/** 领取心跳礼物URL */
	private final static String HB_GIFT_URL = Config.getInstn().HB_GIFT_URL();
	
	/** 检查小学数学任务URL */
	private final static String MATH_CHECK_URL = Config.getInstn().MATH_CHECK_URL();
	
	/** 执行小学数学任务URL */
	private final static String MATH_EXEC_URL = Config.getInstn().MATH_EXEC_URL();
	
	/** 获取小学数学任务验证码URL */
	private final static String MATH_CODE_URL = Config.getInstn().MATH_CODE_URL();
	
	/** 图片缓存目录 */
	private final static String IMG_DIR = Config.getInstn().IMG_DIR();
	
	/** 小学数学任务重试间隔(验证码计算成功率只有90%左右, 失败后需重试) */
	private final static long SLEEP_TIME = 500L;
	
	/** 执行下次任务的延迟时间点（5分钟后） */
	private final static long DELAY_5_MIN = 300000L;
	
	/** 执行下次任务的延迟时间点（10分钟后） */
	private final static long DELAY_10_MIN = 600000L;
	
	/** 私有化构造函数 */
	protected DailyTasks() {}
	
	/**
	 * 友爱社签到
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long toAssn(BiliCookie cookie) {
		Map<String, String> header = getHeader(cookie.toNVCookie());
		Map<String, String> request = getRequest(cookie.CSRF());
		String response = HttpURLUtils.doPost(ASSN_URL, header, request);
		return analyse(response, cookie.NICKNAME(), true);
	}
	
	/**
	 * 有爱社请求头
	 * @param cookie
	 * @return
	 */
	private static Map<String, String> getHeader(String cookie) {
		Map<String, String> header = POST_HEADER(cookie);
		header.put(HttpHead.KEY.HOST, LIVE_HOST);
		header.put(HttpHead.KEY.ORIGIN, LINK_HOME);
		header.put(HttpHead.KEY.REFERER, LINK_HOME.concat("/p/center/index"));
		return header;
	}
	
	/**
	 * 友爱社请求参数
	 * @param csrf
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.task_id, "double_watch_task");
		request.put(BiliCmdAtrbt.csrf_token, csrf);
		return request;
	}
	
	/**
	 * 每日签到
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long toSign(BiliCookie cookie) {
		String roomId = getRealRoomId();
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), roomId);
		String response = HttpURLUtils.doGet(SIGN_URL, header, null);
		return analyse(response, cookie.NICKNAME(), false);
	}
	
	/**
	 * （友爱社/每日）签到结果解析
	 * @param response  {"code":0,"msg":"","message":"","data":[]}
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	private static long analyse(String response, String username, boolean assn) {
		long nextTaskTime = -1;
		String signType = (assn ? "友爱社" : "每日");
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
			if(code == 0) {
				sttclog.info("[{}] [{}] [{}] [{}]", signType, username, "T", reason);
				UIUtils.log("[", username, "] ", signType, "签到完成");
				
				// FIXME: 每日签到时, 顺便打印领取日常/周常礼包提示
				// （这些礼物如果没赠送，领取状态一直都是成功, 只能放在此处打印）
				if(assn == false) {
					UIUtils.log("[", username, "] 已领取日常/周常礼包(含签到/勋章/友爱社奖励)");
				}
				
			} else if(!reason.contains("已签到") && !reason.contains("已领取")) {
				sttclog.info("[{}] [{}] [{}] [{}]", signType, username, "F", reason);
				log.warn("[{}] {}签到失败: {}", username, signType, reason);
				if(!reason.contains("需要绑定手机号")) {
					nextTaskTime = System.currentTimeMillis() + DELAY_5_MIN;
				}
			}
		} catch(Exception e) {
			nextTaskTime = System.currentTimeMillis() + DELAY_5_MIN;
			log.error("[{}] {}签到失败: {}", username, signType, response, e);
		}
		return nextTaskTime;
	}
	
	/**
	 * 领取日常/周常礼包(含签到/勋章/友爱社奖励)
	 *  {"code":0,"msg":"success","message":"success","data":{"bag_status":2,"bag_expire_status":1,"bag_list":[{"type":1,"bag_name":"粉丝勋章礼包","source":{"medal_id":"571606","medal_name":"翘李吗","level":17},"gift_list":[{"gift_id":"6","gift_num":4,"expire_at":1520524800}]}],"time":1520438809}}
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long receiveDailyGift(BiliCookie cookie) {
		String roomId = getRealRoomId();
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), roomId);
		String response = HttpURLUtils.doGet(GIFT_URL, header, null);
		
		long nextTaskTime = System.currentTimeMillis() + DELAY_10_MIN;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				nextTaskTime = -1;
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray bagList = JsonUtils.getArray(data, BiliCmdAtrbt.bag_list);
				if(!bagList.isEmpty()) {
					
					// FIXME: 这些礼物如果没赠送，领取状态一直都是成功
					// 因此暂时把领取成功的提示放到每日签到时一起打印
					log.info("[{}] 已领取日常/周常礼包(含签到/勋章/友爱社奖励)", cookie.NICKNAME());
				}
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("[{}] 领取日常/周常礼包失败: {}", cookie.NICKNAME(), reason);
			}
		} catch(Exception e) {
			log.error("[{}] 领取日常/周常礼包失败: {}", cookie.NICKNAME(), response, e);
		}
		return nextTaskTime;
	}
	
	/**
	 * 领取活动心跳礼物（每在线10分钟领取一个xxx）
	 * {"code":0,"msg":"success","message":"success","data":{"gift_list":{"115":{"gift_id":115,"gift_name":"桃花","bag_id":67513170,"gift_num":1,"day_num":1,"day_limit":6}},"heart_status":1,"heart_time":300}}
	 * {"code":0,"msg":"success","message":"success","data":{"gift_list":null,"heart_status":1,"heart_time":300}}
	 * {"code":0,"msg":"success","message":"success","data":{"gift_list":[],"heart_status":1,"heart_time":300}}
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	@SuppressWarnings("unchecked")
	public static long receiveHolidayGift(BiliCookie cookie) {
		String roomId = getRealRoomId();
		holidayHeartbeat(cookie, roomId);
		
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), roomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, roomId);
		request.put(BiliCmdAtrbt.area_v2_id, "0");	// 当前主播所在的直播分区
		String response = HttpURLUtils.doGet(HB_GIFT_URL, header, request);
		
		long nextTaskTime = System.currentTimeMillis() + DELAY_10_MIN;
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
			Object obj = data.get(BiliCmdAtrbt.gift_list);
			if(obj instanceof JSONObject) {
				JSONObject giftList = (JSONObject) obj;
				Set<String> keys = giftList.keySet();
				for(String key : keys) {
					JSONObject gift = giftList.getJSONObject(key);
					int dayNum = JsonUtils.getInt(gift, BiliCmdAtrbt.day_num, -1);
					int dayLimit = JsonUtils.getInt(gift, BiliCmdAtrbt.day_limit, 0);
					if(dayNum >= dayLimit) {
						nextTaskTime = -1;
					}
					
					UIUtils.log("[", cookie.NICKNAME(), "] 已领取活动礼物: ", dayNum, "/", dayLimit);
					break;
				}
			}
		} catch(Exception e) {
			log.error("[{}] 领取活动礼物失败: {}", cookie.NICKNAME(), response, e);
		}
		return nextTaskTime;
	}
	
	/**
	 * 活动心跳
	 * @param cookie
	 */
	private static void holidayHeartbeat(BiliCookie cookie, String roomId) {
		
		// ping心跳
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), roomId);
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.csrf_token, cookie.CSRF());
		HttpURLUtils.doPost(PING_HB_URL, header, request);
		
		// pong心跳
		header.clear();
		request.clear();
		header = GET_HEADER(cookie.toNVCookie(), roomId);
		request.put(BiliCmdAtrbt.underline, String.valueOf(System.currentTimeMillis()));
		HttpURLUtils.doGet(PONG_HB_URL, header, request);
	}
	
	/**
	 * 执行小学数学任务
	 * @param cookie
	 * @return 返回执行下次任务的时间点(<=0表示已完成该任务)
	 */
	public static long doMathTask(BiliCookie cookie) {
		long nextTaskTime = -1;
		String roomId = getRealRoomId();
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), roomId);
		
		MathTask task = checkMathTask(header);
		if(task != MathTask.NULL) {
			nextTaskTime = task.getEndTime() * 1000;
			
			// 已到达任务执行时间
			if(nextTaskTime > 0 && nextTaskTime <= System.currentTimeMillis()) {
				if(!doMathTask(header, cookie.NICKNAME(), task)) {
					nextTaskTime = -1;	// 标记不存在下一轮任务
				}
			}
		}
		return nextTaskTime;
	}
	
	/**
	 * 执行小学数学任务
	 * @param header
	 * @param username
	 * @param task
	 * @return 是否存在下一轮任务
	 */
	private static boolean doMathTask(Map<String, String> header, 
			String username, MathTask task) {
		for(int retry = 0; retry < 5; retry++) {	// 最多重试5次验证码, 避免阻塞抽奖
			int answer = calculateAnswer(header);
			if(answer >= 0) {
				if(execMathTask(header, username, task, answer)) {
					break;
				} else {
					ThreadUtils.tSleep(SLEEP_TIME);
				}
			}
		}
		return task.existNext();
	}
	
	/**
	 * 提取小学数学日常任务
	 * {"code":0,"msg":"","data":{"minute":6,"silver":80,"time_start":1514015075,"time_end":1514015435,"times":3,"max_times":5}}
	 * {"code":0,"msg":"","data":{"minute":9,"silver":190,"time_start":1514036545,"time_end":1514037085,"times":3,"max_times":5}}
	 * @param header
	 * @return
	 */
	private static MathTask checkMathTask(Map<String, String> header) {
		MathTask task = MathTask.NULL;
		String response = HttpURLUtils.doGet(MATH_CHECK_URL, header, null);
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				task = new MathTask(json);
			}
		} catch(Exception e) {
			log.error("获取小学数学任务失败: {}", response, e);
		}
		return task;
	}
	
	/**
	 * 计算验证码图片的小学数学题
	 * @param header
	 * @return
	 */
	private static int calculateAnswer(Map<String, String> header) {
		Map<String, String> request = new HashMap<String, String>();
		request.put("ts", String.valueOf(System.currentTimeMillis()));
		String response = HttpURLUtils.doGet(MATH_CODE_URL, header, request);
		
		int answer = -1;
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
			String img = JsonUtils.getStr(data, BiliCmdAtrbt.img);
			String imgPath = HttpUtils.convertBase64Img(img, IMG_DIR, "vercode");
			answer = VercodeUtils.calculateImageExpression(imgPath);
			
		} catch(Exception e) {
			log.error("下载小学数学验证码图片失败", e);
		}
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
	 * @return 是否执行成功
	 */
	private static boolean execMathTask(Map<String, String> header, 
			String username, MathTask task, int answer) {
		Map<String, String> request = getRequest(task, answer);
		String response = HttpURLUtils.doGet(MATH_EXEC_URL, header, request);
		
		boolean isOk = false;
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
			if(code == 0) {
				isOk = true;
				sttclog.info("[{}] [{}] [{}] [{}]", "MATH", username, "T", reason);
				UIUtils.log("[", username, "] 小学数学任务进度: ", task.getCurRound(), "/", 
						task.getMaxRound(), "轮-", task.getStep(), "分钟");
				
			} else if(reason.contains("验证码错误")) {
				sttclog.info("[{}] [{}] [{}] [{}]", "MATH", username, "F", reason);
				isOk = false;
				
			} else if(reason.contains("访问被拒绝")) {
				sttclog.info("[{}] [{}] [{}] [{}]", "MATH", username, "F", reason);
				isOk = true;	// 账号被临时冻结，等待下一轮再重试
				
			} else if(reason.contains("未绑定手机") || reason.contains("已经领完")) {
				sttclog.info("[{}] [{}] [{}] [{}]", "MATH", username, "T", reason);
				isOk = true;
				task.setExistNext(false);	// 标记不存在下一轮任务
			}
		} catch(Exception e) {
			log.error("[{}] 执行小学数学任务失败: {}", username, response, e);
		}
		return isOk;
	}
	
	/**
	 * 执行小学数学任务请求参数
	 * @param task
	 * @param answer
	 * @return
	 */
	private static Map<String, String> getRequest(MathTask task, int answer) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.time_start, String.valueOf(task.getBgnTime()));
		request.put(BiliCmdAtrbt.end_time, String.valueOf(task.getEndTime()));
		request.put(BiliCmdAtrbt.captcha, String.valueOf(answer));
		return request;
	}
	
}
