package baseDeDonnee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import bufferManager.BufferManager;
import fileManager.FileManager;
import util.Constantes;

public class DBManager {

	private DBManager() {

	}

	// Singleton
	private static DBManager INSTANCE = null;
	
	public static DBManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new DBManager();
		}
		return INSTANCE;
		
	}

	
	/**
	 * Initialise le syst�me
	 */
	public void init() {
		DBDef.getInstance().init();
		FileManager.getInstance().init();
	}

	
	/**
	 * Termine le syst�me et sauvegarde les donn�es persistantes
	 */
	public void finish() {
		DBDef.getInstance().finish();
	}

	
	/**
	 * Appelle l'action correspondant � la commande
	 * @param command la commande � effectuer avec les param�tres
	 * @throws NumberFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void processCommand(String command) throws NumberFormatException, FileNotFoundException, IOException {
		
		/*
		 * [0] La commande
		 * [1] Le nom de la relation
		 * [2] Nombre de colonnes
		 * [3,+] Les types des colonnes
		 */
		
		String[] splitCommand = command.split(" ");
		switch (splitCommand[0]) {
		case "create":
			ArrayList<String> types = new ArrayList<String>();
			for (int i = 3; i < splitCommand.length; i++) {
				types.add(splitCommand[i]);
			}
			createRelation(splitCommand[1], Integer.parseInt(splitCommand[2]), types);
			break;
			
		case "insert":
			ArrayList<String> valeurs = new ArrayList<String>();
			for (int i = 2; i < splitCommand.length; i++) {
				valeurs.add(splitCommand[i]);
			}
			insertRelation(splitCommand[1], valeurs);
			break;
			
		case "clean":
			clean();
			break;
			
		case "fill":
			ArrayList<String> lignes = new ArrayList<String>();
			lignes = getLinesFromCSV(splitCommand[2]);
			
			for (String string : lignes) 
			{
				ArrayList<String> values = new ArrayList<String>();
				
				String[] valeursFile = string.split(",");
				
				for (String string2 : valeursFile) 
				{
					values.add(string2);
				}
				
				insertRelation(splitCommand[1], values);
				
			}
			break;
		case "selectall":
			List<Record> records = FileManager.getInstance().getAllRecords(splitCommand[1]);
			for (Record record : records) {
				System.out.println(record);
			}
			System.out.println("Total records : "+records.size());
			break;
		
		case "select":
			List<Record> records2 = FileManager.getInstance().getAllRecordsWithFilter(splitCommand[1], Integer.parseInt(splitCommand[2]), splitCommand[2]);
			for (Record record : records2) {
				System.out.println(record);
			}
			System.out.println("Total records : "+records2.size());
			
		default:
			System.out.println("Commande non reconnue");
		}
	}
	
	
	public ArrayList<String> getLinesFromCSV(String fileName){
		File file = new File(fileName);
		
		ArrayList<String> values = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			char[] res = new char[(int) file.length()];
			
			br.read(res);
			br.close();
			
			String str = new String(res);
			String[] ligne = str.split("\\r?\\n");
			
			for (int i = 0; i < ligne.length; i++) 
			{
				values.add(ligne[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return values;
	}
	
	
	/**
	 * Vide le dossier DB et remet le buffer manager, DBDef et file manager � 0
	 */
	public void clean() {
		
		// On supprime le contenu du sous-r�pertoire DB
		File f = new File("DB");
		for (File c : f.listFiles()) 
		{
			System.out.println("Deleting file " + c.getName());
			c.delete();
		}
		
		// On vide DBDef, Buffer Manager et File Manager
		DBDef.getInstance().reset();
		BufferManager.getInstance().reset();
		FileManager.getInstance().reset();
		
	}
	
	
	/**
	 * Insert une relation
	 * @param nomRelation le nom de la relation
	 * @param valeurs les valeurs du record
	 */
	public void insertRelation(String nomRelation, ArrayList<String> valeurs) {
		
		Record rec = new Record();
		rec.setValues(valeurs);
		FileManager.getInstance().insertRecordInRelation(nomRelation, rec);
	}
	
	
	/**
	 * Cr�e une relation dans DBDef et le Heap File correspondant
	 * @param nomRelation le nom de la relation � cr�er
	 * @param nombreColonne le nombre de colonne de la table
	 * @param typesColonne les types des colonnes
	 * @throws FileNotFoundException si le fichier n'est pas trouv�
	 * @throws IOException
	 */
	public void createRelation(String nomRelation, int nombreColonne, ArrayList<String> typesColonne) throws FileNotFoundException, IOException {
		
		RelDef def = new RelDef(nomRelation, nombreColonne, typesColonne);
		
		int recordSize = 0;
		
		for (String string : typesColonne) {
			if (string.equalsIgnoreCase("int") || string.equalsIgnoreCase("float")) {
				recordSize += 4;
			} else if (string.contains("string")) {
				recordSize += Integer.parseInt(string.substring(6))*2;
			}
		}
		
		def.setRecordSize(recordSize);
		def.setSlotCount(Constantes.pageSize / recordSize);
		def.setFileIdx(DBDef.getInstance().getCompteurRelations());
		
		
		DBDef.getInstance().addRelation(def);
		FileManager.getInstance().createNewHeapFile(def);
	}
	
}
