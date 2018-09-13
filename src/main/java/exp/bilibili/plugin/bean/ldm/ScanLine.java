package exp.bilibili.plugin.bean.ldm;

public class ScanLine {
	
	private int bgn;
	
	private int end;
	
	public ScanLine(int bgn, int end) {
		this.bgn = bgn;
		this.end = end;
	}

	public int getBgn() {
		return bgn;
	}

	public void setBgn(int bgn) {
		this.bgn = bgn;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getDist() {
		return end - bgn + 1;
	}
	
}
