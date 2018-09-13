package exp.bilibili.plugin.bean.ldm;

/**
 * <PRE>
 * 热门直播间的页码范围.
 * 	用于节奏风暴扫描(每页30个房间)
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HotLiveRange {

	private int bgnPage;
	
	private int endPage;
	
	/**
	 * 构造函数
	 * @param bgnPage 起始页码（包括）
	 * @param endPage 终止页码（包括）
	 */
	public HotLiveRange(int bgnPage, int endPage) {
		this.bgnPage = (bgnPage < 1 ? 1 : bgnPage);
		this.endPage = (endPage < bgnPage ? (bgnPage + 1) : endPage);
	}
	
	public int BGN_PAGE() {
		return bgnPage;
	}
	
	public int END_PAGE() {
		return endPage;
	}
	
}
