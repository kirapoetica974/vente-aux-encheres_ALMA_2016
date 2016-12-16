package com.encheres_client.vente_aux_encheres_client;

public class Chronometre {

	private long depart = 0;
	private long fin = 0;
	//Durée en seconde
	private long duree = 0;

	public void start() {
		depart=System.currentTimeMillis();
		fin=0;
		duree=0;
	}

	public void stop() {
		if(depart==0) {return;}
		fin = System.currentTimeMillis();
		duree = fin-depart;
		depart = 0;
		fin = 0;
	}        
	
	//Retourne la durée en seconde
	public long getDuree() {
		return duree/1000;
	}
	
}
