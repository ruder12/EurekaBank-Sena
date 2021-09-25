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
public class ConfigEmpleadoSesion {

    private String codigo;
    private String Usuario;
    private String Password;
    private String code_sucursal;

    public ConfigEmpleadoSesion(String codigo, String Usuario, String Password, String code_sucursal) {
        this.codigo = codigo;
        this.Usuario = Usuario;
        this.Password = Password;
        this.code_sucursal = code_sucursal;
    }
public ConfigEmpleadoSesion(){
        this.codigo = getCodigo();
        this.Usuario = Usuario;
        this.Password = Password;
        this.code_sucursal = code_sucursal;
}
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getCode_sucursal() {
        return code_sucursal;
    }

    public void setCode_sucursal(String code_sucursal) {
        this.code_sucursal = code_sucursal;
    }

}
