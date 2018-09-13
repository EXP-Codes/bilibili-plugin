package exp.bilibili.plugin.envm;

public class LoginType {

	public final static LoginType MAIN = new LoginType(1, "主号");
	
	public final static LoginType MINI = new LoginType(2, "小号");
	
	public final static LoginType VEST = new LoginType(3, "马甲号");
	
	public final static LoginType TEMP = new LoginType(3, "临时号");
	
	private int id;
	
	private String desc;
	
	private LoginType(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public int ID() {
		return id;
	}
	
	public String DESC() {
		return desc;
	}
	
	@Override
	public String toString() {
		return DESC();
	}
	
}
