package baseDeDonnee;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.Constantes;

public class DiskManager {

	private static DiskManager INSTANCE = null;
	
	/**
	 * Constructeur vide de DBManager
	 */
	private DiskManager() {
	}
	
	/**
	 * Getter de l'Instance Unique du DiskManager
	 * @return L'instance unique de DiskManager
	 */
	public static DiskManager getInstance() {

		if (INSTANCE == null) 
		{
			INSTANCE = new DiskManager();
		}
		return INSTANCE;
	}
	
	/**
	 * Creer un fichier avec l'index renseigne
	 * @param iFileIdx index du fichier cree
	 */
	public void createFile(int iFileIdx) {
		File f = new File("DB/Data_" + iFileIdx + ".rf");
		
	}
	
	
	/**
	 * Rajoute une page au DiskManager
	 * @param iFileIdx index du fichier rajoute
	 * @param oPageId PageId de la page
	 * @throws IOException
	 */
	public void addPage(int iFileIdx, PageId oPageId) throws IOException {
		File f = new File("DB/Data_" + iFileIdx + ".rf");
		
		FileOutputStream fos = new FileOutputStream(f, true);
		
		fos.write(new byte[Constantes.pageSize]);
		
		oPageId.setPageIdx((int) ((f.length() / Constantes.pageSize) - 1));
		oPageId.setFileIdx(iFileIdx);
		
		fos.close();
	}
	
	/**
	 * Fonction permettant la lecture d'une page
	 * @param iPageId de la page lue
	 * @param oBuffer buffer ou l'on va lire la page
	 * @throws IOException
	 */
	public void readPage(PageId iPageId, byte[] oBuffer) throws IOException {
//		
//		File f = new File("DB/Data_" + iPageId.getFileIdx() + ".rf");
//		
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
//		
//		bis.read(oBuffer, iPageId.getPageIdx() * Constantes.pageSize, Constantes.pageSize);
//		
//		bis.close();
		String name = "DB/Data_" + iPageId.getFileIdx() + ".rf";
		RandomAccessFile rand = new RandomAccessFile(name, "r");
		rand.seek(iPageId.getPageIdx()*Constantes.pageSize);
		rand.readFully(oBuffer);
		rand.close();
		
	}
	
	/**
	 * Fonction permettant d'ecrire sur une page
	 * @param iPageId de la page sur laquelle on ecrit
	 * @param iBuffer qu'on va ecrire dans la page
	 * @throws IOException
	 */
	public void writePage(PageId iPageId, byte[] iBuffer) throws IOException {
		
		String name = "DB/Data_" + iPageId.getFileIdx() + ".rf";
		RandomAccessFile rand = new RandomAccessFile(name, "rw");
		rand.seek(iPageId.getPageIdx() * Constantes.pageSize);
		rand.write(iBuffer);
		rand.close();
//		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
//		
//		bos.write(iBuffer,, iBuffer.length);
//		
//		bos.close();
		
	}
	
	/*public static void main(String[] args) throws IOException {
		
		DiskManager dm = DiskManager.getInstance();
		byte[] array = new byte[Constantes.pageSize];
		PageId pid = new PageId();
		dm.createFile(3);
		dm.addPage(3, pid);
		dm.writePage(pid, new byte[]{2,3,4});
		dm.readPage(pid, array);
		
		for (byte b : array) {
			System.out.println(b);
		}
		
	}*/

}
