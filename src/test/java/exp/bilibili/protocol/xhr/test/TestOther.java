package exp.bilibili.protocol.xhr.test;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import exp.bilibili.protocol.bean.other.User;
import exp.bilibili.protocol.bean.xhr.Achieve;
import exp.bilibili.protocol.xhr.Other;

public class TestOther extends _Init {

	@Test
	public void testQueryCertificateTags() {
		String rst = Other.queryCertificateTags();
		System.out.println(rst);
	}

	@Test
	public void testQueryServerConfig() {
		String rst = Other.queryServerConfig(cookie, roomId);
		System.out.println(rst);
	}

	@Test
	public void testQueryUserInfo() {
		User user = Other.queryUpInfo(roomId);
		System.out.println(user);
	}

	@Test
	public void testQueryUserSafeInfo() {
		System.out.println("[B] isBindTel:" + cookie.isBindTel());
		System.out.println("[B] isRealName:" + cookie.isRealName());
		Other.queryUserSafeInfo(cookie);
		System.out.println("[A] isBindTel:" + cookie.isBindTel());
		System.out.println("[A] isRealName:" + cookie.isRealName());
	}

	@Test
	public void testQueryUserAuthorityInfo() {
		System.out.println("[B] isRoomAdmin:" + cookie.isRoomAdmin());
		System.out.println("[B] isVip:" + cookie.isVip());
		System.out.println("[B] isGuard:" + cookie.isGuard());
		Other.queryUserAuthorityInfo(cookie, roomId);
		System.out.println("[A] isRoomAdmin:" + cookie.isRoomAdmin());
		System.out.println("[A] isVip:" + cookie.isVip());
		System.out.println("[A] isGuard:" + cookie.isGuard());
	}

	@Test
	public void testQueryUpInfo() {
		User user = Other.queryUpInfo(roomId);
		System.out.println(user);
	}

	@Test
	public void testQueryManagers() {
		Set<User> users = Other.queryManagers(roomId);
		for(User user : users) {
			System.out.println(user);
		}
	}

	@Test
	public void testBlockUser() {
		// Undo
	}

	@Test
	public void testQueryAchieve() {
		List<Achieve> achieves = Other.queryAchieve(cookie);
		for(Achieve achieve : achieves) {
			System.out.println(achieve);
		}
	}

	@Test
	public void testDoAchieve() {
		// Undo
	}

	@Test
	public void testSearchRoomId() {
		String rst = Other.queryServerConfig(cookie, roomId);
		System.out.println(rst);
	}

	@Test
	public void testEntryRoomBiliCookieInt() {
		Other.entryRoom(cookie, roomId);
	}

	@Test
	public void testEntryRoomBiliCookieIntString() {
		// Undo
	}

}
