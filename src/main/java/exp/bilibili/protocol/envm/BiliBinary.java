package exp.bilibili.protocol.envm;

import net.sf.json.JSONObject;
import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 通过Fiddler抓包解析得到的B站WebSocket交互的Binary数据
 * 
 * Fiddler抓包样例
 * 
	14:15:04:9179 WSSession199.WebSocket'WebSocket #199'
	MessageID:	Client.924
	MessageType:	Binary
	PayloadString:	00-00-00-1F-00-10-00-01-00-00-00-02-00-00-00-01-5B-6F-62-6A-65-63-74-20-4F-62-6A-65-63-74-5D
	Masking:	27-72-F5-28

	14:15:04:9529 WSSession199.WebSocket'WebSocket #199'
	MessageID:	Server.925
	MessageType:	Binary
	PayloadString:	00-00-00-14-00-10-00-01-00-00-00-03-00-00-00-01-00-00-00-0B
	Masking:	<none>

	14:47:20:8430 WSSession54.WebSocket'WebSocket #54'
	MessageID:	Client.7
	MessageType:	Close
	PayloadString:	03-E9
	Masking:	AD-73-35-23

 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class BiliBinary {

	/** 房间号长度 */
	private final static String LEN[] = {
		"0", "1", "2", "3", 
		"4", "5", "6", "7", 
		"8", "9", "A", "B", 
		"C", "D", "E", "F"
	};
	
	/** 私有化构造函数 */
	private BiliBinary() {}
	
	/**
	 * B站WebSocket建立会话时发送的链接数据.
	 *   其中前32个字节（两个十六进制数为1个字节， 1个字节8位）是固定.
	 *   第4个字节的低4位代表房间号的长度.
	 *   第33个字节开始就是json请求报文，格式形如 {"uid":0,"roomid":51108,"protover":1}
	 *  
	 * @param realRoomId 真实房间号（未签约主播和签约主播都有的房间号）
	 * @return
	 */
	public static String CLIENT_CONNECT(int realRoomId) {
		JSONObject json = new JSONObject();
		json.put(BiliCmdAtrbt.uid, 0);
		json.put(BiliCmdAtrbt.roomid, realRoomId);
		json.put(BiliCmdAtrbt.protover, 1);
		String hex = BODHUtils.toHex(json.toString().getBytes());
		String len = LEN[String.valueOf(realRoomId).length()];
		return StrUtils.concat("0000003", len, "001000010000000700000001", hex);
	}
	
	/** B站WebSocket保持会话时发送的心跳数据 */
	public final static String CLIENT_HB = 
			"0000001F0010000100000002000000015B6F626A656374204F626A6563745D";
	
	/** B站客户端主动发送断开连接的数据 */
	public final static String CLIENT_CLOSE = "03E9";
	
	/** B站WebSocket返回的连接确认信息 */
	public final static String SERVER_CONN_CONFIRM = 
			"00000010001000010000000800000001";
	
	/** B站WebSocket返回的心跳确认信息（末2个字节因为是变化值，此处已删除） */
	public final static String SERVER_HB_CONFIRM = 
			"00000014001000010000000300000001"; // 已去掉末尾变化字节
	
}
