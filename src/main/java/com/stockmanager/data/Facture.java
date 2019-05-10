/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.EtatFacture;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public class Facture extends Audit {

    private int code;
    private String nomClient;
    private String numeroFacture;
    private List<Vente> ventes;
    private Date date;
    private EtatFacture etat;
    private boolean applicTva;

    public Facture() {
        this.etat = EtatFacture.NonRegler;
        this.ventes = new ArrayList<Vente>();
        this.applicTva = false;
    }
    
    

    public Facture(List<Vente> ventes) {
        this.ventes = ventes;
        this.etat = EtatFacture.NonRegler;
        this.applicTva = false;
    }

    public Facture(String nomClient, List<Vente> ventes) {
        this.nomClient = nomClient;
        this.ventes = ventes;
        this.etat = EtatFacture.NonRegler;
        this.applicTva = false;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public List<Vente> getVentes() {
        return ventes;
    }

    public void setVentes(List<Vente> ventes) {
        this.ventes = ventes;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
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

    public boolean isApplicTva() {
        return applicTva;
    }

    public void setApplicTva(boolean applicTva) {
        this.applicTva = applicTva;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.code;
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
        final Facture other = (Facture) obj;
        if (this.code != other.code) {
            return false;
        }
        return true;
    }

}
