/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.EtatLivraison;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maurice
 */
public class Livraison extends Audit {

    private int code;
    private String OrdreLivraision;
    private String description;
    private int codeFacture;
    private EtatLivraison etat;
    private Date dateLivraison;
    private Map<String, Produit> mapProduitVente;
    private List<LivraisonEcheance> livraisonEcheances;

    public Livraison() {
        etat = EtatLivraison.NonLivrer;
        mapProduitVente = new HashMap<String, Produit>();
        livraisonEcheances = new ArrayList<LivraisonEcheance>();
    }

    public Livraison(String OrdreLivraision, String description, int codeFacture, EtatLivraison etat, Date dateLivraison) {
        this.OrdreLivraision = OrdreLivraision;
        this.description = description;
        this.codeFacture = codeFacture;
        this.etat = etat;
        this.dateLivraison = dateLivraison;
        mapProduitVente = new HashMap<String, Produit>();
        livraisonEcheances = new ArrayList<LivraisonEcheance>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getOrdreLivraision() {
        return OrdreLivraision;
    }

    public void setOrdreLivraision(String OrdreLivraision) {
        this.OrdreLivraision = OrdreLivraision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCodeFacture() {
        return codeFacture;
    }

    public void setCodeFacture(int codeFacture) {
        this.codeFacture = codeFacture;
    }

    public EtatLivraison getEtat() {
        return etat;
    }

    public void setEtat(EtatLivraison etat) {
        this.etat = etat;
    }

    public Map<String, Produit> getMapProduitVente() {
        return mapProduitVente;
    }

    public void setMapProduitVente(Map<String, Produit> mapProduitVente) {
        this.mapProduitVente = mapProduitVente;
    }

    public List<LivraisonEcheance> getLivraisonEcheances() {
        return livraisonEcheances;
    }

    public void setLivraisonEcheances(List<LivraisonEcheance> livraisonEcheances) {
        this.livraisonEcheances = livraisonEcheances;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.code;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Livraison other = (Livraison) obj;
        return this.code == other.code;
    }

}
