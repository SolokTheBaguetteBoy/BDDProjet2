package baseDeDonnée;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import util.Constantes;

public class DBManager {

	private DBManager() {

	}

	private static DBManager INSTANCE = null;
	
	public static DBManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new DBManager();
		}
		return INSTANCE;
		
	}

	public void init() {
		DBDef.getInstance().init();
	}

	public void finish() {
		DBDef.getInstance().finish();
	}

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
		default:
			System.out.println("Commande non reconnue");
		}
	}

	public void createRelation(String nomRelation, int nombreColonne, ArrayList<String> typesColonne) throws FileNotFoundException, IOException {
		
		RelDef def = new RelDef(nomRelation, nombreColonne, typesColonne);
		
		int recordSize = 0;
		
		for (String string : typesColonne) {
			if (string.equalsIgnoreCase("int") || string.equalsIgnoreCase("float")) {
				recordSize += 4;
			} else if (string.contains("string")) {
				recordSize += Character.getNumericValue(string.charAt(6))*2;
			}
		}
		
		def.setRecordSize(recordSize);
		def.setSlotCount(Constantes.pageSize / recordSize);
		def.setFileIdx(DBDef.getInstance().getCompteurRelations());
		
		
		DBDef.getInstance().addRelation(def);
	}

}
