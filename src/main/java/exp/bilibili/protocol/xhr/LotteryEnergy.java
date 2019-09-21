package exp.bilibili.protocol.xhr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.bean.ldm.Raffle;
import exp.bilibili.plugin.bean.ldm.Raffles;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 高能礼物抽奖
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LotteryEnergy extends _Lottery {
	
	/** 高能礼物取号URL */
	private final static String EG_CHECK_URL = Config.getInstn().EG_CHECK_URL();
	
	/** 高能礼物抽奖URL */
	private final static String EG_JOIN_URL = Config.getInstn().EG_JOIN_URL();
	
	/** 已经抽过的高能礼物ID */
	private final static Raffles RAFFLES = new Raffles();
	
	/** 私有化构造函数 */
	protected LotteryEnergy() {}
	
	/**
	 * 高能礼物抽奖
	 * @param roomId
	 * @return
	 */
	public static void toLottery(int roomId) {
		final List<Raffle> raffles = getRaffle(EG_CHECK_URL, roomId, CookiesMgr.MAIN().toNVCookie());
		if(raffles.isEmpty()) {
			return;
		}
		
		new Thread() {
			public void run() {
				while(raffles.size() > 0) {
					long now = System.currentTimeMillis();
					Iterator<Raffle> its = raffles.iterator();
					while(its.hasNext()) {
						Raffle raffle = its.next();
						if(now >= raffle.getDotime()) {
							toLottery(roomId, raffle);
							its.remove();
						}
						ThreadUtils.tSleep(100);
					}
					ThreadUtils.tSleep(1000);
				}
			};
		}.start();
	}
	
	/**
	 * 获取礼物编号
	 * @param response {"code":0,"message":"0","ttl":1,"data":{"pk":[],"guard":[],"gift":[{"raffleId":427802,"type":"GIFT_30035","from_user":{"uid":0,"uname":"sy阿四","face":"http://i2.hdslb.com/bfs/face/7b37c75d6467210cdd211a60c061b3b605156d1b.jpg"},"time_wait":19,"time":79,"max_time":180,"status":1,"sender_type":1,"asset_icon":"http://s1.hdslb.com/bfs/live/28c2f3dd68170391d173ca2efd02bdabc917df26.png","asset_animation_pic":"http://i0.hdslb.com/bfs/live/d7303a91bf00446b2bc53b8726844b4ad813b9ed.gif","thank_text":"感谢\u003c%sy阿四%\u003e 赠送的任意门","weight":0,"gift_id":30035}]}}
	 * @return
	 */
	private static List<Raffle> getRaffle(String url, int roomId, String cookie) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie, sRoomId);
		Map<String, String> request = getRequest(sRoomId);
		String response = HttpURLUtils.doGet(url, header, request);
		
		List<Raffle> raffles = new LinkedList<Raffle>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray array = JsonUtils.getArray(data, BiliCmdAtrbt.gift);
				for(int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Raffle raffle = new Raffle(obj);
					if(RAFFLES.add(raffle)) {
						raffles.add(raffle);
					}
				}
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取礼物编号失败: {}", reason);
			}
		} catch(Exception e) {
			log.error("获取礼物编号异常: {}", response, e);
		}
		return raffles;
	}
	
	/**
	 * 参加抽奖
	 * @param roomId
	 * @param raffle
	 */
	private static void toLottery(int roomId, Raffle raffle) {
		int cnt = 0;
		Set<BiliCookie> cookies = CookiesMgr.ALL();
		for(BiliCookie cookie : cookies) {
			if(!cookie.allowLottery() || !cookie.isBindTel()) {
				continue;	// 未绑定手机的账号无法参与高能抽奖
			}
			
			String reason = join(LotteryType.ENGERY, cookie, EG_JOIN_URL, roomId, raffle);
			if(StrUtils.isEmpty(reason)) {
				log.info("[{}] 参与直播间 [{}] 抽奖成功(高能礼物)", cookie.NICKNAME(), roomId);
				cookie.updateLotteryTime();
				cnt++;
				
			} else if(!reason.contains("已加入抽奖")) {
				log.info("[{}] 参与直播间 [{}] 抽奖失败(小电视/摩天楼/活动) : {}", cookie.NICKNAME(), roomId, reason);
				UIUtils.statistics("失败(", reason, "): 直播间 [", roomId, "],账号[", cookie.NICKNAME(), "]");
				
				if(reason.contains("访问被拒绝")) {
					cookie.freeze();
				}
				
				// 高能已过期, 其他账号无需参与
				if(reason.contains("已过期") || reason.contains("不存在")) {
					break;
				}
			}
			
			ThreadUtils.tSleep(200);
		}
		
		if(cnt > 0) {
			UIUtils.statistics("成功(高能x", cnt, "): 直播间 [", roomId, "]");
			UIUtils.updateLotteryCnt(cnt);
		}
	}
	
}
