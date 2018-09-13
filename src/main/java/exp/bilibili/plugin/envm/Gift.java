package exp.bilibili.plugin.envm;

/**
 * <PRE>
 * 各种礼物对应的活跃度枚举
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Gift {

	public final static Gift CHAT = new Gift("", "弹幕", 1);
	
	public final static Gift HOT_STRIP = new Gift("1", "辣条", 100);
	
	public final static Gift MILLION = new Gift("6", "亿圆", 1000);
	
	public final static Gift MEOW = new Gift("4", "喵娘", 5200);
	
	public final static Gift B_CLOD = new Gift("3", "B坷垃", 9900);
	
	public final static Gift STORM = new Gift("39", "节奏风暴", 100000);
	
	public final static Gift TV = new Gift("25", "小电视", 1245000);
	
	public final static Gift _233 = new Gift("8", "233", 233);
	
	public final static Gift _666 = new Gift("7", "666", 666);
	
	public final static Gift CAPTAIN = new Gift("", "舰长", 198000);
	
	public final static Gift ADMIRAL = new Gift("", "提督", 1998000);
	
	public final static Gift GOVERNOR = new Gift("", "总督", 19998000);
	
	public final static Gift LANTERN = new Gift("109", "红灯笼", 2000);
	
	public final static Gift SQUIB = new Gift("110", "小爆竹", 2000);
	
	/** 礼物ID */
	private String id;
	
	/** 礼物名称 */
	private String name;
	
	/** 价值/活跃值 */
	private int cost;
	
	private Gift(String id, String name, int cost) {
		this.id = id;
		this.name = name;
		this.cost = cost;
	}
	
	public String ID() {
		return id;
	}
	
	public String NAME() {
		return name;
	}
	
	public int COST() {
		return cost;
	}
	
	public static int getCost(String giftName) {
		int cost = 0;
		if(CHAT.NAME().equals(giftName)) {
			cost = CHAT.COST();
			
		} else if(HOT_STRIP.NAME().equals(giftName)) {
			cost = HOT_STRIP.COST();
			
		} else if(MILLION.NAME().equals(giftName)) {
			cost = MILLION.COST();
			
		} else if(MEOW.NAME().equals(giftName)) {
			cost = MEOW.COST();
			
		} else if(B_CLOD.NAME().equals(giftName)) {
			cost = B_CLOD.COST();
			
		} else if(STORM.NAME().equals(giftName)) {
			cost = STORM.COST();
			
		} else if(TV.NAME().equals(giftName)) {
			cost = TV.COST();
			
		} else if(_233.NAME().equals(giftName)) {
			cost = _233.COST();
			
		} else if(_666.NAME().equals(giftName)) {
			cost = _666.COST();
			
		} else if(CAPTAIN.NAME().equals(giftName)) {
			cost = CAPTAIN.COST();
			
		} else if(ADMIRAL.NAME().equals(giftName)) {
			cost = ADMIRAL.COST();
			
		} else if(GOVERNOR.NAME().equals(giftName)) {
			cost = GOVERNOR.COST();
			
		} else if(LANTERN.NAME().equals(giftName)) {
			cost = LANTERN.COST();
			
		} else if(SQUIB.NAME().equals(giftName)) {
			cost = SQUIB.COST();
		}
		return cost;
	}
	
}
