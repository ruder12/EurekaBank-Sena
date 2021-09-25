/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ConsultasMySQL;
import utils.Bitacora;
import utils.bitacoraAsignaciones;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class BitacoraModalAsignacionesController implements Initializable {

    @FXML
    private FontAwesomeIconView cerrar;
    @FXML
    private JFXTextField Buscar;
    @FXML
    private TableView<bitacoraAsignaciones> tableviewsmovi;

    ObservableList<bitacoraAsignaciones> historialMvi = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;
    @FXML
    private TableColumn<bitacoraAsignaciones, String> empleado;
    @FXML
    private TableColumn<bitacoraAsignaciones, String> sucursal;
    @FXML
    private TableColumn<bitacoraAsignaciones, String> fechaalta;
    @FXML
    private TableColumn<bitacoraAsignaciones, String> fechabaja;
    @FXML
    private TableColumn<bitacoraAsignaciones, Integer> code;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultas = new ConsultasMySQL();
        cargartablaCuentas();
    }

    public void cargartablaCuentas() {
        try {
            String query = "SELECT asi.asig_codigo,em.empl_nombre,su.sucu_nombre,asi.asig_fecha_alta,asi.asig_fecha_baja  "
                    + "FROM asignaciones AS asi  "
                    + "INNER JOIN empleado AS em  "
                    + "ON asi.asig_empl_codigo = em.empl_codigo "
                    + "INNER JOIN sucursales AS su  "
                    + "ON asi.asig_sucur_cod = su.sucu_codigo  "
                    + "ORDER BY asi.asig_codigo ASC LIMIT 40";

            rst = consultas.SELECT(query);

            while (rst.next()) {

                historialMvi.add(new bitacoraAsignaciones(rst.getInt("asig_codigo"), rst.getString("empl_nombre"), rst.getString("sucu_nombre"),
                        rst.getString("asig_fecha_alta"), rst.getString("asig_fecha_baja")));
            }
            tableviewsmovi.setItems(historialMvi);
            code.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, Integer>("codigo"));
            empleado.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("empleado"));
            sucursal.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("sucursal"));
            fechaalta.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("fechaalta"));
            fechabaja.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("fechabaja"));

        } catch (SQLException ex) {
            Logger.getLogger(BitacoraModalAsignacionesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    public void cargartablaCuentaslistar(String Codigo) {

        try {

           String query = "SELECT asi.asig_codigo,em.empl_nombre,su.sucu_nombre,asi.asig_fecha_alta,asi.asig_fecha_baja  "
                    + "FROM asignaciones AS asi  "
                    + "INNER JOIN empleado AS em  "
                    + "ON asi.asig_empl_codigo = em.empl_codigo "
                    + "INNER JOIN sucursales AS su  "
                    + "ON asi.asig_sucur_cod = su.sucu_codigo  "
                    + "WHERE ";

            rst = consultas.SELECT(query);

            while (rst.next()) {

                historialMvi.add(new bitacoraAsignaciones(rst.getInt("asig_codigo"), rst.getString("empl_nombre"), rst.getString("sucu_nombre"),
                        rst.getString("asig_fecha_alta"), rst.getString("asig_fecha_baja")));
            }
            tableviewsmovi.setItems(historialMvi);
            code.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, Integer>("codigo"));
            empleado.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("empleado"));
            sucursal.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("sucursal"));
            fechaalta.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("fechaalta"));
            fechabaja.setCellValueFactory(new PropertyValueFactory<bitacoraAsignaciones, String>("fechabaja"));

        } catch (SQLException ex) {
            Logger.getLogger(BitacoraModalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

   @FXML
    private void cerrarclick(MouseEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage.hide();
    }

    @FXML
    private void buscarTYPE(KeyEvent event) {
         tableviewsmovi.getItems().clear();
        cargartablaCuentaslistar(Buscar.getText());
    }

}
