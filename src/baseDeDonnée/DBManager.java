package baseDeDonnée;

import java.util.ArrayList;

public class DBManager {

	private DBManager(){
		
	}
	
	private static DBManager INSTANCE = null;
	
	public static DBManager getInstance(){
		
		if(INSTANCE == null){
			INSTANCE = new DBManager();
		}
		return INSTANCE; 
		
	}
	
	public void init(){
		DBDef.getInstance().init();
	}
	
	public void finish(){
		DBDef.getInstance().finish();
	}
	
	public void processCommand(String command){
		String[] splitCommand = command.split(" ");
		switch(splitCommand[0]){
		case "create":
			ArrayList<String> types = new ArrayList<String>();
			for(int i = 3; i<splitCommand.length; i++){
				types.add(splitCommand[i]);
			}
			createRelation(splitCommand[1],Integer.parseInt(splitCommand[2]), types);
			break;
		default:
			System.out.println("Commande non reconnue");
		}
	}
	
	public void createRelation(String nomRelation, int nombreColonne, ArrayList<String> typesColonne){
		DBDef.getInstance().addRelation(new RelDef(nomRelation, nombreColonne, typesColonne));
	}
	
}
