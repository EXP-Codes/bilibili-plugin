package exp.bilibili.plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exp.bilibili.plugin.cache.RoomMgr;
import exp.libs.envm.Charset;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.conf.xml.XConfig;
import exp.libs.warp.conf.xml.XConfigFactory;

/**
 * <PRE>
 * 程序配置
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Config {
	
	public final static String DEFAULT_CHARSET = Charset.UTF8;
	
	private final static String APP_PATH = "/exp/bilibili/plugin/bp_conf.xml";
	
	private final static String USER_PATH = "./conf/bp_conf.xml";
	
	public final static int DEFAULT_ROOM_ID = 390480;
	
	private Set<Integer> tabuAutoRoomIds;
	
	private static volatile Config instance;
	
	private XConfig xConf;
	
	private Config() {
		this.xConf = XConfigFactory.createConfig("BILIBILI_CONF");
		xConf.loadConfFileInJar(APP_PATH);
		xConf.loadConfFile(USER_PATH);
		
		this.tabuAutoRoomIds = new HashSet<Integer>();
		readTabuAutoRoomIds();
	}
	
	public static Config getInstn() {
		if(instance == null) {
			synchronized (Config.class) {
				if(instance == null) {
					instance = new Config();
				}
			}
		}
		return instance;
	}
	
	public String WEBSOCKET() {
		return xConf.getVal("/config/urls/ws");
	}
	
	public String MAIN_HOME() {
		return xConf.getVal("/config/urls/mainHome");
	}
	
	public String LIVE_HOME() {
		return xConf.getVal("/config/urls/liveHome");
	}
	
	public String LINK_HOME() {
		return xConf.getVal("/config/urls/linkHome");
	}
	
	public String MSG_HOME() {
		return xConf.getVal("/config/urls/msgHome");
	}
	
	public String LINK_HOST() {
		return xConf.getVal("/config/urls/linkHost");
	}
	
	public String MSG_URL() {
		return xConf.getVal("/config/urls/msgURL");
	}
	
	public String LOGIN_HOST() {
		return xConf.getVal("/config/urls/loginHost");
	}
	
	public String QRCODE_URL() {
		return xConf.getVal("/config/urls/qrcodeURL");
	}
	
	public String QRCHECK_URL() {
		return xConf.getVal("/config/urls/qrcheckURL");
	}
	
	public String QRLOGIN_URL() {
		return xConf.getVal("/config/urls/qrloginURL");
	}
	
	public String VCCODE_URL() {
		return xConf.getVal("/config/urls/vccodeURL");
	}
	
	public String RSA_URL() {
		return xConf.getVal("/config/urls/rsaURL");
	}
	
	public String VCLOGIN_URL() {
		return xConf.getVal("/config/urls/vcloginURL");
	}
	
	public String SAFE_URL() {
		return xConf.getVal("/config/urls/safeURL");
	}
	
	public String LIVE_HOST() {
		return xConf.getVal("/config/urls/liveHost");
	}
	
	public String LIVE_URL() {
		return xConf.getVal("/config/urls/liveURL");
	}
	
	public String BLACK_URL() {
		return xConf.getVal("/config/urls/blackURL");
	}
	
	public String CHAT_URL() {
		return xConf.getVal("/config/urls/chatURL");
	}
	
	public String SIGN_URL() {
		return xConf.getVal("/config/urls/signURL");
	}
	
	public String ASSN_URL() {
		return xConf.getVal("/config/urls/assnURL");
	}
	
	public String ACCOUNT_URL() {
		return xConf.getVal("/config/urls/accountURL");
	}
	
	public String ROOM_URL() {
		return xConf.getVal("/config/urls/roomURL");
	}
	
	public String PLAYER_URL() {
		return xConf.getVal("/config/urls/playerURL");
	}
	
	public String MANAGE_URL() {
		return xConf.getVal("/config/urls/manageURL");
	}
	
	public String BAG_URL() {
		return xConf.getVal("/config/urls/bagURL");
	}
	
	public String FEED_URL() {
		return xConf.getVal("/config/urls/feedURL");
	}
	
	public String GET_CAPSULE_URL() {
		return xConf.getVal("/config/urls/getCapsuleURL");
	}
	
	public String OPEN_CAPSULE_URL() {
		return xConf.getVal("/config/urls/openCapsuleURL");
	}
	
	public String GET_ACHIEVE_URL() {
		return xConf.getVal("/config/urls/getAchieveURL");
	}
	
	public String DO_ACHIEVE_URL() {
		return xConf.getVal("/config/urls/doAchieveURL");
	}
	
	public String STORM_CHECK_URL() {
		return xConf.getVal("/config/urls/stormCheckURL");
	}
	
	public String STORM_CODE_URL() {
		return xConf.getVal("/config/urls/stormCodeURL");
	}
	
	public String STORM_JOIN_URL() {
		return xConf.getVal("/config/urls/stormJoinURL");
	}
	
	public String EG_CHECK_URL() {
		return xConf.getVal("/config/urls/egCheckURL");
	}
	
	public String EG_JOIN_URL() {
		return xConf.getVal("/config/urls/egJoinURL");
	}
	
	public String TV_JOIN_URL() {
		return xConf.getVal("/config/urls/tvJoinURL");
	}
	
	public String MATH_CHECK_URL() {
		return xConf.getVal("/config/urls/mathCheckURL");
	}
	
	public String MATH_CODE_URL() {
		return xConf.getVal("/config/urls/mathCodeURL");
	}
	
	public String MATH_EXEC_URL() {
		return xConf.getVal("/config/urls/mathExecURL");
	}
	
	public String APP_VIDEO_URL() {
		return xConf.getVal("/config/urls/apVideoURL");
	}
	
	public String APP_WATCH_URL() {
		return xConf.getVal("/config/urls/appWatchURL");
	}
	
	public String PC_WATCH_URL() {
		return xConf.getVal("/config/urls/pcWatchURL");
	}
	
	public String GET_BUCKET_URL() {
		return xConf.getVal("/config/urls/getBucket");
	}
	
	public String EX_BUCKET_URL() {
		return xConf.getVal("/config/urls/exBucket");
	}
	
	public String GET_REDBAG_URL() {
		return xConf.getVal("/config/urls/getRedbag");
	}
	
	public String EX_REDBAG_URL() {
		return xConf.getVal("/config/urls/exRedbag");
	}
	
	public String COOKIE_DIR() {
		return xConf.getVal("/config/files/cookies");
	}
	
	public String IMG_DIR() {
		return xConf.getVal("/config/files/img");
	}
	
	public String ROOM_PATH() {
		return xConf.getVal("/config/files/room");
	}
	
	public String ADV_PATH() {
		return xConf.getVal("/config/files/advs");
	}
	
	public String NIGHT_PATH() {
		return xConf.getVal("/config/files/nights");
	}
		
	public String CALL_PATH() {
		return xConf.getVal("/config/files/calls");
	}
	
	public String CARD_PATH() {
		return xConf.getVal("/config/files/cards");
	}
	
	public String NOTICE_PATH() {
		return xConf.getVal("/config/files/notices");
	}
	
	public String MUSIC_PATH() {
		return xConf.getVal("/config/files/musics");
	}
	
	public int SIGN_ROOM_ID() {
		return xConf.getInt("/config/app/signRoomId");
	}
	
	public boolean isTabuAutoChat(int roomId) {
		int realRoomId = RoomMgr.getInstn().getRealRoomId(roomId);
		return (realRoomId > 0 ? tabuAutoRoomIds.contains(realRoomId) : false);
	}
	
	public int ACTIVITY_ROOM_ID() {
		return xConf.getInt("/config/app/activityRoomId");
	}
	
	public long STORM_FREQUENCY() {
		return xConf.getLong("/config/app/stormFrequency");
	}
	
	/**
	 * 设置默认房间号（每日签到用）
	 * (房间勋章等级越高签到奖励越多)
	 */
	public boolean setSignRoomId(int roomId) {
		boolean isOk = false;
		final String REGEX = "(<signRoomId[^>]+>)[^<]*(</signRoomId>)";
		if(roomId > 0) {
			String xml = FileUtils.read(USER_PATH, DEFAULT_CHARSET);
			Pattern ptn = Pattern.compile(REGEX);
			Matcher mth = ptn.matcher(xml);
			if(mth.find()) {
				String head = mth.group(1);
				String tail = mth.group(2);
				String txt = StrUtils.concat(head, roomId, tail);
				xml = xml.replace(mth.group(0), txt);
				
				isOk = FileUtils.write(USER_PATH, xml, DEFAULT_CHARSET, false);
			}
		}
		return isOk;
	}
	
	private void readTabuAutoRoomIds() {
		String tabu = xConf.getVal("/config/app/tabuAutoRoomIds");
		String[] roomIds = tabu.split(",");
		for(String roomId : roomIds) {
			roomId = roomId.trim();
			if(StrUtils.isNotEmpty(roomId)) {
				tabuAutoRoomIds.add(NumUtils.toInt(roomId, 0));
			}
		}
	}
}
