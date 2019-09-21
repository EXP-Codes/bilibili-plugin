package exp.bilibili.protocol.xhr.test;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import exp.bilibili.plugin.envm.GuardType;
import exp.bilibili.protocol.xhr.Guard;
import exp.libs.utils.other.StrUtils;

public class TestGuard extends _Init {

	@Test
	public void testCheckGuardIds() {
		Map<String, GuardType> map = Guard.checkGuardIds(cookie, roomId);
		Iterator<String> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			GuardType value = map.get(key);
			System.out.println(StrUtils.concat(key, " = ", value));
		}
	}

	@Test
	public void testGetGuardGiftInt() {
		int num = Guard.getGuardGift(roomId);
		System.out.println(num);
	}

	@Test
	public void testGetGuardGiftBiliCookieIntStringGuardType() {
		// Undo
	}

}
