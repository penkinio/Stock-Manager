/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.EtatFacture;
import java.util.Date;

/**
 *
 * @author Maurice
 */
public class Achat extends Audit {

    private int codeFournisseur;
    private int codeProduit;
    private String reference;
    private double prixUnitaireAchat;
    private Integer quantite;
    private Date date;
    private EtatFacture etat;

    public Achat() {
    }

    /**
     *
     * @param codeFournisseur
     * @param codeProduit
     * @param reference
     * @param prixUnitaireAchat
     * @param quantite
     * @param date
     */
    public Achat(int codeFournisseur, int codeProduit, String reference, double prixUnitaireAchat, int quantite, Date date) {
        this.codeFournisseur = codeFournisseur;
        this.codeProduit = codeProduit;
        this.reference = reference;
        this.prixUnitaireAchat = prixUnitaireAchat;
        this.quantite = quantite;
        this.date = date;
    }

    public int getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(int codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public int getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(int codeProduit) {
        this.codeProduit = codeProduit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getPrixUnitaireAchat() {
        return prixUnitaireAchat;
    }

    public void setPrixUnitaireAchat(double prixUnitaireAchat) {
        this.prixUnitaireAchat = prixUnitaireAchat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EtatFacture getEtat() {
        return etat;
    }

    public void setEtat(EtatFacture etat) {
        this.etat = etat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.codeFournisseur;
        hash = 73 * hash + this.codeProduit;
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
        final Achat other = (Achat) obj;
        if (this.codeFournisseur != other.codeFournisseur) {
            return false;
        }
        return this.codeProduit == other.codeProduit;
    }

    @Override
    public String toString() {
        return "Achat{" + "codeFournisseur=" + codeFournisseur + ", codeProduit=" + codeProduit + ", reference=" + reference + ", prixUnitaireAchat=" + prixUnitaireAchat + ", quantite=" + quantite + ", date=" + date + '}';
    }

}
