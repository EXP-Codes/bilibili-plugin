package exp.bilibili.plugin.bean.ldm;

/**
 * <PRE>
 * 日常任务执行状态
 * </PRE>
 * <br/><B>PROJECT : </B> bilibili-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TaskStatus {

	private final static int _INIT = 0;
	
	private final static int SIGN = 1;
	
	private final static int ASSN = 2;
	
	private final static int MATH = 4;
	
	private final static int DAILT_GIFT = 8;
	
	private final static int HOLIDAY_GIFT = 16;
	
	private final static int FEED = 32;	// 投喂礼物不作为日常任务是否完成的判断依据之一
	
	private final static int _FIN = SIGN | ASSN | MATH | DAILT_GIFT | HOLIDAY_GIFT;
	
	private int status;
	
	public TaskStatus() {
		init();
	}
	
	public void init() {
		this.status = _INIT;
	}
	
	public boolean isAllFinish() {
		return status == _FIN;
	}
	
	public boolean isFinSign() { return (status & SIGN) != _INIT; }
	public boolean isFinAssn() { return (status & ASSN) != _INIT; }
	public boolean isFinMath() { return (status & MATH) != _INIT; }
	public boolean isFinDailyGift() { return (status & DAILT_GIFT) != _INIT; }
	public boolean isFinHoliday() { return (status & HOLIDAY_GIFT) != _INIT; }
	public boolean isFinFeed() { return (status & FEED) != _INIT; }
	
	public void markSign() { status |= SIGN; }
	public void markAssn() { status |= ASSN; }
	public void markMath() { status |= MATH; }
	public void markDailyGift() { status |= DAILT_GIFT; }
	public void markHolidayGift() { status |= HOLIDAY_GIFT; }
	public void markFeed() { status |= FEED; }
	
}
