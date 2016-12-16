/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres_client.vente_aux_encheres_client;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import com.encheres.vente_aux_encheres.packageServeur.Interface_Serveur;

/**
 *
 * @author Rachelle & Naixin & Nina
 */
public class Client extends UnicastRemoteObject implements Interface_Client {
    
	private Timer timer;
	
    /*
    * Pseudo du client
    */
    private static String pseudo;
    
    /*
     * Pour lire les entrées
     */
    private static Scanner scanner = new Scanner(System.in);
    
    /*
     * Chronometre le tour pour surencherir
     */
    private static Chronometre chrono = new Chronometre();
    
    /*
     * Etat du client : s'il a déjà enchéri ou pas
     */
    private static boolean encheri = false;
    
    /*
    * Constructeur
    */
    public Client(String pseudo) throws RemoteException{
        this.pseudo = pseudo;
    }

    /**
     * @return the pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @param pseudo the pseudo to set
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public void nouvelleSoumission(String nomObjet, String descriptionObjet, int prix) throws RemoteException {
        System.out.println("L'objet à vendre : " + nomObjet + " \ndescription : " + descriptionObjet + "\nPrix est : " + prix);    
    }

    @Override
    public void objetVendu(String nomClient) throws RemoteException {
    	System.out.println("Vente terminée !!! L'objet a été vendu à " + nomClient);
    }

    @Override
    public void majPrix(int nouveauPrix)  throws RemoteException {
    	System.out.println("Prix = " + nouveauPrix);
    	chrono.start();
    	encheri = false;
    }
    
    
    public static void main(String[] args) throws RemoteException{
    	
    	//Demande d'inscription au serveur
        try {
        	System.out.println("Pseudo ? ");
        	String pseudoEntre = scanner.next();
        	Client client = new Client(pseudoEntre);
            LocateRegistry.createRegistry(8080);
			Naming.bind("//localhost:8080/" + pseudo, client);
		} catch (MalformedURLException | AlreadyBoundException e1) {
			e1.printStackTrace();
		}
        
        //Connexion du client au serveur
        try{
            Interface_Serveur serv = (Interface_Serveur)Naming.lookup("//localhost:8090/leServeur");
            serv.inscriptionClient(pseudo);

            //TimeUnit.SECONDS.sleep(5); 
            //fait attendre le client pendant 5 seconde avant de surenchérir, sinon il va changer le prix avant la première recupération
            //si le client appelle la méthode surencherissment il n'appelle plus tempsEcoule

            chrono.start();
            
            //Le client a 10 secondes pour surencherir
            while(true){
            	while(!encheri && chrono.getDuree()<=10) {
            		System.out.println("Encherir ? ");
            		int encherissement = scanner.nextInt();
            		chrono.stop();
            		serv.surencherir(encherissement);
            		encheri = true;
            	}
            	if(chrono.getDuree()>10) {
            		serv.tempsEcoule();
            	}
            }
        }
        catch(Exception e){
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}
