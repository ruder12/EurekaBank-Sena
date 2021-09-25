/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ConsultasMySQL;
import utils.Notificacion;
import utils.paises;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalCiudadesController implements Initializable {

    @FXML
    private JFXTextField nombreciudad;
    @FXML
    private FontAwesomeIconView close;
    @FXML
    private JFXButton btnagregar;
    @FXML
    private TableColumn<paises, Integer> idpais;
    @FXML
    private TableColumn<paises, String> pais;
    ObservableList<paises> paises = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Notificacion noti;
    @FXML
    private TableView<paises> tablepaises;

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
        cargartablasucursal();
    }

    public void cargartablasucursal() {

        try {

            String query = "SELECT * FROM ciudad WHERE id_ciudad!=33;";

            rst = consultas.SELECT(query);

            while (rst.next()) {
                paises.add(new paises(rst.getInt("id_ciudad"), rst.getString("nombre_ciudad")));
            }
            tablepaises.setItems(paises);
            idpais.setCellValueFactory(new PropertyValueFactory<paises, Integer>("pais"));
            pais.setCellValueFactory(new PropertyValueFactory<paises, String>("nombrepais"));

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    public void ListarTabla(String buscar) {

        try {

            String query = "SELECT * FROM ciudad WHERE id_ciudad!=33 "
                    + "AND nombre_ciudad LIKE'" + buscar + "%';";

            rst = consultas.SELECT(query);

            while (rst.next()) {
                paises.add(new paises(rst.getInt("id_ciudad"), rst.getString("nombre_ciudad")));
            }
            tablepaises.setItems(paises);
            idpais.setCellValueFactory(new PropertyValueFactory<paises, Integer>("pais"));
            pais.setCellValueFactory(new PropertyValueFactory<paises, String>("nombrepais"));

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }
    }


    @FXML
    private void closecliken(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnagregarAction(ActionEvent event) {
        if (nombreciudad.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            try {
                String Query = "SELECT nombre_ciudad FROM ciudad WHERE nombre_ciudad !='" + nombreciudad.getText().trim() + "';";
                rst = consultas.SELECT(Query);
                if (rst.next()) {

                    int rest = consultas.INSERT_CIUDADES(nombreciudad.getText().trim());
                    switch (rest) {
                        case 1:
                            noti.Confirmar("Muy Bien", "Se Registro la Ciudad", Pos.TOP_CENTER);

                            break;
                        default:
                            noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Registrar ", Pos.TOP_CENTER);
                            break;
                    }
                } else {
                    noti.Info("Error Nuevo", "Verfique el Nombre, Esta Ciudad ya Existe", Pos.TOP_CENTER);
                }

            } catch (SQLException ex) {
                Logger.getLogger(ModalCiudadesController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void nombreciudadAction(KeyEvent event) {
        tablepaises.getItems().clear();
        ListarTabla(nombreciudad.getText());

    }

}
