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
public class GestionCuentasAdmin {

    private String numerocuenta;
    private String moneda;
    private String sucursal;
    private String empleado;
    private String saldo;
    private String fecha;
    private String estado;
  
    private int conMvimientos;

    public GestionCuentasAdmin(String numerocuenta, String moneda, String sucursal, String empleado, String saldo, String fecha, String estado, int conMvimientos) {

        this.numerocuenta = numerocuenta;
        this.moneda = moneda;
        this.sucursal = sucursal;
        this.empleado = empleado;
        this.fecha = fecha;
        this.estado = estado;
        this.conMvimientos = conMvimientos;
     
        this.saldo = saldo;

    }

   

    public String getNumerocuenta() {
        return numerocuenta;
    }

    public void setNumerocuenta(String numerocuenta) {
        this.numerocuenta = numerocuenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {

        this.saldo = saldo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getConMvimientos() {
        return conMvimientos;
    }

    public void setConMvimientos(int conMvimientos) {
        this.conMvimientos = conMvimientos;
    }

}
