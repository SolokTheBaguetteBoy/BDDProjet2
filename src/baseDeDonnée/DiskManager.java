package baseDeDonnée;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import util.Constantes;

public class DiskManager {

	private static DiskManager INSTANCE = null;

	private DiskManager() {
	}

	public DiskManager getInstance() {

		if (INSTANCE == null) 
		{
			INSTANCE = new DiskManager();
		}
		return INSTANCE;
	}
	
	public void createFile(int iFileIdx) {
		File f = new File("DB\\Data_" + iFileIdx + ".rf");
	}
	
	public void addPage(int iFileIdx, int oPageId) throws IOException {
		File f = new File("DB\\Data_" + iFileIdx + ".rf");
		
		FileOutputStream fos = new FileOutputStream(f, true);
		
		fos.write(new byte[Constantes.pageSize]);
		
		oPageId = (int) ((f.length() / Constantes.pageSize) - 1);
		
		fos.close();
	}
	
	public void readPage(PageId iPageId, byte[] oBuffer) {
		
		
		
	}
	
	public void writePage(int iPageId, byte[] iBuffer) {
		
		
		
	}

}
