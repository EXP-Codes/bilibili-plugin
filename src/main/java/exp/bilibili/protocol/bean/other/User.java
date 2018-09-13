package exp.bilibili.protocol.bean.other;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 主播/房管/用户对象
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class User {

	public final static User NULL = new User("", "", 0);
	
	private String id;
	
	private String name;
	
	private int level;
	
	public User(String id, String name) {
		this(id, name, 0);
	}
	
	public User(String id, String name, int level) {
		this.id = (id == null ? "" : id);
		this.name = (name == null ? "" : name);
		this.level = (level < 0 ? 0 : level);
	}
	
	public String ID() {
		return id;
	}
	
	public String NAME() {
		return name;
	}
	
	public int LV() {
		return level;
	}
	
	@Override
	public String toString() {
		return StrUtils.concat(ID(), "(", LV(), ")", ":", NAME());
	}
	
}
