package baseDeDonnee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void writeRecordInBuffer(Record iRecord, byte[] ioBuffer, int iSlotIdx) throws FileNotFoundException, IOException
	{
		//Retrouver les types de champs du Record
		
		int position  = this.listeChainee.getSlotCount()+(iSlotIdx*iRecord.getValues().size());
		ByteBuffer tempBuffer = ByteBuffer.wrap(ioBuffer);
		ArrayList<String> TypeColonnes = this.listeChainee.getTypesColonne();
		//Étape 1 : récupération des données et écriture en byte depuis ByteBuffer
		for(int i = 0; i < TypeColonnes.size(); i++)
		{
			
			if(!TypeColonnes.get(i).contains("string")) {
				switch(TypeColonnes.get(i))
				{
					case "int":
						tempBuffer.putInt(Integer.valueOf(iRecord.getValues().get(i)));
						break;
					case "float":
						tempBuffer.putFloat(Float.valueOf(iRecord.getValues().get(i)));
						break;
				}

			}
			else //stringx => x = longueur string, la récupérer
			{
				int longueurString= Integer.parseInt(TypeColonnes.get(i).replaceAll("\\D+", ""));
				for(int j = 0; j<longueurString; j++) {
					tempBuffer.putChar(iRecord.getValues().get(i).charAt(j));
				}
			}

		}
		//Écriture dans le buffer
		tempBuffer.position(position);
		ioBuffer = tempBuffer.array(); 
	

	}
	
	/** NARESH
	 * 	Méthode qui prend en argument un Record IRecord et un PageId iPageId et qui retourne un Rid
	 *  @param iRecord de type Record
	 *  @param iPageId de type PageId
	 *  @return un Rid
	 */
	
	public Rid insertRecordInPage(Record iRecord, PageId iPageId)
	{
		byte[] buffer = this.bm.get(iPageId);
		int index = -1;
		int i=0;
		do {
			if(buffer[i]>0)
				index = i;
			i++;
		}while(index==-1);
		Record record = new Record();
		//writeRecordInBuffer (iRecord, buffer, index)
		buffer[index]=1;
		bm.free(iPageId, true);
		return (new Rid(iPageId,index));
	}
	
	public RelDef getRelDef()
	{
		return this.listeChainee;
	}
	
	public Record readRecordFromBuffer(byte[] buffer, int slotIdx) {
		
		ByteBuffer b = ByteBuffer.wrap(buffer);
		Record result = new Record();
		
		return result;
		
	}
}