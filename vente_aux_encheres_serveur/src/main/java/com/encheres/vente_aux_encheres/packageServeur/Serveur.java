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
 * @author Rachelle & Naixin & Nina
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{
    
	public static int Nb_client_inscrit = 0;//le nombre de clients inscrit pour un tour de vente
	
	public static int Nb_client_repondu = 0;//le nombre de clients qui a déjà fait une action pendant un tour de vente
	
	public List<Integer> liste_prix = new ArrayList<Integer>(); //list qui prend les prix encherissé par les clients
	
	public static int prix = 10;
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
	        	Nb_client_inscrit = Nb_client_inscrit + 1;
	        	System.out.println("client " + pseudo + " est inscrit");
	        	serveur.notify();
		}
    }

    @Override
    synchronized public void surencherir(int prix) throws RemoteException {
        liste_prix.add(prix);//ajoute le prix proposé par un client dans la liste
        Nb_client_repondu++;
        if(Nb_client_repondu == Nb_client_inscrit){//tous le mondes a répondu commence un nouveau tour
        	serveur.notify();
        	es = Etat_Serveur.vente_terminee;
        	for(Integer i : liste_prix){ //prend le plus grand prix proposé
        		if(i > prix){
        			prix = i;
        		}
        	}
        }
    }

    @Override
    synchronized public void tempsEcoule() {
        serveur.notify();//réveille le serveur qui attend les réponse des clients
        es = Etat_Serveur.vente_terminee;//quand le temps écoule le vente termine
        if(!liste_prix.isEmpty()){//si il y a quelqu'un a encherir on prends le plus grand valeur
        	for(Integer i : liste_prix){ //prend le plus grand prix proposé
        		if(i > prix){
        			prix = i;
        		}
        	}
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
            	System.out.println("nouveau tour");
            	es =Etat_Serveur.en_attente;
            	/*
                 * attend qu'il y a suffisament de clients inscrit pour l'objet
                 */
	            synchronized (serveur) {
	            	if(Nb_client_inscrit < 1){
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
	                Interface_Client client = (Interface_Client)Naming.lookup("//localhost:8080/naixinWANG");  
	                client.nouvelleSoumission("NotreDame", "livre", prix);//appel methode de lient et le donne un objet à vendre
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
