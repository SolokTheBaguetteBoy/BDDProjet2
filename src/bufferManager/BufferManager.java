package bufferManager;

import java.io.IOException;
import java.util.ArrayList;

import baseDeDonnee.DBManager;
import baseDeDonnee.DiskManager;
import baseDeDonnee.PageId;
import util.Constantes;

public class BufferManager {

	private ArrayList<Frame> bufferPool;
	private int frameCount = Constantes.frameCount;
	private int currentLoadedFrame = 0;
	
	public BufferManager() {
		
		frameCount = 2;
		bufferPool = new ArrayList<Frame>();
		
	}
	
	public Frame getFrame1() {
		return bufferPool.get(0);
	}
	
	public Frame getFrame2() {
		return bufferPool.get(1);
	}
	
	private static BufferManager INSTANCE = null;
	
	public static BufferManager getInstance() {
		if(INSTANCE == null){
			INSTANCE = new BufferManager();
		}
		return INSTANCE; 
	}
	
	public byte[] get(PageId pid) {
		boolean pageFound = false;
		
		for(Frame f : bufferPool) {
			if(f.getPageId().equals(pid)) {
				pageFound = true;
				f.incrementPinCount();
				System.out.println(f);
				return f.getBuffer();
			}
		}
		if(pageFound == false) {
			return load(pid);
		}
		return new byte[1]; //ne devrait jamais arriv� ici
	}

	private byte[] load(PageId pid) {
		Frame toAdd = new Frame(pid);
		toAdd.incrementPinCount();
		if(currentLoadedFrame < frameCount) {
			currentLoadedFrame++;		
			bufferPool.add(toAdd);
			return toAdd.getBuffer();
		}
		else {
			int frameToReplace = 0;
			int minPinCount = 0;
			if((bufferPool.get(0).getDirty() && bufferPool.get(1).getDirty())||(!bufferPool.get(0).getDirty() && !bufferPool.get(1).getDirty())) {
				for(int j = 0; j < frameCount; j++) {
					if(minPinCount > bufferPool.get(j).getPinCount() ) {
						minPinCount = bufferPool.get(j).getPinCount();
						frameToReplace = j;
					}
					else if(minPinCount == bufferPool.get(j).getPinCount() && bufferPool.get(frameToReplace).getLastUnpin() > bufferPool.get(j).getLastUnpin()) {
						frameToReplace = j;
					}
				}
				
			}
			else {
				if(bufferPool.get(0).getDirty()) {
					frameToReplace = 1;
				}
				if(bufferPool.get(1).getDirty()) {
					frameToReplace = 0;
				}
			}
			if(bufferPool.get(frameToReplace).getDirty()) {
				try {
					DiskManager.getInstance().writePage(bufferPool.get(frameToReplace).getPageId(), bufferPool.get(frameToReplace).getBuffer());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			bufferPool.remove(frameToReplace);
			bufferPool.add(frameToReplace,toAdd);
			return toAdd.getBuffer();
		}
	}
	
	public void free(PageId pid, boolean dirty) {
		for(Frame f : bufferPool) {
			if(f.getPageId().equals(pid)) {
				f.decrementPinCount();
				if(dirty == true) {
					f.frameContentModified();
				}
			}
		}
		
	}
	
	public void flushAll() throws IOException {
		for(Frame f : bufferPool) {
			if(f.getDirty() == true) {
				DiskManager.getInstance().writePage(f.getPageId(), f.getBuffer());
			}
		}
	}

	/**
	 * Remet le Buffer Manager � 0
	 */
	public void reset() {
		this.currentLoadedFrame = 0;
		this.bufferPool.clear();
	}
}
