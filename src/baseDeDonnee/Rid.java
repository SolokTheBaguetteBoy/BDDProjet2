package baseDeDonnee;

public class Rid {
	
	private PageId pid;
	private int sltIdx;
	
	public Rid(PageId pid, int sltIdx) {
		this.pid = pid;
		this.sltIdx = sltIdx;
	}
	
	public PageId getPid() {
		return pid;
	}
	public void setPid(PageId pid) {
		this.pid = pid;
	}
	public int getSltIdx() {
		return sltIdx;
	}
	public void setSltIdx(int sltIdx) {
		this.sltIdx = sltIdx;
	}
	
	
	
}
