package exp.bilibili.robot;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.envm.CookieType;
import exp.libs.envm.HttpHead;
import exp.libs.utils.format.JsonUtils;
import exp.libs.warp.net.http.HttpURLUtils;

/**
 * <PRE>
 * 图灵AI聊天机器人
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ChatRobot {
	
	private final static String CHAT_SERVER = Config.getInstn().CHAT_SERVER();
	
	private final static String CHAT_KEY = Config.getInstn().CHAT_KEY();
	
	public static String send(String msg) {
		Map<String, String> header = getHeader();
		Map<String, String> request = getRequest(msg);
		String json = HttpURLUtils.doPost(CHAT_SERVER, header, request);
		
		String response = "";
		try {
			response = JsonUtils.getStr(JSONObject.fromObject(json), "text"); 
		} catch(Exception e) {}
		return response;
	}
	
	private static Map<String, String> getHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put(HttpHead.KEY.ACCEPT, "application/json, text/javascript, */*; q=0.01");
		header.put(HttpHead.KEY.ACCEPT_ENCODING, "gzip, deflate");
		header.put(HttpHead.KEY.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8");
		header.put(HttpHead.KEY.HOST, "www.tuling123.com");
		header.put(HttpHead.KEY.USER_AGENT, HttpHead.VAL.USER_AGENT);
		return header;
	}
	
	private static Map<String, String> getRequest(String msg) {
		Map<String, String> request = new HashMap<String, String>();
		request.put("key", CHAT_KEY);
		request.put("info", msg);
		return request;
	}
	
	
}
