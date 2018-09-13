package exp.bilibili.plugin.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <PRE>
 * 在线用户管理器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class OnlineUserMgr {

	private Set<String> users;
	
	private static volatile OnlineUserMgr instance;
	
	private OnlineUserMgr() {
		this.users = new HashSet<String>();
	}
	
	public static OnlineUserMgr getInstn() {
		if(instance == null) {
			synchronized (OnlineUserMgr.class) {
				if(instance == null) {
					instance = new OnlineUserMgr();
				}
			}
		}
		return instance;
	}
	
	public void add(String username) {
		users.add(username);
	}
	
	public void del(String username) {
		users.remove(username);
	}
	
	public void clear() {
		users.clear();
	}
	
	public List<String> getAllUsers() {
		return new ArrayList<String>(users);
	}
	
}
