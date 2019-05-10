/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.CategorieClient;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceCategorie {

    public List<CategorieClient> findCategorieByCritere(String nom);

    public List<CategorieClient> findAllCategorie();

    public int deleteCategorieByCode(Integer integer);

    public CategorieClient findCategorieByCode(int code);

    public int saveCategorie(CategorieClient c);

    public boolean isExist(String nom);

    public int modifierCategorie(String nom, Double taux, int code);

    public CategorieClient findCategorieByName(String nomCategorie);
    
    public Double getTauxByCode(int code);
    
}
