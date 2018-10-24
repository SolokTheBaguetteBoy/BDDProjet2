package application;

import java.util.Scanner;

import baseDeDonnée.DBManager;

public class Main {

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String commande = "";
		DBManager manager = DBManager.getInstance();
		manager.init();
		while(commande.compareTo("exit") != 0){
			commande = sc.nextLine();
			if(commande.compareTo("exit") != 0){ 
				manager.processCommand(commande);
			}
		}
		manager.finish();
	}
	
}
