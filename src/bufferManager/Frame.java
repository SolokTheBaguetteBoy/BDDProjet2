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
	
	public Frame(PageId pid) {
		pin_count = 0;
		id = pid;
		dirtyFlag = false;
		loaded = false;
		buffer = new byte[Constantes.pageSize];
	}
	public Frame()
	{
		buffer = new byte [Constantes.pageSize];
	}
	public void incrementPinCount() {
		pin_count++;
	}
	
	public void decrementPinCount() {
		pin_count--;
//		if(pin_count == 0)//Le pin_count peut être supérieur à 1 si on a plusieurs utilisateurs qui utilisent une même page
//			lastUnpin = System.currentTimeMillis();
	}
	
	public void frameContentModified() {
		dirtyFlag = true;
	}
	
	public void frameLoad() {
		loaded = true;
	}
	
	public void frameUnloaded() {
		loaded = false;
		dirtyFlag = false;
		pin_count = 0;
	}
	
	public PageId getPageId() {
		return id;
	}

	public int getPinCount() {
		return pin_count;
	}
	
	public boolean getDirty() {
		return dirtyFlag;
	}
	
//	public long getLastUnpin() {
//		return lastUnpin;
//	}
	
	public byte[] getBuffer() {
		return buffer;
	}
	

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public void setId(PageId id) {
		this.id = id;
	}
	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	public void setPin_count(int pin_count) {
		this.pin_count = pin_count;
	}
//	public void setLastUnpin(long lastUnpin) {
//		this.lastUnpin = lastUnpin;
//	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	@Override
	public String toString() {
		return "Frame [loaded=" + loaded + ", id=" + id + ", dirtyFlag=" + dirtyFlag + ", pin_count=" + pin_count
				+ ", buffer=" + Arrays.toString(buffer) + "]";
	}

	
}
