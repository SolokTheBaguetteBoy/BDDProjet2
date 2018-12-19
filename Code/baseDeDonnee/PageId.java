package baseDeDonnee;

public class PageId {
	
	private int pageIdx;
	
	private int fileIdx;
	
	/**
	 * Constructeur de PageId
	 * @param pageIndex index de la page
	 * @param fileIndex index du fichier
	 */
	public PageId(int pageIndex, int fileIndex) {
		this.pageIdx = pageIndex;
		this.fileIdx = fileIndex;
	}
	
	/**
	 * Version vide du constructeur de PageId
	 */
	public PageId()
	{
		
	}

	/**
	 * Getter de pageIdx
	 * @return l'index pageIdx de la page
	 */
	public int getPageIdx() {
		return pageIdx;
	}

	/**
	 * Setter de pageIdx
	 * @param pageIdx que l'on attribue
	 */
	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	/**
	 * Getter de fileIdx
	 * @return fileIdx index du fichier
	 */
	public int getFileIdx() {
		return fileIdx;
	}

	/**
	 * Setter de fileIdx
	 * @param fileIdx que l'on attribue
	 */
	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}

	@Override
	public String toString() {
		return "PageId [pageIdx=" + pageIdx + ", fileIdx=" + fileIdx + "]";
	}
	

}
