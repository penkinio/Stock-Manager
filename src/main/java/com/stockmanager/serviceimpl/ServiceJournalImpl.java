/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import com.stockmanager.connection.Connexion;
import com.stockmanager.data.Journal;
import com.stockmanager.service.IServiceJournal;
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
public class ServiceJournalImpl implements IServiceJournal {

    public int saveJournal(Journal j) {
        PreparedStatement pst = null;
        int result = 1;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into journal(operation,user, dateOperation,description) values (?,?,?,?)");
            pst.setString(1, j.getOperation());
            pst.setString(2, j.getUser());
            pst.setDate(3, new java.sql.Date(j.getDateOperation().getTime()));
            pst.setString(4, j.getDescription());
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

    public List<Journal> findAllByCritere(String user, String operation, Date DateOperationLeft, Date DateOperationRight) throws SQLException {
        List<Journal> listJournal = new ArrayList<Journal>();
        List<Journal> liste1 = new ArrayList<Journal>();
        List<Journal> liste2 = new ArrayList<Journal>();
        List<Journal> liste3 = new ArrayList<Journal>();

        if (user.isEmpty() && operation.isEmpty() && (DateOperationLeft == null || DateOperationRight == null)) {
            return findAllJournal();
        }

        if (user != null && !user.isEmpty()) {
            liste1 = getListeJournalByUser(user);
        }

        if (operation != null && !operation.isEmpty()) {
            liste2 = getListeJournalByOperation(operation);
        }

        if (DateOperationLeft != null && DateOperationRight != null) {
            liste3 = getListeJournalByDate(DateOperationLeft, DateOperationRight);
        }

        if (liste1 != null && liste1.size() > 0) {
            for (Journal journal : liste1) {
                if (!listJournal.contains(journal)) {
                    listJournal.add(journal);
                }
            }
        }

        if (liste2 != null && liste2.size() > 0) {
            for (Journal journal : liste2) {
                if (!listJournal.contains(journal)) {
                    listJournal.add(journal);
                }
            }
        }

        if (liste3 != null && liste3.size() > 0) {
            for (Journal journal : liste3) {
                if (!listJournal.contains(journal)) {
                    listJournal.add(journal);
                }
            }
        }
        return listJournal;
    }

    public List<Journal> findAllJournal() throws SQLException {
        List<Journal> listJournal = new ArrayList<Journal>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Journal j;
        try {
            pst = Connexion.getCon().prepareStatement("select * from journal");
            rs = pst.executeQuery();
            while (rs.next()) {
                j = new Journal();
                j.setCode(rs.getInt("code"));
                j.setDateOperation(rs.getDate("dateOperation"));
                j.setDescription(rs.getString("description"));
                j.setOperation(rs.getString("operation"));
                j.setUser(rs.getString("user"));
                listJournal.add(j);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listJournal;
    }

    private List<Journal> getListeJournalByUser(String user) {
        List<Journal> listJournal = new ArrayList<Journal>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Journal c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from journal where user like ? ");
            pst.setString(1, "%" + user + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Journal();
                c.setCode(rs.getInt("code"));
                c.setDateOperation(rs.getDate("dateOperation"));
                c.setDescription(rs.getString("description"));
                c.setOperation(rs.getString("operation"));
                c.setUser(rs.getString("user"));
                listJournal.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listJournal;
    }

    private List<Journal> getListeJournalByOperation(String operation) {
        List<Journal> listJournal = new ArrayList<Journal>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Journal c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from journal where operation like ? ");
            pst.setString(1, "%" + operation + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Journal();
                c.setCode(rs.getInt("code"));
                c.setDateOperation(rs.getDate("dateOperation"));
                c.setDescription(rs.getString("description"));
                c.setOperation(rs.getString("operation"));
                c.setUser(rs.getString("user"));
                listJournal.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listJournal;
    }

    private List<Journal> getListeJournalByDate(Date DateOperationLeft, Date DateOperationRight) {
        List<Journal> listJournal = new ArrayList<Journal>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Journal c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from journal where dateOperation between ? and ? ");
            pst.setDate(1, new java.sql.Date(DateOperationLeft.getTime()));
            pst.setDate(2, new java.sql.Date(DateOperationRight.getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Journal();
                c.setCode(rs.getInt("code"));
                c.setDateOperation(rs.getDate("dateOperation"));
                c.setDescription(rs.getString("description"));
                c.setOperation(rs.getString("operation"));
                c.setUser(rs.getString("user"));
                listJournal.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceJournalImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listJournal;
    }

}
