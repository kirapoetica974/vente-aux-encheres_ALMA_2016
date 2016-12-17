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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.encheres_client.vente_aux_encheres_client.*;

/**
 *
 * @author Rachelle & Naixin & Nina
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{

	public static Map<String, String>listeClient = new HashMap<String, String>(); //liste de couple <pseudo de client, son adresse IP>
	
	public static int nbClientRepondu = 0;//le nombre de clients qui a déjà fait une action pendant un tour de vente
	
	public List<Integer> listePrix = new ArrayList<Integer>(); //liste qui prend les prix encheris par les clients
	
	//Liste des prix correspondants aux clients qui ont surenchéri
	public static Map<String, Integer>listeClientPrix = new HashMap<String, Integer>();
	
	public static Integer prixCourant = 0; //valeur du prix courant de l'objet en vente
	
	//Client gagnant du dernier tour
	public static String clientGagnantCourant = null;
	
	private static Etat_Serveur es = Etat_Serveur.en_attente;
	
	private int nbClientInscrit = 0;
	
	private static List<Objet> listeObjet = new ArrayList<Objet>();
	
	//Pour parcourir le tableau de vente des objets
	private static int indiceTableauObjet = 0;
	
	public static Serveur serveur;

	private static Chronometre chrono = new Chronometre();
	
    public Serveur() throws RemoteException{
        
    }


    @Override
    public void inscriptionClient(String pseudo) throws RemoteException {
    	
    	synchronized (serveur) {
	    		if(es == Etat_Serveur.encherissement ){ //si le serveur est en etat encherissement, les client ne peuvent pas s'inscrire
	    			try {
						serveur.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
	    		try {
	    			listeClient.put(pseudo, getClientHost()); //enregistre le nom du client et son adresse IP dans un map
	    			System.out.println("Le client " + pseudo + " a été enregistré.");
	    		} catch (ServerNotActiveException e) {
					e.printStackTrace();
				}
	        	nbClientInscrit++;
	        	System.out.println("Client " + pseudo + " est inscrit");
	        	serveur.notify();
		}
    }

    @Override
    synchronized public int surencherir(int prix) throws RemoteException {
    	String clientHost = null;
    	try {
    		clientHost = getClientHost();
		} catch (ServerNotActiveException e1) {
			e1.printStackTrace();
		}
    	//Ajoute le prix proposé par un client dans la liste
    	listeClientPrix.put(clientHost, prix);
    	
        //listePrix.add(prix);//ajoute le prix proposé par un client dans la liste
    	System.out.println("Prix proposé :" + prix);
    	
        nbClientRepondu++;
        if(nbClientRepondu == listeClient.size()){//tout le monde a répondu un nouveau tour commence
        	//es = Etat_Serveur.vente_terminee;
        	es = Etat_Serveur.en_attente;
        	
        	Iterator<String> i = listeClientPrix.keySet().iterator();
        	 
        	while (i.hasNext())
        	{
        	    String clef = (String)i.next();
        	    Integer prixPropose = (Integer)listeClientPrix.get(clef);
        	    if(prixPropose > Serveur.prixCourant){
        			Serveur.prixCourant = prixPropose;
        			Serveur.clientGagnantCourant = clef;
        		}
        	
        	}
        	/*for(Integer i : listePrix){ //prend le plus grand prix proposé
        		if(i > Serveur.prix){
        			Serveur.prix = i;
        			System.out.println("prix proposé :" +prix);
        		}
        	}*/
        	serveur.notify();
        }
		return prixCourant;
    }

    @Override
    synchronized public void tempsEcoule() {
        serveur.notify();//réveille le serveur qui attend les réponses des clients
    	
        /*if(!listePrix.isEmpty()){//si quelqu'un a encheri on prend la plus grande valeur
        	for(Integer i : listePrix){ //prend le plus grand prix proposé
        		if(i > prixCourant){
        			Serveur.prixCourant = i;
        		}
        	}
        }*/
        Iterator<String> i = listeClientPrix.keySet().iterator();
        
        if(!listeClientPrix.isEmpty()) {
        	while (i.hasNext())
        	{
        	    String clef = (String)i.next();
        	    Integer prixPropose = (Integer)listeClientPrix.get(clef);
        	    if(prixPropose > Serveur.prixCourant){
        			Serveur.prixCourant = prixPropose;
        			Serveur.clientGagnantCourant = clef;
        		}
        	
        	}
        }
        else{
        	//Vente terminée
        	System.out.println("Aucun client n'a enchéri");
        	es = Etat_Serveur.vente_terminee;
        	
        	//Informer les clients de l'acheteur
        	try{
            	for (Map.Entry<String,String> e : listeClient.entrySet()) {
            		System.out.println("//"+ e.getValue() +":8080/" + e.getKey());
            		Interface_Client client = (Interface_Client)Naming.lookup("//"+ e.getValue() +":8080/" + e.getKey());  
            		client.objetVendu(clientGagnantCourant);
            	}
            }
            catch(Exception e){
                System.out.println("Erreur in serveur.java main()");
                e.printStackTrace();
            }
        	//Reinitialisation
        	clientGagnantCourant = null;
        	prixCourant = null;
        	indiceTableauObjet++;
        }
        	
    }
    
    public static void main(String[] args){
        try {
        	//Creation des objets
        	listeObjet.add(new Objet("Livre",10,"NotreDame"));
        	listeObjet.add(new Objet("Tshirt",15,"Caporal"));
        	
            serveur = new Serveur();
            LocateRegistry.createRegistry(8090);
            Naming.bind("//localhost:8090/leServeur",serveur);
            System.out.println("Server ready");

            //attend les inscriptions
            while(es == Etat_Serveur.en_attente || es == Etat_Serveur.vente_terminee){//quand le serveur est en etat attente ou vente_terminee refait le processus de vente
            	if(indiceTableauObjet == listeObjet.size()) {
            		//Fin des enchères
            		break;
            	}
            	
            	Objet objetCourant = new Objet(listeObjet.get(indiceTableauObjet).getNom(), listeObjet.get(indiceTableauObjet).getPrix(), listeObjet.get(indiceTableauObjet).getDescription());
            	
            	System.out.println("Nouveau tour !");
            	
            	//es = Etat_Serveur.en_attente;
                
            	//attend qu'il y ait suffisament de clients inscrit pour l'objet ou 10 secondes
	            synchronized (serveur) {
	            	chrono.start();
	            	//Attend indéfiniment un client s'il n'y en a pas
	            	if(listeClient.size() < 1 || chrono.getDuree()<=10) {	    		
	            		serveur.wait();
					}
	            	serveur.notify();
	            	es = Etat_Serveur.encherissement;
	            }
	            
	            //Informer les client des nouvelles soumissions
	            try{
	            	for (Map.Entry<String,String> e : listeClient.entrySet()) {
	            		System.out.println("//"+ e.getValue() +":8080/" + e.getKey());
	            		Interface_Client client = (Interface_Client)Naming.lookup("//"+ e.getValue() +":8080/" + e.getKey());  
	            		client.nouvelleSoumission(objetCourant.getNom(), objetCourant.getDescription(), objetCourant.getPrix());
	            	}
	            	
	                //Interface_Client client = (Interface_Client)Naming.lookup("//localhost:8080/naixinWANG");
	            }
	            catch(Exception e){
	                System.out.println("Erreur in serveur.java main()");
	                e.printStackTrace();
	            }
	            
	            //Attente que le serveur reçoit toutes les réponse des clients ou temps écoule
	            synchronized (serveur) {
	            	if(es == Etat_Serveur.encherissement){
	            		serveur.wait();
	            	}
	            	serveur.notify();
	            }

            }
            
        } catch (Exception e) {
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
        
        System.out.println("Plus d'objets à vendre");
    }
}
