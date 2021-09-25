/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Sucursales;
import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import models.ConsultasMySQL;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class SucursalesController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Sucursales> tablaviews;
    @FXML
    private TableColumn<Sucursales, String> codigo;
    @FXML
    private TableColumn<Sucursales, String> namesucursal;
    @FXML
    private TableColumn<Sucursales, String> ciudad;
    @FXML
    private TableColumn<Sucursales, String> direccion;
    @FXML
    private TableColumn<Sucursales, Integer> cuentas;

    @FXML
    private JFXTextField cod;
    @FXML
    private JFXTextField namesuc;

    @FXML
    private JFXTextField direc;

    @FXML
    private JFXTextField txtbuscar;

    ConsultasMySQL consultas;
    ResultSet rst = null;

    /**
     *
     */
    public static AnchorPane mainRootPane;
    ObservableList<Sucursales> Sucursales = FXCollections.observableArrayList();
    @FXML
    private JFXButton btnAgregar;
    @FXML
    private JFXButton btnActualizar;
    Notificacion noti;

    @FXML
    private Label labeltitle;
    @FXML
    private JFXButton btnclear;
    @FXML
    private JFXComboBox<String> ciud;
    @FXML
    private JFXButton btnasignardireccion;
    SucursalesController sucursalController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainRootPane = rootPane;
        sucursalController = this;
        consultas = new ConsultasMySQL();
        noti = new Notificacion();
        setciudadcombo();
        btnActualizar.setDisable(true);
    }

    public void asignardireccion(String ru) {
        direc.setText(ru);
    }

    /**
     *
     * @param idtrol
     */
    public void confiniciopermiso(int idtrol) {

        try {
            String query = "SELECT v,c,e,a FROM permisos WHERE idRol = " + idtrol + " AND id_modulo=1";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                int v = rst.getInt("v");
                int c = rst.getInt("c");
                int e = rst.getInt("e");
                int a = rst.getInt("a");
                if (v == 1) {
                    cargartablasucursal();
                    contenMenu();

                    txtbuscar.setVisible(true);

                } else {

                    txtbuscar.setVisible(false);
                }
                if (c == 1) {
                    btnAgregar.setVisible(true);
                    cod.setVisible(true);
                    namesuc.setVisible(true);
                    ciud.setVisible(true);
                    direc.setVisible(true);
                    btnasignardireccion.setVisible(true);
                    btnclear.setVisible(true);
                    num_maximo();
                } else {
                    btnAgregar.setVisible(false);
                    cod.setVisible(false);
                    namesuc.setVisible(false);
                    ciud.setVisible(false);
                    direc.setVisible(false);
                    btnasignardireccion.setVisible(false);

                    btnclear.setVisible(false);

                }
                if (e == 1) {

                } else {

                }
                if (a == 1) {
                    btnActualizar.setVisible(true);
                } else {
                    btnActualizar.setVisible(false);
                }

            } else {
                labeltitle.setText("Sin Acceso");
                btnAgregar.setVisible(false);
                cod.setVisible(false);
                namesuc.setVisible(false);
                ciud.setVisible(false);
                direc.setVisible(false);
                btnasignardireccion.setVisible(true);
                btnclear.setVisible(false);
                btnActualizar.setVisible(false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public void cargartablasucursal() {

        try {

            String query = "SELECT su.sucu_codigo,su.sucu_nombre,c.id_ciudad,c.nombre_ciudad,su.sucu_direccion,COUNT(*) AS total  "
                    + "FROM sucursales AS su "
                    + "INNER JOIN ciudad AS c "
                    + "ON su.sucu_id_ciudad = c.id_ciudad "
                    + "INNER JOIN cuenta  AS cu  "
                    + "ON cu.cuent_sucucodigo = su.sucu_codigo  "
                    + "GROUP BY su.sucu_codigo DESC LIMIT 50;;";

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                String idciudad = rst.getString("id_ciudad");
                Sucursales.add(new Sucursales(rst.getInt("sucu_codigo"), rst.getString("sucu_nombre"),
                        idciudad, rst.getString("nombre_ciudad"), rst.getString("sucu_direccion"), rst.getInt("total")));
            }
            tablaviews.setItems(Sucursales);
            codigo.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("codigo"));
            namesucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("nombre_sucursal"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("direccion"));
            cuentas.setCellValueFactory(new PropertyValueFactory<Sucursales, Integer>("numCuentas"));

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    private void setciudadcombo() {
        try {
            String query = "SELECT id_ciudad,nombre_ciudad FROM ciudad WHERE id_ciudad !=33";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                ciud.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));
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

    /**
     *
     */
    public void contenMenu() {

        tablaviews.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {

                    Sucursales suc = tablaviews.getSelectionModel().getSelectedItem();
                    cod.setText(String.valueOf(suc.getCodigo()));
                    namesuc.setText(suc.getNombre_sucursal());
                    ciud.getSelectionModel().select(suc.getIdciudad() + "-" + suc.getCiudad());
                    direc.setText(suc.getDireccion());
                    btnAgregar.setDisable(true);
                    btnActualizar.setDisable(false);
                    cod.setDisable(true);
                }

            }
        });

    }

    /**
     *
     * @param buscar
     */
    public void ListarTabla(String buscar) {

        try {

            String query = "SELECT su.sucu_codigo,su.sucu_nombre,c.id_ciudad,c.nombre_ciudad,su.sucu_direccion,COUNT(*) AS total  "
                    + "FROM sucursales AS su   "
                    + "INNER JOIN ciudad AS c  "
                    + "ON su.sucu_id_ciudad = c.id_ciudad  "
                    + "WHERE su.sucu_nombre LIKE'" + buscar + "%' ORDER BY su.sucu_codigo;";

            rst = consultas.SELECT(query);

            while (rst.next()) {
                String idciudad = rst.getString("id_ciudad");
               int total = rst.getInt("total")==1?0:rst.getInt("total");
                Sucursales.add(new Sucursales(rst.getInt("sucu_codigo"), rst.getString("sucu_nombre"),
                        idciudad, rst.getString("nombre_ciudad"), rst.getString("sucu_direccion"), total));
            }
            tablaviews.setItems(Sucursales);
            codigo.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("codigo"));
            namesucursal.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("nombre_sucursal"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Sucursales, String>("direccion"));
            cuentas.setCellValueFactory(new PropertyValueFactory<Sucursales, Integer>("numCuentas"));

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
    private void txtbuscar(KeyEvent event) {
       // este evento en el campo buscar , lista la inofrmacion solicitada y si no hay datos carga la tabla principal
        tablaviews.getItems().clear();
        ListarTabla(txtbuscar.getText());
        if (txtbuscar.getText().equals("")) {
            tablaviews.getItems().clear();
            cargartablasucursal();
        }
    }

    @FXML
    private void btnagregar(ActionEvent event) {

        if (namesuc.getText().equals("") || ciud.getSelectionModel().getSelectedItem().equals("")
                || direc.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {

            String idciudad = ciud.getSelectionModel().getSelectedItem().trim();
            String[] idci = idciudad.split("-");
            int ciuda = Integer.valueOf(idci[0]);
            int rest = consultas.INSERT_SUCURSAL(namesuc.getText().trim(), ciuda, direc.getText().trim());
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_CENTER);
                    tablaviews.getItems().clear();
                    cargartablasucursal();
                    btnclear(event);
                    break;
                case 2:
                    noti.Info("Informes del Sistema", "Los Datos Ingresados Ya se Encuentran Disponibles", Pos.TOP_CENTER);
                    break;
                default:
                    noti.Error("Error Base de Datos", "Error Inesperado !No se Pudo Ingresar la Informacion", Pos.TOP_CENTER);
                    break;
            }

        }
    }

    @FXML
    private void btnactualizar(ActionEvent event) {

        if (cod.getText().equals("") || namesuc.getText().equals("") || ciud.getSelectionModel().getSelectedItem().equals("")
                && direc.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            String idciudad = ciud.getSelectionModel().getSelectedItem().trim();
            String[] idci = idciudad.split("-");
            int ciuda = Integer.valueOf(idci[0]);
            int rest = consultas.UPDATE_SUCURSAL(cod.getText().trim(), namesuc.getText().trim(), ciuda, direc.getText().trim());
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Actualizados Correctamente", Pos.TOP_CENTER);
                    tablaviews.getItems().clear();
                    cargartablasucursal();
                    btnclear(event);
                    break;
                case 2:
                    noti.Info("Informes del Sistema", "Los Datos Ingresados Ya se Encuentran Disponibles", Pos.TOP_CENTER);
                    break;
                default:
                    noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Actualizar la Informacion", Pos.TOP_CENTER);
                    break;
            }

        }
    }

    @FXML
    private void btnclear(ActionEvent event) {

        namesuc.setText("");
        ciud.getSelectionModel().select("");
        direc.setText("");
        btnAgregar.setDisable(false);
        btnActualizar.setDisable(true);
        cod.setDisable(false);
        num_maximo();
    }

    public void num_maximo() {
        try {
            String query = "SELECT MAX(sucu_codigo) FROM sucursales;";

            rst = consultas.SELECT(query);
            if (rst.next()) {
                int codsucu = rst.getInt(1);
                cod.setText(String.valueOf(codsucu + 1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void validnombre(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (Character.isDigit(c) || namesuc.getText().length() > 35) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    private void validciuda(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (Character.isDigit(c)) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    @FXML
    private void valicode(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c)) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    @FXML
    private void CLICKVERTABLA(MouseEvent event) {
        setStage("/views/ModalVistaSucursales.fxml");
    }

    @FXML
    private void btnasignardireccionAction(ActionEvent event) {
        setStageAsignardireccion("/views/ModalVistaAsignarDirecciones.fxml");
    }

    private void setStage(String fxml) {
        try {

            //dim overlay on new stage opening
            final Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();

            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);

            newStage.getScene().getRoot().setEffect(new DropShadow());
            newStage.showingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {

                        rootPane.getChildren().add(veil);

                    } else if (rootPane.getChildren().contains(veil)) {
                        rootPane.getChildren().removeAll(veil);
                    }

                }
            });

            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setStageAsignardireccion(String fxml) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalVistaAsignarDireccionesController mvac = (ModalVistaAsignarDireccionesController) loader.getController();
            mvac.closeApp(sucursalController);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);

            newStage.getScene().getRoot().setEffect(new DropShadow());

            newStage.centerOnScreen();
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
