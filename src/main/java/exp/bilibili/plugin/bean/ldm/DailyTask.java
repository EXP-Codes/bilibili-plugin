package exp.bilibili.plugin.bean.ldm;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

public class DailyTask {

	public final static DailyTask NULL = new DailyTask(null);
	
	private final static int MAX_STEP = 9;
	
	private long bgnTime;
	
	private long endTime;
	
	/** 当前任务轮数 */
	private int curRound;
	
	/** 最大任务轮数 */
	private int maxRound;
	
	/** 当前轮的执行阶段:3min/6min/9min */
	private int step;
	
	public DailyTask(JSONObject json) {
		if(json != null) {
			JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data);
			this.bgnTime = JsonUtils.getLong(data, BiliCmdAtrbt.time_start, 0);
			this.endTime = JsonUtils.getLong(data, BiliCmdAtrbt.time_end, 0);
			this.curRound = JsonUtils.getInt(data, BiliCmdAtrbt.times, 0);
			this.maxRound = JsonUtils.getInt(data, BiliCmdAtrbt.max_times, 0);
			this.step = JsonUtils.getInt(data, BiliCmdAtrbt.minute, 0);
			
		} else {
			this.bgnTime = 0;
			this.endTime = 0;
			this.curRound = 0;
			this.maxRound = 0;
			this.step = MAX_STEP;
		}
	}
	
	public boolean existNext() {
		return !(curRound == maxRound && step == MAX_STEP);
	}

	public long getBgnTime() {
		return bgnTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public int getCurRound() {
		return curRound;
	}

	public int getMaxRound() {
		return maxRound;
	}

	public int getStep() {
		return step;
	}
	
}
