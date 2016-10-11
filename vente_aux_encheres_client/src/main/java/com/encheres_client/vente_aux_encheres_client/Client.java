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

import com.encheres.vente_aux_encheres.packageServeur.Interface_Serveur;


/**
 * @author Rachelle & Naixin & Nina
 */

public class Client extends UnicastRemoteObject implements Interface_Client {
    
    /*
    * Pseudo du client
    */
    private static String pseudo;
    
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
    	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void objetVendu(String nomClient) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void majPrix(int nouveauPrix)  throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static void main(String[] args) throws RemoteException{

        try {
        	Client client = new Client("naixinWANG");
            LocateRegistry.createRegistry(8080);
			Naming.bind("//localhost:8080/naixinWANG",client);
		} catch (MalformedURLException | AlreadyBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	
        try{
            Interface_Serveur serv = (Interface_Serveur)Naming.lookup("//localhost:8090/leServeur");
            serv.inscriptionClient(pseudo);

            TimeUnit.SECONDS.sleep(5); 
            //fait attendre le client pendant 5 seconde avant de surenchérir, sinon il va changer le prix avant de la première recupération
            
            
            /*
             * si le client appel la méthode surencherissment il n'appel plus temsEcoule
             */
            //serv.surencherir(20);
            serv.tempsEcoule();
        }
        catch(Exception e){
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}