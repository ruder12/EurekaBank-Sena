/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ConsultasMySQL;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalConfigEmpleadoController implements Initializable {

    @FXML
    private JFXTextField codigo;
    @FXML
    private JFXPasswordField pass;
    @FXML
    private JFXComboBox<String> sucursal;
    @FXML
    private JFXButton guardarSesion;
    @FXML
    private JFXComboBox<String> estado;
    ConsultasMySQL consultas;

    ResultSet rst = null;
    Notificacion noti;
    public static AnchorPane mainRootPane;
    EmpleadosController empleadoController;

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
        setsucursalCombo();
        setstatusCombo();

    }

    /**
     *
     * @param emplecontrol
     * @param code
     * @param passw
     * @param sucursa
     * @param status
     */
    public void datosuser(EmpleadosController emplecontrol, String code, String passw, String sucursa, int status) {
        empleadoController = emplecontrol;
        codigo.setText(code);
        pass.setText(passw);
        sucursal.getSelectionModel().select(sucursa);
        estado.getSelectionModel().select(status);
    }

    private void setstatusCombo() {
        estado.getItems().add(0, "Inactivo");
        estado.getItems().add(1, "Activo");
    }

    private void setsucursalCombo() {
        try {
            String query = "SELECT sucu_codigo,sucu_nombre FROM sucursales";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                sucursal.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModalConfigEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

  

    @FXML
    private void guardar(ActionEvent event) {
        if (codigo.getText().equals("") || pass.getText().equals("")
                || sucursal.getSelectionModel().getSelectedItem().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            String sucu = sucursal.getSelectionModel().getSelectedItem();
            String[] su = sucu.split("-");
            int idsucursal = Integer.valueOf(su[0]);
            int cod = Integer.valueOf(codigo.getText().trim());
            int rest = consultas.UPDATE_SESION_EMPLEADO(cod, pass.getText().trim(), idsucursal, estado.getSelectionModel().getSelectedIndex());
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Actualizados Correctamente", Pos.TOP_CENTER);
                    empleadoController.cargartablaempleado();
                    close(event);
                    break;
                default:
                    noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Actualizar la Informacion", Pos.TOP_CENTER);
                    break;
            }
       
        }
    }

    @FXML
    private void CerrarModal(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void infousuario(MouseEvent event) {
        noti.InfoAyuda("Usuario", "Este Campo solo Recibe Max 10 Caracteres", Pos.TOP_CENTER);
    }

    @FXML
    private void infopasword(MouseEvent event) {
        noti.InfoAyuda("Contraseña", "Este Campo solo Recibe Max 20 Caracteres", Pos.TOP_CENTER);
    }

    @FXML
    private void infoestado(MouseEvent event) {
         noti.InfoAyuda("Estado", "si marca el estado inactivo es como Eliminar el Usuario", Pos.TOP_CENTER);
    }

    @FXML
    private void infosucursal(MouseEvent event) {
         noti.InfoAyuda("Sucursal", "Al seleccionar la sucursal se asigna la ciudad al empleado", Pos.TOP_CENTER);
    }

    @FXML
    private void passwordtype(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (pass.getText().length()>19) {
            event.consume();
        }
    }

}
