package baseDeDonnee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bufferManager.BufferManager;
import headerPageInfo.CoupleEntiers;
import headerPageInfo.HeaderPageInfo;

public class HeapFile {

	private RelDef relation;


	public HeapFile(RelDef relation) {
		// TODO Auto-generated constructor stub
		this.relation = relation;
	}

	public void createNewOnDisk(int iFileIdx) throws IOException
	{
		HeaderPageInfo header = new HeaderPageInfo();
		PageId pid = new PageId();
		DiskManager.getInstance().createFile(iFileIdx);
		DiskManager.getInstance().addPage(iFileIdx, pid);
		BufferManager.getInstance().get(pid);
		header.writeToBuffer(BufferManager.getInstance().get(pid));
		System.out.println("pid = " + pid.getPageIdx());
		BufferManager.getInstance().free(pid, true);	
	}

	public void getFreePageId(PageId oPageId) throws IOException
	{	
		PageId header = new PageId(relation.getFileIdx(),0);
		/**
		 * Chercher une PageId qui vaut 0 dans le headerPage, la prendre puis l'insérer dans l
		 */
		System.out.println(oPageId);
		byte buffer[] = BufferManager.getInstance().get(header);
//		System.out.println("Debut  getFBufferManagerreePageId " + getClass());
//		System.out.println("avant appel headerPage");
//		System.out.println("avant appel frame1 : " + BufferManager.getInstance().getFrame1());
//		System.out.println("avant appel frame2 : " + BufferManager.getInstance().getFrame2());
		HeaderPageInfo headerPageInfo = new HeaderPageInfo();
		headerPageInfo.readFromBuffer(buffer);
		//System.out.println("apres appel headerPage");
//		System.out.println("apres appel frame1 : " + BufferManager.getInstance().getFrame1());
//		System.out.println("apres appel frame2 : " + BufferManager.getInstance().getFrame2());
//		//for(CoupleEntiers c :headerPageInfo.getCouplesEntier())
//		System.out.println("apres appel headerPage");
//		System.out.println("apres appel frame1 : " + BufferManager.getInstance().getFrame1());
//		System.out.println("apres appel frame2 : " + BufferManager.getInstance().getFrame2());
//		System.out.println("Taille couple : " + headerPageInfo.getCouplesEntier().size());
		for(CoupleEntiers c :headerPageInfo.getCouplesEntier())
		{
			if(c.getFreeSlots() > 0)//vérifier qu'il reste des cases libres
			{
				//oPageId.setPageIdx(c.getPageIdx());//On remplit la page de libre
				System.out.println("FIN 1  getFreePageId " + getClass());
				oPageId.setFileIdx(this.relation.getFileIdx());
				oPageId.setPageIdx(c.getPageIdx());
				BufferManager.getInstance().free(oPageId, false);
				return; //arrêt complet de la méthodetrue

			}
		}	
			DiskManager.getInstance().addPage(oPageId.getFileIdx(), oPageId);
//			System.out.println("nouvele page" + oPageId);
//			System.out.println("apres appel nouvele page");
//			System.out.println("apres appel frame1 : " + BufferManager.getInstance().getFrame1());
//			System.out.println("apres appel frame2 : " + BufferManager.getInstance().getFrame2());
//			System.out.println("Taille couple : " + headerPageInfo.getCouplesEntier().size());
			headerPageInfo.incrementer();
			headerPageInfo.addToHeaderPageInfo(oPageId.getPageIdx(), this.relation.getSlotCount());
			//updateHeaderWithTakenSlot(oPageId);
			headerPageInfo.writeToBuffer(buffer);
			BufferManager.getInstance().free(header, true);

			byte bufferNouvellePage[] = BufferManager.getInstance().get(oPageId);
			for(int i = 0; i<this.relation.getSlotCount(); i++)
				bufferNouvellePage[i] = 0;
			
			
			headerPageInfo.writeToBuffer(buffer);
			BufferManager.getInstance().free(oPageId, true);
			
			System.out.println("FIN 2  getFreePageId " + getClass());
	}

	public void updateHeaderWithTakenSlot(PageId iPageId) throws IOException
	{
		PageId header = new PageId(relation.getFileIdx(),0);
		byte buffer[] = BufferManager.getInstance().get(header);
		HeaderPageInfo headerPageInfo = new HeaderPageInfo();
		headerPageInfo.readFromBuffer(buffer);
		for(int i = 0; i<headerPageInfo.getCouplesEntier().size();i++)
		{
			if(headerPageInfo.getCouplesEntier().get(i).getPageIdx() == iPageId.getPageIdx())
			{
				headerPageInfo.getCoupleEntier(i).setFreeSlots(headerPageInfo.getCoupleEntier(i).getFreeSlots() - 1);
			}
		}
	
		headerPageInfo.writeToBuffer(buffer);
		BufferManager.getInstance().free(header,true);

	}

	public RelDef getListe() {
		return relation;
	}

	public void setListe(RelDef liste) {
		this.relation = liste;
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
		System.err.println("Début writeRecordInBuffer " + iSlotIdx);
		System.out.println("Avant écriture : " + Arrays.toString(ioBuffer));
		int position  = this.relation.getSlotCount()+(iSlotIdx*this.relation.getRecordSize());
		System.out.println("Position écriture : " + position);
		ByteBuffer tempBuffer = ByteBuffer.wrap(ioBuffer);
		tempBuffer.position(position);
		ArrayList<String> TypeColonnes = this.relation.getTypesColonne();
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
		
		ioBuffer = tempBuffer.array(); 
		System.out.println("Après écriture " + Arrays.toString(ioBuffer));
		System.err.println("Fin writeRecordInBuffer");

	}

	/** NARESH
	 * 	Méthode qui prend en argument un Record IRecord et un PageId iPageId et qui retourne un Rid
	 *  @param iRecord de type Record
	 *  @param iPageId de type PageId
	 *  @return un Rid
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */

	public Rid insertRecordInPage(Record iRecord, PageId iPageId) throws FileNotFoundException, IOException
	{
		byte[] buffer = BufferManager.getInstance().get(iPageId);
		//System.out.println(buffer.length);
		int index = -1;
		
		for(int j = 0; this.relation.getSlotCount()>j ; j++) {
			//System.out.println("Index : " + index);
			if(buffer[j]==0) { 
				index = j;
				break;
			}
		}
		writeRecordInBuffer (iRecord, buffer, index);
		buffer[index]=1;
		BufferManager.getInstance().free(iPageId, true);
		updateHeaderWithTakenSlot(iPageId);
		return (new Rid(iPageId,index));
	}
	
	public Rid insertRecord(Record iRecord) throws IOException
	{
		PageId tempPage =  new PageId();
		getFreePageId(tempPage);
		System.out.println("Temppage : " + tempPage);
		return this.insertRecordInPage(iRecord,tempPage);
		
	}

	public RelDef getRelDef()
	{
		return this.relation;
	}

	public Record readRecordFromBuffer(byte[] buffer, int slotIdx) {
		//System.out.println("SlotIdx recordFromBuffer : " + slotIdx);
		int position = this.relation.getSlotCount()+(slotIdx*relation.getRecordSize());
		
		System.out.println("Position lecture : " + position);
		ByteBuffer b = ByteBuffer.wrap(buffer);
		b.position(position);
		ArrayList<String> record = new ArrayList<>();
		StringBuffer tempString; //String temporaire pour le for

		Record result = new Record();
		for(String relation_temp : relation.getTypesColonne())
		{
			tempString = new StringBuffer("");
			//System.out.println("for : " + relation_temp);
			if(relation_temp.contains("string")) {
				String[] s = relation_temp.split("string");
				for(int j = 0; j<Integer.parseInt(s[1]); j++)
					tempString.append(b.getChar());
			}
			else {
				switch(relation_temp) {
					
					case "int":
						tempString.append(b.getInt());
						break;
					case "float":
						tempString.append(b.getFloat());
						break;
				}		
			}
			record.add(tempString.toString());
		}
		result.setValues(record);
		return result;
	}


	public List<Record> getRecordsOnPage(PageId pid) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("getRecordsOnPage : "+ pid);
		List<Record> listeRecords = new ArrayList<>();
		byte buffer[] = BufferManager.getInstance().get(pid);
		for (int i = 0; i < this.relation.getSlotCount(); i++) {
			if(buffer[i] == 1)//on vérifie si le slot n'est pas 0 (si le slot n'est pas vide)
			{
				listeRecords.add(this.readRecordFromBuffer(buffer, i));
			}
		}
		BufferManager.getInstance().free(pid, false);
		return listeRecords;
	}
	
	@Override
	public String toString() {
		return "HeapFile [relation=" + relation + "]";
	}

	public ArrayList<PageId> getDataPagesId() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<PageId> pages = new ArrayList<>();
		PageId headerPage = new PageId(this.relation.getFileIdx(), 0);
		byte buffer[] = BufferManager.getInstance().get(headerPage);
		HeaderPageInfo hp = new HeaderPageInfo();
		hp.readFromBuffer(buffer);
		for(CoupleEntiers c : hp.getCouplesEntier())
			pages.add(new PageId(c.getPageIdx(),this.relation.getFileIdx()));
		BufferManager.getInstance().free(headerPage, false);
		return pages;
	}

}