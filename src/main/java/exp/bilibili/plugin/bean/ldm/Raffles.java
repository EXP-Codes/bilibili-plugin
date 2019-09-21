package exp.bilibili.plugin.bean.ldm;

import java.util.HashSet;
import java.util.Set;

/**
 * <PRE>
 * 抽奖礼物表 (服务器返还的是乱序列表, 不能使用递增ID流水方式进行筛选, 因此通过一定大小的缓存进行记录)
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Raffles {

	/** 避免内存溢出, 最多缓存256个 */
	private final static int LIMIT = 256; 
	
	private Set<Raffle> raffles;
	
	public Raffles() {
		this.raffles = new HashSet<Raffle>();
	}
	
	public boolean add(Raffle raffle) {
		boolean isOk = raffle != null && raffles.add(raffle);
		if(raffles.size() >= LIMIT) {
			raffles.clear();
		}
		return isOk;
	}
	
	public void clear() {
		raffles.clear();
	}
	
}
