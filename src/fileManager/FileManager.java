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
			if(hp.getRelDef().getNomRelation().equals(RelationName)) {
				//return hp.insertRecord();
			}
		}
		return new Rid(new PageId(), 0);
	}
}
