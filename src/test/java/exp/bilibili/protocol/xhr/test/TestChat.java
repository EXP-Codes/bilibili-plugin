package exp.bilibili.protocol.xhr.test;

import org.junit.Test;

import exp.bilibili.protocol.xhr.Chat;
import exp.libs.envm.Colors;

public class TestChat extends _Init {

	@Test
	public void testSendDanmu() {
		boolean isOk = Chat.sendDanmu(cookie, roomId, "Test Msg", Colors.GOLD);
		System.out.println(isOk);
	}

	@Test
	public void testSendPM() {
		// Undo
	}

}
