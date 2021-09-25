/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Recursos;
import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ConsultasMySQL;
import utils.VariableSesion;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalRetiroController implements Initializable {

    @FXML
    private JFXTextField NumCuenta;
    @FXML
    private JFXPasswordField pasword;
    @FXML
    private JFXButton btnguardar;
    @FXML
    private JFXTextField Retiro;
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion noti;
    int idempl = VariableSesion.getcodemple();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultas = new ConsultasMySQL();
        noti = new Notificacion();
    }

    /**
     *
     * @param NumCuenta
     */
    public void datosuser(String NumCuenta) {
        this.NumCuenta.setText(NumCuenta);
    }

    @FXML
    private void validpass(KeyEvent event) {
        try {

            if (pasword.getText().length() > 20) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    @FXML
    private void btnguardarAction(ActionEvent event) {
        if (pasword.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            try {

                String Query = "SELECT cu.cuent_monecodigo,cu.cuent_cuenta_saldo,cu.cuent_estado,COUNT(cu.cuent_codigo) AS contador "
                        + "FROM cuenta AS cu "
                        + "INNER JOIN movimiento AS mo "
                        + "ON cu.cuent_codigo = mo.movi_cuencodigo "
                        + "WHERE cu.cuent_codigo='" + NumCuenta.getText().trim() + "' AND cu.cuent_estado !='CANCELADO';";

                rst = consultas.SELECT(Query);
                if (rst.next()) {
                    String estado = rst.getString("cuent_estado");
                    if (estado != null) {
                        if (pasword.getText().trim().equals(VariableSesion.getClavetemporal())) {
                            int cont = rst.getInt("contador");
                            int moneda = rst.getInt("cuent_monecodigo");
                            String retirointeres = Retiro.getText().trim();
                            String punto = retirointeres.replace(".", "");
                            String coma = punto.replace(",", "");
                            int retirointere = Integer.parseInt(coma);

                            int interes = 0;
                            //VALIDAMOS SI EL USUARIO ES DE INTERNET
                            if (idempl != 9999) {
                                // despues que la cuenta supera los 15 movimiento se cobra el cargo por mantenimiento
                                if (moneda == 2 && cont > 14) {
                                    interes = 60;
                                    retirointere = (retirointere - interes);
                                } else if (moneda == 1 && cont > 14) {
                                    interes = 2000;
                                    retirointere = (retirointere - interes);
                                }
                            }
                            int saldobd = rst.getInt("cuent_cuenta_saldo");
                            if (saldobd < retirointere) {
                                noti.Info("No se Puede Retirar", "Esta Cuenta no Tiene los Recursos para Realizar esta Operacion", Pos.TOP_CENTER);
                                return;
                            }
                            int rest = consultas.UPDATE_RETIRO(NumCuenta.getText().trim(), retirointere, interes);
                            switch (rest) {
                                case 1:
                                    noti.Confirmar("Muy Bien", "Retiro Exitoso", Pos.TOP_CENTER);
                                    break;
                                default:
                                    noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Retirar ", Pos.TOP_CENTER);
                                    break;
                            }
                        } else {
                            noti.Error("Error! Clave incorrecta", "Verifique su Clave, inicia Sesion Nuevamente", Pos.TOP_CENTER);
                        }
                    } else {
                        noti.Info("No se Puede Retirar", "Esta Cuenta ya se Encuentra Cancelada", Pos.TOP_CENTER);
                    }
                }
                pasword.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(ModalRetiroController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void validRetiro(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c)) {
            event.consume();
        }
    }

    private String format(int valor) {
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorFormateado = formato.format(valor);
        return valorFormateado;
    }

    @FXML
    private void retirarexit(MouseEvent event) {
        try {
            if (!Retiro.getText().equals("")) {
                int val = Integer.parseInt(Retiro.getText());
                Retiro.setText(format(val));
            }
        } catch (NumberFormatException e) {

        }
    }

    public void closeApp(String cod, String tipocliente) {

        try {
            String query = "SELECT *  "
                    + "FROM clientes cli "
                    + "INNER JOIN ciudad AS c "
                    + "ON cli.client_id_ciudad = c.id_ciudad "
                    + "WHERE client_codigo = '" + cod + "'";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                String co = rst.getString("client_codigo");
                String cc = rst.getString("client_doc");
                String nom = rst.getString("client_nombre");
                String cd = rst.getString("nombre_ciudad");

                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModalGestionCuentas.fxml"));
                Parent parent = (Parent) loader.load();
                ModalGestionCuentasController mgc = (ModalGestionCuentasController) loader.getController();
                mgc.datosuser(co, cc, nom, cd, tipocliente);
                Scene scene = new Scene(parent);
                scene.setFill(Color.TRANSPARENT);

                newStage.setScene(scene);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.TRANSPARENT);

                newStage.centerOnScreen();
                newStage.show();
            }

        } catch (IOException | SQLException ex) {
            Logger.getLogger(ModalCrearCuentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
