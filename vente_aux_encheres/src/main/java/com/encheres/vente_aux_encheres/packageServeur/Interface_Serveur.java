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
    /*
    *InterfaceClient en paramètre
    */
    public void inscriptionClient(String pseudo);
    
    /*
    *InterfaceClient en paramètre
    */
    public void sarencherir(int prix);
    
    /*
    *InterfaceClient en paramètre
    */
    public void tempsecoule();
}
