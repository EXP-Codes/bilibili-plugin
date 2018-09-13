package exp.bilibili.plugin.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exp.bilibili.protocol.XHRSender;
import exp.bilibili.protocol.bean.other.User;
import exp.libs.utils.other.StrUtils;

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

	/** 当前监听直播间的在线用户 */
	private Set<String> users;
	
	/**
	 * 当前监听直播间的房管列表(含主播)
	 * uname -> uid
	 */
	private Map<String, String> managers;
	
	/** 被举报用户 -> 举报群众列表 */
	private Map<String, Set<String>> blacks;
	
	private static volatile OnlineUserMgr instance;
	
	private OnlineUserMgr() {
		this.users = new HashSet<String>();
		this.managers = new HashMap<String, String>();
		this.blacks = new HashMap<String, Set<String>>();
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
	
	public void clear() {
		users.clear();
		managers.clear();
		blacks.clear();
	}
	
	public void addOnlineUser(String username) {
		users.add(username);
	}
	
	/**
	 * 从当前直播间的在线用户列表中, 找到昵称最接近的用户
	 * @param unameKey 目标用户名的关键字
	 * @return 
	 */
	public List<String> findOnlineUser(String unameKey) {
		List<String> usernames = new LinkedList<String>();
		if(StrUtils.isEmpty(unameKey)) {
			// Undo
			
		} else if(users.contains(unameKey)) {
			usernames.add(unameKey);
			
		} else {
			Iterator<String> usernameIts = users.iterator();
			while(usernameIts.hasNext()) {
				String username = usernameIts.next();
				if(username.contains(unameKey)) {
					usernames.add(username);
				}
			}
		}
		return usernames;
	}
	
	public List<String> getAllOnlineUsers() {
		return new ArrayList<String>(users);
	}
	
	public void updateManagers() {
		managers.clear();
		Set<User> users = XHRSender.queryManagers();
		for(User user : users) {
			managers.put(user.NAME(), user.ID());
		}
	}
	
	public boolean isManager(String username) {
		return managers.containsKey(username);
	}
	
	public String getManagerID(String username) {
		String uid = managers.get(username);
		return (uid == null ? "" : uid);
	}
	
	/**
	 * 举报
	 * @param accuser 举报人
	 * @param accused 被举报人
	 * @return 被举报次数
	 */
	public int complaint(String accuser, String accused) {
		Set<String> accusers = blacks.get(accused);
		if(accusers == null) {
			accusers = new HashSet<String>();
			blacks.put(accused, accusers);
		}
		return (accusers.add(accuser) ? accusers.size() : 0);
	}
	
	/**
	 * 撤销举报
	 * @param accused 被举报人
	 */
	public void cancel(String accused) {
		blacks.remove(accused);
	}
	
}
