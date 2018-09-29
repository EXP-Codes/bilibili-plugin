package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;

/**
 * 
 * <PRE>
 * 
 	(直播间内)高能抽奖结束消息
	{
	  "cmd": "RAFFLE_END",
	  "roomid": 269706,
	  "data": {
	    "raffleId": 61825,
	    "type": "openfire",
	    "from": "\u9f20\u4e8c\u4e09\u4e09",
	    "fromFace": "http:\/\/i2.hdslb.com\/bfs\/face\/4b309c88cc66c5d11558c47beb716b0fd8dd1438.jpg",
	    "win": {
	      "uname": "\u51b7\u7406Remonn",
	      "face": "http:\/\/i2.hdslb.com\/bfs\/face\/4b309c88cc66c5d11558c47beb716b0fd8dd1438.jpg",
	      "giftId": 105,
	      "giftName": "\u706b\u529b\u7968",
	      "giftNum": 50
	    }
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RaffleEnd extends RaffleStart {

	private String winner;
	
	private String giftName;
	
	private String giftNum;
	
	public RaffleEnd(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.RAFFLE_END;
	}
	
	@Override
	protected void analyse(JSONObject json) {
		super.analyse(json);
		
		JSONObject data = JsonUtils.getObject(json, BiliCmdAtrbt.data); {
			JSONObject win = JsonUtils.getObject(data, BiliCmdAtrbt.win); {
				this.winner = JsonUtils.getStr(win, BiliCmdAtrbt.uname);
				this.giftName = JsonUtils.getStr(win, BiliCmdAtrbt.giftName);
				this.giftNum = JsonUtils.getStr(win, BiliCmdAtrbt.giftNum);
			}
		}
	}

	public String getWinner() {
		return winner;
	}

	public String getGiftName() {
		return giftName;
	}

	public String getGiftNum() {
		return giftNum;
	}

}
