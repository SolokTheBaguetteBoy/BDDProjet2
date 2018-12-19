package baseDeDonnee;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import bufferManager.BufferManager;

public class DBDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5832825649982903389L;
	private ArrayList<RelDef> listeRelations;
	private int compteurRelations;

	/*
	 * Classe Singleton contenant les relations
	 * */
	private DBDef() {
		listeRelations = new ArrayList<RelDef>();
		compteurRelations = listeRelations.size();
	}

	private static DBDef INSTANCE = null;

	
	/**
	 * Getter de l'Instance Unique de DBDef
	 * @return L'instance unique de la DBDef
	 */
	public static DBDef getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DBDef();
		}
		return INSTANCE;
	}

	/**
	 * Fonction d'ajout de relation
	 * @param RD la RelDef a rajouter à la DBDef
	 */
	public void addRelation(RelDef RD) {
		listeRelations.add(RD);
		compteurRelations++;
	}

	/**
	 * Fonction pour accèder à la liste des relations
	 * @return La liste des relations de la DBDef
	 */
	public ArrayList<RelDef> getListeRelations() {
		return listeRelations;
	}

	/**
	 * Fonction permettant d'ajouter tout une liste de relations à la DBDef
	 * @param listeRelations que l'on attribue a la DBDef
	 */
	public void setListeRelations(ArrayList<RelDef> listeRelations) {
		this.listeRelations = listeRelations;
	}

	/**
	 * Getter du compteur de relations
	 * @return int compteurRelations comptant les relations
	 */
	public int getCompteurRelations() {
		return compteurRelations;
	}

	/**
	 * Setter du compteur de relations
	 * @param compteurRelations que l'on attribue
	 */
	public void setCompteurRelations(int compteurRelations) {
		this.compteurRelations = compteurRelations;
	}


	/**
	 * Fonction initialisant la DBDef à partir de Catalog.def. Si le fichier n'existe pas, le crée
	 */
	public void init() {
		
		File f = new File("DB/Catalog.def");
		
		if (f.exists())
		{
			try {
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				
				
				try {
					Object obj = ois.readObject();
					
					if (obj != null)
					{							
						DBDef temp = (DBDef) obj; 
						this.setCompteurRelations(temp.getCompteurRelations());
						this.setListeRelations(temp.getListeRelations());
						//System.out.println("Fichier trouve");
					}
				} catch (EOFException msg) {
					msg.printStackTrace();
				} finally {
					ois.close();
					System.out.println("Initialisation du systeme finie");
				}
			
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
			//System.out.println(compteurRelations);
			/*for (RelDef relDef : listeRelations) {
				System.out.println(relDef.getFileIdx() + " " + relDef.getNomRelation());
			}*/
		} else {
		return;
		}
	}

	@Override
	public String toString() {
		return "DBDef [listeRelations=" + listeRelations + ", compteurRelations=" + compteurRelations + "]";
	}

	/**
	 * Fonction s'utilisant à la fin du fonctionnement de l'application.
	 * Permet d'enregistrer la DBDef dans Catalog.def
	 */
	public void finish() {

		File f = new File("DB/Catalog.def");

		try {

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));

			oos.writeObject(this);

			oos.close();
			BufferManager.getInstance().flushAll();

		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * Remet DBDef a 0
	 */
	public void reset() {
		this.compteurRelations = 0;
		this.listeRelations = new ArrayList<RelDef>();
	}
}
