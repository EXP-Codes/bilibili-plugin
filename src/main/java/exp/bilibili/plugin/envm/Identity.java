package exp.bilibili.plugin.envm;

import exp.libs.utils.encode.CryptoUtils;

/**
 * <PRE>
 * 登陆用户的身份授权等级
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Identity {

	/** 普通用户: -user */
	public final static Identity USER = new Identity(1, CryptoUtils.deDES("637B5DE0EB673958"));
	
	/** 主播用户: -uplive */
	public final static Identity UPLIVE = new Identity(2, CryptoUtils.deDES("46E8E743224934BA"));
	
	/** 管理员: -admin */
	public final static Identity ADMIN = new Identity(3, CryptoUtils.deDES("2FE643641A75D30D"));
	
	/** 当前登陆用户的身份 */
	private static Identity identity = USER;
	
	/** 授权等级 */
	private int level;
	
	/** 鉴别命令 */
	private String cmd;
	
	/**
	 * 构造函数
	 * @param level 授权等级
	 * @param cmd 鉴别命令
	 */
	private Identity(int level, String cmd) {
		this.level = level;
		this.cmd = cmd;
	}
	
	public int LV() {
		return level;
	}
	
	public String CMD() {
		return cmd;
	}
	
	public static Identity CURRENT() {
		return identity;
	}
	
	public static void set(Identity identity) {
		Identity.identity = (identity == null ? USER : identity);
	}
	
	public static boolean less(Identity identity) {
		boolean isLess = true;
		if(identity != null) {
			isLess = (CURRENT().LV() < identity.LV());
		}
		return isLess;
	}
	
}
