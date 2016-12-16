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
	TextField pseudo = new TextField("toto");
	Label titreNomObjet = new Label();
	Label nomObjet = new Label();
	Label titreDescriptionObjet = new Label();
	Label descriptionObjet = new Label();
	Label nouveauPrix = new Label();
	Label prix = new Label();

	TextField prixASurencherir = new TextField(4);
	Label prixCourant = new Label("100");

	private static Interface_Serveur serveurCourant;

	Button boutonSurencherirPrix = new Button("SURENCHERIR");
	Button boutonCommencer = new Button("GO");

	class ActionSurencherirPrix implements ActionListener {
		public synchronized void actionPerformed(ActionEvent e) {
			Integer prix;
			try {
				prix = Integer.parseInt(prixASurencherir.getText());
				//prixCourant.setText(serveurCourant.surencherir(prix));
			} catch(Exception ex){};
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

}