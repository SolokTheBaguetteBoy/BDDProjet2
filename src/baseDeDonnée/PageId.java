package baseDeDonnée;

public class PageId {
	
	private int pageIdx;
	
	private int fileIdx;
	
	public PageId(int pageIndex, int fileIndex) {
		this.pageIdx = pageIndex;
		this.fileIdx = fileIndex;
	}
	
	public PageId()
	{
		
	}

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}
	

}
