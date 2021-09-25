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
public class Sucursales {

    private int codigo;
    private String nombre_sucursal;
    private String ciudad;
    private String idciudad;
    private String direccion;
    private int numCuentas;

    public Sucursales(int codigo, String nombre_sucursal,String idciudad, String ciudad, String direccion, int numCuentas) {
        this.codigo = codigo;
        this.nombre_sucursal = nombre_sucursal;
        this.ciudad = ciudad;
         this.idciudad = idciudad;
        this.direccion = direccion;
        this.numCuentas = numCuentas;
    }

    public String getIdciudad() {
        return idciudad;
    }

    public void setIdciudad(String idciudad) {
        this.idciudad = idciudad;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumCuentas() {
        return numCuentas;
    }

    public void setNumCuentas(int numCuentas) {
        this.numCuentas = numCuentas;
    }

}
