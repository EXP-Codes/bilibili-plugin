package exp.bilibili.plugin.cache;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.bilibili.protocol.XHRSender;
import exp.libs.utils.other.ListUtils;
import exp.libs.warp.thread.LoopThread;

/**
 * <PRE>
 * 节奏风暴扫描器
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-01-11
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class StormScanner extends LoopThread {

	private final static Logger log = LoggerFactory.getLogger(StormScanner.class);
	
	/** 试探轮询行为的间隔 */
	private final static long SLEEP_TIME = 2000;
	
	/** 每轮询N次所有房间，则刷新房间列表 */
	private final static int LOOP_LIMIT = 10;
	
	/** 轮询所有房间次数 */
	private int loopCnt;
	
	/** 总开关：是否扫描房间 */
	private boolean scan;
	
	/** 人气房间号(真实房号, 即长号) */
	private List<Integer> hotRoomIds;
	
	private static volatile StormScanner instance;
	
	protected StormScanner() {
		super("节奏风暴扫描器");
		
		this.loopCnt = LOOP_LIMIT;
		this.scan = false;
		this.hotRoomIds = new LinkedList<Integer>();
	}

	public static StormScanner getInstn() {
		if(instance == null) {
			synchronized (StormScanner.class) {
				if(instance == null) {
					instance = new StormScanner();
				}
			}
		}
		return instance;
	}
	
	public boolean isScan() {
		return scan;
	}

	public void setScan() {
		scan = !scan;
		if(scan == true) {
			loopCnt = LOOP_LIMIT;	// 触发重新扫描房间号
		}
	}
	
	@Override
	protected void _before() {
		log.info("{} 已启动", getName());
	}

	@Override
	protected void _loopRun() {
		if(isScan() == true) {
			if(loopCnt++ >= LOOP_LIMIT) {
				loopCnt = 0;
				reflashHotLives();
			}
			
			// 主动扫描: 在刷新直播间列表之前尽可能扫描每一个直播间
			sancAndJoinStorm();
		}
		_sleep(SLEEP_TIME);
	}

	@Override
	protected void _after() {
		log.info("{} 已停止", getName());
	}
	
	/**
	 * 刷新热门直播间
	 * @return
	 */
	public boolean reflashHotLives() {
		List<Integer> roomIds = XHRSender.queryTopLiveRoomIds();
		if(ListUtils.isNotEmpty(roomIds)) {
			hotRoomIds.clear();
			hotRoomIds.addAll(roomIds);
			log.info("已更新 [Top {}] 的人气直播间.", hotRoomIds.size());
		}
		return hotRoomIds.isEmpty();
	}
	
	/**
	 * 扫描并加入其他热门房间的节奏风暴抽奖
	 */
	public void sancAndJoinStorm() {
		XHRSender.scanAndJoinStorms(hotRoomIds);
	}
	
}
