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
import util.Constantes;
/**
 * 
 * Décrit l'organisation d'une page mémoire
 *
 */
public class HeapFile {

	private RelDef relation;

	/**
	 * Constructeur
	 * @param relation
	 */
	public HeapFile(RelDef relation) {
		// TODO Auto-generated constructor stub
		this.relation = relation;
	}
	
	/**
	 * Crée une nouvelle HeaderPage correspondant à l'id du file sur le disque
	 * @param iFileIdx -> l'id du file
	 * @throws IOException
	 */
	public void createNewOnDisk(int iFileIdx) throws IOException
	{
		HeaderPageInfo header = new HeaderPageInfo();
		PageId pid = new PageId(0,iFileIdx);
		DiskManager.getInstance().createFile(iFileIdx);
		DiskManager.getInstance().addPage(iFileIdx, pid);
		//BufferManager.getInstance().get(pid);
		header.writeToBuffer(BufferManager.getInstance().get(pid));
		//System.out.println("pid = " + pid.getPageIdx());
		BufferManager.getInstance().free(pid, true);	
	}
	
	/**
	 * Recherche une page de disponible (qui possède au moins un slot de libre)
	 * @param oPageId -> la page trouvée
	 * @throws IOException
	 */
	public void getFreePageId(PageId oPageId) throws IOException
	{	
		PageId header = new PageId(0,relation.getFileIdx());
		/**
		 * Chercher une PageId qui vaut 0 dans le headerPage, la prendre puis l'insérer dans l
		 */
		//System.out.println(oPageId);
		byte buffer[] = BufferManager.getInstance().get(header);
		HeaderPageInfo headerPageInfo = new HeaderPageInfo();
		headerPageInfo.readFromBuffer(buffer);

		for(CoupleEntiers c :headerPageInfo.getCouplesEntier())
		{
			//System.err.println("FREE SLOTS C " + c.getFreeSlots());
			if(c.getFreeSlots() > 0)//vérifier qu'il reste des cases libres
			{
				
				//oPageId.setPageIdx(c.getPageIdx());//On remplit la page de libre
				//System.out.println("FIN 1  getFreePageId " + getClass());
				oPageId.setFileIdx(this.relation.getFileIdx());
				oPageId.setPageIdx(c.getPageIdx());
				BufferManager.getInstance().free(header, false);
				return; //arrêt complet de la méthodetrue

			}
		}	
			//System.err.println("  add page");
			DiskManager.getInstance().addPage(this.relation.getFileIdx(), oPageId);
//			System.out.println("nouvele page" + oPageId);
//			System.out.println("apres appel nouvele page");
//			System.out.println("apres appel frame1 : " + BufferManager.getInstance().getFrame1());
//			System.out.println("apres appel frame2 : " + BufferManager.getInstance().getFrame2());
//			System.out.println("Taille couple : " + headerPageInfo.getCouplesEntier().size());
			headerPageInfo.incrementer();
			headerPageInfo.addToHeaderPageInfo(oPageId.getPageIdx(), this.relation.getSlotCount());
			//updateHeaderWithTakenSlot(oPageId);
			headerPageInfo.writeToBuffer(buffer);
			//System.out.println("Buffer dans getFreePageId : " + Arrays.toString(buffer));
			BufferManager.getInstance().free(header, true);

			byte bufferNouvellePage[] = BufferManager.getInstance().get(oPageId);
			for(int i = 0; i<this.relation.getSlotCount(); i++)
				bufferNouvellePage[i] = 0;
			
			//System.out.println("FIN 2  getFreePageId " + getClass());
			//headerPageInfo.writeToBuffer(buffer);
			
			BufferManager.getInstance().free(oPageId, true);
			
			
	}
	
	/**
	 * Actualise l'HeaderPage après occupation d'un des slots par une page
	 * @param iPageId L'id de la page qui occupe le slot du header
	 * @throws IOException
	 */
	public void updateHeaderWithTakenSlot(PageId iPageId) throws IOException
	{
		PageId header = new PageId(0,relation.getFileIdx());
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
	
	/**
	 * Getter de la relation de la HeapFile
	 * @return retourne la relation 
	 */
	public RelDef getRelation() {
		return relation;
	}
	
	/**
	 * Setter de la relation de la HeapFile
	 * @param liste
	 */
	public void setListe(RelDef relation) {
		this.relation = relation;
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
//		System.err.println("Début writeRecordInBuffer " + iSlotIdx);
//		System.out.println("Avant écriture : " + Arrays.toString(ioBuffer));
		int position  = this.relation.getSlotCount()+(iSlotIdx*this.relation.getRecordSize());
//		System.out.println("Position écriture : " + position);
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
//		System.out.println("Après écriture " + Arrays.toString(ioBuffer));
//		System.err.println("Fin writeRecordInBuffer");

	}

	/** 
	 * 	Détermine l'identifiant du Record à insérer
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
//		System.err.println("Buffer InsertRecordInPage" +Arrays.toString(buffer));
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
	
	/**
	 * Renvoie l'identifiant du Record à insérer dans la page après avoir déterminé la page où écrire
	 * @param iRecord -> Record à insérer
	 * @return L'identifiant du Record
	 * @throws IOException
	 */
	public Rid insertRecord(Record iRecord) throws IOException
	{
		PageId tempPage =  new PageId();
		getFreePageId(tempPage);
//		System.out.println("Temppage : " + tempPage);
		return this.insertRecordInPage(iRecord,tempPage);
		
	}
	
	/**
	 * Retourne la relation de la HeapFile
	 * @return
	 */
	public RelDef getRelDef()
	{
		return this.relation;
	}
	
	/**
	 * Lecture du record depuis le buffer
	 * @param buffer -> Buffer fourni
	 * @param slotIdx -> slot nécessaire à déterminer la position depuis la bytemap
	 * @return
	 */
	public Record readRecordFromBuffer(byte[] buffer, int slotIdx) {
		//System.out.println("SlotIdx recordFromBuffer : " + slotIdx);
		int position = this.relation.getSlotCount()+(slotIdx*relation.getRecordSize());
		
//		System.out.println("Position lecture : " + position);
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

	/**
	 * Fournit tous les records de la page
	 * @param pid PageId
	 * @return Liste des records
	 * @throws IOException
	 */
	public List<Record> getRecordsOnPage(PageId pid) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("getRecordsOnPage : "+ pid);
		List<Record> listeRecords = new ArrayList<>();
		byte buffer[] = BufferManager.getInstance().get(pid);
		boolean zeros = true;
		for(byte b : buffer)
		{
			if(b != 0)
			{
				zeros = false;
				break;
			}
		}
		if(zeros)
			DiskManager.getInstance().readPage(pid, buffer);
		//byte buffer[] = new byte[Constantes.pageSize];
		
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

	/**
	 * Fournit toutes les pages appartenant au HeapFile
	 * @return la liste des pages
	 * @throws IOException
	 */
	public ArrayList<PageId> getDataPagesId() throws IOException {
		// TODO Auto-generated method stub
		ArrayList<PageId> pages = new ArrayList<>();
		PageId headerPage = new PageId(0,this.relation.getFileIdx());
		byte buffer[] = BufferManager.getInstance().get(headerPage);
		boolean zeros = true;
		for(byte b : buffer)
		{
			if(b != 0)
			{
				zeros = false;
				break;
			}
		}
		if(zeros)
			DiskManager.getInstance().readPage(headerPage, buffer);
		HeaderPageInfo hp = new HeaderPageInfo();
		hp.readFromBuffer(buffer);
		for(CoupleEntiers c : hp.getCouplesEntier())
			pages.add(new PageId(c.getPageIdx(),this.relation.getFileIdx()));
		BufferManager.getInstance().free(headerPage, false);
		return pages;
	}

}