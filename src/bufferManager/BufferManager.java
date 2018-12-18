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
		//boolean pageFound = false;
		System.out.println("Taille bufferpool : " + bufferPool.size());
		for(Frame f : bufferPool) {
			if((f.getPageId() == null)) {
				f.setId(pid);
				f.incrementPinCount();
				System.out.println(f);
				return f.getBuffer();
			}
			if(f.getPageId() != null) {
				if((f.getPageId().getPageIdx() == pid.getPageIdx() && f.getPageId().getFileIdx() == pid.getFileIdx())) {
					//pageFound = true;
					f.incrementPinCount();
					System.out.println("get() trouvé : " + f);
					return f.getBuffer();
				}
			}
		}
		
		int indice = load(pid);
		if(bufferPool.get(indice).getDirty())
		{
			DiskManager.getInstance().writePage(bufferPool.get(indice).getPageId(), bufferPool.get(indice).getBuffer());
			bufferPool.get(indice).setDirtyFlag(false);
		}
		bufferPool.set(indice, new Frame());
		bufferPool.get(indice).setId(pid);
		
		bufferPool.get(indice).incrementPinCount();
		//bufferPool.get(indice).setBuffer(new byte[Constantes.pageSize]);
		System.out.println("BufferManager get(pid) pid = " + pid);
		DiskManager.getInstance().readPage(bufferPool.get(indice).getPageId(), bufferPool.get(indice).getBuffer());
		
		return bufferPool.get(indice).getBuffer();
	}

	private int load(PageId pid) throws IOException {
		
		for(int i = 0; i<bufferPool.size(); i++)
		{
			if(bufferPool.get(i).getPinCount() == 0)
				return i;
		}
		return 0;
		
		

	}
	
	/**
	 * Libere la page d'un utilisateur
	 * @param pid
	 * @param dirty
	 */
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
	
	/**
	 * Ecrit le contenu des frames dans le fichier
	 * @throws IOException
	 */
	public void flushAll() throws IOException {
		for(Frame f : bufferPool) {
			if(f.getDirty() == true) {
				DiskManager.getInstance().writePage(f.getPageId(), f.getBuffer());
			}
			f.setDirtyFlag(false);
			f.setBuffer(new byte[Constantes.pageSize]);
		}
		
		
	}

	/**
	 * Remet le Buffer Manager a 0
	 */
	public void reset() {
		//this.currentLoadedFrame = 0;
		//Permettre la réinitialisation du BufferManager car Singleton
		INSTANCE = null;
	}
}
