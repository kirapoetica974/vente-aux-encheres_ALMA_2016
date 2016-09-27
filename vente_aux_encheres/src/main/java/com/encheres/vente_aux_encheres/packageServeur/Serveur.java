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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Rachelle
 */
public class Serveur extends UnicastRemoteObject implements Interface_Serveur{
    

    public Serveur() throws RemoteException{
        
    }


    @Override
    public void inscriptionClient(String pseudo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void surencherir(int prix) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tempsEcoule() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args){
        try {
            LocateRegistry.createRegistry(8090);
            Serveur leServeur = new Serveur();
            Naming.bind("//blabla:8090/leServeur", leServeur);
        } catch (Exception e) {
            System.out.println("Erreur in serveur.java main()");
        }
    }
}
