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
	private long lastUnpin;
	private byte[] buffer;
	
	public Frame(PageId pid) {
		pin_count = 0;
		id = pid;
		dirtyFlag = false;
		loaded = false;
		buffer = new byte[Constantes.pageSize];
	}
	
	public void incrementPinCount() {
		pin_count++;
	}
	
	public void decrementPinCount() {
		pin_count--;
		lastUnpin = System.currentTimeMillis();
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
	
	public long getLastUnpin() {
		return lastUnpin;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}

	@Override
	public String toString() {
		return "Frame [loaded=" + loaded + ", id=" + id + ", dirtyFlag=" + dirtyFlag + ", pin_count=" + pin_count
				+ ", lastUnpin=" + lastUnpin + ", buffer=" + Arrays.toString(buffer) + "]";
	}
	
	
}
