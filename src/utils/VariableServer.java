/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Cruz Roja
 */
public class VariableServer {
    private static String host;
    private static String baseDatos;
    private static String user;
    private static String pass;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        VariableServer.host = host;
    }

    public static String getBaseDatos() {
        return baseDatos;
    }

    public static void setBaseDatos(String baseDatos) {
        VariableServer.baseDatos = baseDatos;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        VariableServer.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        VariableServer.pass = pass;
    }

  
 
    
}
