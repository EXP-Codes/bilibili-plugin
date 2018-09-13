package exp.bilibili.plugin.bean.pdm;

import net.sf.json.JSONObject;
import exp.bilibili.plugin.envm.BiliCmd;

/**
 * 
 * <PRE>
 * 
	(直播间内)许愿瓶实现进度消息：
	{
	  "cmd": "WISH_BOTTLE",
	  "data": {
	    "action": "update",
	    "id": 2861,
	    "wish": {
	      "id": 2861,
	      "uid": 20872515,
	      "type": 1,
	      "type_id": 4,
	      "wish_limit": 100,
	      "wish_progress": 7,
	      "status": 1,
	      "content": "唱青媚狐",
	      "ctime": "2018-01-13 22:28:45",
	      "count_map": [
	        1,
	        10,
	        100
	      ]
	    }
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class WishBottle extends _Msg {

	public WishBottle(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.WISH_BOTTLE;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		// Undo
	}

}
