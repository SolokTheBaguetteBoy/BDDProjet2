package baseDeDonnée;

import java.io.File;

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
	
	public void createFile(int iFileIdx){
		File f = new File("Data_" + iFileIdx + ".rf");
	}

}
