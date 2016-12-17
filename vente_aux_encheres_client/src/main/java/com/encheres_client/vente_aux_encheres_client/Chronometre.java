package com.encheres_client.vente_aux_encheres_client;

public class Chronometre extends Thread {

	private boolean tempsEcoule;

	public void run() {
		setTempsEcoule(false);
		
		long start = System.currentTimeMillis();
		//Boucle tant que la durée de vie du thread est < à 20 secondes
		while(System.currentTimeMillis() < (start + (1000 * 20)) || !tempsEcoule) {
			try {
				//Incrémente chaque seconde
				Thread.sleep(1000);
			}
			catch (InterruptedException ex) {}
		}
		setTempsEcoule(true);
	}

	public boolean isTempsEcoule() {
		return tempsEcoule;
	}

	public void setTempsEcoule(boolean tempsEcoule) {
		this.tempsEcoule = tempsEcoule;
	}    
}