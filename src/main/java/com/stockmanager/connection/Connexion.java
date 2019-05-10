/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maurice PENKA<mauricepenka@yahoo.fr>
 * utilisation du patern singleton pour creer une classe permettant d'accédé à
 * la base de donnée tout en s'assurant qu'une seule instance de cette classe
 * sera crée
 */
public class Connexion {

    private static Connection con;
    private static Connexion iniqueInstance = null;

    /**
     * constructeur de la classe de connexion
     */
    private Connexion() {
    }

    /**
     * methode permetant de retourner une instance de connection a la base de
     * donnée
     *
     * @return une instance de la connection a la base de donnée
     */
    public static Connection getCon() {
        return con;
    }

    /**
     * methode retournant une instance de la connexion a la base de données
     *
     * @return iniqueInstance
     */
    public static Connexion getIniqueInstance() {
        return iniqueInstance;
    }

    /**
     * methode permettant de créer une instance de connexion et s'assure de
     * l'unicité d'une seule instance de connexion
     *
     * @return une instance de connexion
     */
    public static Connexion instance() {

//        ResourceBundle bundle = ResourceBundle.getBundle("config",Locale.getDefault());
//        String adresse = bundle.getString("IpAdresse");
//        String database = bundle.getString("DataBase");
        if (iniqueInstance == null) {
            iniqueInstance = new Connexion();
            try {
                //chargement du pilote du sgbd sqlite
                Class.forName("com.mysql.jdbc.Driver");
                //creation connexion à la base de donnée
                //String url = "jdbc:mysql://" + adresse + ":3306/" + database;
                String url = "jdbc:mysql://localhost:3306/stockmanager";
                String login = "root";
                String password = "root";
                con = DriverManager.getConnection(url, login, password);
                if (con != null) {
                    System.out.println("success connection");
                }
            } catch (Exception e) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return iniqueInstance;
    }
}
