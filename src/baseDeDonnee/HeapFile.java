package baseDeDonnee;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bufferManager.BufferManager;
import headerPageInfo.CoupleEntiers;
import headerPageInfo.HeaderPageInfo;

public class HeapFile {
	
	private RelDef listeChainee;
	
	private DiskManager dm;
	private BufferManager bm;
	public HeapFile(RelDef listeChainee) {
		// TODO Auto-generated constructor stub
		this.listeChainee = listeChainee;
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
			
			byte bufferNouvellePage[] = this.bm.get(oPageId);
			for(int i = 0; i<this.listeChainee.getSlotCount(); i++)
			{
				bufferNouvellePage[i] = 0;
			}
			this.bm.free(oPageId, true);
		}
	}
	
	public void updateHeaderWithTakenSlot(PageId iPageId) throws IOException
	{
		byte buffer[] = this.bm.get(iPageId);
		HeaderPageInfo header = new HeaderPageInfo();
		header.writeToBuffer(buffer);
		int freeSlots = header.getCoupleEntier(iPageId.getPageIdx()).getFreeSlots();
		header.getCoupleEntier(iPageId.getPageIdx()).setFreeSlots(freeSlots --); //d�cr�menter cases dispo
		header.writeToBuffer(buffer);
		this.bm.flushAll(); //Actualisation
		this.bm.free(iPageId,true);
		 
	}
	
	public RelDef getListe() {
		return listeChainee;
	}

	public void setListe(RelDef liste) {
		this.listeChainee = liste;

