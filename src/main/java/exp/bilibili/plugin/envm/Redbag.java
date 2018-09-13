package exp.bilibili.plugin.envm;

import exp.libs.utils.other.StrUtils;

public class Redbag {

	public final static Redbag UNKNOW = new Redbag("unknow", 0, "未知", 0);
	
	public final static Redbag REDBAG_POOL = new Redbag("award-pool", 6666, "刷新兑换池", 0);
	
	public final static Redbag SILVER = new Redbag("silver-100", 10, "银瓜子×100", 0);
	
	public final static Redbag B_CLOD = new Redbag("gift-3", 450, "B坷垃", 1);
	
	public final static Redbag MEOW = new Redbag("gift-4", 233, "喵娘", 20);
	
	public final static Redbag LANTERN = new Redbag("gift-109", 15, "红灯笼", 500);
	
	public final static Redbag SPRING = new Redbag("gift-113", 23333, "新春抽奖", 0);
	
	public final static Redbag STUFF1 = new Redbag("stuff-1", 30, "经验原石", 80);
	
	public final static Redbag STUFF2 = new Redbag("stuff-2", 233, "经验曜石", 10);
	
	public final static Redbag STUFF3 = new Redbag("stuff-3", 1888, "贤者之石", 5);
	
	public final static Redbag FIRECRACKER = new Redbag("title-89", 888, "爆竹头衔", 10);
	
	public final static Redbag BEAST = new Redbag("title-92", 999, "年兽头衔", 1);
	
	public final static Redbag DOG = new Redbag("title-140", 666, "秋田君头衔", 20);
	
	public final static Redbag GUARD = new Redbag("guard-3", 6699, "舰长体验券（1个月）", 5);
	
	public final static Redbag GOLD_DANMU = new Redbag("danmu-gold", 2233, "金色弹幕特权（1天）", 42);
	
	public final static Redbag GOLD_NAME = new Redbag("uname-gold", 8888, "金色昵称特权（1天）", 42);
	
	public final static Redbag CALENDAR = new Redbag("award-calendar", 8888, "哔哩哔哩直播2018新年台历", 2);
	
	public final static Redbag MASTER = new Redbag("award-master", 66666, "直播首页推荐卡1小时", 0);
	
	private String id;
	
	private int price;
	
	private String desc;
	
	private int max;
	
	private Redbag(String id, int price, String desc, int max) {
		this.id = id;
		this.price = price;
		this.desc = desc;
		this.max = max;
	}
	
	public String ID() {
		return id;
	}
	
	public int PRICE() {
		return price;
	}
	
	public String DESC() {
		return desc;
	}
	
	public int MAX() {
		return max;
	}
	
	public static Redbag toRedbag(String id) {
		Redbag redbag = UNKNOW;
		if(REDBAG_POOL.ID().equals(id)) {
			redbag = REDBAG_POOL;
			
		} else if(SILVER.ID().equals(id)) {
			redbag = SILVER;
			
		} else if(B_CLOD.ID().equals(id)) {
			redbag = B_CLOD;
			
		} else if(MEOW.ID().equals(id)) {
			redbag = MEOW;
			
		} else if(LANTERN.ID().equals(id)) {
			redbag = LANTERN;
			
		} else if(SPRING.ID().equals(id)) {
			redbag = SPRING;
			
		} else if(STUFF1.ID().equals(id)) {
			redbag = STUFF1;
			
		} else if(STUFF2.ID().equals(id)) {
			redbag = STUFF2;
			
		} else if(STUFF3.ID().equals(id)) {
			redbag = STUFF3;
			
		} else if(FIRECRACKER.ID().equals(id)) {
			redbag = FIRECRACKER;
			
		} else if(BEAST.ID().equals(id)) {
			redbag = BEAST;
			
		} else if(DOG.ID().equals(id)) {
			redbag = DOG;
			
		} else if(GUARD.ID().equals(id)) {
			redbag = GUARD;
			
		} else if(GOLD_DANMU.ID().equals(id)) {
			redbag = GOLD_DANMU;
			
		} else if(GOLD_NAME.ID().equals(id)) {
			redbag = GOLD_NAME;
			
		} else if(CALENDAR.ID().equals(id)) {
			redbag = CALENDAR;
			
		} else if(MASTER.ID().equals(id)) {
			redbag = MASTER;
		}
		return redbag;
	}
	
	@Override
	public String toString() {
		return StrUtils.concat(DESC(), " [兑换花费:", PRICE(), 
				"] [兑换上限:", (MAX() <= 0 ? "∞" : MAX()), "]");
	}
	
}
