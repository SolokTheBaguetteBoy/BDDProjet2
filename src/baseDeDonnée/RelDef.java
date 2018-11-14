package baseDeDonnée;

import java.io.Serializable;
import java.util.ArrayList;

public class RelDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2317310855096968870L;
	
	private String nomRelation;
	private int nombreColonne;
	private ArrayList<String> typesColonne; //changer en ArrayList<Type> quand la classe sera crée
	
	private int recordSize;
	private int slotCount; // Nombres cases disponibles
	
	private int fileIdx;
	
	
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

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}
	
	

}
