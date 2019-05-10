/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Tiers;

/**
 *
 * @author Maurice
 */
public class Fournisseur extends Tiers {

    public Fournisseur() {
        super();
    }

    public Fournisseur(String nom, String contact, String localisation, String ville, String email, String pays, String boitePostal, String siteWeb) {
        super(nom, contact, localisation, ville, email, pays, boitePostal, siteWeb);
    }
}
