/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Vente;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceVente {

    public Vente getVente(int codeProduit, int codeClient, String reference);

    public int saveVente(Vente vente);

    public List<Vente> findAllVente();

    public int modifierVente(Integer integer, Integer integer0, String string);

    public List<Vente> findVenteByCritere(String nomProduit, String nomClient, String reference, Date dateL, Date dateR);
    
    public List<Vente> getListeVenteByFacture(String facture);

    public boolean isExist(String ref);

    public int updateVente(Vente vente);

    public List<String> findAllProduitNameFacture(String facture);

    public int deleteVente(String facture);

    public Vente getVente(String reference);
    
}
