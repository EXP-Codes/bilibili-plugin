package exp.bilibili.protocol.xhr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exp.bilibili.plugin.Config;
import exp.bilibili.plugin.bean.ldm.BiliCookie;
import exp.bilibili.plugin.bean.ldm.HotLiveRange;
import exp.bilibili.plugin.bean.ldm.Raffle;
import exp.bilibili.plugin.bean.ldm.Raffles;
import exp.bilibili.plugin.cache.CookiesMgr;
import exp.bilibili.plugin.cache.RoomMgr;
import exp.bilibili.plugin.envm.LotteryType;
import exp.bilibili.plugin.utils.UIUtils;
import exp.bilibili.protocol.envm.BiliCmdAtrbt;
import exp.libs.utils.format.JsonUtils;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.http.HttpClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 节奏风暴抽奖
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class LotteryStorm extends _Lottery {

	/** 查询热门直播间列表URL */
	private final static String LIVE_URL = Config.getInstn().LIVE_URL();
	
	/** 节奏风暴检测URL */
	private final static String STORM_CHECK_URL = Config.getInstn().STORM_CHECK_URL();
	
	/** 节奏风暴抽奖URL */
	private final static String STORM_JOIN_URL = Config.getInstn().STORM_JOIN_URL();
	
	/** 已经抽过的节奏风暴ID */
	private final static Raffles RAFFLES = new Raffles();
	
	/** 私有化构造函数 */
	protected LotteryStorm() {}
	
	/**
	 * 扫描当前的人气直播间房号列表
	 * @param range 扫描范围
	 * @return
	 */
	public static List<Integer> queryHotLiveRoomIds(HotLiveRange range) {
		Map<String, String> header = GET_HEADER("", "all?visit_id=".concat(getVisitId()));
		Map<String, String> request = getRequest();
		
		List<Integer> roomIds = new LinkedList<Integer>();
		HttpClient client = new HttpClient();
		for(int page = range.BGN_PAGE(); page <= range.END_PAGE(); page++) {
			request.put(BiliCmdAtrbt.page, String.valueOf(page));
			String response = client.doGet(LIVE_URL, header, request);
			roomIds.addAll(analyse(response));
		}
		client.close();
		return roomIds;
	}
	
	/**
	 * 查询热门直播间请求参数
	 * @return
	 */
	private static Map<String, String> getRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(BiliCmdAtrbt.areaId, "0");
		request.put(BiliCmdAtrbt.sort, "online");
		request.put(BiliCmdAtrbt.pageSize, "30");
		return request;
	}
	
	/**
	 * 提取热门直播间的房间号.
	 * 	(同时顺便关联每个房间的长短号: 此行为与节奏风暴抽奖无关)
	 * @param response {"code":0,"msg":"ok","data":[{"roomid":99783,"short_id":828,"uid":7005369,"uname":"\u5251\u7f513\u5b98\u65b9\u89c6\u9891","face":"http:\/\/i1.hdslb.com\/bfs\/face\/85bd12a028ea4c4fa66448acc4ddc0609e824e01.jpg","title":"\u5251\u7f513\u5168\u6c11\u5403\u9e21\u2014\u2014\u51b3\u6218\u9f99\u95e8\uff01\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/99783.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/981b2bc3fd7aff23ba1725e9bb2b6fba6938e212.jpg","online":645731,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/828","stream_id":90886,"area_v2_id":82,"area_v2_name":"\u5251\u7f513","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":271744,"short_id":0,"uid":1577804,"uname":"\u67d0\u5e7b\u541b","face":"http:\/\/i1.hdslb.com\/bfs\/face\/9ed5ebf1e3694d9cd2b4fcd1d353759ee83b3dfe.jpg","title":"\u4e2d\u6587\u516b\u7ea7","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/271744.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/1c737f7ee1ec2f4400b61c863bcd5a0585900958.jpg","online":194894,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/271744","stream_id":262803,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1011,"short_id":0,"uid":4162287,"uname":"\u6e17\u900f\u4e4bC\u541b","face":"http:\/\/i0.hdslb.com\/bfs\/face\/623ccce0ab28b721edb61dd64749d91de18fb384.jpg","title":"\u6210\u5e74\u4eba\u7684\u6e38\u620f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1011.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e09ed2b466b9ca9481b3a5a477305b3227aeac36.jpg","online":171792,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/1011","stream_id":479,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5441,"short_id":528,"uid":322892,"uname":"\u75d2\u5c40\u957f","face":"http:\/\/i1.hdslb.com\/bfs\/face\/bcdf640faa16ebaacea1d4c930baabaec9087a80.jpg","title":"\u4eba\u7c7b\u4e00\u8d25\u6d82\u5730","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5441.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/0b564ffecbee9d962d6eee53b4c4c17b82f84beb.jpg","online":133722,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/528","stream_id":930,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":12722,"short_id":1040,"uid":352577,"uname":"\u6df3\u8272","face":"http:\/\/i2.hdslb.com\/bfs\/face\/39f3f9d4f1a0679a3409ee8b76ea8737307ba6b4.jpg","title":"\u8363\u8000\u4e00\u5439\uff1a\u8363\u8000\u5c40\u4e0a51\u661f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/12722.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/1844209d7a58f61adfb117f34fd844ee09d98bd9.jpg","online":119625,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/1040","stream_id":3658,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":5279,"short_id":102,"uid":110631,"uname":"\u5bab\u672c\u72d7\u96e8","face":"http:\/\/i1.hdslb.com\/bfs\/face\/8c49a758216f9bd14b0046afe48a3514f44126f0.jpg","title":"\u7edd\u5730\u5927\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5279.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/63602c757dd6aaf2f498cb3d44b47fced6589a1e.jpg","online":117283,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/102","stream_id":774,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1029,"short_id":139,"uid":43536,"uname":"\u9ed1\u6850\u8c37\u6b4c","face":"http:\/\/i1.hdslb.com\/bfs\/face\/e2dae77b01436e8e9c99a392caeb58dff0415cf4.jpg","title":"\u4ece\u96f6\u5f00\u59cb\u7684\u4e9a\u6960\u751f\u6d3b","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1029.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/ddabaa6a568a8c19417b34de70dbc1c40a22d085.jpg","online":100075,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/139","stream_id":497,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":66688,"short_id":0,"uid":20848957,"uname":"\u98ce\u7af9\u6559\u4e3b\u89e3\u8bf4","face":"http:\/\/i0.hdslb.com\/bfs\/face\/288f13d1f589a3d6386d022044fbc10b705cab4f.jpg","title":"\u573a\u5747\u4e0d\u80fd12\u6740\uff0c\u5403\u4ec0\u4e48\u9e21\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/66688.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e45d8e74090431aa91d8749a6b4ada6dd2de768e.jpg","online":89878,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/66688","stream_id":57731,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5313,"short_id":0,"uid":6043533,"uname":"\u9b45\u84dd\u624b\u673a","face":"http:\/\/i0.hdslb.com\/bfs\/face\/797907168d743cf24b347d8d329bd4ae3ce0ff11.jpg","title":"\u674e\u6960\u80fd\u5426\u5403\u9e21\uff1f0117\u9b45\u84dd\u65b0\u54c1\u60e8\u906d\u66dd\u5149\uff1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5313.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/56711ac3d8a2a85ee625e370b62e5fddb3506955.jpg","online":89675,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/5313","stream_id":806,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5096,"short_id":388,"uid":183430,"uname":"\u4e24\u4eea\u6eda","face":"http:\/\/i0.hdslb.com\/bfs\/face\/10542620e3225773e0a3848888ccc4bf93d12488.jpg","title":"\u3010\u6eda\u3011\u6253\u724c\u665a\u4e0a\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5096.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/e64902520ab6e0aaeb6e2d1b721cccbf241045d3.jpg","online":82395,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/388","stream_id":594,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":83264,"short_id":1125,"uid":1864366,"uname":"\u9ed1\u54f2\u541b","face":"http:\/\/i2.hdslb.com\/bfs\/face\/0053321a0ec1e53824b8fc0ab8297469c9ce2816.jpg","title":"(\uff40\uff65\u0434\uff65\u00b4\uff09\u611f\u53d7\u7edd\u671b\u5427\u4f60","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/83264.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/98a2f3578cc4410298e2560aa716ddd6000a68cf.jpg","online":68605,"area":4,"areaName":"\u7535\u5b50\u7ade\u6280","link":"\/1125","stream_id":74307,"area_v2_id":86,"area_v2_name":"\u82f1\u96c4\u8054\u76df","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5067,"short_id":1000,"uid":227933,"uname":"\u5742\u672c\u53d4","face":"http:\/\/i1.hdslb.com\/bfs\/face\/1e31ac069058528e26b9be60b26d86c9c9a99f62.jpg","title":"\u3010\u5742\u672c\u3011\u4e0d\u770b\u4eac\u7d2b\u5168\u5c01\u4e86\uff01","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5067.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/c4accb76d4cd291a51e129c002e0cf97c9604d12.jpg","online":61137,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/1000","stream_id":565,"area_v2_id":65,"area_v2_name":"\u5f69\u8679\u516d\u53f7","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":92613,"short_id":0,"uid":13046,"uname":"\u5c11\u5e74Pi","face":"http:\/\/i0.hdslb.com\/bfs\/face\/d851f48a579778b06249bf3debaa62d353694e91.jpg","title":"\u73a9\u706b\u67f4\u4eba\u6253\u67b6","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/92613.jpg?01112020","online":66560,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/92613","stream_id":83716,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":1175880,"short_id":367,"uid":1872628,"uname":"\u67ab\u8a00w","face":"http:\/\/i1.hdslb.com\/bfs\/face\/6590e763ec8ad1a3ba7ed5237949c048be91a7c3.jpg","title":"\u65b0\u7248\u672c\u65e5\u5e38","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1175880.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/cb06bbfb83dd510ceab7171cbd61ed123416d1c5.jpg","online":50619,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/367","stream_id":1167310,"area_v2_id":40,"area_v2_name":"\u5d29\u574f3","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":175412,"short_id":553,"uid":14308645,"uname":"37\u4e0d\u662f37","face":"http:\/\/i2.hdslb.com\/bfs\/face\/d7b5e23b8ad7140240fd1ed132c180420ae5b54a.jpg","title":"\u301037\u3011\u9e21\u901f\u4e16\u754c!","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/175412.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/8efc55ced0655a0411a5731cbb65c14c28836581.jpg","online":51185,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/553","stream_id":166470,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":56948,"short_id":446,"uid":1767542,"uname":"\u7b28\u5c3c\u65af\u7279","face":"http:\/\/i2.hdslb.com\/bfs\/face\/f7611e57f90efa247f58422634f628dcf07aafb0.jpg","title":"\u5413\u6b7b\u4eba\u4e86\u5413\u6b7b\u4eba\u4e86","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/56948.jpg?01112017","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/70f3431665da09151e377779e6cc3c9d32e56886.jpg","online":42991,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/446","stream_id":47991,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":63727,"short_id":48,"uid":2832224,"uname":"SNH48\u5b98\u65b9\u8d26\u53f7","face":"http:\/\/i1.hdslb.com\/bfs\/face\/0bf6a963fbdfa98f5e243bb2e4d152ace4544592.jpg","title":"SNH48 XII\u961f\u300a\u4ee3\u53f7XII\u300b\u516c\u6f14","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/63727.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/82f6baacccaa1a5085d535bfff6643c2456ad9b5.jpg","online":41509,"area":10,"areaName":"\u5531\u89c1\u821e\u89c1","link":"\/48","stream_id":54770,"area_v2_id":22,"area_v2_name":"\u821e\u89c1","area_v2_parent_id":1,"area_v2_parent_name":"\u5a31\u4e50","is_tv":0,"is_bn":""},{"roomid":544893,"short_id":0,"uid":13705279,"uname":"\u7761\u4e0d\u9192\u7684\u67d0\u67d0\u9633","face":"http:\/\/i2.hdslb.com\/bfs\/face\/a198ab1e6fc7e33480af5b4c989fd4092d154e7e.jpg","title":"\u8f6f\u7ef5\u7ef58\u4eba\u8054\u673a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/544893.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/badf0684e43ac6fd7c50b1bc67676aeb2dd6175e.jpg","online":40825,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/544893","stream_id":535954,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":544941,"short_id":594,"uid":6501408,"uname":"\u86c7\u8db3","face":"http:\/\/i2.hdslb.com\/bfs\/face\/54f174a503c7830b52c02faa5732520b3e2986bf.jpg","title":"\u3010\u86c7\u8db3\u3011\u4eca\u5929\u662f\u5e05\u6c14\u7684\u773c\u955c\u54e6","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/544941.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/54907967cd6bca4140e4bbfe12570d834eef139b.png","online":36547,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/594","stream_id":536002,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":90713,"short_id":469,"uid":7946235,"uname":"\u987e\u4e8e\u6d6e\u751f\u5982\u68a6","face":"http:\/\/i1.hdslb.com\/bfs\/face\/7bba9f90ba3ab44b8e77c54ee300ac1c43b158b3.jpg","title":"\u738b\u8005\u8363\u8000 \u5fae\u4fe1\u533a\u4e0a\u738b\u8005","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/90713.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/32ea33b3aec9654a826ffd3bcb4dbb65571b0040.jpg","online":38818,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/469","stream_id":81816,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":933508,"short_id":512,"uid":4705522,"uname":"\u6c99\u62c9Azusa","face":"http:\/\/i1.hdslb.com\/bfs\/face\/67f01127a411016e2b20cfd3d2e088d651856f31.jpg","title":"\u5973\u88c5up\u5728\u7537\u5bdd\u5ba4\u5973\u88c5\u662f\u4ec0\u4e48\u611f\u89c9\uff1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/933508.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/387163597abdcf9cdc26e3c148cac417cedac69c.jpg","online":39282,"area":6,"areaName":"\u751f\u6d3b\u5a31\u4e50","link":"\/512","stream_id":924597,"area_v2_id":26,"area_v2_name":"\u65e5\u5e38","area_v2_parent_id":1,"area_v2_parent_name":"\u5a31\u4e50","is_tv":0,"is_bn":""},{"roomid":79558,"short_id":305,"uid":6810019,"uname":"AnKe-Poi","face":"http:\/\/i2.hdslb.com\/bfs\/face\/ae8aea930b21e86a83313dd3ad12cd8192e8bf49.jpg","title":"\u3010\u5b89\u53ef\u3011\u4ed9\u5883\u5403\u9e21\u5927\u9003\u6740","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/79558.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/656172a98c80d2eb67c91b2279958e4eea772d7c.jpg","online":36426,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/305","stream_id":70601,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":37338,"short_id":465,"uid":5907649,"uname":"\u5343\u8449\u578bDJ","face":"http:\/\/i0.hdslb.com\/bfs\/face\/8e1e4196cbcf5e5d9144c82bca70b134f324dabf.jpg","title":"ServantPara \u5fa9\u523b \u8d0b\u4f5c\u82f1\u970a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/37338.jpg?01112019","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/2b41199cd44b0859a1a7ef77050c40e410f5f85b.jpg","online":36424,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/465","stream_id":28306,"area_v2_id":37,"area_v2_name":"Fate\/GO","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":1374115,"short_id":0,"uid":7774837,"uname":"\u8fd9\u4e2a\u9ed1\u5ca9\u4e0d\u592a\u51b7","face":"http:\/\/i1.hdslb.com\/bfs\/face\/96bf6348d02dc0985bda0042eab2922da3643b67.jpg","title":"\u55d3\u5b50\u53d1\u708e\uff0c\u8bdd\u5c11...","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/1374115.jpg?01112015","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/78c62f4482152ee201786ed3f57e1024f136250b.jpg","online":34620,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/1374115","stream_id":1365946,"area_v2_id":84,"area_v2_name":"300\u82f1\u96c4","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":21133,"short_id":0,"uid":7450650,"uname":"\u8d85\u679c\u679cmc","face":"http:\/\/i1.hdslb.com\/bfs\/face\/f6deabfcc901bb31e5ab42e8ec23067c1332b9ef.jpg","title":"[\u7edd\u5730\u6c42\u751f]\u5403\u9e21\u5403\u9e21","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/21133.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/7b920862d0230ae38b8e4b10d3faae4798cc409a.jpg","online":34571,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/21133","stream_id":12073,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":5123,"short_id":0,"uid":372418,"uname":"\u6bd2\u5976\u6cd3\u5e0c","face":"http:\/\/i2.hdslb.com\/bfs\/face\/db00f2d1f6557c4a4e3e5e4eacbd48908b4f1498.jpg","title":"\u3010\u6cd3\u5e0c\u7089\u77f3\u3011\u65b0\u53d1\u578b.\u597d\u5eb7\u7684","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5123.jpg?01112018","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/cd65a69fce92597ac44cd43054fcae14ce024879.jpg","online":32644,"area":3,"areaName":"\u7f51\u7edc\u6e38\u620f","link":"\/5123","stream_id":621,"area_v2_id":91,"area_v2_name":"\u7089\u77f3\u4f20\u8bf4","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":73014,"short_id":0,"uid":3405965,"uname":"\u5416\u9b3c123","face":"http:\/\/i1.hdslb.com\/bfs\/face\/b244b4c1fae1e776737d72652ad51d7ff087aa9d.jpg","title":"\u3010\u5416\u9b3c\u3011\u6700\u5f3a\u6cd5\u5e08\u6559\u5b66\uff0c\u592a\u9633\u51fa\u6765\u9e1f","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/73014.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/82dd68b1b4b17c743cd35a94fa342bdf486c9838.jpg","online":24730,"area":12,"areaName":"\u624b\u6e38\u76f4\u64ad","link":"\/73014","stream_id":64057,"area_v2_id":35,"area_v2_name":"\u738b\u8005\u8363\u8000","area_v2_parent_id":3,"area_v2_parent_name":"\u624b\u6e38","is_tv":0,"is_bn":""},{"roomid":5031,"short_id":112,"uid":228342,"uname":"\u7a7a\u8033YAYA","face":"http:\/\/i1.hdslb.com\/bfs\/face\/12324212a2736ea1ee045a95add648531a395555.jpg","title":"\u6700\u7ec8\u5e7b\u60f3\u7eb7\u4e89NT \u5267\u60c5\u8d70\u8d77","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/5031.jpg?01112016","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/9c779c70e9c532c37b57233b8c2de57681a1dbfb.jpg","online":24663,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/112","stream_id":530,"area_v2_id":107,"area_v2_name":"\u5176\u4ed6\u6e38\u620f","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":24065,"short_id":0,"uid":193584,"uname":"\u95fb\u9999\u8bc6","face":"http:\/\/i1.hdslb.com\/bfs\/face\/4d417312a927ea3f895904afeb349bafbaeb45ca.jpg","title":"\u3010\u95fb\u9999\u8bc6\u3011\u6218\u6597","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/24065.jpg?01112019","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/7c6b3e2a94de37151a8076c71afb2a0c51bfc375.jpg","online":25039,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/24065","stream_id":15005,"area_v2_id":65,"area_v2_name":"\u5f69\u8679\u516d\u53f7","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""},{"roomid":50583,"short_id":154,"uid":201293,"uname":"\u4e03\u4e03\u89c1\u5948\u6ce2\u4e36","face":"http:\/\/i0.hdslb.com\/bfs\/face\/e7473106f5435023444dc259719b4b38312bf1b0.jpg","title":"\u5403\u9e21\u9047\u5230\u59b9 \u4e24\u773c\u6cea\u6c6a\u6c6a","cover":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","user_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","system_cover":"http:\/\/i0.hdslb.com\/bfs\/live\/50583.jpg?01112020","pic":"http:\/\/i0.hdslb.com\/bfs\/live\/efb2eaad7564db32af75c8891bd7f4bd924b1aab.jpg","online":25783,"area":1,"areaName":"\u5355\u673a\u8054\u673a","link":"\/154","stream_id":41561,"area_v2_id":80,"area_v2_name":"\u7edd\u5730\u6c42\u751f\uff1a\u5927\u9003\u6740","area_v2_parent_id":2,"area_v2_parent_name":"\u6e38\u620f","is_tv":0,"is_bn":""}]}
	 * @return
	 */
	private static List<Integer> analyse(String response) {
		List<Integer> roomIds = new LinkedList<Integer>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			JSONArray data = JsonUtils.getArray(json, BiliCmdAtrbt.data);
			for(int i = 0 ; i < data.size(); i++) {
				JSONObject room = data.getJSONObject(i);
				int realRoomId = JsonUtils.getInt(room, BiliCmdAtrbt.roomid, 0);
//				int online = JsonUtils.getInt(room, BiliCmdAtrbt.online, 0);
//				if(online > 3000) {	// 在线人气达标才纳入扫描范围
					roomIds.add(realRoomId);
//				}
				
				// 顺便关联房间号(短号)与真实房号(长号)
				int shortId = JsonUtils.getInt(room, BiliCmdAtrbt.short_id, -1);
				int roomId = (shortId > 0 ? shortId : realRoomId);
				RoomMgr.getInstn().relate(roomId, realRoomId);
			}
		} catch(Exception e) {
			log.error("提取人气直播房间号失败: {}", response, e);
		}
		return roomIds;
	}
	
	/**
	 * 扫描房间中是否有节奏风暴, 有则加入节奏风暴抽奖（若有舰队抽奖则顺便加入）
	 * @param hotRoomIds 热门房间列表
	 * @param scanInterval 扫描房间间隔
	 */
	public static void toLottery(List<Integer> hotRoomIds, long scanInterval) {
		HttpClient client = new HttpClient();
		Map<String, String> request = new HashMap<String, String>();
		for(Integer roomId : hotRoomIds) {
			String sRoomId = String.valueOf(roomId);
			request.put(BiliCmdAtrbt.roomid, sRoomId);
			Map<String, String> header = GET_HEADER(
					CookiesMgr.VEST().toNVCookie(), sRoomId);
			
			String response = client.doGet(STORM_CHECK_URL, header, request);
			List<Raffle> raffles = getStormRaffles(roomId, response);
			join(roomId, raffles);	// 参加节奏风暴抽奖
			
			Guard.getGuardGift(roomId);	// 顺便领取舰队奖励
			ThreadUtils.tSleep(scanInterval);
		}
		client.close();
	}
	
	/**
	 * 获取节奏风暴的抽奖ID
	 * @param roomId
	 * @param response {"code":0,"msg":"","message":"","data":{"id":318289831272,"roomid":291623,"num":100,"send_num":"1","time":76,"content":"要优雅，不要污","hasJoin":0,"storm_gif":"https://static.hdslb.com/live-static/live-room/images/gift-section/mobilegift/2/jiezou.gif?2017011901"}}
	 * @return
	 */
	private static List<Raffle> getStormRaffles(int roomId, String response) {
		List<Raffle> raffles = new LinkedList<Raffle>();
		try {
			JSONObject json = JSONObject.fromObject(response);
			Object data = json.get(BiliCmdAtrbt.data);
			if(data instanceof JSONObject) {
				JSONObject room = (JSONObject) data;
				String raffleId = JsonUtils.getStr(room, BiliCmdAtrbt.id);
				Raffle raffle = new Raffle();
				raffle.setRaffleId(raffleId);
				
				if(RAFFLES.add(raffle)) {
					raffles.add(raffle);
				}
						
			} else if(data instanceof JSONArray) {
				JSONArray array = (JSONArray) data;
				for(int i = 0 ; i < array.size(); i++) {
					JSONObject room = array.getJSONObject(i);
					String raffleId = JsonUtils.getStr(room, BiliCmdAtrbt.id);
					Raffle raffle = new Raffle();
					raffle.setRaffleId(raffleId);
					
					if(RAFFLES.add(raffle)) {
						raffles.add(raffle);
					}
				}
			}
		} catch(Exception e) {
			log.error("提取直播间 [{}] 的节奏风暴信息失败: {}", roomId, response, e);
		}
		return raffles;
	}
	
	/**
	 * 加入节奏风暴抽奖
	 * @param roomId
	 * @param raffles
	 * @return
	 */
	private static void join(final int roomId, final List<Raffle> raffles) {
		if(raffles.size() > 0) {
			String msg = StrUtils.concat("直播间 [", roomId, 
					"] 开启了节奏风暴 [x", raffles.size(), "] !!!");
			UIUtils.notify(msg);
			
			new Thread() {
				public void run() {
					for(Raffle raffle : raffles) {
						toLottery(roomId, raffle);
					}
				};
			}.start();
		}
	}
	
	/**
	 * 参加节奏风暴抽奖
	 * @param roomId
	 * @param raffleId
	 * @return
	 */
	public static boolean toLottery(int roomId, Raffle raffle) {
		int cnt = 0;
		String reason = "未知异常";
		Set<BiliCookie> cookies = CookiesMgr.ALL(false);
		
		Iterator<BiliCookie> cookieIts = cookies.iterator();
		while(cookieIts.hasNext()) {
			BiliCookie cookie = cookieIts.next();
			
			// 未绑定手机或未实名认证的账号无法参与节奏风暴  (FIXME 未实名也可参加, 但是需要填验证码, 目前未能自动识别验证码)
			if(!cookie.isBindTel() || !cookie.isRealName()) {
				cookieIts.remove();
				continue;
			}
			
			// 仅被关小黑屋的账号才能参与节奏风暴
			if(UIUtils.isOnlyFreeze() && !cookie.isFreeze()) {
				cookieIts.remove();
				continue;
			}
			
			reason = join(LotteryType.STORM, cookie, STORM_JOIN_URL, roomId, raffle);
			if(StrUtils.isEmpty(reason)) {
				log.info("[{}] 参与直播间 [{}] 抽奖成功(节奏风暴)", cookie.NICKNAME(), roomId);
				cookie.updateLotteryTime();
				cookieIts.remove();	// 已经成功抽奖的在本轮无需再抽
				cnt++;
				
			} else if(reason.contains("访问被拒绝")) {
				UIUtils.statistics("失败(", reason, "): 账号 [", cookie.NICKNAME(), "]");
				cookie.freeze();
				cookieIts.remove();	// 被临时封禁抽奖的账号无需再抽
				
			} else {
				log.info("[{}] 参与直播间 [{}] 抽奖失败(节奏风暴)", cookie.NICKNAME(), roomId);
			}
		}
		
		if(cnt > 0) {
			UIUtils.statistics("成功(节奏风暴x", cnt, "): 直播间 [", roomId, "]");
			UIUtils.updateLotteryCnt(cnt);
			
		} else {
			if(reason.contains("不存在")) {
				reason = "亿圆被抢光啦";
			}
			UIUtils.statistics("失败(", reason, "): 直播间 [", roomId, "]");
		}
		return (cnt > 0);
	}
	
}
