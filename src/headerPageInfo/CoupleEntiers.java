package headerPageInfo;

public class CoupleEntiers {

    private int pageIdx;
    private int freeSlots;

    public CoupleEntiers(int pageIdx, int freeSlots) {
        this.pageIdx = pageIdx;
        this.freeSlots = freeSlots;
    }

    public int getPageIdx() {
        return pageIdx;
    }

    public void setFreeSlots(int freeSlots)
    {
        this.freeSlots = freeSlots;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

	@Override
	public String toString() {
		return "CoupleEntiers [pageIdx=" + pageIdx + ", freeSlots=" + freeSlots + "]";
	}
}