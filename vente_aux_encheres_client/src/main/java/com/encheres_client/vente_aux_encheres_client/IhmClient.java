/**
 * Classe qui correspond à l'interface graphique du client
 */
package com.encheres_client.vente_aux_encheres_client;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.encheres.vente_aux_encheres.packageServeur.Interface_Serveur;

/**
 * @author Rachelle
 *
 */
public class IhmClient extends Applet{
	
	private static IhmClient instanceIhm;
	
	TextField pseudo = new TextField("toto");
	Label titreNomObjet = new Label("Nom de l'objet : ");
	Label nomObjet = new Label();
	Label titreDescriptionObjet = new Label("Description de l'objet : ");
	Label descriptionObjet = new Label();
	Label nouveauPrix = new Label();
	Label prix = new Label();

	TextField prixASurencherir = new TextField(4);
	Label prixCourant = new Label();

	private static Interface_Serveur serveurCourant;

	Button boutonSurencherirPrix = new Button("SURENCHERIR");
	Button boutonCommencer = new Button("GO");
	
	public IhmClient() {
		instanceIhm = this;
	}

	
	public static IhmClient getInstance(){
	      return instanceIhm;
	}

	class ActionSurencherirPrix implements ActionListener {
		public synchronized void actionPerformed(ActionEvent e) {
			Integer prix;
			try {
				prix = Integer.parseInt(prixASurencherir.getText());
				prixCourant.setText(Integer.toString(serveurCourant.surencherir(prix)));
			} catch(Exception ex){}
		}
	}
	
	class ActionCommencer implements ActionListener {
		public synchronized void actionPerformed(ActionEvent e) {
			try {
				if (serveurCourant != null && !pseudo.getText().isEmpty()) {
					Client client = null;
					client = new Client(pseudo.getText());
		            LocateRegistry.createRegistry(8080);
					Naming.bind("//localhost:8080/" + pseudo.getText(), client);
					serveurCourant.inscriptionClient(pseudo.getText());
				}
			}
			catch (MalformedURLException e2) {
				e2.printStackTrace();
			} catch (AlreadyBoundException e2) {
				e2.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void init(){
		setName("Vente");
		setSize(600, 300);
		setLayout(new GridLayout(6,2));
		add(pseudo);
		add(boutonCommencer);
		
		add(titreNomObjet);
		add(nomObjet);
		
		add(titreDescriptionObjet);
		add(descriptionObjet);

		add(prix);
		add(prixCourant);

		add(nouveauPrix);
		add(prixASurencherir);

		add(boutonSurencherirPrix);
		
		try {
			serveurCourant = (Interface_Serveur)Naming.lookup("//localhost:8090/leServeur");
			System.out.println("Connexion avec le serveur réussie");
			boutonCommencer.addActionListener(new ActionCommencer());
			boutonSurencherirPrix.addActionListener(new ActionSurencherirPrix());
			
			
		} catch(Exception e){System.out.println ("ERREUR LORS DE LA CONNEXION AVEC LE SERVEUR");}
	
		
	}
	
	public String getPseudo(){
		return this.pseudo.getText();
	}
	
	public void setPseudo(String pseudo){
		this.pseudo.setText(pseudo);
	}
	
	public String getNomObjet(){
		return this.nomObjet.getText();
	}
	
	public void setNomObjet(String nom){
		this.nomObjet.setText(nom);
	}

	public String getDescObjet(){
		return this.descriptionObjet.getText();
	}
	
	public void setDescObjet(String desc){
		this.descriptionObjet.setText(desc);
	}
	
	public String getPrixInit(){
		return this.prix.getText();
	}
	
	public void setPrix(String prix){
		this.prix.setText(prix);
	}
	
	public void setPrixCourant(String prix){
		this.prixCourant.setText(prix);
	}
	
	public String getPrixCourant(){
		return this.prixCourant.getText();
	}
}