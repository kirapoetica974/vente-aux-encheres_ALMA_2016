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
	
	private static Interface_Serveur serveurCourant;
	
	private Label titreNomObjet = new Label("Nom de l'objet : ");
	private Label nomObjet = new Label();
	private Label titreDescriptionObjet = new Label("Description de l'objet : ");
	private Label titrePrixDepart = new Label("Prix de départ : ");
	private Label titrePrixSurencheri = new Label("Prix en cours : ");
	private Label descriptionObjet = new Label();
	private Label nouveauPrix = new Label("Nouveau prix : ");
	private Label prix = new Label();
	private Label infoServeur = new Label();
	private Label prixCourant = new Label();

	private TextField prixASurencherir = new TextField(4);
	private TextField pseudo = new TextField("toto");

	private Button boutonSurencherirPrix = new Button("SURENCHERIR");
	private Button boutonCommencer = new Button("GO");
	
	public static Chronometre chrono = new Chronometre();
	
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
				// On arrête de chrono lorsque le client surencheri
				//chrono.stop();
				prixCourant.setText(Integer.toString(serveurCourant.surencherir(prix)));
				// On redémarre le chrono quand il y a un nouveau tour d'enchère
				chrono.stop();
				chrono.run();
			} catch(Exception ex){}
		}
	}
	
	class ActionCommencer implements ActionListener {
		public synchronized void actionPerformed(ActionEvent e) {
			try {
				if (serveurCourant != null && !pseudo.getText().isEmpty()) {
					Client client = null;
					client = new Client(pseudo.getText());
					boutonCommencer.setEnabled(false);
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
		setLayout(new GridLayout(7,2));
		
		add(pseudo);
		add(boutonCommencer);
		
		add(titreNomObjet);
		add(nomObjet);
		
		add(titreDescriptionObjet);
		add(descriptionObjet);

		add(titrePrixDepart);
		add(prix);
		
		add(titrePrixSurencheri);
		add(prixCourant);

		add(nouveauPrix);
		add(prixASurencherir);

		add(boutonSurencherirPrix);
		boutonSurencherirPrix.setEnabled(false);
		add(infoServeur);
		
		
		
		try {
			serveurCourant = (Interface_Serveur)Naming.lookup("//localhost:8090/leServeur");
			System.out.println(" Connexion avec le serveur réussie");
			setInfoServeur("Connexion avec le serveur réussie");
			boutonSurencherirPrix.setEnabled(true);
			boutonCommencer.addActionListener(new ActionCommencer());
			boutonSurencherirPrix.addActionListener(new ActionSurencherirPrix());
			
			// On informe au serveur que le temps est écoulé lorsque le chrono atteint 10 secondes
			System.out.println("avant");
			while(chrono.isTempsEcoule()){
				serveurCourant.tempsEcoule();
				System.out.println("bqfqz");
			}
			
			
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
	
	public void setInfoServeur(String info){
		this.infoServeur.setText(info);
	}
	
	public String getInfoServeur(){
		return infoServeur.getText();
	}
	
	public Interface_Serveur getServeurCourant(){
		return IhmClient.serveurCourant;
	}
}