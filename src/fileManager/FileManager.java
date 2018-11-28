package fileManager;

import java.io.IOException;
import java.util.ArrayList;

import baseDeDonnee.DBDef;
import baseDeDonnee.HeapFile;
import baseDeDonnee.PageId;
import baseDeDonnee.Record;
import baseDeDonnee.RelDef;
import baseDeDonnee.Rid;

public class FileManager {

	private ArrayList<HeapFile> heapFileList;
	
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
	
	public void init() {
		for(RelDef rd : DBDef.getInstance().getListeRelations()) {
			heapFileList.add(new HeapFile(rd));
		}
	}
	
	public void createNewHeapFile(RelDef rd) throws IOException {
		HeapFile createdHeapFile = new HeapFile(rd);
		heapFileList.add(createdHeapFile);
		heapFileList.get(heapFileList.size() - 1).createNewOnDisk(rd.getFileIdx());
	}
	
	public Rid insertRecordInRelation(String RelationName, Record Record) {
		for(HeapFile hp : heapFileList) {
			if(hp.getListe().getNomRelation().equals(RelationName)) {
				//return hp.insertRecord();
			}
		}
		return new Rid(new PageId(), 0);
	}
	
	
	/**
	 * Remet le File Manager à 0
	 */
	public void reset() {
		this.heapFileList.clear();
	}
	
	public ArrayList<Record> getAllRecords(String RelationName){
		HeapFile hf = getGivenHeapFile(RelationName);
		ArrayList<PageId> listPids = hf.getDataPagesId();
		ArrayList<Record> listRecordToReturn = new ArrayList<Record>();
		for(PageId pid : listPids) {
			listRecordToReturn.addAll(hf.getRecordsOnPage(pid));
		}
		return listRecordToReturn;
	}
	
	private HeapFile getGivenHeapFile(String RelationName) {
		for(HeapFile hf : heapFileList) {
			if(hf.getRelDef().getNom().equals(RelationName)){
				return hf;
			}
		}
		return new HeapFile(new RelDef());
	}
	
	public ArrayList<Record> getAllRecordsWithFilter(String RelationName, int idxCol, String valeur){
		ArrayList<Record> OGlist = getAllRecords(RelationName);
		ArrayList<Record> filteredList = new ArrayList<Record>();
		for(Record r : OGlist) {
			if(r.getValueForCol(idxCol).equals(valeur)) {
				filteredList.add(r);
			}
		}
		return fitleredList;
	}
}
