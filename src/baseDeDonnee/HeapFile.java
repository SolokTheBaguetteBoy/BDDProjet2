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
	}
	
	/**
	 * Écriture du Record / Enregistrement dans le buffer
	 * On convertit chaque valeur en octets sans oublier leur type (int = 4 octets, float = 4 octets, char = 2 octets, etc.)
	 * @param iRecord L'enregistrement
	 * @param ioBuffer La page en forme buffer (récupérer la page à partir du BufferManager ?)
	 * @param iSlotIdx Indice de la case de la page
	 */
	public void writeRecordInBuffer(Record iRecord, byte[] ioBuffer, int iSlotIdx)
	{
		//Retrouver les types de champs du Record
		byte valeursEnregistrements = 0;
		ByteBuffer allocationType = null;
		ArrayList<String> TypeColonnes = this.listeChainee.getTypesColonne();
		for(int i = 0; i < TypeColonnes.size(); i++)
		{
			if(!TypeColonnes.get(i).contains("string")) {
				switch(TypeColonnes.get(i))
				{
					case "int":
						allocationType = ByteBuffer.allocate(4);
						break;
					case "float":
						allocationType = ByteBuffer.allocate(4);
						break;
				}
				for(byte b : allocationType.array())
					valeursEnregistrements += b;
			}
			else //stringx => x = longueur string, la récupérer
			{
				int longueurString= Integer.parseInt(TypeColonnes.get(i).replaceAll("\\D+", ""));
				for(int j = 0; j<longueurString; j++)
				{
					allocationType = ByteBuffer.allocate(iRecord.getValues().get(i).charAt(j)*2);//Récupérer le caractère de la valeur de type string
					for(byte b: allocationType.array())
						valeursEnregistrements += b;
				}
			}

		}
	}
}
