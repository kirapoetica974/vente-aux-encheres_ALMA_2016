/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface du client
 *
 * @author Rachelle
 */
public interface Interface_Client extends Remote{
    
    /**
     * Méthode qui permet au serveur d'informer le client qu'il y a eu
     * un nouvel objet en vente
     *
     * @param nomObjet le nom/le type d'objet
     * @param descriptionObjet la description de l'objet
     * @param prix le prix initial de l'objet
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public void nouvelleSoumission (String nomObjet, String descriptionObjet, int prix) throws RemoteException;
    
    /**
     * Méthode qui permet au serveur d'informer le client qu'un objet à été vendu
     *
     * @param nomClient le nom du client à qui a été vendu l'objet
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public void objetVendu (String nomClient) throws RemoteException;
    
    /**
     * Méthode qui permet au serveur d'informer le client que le prix a été mis à jour
     *
     * @param nouveauPrix le nouveau prix
     * @throws java.rmi.RemoteException en cas d'erreur
     */
    public void majPrix (int nouveauPrix) throws RemoteException;
    
}
