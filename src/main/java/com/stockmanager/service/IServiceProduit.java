/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Produit;
import com.stockmanager.data.enumeration.EGenreProduit;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceProduit {
     /**
     * methode retournant le nombre de lignes detectés
     *
     * @param code identifiant du client
     * @return le nombre de lignes
     */
    public int modifierProduitByCode(int code);
    
    
    /**
     * methode renvoyant la liste de tous les clients
     *
     * @return la liste de tous les clients
     * @throws java.sql.SQLException
     */
    public List<Produit> findAllProduit() throws SQLException;
    
    
     /**
     * methode renvoyant un client a parti de son identifiant
     *
     * @param code
     * @return un client
     * @throws SQLException
     */
    public Produit findProduitByCode(int code) throws SQLException;
    
    
    /**
     * methode permettant de filter la liste des clients en fonction du nom pris
     * en parametre
     *
     * @param nom : nom des clients
     * @return la liste des clients
     */
    public List<Produit> findProduitByName(String nom);
    
    
     /**
     * methode permettant d'effectuer les modifications sur un client et
     * prennant en parametre le nom l'adresse email le numero de telephone et
     * identifiant du client
     *
     * @param nom
     * @param qt : quantite
     * @param codeType
     * @param code
     * @return 1 si c'est bon et 2 sinon
     */
    public int modifierProduit(String nom, int qt, int code,int codeType);
    
    /**
     * 
     * @param code
     * @param prixUnitaire
     * @param genre
     * @return 
     */
    public int updateParamProduit(int code,double prixUnitaire,EGenreProduit genre);
    
    
     /**
     * methode permettant d'enregistrer un client pris en parametre
     *
     * @param c parametre representant un client
     *
     * @return 1 si tous se passe bien et 2 sinon
     */
    public int saveProduit(Produit c);
    
    /**
     * 
     * @param code
     * @param qt
     * @return 
     */
    public int addQuantiteProduit(int code,int qt);
    
    /**
     * 
     * @param code
     * @param qt
     * @return 
     */
    public int removeQuantiteProduit(int code,int qt);
    
    /**
     * 
     * @param code
     * @param qt
     * @return 
     */
    public int modifierProduit(int code,int qt);
    
    /**
     * 
     * @param code
     * @return 
     */
    public int deleteProduitByCode(int code);
    
    /**
     * 
     * @param code
     * @return 
     */
    public int getQuantiteEnSock(int code);
    
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
    public List<String> findAllProduitName() throws SQLException;
    
    /**
     * 
     * @param nom
     * @return
     * @throws SQLException 
     */
    public Produit getProduitByName(String nom) throws SQLException;
    
    /**
     * 
     * @return 
     */
    public List<Produit> findProduitByNonCmmercialisable();
    
    /**
     * 
     * @return 
     */
    public List<Produit> findProduitByCmmercialisable();

    public List<Produit> findProduitByCriteria(String toString);
    
}
