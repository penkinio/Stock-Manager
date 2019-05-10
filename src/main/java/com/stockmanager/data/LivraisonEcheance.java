/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import java.util.Date;

/**
 *
 * @author Maurice
 */
public class LivraisonEcheance extends Audit {

    private int code;
    private String refLivraison;
    private int codeProduit;
    private int quantiteLivre;
    private Date dateLivraison;

    public LivraisonEcheance() {
    }

    public LivraisonEcheance(int codeProduit, int quantiteLivre, Date dateLivraison) {
        this.codeProduit = codeProduit;
        this.quantiteLivre = quantiteLivre;
        this.dateLivraison = dateLivraison;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(int codeProduit) {
        this.codeProduit = codeProduit;
    }

    public int getQuantiteLivre() {
        return quantiteLivre;
    }

    public void setQuantiteLivre(int quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getRefLivraison() {
        return refLivraison;
    }

    public void setRefLivraison(String refLivraison) {
        this.refLivraison = refLivraison;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.code;
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
        final LivraisonEcheance other = (LivraisonEcheance) obj;
        return this.code == other.code;
    }

}
