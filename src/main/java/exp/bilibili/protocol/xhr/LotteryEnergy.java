package exp.bilibili.protocol.xhr;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.bean.ldm.Raffle;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;

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
