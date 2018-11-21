package fileManager;

import baseDeDonnee.DiskManager;

public class FileManager {

	private static FileManager INSTANCE = null;

	private FileManager() {
	}

	public static FileManager getInstance() {

		if (INSTANCE == null) 
		{
			INSTANCE = new FileManager();
		}
		return INSTANCE;
	}
	
}
