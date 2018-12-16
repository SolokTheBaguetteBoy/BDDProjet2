package bufferManager;

import java.io.IOException;
import java.util.ArrayList;

import baseDeDonnee.DBManager;
import baseDeDonnee.DiskManager;
import baseDeDonnee.PageId;
import util.Constantes;

public class BufferManager {

	private static ArrayList<Frame> bufferPool;
	private int frameCount = Constantes.frameCount;
	private int currentLoadedFrame = 0;
	private static BufferManager INSTANCE = null;
	
	
	public Frame getFrame1() {
		return bufferPool.get(0);
	}
	
	public Frame getFrame2() {
		return bufferPool.get(1);
	}
	
	
	public static BufferManager getInstance() {
		
		
		if(INSTANCE == null){
			INSTANCE = new BufferManager();
			bufferPool = new ArrayList<Frame>();
			for (int i = 0; i < Constantes.frameCount; i++) {
				bufferPool.add(new Frame());
			}
			System.out.println("Taille bufferPool : "+bufferPool.size());
		}
		return INSTANCE; 
	}
	
	public byte[] get(PageId pid) throws IOException {
		boolean pageFound = false;
		System.out.println("Taille bufferpool : " + bufferPool.size());
		for(Frame f : bufferPool) {
//			if((f.getPageId() == null)) {
//				pageFound = true;
//				f.incrementPinCount();
//				System.out.println(f);
//				return load(pid);
//			}
			if(f.getPageId() != null) {
				if((f.getPageId().getPageIdx() == pid.getPageIdx() && f.getPageId().getFileIdx() == pid.getFileIdx())) {
					pageFound = true;
					f.incrementPinCount();
					System.out.println(f);
					return f.getBuffer();
				}
			}
		}
		if(pageFound == false) {
			return load(pid);
		}
		return new byte[1]; //ne devrait jamais arriv� ici
	}

	private byte[] load(PageId pid) throws IOException {
		System.out.println(pid);
		DiskManager.getInstance().readPage(pid, getFrame1().getBuffer());
		System.out.println(pid);
		//toAdd.incrementPinCount();
		if(currentLoadedFrame < frameCount) {
			currentLoadedFrame++;		
			bufferPool.get(0).setId(pid);
			System.err.println("bufferpool : "+bufferPool);
			return bufferPool.get(0).getBuffer();
		}
		else {
			int frameToReplace = 0;
//			int minPinCount = 0;
//			if((bufferPool.get(0).getDirty() && bufferPool.get(1).getDirty())||(!bufferPool.get(0).getDirty() && !bufferPool.get(1).getDirty())) {
//				for(int indiceFrame = 0; indiceFrame < frameCount; indiceFrame++) {
//					if(minPinCount > bufferPool.get(indiceFrame).getPinCount()) {
//						minPinCount = bufferPool.get(indiceFrame).getPinCount();
//						frameToReplace = indiceFrame;
//					}
//					else if(minPinCount == bufferPool.get(indiceFrame).getPinCount() && bufferPool.get(frameToReplace).getLastUnpin() > bufferPool.get(indiceFrame).getLastUnpin()) {
//						frameToReplace = indiceFrame;
//					}
//				}
//				
//			}
//			else {
//				if(bufferPool.get(0).getDirty()) {
//					frameToReplace = 1;
//				}
//				if(bufferPool.get(1).getDirty()) {
//					frameToReplace = 0;
//				}header
//			}
			if(bufferPool.get(0).getPinCount() == 0)
				frameToReplace = 0;
			else if(bufferPool.get(1).getPinCount() == 0)
				frameToReplace = 1;
			else if(bufferPool.get(0).getPinCount() != 0 && bufferPool.get(1).getPinCount() != 0)
			{
				if(bufferPool.get(0).getLastUnpin() < bufferPool.get(1).getLastUnpin())
					frameToReplace = 0;
				else
					frameToReplace = 1;
			}
			if(bufferPool.get(frameToReplace).getDirty()) {
				try {
					DiskManager.getInstance().writePage(bufferPool.get(frameToReplace).getPageId(), bufferPool.get(frameToReplace).getBuffer());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Frame à remplacer : " + bufferPool.get(frameToReplace));
			bufferPool.get(frameToReplace).setId(pid);
			System.out.println("bufferPool : " + bufferPool);
			return bufferPool.get(frameToReplace).getBuffer();
		}
	}
	
	public void free(PageId pid, boolean dirty) {
		System.out.println("Taille bufferPool : " + bufferPool.size());
		System.out.println("Contenu bufferPool : " + bufferPool);
		for(Frame f : bufferPool) {
			if(pid.equals(f.getPageId())) {
				f.decrementPinCount();
				if(dirty == true) {
					f.frameContentModified();
					break;
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
		//this.currentLoadedFrame = 0;
		//Permettre la réinitialisation du BufferManager car Singleton
		INSTANCE = null;
	}
}
