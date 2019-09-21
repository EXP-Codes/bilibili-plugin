package exp.bilibili.protocol.xhr.test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import exp.bilibili.protocol.bean.xhr.BagGift;
import exp.bilibili.protocol.bean.xhr.Medal;
import exp.bilibili.protocol.xhr.Gifts;
import exp.libs.utils.other.StrUtils;

public class TestGifts extends _Init {

	@Test
	public void testQueryBagList() {
		List<BagGift> bagGifts = Gifts.queryBagList(cookie, roomId);
		for(BagGift bagGift : bagGifts) {
			System.out.println(bagGift);
		}
	}

	@Test
	public void testQuerySilver() {
		int num = Gifts.querySilver(cookie);
		System.out.println(num);
	}

	@Test
	public void testFeed() {
		// Undo
	}

	@Test
	public void testQueryCapsuleCoin() {
		int num = Gifts.queryCapsuleCoin(cookie);
		System.out.println(num);
	}

	@Test
	public void testOpenCapsuleCoin() {
		boolean isOk = Gifts.openCapsuleCoin(cookie, 1);
		System.out.println(isOk);
	}

	@Test
	public void testQueryMedals() {
		Map<Integer, Medal> map = Gifts.queryMedals(cookie);
		Iterator<Integer> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			Integer key = keys.next();
			Medal value = map.get(key);
			System.out.println(StrUtils.concat(key, " = ", value));
		}
	}

}
