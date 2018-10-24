package baseDeDonnée;

import java.util.ArrayList;

public class RelDef {

	private String nomRelation;
	private int nombreColonne;
	private ArrayList<String> typesColonne; //changer en ArrayList<Type> quand la classe sera crée
	
	public RelDef(String nomRelation, int nombreColonne, ArrayList<String> typesColonne){
		
		this.nomRelation = nomRelation;
		this.nombreColonne = nombreColonne;
		this.typesColonne = typesColonne;
		
	}

	public String getNomRelation() {
		return nomRelation;
	}

	public void setNomRelation(String nomRelation) {
		this.nomRelation = nomRelation;
	}

	public int getNombreColonne() {
		return nombreColonne;
	}

	public void setNombreColonne(int nombreColonne) {
		this.nombreColonne = nombreColonne;
	}

	public ArrayList<String> getTypesColonne() {
		return typesColonne;
	}

	public void setTypesColonne(ArrayList<String> typesColonne) {
		this.typesColonne = typesColonne;
	}
	
	
	
}
