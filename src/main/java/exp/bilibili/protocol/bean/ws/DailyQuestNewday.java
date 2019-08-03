package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;

/**
 * 
 * <PRE>
 * 
	凌晨 00:00 新一天的通知事件：
	{
	  "cmd": "DAILY_QUEST_NEWDAY",
	  "data": {
	    
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class DailyQuestNewday extends _Msg {

	public DailyQuestNewday(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.DAILY_QUEST_NEWDAY;
	}

	@Override
	protected void analyse(JSONObject json) {
		// Undo
	}

}
