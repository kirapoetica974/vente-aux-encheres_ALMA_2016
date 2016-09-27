/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageServeur;

import java.rmi.Remote;

/**
 *
 * @author Rachelle
 */
public interface Interface_Serveur extends Remote{
    
    /**
     * Méthode qui permet au client de se connecter sur le serveur
     *
     * @param pseudo
     */
    public void inscriptionClient(String pseudo);
    
    /**
     * Méthode qui permet au client d'enchérir
     *
     * @param prix
     */
    public void surencherir(int prix);
    
    /**
     * Métode qui permet au cleint de savoir que le temps de la vente est écoulée
     *
     */
    public void tempsEcoule();
}
