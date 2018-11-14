package headerPageInfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeaderPageInfo {

	private int dataPageCount;
	private ArrayList<CoupleEntiers> couplesEntiers;
	
	public HeaderPageInfo() {
		couplesEntiers = new ArrayList<CoupleEntiers>();
		dataPageCount = 0;
	}
	
	public void addToHeaderPageInfo(int pageIdx, int freeSlots) {
		couplesEntiers.add(new CoupleEntiers(pageIdx, freeSlots));
	}
	
	public CoupleEntiers getCoupleEntier (int i) {
		return couplesEntiers.get(i);
	}
	
	public void readFromBuffer(byte[] buffer) {
		ByteBuffer b = ByteBuffer.wrap(buffer);
		dataPageCount = b.getInt();
		for(int i = 0; i < dataPageCount; i++) {
			couplesEntiers.add(new CoupleEntiers(b.getInt(), b.getInt()));
		}
	}
	
	public void writeToBuffer(byte[] buffer) {
		
	}
	
}