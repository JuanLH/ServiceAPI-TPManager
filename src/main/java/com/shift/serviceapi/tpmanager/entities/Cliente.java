/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.entities;



import com.google.gson.Gson;
import com.shift.serviceapi.tpmanager.classes.Respuesta;
import com.shift.serviceapi.tpmanager.utilities.Utilities;
import db.Db;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author JuanLuis
 */
public class Cliente {
    int id,sexo,estado;
    String nombre,apellido,cedula,direccion,apodo;
    Date nacimiento;

     public Cliente(int id, String nombre, String apellido, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
    }

    public Cliente() {
    }
    
    
    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }
    
     public String insertar_cliente(String informacion){
         
     
        Db dbase = Utilities.getConection();
        String sql = "INSERT INTO cliente(nombre, apellido, cedula, direccion, apodo,sexo,nacimiento, estado)";
        sql+= "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try{
            PreparedStatement p = Db.conexion.prepareStatement(sql);
            Gson json = new Gson();
            Cliente info = json.fromJson(informacion, Cliente.class);
            //p.setInt(1, info.getId());
            p.setString(1, info.getNombre());
            p.setString(2, info.getApellido());
            p.setString(3, info.getCedula());
            p.setString(4, info.getDireccion());
            p.setString(5, info.getApodo());
            p.setInt(6, info.getSexo());
            p.setDate(7, info.getNacimiento());
            p.setInt(8, info.getEstado());
            
            p.executeQuery();
            dbase.CerrarConexion();
            return "1";
        }
        catch(SQLException e){
            return e.getSQLState();
        }
        
     }
     
     public String modificar_cliente(String informacion){
         Db dbase = Utilities.getConection();
         Gson json = new Gson();
         Cliente cli = json.fromJson(informacion, Cliente.class);
         String sql = "UPDATE cliente ";
         sql+="SET  nombre='"+cli.getNombre()+"', apellido='"+cli.getApellido()+"', cedula='"+cli.getCedula()+"', direccion='"+cli.getDireccion()+"', ";
         sql+=" apodo='"+cli.getApodo()+"', sexo='"+Integer.toString(cli.getSexo())+"', nacimiento='"+cli.getNacimiento()+"', estado="+Integer.toString(cli.getEstado())+" ";
         sql+="WHERE id = "+Integer.toString(cli.getId())+";";
         
         
         
         try{
            PreparedStatement p = Db.conexion.prepareStatement(sql);
            p.executeQuery();
            dbase.CerrarConexion();
            return "1";
         
         }
         catch(SQLException e){
            //System.out.println("Query = "+sql);
            return e.getSQLState();
         }
         
     }
     
     public String consultar_cliente(String nombre) {
        Respuesta respon  = new Respuesta();
        Db dbase = Utilities.getConection();
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        String sql="SELECT id, nombre, apellido, cedula, direccion, apodo,sexo,nacimiento,estado edad FROM cliente ";
        sql +="where estado =1 and nombre ilike '"+nombre+"%'  order by id;";
        
        
        
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            
            while(rs.next()){
                Cliente client = new Cliente();
                
                client.setId(rs.getInt(1));
                client.setNombre(rs.getString(2));
                client.setApellido(rs.getString(3));
                client.setCedula(rs.getString(4));
                client.setDireccion(rs.getString(5));
                client.setApodo(rs.getString(6));
                client.setSexo(rs.getInt(7));
                client.setNacimiento(rs.getDate(8));
                client.setEstado(rs.getInt(9));
                
                
                lista.add(client);
                
            }
            
            if(lista.isEmpty())
            {
                respon.setId(0);
                respon.setMensaje("No hay registros actualmente en la base de datos");
                dbase.CerrarConexion();
                return respon.ToJson(respon);
            }
            
            
        }
        catch (SQLException e) 
        {
            //si falla un error de base de datos
            respon.setId(-1);
            respon.setMensaje("Error de la base de datos "+e.getMessage());
            dbase.CerrarConexion();
            return respon.ToJson(respon);
             
                     
        }
        respon.setId(1);
        respon.setMensaje(respon.ToJson(lista));//convierto la lista a Gson
        dbase.CerrarConexion();
        return respon.ToJson(respon); 
     }
     
     public String consultar_cliente(int id){
        Respuesta respon  = new Respuesta();
        Db dbase = Utilities.getConection();
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        String sql="SELECT id, nombre, apellido, cedula, direccion, apodo,sexo,nacimiento,estado edad  ";
        sql +="FROM cliente where estado =1 and id = "+id+" ";
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            if(rs.next()){
                Cliente client = new Cliente();
                
                client.setId(rs.getInt(1));
                client.setNombre(rs.getString(2));
                client.setApellido(rs.getString(3));
                client.setCedula(rs.getString(4));
                client.setDireccion(rs.getString(5));
                client.setApodo(rs.getString(6));
                client.setSexo(rs.getInt(7));
                client.setNacimiento(rs.getDate(8));
                client.setEstado(rs.getInt(9));
                
                respon.setId(1);
                respon.setMensaje(respon.ToJson(client));
                return respon.ToJson(respon);
                
            }
            else{
                respon.setId(0);
                respon.setMensaje("no existe el id = "+id);
                return respon.ToJson(respon);
            }
            
            

        }
        catch(SQLException e){
            respon.setId(-1);
            respon.setMensaje("error en la base de datos = "+e.getMessage());
            return respon.ToJson(respon);
        }
     }
     
     
     public static String getCliente_nombre(int id_cliente,Db dbase){
        //Db dbase = Utilities.getConection();
        String query = "SELECT  nombre FROM cliente where id = "+id_cliente+";";
        try{
            ResultSet rs=dbase.execSelect(query);
            rs.next();
            String nombre = rs.getString(1);
            rs.close();
            return nombre;
        }
        catch(SQLException e){
            System.out.println("Error de bases "+e.getMessage());
            return "error";
            
        }
     }
     
     public static String getCliente_apellido(int id_cliente,Db dbase){
        //Db dbase = Utilities.getConection();
        String query = "SELECT  apellido FROM cliente where id = "+id_cliente+";";
        try{
            ResultSet rs=dbase.execSelect(query);
            rs.next();
            String apellido = rs.getString(1);
            rs.close();
            return apellido;
        }
        catch(SQLException e){
            System.out.println("Error de bases "+e.getMessage());
            return "error";
            
        }
     }
     
    public static String getCliente_telefono(int id_cliente,Db dbase){
        //Db dbase = Utilities.getConection();
        String query = "SELECT  numero FROM telefono where cliente_id ="+id_cliente+";";
        try{
            ResultSet rs=dbase.execSelect(query);
            rs.next();
            String tel = rs.getString(1);
            rs.close();
            return tel;
        }
        catch(SQLException e){
            System.out.println("Error de bases "+e.getMessage());
            return "error";
            
        }
     }
     
    //==============================Nuevos cambios============================== 
    public static Cliente getCliente(int id_cliente){
        Cliente client = new Cliente();
        Db dbase = Utilities.getConection();
        String sql="SELECT id, nombre, apellido, cedula, direccion, apodo,sexo,nacimiento,estado edad  ";
        sql +="FROM cliente where estado =1 and id = "+id_cliente+" ";
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            if(rs.next()){
                client.setId(rs.getInt(1));
                client.setNombre(rs.getString(2));
                client.setApellido(rs.getString(3));
                client.setCedula(rs.getString(4));
                client.setDireccion(rs.getString(5));
                client.setApodo(rs.getString(6));
                client.setSexo(rs.getInt(7));
                client.setNacimiento(rs.getDate(8));
                client.setEstado(rs.getInt(9));    
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            return null;
        }
        
        return client;
     }       
};
