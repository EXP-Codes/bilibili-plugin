package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.bean.ldm.Raffle;
import exp.bilibili.plugin.bean.ldm.Raffles;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.VercodeUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.envm.Colors;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpURLUtils;
import exp.libs.warp.net.http.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 抽奖协议
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
class _Lottery extends __XHR {

	/** 获取节奏风暴验证码URL */
	private final static String STORM_CODE_URL = Config.getInstn().STORM_CODE_URL();
	
	/** 图片缓存目录 */
	private final static String IMG_DIR = Config.getInstn().IMG_DIR();
	
	/** 节奏风暴验证码图片宽度 */
	private final static int IMG_WIDTH = 112;
	
	/** 节奏风暴验证码图片高度 */
	private final static int IMG_HEIGHT = 32;
	
	/** 已经抽过的礼物 */
	private final static Raffles RAFFLES = new Raffles();
	
	/** 私有化构造函数 */
	protected _Lottery() {}
	
	/**
	 * 获取礼物编号
	 * @param response 
	 * 	{"code":0,"message":"0","ttl":1,"data":{"pk":[],"guard":[],"gift":[{"raffleId":427802,"type":"GIFT_30035","from_user":{"uid":0,"uname":"sy阿四","face":"http://i2.hdslb.com/bfs/face/7b37c75d6467210cdd211a60c061b3b605156d1b.jpg"},"time_wait":19,"time":79,"max_time":180,"status":1,"sender_type":1,"asset_icon":"http://s1.hdslb.com/bfs/live/28c2f3dd68170391d173ca2efd02bdabc917df26.png","asset_animation_pic":"http://i0.hdslb.com/bfs/live/d7303a91bf00446b2bc53b8726844b4ad813b9ed.gif","thank_text":"感谢\u003c%sy阿四%\u003e 赠送的任意门","weight":0,"gift_id":30035}]}}
	 *  {"code":0,"message":"0","ttl":1,"data":{"pk":[],"guard":[{"id":1496667,"sender":{"uid":2274252,"uname":"纯洁老爷","face":"http://i1.hdslb.com/bfs/face/6eefafb2e494c377a8470974eedcbdaf66a985f8.jpg"},"keyword":"guard","privilege_type":3,"time":949,"status":1,"time_wait":0,"asset_icon":"https://i0.hdslb.com/bfs/vc/43f488e7c4dca5ba6fbdcb88f40052d56bf777d8.png","asset_animation_pic":"https://i0.hdslb.com/bfs/vc/ff2a28492970850ce73df0cc144f1766b222d471.gif","thank_text":"恭喜\u003c%纯洁老爷%\u003e上任舰长","weight":0}],"gift":[]}}
	 * @return
	 */
	protected static List<Raffle> getRaffle(String url, int roomId, String cookie) {
		String sRoomId = getRealRoomId(roomId);
		Map<String, String> header = GET_HEADER(cookie, sRoomId);
		Map<String, String> request = getRequest(sRoomId);
		String response = HttpURLUtils.doGet(url, header, request);
		
		List<Raffle> raffles = new LinkedList<Raffle>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				JSONArray array = JsonUtils.getArray(data, BiliCmdAtrbt.gift);
				for(int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					log.warn(obj.toString());
					Raffle raffle = new Raffle(obj);
					if(RAFFLES.add(raffle)) {
						raffles.add(raffle);
					}
				}
				
				array = JsonUtils.getArray(data, BiliCmdAtrbt.guard);
				for(int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					log.warn(obj.toString());
					Raffle raffle = new Raffle(obj);
					if(RAFFLES.add(raffle)) {
						raffles.add(raffle);
					}
				}
				
			} else {
				String reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				log.warn("获取礼物编号失败: {}", reason);
			}
		} catch(Exception e) {
			log.error("获取礼物编号异常: {}", response, e);
		}
		return raffles;
	}
	
	/**
	 * 加入抽奖
	 * @param type 抽奖类型
	 * @param cookie 抽奖cookie
	 * @param url 抽奖URL
	 * @param roomId 直播间id
	 * @param raffle 抽奖号
	 * @param retryInterval 抽奖失败重试间隔(ms)
	 * @return 失败原因（若为空则成功）
	 */
	protected static String join(LotteryType type, BiliCookie cookie, 
			String url, int roomId, Raffle raffle) {
		String sRoomId = getRealRoomId(roomId);
		String visitId = getVisitId();
		Map<String, String> header = POST_HEADER(cookie.toNVCookie(), sRoomId);
		String reason = "";
		
		// 加入高能/小电视抽奖
		if(LotteryType.STORM != type) {
			Map<String, String> request = getRequest(cookie.CSRF(), sRoomId, raffle, visitId);
			log.warn(request.toString());
			for(int retry = 0; retry < 20; retry++) {
				String response = HttpURLUtils.doPost(url, header, request);
				log.warn(response);
				
				reason = analyse(response);
				if(StrUtils.isEmpty(reason) || !reason.contains("系统繁忙")) {
					break;
				}
				ThreadUtils.tSleep(100);
			}
			
		// 加入节奏风暴抽奖
		} else {
			for(int retry = 0; retry < 100; retry++) {
				String[] captcha = cookie.isRealName() ? // 实名认证后无需填节奏风暴验证码
						new String[] { "", "" } : getStormCaptcha(cookie);
				Map<String, String> request = getRequest(cookie.CSRF(), sRoomId, raffle, 
						visitId, captcha[0], captcha[1]);
				String response = HttpURLUtils.doPost(url, header, request);	// B站大概150ms才响应
				
				reason = analyse(response);
				if(StrUtils.isEmpty(reason) || reason.contains("不存在")) {
					break;
				}
				ThreadUtils.tSleep(10);	// 模拟连续点击，不能低于10ms，不然服务器会在一段时间拒绝响应
			}
		}
		return reason;
	}
	
	/**
	 * 查询礼物请求参数
	 * @param roomId
	 * @return
	 */
	protected static Map<String, String> getRequest(String roomId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.roomid, roomId);
		return request;
	}
	
	/**
	 * 高能/小电视抽奖请求参数
	 * @param roomId
	 * @return
	 */
	protected static Map<String, String> getRequest(String csrf, String roomId, Raffle raffle, String visitId) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.id, raffle.getRaffleId());
		request.put(BiliCmdAtrbt.roomid, roomId);
		request.put(BiliCmdAtrbt.type, raffle.getType());
		request.put(BiliCmdAtrbt.csrf_token, csrf);
		request.put(BiliCmdAtrbt.csrf, csrf);
		request.put(BiliCmdAtrbt.visit_id, visitId);
		return request;
	}
	
	/**
	 * 节奏风暴抽奖请求参数
	 * @param roomId
	 * @param raffleId
	 * @param csrf
	 * @param captchaToken 验证码token (实名认证的账号可不填)
	 * @param captchaValue 验证码值 (实名认证的账号可不填)
	 * @return
	 */
	private static Map<String, String> getRequest(String csrf, String roomId, Raffle raffle, String visitId, 
			String captchaToken, String captchaValue) {
		Map<String, String> request = getRequest(csrf, roomId, raffle, visitId);
		request.put(BiliCmdAtrbt.color, Colors.WHITE.RGB());
		request.put(BiliCmdAtrbt.captcha_token, captchaToken);
		request.put(BiliCmdAtrbt.captcha_phrase, captchaValue);
		return request;
	}
	
	/**
	 * 抽奖结果分析
	 * @param response 
	 *   小电视     {"code":0,"msg":"加入成功","message":"加入成功","data":{"3392133":"small","511589":"small","8536920":"small","raffleId":"46506","1275939":"small","20177919":"small","12768615":"small","1698233":"small","4986301":"small","102015208":"small","40573511":"small","4799261":"small","from":"喵熊°","time":59,"30430088":"small","558038":"small","5599305":"small","8068250":"small","16293951":"small","7294374":"small","type":"openfire","7384826":"small","2229668":"small","7828145":"small","2322836":"small","915804":"small","86845000":"small","3076423":"small","roomid":"97835","5979210":"small","16345975":"small","7151219":"small","1479304":"small","19123719":"small","29129155":"small","7913373":"small","17049098":"small","9008673":"small","23406718":"small","141718":"small","27880394":"small","942837":"small","107844643":"small","face":"http://i1.hdslb.com/bfs/face/66b91fc04ccd3ccb23ad5f0966a7c3da5600b0cc.jpg","31437943":"small","34810599":"small","102994056":"small","31470791":"small","26643554":"small","29080508":"small","14709391":"small","14530810":"small","46520094":"small","2142310":"small","status":2,"77959868":"small","76979807":"small"}}
	 *   节奏风暴 {"code":0,"msg":"","message":"","data":{"gift_id":39,"title":"节奏风暴","content":"<p>你是前 35 位跟风大师<br />恭喜你获得一个亿圆(7天有效期)</p>","mobile_content":"你是前 35 位跟风大师","gift_img":"http://static.hdslb.com/live-static/live-room/images/gift-section/gift-39.png?2017011901","gift_num":1,"gift_name":"亿圆"}}
	 * @return 失败原因
	 */
	private static String analyse(String response) {
		String reason = "";
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code != 0) {
				reason = JsonUtils.getStr(json, BiliCmdAtrbt.msg);
				
				// 未实名认证且不填写验证码, 则会出现异常原因为空的情况
				if(StrUtils.isEmpty(reason)) {
					reason = "验证码错误";
					
				// 这两种异常实际上是服务器没响应请求，需要一直抢
				} else if(reason.contains("错过了奖励") || reason.contains("已经领取")) {
					reason = "再接再励";
				}
			}
		} catch(Exception e) {
			reason = "服务器异常";
			log.error("参加抽奖异常, 服务器响应内容格式错误: {}", response);
		}
		return reason;
	}
	
	/**
	 * 解析节奏风暴验证码图片
	 * {"code":0,"msg":"","message":"","data":{"token":"aa4f1a6dad33c3b16926a70e9e0eadbfb56ba91c","image":"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2NjIpLCBxdWFsaXR5ID0gODAK/9sAQwAGBAUGBQQGBgUGBwcGCAoQCgoJCQoUDg8MEBcUGBgXFBYWGh0lHxobIxwWFiAsICMmJykqKRkfLTAtKDAlKCko/9sAQwEHBwcKCAoTCgoTKBoWGigoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgo/8AAEQgAIABwAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A+lKKKK883Oa8WeMLDw5LDBPHPc3kw3R28C5YjOMn0FU/Dfjy01jVRps9ld6fesCyJcLjcP8APtWpe6HpqeIU8RXUjR3FvCY8uwEYXnk5HXk965VrL/hOfEb6nArw6VaW720FxyrXDnOWHfaM1yTlVjLR9dvI8+rOvGpo1vou6736HoxIUEsQAOST2rK1vX7HR9PS9unL2zyLGGiw3JOPXpXmel+ILjTvAer6JMW/tS0n+xxAnlhIcD8vm/DFWvHWljQPhnpVjGu6RLiMt/tOcsf1pPFNwcorZX+fYmWObpucFsr+j7fmerIwdFdeVYZFOrzO517xvpemDUbnSbJrCNAzxo+XRPU8+lX/AAX4yuPEnia6hQKmnrbrJGm35gx65PfnNaRxMHJRd035G0cbTclBppvurHX65fLpmjX163S3heT6kDgfnXmfhSHxxPoNtqVhq0Vx54LC3uuSoyQOT64zXT+K/E/h6fStSs7p5ryCMrHdLbKTsBPXd06471gXXg6C10P+2fCOtXcHlQmeMNJuR1Azj2rGs3Od4u6S6OzOfEydSpeDuoro7Pffz2PQPDz6nJpULa5FFFf8+YsRyvU4x+GK0q5Pwn4qS+8FLrWqEReSrCdlHBKnGQPf+tdJYXIvLKG5EckSyoHCSY3AHpnBI/WuqnOMoqzvod1GpGUY8rvpfzLFFFFaGwUUVyt74b1S6vJ5P+EhuooJHZ1jjUjy+cgA7u34VtRpwm3zy5fvf5EttbIpfE7Rda1y30+30dY5LdZGe4jeTYH6bQfUfepmnaX4uZrcX+o6fpmnQ4zBaJklR2yRwPoas/2L4pj/AHEfiBHt/wDno8X7wfof505fCl/esF13Wpru16tbouxWPbJB/pQ8voc7qSrfdf8AyX5nJLDqVR1NbvzsjjNWuNFl+K73rTL9jtY0kmKfMJJhwMY64yM/Q1d+KWuWOt+HLWPSLlZZ0vEcqVKlQFbnkdM4rvdO8NaNp0nmWmnwJJ/fI3N+ZzViTRdLkdmk06zZmGCTCvI/L2qI0MHyShLmfM3qrL8NfzJWDbhODfxNtnlmr/Eee48O3GlNpjDUJYjbtKrho+RgsPwrkdIfUrDUb7T/AA1uubia2WNpY1IKjAZ8Zx3JGa93h8K6HDcGZNNty5GMMCy/98nj9Kq6T4Wi03xZqOtQzKEu4liW2SMKsYAUZznn7voOtc2JwtKTi4Sk3frZWVn263tr+Bz1sBVqSjKUr9O1lqcF4E1K/u9Cm0vStEsXC/u7jzWyWbuXBOTnpVzS/h1Ldw3KzzXmjhnw1tDLvjdcdRyf5mu0tPCllZeJZdZs5Z4JphiWJCPLf6jFWvFd5d2OhXMunW8txdkBI1jUsVJ43EDnA657VbcJUFTq043j1V7/AJ9eqNY4WKpfv1fl7dvkcrHpdtdXtr4Y0sEaVpbrPeSE58x+oTI/izyeR9K9BrH8LaOujaWkb/vLyX95czHlpZD1JPfHStipow5Vd7v+rHVh6fJG7Vm/w7L5BRRRWp0H/9k="}}
	 * @param cookie
	 * @return { 验证码token, 验证码图片的解析值 }
	 */
	private static String[] getStormCaptcha(BiliCookie cookie) {
		Map<String, String> header = GET_HEADER(cookie.toNVCookie(), "");
		Map<String, String> request = _getRequest();
		String response = HttpURLUtils.doGet(STORM_CODE_URL, header, request);
		
		String[] rst = { "", "" };
		try {
			JSONObject json = JSONObject.fromObject(response);
			int code = JsonUtils.getInt(json, BiliCmdAtrbt.code, -1);
			if(code == 0) {
				JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
				String token = JsonUtils.getStr(data, BiliCmdAtrbt.token);
				String image = JsonUtils.getStr(data, BiliCmdAtrbt.image);
				String savePath = HttpUtils.convertBase64Img(image, IMG_DIR, "storm");
				
				rst[1] = VercodeUtils.recognizeStormImage(savePath);
				rst[0] = StrUtils.isEmpty(rst[1]) ? "" : token;
			}
		} catch(Exception e) {
			log.error("获取节奏风暴验证码图片异常: {}", response, e);
		}
		return rst;
	}
	
	/**
	 * 获取节奏风暴验证码参数
	 * @return
	 */
	private static Map<String, String> _getRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.underline, String.valueOf(System.currentTimeMillis()));
		request.put(BiliCmdAtrbt.width, String.valueOf(IMG_WIDTH));
		request.put(BiliCmdAtrbt.height, String.valueOf(IMG_HEIGHT));
		return request;
	}
	
}
