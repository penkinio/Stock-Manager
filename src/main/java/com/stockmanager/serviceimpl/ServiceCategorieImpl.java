/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.CategorieClient;
import com.stockmanager.data.CategorieClient;
import com.stockmanager.service.IServiceCategorie;
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
public class ServiceCategorieImpl implements IServiceCategorie {

    public List<CategorieClient> findCategorieByCritere(String nom) {
        List<CategorieClient> listCategorieClient = new ArrayList<CategorieClient>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        CategorieClient c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from categorie where nom like ? ");
            pst.setString(1, nom);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new CategorieClient();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("libelle"));
                c.setTauxRemise(rs.getDouble("localisation"));
                listCategorieClient.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listCategorieClient;
    }

    public List<CategorieClient> findAllCategorie() {
        List<CategorieClient> list = new ArrayList<CategorieClient>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        CategorieClient tp;
        try {
            pst = Connexion.getCon().prepareStatement("select * from categorie");
            rs = pst.executeQuery();
            while (rs.next()) {
                tp = new CategorieClient();
                tp.setCode(rs.getInt("code"));
                tp.setNom(rs.getString("nom"));
                tp.setTauxRemise(rs.getDouble("taux"));
                list.add(tp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public int deleteCategorieByCode(Integer code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from categorie where code = ?");
            psmt.setInt(1, code);
            line = psmt.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

    public CategorieClient findCategorieByCode(int code) {
        CategorieClient c = new CategorieClient();
        if (code > 0) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from categorie where code = ?");
                ps.setInt(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setCode(rs.getInt("code"));
                    c.setNom(rs.getString("nom"));
                    c.setTauxRemise(rs.getDouble("taux"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return c;
    }

    public int saveCategorie(CategorieClient c) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into categorie(nom,taux) values (?,?)");
            pst.setString(1, c.getNom());
            pst.setDouble(2, c.getTauxRemise());
            pst.executeUpdate();
            Connexion.getCon().commit();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            result = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean isExist(String nom) {
        List<CategorieClient> list = new ArrayList<CategorieClient>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        CategorieClient tp;
        try {
            pst = Connexion.getCon().prepareStatement("select * from categorie where nom = ?");
            pst.setString(1, nom);
            rs = pst.executeQuery();

            while (rs.next()) {
                tp = new CategorieClient();
                tp.setCode(rs.getInt("code"));
                list.add(tp);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list.size() > 0;
    }

    public int modifierCategorie(String nom, Double taux, int code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("update categorie set nom = ?,taux = ? where code = ?");
            psmt.setString(1, nom);
            psmt.setDouble(2, taux);
            psmt.setInt(3, code);
            line = psmt.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

    /**
     *
     * @param nomCategorie
     * @return
     */
    public CategorieClient findCategorieByName(String nomCategorie) {
        CategorieClient c = new CategorieClient();
        if (!nomCategorie.isEmpty()) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from categorie where nom = ?");
                ps.setString(1, nomCategorie);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setCode(rs.getInt("code"));
                    c.setNom(rs.getString("nom"));
                    c.setTauxRemise(rs.getDouble("taux"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceCategorieImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return c;
    }

    public Double getTauxByCode(int code) {
        return findCategorieByCode(code).getTauxRemise();
    }

}
