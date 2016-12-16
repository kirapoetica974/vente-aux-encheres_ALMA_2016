package com.encheres_client.vente_aux_encheres_client;

public class EnchereException extends Exception {

	private String erreur;
	
	public EnchereException(String message) {
		setErreur(message);
	}

	public String getErreur() {
		return erreur;
	}

	public void setErreur(String erreur) {
		this.erreur = erreur;
	}
}

