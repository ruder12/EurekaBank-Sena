/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Notificacion;
import utils.permisos;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ConsultasMySQL;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalvistaPermisosController implements Initializable {

    ObservableList<permisos> PermisosRol = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;

    Notificacion noti;
    @FXML
    private TableView<permisos> tablepermisos;
    @FXML
    private TableColumn<permisos, String> modulos;
    @FXML
    private TableColumn<permisos, Boolean> v;
    @FXML
    private TableColumn<permisos, Boolean> c;
    @FXML
    private TableColumn<permisos, Boolean> e;
    @FXML
    private TableColumn<permisos, Boolean> a;
    @FXML
    private FontAwesomeIconView cerrar;

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
     * @param user
     */
    public void datosuser(int user){

        cargartablaClintes(user);
    }

    /**
     *
     * @param user
     */
    public void cargartablaClintes(int user) {

        try {
            String query = "SELECT id_permisos,m.nombre_Modulo,v,c,e,a "
                    + "FROM permisos AS p "
                    + "INNER JOIN modulos AS m "
                    + "ON p.id_modulo = m.idmod "
                    + "WHERE idRol ="+user;

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                PermisosRol.add(new permisos(rst.getString("nombre_Modulo"), rst.getBoolean("v"),
                        rst.getBoolean("c"), rst.getBoolean("e"), rst.getBoolean("a")));
            }
            tablepermisos.setItems(PermisosRol);

            modulos.setCellValueFactory(new PropertyValueFactory<permisos, String>("modulos"));
            v.setCellValueFactory(new PropertyValueFactory<permisos, Boolean>("v"));
            c.setCellValueFactory(new PropertyValueFactory<permisos, Boolean>("c"));
            e.setCellValueFactory(new PropertyValueFactory<permisos, Boolean>("e"));
            a.setCellValueFactory(new PropertyValueFactory<permisos, Boolean>("a"));

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException ex) {
            }
        }

    }

    @FXML
    private void cerrartabla(MouseEvent event) {
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
