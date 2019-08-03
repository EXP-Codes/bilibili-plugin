package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;

/**
 * 
 * <PRE>
 * 
	红色活动横幅通知事件：
	{
	  "cmd": "ACTIVITY_BANNER_RED_NOTICE_CLOSE",
	  "data": {
	    "id": 297,
	    "type": "revenue_banner"
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ActivityBannerRedNoticeClose extends _Msg {

	public ActivityBannerRedNoticeClose(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.ACTIVITY_BANNER_RED_NOTICE_CLOSE;
	}

	@Override
	protected void analyse(JSONObject json) {
		// Undo
	}

}
