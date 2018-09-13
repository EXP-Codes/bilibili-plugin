package exp.bilibili.plugin.envm;

public class LotteryType {

	/** 其他礼物抽奖 */
	private final static short TYPE_OTHER = 0;
	public final static LotteryType OTHER = new LotteryType(TYPE_OTHER);
	
	/** 小电视抽奖 */
	private final static short TYPE_TV = 1;
	public final static LotteryType TV = new LotteryType(TYPE_TV);
	
	/** 节奏风暴抽奖 */
	private final static short TYPE_STORM = 2;
	public final static LotteryType STORM = new LotteryType(TYPE_STORM);
	
	private short type;
	
	private LotteryType(short type) {
		this.type = type;
	}
	
	public short TYPE() {
		return type;
	}
	
}
