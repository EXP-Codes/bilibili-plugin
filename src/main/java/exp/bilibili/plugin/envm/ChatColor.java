package exp.bilibili.plugin.envm;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.num.RandomUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 弹幕颜色.
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ChatColor {
	
	public final static ChatColor WHITE = new ChatColor(
			"white", "脑残白", 255, 255, 255);
	
	public final static ChatColor RED = new ChatColor(
			"red", "姨妈红", 255, 104, 104);
	
	public final static ChatColor BLUE = new ChatColor(
			"blue", "海底蓝", 102, 204, 255);
	
	public final static ChatColor PURPLE = new ChatColor(
			"purple", "基佬紫", 227, 63, 255);

	public final static ChatColor CYAN = new ChatColor(
			"cyan", "散光青", 0, 255, 252);
	
	public final static ChatColor GREEN = new ChatColor(
			"green", "宝强绿", 126, 255, 0);
	
	public final static ChatColor YELLOW = new ChatColor(
			"yellow", "菊花黄", 255, 237, 79);
	
	public final static ChatColor ORANGE = new ChatColor(
			"orange", "柠檬橙", 255, 152, 0);
	
	public final static ChatColor PINK = new ChatColor(
			"pink", "蜜桃粉", 255, 115, 154);
	
	public final static ChatColor GOLD = new ChatColor(
			"gold", "土豪金", 251, 254, 182);
	
	/** 颜色集: 用于取随机颜色 */
	private final static List<ChatColor> COLORS = Arrays.asList(new ChatColor[] {
			WHITE, RED, BLUE, PURPLE, CYAN, GREEN, YELLOW, ORANGE, PINK, GOLD
	});
	
	private String en;
	
	private String zh;
	
	private String rgb;
	
	private Color color;
	
	private ChatColor(String en, String zh, int R, int G, int B) {
		this.en = en;
		this.zh = zh;
		this.rgb = String.valueOf(toRGB(R, G, B));
		this.color = new Color(R, G, B);
	}
	
	/**
	 * RGB颜色值计算
	 * @param R
	 * @param G
	 * @param B
	 * @return
	 */
	private long toRGB(int R, int G, int B) {
		String RGB = StrUtils.concat(
				StrUtils.leftPad(BODHUtils.decToHex(R), '0', 2), 
				StrUtils.leftPad(BODHUtils.decToHex(G), '0', 2), 
				StrUtils.leftPad(BODHUtils.decToHex(B), '0', 2));
		return BODHUtils.hexToDec(RGB);
	}
	
	public String EN() {
		return en;
	}
	
	public String ZH() {
		return zh;
	}
	
	public String RGB() {
		return rgb;
	}
	
	public Color COLOR() {
		return color;
	}
	
	public static ChatColor RANDOM() {
		return RandomUtils.randomElement(COLORS);
	}
	
}
