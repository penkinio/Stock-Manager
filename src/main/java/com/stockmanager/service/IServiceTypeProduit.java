/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.TypeProduit;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceTypeProduit {

    public List<TypeProduit> findTypeProduitByCritere(String lib, String loc);

    public TypeProduit findTypeProduitByCode(int code);
    
    public TypeProduit findTypeProduitByName(String name);

    public int saveTypeProduit(TypeProduit c);

    public boolean isExist(String lib);

    public int modifierTypeProduit(String lib, String loc, int code);

    public int deleteTypeProduitByCode(Integer integer);

    public List<TypeProduit> findAllTypeProduit();

    public TypeProduit getTypeProduitByLibelle(String libelle);
    
}
