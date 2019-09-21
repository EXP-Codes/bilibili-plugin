package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.plugin.bean.ldm.Raffle;
import exp.bilibili.protocol.xhr.LotteryTV;

public class TestLotteryTV extends _Init {

	@Test
	public void testToLotteryInt() {
		LotteryTV.toLottery(roomId);
	}

	@Test
	public void testToLotteryIntString() {
		Raffle raffle = new Raffle(); 	// FIXME
		LotteryTV.toLottery(roomId, raffle);
	}

}
