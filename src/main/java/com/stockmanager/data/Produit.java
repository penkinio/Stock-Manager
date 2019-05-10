/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.EGenreProduit;

/**
 *
 * @author Maurice
 */
public class Produit extends Audit {

    private Integer code;
    private String nom;
    private Double prixUnitaire;
    private Integer quanteStock;
    private Integer codeType;
    private EGenreProduit genre;

    public Produit() {
    }

    /**
     *
     * @param nom
     * @param prixUnitaire
     * @param quanteStock
     * @param codeType
     */
    public Produit(String nom, Double prixUnitaire, Integer quanteStock, Integer codeType) {
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.quanteStock = quanteStock;
        this.codeType = codeType;
        this.genre = EGenreProduit.Commercialisable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuanteStock() {
        return quanteStock;
    }

    public void setQuanteStock(Integer quanteStock) {
        this.quanteStock = quanteStock;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public EGenreProduit getGenre() {
        return genre;
    }

    public void setGenre(EGenreProduit genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        Integer hash = 7;
        hash = 53 * hash + this.code;
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
        final Produit other = (Produit) obj;
        return this.code == other.code;
    }

    @Override
    public String toString() {
        return "Produit{" + "code=" + code + ", nom=" + nom + ", prixUnitaire=" + prixUnitaire + ", quanteStock=" + quanteStock + '}';
    }

}
