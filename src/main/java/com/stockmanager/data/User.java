/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.data;

import com.stockmanager.data.abstrait.Audit;
import com.stockmanager.data.enumeration.EStatusUtilisateur;
import com.stockmanager.data.enumeration.RoleType;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * @author Ngnawen Samuel<sngawen@yahoo.fr>
 * classe stockant les informations sur un utilisateur
 */
public class User extends Audit {
    //Attributs

    private int code;
    private String userName;
    private String passWord;
    private String nom;
    private String prenom;
    private RoleType role;
    private EStatusUtilisateur status;

    /**
     *
     */
    public User() {
    }

    /**
     *
     * @param nom
     * @param prenom
     * @param userName
     * @param passWord
     * @param role
     */
    public User(String nom, String prenom, String userName, String passWord, RoleType role) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.status = EStatusUtilisateur.Actif;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     *
     * @param passWord
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     *
     * @return
     */
    public RoleType getRole() {
        return role;
    }

    /**
     *
     * @param role
     */
    public void setRole(RoleType role) {
        this.role = role;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public EStatusUtilisateur getStatus() {
        return status;
    }

    public void setStatus(EStatusUtilisateur status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.code;
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
        final User other = (User) obj;
        if (this.code != other.code) {
            return false;
        }
        return true;
    }

}
