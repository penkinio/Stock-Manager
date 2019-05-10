/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.MouvementStock;
import com.stockmanager.data.enumeration.ESensMouvement;
import com.stockmanager.service.IServiceMouvement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maurice
 */
public class ServiceMouvementImpl implements IServiceMouvement {

    public List<MouvementStock> findAllMouvement() {
        List<MouvementStock> listMouvement = new ArrayList<MouvementStock>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        MouvementStock j;
        try {
            pst = Connexion.getCon().prepareStatement("select * from mouvement");
            rs = pst.executeQuery();
            while (rs.next()) {
                j = new MouvementStock();
                j.setCode(rs.getInt("code"));
                j.setDescription(rs.getString("description"));
                j.setCodeProduit(rs.getInt("codeProduit"));
                j.setDateMouvement(rs.getDate("dateMouvement"));
                j.setQuantiteProduit(rs.getInt("quantiteProduit"));
                j.setSens(ESensMouvement.values()[rs.getInt("sens")]);
                listMouvement.add(j);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceMouvementImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceMouvementImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listMouvement;
    }

    public int updateMouvement(MouvementStock mouv) {
        PreparedStatement pst = null;
        int res = 1;
        try {
            pst = Connexion.getCon().prepareStatement("update mouvement set description =?,dateMouvement=?,codeProduit=?,quantiteProduit=?,sens=? where code =?");
            pst.setString(1, mouv.getDescription());
            pst.setDate(2, new java.sql.Date(mouv.getDateMouvement().getTime()));
            pst.setInt(3, mouv.getCodeProduit());
            pst.setInt(4, mouv.getQuantiteProduit());
            pst.setInt(5, mouv.getSens().ordinal());
            pst.setInt(6, mouv.getCode());
            pst.executeUpdate();
            res = 1;
        } catch (SQLException ex) {
            res = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }

    public List<MouvementStock> findMouvementByCritere(String description, Date dateL, Date dateR) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int saveMouvement(MouvementStock u) {
        PreparedStatement pst = null;
        int res = 1;
        try {
            pst = Connexion.getCon().prepareStatement("insert into mouvement (description,dateMouvement,codeProduit,quantiteProduit,sens) values(?,?,?,?,?)");
            pst.setString(1, u.getDescription());
            pst.setDate(2, new java.sql.Date(u.getDateMouvement().getTime()));
            pst.setInt(3, u.getCodeProduit());
            pst.setInt(4, u.getQuantiteProduit());
            pst.setInt(5, u.getSens().ordinal());
            pst.executeUpdate();
            res = 1;
        } catch (SQLException ex) {
            res = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }

    public MouvementStock getMouvementByCode(Integer code) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        MouvementStock mouvement = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from mouvement where code = ?");
            pst.setInt(1, code);
            rs = pst.executeQuery();
            if (rs.next()) {
                mouvement = new MouvementStock();
                mouvement.setCode(rs.getInt("code"));
                mouvement.setDescription(rs.getString("description"));
                mouvement.setCodeProduit(rs.getInt("codeProduit"));
                mouvement.setDateMouvement(rs.getDate("dateMouvement"));
                mouvement.setQuantiteProduit(rs.getInt("quantiteProduit"));
                mouvement.setSens(ESensMouvement.values()[rs.getInt("sens")]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceMouvementImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceMouvementImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return mouvement;
    }

    public int deleteMouvement(Integer code) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from mouvement where code = ?");
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

}
