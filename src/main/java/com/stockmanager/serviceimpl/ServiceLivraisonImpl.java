/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Facture;
import com.stockmanager.data.Livraison;
import com.stockmanager.data.LivraisonEcheance;
import com.stockmanager.data.enumeration.EtatLivraison;
import com.stockmanager.service.IServiceFacture;
import com.stockmanager.service.IServiceLivraison;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Maurice
 */
public class ServiceLivraisonImpl implements IServiceLivraison {

    IServiceFacture modelFacture = new ServiceFactureImpl();

    public List<Livraison> findLivraisonByCritere(String facture, String ol, Date dateL, Date dateR) {
        List<Livraison> listLivraisons = new ArrayList<Livraison>();
        List<Livraison> liste1 = new ArrayList<Livraison>();
        List<Livraison> liste2 = new ArrayList<Livraison>();
        List<Livraison> liste3 = new ArrayList<Livraison>();
        List<Facture> listFacture = modelFacture.findFactureLikeNumero(facture);

        if (facture.isEmpty() && ol.isEmpty()) {
            return findAllLivraison();
        }

        if (facture != null && !facture.isEmpty()) {
            liste1 = getListeLivraisonByFactures(listFacture);
        }

        if (ol != null && !ol.isEmpty()) {
            liste2 = getListeLivraisonByOl(ol);
        }

        if (dateL != null && dateR != null && dateL.before(dateR)) {
            liste3 = getListeLivraisonByDate(dateL, dateR);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (Livraison livraison : liste1) {
                if (!listLivraisons.contains(livraison)) {
                    listLivraisons.add(livraison);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (Livraison livraison : liste2) {
                if (!listLivraisons.contains(livraison)) {
                    listLivraisons.add(livraison);
                }
            }
        }

        if (liste3 != null && liste3.size() > 0) {
            for (Livraison livraison : liste3) {
                if (!listLivraisons.contains(livraison)) {
                    listLivraisons.add(livraison);
                }
            }
        }
        return listLivraisons;
    }

    public List<Livraison> findAllLivraison() {
        List<Livraison> listLivraison = new ArrayList<Livraison>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Livraison c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from livraison");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Livraison();
                c.setCode(rs.getInt("code"));
                c.setOrdreLivraision(rs.getString("ordreLivraison"));
                c.setDescription(rs.getString("description"));
                c.setCodeFacture(rs.getInt("codeFacture"));
                c.setEtat(EtatLivraison.values()[rs.getInt("etat")]);
                c.setDateLivraison(rs.getDate("dateLivraison"));
                listLivraison.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listLivraison;
    }

    public int saveLivraisonEcheance(LivraisonEcheance livraisonEcheance) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into livraisonecheance(code,refLivraison,codeProduit,quantiteLivre,dateLivraison) values (?,?,?,?,?)");
            pst.setInt(1, livraisonEcheance.getCode());
            pst.setString(2, livraisonEcheance.getRefLivraison());
            pst.setInt(3, livraisonEcheance.getCodeProduit());
            pst.setInt(4, livraisonEcheance.getQuantiteLivre());
            pst.setDate(5, new java.sql.Date(livraisonEcheance.getDateLivraison().getTime()));
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

    public int saveLivraison(Livraison livraison) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into livraison(code,ordreLivraison,description,codeFacture,etat,dateLivraison) values (?,?,?,?,?,?)");
            pst.setInt(1, livraison.getCode());
            pst.setString(2, livraison.getOrdreLivraision());
            pst.setString(3, livraison.getDescription());
            pst.setInt(4, livraison.getCodeFacture());
            pst.setInt(5, livraison.getEtat().ordinal());
            pst.setDate(6, new java.sql.Date(livraison.getDateLivraison().getTime()));
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

    private List<Livraison> getListeLivraisonByOl(String ol) {
        List<Livraison> listLivraison = new ArrayList<Livraison>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Livraison c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from livraison where ordreLivraision like ?");
            pst.setString(1, "%" + ol + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Livraison();
                c.setCode(rs.getInt("code"));
                c.setOrdreLivraision(rs.getString("ordreLivraision"));
                c.setDescription(rs.getString("description"));
                c.setCodeFacture(rs.getInt("codeFacture"));
                c.setEtat(EtatLivraison.values()[rs.getInt("etat")]);
                c.setDateLivraison(rs.getDate("dateLivraison"));
                listLivraison.add(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listLivraison;
    }

    private List<Livraison> getListeLivraisonByDate(Date dateL, Date dateR) {
        List<Livraison> listLivraison = new ArrayList<Livraison>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Livraison c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from livraison where dateLivraison between ? and ? ");
            pst.setDate(1, new java.sql.Date(dateL.getTime()));
            pst.setDate(2, new java.sql.Date(dateR.getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Livraison();
                c.setCode(rs.getInt("code"));
                c.setOrdreLivraision(rs.getString("ordreLivraision"));
                c.setDescription(rs.getString("description"));
                c.setCodeFacture(rs.getInt("codeFacture"));
                c.setEtat(EtatLivraison.values()[rs.getInt("etat")]);
                c.setDateLivraison(rs.getDate("dateLivraison"));
                listLivraison.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listLivraison;
    }

    private List<Livraison> getListeLivraisonByFactures(List<Facture> listFacture) {
        List<Livraison> listLivraison = new ArrayList<Livraison>();

        if (listFacture != null && listFacture.size() > 0) {
            for (Facture facture : listFacture) {
                List<Livraison> liste = new ArrayList<Livraison>();
                PreparedStatement pst = null;
                ResultSet rs = null;
                Livraison c;
                try {
                    pst = Connexion.getCon().prepareStatement("select * from livraison where codeFacture =? ");
                    pst.setInt(1, facture.getCode());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        c = new Livraison();
                        c.setCode(rs.getInt("code"));
                        c.setOrdreLivraision(rs.getString("ordreLivraision"));
                        c.setDescription(rs.getString("description"));
                        c.setCodeFacture(rs.getInt("codeFacture"));
                        c.setEtat(EtatLivraison.values()[rs.getInt("etat")]);
                        c.setDateLivraison(rs.getDate("dateLivraison"));
                        liste.add(c);
                    }
                    listLivraison.addAll(liste);
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listLivraison;
    }

    public Livraison getLivraisonByOrder(String ol) {
        Livraison livraison = new Livraison();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from livraison where ordreLivraison = ?");
            pst.setString(1, ol);
            rs = pst.executeQuery();

            while (rs.next()) {
                livraison.setCode(rs.getInt("code"));
                livraison.setOrdreLivraision(rs.getString("ordreLivraison"));
                livraison.setDescription(rs.getString("description"));
                livraison.setCodeFacture(rs.getInt("codeFacture"));
                livraison.setEtat(EtatLivraison.values()[rs.getInt("etat")]);
                livraison.setDateLivraison(rs.getDate("dateLivraison"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return livraison;
    }

    public int modifierLivraisonByCode(Integer code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int deleteLivraisonByCode(String ol) {
        PreparedStatement psmt = null;
        int result = 0;
        int line = 0;
        deleteAllLivraisonEcheance(ol);

        try {
            psmt = Connexion.getCon().prepareStatement("delete from livraison where ordreLivraison = ?");
            psmt.setString(1, ol);
            line = psmt.executeUpdate();
            psmt.close();
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

    private void deleteAllLivraisonEcheance(String ol) {
        List<LivraisonEcheance> livraisonEcheance = getEcheances(ol);

        for (LivraisonEcheance liste : livraisonEcheance) {
            deleteLivraisonEcheance(liste.getCode());
        }
    }

    private List<LivraisonEcheance> getEcheances(String ol) {
        List<LivraisonEcheance> livraisonEcheance = new ArrayList<LivraisonEcheance>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        LivraisonEcheance c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from livraisonecheance where refLivraison = ?");
            pst.setString(1, ol);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new LivraisonEcheance();
                c.setCode(rs.getInt("code"));
                c.setRefLivraison(rs.getString("refLivraison"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setQuantiteLivre(rs.getInt("quantiteLivre"));
                c.setDateLivraison(rs.getDate("dateLivraison"));
                livraisonEcheance.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceLivraisonImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return livraisonEcheance;
    }

    private void deleteLivraisonEcheance(int code) {
        PreparedStatement psmt = null;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from livraisonecheance where code = ?");
            psmt.setInt(1, code);
            psmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceTypeProduitImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<LivraisonEcheance> getLivraisonEcheances(String ol) {
        return getEcheances(ol);
    }
}
