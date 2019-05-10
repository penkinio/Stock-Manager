/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.enumeration.EtatFacture;
import com.stockmanager.data.Achat;
import com.stockmanager.data.Fournisseur;
import com.stockmanager.data.Produit;
import com.stockmanager.service.IServiceAchat;
import com.stockmanager.service.IServiceFournisseur;
import com.stockmanager.service.IServiceProduit;
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
public class ServiceAchatImpl implements IServiceAchat {

    IServiceFournisseur modelFournisseur = new ServiceFournisseurImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();

    public List<Achat> findAchatByCritere(String nomProduit, String nomFournisseur, String reference, Date dateL, Date dateR) {
        List<Achat> listAchat = new ArrayList<Achat>();
        List<Achat> liste1 = new ArrayList<Achat>();
        List<Achat> liste2 = new ArrayList<Achat>();
        List<Achat> liste3 = new ArrayList<Achat>();
        List<Achat> liste4 = new ArrayList<Achat>();
        List<Fournisseur> listFournisseur = modelFournisseur.findFournisseurByName(nomFournisseur);
        List<Produit> listProduit = modelProduit.findProduitByName(nomProduit);

        if (nomProduit.isEmpty() && nomFournisseur.isEmpty() && reference.isEmpty()) {
            return findAllAchat();
        }

        if (nomProduit != null && !nomProduit.isEmpty()) {
            liste1 = getListeAchatByProduits(listProduit);
        }

        if (nomFournisseur != null && !nomFournisseur.isEmpty()) {
            liste2 = getListeAchatByFournisseur(listFournisseur);
        }

        if (reference != null && !reference.isEmpty()) {
            liste3 = getListeAchatByReference(reference);
        }

        if (dateL != null && dateR != null) {
            liste4 = getListeAchatByDate(dateL, dateR);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (Achat achat : liste1) {
                if (!listAchat.contains(achat)) {
                    listAchat.add(achat);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (Achat achat : liste2) {
                if (!listAchat.contains(achat)) {
                    listAchat.add(achat);
                }
            }
        }

        if (liste3 != null && liste3.size() > 0) {
            for (Achat achat : liste3) {
                if (!listAchat.contains(achat)) {
                    listAchat.add(achat);
                }
            }
        }

        if (liste4 != null && liste4.size() > 0) {
            for (Achat achat : liste4) {
                if (!listAchat.contains(achat)) {
                    listAchat.add(achat);
                }
            }
        }

        return listAchat;
    }

    public int saveAchat(Achat achat) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into achat(codeFournisseur,codeProduit, reference,prixUniteAchat,quantite,dateAchat,etat) values (?,?,?,?,?,?,?)");
            pst.setInt(1, achat.getCodeFournisseur());
            pst.setInt(2, achat.getCodeProduit());
            pst.setString(3, achat.getReference());
            pst.setDouble(4, achat.getPrixUnitaireAchat());
            pst.setInt(5, achat.getQuantite());
            pst.setDate(6, new java.sql.Date(achat.getDate().getTime()));
            pst.setInt(7, 0);
            pst.executeUpdate();
            Connexion.getCon().commit();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            result = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public Achat getAchat(String reference) {
        Achat achat = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Achat c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from achat where reference =?");
            pst.setString(1, reference);
            rs = pst.executeQuery();
            if (rs.next()) {
                achat = new Achat();
                achat.setCodeFournisseur(rs.getInt("codeFournisseur"));
                achat.setCodeProduit(rs.getInt("codeProduit"));
                achat.setReference(rs.getString("reference"));
                achat.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                achat.setQuantite(rs.getInt("quantite"));
                achat.setDate(rs.getDate("dateAchat"));
                achat.setEtat(EtatFacture.values()[rs.getInt("etat")]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return achat;
    }

    public int modifierAchat(Integer integer, Integer integer0, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Achat> findAllAchat() {
        List<Achat> listAchat = new ArrayList<Achat>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Achat c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from achat");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Achat();
                c.setCodeFournisseur(rs.getInt("codeFournisseur"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateAchat"));
                c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                listAchat.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listAchat;
    }

    private List<Achat> getListeAchatByProduits(List<Produit> listProduit) {
        List<Achat> listAchat = new ArrayList<Achat>();

        if (listProduit != null && listProduit.size() > 0) {
            for (Produit prod : listProduit) {
                List<Achat> liste = new ArrayList<Achat>();
                PreparedStatement pst = null;
                ResultSet rs = null;
                Achat c;
                try {
                    pst = Connexion.getCon().prepareStatement("select * from achat where codeProduit =? ");
                    pst.setInt(1, prod.getCode());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        c = new Achat();
                        c.setCodeFournisseur(rs.getInt("codeFournisseur"));
                        c.setCodeProduit(rs.getInt("codeProduit"));
                        c.setReference(rs.getString("reference"));
                        c.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                        c.setQuantite(rs.getInt("quantite"));
                        c.setDate(rs.getDate("dateAchat"));
                        c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                        liste.add(c);
                    }
                    listAchat.addAll(liste);
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listAchat;
    }

    private List<Achat> getListeAchatByFournisseur(List<Fournisseur> listFournisseur) {
        List<Achat> listAchat = new ArrayList<Achat>();

        if (listFournisseur != null && listFournisseur.size() > 0) {
            for (Fournisseur client : listFournisseur) {
                List<Achat> liste = new ArrayList<Achat>();
                PreparedStatement pst = null;
                ResultSet rs = null;
                Achat c;
                try {
                    pst = Connexion.getCon().prepareStatement("select * from achat where codeFournisseur =? ");
                    pst.setInt(1, client.getCode());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        c = new Achat();
                        c.setCodeFournisseur(rs.getInt("codeFournisseur"));
                        c.setCodeProduit(rs.getInt("codeProduit"));
                        c.setReference(rs.getString("reference"));
                        c.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                        c.setQuantite(rs.getInt("quantite"));
                        c.setDate(rs.getDate("dateAchat"));
                        c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                        liste.add(c);
                    }
                    listAchat.addAll(liste);
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listAchat;
    }

    private List<Achat> getListeAchatByReference(String reference) {
        List<Achat> listAchat = new ArrayList<Achat>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Achat c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from achat where reference like ? ");
            pst.setString(1, "%" + reference + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Achat();
                c.setCodeFournisseur(rs.getInt("codeFournisseur"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateAchat"));
                c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                listAchat.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listAchat;
    }

    public int updateAchat(Achat achat) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("update achat set codeFournisseur = ?,codeProduit = ?,prixUniteAchat = ?,quantite = ?,dateAchat=? where reference = ?");
            psmt.setInt(1, achat.getCodeFournisseur());
            psmt.setInt(2, achat.getCodeProduit());
            psmt.setDouble(3, achat.getPrixUnitaireAchat());
            psmt.setInt(4, achat.getQuantite());
            psmt.setDate(5, new java.sql.Date(achat.getDate().getTime()));
            psmt.setString(6, achat.getReference());
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

    public int deleteAchat(String ref) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from achat where reference = ?");
            psmt.setString(1, ref);
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

    private List<Achat> getListeAchatByDate(Date dateL, Date dateR) {
        List<Achat> listAchat = new ArrayList<Achat>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Achat c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from achat where dateAchat between ? and ?");
            pst.setDate(1, new java.sql.Date(dateL.getTime()));
            pst.setDate(2, new java.sql.Date(dateR.getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Achat();
                c.setCodeFournisseur(rs.getInt("codeFournisseur"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireAchat(rs.getDouble("prixUniteAchat"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateAchat"));
                c.setEtat(EtatFacture.values()[rs.getInt("etat")]);
                listAchat.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAchatImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listAchat;
    }

}
