/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Facture;
import com.stockmanager.data.Vente;
import com.stockmanager.data.enumeration.EtatFacture;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceVente;
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
public class ServiceFactureImpl implements IServiceFacture {

    IServiceVente venteModels = new ServiceVenteImpl();

    public List<Facture> findFactureByCritere(String nomfacture, String numFacture) {
        List<Facture> listFactures = new ArrayList<Facture>();
        List<Facture> liste1 = new ArrayList<Facture>();
        List<Facture> liste2 = new ArrayList<Facture>();

        if (nomfacture.isEmpty() && numFacture.isEmpty()) {
            return findAllFacture();
        }

        if (nomfacture != null && !nomfacture.isEmpty()) {
            liste1 = getListeFactureByFacture(nomfacture);
        }

        if (numFacture != null && !numFacture.isEmpty()) {
            liste2 = getListeFactureByNumFacture(numFacture);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (Facture facture : liste1) {
                if (!listFactures.contains(facture)) {
                    listFactures.add(facture);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (Facture facture : liste2) {
                if (!listFactures.contains(facture)) {
                    listFactures.add(facture);
                }
            }
        }
        return listFactures;
    }

    public int saveFacture(Facture facture) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into facture(numeroFacture,nomClient,dateFacture,etat,tva) values (?,?,?,?,?)");
            pst.setString(1, facture.getNumeroFacture());
            pst.setString(2, facture.getNomClient());
            pst.setDate(3, new java.sql.Date(facture.getDate().getTime()));
            pst.setInt(4, facture.getEtat().ordinal());
            pst.setBoolean(5, facture.isApplicTva());
            pst.executeUpdate();
            Connexion.getCon().commit();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            result = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public int modifierFacture(Integer integer, Integer integer0, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Facture> findAllFacture() {
        List<Facture> listFacture = new ArrayList<Facture>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Facture c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from facture");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Facture();
                c.setNomClient(rs.getString("nomClient"));
                c.setCode(rs.getInt("code"));
                c.setNumeroFacture(rs.getString("numeroFacture"));
                c.setDate(rs.getDate("dateFacture"));
                c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                c.setApplicTva(rs.getBoolean("tva"));
                listFacture.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listFacture;
    }

    public double getMontantFacture(String numFactute) {
        double montant = 0.0;
        List<Vente> listes = venteModels.getListeVenteByFacture(numFactute);
        if (listes == null || listes.isEmpty()) {
            return montant;
        }

        for (Vente vente : listes) {
            montant += vente.getPrixUnitaireVente() * vente.getQuantite();
        }
        return montant;
    }

    public int modifierFacture(String numFacture) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Facture> getListeFactureByFacture(String facture) {
        List<Facture> listFacture = new ArrayList<Facture>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Facture c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from facture where nomClient like ? ");
            pst.setString(1, "%" + facture + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Facture();
                c.setNomClient(rs.getString("nomClient"));
                c.setCode(rs.getInt("code"));
                c.setNumeroFacture(rs.getString("numeroFacture"));
                c.setDate(rs.getDate("dateFacture"));
                c.setApplicTva(rs.getBoolean("tva"));
                listFacture.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listFacture;
    }

    private List<Facture> getListeFactureByNumFacture(String numFacture) {
        List<Facture> listFacture = new ArrayList<Facture>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Facture c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from facture where numeroFacture like ? ");
            pst.setString(1, "%" + numFacture + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Facture();
                c.setNomClient(rs.getString("nomClient"));
                c.setCode(rs.getInt("code"));
                c.setNumeroFacture(rs.getString("numeroFacture"));
                c.setDate(rs.getDate("dateFacture"));
                c.setApplicTva(rs.getBoolean("tva"));
                listFacture.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listFacture;
    }

    public Facture findFactureByCode(int code) {
        Facture facture = new Facture();
        ResultSet rs = null;
        PreparedStatement ps = null;
        if (code > 0) {

            try {
                ps = Connexion.getCon().prepareStatement("select * from facture where code = ?");
                ps.setInt(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    facture.setCode(code);
                    facture.setNumeroFacture(rs.getString("numeroFacture"));
                    facture.setNomClient(rs.getString("nomClient"));
                    facture.setDate(rs.getDate("dateFacture"));
                    facture.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                    facture.setApplicTva(rs.getBoolean("tva"));
                }
                facture.setVentes(venteModels.getListeVenteByFacture(facture.getNumeroFacture()));
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return facture;
    }

    public int updateFacture(Facture facture) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("update facture set etat = ?,tva = ? where code = ?");
            pst.setInt(1, facture.getEtat().ordinal());
            pst.setBoolean(2, facture.isApplicTva());
            pst.setInt(3, facture.getCode());
            pst.executeUpdate();
            Connexion.getCon().commit();
            result = 1;
        } catch (SQLException ex) {
            result = 2;
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public Facture findFactureByNumero(String refFacture) {
        Facture facture = new Facture();
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = Connexion.getCon().prepareStatement("select * from facture where numeroFacture = ?");
            ps.setString(1, refFacture);
            rs = ps.executeQuery();
            if (rs.next()) {
                facture.setCode(rs.getInt("code"));
                facture.setNumeroFacture(rs.getString("numeroFacture"));
                facture.setNomClient(rs.getString("nomClient"));
                facture.setDate(rs.getDate("dateFacture"));
                facture.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                facture.setApplicTva(rs.getBoolean("tva"));
            }
            facture.setVentes(venteModels.getListeVenteByFacture(facture.getNumeroFacture()));
        } catch (SQLException ex) {
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return facture;
    }

    public List<Facture> findFactureLikeNumero(String facture) {
        List<Facture> listFacture = new ArrayList<Facture>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Facture c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from facture where numeroFacture like ?");
            pst.setString(1, "%" + facture + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Facture();
                c.setCode(rs.getInt("code"));
                c.setNomClient(rs.getString("nomClient"));
                c.setCode(rs.getInt("code"));
                c.setNumeroFacture(rs.getString("numeroFacture"));
                c.setDate(rs.getDate("dateFacture"));
                c.setApplicTva(rs.getBoolean("tva"));
                listFacture.add(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listFacture;
    }

    public int deleteFacture(String numFacture) {
        PreparedStatement psmt = null;

        for (Vente vente : venteModels.getListeVenteByFacture(numFacture)) {
            venteModels.deleteVente(vente.getFacture());
        }

        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from facture where numeroFacture = ?");
            psmt.setString(1, numFacture);
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

    public Iterable<Facture> findAllFactureRegler() {
        List<Facture> listFacture = new ArrayList<Facture>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Facture c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from facture where etat = 2");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Facture();
                c.setNomClient(rs.getString("nomClient"));
                c.setCode(rs.getInt("code"));
                c.setNumeroFacture(rs.getString("numeroFacture"));
                c.setDate(rs.getDate("dateFacture"));
                c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                c.setApplicTva(rs.getBoolean("tva"));
                listFacture.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceFactureImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listFacture;
    }

}
