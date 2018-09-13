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
	
	public final static Gift CAPTAIN = new Gift("", "舰长", 198000);
	
	public final static Gift ADMIRAL = new Gift("", "提督", 1998000);
	
	public final static Gift GOVERNOR = new Gift("", "总督", 19998000);
	
	public final static Gift HOT_STRIP = new Gift("1", "辣条", 100);
	
	public final static Gift B_CLOD = new Gift("3", "B坷垃", 9900);
	
	public final static Gift MILLION = new Gift("6", "亿圆", 1000);
	
	public final static Gift _666 = new Gift("7", "666", 666);
	
	public final static Gift _233 = new Gift("8", "233", 233);
	
	public final static Gift LUNCH = new Gift("9", "爱心便当", 4500);
	
	public final static Gift PANGCI = new Gift("10", "蓝白胖次", 19900);
	
	public final static Gift TV = new Gift("25", "小电视", 1245000);
	
	public final static Gift STORM = new Gift("39", "节奏风暴", 100000);
	
	public final static Gift LANTERN = new Gift("109", "红灯笼", 2000);
	
	public final static Gift SQUIB = new Gift("110", "小爆竹", 2000);
	
	public final static Gift PEACH = new Gift("115", "桃花", 2000);
	
	public final static Gift GAMEBOY = new Gift("120", "游戏机", 100);
	
	public final static Gift STAR = new Gift("121", "闪耀之星", 200);
	
	public final static Gift FLAG = new Gift("20002", "flag", 100);
	
	public final static Gift SKYSCRAPER = new Gift("20003", "摩天大楼", 450000);
	
	public final static Gift RUBBERNECK = new Gift("20004", "吃瓜", 100);
	
	public final static Gift COIN = new Gift("20005", "金币", 1000);
	
	public final static Gift BQM = new Gift("20007", "？？？", 100);
	
	public final static Gift COLA = new Gift("20008", "冰阔乐", 1000);
	
	public final static Gift SPRAY = new Gift("20009", "变欧喷雾", 12000);
	
	public final static Gift COLD = new Gift("20010", "凉了", 100);
	
	public final static Gift CHEERS = new Gift("20011", "干杯", 100);
	
	public final static Gift KEYBOARD = new Gift("20012", "氪金键盘", 38000);
	
	public final static Gift FLOWER = new Gift("20013", "小花花", 2000);
	
	public final static Gift HEART = new Gift("20014", "比心", 100);
	
	public final static Gift LETTER = new Gift("30003", "情书", 2000);
	
	public final static Gift MEOW = new Gift("30004", "喵娘", 5200);

	public final static Gift CHICKEN = new Gift("30006", "鸡小萌", 2000);
	
	public final static Gift BOAT = new Gift("30010", "海带缠潜艇", 1000);
	
	public final static Gift ICE = new Gift("30011", "棒冰", 200);
	
	public final static Gift TOWEL = new Gift("30012", "应援毛巾", 2000);
	
	public final static Gift C_AURA = new Gift("30013", "C位光环", 450000);
	
	public final static Gift TEA = new Gift("30014", "盛夏么么茶", 0);
	
	public final static Gift VIP_C_AURA_1 = new Gift("30015", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_2 = new Gift("30016", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_3 = new Gift("30017", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_4 = new Gift("30018", "专属C位光环", 450000);
		
	public final static Gift VIP_C_AURA_5 = new Gift("30019", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_6 = new Gift("30020", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_7 = new Gift("30022", "专属C位光环", 450000);
	
	public final static Gift VIP_C_AURA_8 = new Gift("30025", "专属C位光环", 450000);
	
	public final static Gift BALD = new Gift("30048", "小光头", 2333);
	
	public final static Gift DONT_CRY = new Gift("30049", "你别哭啊", 2000);
	
	public final static Gift DOORKNOB = new Gift("30050", "门把手", 2000);
	
	public final static Gift PINCH_HIT = new Gift("30051", "给代打的礼物", 3000);
	
	public final static Gift ICE_CREAM = new Gift("30052", "冰淇淋", 1011);
	
	public final static Gift FANS = new Gift("30054", "粉丝卡", 5000);
	
	public final static Gift HELMET_1 = new Gift("30055", "三级头", 2000);
	
	public final static Gift PAN = new Gift("30056", "平底锅", 1000);
	
	public final static Gift HELMET_2 = new Gift("30057", "三级头", 2000);
	
	public final static Gift _460 = new Gift("30058", "460", 1000);
	
	public final static Gift HOE = new Gift("30059", "锄头", 2000);
	
	public final static Gift CANNON = new Gift("30060", "炮车", 1000);
	
	public final static Gift FISH = new Gift("30062", "咸鱼", 1000);
	
	public final static Gift SEND_TEA = new Gift("30063", "给大佬递茶", 2000);
	
	public final static Gift FIREWORKS = new Gift("30064", "礼花", 28000);
	
	public final static Gift CHAIR = new Gift("30065", "狂欢之椅", 2000);
	
	public final static Gift FRAGRANT = new Gift("30066", "真香", 2000);
	
	/** 礼物ID */
	private String id;
	
	/** 礼物名称 */
	private String name;
	
	/** 价值/活跃值 */
	private int cost;
	
	/** 亲密度（礼物价值/100） */
	private int intimacy;
	
	private Gift(String id, String name, int cost) {
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.intimacy = (int) Math.ceil(cost / 100.0);	// 向上取整, 便于计算
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
	
	public int INTIMACY() {
		return intimacy;
	}
	
	public static int getIntimacy(String giftId) {
		int intimacy = 0;
		if(HOT_STRIP.ID().equals(giftId)) {
			intimacy = HOT_STRIP.INTIMACY();

		} else if(B_CLOD.ID().equals(giftId)) {
			intimacy = B_CLOD.INTIMACY();

		} else if(MILLION.ID().equals(giftId)) {
			intimacy = MILLION.INTIMACY();

		} else if(_666.ID().equals(giftId)) {
			intimacy = _666.INTIMACY();

		} else if(_233.ID().equals(giftId)) {
			intimacy = _233.INTIMACY();

		} else if(LUNCH.ID().equals(giftId)) {
			intimacy = LUNCH.INTIMACY();

		} else if(PANGCI.ID().equals(giftId)) {
			intimacy = PANGCI.INTIMACY();

		} else if(TV.ID().equals(giftId)) {
			intimacy = TV.INTIMACY();

		} else if(STORM.ID().equals(giftId)) {
			intimacy = STORM.INTIMACY();

		} else if(LANTERN.ID().equals(giftId)) {
			intimacy = LANTERN.INTIMACY();

		} else if(SQUIB.ID().equals(giftId)) {
			intimacy = SQUIB.INTIMACY();

		} else if(PEACH.ID().equals(giftId)) {
			intimacy = PEACH.INTIMACY();

		} else if(GAMEBOY.ID().equals(giftId)) {
			intimacy = GAMEBOY.INTIMACY();

		} else if(STAR.ID().equals(giftId)) {
			intimacy = STAR.INTIMACY();

		} else if(FLAG.ID().equals(giftId)) {
			intimacy = FLAG.INTIMACY();

		} else if(SKYSCRAPER.ID().equals(giftId)) {
			intimacy = SKYSCRAPER.INTIMACY();

		} else if(RUBBERNECK.ID().equals(giftId)) {
			intimacy = RUBBERNECK.INTIMACY();

		} else if(COIN.ID().equals(giftId)) {
			intimacy = COIN.INTIMACY();

		} else if(BQM.ID().equals(giftId)) {
			intimacy = BQM.INTIMACY();

		} else if(COLA.ID().equals(giftId)) {
			intimacy = COLA.INTIMACY();

		} else if(SPRAY.ID().equals(giftId)) {
			intimacy = SPRAY.INTIMACY();

		} else if(COLD.ID().equals(giftId)) {
			intimacy = COLD.INTIMACY();

		} else if(CHEERS.ID().equals(giftId)) {
			intimacy = CHEERS.INTIMACY();

		} else if(KEYBOARD.ID().equals(giftId)) {
			intimacy = KEYBOARD.INTIMACY();

		} else if(FLOWER.ID().equals(giftId)) {
			intimacy = FLOWER.INTIMACY();

		} else if(HEART.ID().equals(giftId)) {
			intimacy = HEART.INTIMACY();

		} else if(LETTER.ID().equals(giftId)) {
			intimacy = LETTER.INTIMACY();

		} else if(MEOW.ID().equals(giftId)) {
			intimacy = MEOW.INTIMACY();

		} else if(CHICKEN.ID().equals(giftId)) {
			intimacy = CHICKEN.INTIMACY();

		} else if(BOAT.ID().equals(giftId)) {
			intimacy = BOAT.INTIMACY();

		} else if(ICE.ID().equals(giftId)) {
			intimacy = ICE.INTIMACY();

		} else if(TOWEL.ID().equals(giftId)) {
			intimacy = TOWEL.INTIMACY();

		} else if(C_AURA.ID().equals(giftId)) {
			intimacy = C_AURA.INTIMACY();

		} else if(TEA.ID().equals(giftId)) {
			intimacy = TEA.INTIMACY();

		} else if(VIP_C_AURA_1.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_1.INTIMACY();

		} else if(VIP_C_AURA_2.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_2.INTIMACY();

		} else if(VIP_C_AURA_3.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_3.INTIMACY();

		} else if(VIP_C_AURA_4.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_4.INTIMACY();

		} else if(VIP_C_AURA_5.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_5.INTIMACY();

		} else if(VIP_C_AURA_6.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_6.INTIMACY();

		} else if(VIP_C_AURA_7.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_7.INTIMACY();

		} else if(VIP_C_AURA_8.ID().equals(giftId)) {
			intimacy = VIP_C_AURA_8.INTIMACY();

		} else if(BALD.ID().equals(giftId)) {
			intimacy = BALD.INTIMACY();

		} else if(DONT_CRY.ID().equals(giftId)) {
			intimacy = DONT_CRY.INTIMACY();

		} else if(DOORKNOB.ID().equals(giftId)) {
			intimacy = DOORKNOB.INTIMACY();

		} else if(PINCH_HIT.ID().equals(giftId)) {
			intimacy = PINCH_HIT.INTIMACY();

		} else if(ICE_CREAM.ID().equals(giftId)) {
			intimacy = ICE_CREAM.INTIMACY();

		} else if(FANS.ID().equals(giftId)) {
			intimacy = FANS.INTIMACY();

		} else if(HELMET_1.ID().equals(giftId)) {
			intimacy = HELMET_1.INTIMACY();

		} else if(PAN.ID().equals(giftId)) {
			intimacy = PAN.INTIMACY();

		} else if(HELMET_2.ID().equals(giftId)) {
			intimacy = HELMET_2.INTIMACY();

		} else if(_460.ID().equals(giftId)) {
			intimacy = _460.INTIMACY();

		} else if(HOE.ID().equals(giftId)) {
			intimacy = HOE.INTIMACY();

		} else if(CANNON.ID().equals(giftId)) {
			intimacy = CANNON.INTIMACY();

		} else if(FISH.ID().equals(giftId)) {
			intimacy = FISH.INTIMACY();

		} else if(SEND_TEA.ID().equals(giftId)) {
			intimacy = SEND_TEA.INTIMACY();

		} else if(FIREWORKS.ID().equals(giftId)) {
			intimacy = FIREWORKS.INTIMACY();

		} else if(CHAIR.ID().equals(giftId)) {
			intimacy = CHAIR.INTIMACY();

		} else if(FRAGRANT.ID().equals(giftId)) {
			intimacy = FRAGRANT.INTIMACY();

		}
		return intimacy;
	}
	
	public static int getCost(String giftName) {
		int cost = 0;
		if(CHAT.NAME().equals(giftName)) {
			cost = CHAT.COST();

		} else if(CAPTAIN.NAME().equals(giftName)) {
			cost = CAPTAIN.COST();

		} else if(ADMIRAL.NAME().equals(giftName)) {
			cost = ADMIRAL.COST();

		} else if(GOVERNOR.NAME().equals(giftName)) {
			cost = GOVERNOR.COST();

		} else if(HOT_STRIP.NAME().equals(giftName)) {
			cost = HOT_STRIP.COST();

		} else if(B_CLOD.NAME().equals(giftName)) {
			cost = B_CLOD.COST();

		} else if(MILLION.NAME().equals(giftName)) {
			cost = MILLION.COST();

		} else if(_666.NAME().equals(giftName)) {
			cost = _666.COST();

		} else if(_233.NAME().equals(giftName)) {
			cost = _233.COST();

		} else if(LUNCH.NAME().equals(giftName)) {
			cost = LUNCH.COST();

		} else if(PANGCI.NAME().equals(giftName)) {
			cost = PANGCI.COST();

		} else if(TV.NAME().equals(giftName)) {
			cost = TV.COST();

		} else if(STORM.NAME().equals(giftName)) {
			cost = STORM.COST();

		} else if(LANTERN.NAME().equals(giftName)) {
			cost = LANTERN.COST();

		} else if(SQUIB.NAME().equals(giftName)) {
			cost = SQUIB.COST();

		} else if(PEACH.NAME().equals(giftName)) {
			cost = PEACH.COST();

		} else if(GAMEBOY.NAME().equals(giftName)) {
			cost = GAMEBOY.COST();

		} else if(STAR.NAME().equals(giftName)) {
			cost = STAR.COST();

		} else if(FLAG.NAME().equals(giftName)) {
			cost = FLAG.COST();

		} else if(SKYSCRAPER.NAME().equals(giftName)) {
			cost = SKYSCRAPER.COST();

		} else if(RUBBERNECK.NAME().equals(giftName)) {
			cost = RUBBERNECK.COST();

		} else if(COIN.NAME().equals(giftName)) {
			cost = COIN.COST();

		} else if(BQM.NAME().equals(giftName)) {
			cost = BQM.COST();

		} else if(COLA.NAME().equals(giftName)) {
			cost = COLA.COST();

		} else if(SPRAY.NAME().equals(giftName)) {
			cost = SPRAY.COST();

		} else if(COLD.NAME().equals(giftName)) {
			cost = COLD.COST();

		} else if(CHEERS.NAME().equals(giftName)) {
			cost = CHEERS.COST();

		} else if(KEYBOARD.NAME().equals(giftName)) {
			cost = KEYBOARD.COST();

		} else if(FLOWER.NAME().equals(giftName)) {
			cost = FLOWER.COST();

		} else if(HEART.NAME().equals(giftName)) {
			cost = HEART.COST();

		} else if(LETTER.NAME().equals(giftName)) {
			cost = LETTER.COST();

		} else if(MEOW.NAME().equals(giftName)) {
			cost = MEOW.COST();

		} else if(CHICKEN.NAME().equals(giftName)) {
			cost = CHICKEN.COST();

		} else if(BOAT.NAME().equals(giftName)) {
			cost = BOAT.COST();

		} else if(ICE.NAME().equals(giftName)) {
			cost = ICE.COST();

		} else if(TOWEL.NAME().equals(giftName)) {
			cost = TOWEL.COST();

		} else if(C_AURA.NAME().equals(giftName)) {
			cost = C_AURA.COST();

		} else if(TEA.NAME().equals(giftName)) {
			cost = TEA.COST();

		} else if(VIP_C_AURA_1.NAME().equals(giftName)) {
			cost = VIP_C_AURA_1.COST();

		} else if(VIP_C_AURA_2.NAME().equals(giftName)) {
			cost = VIP_C_AURA_2.COST();

		} else if(VIP_C_AURA_3.NAME().equals(giftName)) {
			cost = VIP_C_AURA_3.COST();

		} else if(VIP_C_AURA_4.NAME().equals(giftName)) {
			cost = VIP_C_AURA_4.COST();

		} else if(VIP_C_AURA_5.NAME().equals(giftName)) {
			cost = VIP_C_AURA_5.COST();

		} else if(VIP_C_AURA_6.NAME().equals(giftName)) {
			cost = VIP_C_AURA_6.COST();

		} else if(VIP_C_AURA_7.NAME().equals(giftName)) {
			cost = VIP_C_AURA_7.COST();

		} else if(VIP_C_AURA_8.NAME().equals(giftName)) {
			cost = VIP_C_AURA_8.COST();

		} else if(BALD.NAME().equals(giftName)) {
			cost = BALD.COST();

		} else if(DONT_CRY.NAME().equals(giftName)) {
			cost = DONT_CRY.COST();

		} else if(DOORKNOB.NAME().equals(giftName)) {
			cost = DOORKNOB.COST();

		} else if(PINCH_HIT.NAME().equals(giftName)) {
			cost = PINCH_HIT.COST();

		} else if(ICE_CREAM.NAME().equals(giftName)) {
			cost = ICE_CREAM.COST();

		} else if(FANS.NAME().equals(giftName)) {
			cost = FANS.COST();

		} else if(HELMET_1.NAME().equals(giftName)) {
			cost = HELMET_1.COST();

		} else if(PAN.NAME().equals(giftName)) {
			cost = PAN.COST();

		} else if(HELMET_2.NAME().equals(giftName)) {
			cost = HELMET_2.COST();

		} else if(_460.NAME().equals(giftName)) {
			cost = _460.COST();

		} else if(HOE.NAME().equals(giftName)) {
			cost = HOE.COST();

		} else if(CANNON.NAME().equals(giftName)) {
			cost = CANNON.COST();

		} else if(FISH.NAME().equals(giftName)) {
			cost = FISH.COST();

		} else if(SEND_TEA.NAME().equals(giftName)) {
			cost = SEND_TEA.COST();

		} else if(FIREWORKS.NAME().equals(giftName)) {
			cost = FIREWORKS.COST();

		} else if(CHAIR.NAME().equals(giftName)) {
			cost = CHAIR.COST();

		} else if(FRAGRANT.NAME().equals(giftName)) {
			cost = FRAGRANT.COST();

		}
		return cost;
	}
	
}
