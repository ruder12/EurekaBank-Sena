/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ConsultasMySQL;
import utils.VariableSesion;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class EurekaBanckController implements Initializable {

    @FXML
    private JFXButton btnmenu;
    @FXML
    private JFXButton btnempleados;
    @FXML
    private JFXButton btngestioncuentas;
    @FXML
    private Pane paneles;
    @FXML
    private JFXButton btnsucursal;
    @FXML
    private BorderPane bp;
    @FXML
    private JFXButton btnconfiguraciones;
    @FXML
    private Label namerol;
    @FXML
    private Label labelidrols;
    private Label codEmpleado;

    ConsultasMySQL consultas;
    ResultSet rst = null;

    Notificacion noti;
    @FXML
    private Label labelmsj;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultas = new ConsultasMySQL();
        labelmsj.setText(VariableSesion.getMsj());
    }

    @FXML
    private void actmenu(ActionEvent event) {
        bp.setCenter(paneles);

    }

    @FXML
    private void actsucursales(ActionEvent event) {
        int id = Integer.parseInt(this.labelidrols.getText());
        loadpageSucursales("Sucursales", id);

    }

    @FXML
    private void actEmpleado(ActionEvent event) {
        int id = Integer.parseInt(this.labelidrols.getText());
        loadpageEmpleado("Empleados", id);
    }

    @FXML
    private void actgestioncuentas(ActionEvent event) {
        int id = Integer.parseInt(this.labelidrols.getText());
        loadpagegestioncuentas("GestionCuentas", id);
    }

    @FXML
    private void btnconfiguracionesAction(ActionEvent event) {
        int id = Integer.parseInt(this.labelidrols.getText());
        loadpageconfiguraciones("Configuraciones", id);
    }

    private void loadpageSucursales(String page, int idrol) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + page + ".fxml"));
            root = (Parent) loader.load();
            SucursalesController sc = (SucursalesController) loader.getController();
            sc.confiniciopermiso(idrol);
        } catch (IOException ex) {
            Logger.getLogger(EurekaBanckController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);
    }

    private void loadpageEmpleado(String page, int idrol) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + page + ".fxml"));
            root = (Parent) loader.load();
            EmpleadosController ec = (EmpleadosController) loader.getController();
            ec.confiniciopermiso(idrol);
        } catch (IOException ex) {
            Logger.getLogger(EurekaBanckController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);
    }

    private void loadpagegestioncuentas(String page, int idrol) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + page + ".fxml"));
            root = (Parent) loader.load();
            GestionCuentasController gc = (GestionCuentasController) loader.getController();
            gc.confiniciopermiso(idrol);
        } catch (IOException ex) {
            Logger.getLogger(EurekaBanckController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);
    }

    private void loadpageconfiguraciones(String page, int idrol) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + page + ".fxml"));
            root = (Parent) loader.load();
            ConfiguracionesController cc = (ConfiguracionesController) loader.getController();
            cc.confiniciopermiso(idrol);
        } catch (IOException ex) {
            Logger.getLogger(EurekaBanckController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);
    }

    /**
     *
     * @param idtrol
     * @param userrol
     */
    public void confiniciopermiso(int idtrol, String userrol) {
        namerol.setText(userrol);
        this.labelidrols.setText(String.valueOf(idtrol));
        try {
            String query = "SELECT id_modulo,idRol,v,c,e,a FROM permisos WHERE idRol = " + idtrol;

            rst = consultas.SELECT(query);

            while (rst.next()) {
                int m = rst.getInt("id_modulo");
                int r = rst.getInt("idRol");

                int v = rst.getInt("v");
                int c = rst.getInt("c");
                int e = rst.getInt("e");
                int a = rst.getInt("a");
                if (m == 1) {//sucursales
                    if (v == 1) {
                        btnsucursal.setVisible(true);
                    } else {
                        btnsucursal.setVisible(false);
                    }
                }
                if (m == 2) {//Empleados
                    if (v == 1) {
                        btnempleados.setVisible(true);
                    } else {
                        btnempleados.setVisible(false);
                    }
                }
                if (m == 3) {//Gestion de Cuentas
                    if (v == 1) {
                        btngestioncuentas.setVisible(true);
                    } else {
                        btngestioncuentas.setVisible(false);
                    }
                }
                if (m == 4) {//Configuraciones
                    if (v == 1) {
                        btnconfiguraciones.setVisible(true);
                    } else {
                        btnconfiguraciones.setVisible(false);
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void closeApp(){
        try {
            btnsucursal.getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Scene scene = new Scene(root);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/utils/img/user_menu.png")));
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EurekaBanckController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
