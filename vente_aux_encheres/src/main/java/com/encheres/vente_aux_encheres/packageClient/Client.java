/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encheres.vente_aux_encheres.packageClient;

import com.encheres.vente_aux_encheres.packageServeur.Serveur;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Rachelle
 */
public class Client extends UnicastRemoteObject implements Interface_Client {
    
    /*
    * Pseudo du client
    */
    private String pseudo;
    
    
    /*
    * Pointeur vers le client
    */
    private Serveur serveur;
}
