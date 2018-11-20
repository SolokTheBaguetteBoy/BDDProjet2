package baseDeDonnee;

import java.io.IOException;

import bufferManager.BufferManager;
import headerPageInfo.CoupleEntiers;
import headerPageInfo.HeaderPageInfo;

public class HeapFile {
	
	private RelDef listeChainee;
	private DiskManager dm;
	private BufferManager bm;
	public HeapFile() {
		// TODO Auto-generated constructor stub
		this.dm = DiskManager.getInstance();
		this.bm = BufferManager.getInstance();
	}
	
	public void createNewOnDisk(int iFileIdx) throws IOException
	{
		HeaderPageInfo header = new HeaderPageInfo();
		PageId pid = new PageId();
		dm.createFile(iFileIdx);
		dm.addPage(iFileIdx, pid);
		header.writeToBuffer(bm.get(pid));
		this.bm.free(pid, true);	
	}
	
	public void getFreePageId(PageId oPageId) throws IOException
	{
		boolean libre = false;
		byte buffer[] = this.bm.get(oPageId);
		HeaderPageInfo header = new HeaderPageInfo();
		header.readFromBuffer(buffer);
		for(CoupleEntiers c :header.getCouplesEntier())
		{
			if(c.getFreeSlots() > 0)//vérifier qu'il reste des pages libres
			{
				oPageId.setPageIdx(c.getPageIdx());//On remplit la page de libre
				this.bm.free(oPageId, false); //Libération sans modification
				libre = true;
				break;
				
			}
		}
		if(!libre)
		{
			this.dm.addPage(oPageId.getFileIdx(), oPageId);
			this.bm.flushAll();//Actualisation complète ?
			header.writeToBuffer(buffer);
			this.bm.free(oPageId, true);
			
			//byte bufferNouvellePage[] = this.bm.get(oPageId);
			//Utilisation du bytemap... comment faire ?
		}
	}
	
	public void updateHeaderWithTakenSlot(PageId iPageId)
	{
		byte buffer[] = this.bm.get(iPageId);
		HeaderPageInfo header = new HeaderPageInfo();
		header.writeToBuffer(buffer);
		
		/*
		 * Impossible de changer le nombre de slots disponibles (changer CoupleEntiers correspondant à iPageId dans HeaderPageInfo)
		 * --> setter pour le couple de HeaderPageInfo afin de changer le nombre de slots dispos ?
		 * 
		 * this.bm.flushAll(); //Actualisation
		 * this.bm.free
		 */
	}
	
	public RelDef getListe() {
		return listeChainee;
	}

	public void setListe(RelDef liste) {
		this.listeChainee = liste;
	}
}
