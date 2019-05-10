/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.Achat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceAchat {

    public List<Achat> findAchatByCritere(String nomProduit, String nomFournisseur, String reference, Date dateL, Date dateR);

    public int saveAchat(Achat achat);

    public Achat getAchat(String reference);

    public int modifierAchat(Integer integer, Integer integer0, String string);

    public List<Achat> findAllAchat();

    public int updateAchat(Achat achat);
    
    public int deleteAchat(String ref);
}
