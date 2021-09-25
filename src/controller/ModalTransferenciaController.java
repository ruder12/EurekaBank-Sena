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
import java.sql.PreparedStatement;
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
import javafx.scene.Node;
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
public class ModalTransferenciaController implements Initializable {

    @FXML
    private JFXTextField NumCuentaOrigen;
    @FXML
    private JFXButton btnguardar;
    @FXML
    private JFXTextField MontoTranferir;
    @FXML
    private JFXTextField NumCuentaDestino;
    @FXML
    private JFXPasswordField password;
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion noti;
    PreparedStatement pst = null;

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
        this.NumCuentaOrigen.setText(NumCuenta);
    }

    private void btncancelarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage.hide();
    }

    @FXML
    private void btnguardarAction(ActionEvent event) {
        if ((password.getText().equals("")) || (NumCuentaDestino.getText().equals("")) || (MontoTranferir.getText().equals(""))) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            try {
                String Query = "SELECT cuent_monecodigo,cuent_cuenta_saldo,cuent_estado FROM cuenta WHERE cuent_codigo=" + NumCuentaOrigen.getText().trim() + " AND cuent_estado !='CANCELADO';";
                rst = consultas.SELECT(Query);
                if (rst.next()) {
                    int saldobd = rst.getInt("cuent_cuenta_saldo");
                    String moned = rst.getString("cuent_monecodigo");
                    String rets = MontoTranferir.getText().trim();
                     String punto = rets.replace(".", "");
                        String coma = punto.replace(",", "");
                    int ret = Integer.parseInt(coma);
                    if (saldobd < ret) {
                        noti.Info("No se Puede Tranferir", "Esta Cuenta no Tiene los Recursos para Realizar esta Operacion", Pos.TOP_CENTER);
                        return;
                    }
                    rst = null;
                    //SELECCIONAMO LA CUENTA DESTINO
                    String QueryDestino = "SELECT cuent_monecodigo,cuent_cuenta_saldo,cuent_estado FROM cuenta WHERE cuent_codigo=" + NumCuentaDestino.getText().trim() + " AND cuent_estado !='CANCELADO';";
                    rst = consultas.SELECT(QueryDestino);
                    if (rst.next()) {
                        //VALIDAMOS LA CLAVE DE ACCESO
                        if (password.getText().trim().equals(VariableSesion.getClavetemporal())) {
                            String monedcode = rst.getString("cuent_monecodigo");
                            //VERIFICAMOS QUE LA MODEDA SEA VALIDAD PARA LA TRANFERENCIA
                            if (!monedcode.equals(moned)) {
                                noti.Info("No se Puede Tranferir", "Los Tipos de Moneda No son Compatibles Contacte al Adminitrador", Pos.TOP_CENTER);
                                return;
                            }
                            int rest = consultas.UPDATE_TRANSFERENCIA(NumCuentaOrigen.getText().trim(), NumCuentaDestino.getText().trim(), moned, ret);
                            switch (rest) {
                                case 1:

                                    noti.Confirmar("Muy Bien", "Tranferencia Exitosa", Pos.TOP_CENTER);

                                    break;
                                case 2:
                                    noti.Info("Aviso Importante", "No se puede Tranferir al Mismo numero de cuenta", Pos.TOP_CENTER);

                                    break;
                                default:
                                    noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Realizar la Transferencia", Pos.TOP_CENTER);
                                    break;
                            }
                        } else {
                            noti.Error("Error! Clave incorrecta", "Verifique su Clave, inicia Sesion Nuevamente", Pos.TOP_CENTER);
                        }
                    } else {
                        noti.Info("No se Puede Tranferir", "La cuenta destino se Encuentra Cancelada", Pos.TOP_CENTER);
                    }

                } else {
                    noti.Info("No se Puede Tranferir", "La cuenta origen se Encuentra Cancelada", Pos.TOP_CENTER);
                }
                password.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(ModalTransferenciaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void validMontoTranferir(KeyEvent event) {
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

    @FXML
    private void validpass(KeyEvent event) {
        try {

            if (password.getText().length() > 20) {
                event.consume();
            }

        } catch (Exception e) {

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

    @FXML
    private void numCuentaDestino(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c)) {
            event.consume();
        }
    }
   @FXML
    private void mouseexit(MouseEvent event) {
        try{
            if (!MontoTranferir.getText().equals("")) {
           int val= Integer.parseInt(MontoTranferir.getText());
            MontoTranferir.setText(format(val));
        }
        }catch(NumberFormatException e){
            
        }
  
    }
}
