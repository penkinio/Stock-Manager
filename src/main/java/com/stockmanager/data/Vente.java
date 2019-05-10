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
public class Vente extends Audit {

    private int codeClient;
    private int codeProduit;
    private String reference;
    private double prixUnitaireVente;
    private int quantite;
    private Date date;
    private String facture;
    private double tauxRemise;

    public Vente() {
    }

    /**
     *
     * @param codeClient
     * @param codeProduit
     * @param reference
     * @param prixUnitaireVente
     * @param quantite
     * @param date
     */
    public Vente(int codeClient, int codeProduit, String reference, double prixUnitaireVente, int quantite, Date date) {
        this.codeClient = codeClient;
        this.codeProduit = codeProduit;
        this.reference = reference;
        this.prixUnitaireVente = prixUnitaireVente;
        this.quantite = quantite;
        this.date = date;
        this.tauxRemise = 0.0;
    }

    /**
     *
     * @param codeClient
     * @param codeProduit
     * @param reference
     * @param prixUnitaireVente
     * @param quantite
     * @param date
     * @param numFacture
     */
    public Vente(int codeClient, int codeProduit, String reference, double prixUnitaireVente, int quantite, Date date, String numFacture) {
        this.codeClient = codeClient;
        this.codeProduit = codeProduit;
        this.reference = reference;
        this.prixUnitaireVente = prixUnitaireVente;
        this.quantite = quantite;
        this.date = date;
        this.facture = numFacture;
        this.tauxRemise = 0.0;
    }

    public int getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(int codeClient) {
        this.codeClient = codeClient;
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

    public double getPrixUnitaireVente() {
        return prixUnitaireVente;
    }

    public void setPrixUnitaireVente(double prixUnitaireVente) {
        this.prixUnitaireVente = prixUnitaireVente;
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

    public String getFacture() {
        return facture;
    }

    public void setFacture(String facture) {
        this.facture = facture;
    }

    public double getTauxRemise() {
        return tauxRemise;
    }

    public void setTauxRemise(double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.codeClient;
        hash = 59 * hash + this.codeProduit;
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
        final Vente other = (Vente) obj;
        if (this.codeClient != other.codeClient) {
            return false;
        }
        return this.codeProduit == other.codeProduit;
    }

    @Override
    public String toString() {
        return "Vente{" + "codeClient=" + codeClient + ", codeProduit=" + codeProduit + ", reference=" + reference + ", prixUnitaireVente=" + prixUnitaireVente + ", quantite=" + quantite + ", date=" + date + '}';
    }

}
