/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.service;

import com.stockmanager.data.MouvementStock;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Maurice
 */
public interface IServiceMouvement {

    public List<MouvementStock> findAllMouvement();

    public int updateMouvement(MouvementStock mouv);

    public List<MouvementStock> findMouvementByCritere(String description, Date dateL, Date dateR);

    public int saveMouvement(MouvementStock u);

    public MouvementStock getMouvementByCode(Integer code);

    public int deleteMouvement(Integer integer);
    
}
