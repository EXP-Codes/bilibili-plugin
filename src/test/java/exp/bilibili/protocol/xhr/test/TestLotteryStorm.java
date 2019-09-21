package exp.bilibili.protocol.xhr.test;

import java.util.List;

import org.junit.Test;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.protocol.xhr.LotteryStorm;

public class TestLotteryStorm extends _Init {

	@Test
	public void testQueryHotLiveRoomIds() {
		HotLiveRange range = new HotLiveRange(1, 2);
		List<Integer> roomIds = LotteryStorm.queryHotLiveRoomIds(range);
		System.out.println(roomIds);
	}

	@Test
	public void testToLotteryListOfIntegerLong() {
		HotLiveRange range = new HotLiveRange(1, 2);
		List<Integer> hotRoomIds = LotteryStorm.queryHotLiveRoomIds(range);
		long scanInterval = Config.getInstn().STORM_FREQUENCY();
		LotteryStorm.toLottery(hotRoomIds, scanInterval);
	}

	@Test
	public void testToLotteryIntString() {
		String raffleId = ""; 	// FIXME
		LotteryStorm.toLottery(roomId, raffleId);
	}

}
