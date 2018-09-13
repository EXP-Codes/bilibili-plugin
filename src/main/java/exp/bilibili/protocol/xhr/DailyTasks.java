package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.plugin.utils.VercodeUtils;
import exp.bilibili.protocol.bean.xhr.MathTask;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
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
	
	/** 检查小学数学任务URL */
	private final static String MATH_CHECK_URL = Config.getInstn().MATH_CHECK_URL();
	
	/** 执行小学数学任务URL */
	private final static String MATH_EXEC_URL = Config.getInstn().MATH_EXEC_URL();
	
	/** 获取小学数学任务验证码URL */
	private final static String MATH_CODE_URL = Config.getInstn().MATH_CODE_URL();
	
	/** 小学数学任务验证码图片的下载保存路径 */
	private final static String VERCODE_PATH = Config.getInstn().IMG_DIR().concat("/vercode.png");
	
	/** 小学数学任务重试间隔(验证码计算成功率只有90%左右, 失败后需重试) */
	private final static long SLEEP_TIME = 500L;
	
	/** 执行下次任务的延迟时间点（5分钟后） */
	private final static long NEXT_TASK_DELAY = 300000L;
	
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
		header.put(HttpUtils.HEAD.KEY.HOST, LIVE_HOST);
		header.put(HttpUtils.HEAD.KEY.ORIGIN, LINK_HOME);
		header.put(HttpUtils.HEAD.KEY.REFERER, LINK_HOME.concat("/p/center/index"));
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
				UIUtils.log("[", username, "] ", signType, "签到完成");
				
			} else if(!reason.contains("已签到") && !reason.contains("已领取")) {
				log.warn("[{}] {}签到失败: {}", username, signType, reason);
				if(!reason.contains("需要绑定手机号")) {
					nextTaskTime = System.currentTimeMillis() + NEXT_TASK_DELAY;
				}
			}
		} catch(Exception e) {
			nextTaskTime = System.currentTimeMillis() + NEXT_TASK_DELAY;
			log.error("[{}] {}签到失败: {}", username, signType, response, e);
		}
		return nextTaskTime;
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
			if(nextTaskTime <= System.currentTimeMillis()) {
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
		
		boolean isOk = HttpURLUtils.downloadByGet(VERCODE_PATH, MATH_CODE_URL, header, request);
		int answer = (isOk ? VercodeUtils.calculateExpressionImage(VERCODE_PATH) : -1);
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
				UIUtils.log("[", username, "] 小学数学任务进度: ", task.getCurRound(), "/", 
						task.getMaxRound(), "轮-", task.getStep(), "分钟");
				
			} else if(reason.contains("验证码错误")) {
				isOk = false;
				
			} else if(reason.contains("未绑定手机")) {
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
