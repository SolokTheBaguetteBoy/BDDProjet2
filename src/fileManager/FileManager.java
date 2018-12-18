package fileManager;

import java.io.IOException;
import java.util.ArrayList;

import baseDeDonnee.DBDef;
import baseDeDonnee.HeapFile;
import baseDeDonnee.PageId;
import baseDeDonnee.Record;
import baseDeDonnee.RelDef;
import baseDeDonnee.Rid;
/**
 * Gestion des fichiers pour les records
 *
 */
public class FileManager {

	private static ArrayList<HeapFile> heapFileList;
	
	private static FileManager INSTANCE = null;

	private FileManager() {
		
		heapFileList = new ArrayList<HeapFile>();
		
	}

	public static FileManager getInstance() {

		if (INSTANCE == null) 
		{
			INSTANCE = new FileManager();
		}
		
		return INSTANCE;
	}
	/**
	 * Initialisation du FileManager
	 */
	public void init() {
		for(RelDef rd : DBDef.getInstance().getListeRelations()) {
			heapFileList.add(new HeapFile(rd));
		}
	}
	
	/**
	 * Création d'une HeapFile pour l'insérer ensuite dans la liste des heapfiles
	 * @param rd RelDef pour le nouveau HeapFile
	 * @throws IOException
	 */
	public void createNewHeapFile(RelDef rd) throws IOException {
		//System.err.println("Relations : " + heapFileList);
		HeapFile createdHeapFile = new HeapFile(rd);
		heapFileList.add(createdHeapFile);
		heapFileList.get(heapFileList.size() - 1).createNewOnDisk(rd.getFileIdx());

	}
	
	/**
	 * Insère le record dans une des relations de la HeapFile
	 * @param RelationName -> Nom de la relation
	 * @param Record -> Le record à insérer
	 * @return
	 * @throws IOException
	 */
	
	public Rid insertRecordInRelation(String RelationName, Record Record) throws IOException {
		for(HeapFile hp : heapFileList) {
			if(hp.getRelation().getNomRelation().equals(RelationName)) {
				return hp.insertRecord(Record);
			}
		}
		return new Rid(new PageId(), 0);
	}
	
	
	/**
	 * Remet le File Manager � 0
	 */
	public void reset() {
		this.heapFileList.clear();
	}
	
	/**
	 * Renvoie la liste de tous les records d'une relation
	 * @param RelationName -> le nom de la relation
	 * @return -> la liste de records
	 * @throws IOException
	 */
	public ArrayList<Record> getAllRecords(String RelationName) throws IOException{
		HeapFile hf = getGivenHeapFile(RelationName);
		ArrayList<PageId> listPids = hf.getDataPagesId();
		ArrayList<Record> listRecordToReturn = new ArrayList<Record>();
		for(PageId pid : listPids) {
			listRecordToReturn.addAll(hf.getRecordsOnPage(pid));
		}
		return listRecordToReturn;
	}
	
	/**
	 * Renvoie le HeapFile correspondant à une relation
	 * @param RelationName -> le nom de la relation
	 * @return -> le HeapFile correspondant
	 */
	private HeapFile getGivenHeapFile(String RelationName) {
		for(HeapFile hf : heapFileList) {
			if(hf.getRelDef().getNom().equals(RelationName)){
				return hf;
			}
		}
		return new HeapFile(new RelDef());
	}
	
	/**
	 * Renvoie tous les records selon un paramètre (select)
	 * @param RelationName -> le nom de la relation à afficher
	 * @param idxCol -> la colonne à filtrer
	 * @param valeur -> la valeur à filtrer
	 * @return la liste des records de la relation filtrée
	 * @throws IOException
	 */
	public ArrayList<Record> getAllRecordsWithFilter(String RelationName, int idxCol, String valeur) throws IOException{
		ArrayList<Record> OGlist = getAllRecords(RelationName);
		ArrayList<Record> filteredList = new ArrayList<Record>();
		for(Record r : OGlist) {
			if(r.getValueForCol(idxCol).equals(valeur)) {
				filteredList.add(r);
			}
		}
		return filteredList;
	}
	
	/**
	 * Fonction generant une liste de record a afficher parmis les tables donnees.
	 * Prends les records ou les valeurs des colonnes indiquees pour chaque table est la meme
	 * @param nomRelation1 nom de la premiere table
	 * @param nomRelation2 nom de la deuxieme table
	 * @param indiceCol1 indice de la colonne du join dans la premiere table
	 * @param indiceCol2 indice de la colonne du join dans la deuxieme table
	 * @return Liste des records a affiche
	 * @throws IOException
	 */
	public ArrayList<Record> join(String nomRelation1, String nomRelation2, int indiceCol1, int indiceCol2) throws IOException {
		HeapFile hp1 = getGivenHeapFile(nomRelation1);
		HeapFile hp2 = getGivenHeapFile(nomRelation2);
		ArrayList<PageId> alpid1 = hp1.getDataPagesId();
		ArrayList<PageId> alpid2 = hp2.getDataPagesId();
		ArrayList<Record> alr1 = new ArrayList<Record>();
		ArrayList<Record> alr2 = new ArrayList<Record>();
		ArrayList<Record> toReturn = new ArrayList<Record>();
		
		for(PageId pid : alpid1) {
			alr1.addAll(hp1.getRecordsOnPage(pid));
		}
		for(PageId pid : alpid2) {
			alr2.addAll(hp2.getRecordsOnPage(pid));
		}
		
		for(Record r1 : alr1) {
			for(Record r2 : alr2) {
				if(r1.getValueForCol(indiceCol1).equals(r2.getValueForCol(indiceCol2))) {
					toReturn.add(r1);
					toReturn.add(r2);
				}
			}
		}
		return toReturn;
	}
}
