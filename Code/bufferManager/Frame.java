package bufferManager;

import java.util.Arrays;
import java.util.Date;

import baseDeDonnee.PageId;
import util.Constantes;

public class Frame {

	private boolean loaded;
	private PageId id;
	private boolean dirtyFlag;
	private int pin_count;
	//private long lastUnpin;
	private byte[] buffer;
	
	/**
	 * Constructeur de Frame
	 * @param pid PageId de la page
	 */
	public Frame(PageId pid) {
		pin_count = 0;
		id = pid;
		dirtyFlag = false;
		loaded = false;
		buffer = new byte[Constantes.pageSize];
	}
	/**
	 * Constructeur de Frame sans parametre, le met en place que le buffer
	 */
	public Frame()
	{
		buffer = new byte [Constantes.pageSize];
	}
		
	/**
	 * Fonction augmentant la valeur de pinCount de 1
	 */
	public void incrementPinCount() {
		pin_count++;
	}
	
	/**
	 * Fonction reduisant la valeur de pinCount de 1
	 */
	public void decrementPinCount() {
		pin_count--;
//		if(pin_count == 0)//Le pin_count peut être supérieur à 1 si on a plusieurs utilisateurs qui utilisent une même page
//			lastUnpin = System.currentTimeMillis();
	}
	
	/**
	 * Met le dirtyFlage de la Frame a true
	 */
	public void frameContentModified() {
		dirtyFlag = true;
	}
	
	/**
	 * Attribue true au boolean loaded
	 */
	public void frameLoad() {
		loaded = true;
	}
	
	/**
	 * Decharge la frame, passant loader a false, dirtyFlage a false et mettant le pin_count a 0
	 */
	public void frameUnloaded() {
		loaded = false;
		dirtyFlag = false;
		pin_count = 0;
	}
	
	/**
	 * Getter de id
	 * @return id PageId de la frame
	 */
	public PageId getPageId() {
		return id;
	}

	/**
	 * Getter de pin_count
	 * @return pin_count le pin_count de la frame
	 */
	public int getPinCount() {
		return pin_count;
	}
	
	/**
	 * Getter de dirtyFlag
	 * @return dirtyFlag le dirtyFlag de la frame
	 */
	public boolean getDirty() {
		return dirtyFlag;
	}
	
//	public long getLastUnpin() {
//		return lastUnpin;
//	}
	
	/**
	 * Getter du buffer de la frame
	 * @return buffer le buffer de la frame
	 */
	public byte[] getBuffer() {
		return buffer;
	}
	
	/**
	 * Setter de loaded
	 * @param loaded la valeur que l'on veut attribue a loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
	/**
	 * Setter de id
	 * @param id PageId que l'on souhaite attribue a la frame
	 */
	public void setId(PageId id) {
		this.id = id;
	}
	
	/**
	 * Setter de dirtyFlag
	 * @param dirtyFlag la valeur que l'on attribue au dirtyFlag de la frame
	 */
	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	
	/**
	 * Setter de pin_count
	 * @param pin_count valeur que l'on attribue au pin_count de la frame
	 */
	public void setPin_count(int pin_count) {
		this.pin_count = pin_count;
	}
//	public void setLastUnpin(long lastUnpin) {
//		this.lastUnpin = lastUnpin;
//	}
	
	/**
	 * Setter du buffer de la frame
	 * @param buffer que l'on souhaite attribue a la frame
	 */
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	@Override
	public String toString() {
		return "Frame [loaded=" + loaded + ", id=" + id + ", dirtyFlag=" + dirtyFlag + ", pin_count=" + pin_count
				+ ", buffer=" + Arrays.toString(buffer) + "]";
	}

	
}
