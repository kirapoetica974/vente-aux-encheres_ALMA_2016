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
/**
 *
 * @author Rachelle
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{
    

    public Serveur() throws RemoteException{
        
    }


    @Override
    public void inscriptionClient(String pseudo) throws RemoteException {
        System.out.println("hello" + pseudo);
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void surencherir(int prix) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tempsEcoule() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args){
        try {
            Serveur serveur = new Serveur();
            //Interface_Serveur stub = (Interface_Serveur)UnicastRemoteObject.exportObject(serveur, 8090);
            LocateRegistry.createRegistry(8090);
            //Registry registry = LocateRegistry.getRegistry();
            //registry.bind("SERV",serveur);
            //Serveur leServeur = new Serveur();
            Naming.bind("//localhost:8090/leServeur",serveur);
            System.out.println("Server ready");

            
        } catch (Exception e) {
            System.out.println("Erreur in serveur.java main()");
            e.printStackTrace();
        }
    }
}
