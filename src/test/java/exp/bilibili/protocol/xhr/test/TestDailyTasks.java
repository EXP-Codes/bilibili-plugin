package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.plugin.utils.TimeUtils;
import exp.bilibili.protocol.xhr.DailyTasks;

public class TestDailyTasks extends _Init {

	@Test
	public void testToAssn() {
		long millis = DailyTasks.toAssn(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

	@Test
	public void testToSign() {
		long millis = DailyTasks.toSign(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

	@Test
	public void testReceiveDailyGift() {
		long millis = DailyTasks.receiveDailyGift(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

	@Test
	public void testReceiveHolidayGift() {
		long millis = DailyTasks.receiveHolidayGift(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

	@Test
	public void testOnlineHeartbeat() {
		long millis = DailyTasks.onlineHeartbeat(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

	@Test
	public void testDoMathTask() {
		long millis = DailyTasks.doMathTask(cookie);
		System.out.println(TimeUtils.toStr(millis));
	}

}
