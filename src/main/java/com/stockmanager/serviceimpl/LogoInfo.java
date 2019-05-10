/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockmanager.serviceimpl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Maurice
 */
public class LogoInfo {

    public static InputStream getLogo() {
        return new BufferedInputStream(LogoInfo.class.getClassLoader()
                .getResourceAsStream("com/stockmanager/images/logo.png"));
    }
    
    public static URL getFile(String name) {
        return LogoInfo.class.getClassLoader().getResource(name);
    }
}
