/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

/**
 *
 * @author Maurice
 */
public class CategorieClient {

    private int code;
    private String nom;
    private Double tauxRemise = 0.0d;

    public CategorieClient(String nom, Double taux) {
        this.nom = nom;
        this.tauxRemise = taux;
    }

    public CategorieClient() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getTauxRemise() {
        return tauxRemise;
    }

    public void setTauxRemise(Double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.code;
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
        final CategorieClient other = (CategorieClient) obj;
        return this.code == other.code;
    }

    @Override
    public String toString() {
        return "CategorieClient{" + "code=" + code + ", nom=" + nom + ", tauxRemise=" + tauxRemise + '}';
    }

}
