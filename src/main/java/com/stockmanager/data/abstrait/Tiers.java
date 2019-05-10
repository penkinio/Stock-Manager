/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data.abstrait;

/**
 *
 * @author Maurice
 */
public abstract class Tiers extends Audit {

    protected int code;
    protected String nom;
    protected String contact;
    protected String localisation;
    protected String ville;
    protected String email;
    protected String pays;
    protected String boitePostal;
    protected String siteWeb;

    public Tiers(String nom, String contact, String localisation, String ville, String email, String pays, String boitePostal, String siteWeb) {
        this.nom = nom;
        this.contact = contact;
        this.localisation = localisation;
        this.ville = ville;
        this.email = email;
        this.pays = pays;
        this.boitePostal = boitePostal;
        this.siteWeb = siteWeb;
    }

    public Tiers() {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getBoitePostal() {
        return boitePostal;
    }

    public void setBoitePostal(String boitePostal) {
        this.boitePostal = boitePostal;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.code;
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
        final Tiers other = (Tiers) obj;
        return this.code == other.code;
    }

    @Override
    public String toString() {
        return "Tiers{" + "code=" + code + ", nom=" + nom + ", contact=" + contact + ", localisation=" + localisation + ", ville=" + ville + ", email=" + email + ", pays=" + pays + ", boitePostal=" + boitePostal + ", siteWeb=" + siteWeb + '}';
    }
}
