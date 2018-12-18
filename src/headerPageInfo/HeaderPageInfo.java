package headerPageInfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author ik04346
 *
 */
/**
 * @author ik04346
 *
 */
/**
 * @author ik04346
 *
 */
public class HeaderPageInfo {

	@Override
	public String toString() {
		return "HeaderPageInfo [dataPageCount=" + dataPageCount + ", couplesEntiers=" + couplesEntiers + "]";
	}

	private int dataPageCount;
	private ArrayList<CoupleEntiers> couplesEntiers;
	
	public HeaderPageInfo() {
		couplesEntiers = new ArrayList<CoupleEntiers>();
		dataPageCount = 0;
	}
	
	public int getPageCount()
	{
		return this.dataPageCount;
	}
	
	public void incrementer()
	{
		this.dataPageCount++;
	}
	
	
	
	public void addToHeaderPageInfo(int pageIdx, int freeSlots) {
		System.out.println("\n addToHeaderPageInfo() freeslots : " + freeSlots);
		couplesEntiers.add(new CoupleEntiers(pageIdx, freeSlots));
	}
	
	public CoupleEntiers getCoupleEntier (int i) {
		System.out.println("Contenu couplesEntiers : " + couplesEntiers);
		return couplesEntiers.get(i);
	}
	
	public ArrayList<CoupleEntiers> getCouplesEntier(){
		return couplesEntiers;
	}
	
	
	/**
	 * Lit les couples depuis le buffer (on rajoute les couples lus dans la liste des couples entiers)
	 * @param buffer -> le buffer à lire
	 */
	public void readFromBuffer(byte[] buffer) {
		System.err.println("Début readFromBuffer " + Arrays.toString(buffer));
		ByteBuffer b = ByteBuffer.wrap(buffer);
		dataPageCount = b.getInt();//DataCount pas bon (maxi : 2^24) à partir du moment où on veut insérer dans une autre page
		System.out.println("DataCount : " + dataPageCount);
		for(int i = 0; i < dataPageCount; i++) {
			couplesEntiers.add(new CoupleEntiers(b.getInt(), b.getInt()));
		}
		System.err.println("Fin readFromBuffer " + Arrays.toString(buffer));
	}
	
	
	/**
	 * Renseigne les couples dans le buffer
	 * @param buffer -> buffer à remplir
	 */
	public void writeToBuffer(byte[] buffer) {
		ByteBuffer b = ByteBuffer.wrap(buffer);
		b.putInt(dataPageCount);
		for(int i = 0; i < dataPageCount; i++) {
			b.putInt(getCoupleEntier(i).getPageIdx());
			b.putInt(getCoupleEntier(i).getFreeSlots());
		}
	}
	
}
