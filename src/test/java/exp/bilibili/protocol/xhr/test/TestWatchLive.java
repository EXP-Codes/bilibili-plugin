package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.protocol.xhr.WatchLive;

public class TestWatchLive extends _Init {

	@Test
	public void testToWatchPCLive() {
		WatchLive.toWatchPCLive(cookie, roomId);
	}
	
	@Test
	public void testToWatchAppLive() {
		WatchLive.toWatchAppLive(cookie, roomId);
	}
	
}
