/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Client;
import com.stockmanager.service.IServiceCategorie;
import com.stockmanager.service.IServiceClient;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Maurice
 */
public class ServiceClientImpl implements IServiceClient {

    IServiceCategorie modelCategorie = new ServiceCategorieImpl();

    @Override
    public int deleteClientByCode(int code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from client where code = ?");
            psmt.setInt(1, code);
            line = psmt.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

    @Override
    public List<Client> findAllClient() throws SQLException {
        List<Client> listClient = new ArrayList<Client>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Client c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from client");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Client();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setContact(rs.getString("contact"));
                c.setLocalisation(rs.getString("localisation"));
                c.setVille(rs.getString("ville"));
                c.setEmail(rs.getString("email"));
                c.setPays(rs.getString("pays"));
                c.setBoitePostal(rs.getString("boitePostal"));
                c.setSiteWeb(rs.getString("siteWeb"));
                listClient.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listClient;
    }

    @Override
    public Client findClientByCode(int code) throws SQLException {
        Client c = new Client();
        if (code > 0) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from client where code = ?");
                ps.setInt(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setNom(rs.getString("nom"));
                    c.setContact(rs.getString("contact"));
                    c.setLocalisation(rs.getString("localisation"));
                    c.setVille(rs.getString("ville"));
                    c.setEmail(rs.getString("email"));
                    c.setPays(rs.getString("pays"));
                    c.setBoitePostal(rs.getString("boitePostal"));
                    c.setSiteWeb(rs.getString("siteWeb"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                rs.close();
                ps.close();
            }
        }
        return c;
    }

    @Override
    public List<Client> findClientByName(String nom) {
        List<Client> listClient = new ArrayList<Client>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Client c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from client where nom like ?");
            pst.setString(1, "%" + nom + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Client();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setContact(rs.getString("contact"));
                c.setLocalisation(rs.getString("localisation"));
                c.setVille(rs.getString("ville"));
                c.setEmail(rs.getString("email"));
                c.setPays(rs.getString("pays"));
                c.setBoitePostal(rs.getString("boitePostal"));
                c.setSiteWeb(rs.getString("siteWeb"));
                listClient.add(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listClient;
    }

    @Override
    public int modifierClient(String nom, String contact, String ville, String localisation, String email, String pays, String boitePostale, String siteWeb, int code) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("update client set nom =?, contact=?,ville=?,localisation=?,email =?, pays=?,boitePostal=?,siteWeb=? where code = ?");
            pst.setString(1, nom);
            pst.setString(2, contact);
            pst.setString(3, ville);
            pst.setString(4, localisation);
            pst.setString(5, email);
            pst.setString(6, pays);
            pst.setString(7, boitePostale);
            pst.setString(8, siteWeb);
            pst.setInt(9, code);
            pst.executeUpdate();
            Connexion.getCon().commit();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int saveClient(Client c) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into client(nom,contact, ville, localisation,email,pays,boitePostal,siteWeb) values (?,?,?,?,?,?,?,?)");
            pst.setString(1, c.getNom());
            pst.setString(2, c.getContact());
            pst.setString(3, c.getVille());
            pst.setString(4, c.getLocalisation());
            pst.setString(5, c.getEmail());
            pst.setString(6, c.getPays());
            pst.setString(7, c.getBoitePostal());
            pst.setString(8, c.getSiteWeb());
            pst.executeUpdate();
            Connexion.getCon().commit();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            result = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean isExist(String nom) {
        List<Client> listClient = new ArrayList<Client>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Client c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from client where nom = ?");
            pst.setString(1, nom);
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Client();
                c.setCode(rs.getInt("code"));
                listClient.add(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return listClient.size() > 0;
    }

    public List<String> findAllClientName() throws SQLException {
        List<String> liste = new ArrayList<String>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from client");
            rs = pst.executeQuery();
            while (rs.next()) {
                liste.add(rs.getString("nom"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return liste;
    }

    public Client getClientByName(String nom) throws SQLException {
        Client c = new Client();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Connexion.getCon().prepareStatement("select * from client where nom = ?");
            ps.setString(1, nom);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setContact(rs.getString("contact"));
                c.setLocalisation(rs.getString("localisation"));
                c.setVille(rs.getString("ville"));
                c.setEmail(rs.getString("email"));
                c.setPays(rs.getString("pays"));
                c.setBoitePostal(rs.getString("boitePostal"));
                c.setSiteWeb(rs.getString("siteWeb"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            ps.close();
        }

        return c;
    }
}
