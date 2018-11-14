package baseDeDonnée;

import java.util.ArrayList;

public class DBDef {

	private ArrayList<RelDef> listeRelations;
	private int compteurRelations;
	
	private DBDef(){
		listeRelations = new ArrayList<RelDef>();
		compteurRelations = listeRelations.size();
	}
	
	private static DBDef INSTANCE = null;
	
	public static DBDef getInstance(){
		if(INSTANCE == null){
			INSTANCE = new DBDef();
		}
		return INSTANCE;
	}
	
	public void addRelation(RelDef RD){
		listeRelations.add(RD);
		compteurRelations++;
	}

	public ArrayList<RelDef> getListeRelations() {
		return listeRelations;
	}

	public void setListeRelations(ArrayList<RelDef> listeRelations) {
		this.listeRelations = listeRelations;
	}

	public int getCompteurRelations() {
		return compteurRelations;
	}

	public void setCompteurRelations(int compteurRelations) {
		this.compteurRelations = compteurRelations;
	}
	
	public void init(){
		
	}
	
	public void finish(){
		
	}
}
