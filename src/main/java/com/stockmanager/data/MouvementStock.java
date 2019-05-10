/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.ESensMouvement;
import java.util.Date;

/**
 *
 * @author Maurice
 */
public class MouvementStock extends Audit {

    private int code;
    private String description;
    private Date dateMouvement;
    private int codeProduit;
    private int quantiteProduit;
    private ESensMouvement sens;

    public MouvementStock() {
    }

    /**
     *
     * @param code
     * @param description
     * @param dateMouvement
     * @param codeProduit
     * @param quantiteProduit
     * @param sens
     */
    public MouvementStock(String description, Date dateMouvement, int codeProduit, int quantiteProduit, ESensMouvement sens) {
        this.description = description;
        this.dateMouvement = dateMouvement;
        this.codeProduit = codeProduit;
        this.quantiteProduit = quantiteProduit;
        this.sens = sens;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(Date dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public int getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(int codeProduit) {
        this.codeProduit = codeProduit;
    }

    public int getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(int quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public ESensMouvement getSens() {
        return sens;
    }

    public void setSens(ESensMouvement sens) {
        this.sens = sens;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.code;
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
        final MouvementStock other = (MouvementStock) obj;
        return this.code == other.code;
    }

}
