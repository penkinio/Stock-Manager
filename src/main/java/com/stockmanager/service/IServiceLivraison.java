/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Livraison;
import com.stockmanager.data.LivraisonEcheance;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceLivraison {

    public List<Livraison> findLivraisonByCritere(String facture, String ol, Date dateL, Date dateR);

    public List<Livraison> findAllLivraison();

    public int modifierLivraisonByCode(Integer integer);

    public int saveLivraisonEcheance(LivraisonEcheance livraisonEcheance);

    public int saveLivraison(Livraison livraison); 

    public Livraison getLivraisonByOrder(String string);

    public int deleteLivraisonByCode(String ol);

    public List<LivraisonEcheance> getLivraisonEcheances(String ol);
}
