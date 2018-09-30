package exp.bilibili.plugin.envm;

/**
 * <PRE>
 * 船员类型
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GuardType {
	
	public final static GuardType CIVILIAN = new GuardType(0, "平民");
	
	public final static GuardType CAPTAIN = new GuardType(1, "舰长");
	
	public final static GuardType GOVERNOR = new GuardType(2, "提督");
	
	public final static GuardType VICEROY = new GuardType(3, "总督");
	
	/** 权限编号 */
	private int id;
	
	private String desc;
	
	private GuardType(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public int ID() {
		return id;
	}
	
	public String DESC() {
		return desc;
	}
	
	public static GuardType toGuardType(int id) {
		GuardType type = CIVILIAN;
		if(id == CAPTAIN.ID()) {
			type = CAPTAIN;
			
		} else if(id == GOVERNOR.ID()) {
			type = GOVERNOR;
			
		} else if(id == VICEROY.ID()) {
			type = VICEROY;
		}
		return type;
	}
	
	public static GuardType toGuardType(String desc) {
		GuardType type = CIVILIAN;
		if(CAPTAIN.DESC().equals(desc)) {
			type = CAPTAIN;
			
		} else if(GOVERNOR.DESC().equals(desc)) {
			type = GOVERNOR;
			
		} else if(VICEROY.DESC().equals(desc)) {
			type = VICEROY;
		}
		return type;
	}
	
}
