/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import utils.VariableSesion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class ConsultasMySQL extends BDEureka {

    private final Connection con;
    private String strQuery;
    VariableSesion vss;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int idempl = VariableSesion.getcodemple();
    String fechaactual = String.valueOf(LocalDate.now());
    Calendar fecha = new GregorianCalendar();

    /**
     *
     */
    public ConsultasMySQL() {

        this.con = getConnection();

    }

    /**
     * esta metodo realiza el cobro mensual para cada una de las cuentas recibe
     * como parametro el id del empleado
     *
     * @param idem
     */
    public void cobroAutoMes(int idem) {
        String fechacobro;

        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        // int dia = fecha.get(Calendar.DAY_OF_MONTH);
        fechacobro = String.valueOf(año + "-0" + (mes + 1) + "-" + "01");
        if (mes > 9) {
            fechacobro = String.valueOf(año + "-" + (mes + 1) + "-" + "01");
        }

        int r = cobroMes(fechaactual, fechacobro);
        if (r != 0) {
            try {
                String sql = "SELECT cuent_codigo,cuent_monecodigo,cuent_cuenta_saldo FROM cuenta;";
                rs = SELECT(sql);
                //MONTO MAXIMO PARA EL COBRO MENSUAL y INTERESES
                int montomaximoCOP = 3500000;
                int montomaximoUSA = 1200;
                int CargomantenimientoCOP = 7000;
                double CargomantenimientoUSA = 2.50;
                double interese = 0;

                int i = 1;
                while (rs.next()) {

                    int numCuenta = rs.getInt(1);
                    int CuentaMoneda = rs.getInt(2);
                    int CuentaSaldo = rs.getInt(3);

                    if (CuentaMoneda == 1 && CuentaSaldo > montomaximoCOP)//peso colombiano
                    {
                        interese = CargomantenimientoCOP;

                    } else if (CuentaMoneda == 2 && CuentaSaldo > montomaximoUSA)//dolar
                    {
                        interese = CargomantenimientoUSA;

                    }

                    String Queryactualizar = "UPDATE cuenta SET cuent_cuenta_saldo=cuent_cuenta_saldo-" + interese + " WHERE cuent_codigo=" + numCuenta + ";";
                    pst = this.con.prepareStatement(Queryactualizar);
                    int rest = pst.executeUpdate();
                    if (rest == 1) {
                        pst = null;
                        String Querymovi = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe)VALUES(?,?,?,?)";
                        //MOVIMIENTO CADA CUENTA
                        pst = this.con.prepareStatement(Querymovi);
                        pst.setInt(1, numCuenta);
                        pst.setInt(2, idem);
                        pst.setInt(3, 12);
                        pst.setDouble(4, interese);
                        pst.executeUpdate();

                    }
                    pst = null;
                    i++;

                }

                VariableSesion.setMsj("Cobros por Cargo de Mantenimiento Realizado Existosamente");
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                pst = null;
            }
        } else {
            VariableSesion.setMsj("No se Realizo Cobros por Cargo de Mantenimiento, Revise la fecha");
        }
    }

    /**
     * metodo para verificar que exista la base de datos
     *
     * @param nom
     * @return
     */
    public int existeBaseDatos(String nom) {
        int msj = 0;
        try {

            pst = this.con.prepareStatement("show databases");
            rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getString(1).equals(nom)) {
                    msj = 1;
                    return msj;
                }
            }

        } catch (SQLException ex) {
            System.out.println("error: " + ex);
        }
        return msj;
    }

    /**
     * consulta select a la base de datos
     *
     * @param Query
     * @return
     */
    public ResultSet SELECT(String Query) {
        this.strQuery = Query;
        try {

            rs = con.createStatement().executeQuery(this.strQuery);

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;

    }

    /**
     *
     * @param user
     * @param pass
     * @return
     */
    public String ValidarUSER(int user, String pass) {
        String res = "0";

        try {

            String sql = "SELECT * "
                    + "FROM empleado AS e "
                    + "INNER JOIN rol AS r "
                    + "ON e.rol = r.idrol "
                    + "WHERE BINARY e.cedula=?  "
                    + "AND BINARY e.empl_clave=?  "
                    + "AND e.rol != 0 AND e.empl_status != 0";

            pst = con.prepareStatement(sql);
            pst.setInt(1, user);
            pst.setString(2, pass);

            rs = pst.executeQuery();

            if (rs.next()) {
                int codeuse = rs.getInt("empl_codigo");
                String nombreRol = rs.getString("nombre_rol");
                String idrol = rs.getString("rol");
                String code_sucursal = rs.getString("empl_sucursal_cod");

                res = idrol + "-" + nombreRol + "-" + codeuse + "-" + code_sucursal;

            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;

    }

    /**
     *
     * @param nombre
     * @param ciudad
     * @param direccion
     * @return
     */
    public int INSERT_SUCURSAL(String nombre, int ciudad, String direccion) {

        int resultado = 0;

        try {
            String queryVAL = "SELECT sucu_nombre,sucu_id_ciudad FROM sucursales WHERE sucu_nombre = '" + nombre + "' AND sucu_id_ciudad = " + ciudad + ";";
            SELECT(queryVAL);
            if (rs.next()) {
                resultado = 2;
            } else {
                String Query = "INSERT INTO sucursales(sucu_nombre,sucu_id_ciudad,sucu_direccion)VALUES(?,?,?)";

                pst = this.con.prepareStatement(Query);
                pst.setString(1, nombre);
                pst.setInt(2, ciudad);
                pst.setString(3, direccion);
                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    /**
     *
     * @param codigo
     * @param nombre
     * @param ciudad
     * @param direccion
     * @return
     */
    public int UPDATE_SUCURSAL(String codigo, String nombre, int ciudad, String direccion) {

        int resultado = 0;

        try {
            //validamos que al momento de actualizar el no este repetido
            String queryVAL = "SELECT sucu_nombre,sucu_id_ciudad FROM sucursales WHERE sucu_nombre = '" + nombre + "' AND sucu_codigo != " + codigo + ";";
            SELECT(queryVAL);
            if (rs.next()) {
                resultado = 2;
            } else {
                String Query = "UPDATE sucursales SET sucu_nombre=?,sucu_id_ciudad=?,sucu_direccion=? WHERE sucu_codigo='" + codigo + "'";

                pst = this.con.prepareStatement(Query);
                pst.setString(1, nombre);
                pst.setInt(2, ciudad);
                pst.setString(3, direccion);
                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    /**
     *
     * @param codigo
     * @param cedula
     * @param nombre
     * @param direccion
     * @param idciudad
     * @return
     */
    public int UPDATE_EMPLEADO(String codigo, int cedula, String nombre, String direccion, int idciudad) {
        int resultado = 0;

        try {
            String queryval = "SELECT cedula, empl_nombre FROM empleado WHERE cedula=" + cedula + " AND empl_codigo !='" + codigo + "'";
            SELECT(queryval);
            if (rs.next()) {
                resultado = 2;
            } else {

                String Query = "UPDATE empleado SET cedula=?,empl_nombre=?,empl_direccion=?,empl_id_ciudad=? WHERE empl_codigo='" + codigo + "'";

                pst = this.con.prepareStatement(Query);
                pst.setInt(1, cedula);
                pst.setString(2, nombre);
                pst.setString(3, direccion);
                pst.setInt(4, idciudad);
                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param codigo
     * @return
     */
    public boolean DELETE_EMPLEADO(String codigo) {

        boolean resultado = false;
        try {
            String Query = "UPDATE empleado SET empl_status=0 WHERE empl_codigo='" + codigo + "'";

            pst = this.con.prepareStatement(Query);

            resultado = pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;

    }

    /**
     *
     * @param code
     * @param cedula
     * @param nombre
     * @param direccion
     * @param idciudad
     * @return
     */
    public int INSERT_EMPLEADO(int code, int cedula, String nombre, String direccion, int idciudad) {

        int resultado = 0;

        try {
            String queryval = "SELECT cedula, empl_nombre FROM empleado WHERE cedula=" + cedula + " OR empl_nombre='" + nombre + "';";
            SELECT(queryval);
            if (rs.next()) {
                resultado = 2;
            } else {
                String Query = "INSERT INTO empleado(empl_codigo,cedula,empl_nombre,empl_direccion,empl_id_ciudad)VALUES(?,?,?,?,?)";

                pst = this.con.prepareStatement(Query);
                pst.setInt(1, code);
                pst.setInt(2, cedula);
                pst.setString(3, nombre);
                pst.setString(4, direccion);
                pst.setInt(5, idciudad);
                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param codigo
     * @param clave
     * @param sucursal
     * @param status
     * @return
     */
    public int UPDATE_SESION_EMPLEADO(int codigo, String clave, int sucursal, int status) {

        int resultado = 0;

        try {

            String Query = "UPDATE empleado SET empl_clave=?,empl_sucursal_cod=?,empl_status=? WHERE empl_codigo=" + codigo;

            pst = this.con.prepareStatement(Query);
            pst.setString(1, clave);
            pst.setInt(2, sucursal);
            pst.setInt(3, status);
            int rest = pst.executeUpdate();
            if (rest == 1) {
                resultado = 1;
            } else {
                resultado = 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    /**
     *
     * @param codigo
     * @param cedula
     * @param nombre
     * @param ciudad
     * @param direccion
     * @param telf
     * @param email
     * @param tipocliente
     * @return
     */
    public int INSERT_CLIENTE(int codigo, String cedula, String nombre, int ciudad, String direccion, String telf, String email, int tipocliente) {

        int resultado = 0;

        try {
            // se valida YA EXISTA UN CLIENTE CON EL MISMO DOCUMENTO O CON EL MISMO NOMBRE
            String Queryvali = "SELECT client_codigo,client_doc,client_nombre FROM clientes WHERE client_codigo !='" + codigo + "' AND client_doc ='" + cedula + "' OR client_nombre='" + nombre + "';";
            rs = con.createStatement().executeQuery(Queryvali);
            if (rs.next()) {
                resultado = 2;
            } else {
                String Query = "INSERT INTO clientes(client_codigo,client_doc,client_nombre,client_id_ciudad,client_direccion,client_telefono,client_email,tipocliente)VALUES(?,?,?,?,?,?,?,?)";

                pst = this.con.prepareStatement(Query);
                pst.setInt(1, codigo);
                pst.setString(2, cedula);
                pst.setString(3, nombre);
                pst.setInt(4, ciudad);
                pst.setString(5, direccion);
                pst.setString(6, telf);
                pst.setString(7, email);
                pst.setInt(8, tipocliente);
                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param codigo
     * @param cedula
     * @param nombre
     * @param ciudad
     * @param direccion
     * @param telf
     * @param email
     * @param tipocliente
     * @return
     */
    public int UPDATE_CLIENTE(String codigo, String cedula, String nombre, int ciudad, String direccion, String telf, String email, int tipocliente) {
        int resultado = 0;

        try {
            //validamos el tipo de cliente donde empresa es 1 y persona natural es 2
            String query = "";
            if (tipocliente == 1) { //si es igual a empresa 
                query = "SELECT client_nombre FROM clientes WHERE client_codigo!='" + codigo + "' AND client_doc='" + cedula + "'";

            } else if (tipocliente == 2) {// si es persona natural solo validamos la cedula ya que los nombres se pueden repetir
                query = "SELECT client_doc FROM clientes WHERE client_codigo!='" + codigo + "' AND client_doc='" + cedula + "'";
            }
            rs = SELECT(query);

            if (rs.next()) {
                //si el resultado es valido retornamos que ya existe este cliente con el mismo doc
                resultado = 3;

            } else {
                String Query = "";
                //validamos si el nombre existe para empresa ,para persona natural se pueden repetir
                if (tipocliente == 1) { //si es igual a empresa 
                    String querye = "SELECT client_nombre FROM clientes WHERE client_codigo!='" + codigo + "' AND client_nombre='" + nombre + "'";
                    rs = SELECT(querye);
                    if (rs.next()) {
                        //si el resultado es valido retornamos que ya existe este cliente con el mismo nombre
                        resultado = 2;

                    } else {
                        Query = "UPDATE clientes SET client_doc=?,client_nombre=?,client_id_ciudad=?,client_direccion=?,client_telefono=?,client_email=?,tipocliente=? WHERE client_codigo='" + codigo + "'";

                        pst = this.con.prepareStatement(Query);

                        pst.setString(1, cedula);
                        pst.setString(2, nombre);
                        pst.setInt(3, ciudad);
                        pst.setString(4, direccion);
                        pst.setString(5, telf);
                        pst.setString(6, email);
                        pst.setInt(7, tipocliente);
                        int rest = pst.executeUpdate();
                        if (rest == 1) {
                            resultado = 1;
                        } else {
                            resultado = 0;
                        }
                    }

                } else {
                    Query = "UPDATE clientes SET client_doc=?,client_nombre=?,client_id_ciudad=?,client_direccion=?,client_telefono=?,client_email=?,tipocliente=? WHERE client_codigo='" + codigo + "'";

                    pst = this.con.prepareStatement(Query);

                    pst.setString(1, cedula);
                    pst.setString(2, nombre);
                    pst.setInt(3, ciudad);
                    pst.setString(4, direccion);
                    pst.setString(5, telf);
                    pst.setString(6, email);
                    pst.setInt(7, tipocliente);
                    int rest = pst.executeUpdate();
                    if (rest == 1) {
                        resultado = 1;
                    } else {
                        resultado = 0;
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param codigo
     * @return
     */
    public boolean DELETE_CLIENTE(String codigo) {

        boolean resultado = false;

        try {

            String Query = "UPDATE  clientes SET client_status = 0 WHERE client_codigo='" + codigo + "'";

            pst = this.con.prepareStatement(Query);

            resultado = pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    /**
     *
     * @param codigo
     * @param moneda
     * @param sucursal
     * @param cliente
     * @param saldo
     * @param clave
     * @return
     */
    public int INSERT_CREAR_CUENTAS(int codigo, int moneda, int sucursal,
            String cliente, Double saldo, String clave) {
        int resultado = 0;

        if (idempl == 0) {
            resultado = 0;

        } else {
            try {

                String Query = "INSERT INTO cuenta(cuent_codigo,cuent_monecodigo,cuent_sucucodigo,cuent_empl_cuenta_creada"
                        + ",cuent__cliente_codigo,cuent_cuenta_saldo,cuent_clave)VALUES(?,?,?,?,?,?,?)";

                pst = this.con.prepareStatement(Query);
                pst.setInt(1, codigo);
                pst.setInt(2, moneda);
                pst.setInt(3, sucursal);
                pst.setInt(4, idempl);
                pst.setString(5, cliente);
                pst.setDouble(6, saldo);
                pst.setString(7, clave);
                int rest = pst.executeUpdate();
                if (rest == 1) {

                    resultado = 1;
                } else {
                    resultado = 0;
                }

            } catch (SQLException ex) {
                Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultado;

    }

    /**
     *
     * @param NumCuenta
     * @return
     */
    public int UPDATE_CANCELAR_CUENTA(String NumCuenta) {
        int resultado = 0;
        try {
            String Query = "UPDATE cuenta SET cuent_estado=? WHERE cuent_codigo=" + NumCuenta;
            pst = this.con.prepareStatement(Query);
            pst.setString(1, "CANCELADO");
            int rest = pst.executeUpdate();
            if (rest == 1) {
                String Querymovi = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe)VALUES(?,?,?,?)";

                pst = this.con.prepareStatement(Querymovi);
                pst.setString(1, NumCuenta);
                pst.setInt(2, idempl);
                pst.setString(3, "05");
                pst.setString(4, "0");
                pst.executeUpdate();
                resultado = 1;
            } else {
                resultado = 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param NumCuenta
     * @param saldo
     * @param intereses
     * @return
     */
    public int UPDATE_DEPOSITO(String NumCuenta, double saldo, double intereses) {
        int resultado = 0;

        try {

            String Query = "UPDATE cuenta SET cuent_cuenta_saldo=cuent_cuenta_saldo+" + saldo + " WHERE cuent_codigo=" + NumCuenta;

            pst = this.con.prepareStatement(Query);

            int rest = pst.executeUpdate();
            if (rest == 1) {
                String Querymovi = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe)VALUES(?,?,?,?)";

                pst = this.con.prepareStatement(Querymovi);
                pst.setString(1, NumCuenta);
                pst.setInt(2, idempl);
                pst.setString(3, "02");
                pst.setDouble(4, saldo);
                pst.executeUpdate();
                if (intereses > 0) {
                    pst = null;
                    pst = this.con.prepareStatement(Querymovi);
                    pst.setString(1, NumCuenta);
                    pst.setInt(2, idempl);
                    pst.setString(3, "08");
                    pst.setDouble(4, intereses);
                    pst.executeUpdate();
                }
                resultado = 1;

            } else {
                resultado = 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param NumCuenta
     * @param saldo
     * @param interes
     * @return
     */
    public int UPDATE_RETIRO(String NumCuenta, double saldo, int interes) {
        int resultado = 0;

        try {

            String Query = "UPDATE cuenta SET cuent_cuenta_saldo=cuent_cuenta_saldo-" + saldo + " WHERE cuent_codigo=" + NumCuenta;

            pst = this.con.prepareStatement(Query);

            int rest = pst.executeUpdate();
            if (rest == 1) {

                String Querymovi = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe)VALUES(?,?,?,?)";

                pst = this.con.prepareStatement(Querymovi);
                pst.setString(1, NumCuenta);
                pst.setInt(2, idempl);
                pst.setString(3, "04");
                pst.setDouble(4, saldo);
                int rss = pst.executeUpdate();

                if (rss == 1) {

                    String Querymovis = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe)VALUES(?,?,?,?)";
                    pst = this.con.prepareStatement(Querymovis);
                    pst.setString(1, NumCuenta);
                    pst.setInt(2, idempl);
                    pst.setString(3, "08");
                    pst.setInt(4, interes);
                    pst.executeUpdate();

                }

                resultado = 1;
            } else {
                resultado = 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param NumCuenta
     * @param NumcuentaDestino
     * @param mon
     * @param saldo
     * @return
     */
    public int UPDATE_TRANSFERENCIA(String NumCuenta, String NumcuentaDestino, String mon, float saldo) {
        int resultado = 0;

        try {
            //VALIDAMOS QUE EL NUMERO DE CUENTA ORIGEN SEA DIFERENTE DE LA CUENTA DESTINO
            //AL IGUAL QUE LA CUENTA DE REFERENCIA 
            if (NumCuenta.equals(NumcuentaDestino)) {
                resultado = 2;
            } else {
                //ACTUALIZA LA CUENTA DESTINO
                String Query = "UPDATE cuenta SET cuent_cuenta_saldo=cuent_cuenta_saldo+" + saldo + " WHERE cuent_codigo=" + NumcuentaDestino;
                pst = this.con.prepareStatement(Query);
                int rest = pst.executeUpdate();
                if (rest == 1) {

                    double interes = 0;

                    //VALIDAMOS SI EL USUARIO ES DE INTERNET
                    if (idempl != 9999) {
                        //cobro ITF
                        Double interesTransacciones = 0.08;
                        interes = (interesTransacciones * saldo) / 100;
                        String Querymoviinte = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe,movi_cuentareferencia)VALUES(?,?,?,?,?)";
                        pst = this.con.prepareStatement(Querymoviinte);
                        pst.setString(1, NumCuenta);
                        pst.setInt(2, idempl);
                        pst.setString(3, "11");
                        pst.setDouble(4, interes);
                        pst.setString(5, NumcuentaDestino);
                        pst.executeUpdate();
                    }
                    //CIERRO LA CONSULTA DEL PREPARESTATEMENT
                    pst = null;
                    //ACTUALIZA LA CUENTA ORIGEN
                    String QueryDes = "UPDATE cuenta SET cuent_cuenta_saldo=cuent_cuenta_saldo-" + (saldo - interes) + " WHERE cuent_codigo=" + NumCuenta;
                    pst = this.con.prepareStatement(QueryDes);
                    pst.executeUpdate();

                    //INSERTAMOS EL SEGUNDO MOVIMIENTO DE LA CUENTA 
                    String Querymovi = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe,movi_cuentareferencia)VALUES(?,?,?,?,?)";
                    //MOVIMIENTO TRANSFERENCIA CUENTA ORIGEN
                    pst = this.con.prepareStatement(Querymovi);
                    pst.setString(1, NumCuenta);
                    pst.setInt(2, idempl);
                    pst.setString(3, "1");
                    pst.setFloat(4, saldo);
                    pst.setString(5, NumcuentaDestino);
                    int m = pst.executeUpdate();
                    if (m == 1) {
                        pst = null;
                        String QuerymoviCUENTA2 = "INSERT INTO movimiento(movi_cuencodigo,movi_emplcodigo,movi_tipocodigo,movi_importe,movi_cuentareferencia)VALUES(?,?,?,?,?)";
                        //MOVIMIENTO TRABFERENCIA CUENTA DESTINO
                        pst = this.con.prepareStatement(QuerymoviCUENTA2);
                        pst.setString(1, NumcuentaDestino);
                        pst.setInt(2, idempl);
                        pst.setString(3, "7");
                        pst.setFloat(4, saldo);
                        pst.setString(5, NumCuenta);
                        pst.executeUpdate();

                    }

                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param emplcodigo
     * @param rol
     * @return
     */
    public int UPDATE_USUARIO(String emplcodigo, int rol) {
        int resultado = 0;

        try {

            String Query = "UPDATE empleado SET rol=? WHERE empl_codigo='" + emplcodigo + "'";
            pst = this.con.prepareStatement(Query);
            pst.setInt(1, rol);
            int rest = pst.executeUpdate();
            if (rest == 1) {
                resultado = 1;
            } else {
                resultado = 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;
    }

    /**
     *
     * @param idm
     * @param r
     * @param v
     * @param a
     * @param c
     * @param e
     * @return
     */
    public int INSERT_PERMISOS(int idm, int r, int v, int c, int e, int a) {
        int resultado = 0;

        try {
            String Queryvali = "SELECT id_modulo,idRol FROM permisos WHERE id_modulo =" + idm + " AND idRol =" + r;
            rs = con.createStatement().executeQuery(Queryvali);
            if (rs.next()) {
                String Query = "UPDATE permisos SET v=?,c=?,e=?,a=? WHERE id_modulo=" + idm + " AND   idRol=" + r;
                pst = this.con.prepareStatement(Query);
                pst.setInt(1, v);
                pst.setInt(2, c);
                pst.setInt(3, e);
                pst.setInt(4, a);
                pst.executeUpdate();
                resultado = 2;
            } else {
                String Query = "INSERT INTO permisos(id_modulo,idRol,v,c,e,a)VALUES(?,?,?,?,?,?)";

                pst = this.con.prepareStatement(Query);
                pst.setInt(1, idm);
                pst.setInt(2, r);
                pst.setInt(3, v);
                pst.setInt(4, c);
                pst.setInt(5, e);
                pst.setInt(6, a);

                int rest = pst.executeUpdate();
                if (rest == 1) {
                    resultado = 1;
                } else {
                    resultado = 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    public int INSERT_CIUDADES(String ciudad) {
        int resultado = 0;

        try {

            String Query = "INSERT INTO ciudad(nombre_ciudad)VALUES(?)";

            pst = this.con.prepareStatement(Query);
            pst.setString(1, ciudad);

            int rest = pst.executeUpdate();
            if (rest == 1) {
                resultado = 1;
            } else {
                resultado = 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultado;

    }

    private int cobroMes(String fechaactual, String fechaCobro) {
        int resultado = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fecha1 = LocalDate.parse(fechaCobro, formatter);
        LocalDate fecha2 = LocalDate.parse(fechaactual, formatter);
        if (fecha1.equals(fecha2)) {
            resultado = 1;
        }

        return resultado;
    }
}
