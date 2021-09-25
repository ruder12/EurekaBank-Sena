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
public class bitacoraAsignaciones {
    private int codigo;
    private String empleado;
    private String sucursal;
    private String fechaalta;
    private String fechabaja;

    public bitacoraAsignaciones(int codigo, String empleado, String sucursal, String fechaalta, String fechabaja) {
        this.codigo = codigo;
        this.empleado = empleado;
        this.sucursal = sucursal;
        this.fechaalta = fechaalta;
        this.fechabaja = fechabaja;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFechaalta() {
        return fechaalta;
    }

    public void setFechaalta(String fechaalta) {
        this.fechaalta = fechaalta;
    }

    public String getFechabaja() {
        return fechabaja;
    }

    public void setFechabaja(String fechabaja) {
        this.fechabaja = fechabaja;
    }
    
}
