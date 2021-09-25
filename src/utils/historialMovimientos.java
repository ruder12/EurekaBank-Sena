/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class historialMovimientos {
    private  String despcripcion;
    private  String accion;
    private  String fecha;
    private  String importe;
     private  String cuentareferencia;

    public historialMovimientos(String despcripcion, String accion, String fecha, String importe,String cuentareferencia) {
        this.despcripcion = despcripcion;
        this.accion = accion;
        this.fecha = fecha;
        this.importe = importe;
        this.cuentareferencia = cuentareferencia;
    }

    public String getCuentareferencia() {
        return cuentareferencia;
    }

    public void setCuentareferencia(String cuentareferencia) {
        this.cuentareferencia = cuentareferencia;
    }

    public String getDespcripcion() {
        return despcripcion;
    }

    public void setDespcripcion(String despcripcion) {
        this.despcripcion = despcripcion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }
    
}
