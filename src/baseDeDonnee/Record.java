package baseDeDonnee;

import java.util.ArrayList;

public class Record {
	
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

	public String getValueForCol(int idxCol) {
		return values.get(idxCol - 1);
	}
}
