package baseDeDonnee;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RelDef correspond à une relation
 */
public class RelDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2317310855096968870L;
	
	private String nomRelation;
	
	private int nombreColonne;
	
	private ArrayList<String> typesColonne; //changer en ArrayList<Type> quand la classe sera cr�e
	
	private int recordSize; // Taille d'un enregistrement
	
	private int slotCount; // Nombres cases disponibles
	
	private int fileIdx; // Index du fichier
	
	
	public RelDef(String nomRelation, int nombreColonne, ArrayList<String> typesColonne){
		
		this.nomRelation = nomRelation;
		this.nombreColonne = nombreColonne;
		this.typesColonne = typesColonne;
	}
	
	public RelDef(){
		this.typesColonne = new ArrayList<String>();
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

	@Override
	public String toString() {
		return "RelDef [nomRelation=" + nomRelation + ", nombreColonne=" + nombreColonne + ", typesColonne="
				+ typesColonne + ", recordSize=" + recordSize + ", slotCount=" + slotCount + ", fileIdx=" + fileIdx
				+ "]";
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
	
	public String getNom() {
		return nomRelation;
	}

}
