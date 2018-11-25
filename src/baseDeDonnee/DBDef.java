package baseDeDonnee;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DBDef {

	private ArrayList<RelDef> listeRelations;
	private int compteurRelations;

	private DBDef() {
		listeRelations = new ArrayList<RelDef>();
		compteurRelations = listeRelations.size();
	}

	private static DBDef INSTANCE = null;

	public static DBDef getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DBDef();
		}
		return INSTANCE;
	}

	public void addRelation(RelDef RD) {
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

	public void init() {
		
		File f = new File("DB/Catalog.def");
		
		if (f.exists())
		{
			try {
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				
				this.compteurRelations = ois.readInt();
				
				boolean finished = false;
				
				while (!finished)
				{
					try {
						Object obj = ois.readObject();
						
						if (obj != null)
						{
							this.listeRelations.add((RelDef) obj);
						}
						else {
							finished = true;
						}
					} catch (EOFException msg) {
						ois.close();
						System.out.println("Initialisation du systeme finie");
					}
				}
			
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
			
			/*for (RelDef relDef : listeRelations) {
				System.out.println(relDef.getFileIdx() + " " + relDef.getNomRelation());
			}*/
		} else {
			return;
		}
	}

	public void finish() {

		File f = new File("DB/Catalog.def");

		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			
			oos.writeInt(compteurRelations);

			for (RelDef relDef : listeRelations) {

				oos.writeObject(relDef);
			}
			
			oos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Remet DBDef à 0
	 */
	public void reset() {
		this.compteurRelations = 0;
		this.listeRelations = new ArrayList<RelDef>();
	}
}
