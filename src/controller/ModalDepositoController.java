/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Recursos;
import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
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
public class ModalDepositoController implements Initializable {

    @FXML
    private JFXTextField NumCuenta;
    @FXML
    private JFXButton btnguardar;
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion notif;
    @FXML
    private JFXTextField deposito;
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
        notif = new Notificacion();

    }

    /**
     *
     * @param NumCuenta
     */
    public void datosuser(String NumCuenta) {
        this.NumCuenta.setText(NumCuenta);
    }

    @FXML
    private void btnguardarAction(ActionEvent event) {
        if (deposito.getText().equals("")) {
            notif.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            try {
                String Query = "SELECT cu.cuent_monecodigo,cu.cuent_cuenta_saldo,cu.cuent_estado,COUNT(cu.cuent_codigo) AS contador "
                        + "FROM cuenta AS cu "
                        + "INNER JOIN movimiento AS mo "
                        + "ON cu.cuent_codigo = mo.movi_cuencodigo "
                        + "WHERE cu.cuent_codigo='" + NumCuenta.getText().trim() + "' AND cu.cuent_estado !='CANCELADO';";
                rst = consultas.SELECT(Query);
                if (rst.next()) {
                    //valido que el estado no sea nulo,si es nulo no 
                    //existe el dato o la cuenta esta cancelada
                    String estado = rst.getString("cuent_estado");
                    if (estado != null) {
                        int cont = rst.getInt("contador");
                        int moneda = rst.getInt("cuent_monecodigo");
                        String depositoconinteres = deposito.getText().trim();
                        String punto = depositoconinteres.replace(".", "");
                        String coma = punto.replace(",", "");
                        int depositoint = Integer.parseInt(coma);
                        int interes = 0;
                        if (idempl != 9999) {

                            if (moneda == 2 && cont > 14) {
                                interes = 60;
                                depositoint = depositoint - interes;
                            } else if (moneda == 1 && cont > 14) {
                                interes = 2000;
                                depositoint = depositoint - interes;
                            }
                        }
                        int rest = consultas.UPDATE_DEPOSITO(NumCuenta.getText().trim(), depositoint, interes);
                        switch (rest) {
                            case 1:
                                notif.Confirmar("Muy Bien", "Se Realizo un Deposito a tu cuenta", Pos.TOP_CENTER);

                                break;
                            default:
                                notif.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Actualizar la Informacion", Pos.TOP_CENTER);
                                break;

                        }
                    } else {
                        notif.Info("No se Puede Depositar", "Esta Cuenta ya se Encuentra Cancelada", Pos.TOP_CENTER);
                    }
                }
                deposito.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(ModalDepositoController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void validdeposito(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c)) {
            event.consume();
        }
    }

    private String format(double valor) {
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorFormateado = formato.format(valor);
        return valorFormateado;
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
                //newStage.setResizable(false);
                newStage.centerOnScreen();
                newStage.show();
            }

        } catch (IOException | SQLException ex) {
            Logger.getLogger(ModalCrearCuentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void mouseexit(MouseEvent event) {
        try {
            if (!deposito.getText().equals("")) {
                int val = Integer.parseInt(deposito.getText());
                deposito.setText(format(val));
            }
        } catch (NumberFormatException e) {

        }
    }
}
