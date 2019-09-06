/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.classes;

import com.google.gson.Gson;
/**
 *
 * @author Juan Luis
 */
public class Respuesta {
    
    private int id;
    private String mensaje;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
    public static String ToJson(Object objeto){
    
        Gson json = new Gson();
        return json.toJson(objeto);
    }
    
    public static String ToJson(int id,Object objeto){
    
        Gson json = new Gson();
        Respuesta response = new Respuesta();
        response.setId(id);
        response.setMensaje(json.toJson(objeto));
        return json.toJson(response);
    }
    
    public  static Respuesta FromJson(String gson)
    {
        Gson json = new Gson();
        Respuesta resp = json.fromJson(gson, Respuesta.class);
        return resp; 
        
    }
}
