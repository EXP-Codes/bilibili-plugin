package exp.bilibili.protocol.xhr.test;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import exp.bilibili.plugin.envm.Area;
import exp.bilibili.protocol.xhr.LiveArea;
import exp.libs.utils.other.StrUtils;

public class TestLiveArea extends _Init {

	@Test
	public void testGetAreaTopOnes() {
		Map<Area, Integer> map = LiveArea.getAreaTopOnes();
		Iterator<Area> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			Area key = keys.next();
			Integer value = map.get(key);
			System.out.println(StrUtils.concat(key, " = ", value));
		}
	}

}
