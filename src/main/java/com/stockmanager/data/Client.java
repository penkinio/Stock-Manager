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
public class Client extends Tiers {

    private int codeCategorie;

    public Client() {
        super();
    }

    public Client(String nom, String contact, String localisation, String ville, String email, String pays, String boitePostal, String siteWeb) {
        super(nom, contact, localisation, ville, email, pays, boitePostal, siteWeb);
    }

    public int getCodeCategorie() {
        return codeCategorie;
    }

    public void setCodeCategorie(int codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

}
