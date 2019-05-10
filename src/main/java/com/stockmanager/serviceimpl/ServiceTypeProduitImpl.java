/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.TypeProduit;
import com.stockmanager.service.IServiceTypeProduit;
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
public class ServiceTypeProduitImpl implements IServiceTypeProduit {

    public List<TypeProduit> findTypeProduitByCritere(String lib, String loc) {
        List<TypeProduit> list = new ArrayList<TypeProduit>();
        List<TypeProduit> liste1 = new ArrayList<TypeProduit>();
        List<TypeProduit> liste2 = new ArrayList<TypeProduit>();

        if (lib.isEmpty() && loc.isEmpty()) {
            return findAllTypeProduit();
        }

        if (lib != null && !lib.isEmpty()) {
            liste1 = getListeByLibelle(lib);
        }

        if (loc != null && !loc.isEmpty()) {
            liste2 = getListeByLocalisation(loc);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (TypeProduit tp : liste1) {
                if (!list.contains(tp)) {
                    list.add(tp);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (TypeProduit tp : liste2) {
                if (!list.contains(tp)) {
                    list.add(tp);
                }
            }
        }

        return list;
    }

    public TypeProduit findTypeProduitByCode(int code) {
        TypeProduit c = new TypeProduit();
        if (code > 0) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from typeProduit where code = ?");
                ps.setInt(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setCode(rs.getInt("code"));
                    c.setLibelle(rs.getString("libelle"));
                    c.setLocalisation(rs.getString("localisation"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return c;
    }

    public TypeProduit findTypeProduitByName(String name) {
        TypeProduit c = new TypeProduit();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Connexion.getCon().prepareStatement("select * from typeProduit where libelle = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setCode(rs.getInt("code"));
                c.setLibelle(rs.getString("libelle"));
                c.setLocalisation(rs.getString("localisation"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return c;
    }

    public int saveTypeProduit(TypeProduit c) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into typeproduit(libelle,localisation) values (?,?)");
            pst.setString(1, c.getLibelle());
            pst.setString(2, c.getLocalisation());
            pst.executeUpdate();
            Connexion.getCon().commit();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            result = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean isExist(String lib) {
        List<TypeProduit> list = new ArrayList<TypeProduit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        TypeProduit tp;
        try {
            pst = Connexion.getCon().prepareStatement("select * from typeproduit where libelle = ?");
            pst.setString(1, lib);
            rs = pst.executeQuery();

            while (rs.next()) {
                tp = new TypeProduit();
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

    public int modifierTypeProduit(String lib, String loc, int code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("update typeproduit set libelle = ?,localisation = ? where code = ?");
            psmt.setString(1, lib);
            psmt.setString(2, loc);
            psmt.setInt(3, code);
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

    public int deleteTypeProduitByCode(Integer code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from typeproduit where code = ?");
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

    public List<TypeProduit> findAllTypeProduit() {
        List<TypeProduit> list = new ArrayList<TypeProduit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        TypeProduit tp;
        try {
            pst = Connexion.getCon().prepareStatement("select * from typeproduit");
            rs = pst.executeQuery();
            while (rs.next()) {
                tp = new TypeProduit();
                tp.setCode(rs.getInt("code"));
                tp.setLibelle(rs.getString("libelle"));
                tp.setLocalisation(rs.getString("localisation"));
                list.add(tp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    private List<TypeProduit> getListeByLibelle(String lib) {
        List<TypeProduit> listTypeProduit = new ArrayList<TypeProduit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        TypeProduit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from typeProduit where libelle like ? ");
            pst.setString(1, lib);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new TypeProduit();
                c.setCode(rs.getInt("code"));
                c.setLibelle(rs.getString("libelle"));
                c.setLocalisation(rs.getString("localisation"));
                listTypeProduit.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listTypeProduit;
    }

    private List<TypeProduit> getListeByLocalisation(String loc) {
        List<TypeProduit> listTypeProduit = new ArrayList<TypeProduit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        TypeProduit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from typeProduit where localisation like ? ");
            pst.setString(1, loc);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new TypeProduit();
                c.setCode(rs.getInt("code"));
                c.setLibelle(rs.getString("libelle"));
                c.setLocalisation(rs.getString("localisation"));
                listTypeProduit.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listTypeProduit;
    }

    public TypeProduit getTypeProduitByLibelle(String libelle) {
        TypeProduit c = new TypeProduit();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Connexion.getCon().prepareStatement("select * from typeProduit where libelle = ?");
            ps.setString(1, libelle);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setCode(rs.getInt("code"));
                c.setLibelle(rs.getString("libelle"));
                c.setLocalisation(rs.getString("localisation"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return c;
    }

}
