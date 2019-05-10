package com.stockmanager.serviceimpl;

import com.stockmanager.data.enumeration.RoleType;

/**
 *
 */
public class UserInfo {

    private static String username;
    private static int user_code;
    private static RoleType role;
    private static boolean logged = false;
    private static String nom;
    private static String prenom;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    public static int getUserCode() {
        return user_code;
    }

    public static void setUserCode(int user_code) {
        UserInfo.user_code = user_code;
    }

    public static RoleType getRole() {
        return role;
    }

    public static void setRole(RoleType role_id) {
        UserInfo.role = role_id;
    }

    public static boolean isLogged() {
        return logged;
    }

    public static void setLogged(boolean logged) {
        UserInfo.logged = logged;
    }

    public static int getUser_code() {
        return user_code;
    }

    public static void setUser_code(int user_code) {
        UserInfo.user_code = user_code;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        UserInfo.nom = nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static void setPrenom(String prenom) {
        UserInfo.prenom = prenom;
    }
}
