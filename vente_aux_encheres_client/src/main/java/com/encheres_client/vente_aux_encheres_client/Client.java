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
import java.util.ArrayList;
import java.util.List;

import vente_aux_encheres_serveur.Interface_Serveur;

/**
 * @author Rachelle
 */
public class Client extends UnicastRemoteObject implements Interface_Client {
    
	/*
	 * Liste des objets à mettre aux enchères
	 */
	List<ObjetEnchere> listeObjetsEncheres = new ArrayList<ObjetEnchere>();
	
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
     * @return le pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @param pseudo le pseudo à modifier
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

        /*
         * attend que le serveur soit libre
         */
        //while(){}
    	
        try{
            Interface_Serveur serv = (Interface_Serveur)Naming.lookup("//localhost:8090/leServeur");
            serv.inscriptionClient(pseudo);
        }
        catch(Exception e){
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}
