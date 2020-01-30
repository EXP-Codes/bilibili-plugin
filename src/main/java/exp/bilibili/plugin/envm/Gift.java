package exp.bilibili.plugin.envm;

import java.util.LinkedList;
import java.util.List;

import exp.libs.utils.other.ListUtils;

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
@SuppressWarnings("unchecked")
public class Gift {

	public final static Gift CHAT = new Gift("", "弹幕", 1);
	
	public final static Gift CAPTAIN = new Gift("", "舰长", 198000);
	
	public final static Gift ADMIRAL = new Gift("", "提督", 1998000);
	
	public final static Gift GOVERNOR = new Gift("", "总督", 19998000);
	
	public final static Gift HOT_STRIP = new Gift("1", "辣条", 100);
    
	public final static Gift B_CLOD = new Gift("3", "B坷垃", 9900);

	public final static Gift MEOW2 = new Gift("4", "喵娘", 5200);

	public final static Gift MILLION = new Gift("6", "亿圆", 1000);

	public final static Gift _666 = new Gift("7", "666", 666);

	public final static Gift _233 = new Gift("8", "233", 233);

	public final static Gift LUNCH = new Gift("9", "爱心便当", 4500);

	public final static Gift PANGCI = new Gift("10", "蓝白胖次", 19900);

	public final static Gift TV = new Gift("25", "小电视飞船", 1245000);

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

	public final static Gift BUBBLE_GUM = new Gift("30027", "泡泡糖", 40);

	public final static Gift KANBAN_HEADDRESS = new Gift("30032", "看板娘头饰", 0);

	public final static Gift CHARGED_CANNON = new Gift("30033", "充能炮", 0);

	public final static Gift ARBITRARY_GATE = new Gift("30035", "任意门", 1);

	public final static Gift MAKE_LIST = new Gift("30046", "打榜", 2000);

	public final static Gift FRIENDSHIP_BOAT = new Gift("30047", "友谊的小船", 5200);

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

	public final static Gift STEERING_WHEEL = new Gift("30070", "方向盘", 2000);

	public final static Gift LIGHTHOUSE = new Gift("30071", "灯塔", 2000);

	public final static Gift CRAZY_CALL = new Gift("30072", "疯狂打call", 52000);

	public final static Gift BONUS_CARD = new Gift("30081", "积分加成卡", 0);

	public final static Gift KFC_GIFT = new Gift("30082", "KFC新年贺礼", 1);

	public final static Gift LITTLE_STARS = new Gift("30085", "小星星", 100);

	public final static Gift SAN = new Gift("30086", "小散", 2000);

	public final static Gift SKY_WINGS = new Gift("30087", "天空之翼", 100000);

	public final static Gift NICE = new Gift("30088", "奈斯", 2000);

	public final static Gift CRITICAL_GLOVES = new Gift("30089", "暴击手套", 2000);

	public final static Gift MUAH = new Gift("30090", "么么哒", 2000);

	public final static Gift MICROPHONE = new Gift("30091", "变身话筒", 2000);

	public final static Gift LICENSING = new Gift("30117", "发牌姬", 100);

	public final static Gift BELL   = new Gift("30135", "铃铛", 100);

	public final static Gift MORI = new Gift("30136", "御守", 1000);

	public final static Gift SPA = new Gift("30139", "温泉", 2000);

	public final static Gift BANG = new Gift("30140", "Bang！", 1000);

	public final static Gift LUFF_BUILDING = new Gift("30146", "摩夫大楼", 250);

	public final static Gift EAT_CLAWS = new Gift("30147", "吃爪", 10);

	public final static Gift CHERRY1 = new Gift("30148", "樱花欲放", 0);

	public final static Gift CHERRY2 = new Gift("30149", "樱花初放", 0);

	public final static Gift CHERRY3 = new Gift("30150", "樱花盛放", 0);

	public final static Gift CHERRY4 = new Gift("30151", "樱花怒放", 0);

	public final static Gift CHERRY5 = new Gift("30152", "樱花齐放", 0);

	public final static Gift WIN = new Gift("30153", "棋开得胜", 1000);

	public final static Gift XUFU = new Gift("30197", "xufu", 0);

	public final static Gift WIND_SOUND = new Gift("30205", "风吟", 1000);

	public final static Gift PIANO_SOUND = new Gift("30206", "琴语", 100000);

	public final static Gift ILLUSION_VOICE = new Gift("30207", "幻乐之声", 520000);

	public final static Gift RINGTONE = new Gift("30208", "铃音", 0);

	public final static Gift PLATINUM_HANDLE = new Gift("30229", "白金手柄", 2000);

	public final static Gift DORAYAKI = new Gift("30235", "铜锣烧", 1000);

	public final static Gift TIME_HOURGLASS = new Gift("30240", "时光沙漏", 0);

	public final static Gift HAPPY_10_YEARS = new Gift("30241", "十周年快乐", 0);

	public final static Gift DAZZLING = new Gift("30244", "晃悠悠", 100);

	public final static Gift TRIP_LOVE_STICK = new Gift("30245", "绊爱应援棒", 1000);

	public final static Gift TRIP_LOVE_STICK2 = new Gift("30247", "绊爱应援棒", 1000);

	public final static Gift TRIP_LOVE_CAKE = new Gift("30248", "绊爱生日蛋糕", 100000);

	public final static Gift GOGOGO = new Gift("30250", "冲鸭", 0);

	public final static Gift HAPPY_BIRTHDAY = new Gift("30254", "生日快乐", 1000);

	public final static Gift BEST_WISHES = new Gift("30256", "Best wishes!", 100000);

	public final static Gift FIST = new Gift("30264", "小拳拳", 0);

	public final static Gift FLAME_PUNCH = new Gift("30265", "火焰拳", 1000);

	public final static Gift GOLD_BELT = new Gift("30266", "金腰带", 520000);

	public final static Gift PARTITION_PORTAL = new Gift("30268", "分区任意门", 1);

	public final static Gift THOUSAND_PAPER_CRANES = new Gift("30274", "千纸鹤", 0);

	public final static Gift SHAVED_ICE = new Gift("30275", "激爽刨冰", 100);

	public final static Gift LOVE_HANDBOOK = new Gift("30276", "恋恋手账", 1000);

	public final static Gift SUPPORT_CAT = new Gift("30277", "应援喵", 520000);

	public final static Gift ENCOUNTERS = new Gift("30278", "邂逅", 0);

	public final static Gift LISTEN = new Gift("30279", "聆听", 0);

	public final static Gift WHISPER = new Gift("30280", "絮语", 0);

	public final static Gift HEARTBEAT = new Gift("30281", "心动", 0);

	public final static Gift OATH = new Gift("30282", "誓言", 0);

	public final static Gift TIME_HEADDRESS = new Gift("30283", "元气时光头饰", 0);

	public final static Gift OUTING_HEADDRESS = new Gift("30284", "夏日出游头饰", 0);

	public final static Gift TEST_GOLD = new Gift("30293", "测试道具-金", 10);

	public final static Gift GANBEI = new Gift("30294", "ganbei", 1);

	public final static Gift JINBI = new Gift("30295", "jinbi", 100);

	public final static Gift SHALLOT2 = new Gift("30296", "葱", 0);

	public final static Gift PORTAL = new Gift("30299", "传送门", 0);

	public final static Gift SUPPORT_STICK = new Gift("30316", "应援棒", 0);

	public final static Gift TEST = new Gift("30323", "test", 10);

	public final static Gift OSMANTHUS = new Gift("30329", "桂花", 10);

	public final static Gift HIGH_ENERGY = new Gift("30331", "大糕能", 0);

	public final static Gift LAST_ENERGY = new Gift("30332", "最终糕能", 0);

	public final static Gift SHALLOT = new Gift("30333", "葱", 0);

	public final static Gift RICE_BALL = new Gift("30339", "饭团", 2000);

	public final static Gift TARO_ICE_TEA = new Gift("30355", "香芋冰淇淋茶", 28000);

	public final static Gift LEMON_ICE_TEA = new Gift("30356", "柠檬冰淇淋茶", 28000);

	public final static Gift GUGUKA = new Gift("30362", "咕咕卡", 0);

	public final static Gift WINE_CALL = new Gift("30365", "酒姬民打call", 2000);

	public final static Gift CAT_CLAW = new Gift("30403", "猫爪软糖", 0);

	public final static Gift MAP_22 = new Gift("30404", "22地图", 0);

	public final static Gift MAP_33 = new Gift("30405", "33地图抽奖", 0);

	public final static Gift TV2 = new Gift("30406", "小电视抽奖", 0);

	public final static Gift KANBAN_HEADDRESS_22 = new Gift("30407", "22看板娘头饰", 0);

	public final static Gift KANBAN_HEADDRESS_33 = new Gift("30408", "33看板娘头饰", 0);

	public final static Gift HEADDRESS_22 = new Gift("30416", "22娘头饰", 0);

	public final static Gift CLOTHES_22 = new Gift("30417", "22娘上装", 0);

	public final static Gift PANTS_22 = new Gift("30418", "22下装", 0);

	public final static Gift HEADDRESS_33 = new Gift("30419", "33头饰", 0);

	public final static Gift CLOTHES_33 = new Gift("30420", "33上装", 0);

	public final static Gift PANTS_33 = new Gift("30421", "33下装", 0);

	public final static Gift BLS_ITEM = new Gift("30424", "BLS抽奖道具", 0);

	public final static Gift BLS_STONE = new Gift("30426", "BLS能量石", 0);

	public final static Gift MUSHROOM_ARK_1219 = new Gift("30436", "1219蘑菇方舟", 1999000);

	public final static Gift GOGOGOGOGO = new Gift("30444", "发卡卡冲呀！", 100);

	public final static Gift LUCKY_STONE = new Gift("30446", "欧气石头", 1000);

	public final static Gift TIMO_LOVE = new Gift("30447", "提莫比心", 10000);

	public final static Gift MUSHROOM_STOP = new Gift("30448", "蘑菇别跑", 121900);

	public final static Gift SKYSCRAPER2 = new Gift("30452", "摩天大楼", 450000);

	public final static Gift HANGOUT = new Gift("30458", "福利章小聚", 9900);

	public final static Gift RED_ENVELOPE = new Gift("30461", "新年红包", 0);

	public final static Gift HAPPY = new Gift("30465", "欢心", 0);

	public final static Gift HEALTH = new Gift("30466", "安康", 0);

	public final static Gift WISH = new Gift("30467", "如意", 0);

	public final static Gift COMPLETE = new Gift("30468", "美满", 0);

	public final static Gift BLESSING = new Gift("30469", "福至", 0);

	public final static Gift TEST2 = new Gift("30475", "test", 9900);

	public final static Gift DMO = new Gift("30477", "DMO加油！", 1000);

	public final static Gift EDG = new Gift("30478", "EDG加油！", 1000);

	public final static Gift TES = new Gift("30488", "TES加油！", 1000);

	public final static Gift VG = new Gift("30490", "VG加油！", 1000);

	public final static Gift TV3 = new Gift("30495", "小电视飞船", 1245000);

	public final static Gift KFC = new Gift("30501", "KFC", 10000);

	public final static Gift HARBIN_BEER = new Gift("30502", "一起哈啤", 10000);
	
	public final static List<Gift> GIFTS = new LinkedList<Gift>();
	
	static {
		GIFTS.addAll(ListUtils.asList(new Gift[] {
				HOT_STRIP, 
				B_CLOD, 
				MEOW2, 
				MILLION, 
				_666, 
				_233, 
				LUNCH, 
				PANGCI, 
				TV, 
				STORM, 
				LANTERN, 
				SQUIB, 
				PEACH, 
				GAMEBOY, 
				STAR, 
				FLAG, 
				SKYSCRAPER, 
				RUBBERNECK, 
				COIN, 
				BQM, 
				COLA, 
				SPRAY, 
				COLD, 
				CHEERS, 
				KEYBOARD, 
				FLOWER, 
				HEART, 
				LETTER, 
				MEOW, 
				CHICKEN, 
				BOAT, 
				ICE, 
				TOWEL, 
				C_AURA, 
				TEA, 
				VIP_C_AURA_1, 
				VIP_C_AURA_2, 
				VIP_C_AURA_3, 
				VIP_C_AURA_4, 
				VIP_C_AURA_5, 
				VIP_C_AURA_6, 
				VIP_C_AURA_7, 
				VIP_C_AURA_8, 
				BUBBLE_GUM, 
				KANBAN_HEADDRESS, 
				CHARGED_CANNON, 
				ARBITRARY_GATE, 
				MAKE_LIST, 
				FRIENDSHIP_BOAT, 
				BALD, 
				DONT_CRY, 
				DOORKNOB, 
				PINCH_HIT, 
				ICE_CREAM, 
				FANS, 
				HELMET_1, 
				PAN, 
				HELMET_2, 
				_460, 
				HOE, 
				CANNON, 
				FISH, 
				SEND_TEA, 
				FIREWORKS, 
				CHAIR, 
				FRAGRANT, 
				STEERING_WHEEL, 
				LIGHTHOUSE, 
				CRAZY_CALL, 
				BONUS_CARD, 
				KFC_GIFT, 
				LITTLE_STARS, 
				SAN, 
				SKY_WINGS, 
				NICE, 
				CRITICAL_GLOVES, 
				MUAH, 
				MICROPHONE, 
				LICENSING, 
				BELL  , 
				MORI, 
				SPA, 
				BANG, 
				LUFF_BUILDING, 
				EAT_CLAWS, 
				CHERRY1, 
				CHERRY2, 
				CHERRY3, 
				CHERRY4, 
				CHERRY5, 
				WIN, 
				XUFU, 
				WIND_SOUND, 
				PIANO_SOUND, 
				ILLUSION_VOICE, 
				RINGTONE, 
				PLATINUM_HANDLE, 
				DORAYAKI, 
				TIME_HOURGLASS, 
				HAPPY_10_YEARS, 
				DAZZLING, 
				TRIP_LOVE_STICK, 
				TRIP_LOVE_STICK2, 
				TRIP_LOVE_CAKE, 
				GOGOGO, 
				HAPPY_BIRTHDAY, 
				BEST_WISHES, 
				FIST, 
				FLAME_PUNCH, 
				GOLD_BELT, 
				PARTITION_PORTAL, 
				THOUSAND_PAPER_CRANES, 
				SHAVED_ICE, 
				LOVE_HANDBOOK, 
				SUPPORT_CAT, 
				ENCOUNTERS, 
				LISTEN, 
				WHISPER, 
				HEARTBEAT, 
				OATH, 
				TIME_HEADDRESS, 
				OUTING_HEADDRESS, 
				TEST_GOLD, 
				GANBEI, 
				JINBI, 
				SHALLOT2, 
				PORTAL, 
				SUPPORT_STICK, 
				TEST, 
				OSMANTHUS, 
				HIGH_ENERGY, 
				LAST_ENERGY, 
				SHALLOT, 
				RICE_BALL, 
				TARO_ICE_TEA, 
				LEMON_ICE_TEA, 
				GUGUKA, 
				WINE_CALL, 
				CAT_CLAW, 
				MAP_22, 
				MAP_33, 
				TV2, 
				KANBAN_HEADDRESS_22, 
				KANBAN_HEADDRESS_33, 
				HEADDRESS_22, 
				CLOTHES_22, 
				PANTS_22, 
				HEADDRESS_33, 
				CLOTHES_33, 
				PANTS_33, 
				BLS_ITEM, 
				BLS_STONE, 
				MUSHROOM_ARK_1219, 
				GOGOGOGOGO, 
				LUCKY_STONE, 
				TIMO_LOVE, 
				MUSHROOM_STOP, 
				SKYSCRAPER2, 
				HANGOUT, 
				RED_ENVELOPE, 
				HAPPY, 
				HEALTH, 
				WISH, 
				COMPLETE, 
				BLESSING, 
				TEST2, 
				DMO, 
				EDG, 
				TES, 
				VG, 
				TV3, 
				KFC, 
				HARBIN_BEER, 
		}));
	}
	
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
		for(Gift gift : GIFTS) {
			if(gift.ID().equals(giftId)) {
				intimacy = gift.INTIMACY();
				break;
			}
		}
		return intimacy;
	}
	
	public static int getCost(String giftName) {
		int cost = -1;
		if(CHAT.NAME().equals(giftName)) {
			cost = CHAT.COST();

		} else if(CAPTAIN.NAME().equals(giftName)) {
			cost = CAPTAIN.COST();

		} else if(ADMIRAL.NAME().equals(giftName)) {
			cost = ADMIRAL.COST();

		} else if(GOVERNOR.NAME().equals(giftName)) {
			cost = GOVERNOR.COST();
		}
		
		if(cost < 0) {
			for(Gift gift : GIFTS) {
				if(gift.NAME().equals(giftName)) {
					cost = gift.COST();
					break;
				}
			}
		}
		return cost < 0 ? 0 : cost;
	}
	
}
