package baseDeDonnee;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La classe Record correspond aux enregistrements dans une relation
 */
public class Record implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2035518808407956007L;
	
	// La liste des valeurs
	private ArrayList<String> values;

	public Record() {
		this.values = new ArrayList<String>();
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Record [values=" + values + "]";
	}

	/**
	 * Renvoie la valeur d'un record pour une colonne donnée
	 * @param idxCol l'index de la colonne
	 * @return
	 */
	public String getValueForCol(int idxCol) {
		return values.get(idxCol - 1);
	}
}
