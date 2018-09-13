package exp.bilibili.plugin.envm;

import java.awt.Color;

public class ChatColor {

	public final static ChatColor WHITE = new ChatColor(
			"white", "脑残白", "16777215", 255, 255, 255);
	
	public final static ChatColor RED = new ChatColor(
			"red", "姨妈红", "16738408", 255, 104, 104);
	
	public final static ChatColor BLUE = new ChatColor(
			"blue", "海底蓝", "6737151", 102, 204, 255);
	
	public final static ChatColor PURPLE = new ChatColor(
			"purple", "基佬紫", "14893055", 227, 63, 255);
	
	public final static ChatColor CYAN = new ChatColor(
			"cyan", "散光青", "65532", 0, 255, 252);
	
	public final static ChatColor GREEN = new ChatColor(
			"green", "宝强绿", "8322816", 126, 255, 0);
	
	public final static ChatColor YELLOW = new ChatColor(
			"yellow", "菊花黄", "16772431", 255, 237, 79);
	
	// FIXME 暂时未取到颜色编码
	public final static ChatColor ORANGE = new ChatColor(
			"orange", "柠檬橙", "??????", 255, 152, 0);
	
	// FIXME 暂时未取到颜色编码
	public final static ChatColor PINK = new ChatColor(
			"pink", "柠檬橙", "??????", 255, 115, 154);
	
	// FIXME 暂时未取到颜色编码
	public final static ChatColor GOLD = new ChatColor(
			"gold", "土豪金", "??????", 251, 254, 182);
	
	private String en;
	
	private String zh;
	
	private String code;
	
	private Color color;
	
	private ChatColor(String en, String zh, String code, int R, int G, int B) {
		this.en = en;
		this.zh = zh;
		this.code = code;
		this.color = new Color(R, G, B);
	}
	
	public String EN() {
		return en;
	}
	
	public String ZH() {
		return zh;
	}
	
	public String CODE() {
		return code;
	}
	
	public Color COLOR() {
		return color;
	}
	
}
