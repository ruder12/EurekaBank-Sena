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
public class ModalCancelarCuentaController implements Initializable {
    
    @FXML
    private JFXTextField NumCuenta;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btnguardar;
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion notif;
   

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
    private void validpass(KeyEvent event) {
        if (password.getText().length() > 20) {
            event.consume();
        }
    }
    
    private void btncancelarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void btnguardarAction(ActionEvent event) {
        if (password.getText().equals("")) {
            notif.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);
            
        } else {
            try {
                String Query = "SELECT cuent_estado FROM cuenta WHERE cuent_codigo=" + NumCuenta.getText().trim() + " AND cuent_estado !='CANCELADO';";
                rst = consultas.SELECT(Query);
                if (rst.next()) {
                    if (password.getText().trim().equals(VariableSesion.getClavetemporal())) {
                     int rest = consultas.UPDATE_CANCELAR_CUENTA(NumCuenta.getText().trim());
                    switch (rest) {
                        case 1:
                            notif.Confirmar("Muy Bien", "Cuenta Cancelada Correctamente", Pos.TOP_CENTER);
                            
                            break;
                        default:
                            notif.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Cancelar la Cuenta", Pos.TOP_CENTER);
                            break;
                    }
                    }else{
                         notif.Error("Error! Clave incorrecta", "Verifique su Clave, inicia Sesion Nuevamente", Pos.TOP_CENTER);
                    }
                   
                    
                } else {
                    notif.Info("No se Puede Cancelar", "Esta Cuenta ya se Encuentra Cancelada", Pos.TOP_CENTER);
                }
                password.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(ModalCancelarCuentaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
  public void closeApp(String cod,String tipocliente){
         
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
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            }
        
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ModalCrearCuentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
  }
   
}
