/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Empleados;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ConsultasMySQL;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class EmpleadosController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Empleados> tablaviews;
    @FXML
    private TableColumn<Empleados, Integer> codigo;
    @FXML
    private TableColumn<Empleados, Integer> cedula;
    @FXML
    private TableColumn<Empleados, String> nameempleado;
    @FXML
    private TableColumn<Empleados, String> direccion;
    @FXML
    private JFXTextField txtbuscar;
    @FXML
    private JFXTextField cod;

    @FXML
    private JFXTextField direc;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnAgregar;
    @FXML
    private JFXTextField nameempl;
    @FXML
    private JFXButton config;
    @FXML
    private JFXTextField txtcedula;

    ObservableList<Empleados> Empleados = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;

    Notificacion noti;
    @FXML
    private Label labeltitle;
    @FXML
    private JFXButton btnclear;

    public static BorderPane mainRootPane;
    @FXML
    private TableColumn<Empleados, String> ciudad;
    @FXML
    private JFXButton btnasignardireccion;

    EmpleadosController empleadoController;
    @FXML
    private JFXComboBox<String> Ciudades;

    /**
     * @param url
     * @param rb
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        empleadoController = this;
        consultas = new ConsultasMySQL();
        noti = new Notificacion();
        num_maximo();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
        setciudadCombo();
    }

    public void asignardireccion(String ru) {
        direc.setText(ru);
    }

    private void setciudadCombo() {
        try {
            String query = "SELECT id_ciudad,nombre_ciudad FROM ciudad";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                Ciudades.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));
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
     * @param idtrol
     */
    public void confiniciopermiso(int idtrol) {

        try {
            String query = "SELECT v,c,e,a FROM permisos WHERE idRol = " + idtrol + " AND id_modulo=2";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                int v = rst.getInt("v");
                int c = rst.getInt("c");
                int e = rst.getInt("e");
                int a = rst.getInt("a");
                if (v == 1) {
                    cargartablaempleado();
                    contenMenu();
                    txtbuscar.setVisible(true);
                } else {
                    txtbuscar.setVisible(false);
                }
                if (c == 1) {
                    btnAgregar.setVisible(true);
                    cod.setVisible(true);
                    nameempl.setVisible(true);
                    btnasignardireccion.setVisible(true);
                    direc.setVisible(true);
                    config.setVisible(true);

                    btnclear.setVisible(true);
                    txtcedula.setVisible(true);

                } else {
                    btnAgregar.setVisible(false);
                    cod.setVisible(false);
                    nameempl.setVisible(false);
                    btnasignardireccion.setVisible(false);
                    direc.setVisible(false);
                    config.setVisible(false);

                    btnclear.setVisible(false);
                    txtcedula.setVisible(false);
                }
                if (e == 1) {
                    btnEliminar.setVisible(true);
                } else {
                    btnEliminar.setVisible(false);
                }
                if (a == 1) {
                    btnActualizar.setVisible(true);
                } else {
                    btnActualizar.setVisible(false);
                }

            } else {
                config.setVisible(false);
                txtbuscar.setVisible(false);
                btnAgregar.setVisible(false);
                cod.setVisible(false);
                nameempl.setVisible(false);
                btnasignardireccion.setVisible(false);
                direc.setVisible(false);
                txtcedula.setVisible(false);
                btnActualizar.setVisible(false);
                btnEliminar.setVisible(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * funcion click derecho asignar campos
     */
    public void contenMenu() {

        tablaviews.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {

                    Empleados emp = tablaviews.getSelectionModel().getSelectedItem();
                    cod.setText(String.valueOf(emp.getCodigo()));
                    txtcedula.setText(String.valueOf(emp.getCedula()));
                    nameempl.setText(emp.getNombre_Empleado());
                    direc.setText(emp.getDireccion());
                    btnAgregar.setDisable(true);
                    Ciudades.getSelectionModel().select(emp.getCiudad());
                    btnActualizar.setDisable(false);
                    btnEliminar.setDisable(false);
                    cod.setDisable(true);
                    config.setDisable(false);

                }

            }
        });

    }

    /**
     *
     */
    public void cargartablaempleado() {

        try {

            String query = "SELECT em.empl_codigo,em.cedula,em.empl_nombre,c.nombre_ciudad,em.empl_direccion "
                    + "FROM empleado AS em "
                    + "INNER JOIN ciudad AS c "
                    + "ON em.empl_id_ciudad = c.id_ciudad "
                    + "WHERE empl_status!=0 AND em.empl_codigo != 1;";

            rst = consultas.SELECT(query);
            tablaviews.getItems().clear();
            while (rst.next()) {
                Empleados.add(new Empleados(rst.getInt("empl_codigo"), rst.getInt("cedula"),
                        rst.getString("empl_nombre"), rst.getString("nombre_ciudad"), rst.getString("empl_direccion")));
            }
            tablaviews.setItems(Empleados);
            codigo.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigo"));
            nameempleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombre_Empleado"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Empleados, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
            cedula.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("cedula"));

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    /**
     *
     * @param buscar
     */
    public void ListarTabla(String buscar) {

        try {

            String query = "SELECT em.empl_codigo,em.cedula,em.empl_nombre,c.nombre_ciudad,em.empl_direccion "
                    + "FROM empleado AS em "
                    + "INNER JOIN ciudad AS c "
                    + "ON em.empl_id_ciudad = c.id_ciudad "
                    + "WHERE empl_status!=0 && em.empl_codigo LIKE'" + buscar + "%' OR empl_nombre LIKE'" + buscar + "%' OR cedula LIKE'" + buscar + "%' AND em.empl_codigo != 1;";

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                Empleados.add(new Empleados(rst.getInt("empl_codigo"), rst.getInt("cedula"),
                        rst.getString("empl_nombre"), rst.getString("nombre_ciudad"), rst.getString("empl_direccion")));
            }
            tablaviews.setItems(Empleados);
            codigo.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigo"));
            nameempleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombre_Empleado"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Empleados, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
            cedula.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("cedula"));

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
        //evento buscar datos solicitados Y SI NO HAY DATOS CARGA LA TABAL PRINCIPAL
        tablaviews.getItems().clear();
        ListarTabla(txtbuscar.getText());
        if (txtbuscar.getText().equals("")) {
            tablaviews.getItems().clear();
            cargartablaempleado();
        }
    }

    @FXML
    private void btnConfig(ActionEvent event) {
        try {

            String query = "SELECT em.empl_codigo,em.empl_clave,em.empl_clave,em.empl_sucursal_cod,su.sucu_nombre,em.empl_status "
                    + "FROM empleado AS em "
                    + "INNER JOIN sucursales AS su "
                    + "ON em.empl_sucursal_cod = su.sucu_codigo "
                    + "WHERE empl_codigo = '" + cod.getText() + "'";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                String co = rst.getString("empl_codigo");
                String cl = rst.getString("empl_clave");
                String su = rst.getString("empl_sucursal_cod") + "-" + rst.getString("sucu_nombre");
                int st = rst.getInt("empl_status");

                setStage("/views/ModalConfigEmpleado.fxml", co, cl, su, st);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }
    }

    @FXML
    private void btnactualizar(ActionEvent event) {

        if (nameempl.getText().equals("") || direc.getText().equals("") || cedula.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
             String selecciudad = Ciudades.getSelectionModel().getSelectedItem();
            String[] selec = selecciudad.split("-");
            int idciudad = Integer.parseInt(selec[0]);
            int ced = Integer.valueOf(txtcedula.getText().trim());
            int rest = consultas.UPDATE_EMPLEADO(cod.getText().trim(), ced, nameempl.getText().trim(), direc.getText().trim(),idciudad);
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Actualizados Correctamente", Pos.TOP_CENTER);
                    tablaviews.getItems().clear();
                    cargartablaempleado();
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
    private void btneliminar(ActionEvent event) {
        String code = cod.getText();
        if (code.equals("")) {
            noti.Error("Codigo Invalido", "Por Favor Dar Clic Derecho,en la sucursal Deseada", Pos.TOP_CENTER);
        } else {

            consultas.DELETE_EMPLEADO(code.trim());
            noti.Confirmar("Muy Bien", "Datos  Eliminados Correctamente", Pos.TOP_CENTER);
            tablaviews.getItems().clear();
            cargartablaempleado();
            btnclear(event);
        }
    }

    @FXML
    private void btnclear(ActionEvent event) {

        nameempl.setText("");
        direc.setText("");
        btnAgregar.setDisable(false);
        btnActualizar.setDisable(true);
        txtcedula.setText("");
        cod.setDisable(false);
        config.setDisable(true);
        num_maximo();

    }

    public void num_maximo() {
        try {
            String query = "SELECT MAX(empl_codigo) FROM empleado WHERE empl_codigo!=9999;";

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
    private void btnagregar(ActionEvent event) {
        if (cedula.getText().equals("") || nameempl.getText().equals("") || direc.getText().equals("") || Ciudades.getSelectionModel().getSelectedItem().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            String selecciudad = Ciudades.getSelectionModel().getSelectedItem();
            String[] selec = selecciudad.split("-");
            int idciudad = Integer.parseInt(selec[0]);
            int ced = Integer.valueOf(txtcedula.getText().trim());
            int code = Integer.valueOf(cod.getText().trim());
            int rest = consultas.INSERT_EMPLEADO(code, ced, nameempl.getText().trim(), direc.getText().trim(), idciudad);
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_CENTER);
                    tablaviews.getItems().clear();
                    cargartablaempleado();
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
    private void validcedula(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c) || txtcedula.getText().length() > 9) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    @FXML
    private void validnombre(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (Character.isDigit(c) || nameempl.getText().length() > 39) {
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
    private void validcod(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c)) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    private void setStage(String fxml, String code, String passw, String sucursal, int status) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalConfigEmpleadoController mcec = (ModalConfigEmpleadoController) loader.getController();
            mcec.datosuser(empleadoController, code, passw, sucursal, status);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);

            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnasignardireccion(ActionEvent event) {
        setStageAsignardireccion("/views/ModalVistaAsignarDireccionesem.fxml");
    }

    private void setStageAsignardireccion(String fxml) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalVistaAsignarDireccionesemController mvac = (ModalVistaAsignarDireccionesemController) loader.getController();
            mvac.closeApp(empleadoController);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);

            newStage.centerOnScreen();
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
