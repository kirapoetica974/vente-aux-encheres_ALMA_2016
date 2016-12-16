package com.encheres.vente_aux_encheres.packageServeur;

public class Objet {

	private String nom;
	
	private int prix;
	
	private String description;

	public Objet(String nom, int prix, String description) {
		this.nom=nom;
		this.prix=prix;
		this.description=description;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
