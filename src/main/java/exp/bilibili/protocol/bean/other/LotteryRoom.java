package exp.bilibili.protocol.bean.other;

import exp.bilibili.plugin.envm.LotteryType;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 抽奖房间
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LotteryRoom {

	/** 抽奖房间号 */
	private int roomId;
	
	/** 抽奖所在直播间地址 */
	private String url;
	
	/** 抽奖编号 */
	private String raffleId;
	
	/** 抽奖开始时间 */
	private long startTime;
	
	/** 抽奖类型 */
	private LotteryType type;
	
	public LotteryRoom(int roomId, String url, String raffleId, LotteryType type) {
		this.roomId = roomId;
		this.url = (StrUtils.isEmpty(url) ? "" : url);
		this.raffleId = (StrUtils.isEmpty(raffleId) ? "" : raffleId);
		this.startTime = System.currentTimeMillis();
		this.type = (type == null ? LotteryType.ENGERY : type);
	}

	public int getRoomId() {
		return roomId;
	}

	public String getUrl() {
		return url;
	}

	public String getRaffleId() {
		return raffleId;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public LotteryType TYPE() {
		return type;
	}

}
