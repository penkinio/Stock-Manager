/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Journal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceJournal {
    
    /**
     * 
     * @param j
     * @return 
     */
    public int saveJournal(Journal j);
    
    /**
     * 
     * @param user
     * @param operation
     * @param DateOperationLeft
     * @param DateOperationRight
     * @return
     * @throws SQLException 
     */
    public List<Journal> findAllByCritere(String user,String operation,Date DateOperationLeft, Date DateOperationRight) throws SQLException;
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public List<Journal> findAllJournal() throws SQLException;
}
