/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Fournisseur;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceFournisseur {
    
    /**
     * methode retournant le nombre de lignes detectés
     *
     * @param code identifiant du client
     * @return le nombre de lignes
     */
    public int deleteFournisseurByCode(int code);
    
    
    /**
     * methode renvoyant la liste de tous les clients
     *
     * @return la liste de tous les clients
     * @throws java.sql.SQLException
     */
    public List<Fournisseur> findAllFournisseur() throws SQLException;
    
    
     /**
     * methode renvoyant un client a parti de son identifiant
     *
     * @param code
     * @return un client
     * @throws SQLException
     */
    public Fournisseur findFournisseurByCode(int code) throws SQLException;
    
    
    /**
     * methode permettant de filter la liste des clients en fonction du nom pris
     * en parametre
     *
     * @param nom : nom des clients
     * @return la liste des clients
     */
    public List<Fournisseur> findFournisseurByName(String nom);
    
    
     /**
     * methode permettant d'effectuer les modifications sur un client et
     * prennant en parametre le nom l'adresse email le numero de telephone et
     * identifiant du client
     *
     * @param name : nom du client
     * @param contact
     * @param ville
     * @param localisation
     * @param email
     * @param code
     * @param boitePostale
     * @param pays
     * @param siteWeb
     * @return 1 si c'est bon et 2 sinon
     */
    public int modifierFournisseur(String name, String contact,String ville, String localisation, String email, String pays, String boitePostale, String siteWeb, int code);
    
    
     /**
     * methode permettant d'enregistrer un client pris en parametre
     *
     * @param c parametre representant un client
     *
     * @return 1 si tous se passe bien et 2 sinon
     */
    public int saveFournisseur(Fournisseur c);
    
    /**
     * 
     * @param nom
     * @return 
     */
    public boolean isExist(String nom);
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public List<String> findAllFournisseurName() throws SQLException;
    
    /**
     * 
     * @param nom
     * @return
     * @throws SQLException 
     */
    public Fournisseur getFournisseurByName(String nom) throws SQLException;
    
}
