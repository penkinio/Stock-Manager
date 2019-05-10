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
public class Parameter extends Audit {

    private String nomSociete;
    private String adressIpServeur;

    public Parameter(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getAdressIpServeur() {
        return adressIpServeur;
    }

    public void setAdressIpServeur(String adressIpServeur) {
        this.adressIpServeur = adressIpServeur;
    }

}
