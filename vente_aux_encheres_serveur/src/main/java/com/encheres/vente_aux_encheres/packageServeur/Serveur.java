/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageServeur;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

import vente_aux_encheres_client.Interface_Client;
/**
 *
 * @author Rachelle
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{
    
	public static int Nb_client_inscrit = 0;
	
	public static Serveur serveur;

    public Serveur() throws RemoteException{
        
    }


    @Override
    public void inscriptionClient(String pseudo) throws RemoteException {
    	
    	synchronized (serveur) {
    		if(false){
    			try {
					serveur.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		serveur.notify();
		}
    	Nb_client_inscrit = Nb_client_inscrit + 1;
    	System.out.println("client " + pseudo + " est inscrit");

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int surencherir(int prix) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tempsEcoule() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args){
        try {
            serveur = new Serveur();
            //Interface_Serveur stub = (Interface_Serveur)UnicastRemoteObject.exportObject(serveur, 8090);
            LocateRegistry.createRegistry(8090);
            //Registry registry = LocateRegistry.getRegistry();
            //registry.bind("SERV",serveur);
            //Serveur leServeur = new Serveur();
            Naming.bind("//localhost:8090/leServeur",serveur);
            System.out.println("Server ready");
            
            /*
             * attend les inscription
             */
            while(true){//à rédiger
            	 /*
                 * attend qu'il y a des clients inscrit pour l'objet
                 */
	            synchronized (serveur) {
	            	if(Nb_client_inscrit == 0){
	            		serveur.wait();
					}
	            	serveur.notify();
	            }
	            
	            /*
	             * informer les client des nouvelle soumission
	             * comment on sait les ip des clients
	             */
	            try{
	                Interface_Client client = (Interface_Client)Naming.lookup("//localhost:8080/naixinWANG");  
	                client.nouvelleSoumission("NotreDame", "livre", 12);//appel methode de lient et le donne un objet à vendre
	            }
	            catch(Exception e){
	                System.out.println("Erreur in serveur.java main()");
	                e.printStackTrace();
	            }
            }
            
        } catch (Exception e) {
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}
