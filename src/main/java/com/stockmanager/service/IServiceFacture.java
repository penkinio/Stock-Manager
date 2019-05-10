/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Facture;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceFacture {

    public List<Facture> findFactureByCritere(String nomclient, String numFacture);

    public int saveFacture(Facture achat);

    public int modifierFacture(Integer integer, Integer integer0, String string);

    public List<Facture> findAllFacture();

    public double getMontantFacture(String numFactute);

    public int modifierFacture(String numFacture);

    public Facture findFactureByCode(int code);

    public int updateFacture(Facture facture);

    public Facture findFactureByNumero(String refFacture);

    public List<Facture> findFactureLikeNumero(String facture);
    
    public int deleteFacture(String numFacture);

    public Iterable<Facture> findAllFactureRegler();
    
}
