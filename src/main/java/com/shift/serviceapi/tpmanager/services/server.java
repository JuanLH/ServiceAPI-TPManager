/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.services;

/**
 *
 * @author Michael Jared Diaz
 */


import com.shift.serviceapi.tpmanager.classes.PrestamoActivo;
import com.shift.serviceapi.tpmanager.classes.Respuesta;
import static spark.Spark.*;


public class server {
    
    public static void main(String a []){
        
        port(2323);
        
        get("/get_prestamos_activos",(req,res)-> {
            PrestamoActivo pa = new PrestamoActivo();
            String respo = Respuesta.ToJson(1,pa.getPrestamosActivos());
            return respo;
        });
       
    }
    
}
