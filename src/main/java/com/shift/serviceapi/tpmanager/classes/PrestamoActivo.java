/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shift.serviceapi.tpmanager.classes;

import com.shift.serviceapi.tpmanager.entities.Cliente;
import com.shift.serviceapi.tpmanager.entities.Prestamo;
import com.shift.serviceapi.tpmanager.entities.TipoPrestamo;
import com.shift.serviceapi.tpmanager.utilities.Utilities;
import db.Db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JuanLHT
 */
public class PrestamoActivo {
    int id_prestamo,monto_prestamo,monto_pago,monto_pendiente,monto_pen_acum;
    String forma_prestamo,nombre_cliente,telefono_cliente,pagos;

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public int getMonto_prestamo() {
        return monto_prestamo;
    }

    public void setMonto_prestamo(int monto_prestamo) {
        this.monto_prestamo = monto_prestamo;
    }

    public int getMonto_pago() {
        return monto_pago;
    }

    public void setMonto_pago(int monto_pago) {
        this.monto_pago = monto_pago;
    }

    public int getMonto_pendiente() {
        return monto_pendiente;
    }

    public void setMonto_pendiente(int monto_pendiente) {
        this.monto_pendiente = monto_pendiente;
    }

    public int getMonto_pen_acum() {
        return monto_pen_acum;
    }

    public void setMonto_pen_acum(int monto_pen_acum) {
        this.monto_pen_acum = monto_pen_acum;
    }

    public String getForma_prestamo() {
        return forma_prestamo;
    }

    public void setForma_prestamo(String forma_prestamo) {
        this.forma_prestamo = forma_prestamo;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getPagos() {
        return pagos;
    }

    public void setPagos(String pagos) {
        this.pagos = pagos;
    }
    
    public ArrayList<PrestamoActivo> getPrestamosActivos(){
        
        int monto_pendiente_acum= 0;
        int count= -1;
        boolean val = false;
        ArrayList<PrestamoActivo> prestamos = new ArrayList<>();
        
        try {
            
            Db dbase = Utilities.getConection();
            String query_prestamos_activos ="SELECT id, tipo_prestamo_id, cliente_id FROM prestamo where estado =0 order by id asc;";
            ResultSet rs = dbase.execSelect(query_prestamos_activos);
            
            while(rs.next()){
                PrestamoActivo prestamo = new PrestamoActivo();
                int id_prestamo=rs.getInt(1);
                int id_tipo_prestamo = rs.getInt(2);
                int id_cliente = rs.getInt(3);
                
                prestamo.setId_prestamo(id_prestamo);
                prestamo.setForma_prestamo(TipoPrestamo.getFormaPrestamo(id_tipo_prestamo,dbase));
                prestamo.setNombre_cliente(Cliente.getCliente_nombre(id_cliente,dbase)+" "+Cliente.getCliente_apellido(id_cliente,dbase));
                prestamo.setTelefono_cliente(Cliente.getCliente_telefono(id_cliente,dbase));
                prestamo.setMonto_prestamo(TipoPrestamo.getMonto_total(id_tipo_prestamo,dbase));
                prestamo.setPagos(TipoPrestamo.getpagos_realizados(id_prestamo,dbase)+" / "+TipoPrestamo.getpagos(id_tipo_prestamo,dbase));
                prestamo.setMonto_pago(TipoPrestamo.getmonto_cuota(id_tipo_prestamo,dbase));
                prestamo.setMonto_pendiente(Prestamo.getMonto_pendiente(id_prestamo,dbase));
                monto_pendiente_acum += Prestamo.getMonto_pendiente(id_prestamo,dbase);
                prestamo.setMonto_pen_acum(monto_pendiente_acum);
                prestamos.add(prestamo);
            }
            dbase.CerrarConexion();
            return prestamos;
        } catch (SQLException ex) {
            Logger.getLogger(PrestamoActivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prestamos;
    }
    
}
