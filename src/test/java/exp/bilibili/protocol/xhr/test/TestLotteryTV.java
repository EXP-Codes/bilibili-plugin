package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.protocol.xhr.LotteryTV;

public class TestLotteryTV extends _Init {

	@Test
	public void testToLotteryInt() {
		LotteryTV.toLottery(roomId);
	}

	@Test
	public void testToLotteryIntString() {
		String raffleId = "";	// FIXME
		LotteryTV.toLottery(roomId, raffleId);
	}

}
