/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Produit;
import com.stockmanager.data.enumeration.EGenreProduit;
import com.stockmanager.service.IServiceProduit;
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
public class ServiceProduitImpl implements IServiceProduit {

    @Override
    public int modifierProduitByCode(int code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("update produit set status = ? where code = ?");
            psmt.setInt(1, 1);
            psmt.setInt(2, code);
            line = psmt.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

    @Override
    public List<Produit> findAllProduit() throws SQLException {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                listProduit.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listProduit;
    }

    @Override
    public Produit findProduitByCode(int code) throws SQLException {
        Produit c = new Produit();
        if (code > 0) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from produit where code = ?");
                ps.setInt(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setCode(rs.getInt("code"));
                    c.setNom(rs.getString("nom"));
                    c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    c.setQuanteStock(rs.getInt("quantiteStock"));
                    c.setCodeType(rs.getInt("codeType"));
                    c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                rs.close();
                ps.close();
            }
        }
        return c;
    }

    @Override
    public List<Produit> findProduitByName(String nom) {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit where nom like ?");
            pst.setString(1, "%" + nom + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                listProduit.add(c);
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
        return listProduit;
    }

    @Override
    public int modifierProduit(String nom,int qt, int code,int codeType) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update produit set nom =?,quantiteStock=?,codeType=? where code = ?");
            pst.setString(1, nom);
            pst.setInt(2, qt);
            pst.setInt(3, codeType);
            pst.setInt(4, code);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int saveProduit(Produit c) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into produit(nom,prixUnitaire, quantiteStock,codeType,genre) values (?,?,?,?,?)");
            pst.setString(1, c.getNom());
            pst.setDouble(2, c.getPrixUnitaire());
            pst.setInt(3, c.getQuanteStock());
            pst.setInt(4, c.getCodeType());
            pst.setInt(5, c.getGenre().ordinal());
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

    @Override
    public int addQuantiteProduit(int code, int qt) {

        Produit produit;
        try {
            produit = findProduitByCode(code);

            if (produit != null) {
                produit.setQuanteStock(produit.getQuanteStock() + qt);
                return modifierProduit(code, produit.getQuanteStock());
            } else {
                return 2;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 2;
        }

    }

    @Override
    public int removeQuantiteProduit(int code, int qt) {
        Produit produit;
        try {
            produit = findProduitByCode(code);

            if (produit != null) {
                produit.setQuanteStock(produit.getQuanteStock() - qt);
                return modifierProduit(code, produit.getQuanteStock());
            } else {
                return 2;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 2;
        }

    }

    @Override
    public int modifierProduit(int code, int qt) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update produit set quantiteStock=? where code = ?");
            pst.setInt(1, qt);
            pst.setInt(2, code);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public int deleteProduitByCode(int code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from produit where code = ?");
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

    public int getQuantiteEnSock(int code) {
        int qte = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select quantiteStock from produit where code = ?");
            pst.setInt(1, code);
            rs = pst.executeQuery();

            while (rs.next()) {
                qte = rs.getInt("quantiteStock");
            }
            System.out.println(qte);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return qte;
    }

    public boolean isExist(String nom) {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit where nom = ?");
            pst.setString(1, nom);
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                listProduit.add(c);
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
        return listProduit.size() > 0;
    }

    public List<String> findAllProduitName() throws SQLException {
        List<String> liste = new ArrayList<String>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit");
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

    public Produit getProduitByName(String nom) throws SQLException {
        Produit c = new Produit();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Connexion.getCon().prepareStatement("select * from produit where nom = ?");
            ps.setString(1, nom);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            ps.close();
        }
        return c;
    }

    public int updateParamProduit(int code, double prixUnitaire,EGenreProduit genre) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update produit set prixUnitaire=?,genre=? where code = ?");
            pst.setDouble(1, prixUnitaire);
            pst.setInt(2, genre.ordinal());
            pst.setInt(3, code);
            pst.executeUpdate();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Produit> findProduitByNonCmmercialisable() {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit where genre =?");
            pst.setInt(1,1);
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                listProduit.add(c);
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
        return listProduit;
    }

    /**
     * 
     * @return 
     */
    public List<Produit> findProduitByCmmercialisable() {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from produit where genre =?");
            pst.setInt(1,0);
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                listProduit.add(c);
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
        return listProduit;
    }

    public List<Produit> findProduitByCriteria(String requete) {
        List<Produit> listProduit = new ArrayList<Produit>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Produit c;
        try {
            pst = Connexion.getCon().prepareStatement(requete);
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Produit();
                c.setCode(rs.getInt("code"));
                c.setNom(rs.getString("nom"));
                c.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                c.setQuanteStock(rs.getInt("quantiteStock"));
                c.setCodeType(rs.getInt("codeType"));
                c.setGenre(EGenreProduit.values()[rs.getInt("genre")]);
                listProduit.add(c);
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
        return listProduit;
    }

}
