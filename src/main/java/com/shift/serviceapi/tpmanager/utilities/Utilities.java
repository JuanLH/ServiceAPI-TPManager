/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.utilities;

import db.Db;
import java.sql.Connection;

/**
 *
 * @author JuanLH
 */
public class Utilities {
    private static Connection cnn = null;
    
    public static Db getConection(){
        Db dbase = new Db("PManager","postgres","letmein");
        return dbase;
    }
}
