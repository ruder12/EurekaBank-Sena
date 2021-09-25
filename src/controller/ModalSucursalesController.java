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
import utils.Sucursales;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalSucursalesController implements Initializable {

    @FXML
    private JFXTextField nombreSucursal;
    @FXML
    private FontAwesomeIconView close;
    @FXML
    private TableView<Sucursales> tablesucursales;
    @FXML
    private TableColumn<Sucursales, String> idsucursal;
    @FXML
    private TableColumn<Sucursales, String> sucursal;
    @FXML
    private TableColumn<Sucursales, String> ciudad;
    @FXML
    private TableColumn<Sucursales, String> direccion;

    ObservableList<Sucursales> Sucursales = FXCollections.observableArrayList();

    ConsultasMySQL consultas;
    ResultSet rst = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultas = new ConsultasMySQL();
        cargartablasucursal();
    }

    public void cargartablasucursal() {

        try {

            String query = "SELECT su.sucu_codigo,su.sucu_nombre,c.nombre_ciudad,su.sucu_direccion  "
                    + "FROM sucursales AS su  "
                    + "INNER JOIN ciudad AS c  "
                    + "ON su.sucu_id_ciudad = c.id_ciudad  "
                    + "WHERE su.sucu_codigo != 3 "
                    + "ORDER BY su.sucu_codigo DESC LIMIT 50;";

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                String idciudad = "";
                Sucursales.add(new Sucursales(rst.getInt("sucu_codigo"), rst.getString("sucu_nombre"),
                        idciudad, rst.getString("nombre_ciudad"), rst.getString("sucu_direccion"), 0));
            }
            tablesucursales.setItems(Sucursales);
            idsucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("codigo"));
            sucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("nombre_sucursal"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("direccion"));

        } catch (SQLException ex) {
            Logger.getLogger(ModalSucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    public void cargartablasucursalListar(String buscar) {

        try {

            String query = "SELECT su.sucu_codigo,su.sucu_nombre,c.nombre_ciudad,su.sucu_direccion  "
                    + "FROM sucursales AS su  "
                    + "INNER JOIN ciudad AS c "
                    + "ON su.sucu_id_ciudad = c.id_ciudad  "
                    + "WHERE su.sucu_nombre LIKE'" + buscar + "%'  AND su.sucu_codigo != 3  "
                    + "ORDER BY su.sucu_codigo DESC LIMIT 50;";

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                String idciudad = "";
                Sucursales.add(new Sucursales(rst.getInt("sucu_codigo"), rst.getString("sucu_nombre"),
                        idciudad, rst.getString("nombre_ciudad"), rst.getString("sucu_direccion"), 0));
            }
            tablesucursales.setItems(Sucursales);
            idsucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("codigo"));
            sucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("nombre_sucursal"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("direccion"));

        } catch (SQLException ex) {
            Logger.getLogger(ModalSucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    @FXML
    private void nombreciudadAction(KeyEvent event) {
        tablesucursales.getItems().clear();
        cargartablasucursalListar(nombreSucursal.getText());
    }

    @FXML
    private void closecliken(MouseEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
