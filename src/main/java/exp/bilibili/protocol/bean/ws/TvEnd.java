package exp.bilibili.protocol.bean.ws;

import net.sf.json.JSONObject;
import exp.bilibili.protocol.envm.BiliCmd;

/**
 * 
 * <PRE>
 * 
	小电视中奖消息：
	{
	  "cmd": "TV_END",
	  "data": {
	    "id": "165740",
	    "uname": "仇家太多不愿意透露姓名的某楠",
	    "sname": "镜虚名",
	    "giftName": "100000x银瓜子",
	    "mobileTips": "恭喜 仇家太多不愿意透露姓名的某楠 获得100000x银瓜子",
	    "raffleId": "165740",
	    "type": "small_tv",
	    "from": "镜虚名",
	    "fromFace": "http:\/\/i1.hdslb.com\/bfs\/face\/0bd90719b3fd27739c698db7ff24d500630c7685.jpg",
	    "fromGiftId": 25,
	    "win": {
	      "uname": "仇家太多不愿意透露姓名的某楠",
	      "face": "http:\/\/i0.hdslb.com\/bfs\/face\/1fbe1886241d26f2c55a2630ad644fe6a090ec06.jpg",
	      "giftName": "银瓜子",
	      "giftId": "silver",
	      "giftNum": 100000,
	      "giftImage": "http:\/\/s1.hdslb.com\/bfs\/live\/00d768b444f1e1197312e57531325cde66bf0556.png",
	      "msg": "恭喜 <%仇家太多不愿意透露姓名的某楠%> 获得大奖 <%100000x银瓜子%>, 感谢 <%镜虚名%> 的赠送"
	    }
	  }
	}
 * </PRE>
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TvEnd extends _Msg {

	public TvEnd(JSONObject json) {
		super(json);
		this.cmd = BiliCmd.TV_END;
	}

	@Override
	protected void analyse(JSONObject json) {
		// Undo
	}

}
