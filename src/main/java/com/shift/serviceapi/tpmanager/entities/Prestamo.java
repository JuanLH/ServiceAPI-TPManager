/*t
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.entities;


import com.shift.serviceapi.tpmanager.utilities.Utilities;
import com.google.gson.Gson;
import com.shift.serviceapi.tpmanager.classes.Respuesta;
import db.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author JuanLuis
 */
public class Prestamo {
    int id,tipo_prestamo_id,cliente_id,ruta_id,estado,user;
    String nombreClient,ApellidoClient;

    public String getNombreClient() {
        return nombreClient;
    }

    public void setNombreClient(String nombreClient) {
        this.nombreClient = nombreClient;
    }

    public String getApellidoClient() {
        return ApellidoClient;
    }

    public void setApellidoClient(String ApellidoClient) {
        this.ApellidoClient = ApellidoClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo_prestamo_id() {
        return tipo_prestamo_id;
    }

    public void setTipo_prestamo_id(int tipo_prestamo_id) {
        this.tipo_prestamo_id = tipo_prestamo_id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getRuta_id() {
        return ruta_id;
    }

    public void setRuta_id(int ruta_id) {
        this.ruta_id = ruta_id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
    
    public String insertar_Prestamo(String informacion){
        Db dbase = Utilities.getConection();
       /* String sql ="INSERT INTO prestamo(tipo_prestamo_id, cliente_id, ruta_id, estado, usuario)";
        sql +="VALUES (?, ?, ?, ?, ?);";*/
        String respon;
        Gson json = new Gson();
            Prestamo info = json.fromJson(informacion, Prestamo.class);
        String sql = "SELECT insertar_prestamo(0,"+info.getTipo_prestamo_id()+","+info.getCliente_id()+","+info.getRuta_id()+",";
        sql += " "+info.getEstado()+","+info.getUser()+"); ";
        System.out.println("Query --> : "+sql);
        try{
            //PreparedStatement p = Db.conexion.prepareStatement(sql);
            ResultSet rs = dbase.execSelect(sql);
            rs.next();
            
            if(rs.getInt(1)== 1)
                 respon =  "1";
            else if(rs.getInt(1) == 2)
                 respon = "2";
            else
                 respon = "-1";
            //p.setInt(1, info.getId());
            /*p.setInt(1, info.getTipo_prestamo_id());
            p.setInt(2, info.getCliente_id());
            p.setInt(3, info.getRuta_id());
            p.setInt(4, info.getEstado());
            p.setInt(5, info.getUser());
            p.executeQuery();*/
            dbase.CerrarConexion();
            return respon;
            
            
        }
        catch(SQLException e){
            System.out.println("Error de la base de datos -> "+e.getMessage());
            return "-2";
        }
    }
    
    public String consultar_prestamo(int idRuta , int estado){
        Respuesta respon  = new Respuesta();
        Db dbase = Utilities.getConection();
        ArrayList<Prestamo> lista = new ArrayList<Prestamo>();
        
        /*String sql = "SELECT id, tipo_prestamo_id, cliente_id, ruta_id, estado, usuario";
        sql +="FROM prestamo where ruta_id = "+idRuta+";";*/
        String sql;
        
        if(estado >1){
            sql = "SELECT p.id, tipo_prestamo_id,cliente_id,c.nombre,c.apellido, usuario ";
            sql += "FROM prestamo as p LEFT join cliente as c ON c.id = cliente_id where p.ruta_id = "+idRuta+" ;";
        }
        else{
            sql = "SELECT p.id, tipo_prestamo_id,cliente_id,c.nombre,c.apellido, usuario ";
            sql += "FROM prestamo as p LEFT join cliente as c ON c.id = cliente_id where p.ruta_id = "+idRuta+" and p.estado = "+estado+" ;";
        }
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            while(rs.next()){
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt(1));
                prestamo.setTipo_prestamo_id(rs.getInt(2));
                prestamo.setCliente_id(rs.getInt(3));
                prestamo.setNombreClient(rs.getString(4));
                prestamo.setApellidoClient(rs.getString(5));
                prestamo.setUser(rs.getInt(6));
                
                lista.add(prestamo);
            }
            
            if(lista.isEmpty())
            {
                respon.setId(0);
                respon.setMensaje("No hay registros actualmente en la base de datos");
                dbase.CerrarConexion();
                return respon.ToJson(respon);
            }
        }
        catch(SQLException e){
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
    
    public String consultar_prestamo(String nombre , int estado){
        Respuesta respon  = new Respuesta();
        Db dbase = Utilities.getConection();
        ArrayList<Prestamo> lista = new ArrayList<Prestamo>();
        
        /*String sql = "SELECT id, tipo_prestamo_id, cliente_id, ruta_id, estado, usuario";
        sql +="FROM prestamo where ruta_id = "+idRuta+";";*/
        String sql;
        if(estado >1){
            sql = "SELECT p.id, tipo_prestamo_id,cliente_id,c.nombre,c.apellido, usuario ";
            sql += "FROM prestamo as p LEFT join cliente as c ON c.id = cliente_id where c.nombre  ilike '"+nombre+"%';";
        }
        else{
            sql = "SELECT p.id, tipo_prestamo_id,cliente_id,c.nombre,c.apellido, usuario ";
            sql += "FROM prestamo as p LEFT join cliente as c ON c.id = cliente_id where c.nombre  ilike '"+nombre+"%' and p.estado ="+estado+";";
        }
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            while(rs.next()){
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt(1));
                prestamo.setTipo_prestamo_id(rs.getInt(2));
                prestamo.setCliente_id(rs.getInt(3));
                prestamo.setNombreClient(rs.getString(4));
                prestamo.setApellidoClient(rs.getString(5));
                prestamo.setUser(rs.getInt(6));
                
                lista.add(prestamo);
            }
            
            if(lista.isEmpty())
            {
                respon.setId(0);
                respon.setMensaje("No hay registros actualmente en la base de datos");
                dbase.CerrarConexion();
                return respon.ToJson(respon);
            }
        }
        catch(SQLException e){
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
    
    public String consultar_prestamo_id(int id){
        Respuesta respon  = new Respuesta();
        Db dbase = Utilities.getConection();
        Prestamo prestamo = new Prestamo();
        
        
        
        String sql = "SELECT p.id, tipo_prestamo_id,cliente_id,c.nombre,c.apellido, usuario ";
        sql += "FROM prestamo as p LEFT join cliente as c ON c.id = cliente_id where p.id = "+id+";";
        
        try{
            ResultSet rs=dbase.execSelect(sql);
            if(rs.next()){
                
                prestamo.setId(rs.getInt(1));
                prestamo.setTipo_prestamo_id(rs.getInt(2));
                prestamo.setCliente_id(rs.getInt(3));
                prestamo.setNombreClient(rs.getString(4));
                prestamo.setApellidoClient(rs.getString(5));
                prestamo.setUser(rs.getInt(6));
                
                respon.setId(1);
                respon.setMensaje(respon.ToJson(prestamo));//convierto la lista a Gson
                dbase.CerrarConexion();
                return respon.ToJson(respon); 
                
            }
            else
            {
                respon.setId(0);
                respon.setMensaje("No hay registros actualmente en la base de datos");
                dbase.CerrarConexion();
                return respon.ToJson(respon);
            }
        }
        catch(SQLException e){
            respon.setId(-1);
            respon.setMensaje("Error de la base de datos "+e.getMessage());
            dbase.CerrarConexion();
            return respon.ToJson(respon);
        }
        
    }
    
    public String modificar_ruta_cliente(String informacion){
        Db dbase = Utilities.getConection();
        Gson json = new Gson();
        Prestamo prestamo = json.fromJson(informacion, Prestamo.class);
        
        String sql = "UPDATE prestamo ";
        sql += "SET  ruta_id="+prestamo.getRuta_id()+" ";
        sql += "WHERE id="+prestamo.getId()+";";
        
        try
        {
            PreparedStatement p = Db.conexion.prepareStatement(sql);
            p.executeQuery();
            dbase.CerrarConexion();
            return "1";
        }
        catch(SQLException e)
        {
            //System.out.println("SqlStateQuery = "+e.getSQLState()+" - "+sql);
            return e.getSQLState();
        }
        
    }
    
    //Metodo que elimina un prestamo de la base de datos
    public static String eliminar_prestamo(int id){
        Db dbase = Utilities.getConection();
        String query = "SELECT eliminar_prestamo("+id+");";
        try{
            ResultSet rs = dbase.execSelect(query);
            rs.next();
            return Integer.toString(rs.getInt(1));
        }
        catch(SQLException e){
            return e.getMessage();
        }
    }
    
    public static int getMonto_pendiente(int id_prestamo,Db dbase){
        //Db dbase = Utilities.getConection();
        String query = "SELECT get_monto_restante("+id_prestamo+");";
        try{
            ResultSet rs = dbase.execSelect(query);
            rs.next();
            int pendiente = rs.getInt(1);
            rs.close();
            return pendiente;
        }
        catch(SQLException e){
            System.out.println("Error database "+e.getMessage() );
            return -1;
        }
    }
    
}
