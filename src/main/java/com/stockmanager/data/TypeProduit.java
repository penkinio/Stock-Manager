/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;

/**
 *
 * @author Maurice
 */
public class TypeProduit extends Audit {

    private int code;
    private String libelle;
    private String localisation;

    public TypeProduit() {
    }

    public TypeProduit(String libelle, String localisation) {
        this.libelle = libelle;
        this.localisation = localisation;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.code;
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
        final TypeProduit other = (TypeProduit) obj;
        return this.code == other.code;
    }

}
