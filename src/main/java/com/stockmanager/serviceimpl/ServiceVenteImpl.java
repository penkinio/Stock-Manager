/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Client;
import com.stockmanager.data.Produit;
import com.stockmanager.data.Vente;
import com.stockmanager.service.IServiceClient;
import com.stockmanager.service.IServiceProduit;
import com.stockmanager.service.IServiceVente;
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
public class ServiceVenteImpl implements IServiceVente {

    IServiceClient modelClient = new ServiceClientImpl();
    IServiceProduit modelProduit = new ServiceProduitImpl();

    public Vente getVente(int codeVente, int codeClient, String reference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int saveVente(Vente vente) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into vente(codeClient,codeProduit, reference,prixUniteVente,quantite,dateVente,refFacture,tauxRemise) values (?,?,?,?,?,?,?,?)");
            pst.setInt(1, vente.getCodeClient());
            pst.setInt(2, vente.getCodeProduit());
            pst.setString(3, vente.getReference());
            pst.setDouble(4, vente.getPrixUnitaireVente());
            pst.setInt(5, vente.getQuantite());
            pst.setDate(6, new java.sql.Date(vente.getDate().getTime()));
            pst.setString(7, vente.getFacture());
            pst.setDouble(8, vente.getTauxRemise());
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

    public int modifierVente(Integer integer, Integer integer0, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Vente> findVenteByCritere(String nomProduit, String nomClient, String reference, Date dateL, Date dateR) {
        List<Vente> listVente = new ArrayList<Vente>();
        List<Vente> liste1 = new ArrayList<Vente>();
        List<Vente> liste2 = new ArrayList<Vente>();
        List<Vente> liste3 = new ArrayList<Vente>();
        List<Vente> liste4 = new ArrayList<Vente>();
        List<Client> listClient = modelClient.findClientByName(nomClient);
        List<Produit> listProduit = modelProduit.findProduitByName(nomProduit);

        if (nomProduit.isEmpty() && nomClient.isEmpty() && reference.isEmpty()) {
            return findAllVente();
        }

        if (nomProduit != null && !nomProduit.isEmpty()) {
            liste1 = getListeVenteByProduits(listProduit);
        }

        if (nomClient != null && !nomClient.isEmpty()) {
            liste2 = getListeVenteByClient(listClient);
        }

        if (reference != null && !reference.isEmpty()) {
            liste3 = getListeVenteByReference(reference);
        }

        if (dateL != null && dateR != null) {
            liste4 = getListeVenteByDate(dateL, dateR);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (Vente vente : liste1) {
                if (!listVente.contains(vente)) {
                    listVente.add(vente);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (Vente vente : liste2) {
                if (!listVente.contains(vente)) {
                    listVente.add(vente);
                }
            }
        }

        if (liste3 != null && liste3.size() > 0) {
            for (Vente vente : liste3) {
                if (!listVente.contains(vente)) {
                    listVente.add(vente);
                }
            }
        }

        if (liste4 != null && liste4.size() > 0) {
            for (Vente vente : liste4) {
                if (!listVente.contains(vente)) {
                    listVente.add(vente);
                }
            }
        }

        return listVente;
    }

    public List<Vente> findAllVente() {
        List<Vente> listVente = new ArrayList<Vente>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Vente();
                c.setCodeClient(rs.getInt("codeClient"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateVente"));
                listVente.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listVente;
    }

    private List<Vente> getListeVenteByProduits(List<Produit> listProduit) {
        List<Vente> listVente = new ArrayList<Vente>();

        if (listProduit != null && listProduit.size() > 0) {
            for (Produit prod : listProduit) {
                List<Vente> liste = new ArrayList<Vente>();
                PreparedStatement pst = null;
                ResultSet rs = null;
                Vente c;
                try {
                    pst = Connexion.getCon().prepareStatement("select * from vente where codeProduit =? ");
                    pst.setInt(1, prod.getCode());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        c = new Vente();
                        c.setCodeClient(rs.getInt("codeClient"));
                        c.setCodeProduit(rs.getInt("codeProduit"));
                        c.setReference(rs.getString("reference"));
                        c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                        c.setQuantite(rs.getInt("quantite"));
                        c.setDate(rs.getDate("dateVente"));
                        liste.add(c);
                    }
                    listVente.addAll(liste);
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listVente;
    }

    private List<Vente> getListeVenteByClient(List<Client> listClient) {
        List<Vente> listVente = new ArrayList<Vente>();

        if (listClient != null && listClient.size() > 0) {
            for (Client client : listClient) {
                List<Vente> liste = new ArrayList<Vente>();
                PreparedStatement pst = null;
                ResultSet rs = null;
                Vente c;
                try {
                    pst = Connexion.getCon().prepareStatement("select * from vente where codeClient =? ");
                    pst.setInt(1, client.getCode());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        c = new Vente();
                        c.setCodeClient(rs.getInt("codeClient"));
                        c.setCodeProduit(rs.getInt("codeProduit"));
                        c.setReference(rs.getString("reference"));
                        c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                        c.setQuantite(rs.getInt("quantite"));
                        c.setDate(rs.getDate("dateVente"));
                        liste.add(c);
                    }
                    listVente.addAll(liste);
                } catch (SQLException ex) {
                    Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listVente;
    }

    private List<Vente> getListeVenteByReference(String reference) {
        List<Vente> listVente = new ArrayList<Vente>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where reference like ? ");
            pst.setString(1, "%" + reference + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Vente();
                c.setCodeClient(rs.getInt("codeClient"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateVente"));
                listVente.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listVente;
    }

    public List<Vente> getListeVenteByFacture(String facture) {
        List<Vente> listVente = new ArrayList<Vente>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where refFacture =? ");
            pst.setString(1, facture);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Vente();
                c.setCodeClient(rs.getInt("codeClient"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateVente"));
                listVente.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listVente;
    }

    public boolean isExist(String ref) {
        boolean result = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where reference =? ");
            pst.setString(1, ref);
            rs = pst.executeQuery();
            while (rs.next()) {
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public int updateVente(Vente vente) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("update vente set prixUniteVente = ?,quantite = ? where reference = ?");
            pst.setDouble(1, vente.getPrixUnitaireVente());
            pst.setInt(2, vente.getQuantite());
            pst.setString(3, vente.getReference());
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

    public List<String> findAllProduitNameFacture(String facture) {
        List<String> listProduit = new ArrayList<String>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where refFacture =? ");
            pst.setString(1, facture);
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduit.add(modelProduit.findProduitByCode(rs.getInt("codeProduit")).getNom());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listProduit;
    }

    public int deleteVente(String ref) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("delete from vente where reference = ?");
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

    private List<Vente> getListeVenteByDate(Date dateL, Date dateR) {
        List<Vente> listVente = new ArrayList<Vente>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where dateVente between ? and ?");
            pst.setDate(1, new java.sql.Date(dateL.getTime()));
            pst.setDate(2, new java.sql.Date(dateR.getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Vente();
                c.setCodeClient(rs.getInt("codeClient"));
                c.setCodeProduit(rs.getInt("codeProduit"));
                c.setReference(rs.getString("reference"));
                c.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                c.setQuantite(rs.getInt("quantite"));
                c.setDate(rs.getDate("dateAchat"));
                listVente.add(c);
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
        return listVente;
    }

    public Vente getVente(String reference) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Vente vente = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from vente where reference =? ");
            pst.setString(1, reference);
            rs = pst.executeQuery();
            while (rs.next()) {
                vente = new Vente();
                vente.setCodeClient(rs.getInt("codeClient"));
                vente.setCodeProduit(rs.getInt("codeProduit"));
                vente.setReference(rs.getString("reference"));
                vente.setPrixUnitaireVente(rs.getDouble("prixUniteVente"));
                vente.setQuantite(rs.getInt("quantite"));
                vente.setDate(rs.getDate("dateVente"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceVenteImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return vente;
    }
}
