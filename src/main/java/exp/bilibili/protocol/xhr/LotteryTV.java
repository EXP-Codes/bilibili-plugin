package exp.bilibili.protocol.xhr;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 小电视抽奖
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LotteryTV extends _Lottery {
	
	/** 小电视取号URL */
	private final static String TV_CHECK_URL = Config.getInstn().TV_CHECK_URL();
	
	/** 小电视抽奖URL */
	private final static String TV_JOIN_URL = Config.getInstn().TV_JOIN_URL();
	
	/** 已经抽过的小电视ID (服务返还的是乱序列表, 不能使用递增ID流水方式进行筛选) */
	private final static Set<String> RAFFLEIDS = new HashSet<String>();
	
	/** 私有化构造函数 */
	protected LotteryTV() {}
	
	/**
	 * 小电视抽奖
	 * @param roomId
	 * @return
	 */
	public static void toLottery(int roomId) {
		List<String> raffleIds = getRaffleId(TV_CHECK_URL, roomId, 
				CookiesMgr.MAIN().toNVCookie());
		for(String raffleId : raffleIds) {
			if(RAFFLEIDS.add(raffleId)) {
				toLottery(roomId, raffleId);
			}
		}
		
		// 避免内存溢出, 最多缓存128个小电视ID
		if(RAFFLEIDS.size() >= 128) {
			RAFFLEIDS.clear();
		}
	}
	
	/**
	 * 获取礼物编号
	 * @param response {"code":0,"msg":"OK","message":"OK","data":{"last_raffle_id":0,"last_raffle_type":"small_tv","asset_animation_pic":"https://i0.hdslb.com/bfs/live/746a8db0702740ec63106581825667ae525bb11a.gif","asset_tips_pic":"https://i0.hdslb.com/bfs/live/f9924d492fe8bc77bb706480d9d006aaef9ed5f3.png","list":[{"raffleId":52793,"title":"小电视飞船抽奖","type":"small_tv","from":"允宝贝爱吃梨","from_user":{"uname":"允宝贝爱吃梨","face":"https://i0.hdslb.com/bfs/face/f4506c5a8ee5b3cb82eff6093cfa2950d16022fd.jpg"},"time":119,"max_time":180,"status":1,"asset_animation_pic":"https://i0.hdslb.com/bfs/live/746a8db0702740ec63106581825667ae525bb11a.gif","asset_tips_pic":"https://i0.hdslb.com/bfs/live/f9924d492fe8bc77bb706480d9d006aaef9ed5f3.png"}]}}
	 * @return
	 */
	private static List<String> getRaffleId(String url, int roomId, String cookie) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie, sRoomId);
		Map<String, String> request = getRequest(sRoomId);
		String response = HttpURLUtils.doGet(url, header, request);
		
		List<String> raffleIds = new LinkedList<String>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray list = JsonUtils.getArray(data, BiliCmdAtrbt.list);
				for(int i = 0; i < list.size(); i++) {
					JSONObject obj = list.getJSONObject(i);
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
	
	/**
	 * 参加小电视抽奖
	 * @param roomId
	 * @param raffleId
	 * @return
	 */
	public static void toLottery(int roomId, String raffleId) {
		int cnt = 0;
		Set<BiliCookie> cookies = CookiesMgr.ALL();
		for(BiliCookie cookie : cookies) {
			if(cookie.allowLottery() == false) {
				continue;
			}
			
			String reason = join(LotteryType.TV, cookie, TV_JOIN_URL, roomId, raffleId);
			if(StrUtils.isEmpty(reason)) {
				sttclog.info("[{}] [{}] [{}] [{}] [{}]", "TV", roomId, cookie.NICKNAME(), "T", reason);
				log.info("[{}] 参与直播间 [{}] 抽奖成功(小电视/摩天楼/活动)", cookie.NICKNAME(), roomId);
				cookie.updateLotteryTime();
				cnt++;
				
			} else {
				sttclog.info("[{}] [{}] [{}] [{}] [{}]", "TV", roomId, cookie.NICKNAME(), "F", reason);
				log.info("[{}] 参与直播间 [{}] 抽奖失败(小电视/摩天楼/活动)", cookie.NICKNAME(), roomId);
				UIUtils.statistics("失败(", reason, "): 直播间 [", roomId, 
						"],账号[", cookie.NICKNAME(), "]");
				
				if(reason.contains("访问被拒绝")) {
					cookie.freeze();
				}
				
				// 小电视已过期, 其他账号无需参与
				if(reason.contains("已过期") || reason.contains("不存在")) {
					break;
				}
			}
			
			ThreadUtils.tSleep(200);
		}
		
		if(cnt > 0) {
			UIUtils.statistics("成功(小电视/摩天楼/活动x", cnt, "): 直播间 [", roomId, "]");
			UIUtils.updateLotteryCnt(cnt);
		}
	}
	
}
