/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageServeur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface du serveur
 *
 * @author Rachelle
 */
public interface Interface_Serveur extends Remote{
    
    /**
     * Méthode qui permet au client de se connecter sur le serveur
     *
     * @param pseudo le pseudo du client qui veut se connecter
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public void inscriptionClient(String pseudo) throws RemoteException;
    
    /**
     * Méthode qui permet au client d'enchérir
     *
     * @param prix le prix à enchérir
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public int surencherir(int prix)throws RemoteException;
    
    /**
     * Métode qui permet au client de savoir que le temps de la vente est écoulée
     *
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public void tempsEcoule()throws RemoteException;
}
