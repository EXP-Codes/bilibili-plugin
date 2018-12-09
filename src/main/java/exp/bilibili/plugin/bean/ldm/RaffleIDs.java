package exp.bilibili.plugin.bean.ldm;

import java.util.HashSet;
import java.util.Set;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 抽奖ID表 (服务器返还的是乱序列表, 不能使用递增ID流水方式进行筛选, 因此通过一定大小的缓存进行记录)
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RaffleIDs {

	/** 避免内存溢出, 最多缓存128个ID */
	private final static int LIMIT = 128; 
	
	private Set<String> ids;
	
	public RaffleIDs() {
		this.ids = new HashSet<String>();
	}
	
	public boolean add(String id) {
		boolean isOk = StrUtils.isNotEmpty(id) && ids.add(id);
		if(ids.size() >= LIMIT) {
			ids.clear();
		}
		return isOk;
	}
	
	public void clear() {
		ids.clear();
	}
	
}
