/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageServeur;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vente_aux_encheres_client.Interface_Client;
/**
 *
 * @author Rachelle & Naixin & Nina
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{

	public static Map<String, String>listeClient = new HashMap<String, String>(); //liste de couple <pseudo de client, son adresse IP>
	
	public static int Nb_client_repondu = 0;//le nombre de clients qui a déjà fait une action pendant un tour de vente
	
	public List<Integer> liste_prix = new ArrayList<Integer>(); //list qui prend les prix encherissé par les clients
	
	public static int prix = 10;//valeur quelconque pour test
	
	private static Etat_Serveur es = Etat_Serveur.en_attente;
	
	public static Serveur serveur;

    public Serveur() throws RemoteException{
        
    }


    @Override
    public void inscriptionClient(String pseudo) throws RemoteException {
    	
    	synchronized (serveur) {
	    		if(es == Etat_Serveur.encherissement ){ //si le serveur est en etat encherissement, les client ne peuvent pas s'inscrire
	    			try {
						serveur.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		try {
					listeClient.put(pseudo, getClientHost());//enregistre le nom du client et son adresse IP dans un map
				} catch (ServerNotActiveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	//Nb_client_inscrit = Nb_client_inscrit + 1;
	        	System.out.println("client " + pseudo + " est inscrit");
	        	serveur.notify();
		}
    }

    @Override
    synchronized public void surencherir(int prix) throws RemoteException {
        liste_prix.add(prix);//ajoute le prix proposé par un client dans la liste
        Nb_client_repondu++;
        if(Nb_client_repondu == listeClient.size()){//tous le mondes a répondu commence un nouveau tour
        	es = Etat_Serveur.vente_terminee;
        	for(Integer i : liste_prix){ //prend le plus grand prix proposé
        		if(i > Serveur.prix){
        			Serveur.prix = i;
        			System.out.println("prix proposé :" +prix);
        		}
        	}
        	serveur.notify();
        }
    }

    @Override
    synchronized public void tempsEcoule() {
        serveur.notify();//réveille le serveur qui attend les réponse des clients
        es = Etat_Serveur.vente_terminee;//quand le temps écoule le vente termine
        if(!liste_prix.isEmpty()){//si il y a quelqu'un a encherir on prends le plus grand valeur
        	for(Integer i : liste_prix){ //prend le plus grand prix proposé
        		if(i > prix){
        			Serveur.prix = i;
        		}
        	}
        }else{
        	System.out.println("Aucun client a enchérit");
        }
        	
    }
    
    public static void main(String[] args){
        try {
            serveur = new Serveur();
            LocateRegistry.createRegistry(8090);
            Naming.bind("//localhost:8090/leServeur",serveur);
            System.out.println("Server ready");
            
            /*
             * attend les inscription
             */
            while(es == Etat_Serveur.en_attente || es == Etat_Serveur.vente_terminee){//quant le serveur est en etat attente ou vente_terminee refait le processus de vente
            	System.out.println("\nnouveau tour");
            	es =Etat_Serveur.en_attente;
            	/*
                 * attend qu'il y a suffisament de clients inscrit pour l'objet
                 */
	            synchronized (serveur) {
	            	if(listeClient.size() < 1){
	            		serveur.wait();
					}
	            	serveur.notify();
	            	es = Etat_Serveur.encherissement;
	            }
	            
	            /*
	             * informer les client des nouvelle soumission
	             * comment on sait les ip des clients
	             */
	            try{
	            	for (Map.Entry<String,String> e : listeClient.entrySet()) {
	            		System.out.println("//"+ e.getValue() +":8080/" + e.getKey());
	            		Interface_Client client = (Interface_Client)Naming.lookup("//"+ e.getValue() +":8080/" + e.getKey());  
	            		client.nouvelleSoumission("NotreDame", "livre", prix);
	            	}
	            	
	                //Interface_Client client = (Interface_Client)Naming.lookup("//localhost:8080/naixinWANG");  
	                //client.nouvelleSoumission("NotreDame", "livre", prix);//appel methode de lient et le donne un objet à vendre
	            }
	            catch(Exception e){
	                System.out.println("Erreur in serveur.java main()");
	                e.printStackTrace();
	            }
	            
	            /*
	             * attente que le serveur reçoit tous les réponse des clients ou temps écoule
	             */
	            synchronized (serveur) {
	            	if(es == Etat_Serveur.encherissement){
	            		serveur.wait();
	            	}
	            	serveur.notify();
	            }

            }//fin while
            
        } catch (Exception e) {
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}
