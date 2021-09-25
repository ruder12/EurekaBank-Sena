/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Clientes;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class GestionCuentasController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Clientes> tablaviews;
    @FXML
    private TableColumn<Clientes, String> codigo;
    @FXML
    private TableColumn<Clientes, Integer> cedula;
    @FXML
    private TableColumn<Clientes, String> nameCliente;
    @FXML
    private TableColumn<Clientes, String> ciudad;
    @FXML
    private TableColumn<Clientes, String> direccion;
    @FXML
    private TableColumn<Clientes, String> telefono;
    @FXML
    private TableColumn<Clientes, String> email;
    @FXML
    private JFXTextField txtbuscar;
    @FXML
    private JFXButton config;
    @FXML
    private JFXTextField cod;
    @FXML
    private JFXTextField nameclient;
    @FXML
    private JFXComboBox<String> ciud;
    @FXML
    private JFXTextField direc;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnAgregar;
    @FXML
    private JFXTextField txtcedula;
    @FXML
    private JFXTextField telef;
    @FXML
    private JFXTextField correo;

    ObservableList<Clientes> Clientes = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;

    Notificacion noti;
    @FXML
    private Label labeltitle;
    @FXML
    private JFXButton btnclear;

    public static AnchorPane mainRootPane;
    @FXML
    private RadioButton checkempresa;
    @FXML
    private RadioButton checkpersona;
    @FXML
    private ToggleGroup groupcheck;
    @FXML
    private TableColumn<Clientes, String> tipocliente;
    GestionCuentasController getioncuentasController;
    @FXML
    private JFXButton btnasignardireccion;

    /**
     * @param url
     * @param rb
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getioncuentasController = this;
        mainRootPane = rootPane;
        consultas = new ConsultasMySQL();
        noti = new Notificacion();
        setCiudadCombo();
        num_maximo();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
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
            String query = "SELECT id_modulo,idRol,v,c,e,a FROM permisos WHERE idRol = " + idtrol + " AND id_modulo=3";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                int m = rst.getInt("id_modulo");
                int r = rst.getInt("idRol");

                int v = rst.getInt("v");
                int c = rst.getInt("c");
                int e = rst.getInt("e");
                int a = rst.getInt("a");

                if (v == 1) {
                    cargartablaClintes();
                    contenMenu();
                    txtbuscar.setVisible(true);

                } else {

                    txtbuscar.setVisible(false);
                }
                if (c == 1) {
                    btnAgregar.setVisible(true);
                    cod.setVisible(true);
                    nameclient.setVisible(true);
                    ciud.setVisible(true);
                    direc.setVisible(true);
                    txtcedula.setVisible(true);
                    correo.setVisible(true);
                    telef.setVisible(true);
                    config.setVisible(true);
                    btnasignardireccion.setVisible(true);
                    checkempresa.setVisible(true);
                    checkpersona.setVisible(true);
                    btnclear.setVisible(true);

                } else {
                    btnAgregar.setVisible(false);
                    cod.setVisible(false);
                    nameclient.setVisible(false);
                    ciud.setVisible(false);
                    direc.setVisible(false);
                    txtcedula.setVisible(false);
                    correo.setVisible(false);
                    telef.setVisible(false);
                    btnasignardireccion.setVisible(false);
                    checkempresa.setVisible(false);
                    checkpersona.setVisible(false);
                    btnclear.setVisible(false);
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
                labeltitle.setText("No tienes Acceso");
                config.setVisible(false);
                txtbuscar.setVisible(false);
                btnAgregar.setVisible(false);
                cod.setVisible(false);
                nameclient.setVisible(false);
                ciud.setVisible(false);
                direc.setVisible(false);
                txtcedula.setVisible(false);
                correo.setVisible(false);
                telef.setVisible(false);
                btnasignardireccion.setVisible(false);
                btnActualizar.setVisible(false);
                btnEliminar.setVisible(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCiudadCombo() {
        try {
            String query = "SELECT id_ciudad,nombre_ciudad FROM ciudad";
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

                    Clientes cli = tablaviews.getSelectionModel().getSelectedItem();
                    cod.setText(cli.getCodigo());
                    txtcedula.setText(String.valueOf(cli.getCedula()));
                    nameclient.setText(cli.getNombre_cliente());
                    ciud.getSelectionModel().select(cli.getIdciudad() + "-" + cli.getCiudad());
                    direc.setText(cli.getDireccion());
                    telef.setText(cli.getTelefono());
                    correo.setText(cli.getEmail());
                    if (cli.getTipoCliente().equals("Empresa")) {
                        checkempresa.setSelected(true);
                    } else {
                        checkpersona.setSelected(true);
                    }
                    checkpersona.setDisable(true);
                    checkempresa.setDisable(true);
                    btnAgregar.setDisable(true);
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
    public void cargartablaClintes() {

        try {

            String query = "SELECT *  "
                    + "FROM clientes cli "
                    + "INNER JOIN ciudad AS c "
                    + "ON cli.client_id_ciudad = c.id_ciudad "
                    + "WHERE cli.client_status != 0;";

            rst = consultas.SELECT(query);

            //tablaviews.getColumns().clear();
            while (rst.next()) {
                int tipcli = rst.getInt("tipocliente");
                String tipoclientes = "Persona Natural";
                if (tipcli == 1) {
                    tipoclientes = "Empresa";
                }
                String idciudad = rst.getString("id_ciudad");
                Clientes.add(new Clientes(rst.getString("client_codigo"), rst.getString("client_doc"),
                        rst.getString("client_nombre"), idciudad, rst.getString("nombre_ciudad"),
                        rst.getString("client_direccion"), rst.getString("client_telefono"), rst.getString("client_email"), tipoclientes));
            }
            tablaviews.setItems(Clientes);
            codigo.setCellValueFactory(new PropertyValueFactory<Clientes, String>("codigo"));
            nameCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombre_cliente"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Clientes, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
            cedula.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("cedula"));
            telefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
            email.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
            tipocliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("tipoCliente"));
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

            String query = "SELECT *  "
                    + "FROM clientes cli "
                    + "INNER JOIN ciudad AS c "
                    + "ON cli.client_id_ciudad = c.id_ciudad "
                    + "WHERE cli.client_status != 0 AND client_nombre LIKE'" + buscar + "%' OR client_doc LIKE'" + buscar + "%';";

            rst = consultas.SELECT(query);

            while (rst.next()) {
                int tipcli = rst.getInt("tipocliente");
                String tipoclientes = "Persona Natural";
                if (tipcli == 1) {
                    tipoclientes = "Empresa";
                }
                String idciudad = rst.getString("id_ciudad");
                Clientes.add(new Clientes(rst.getString("client_codigo"), rst.getString("client_doc"),
                        rst.getString("client_nombre"), idciudad, rst.getString("nombre_ciudad"),
                        rst.getString("client_direccion"), rst.getString("client_telefono"), rst.getString("client_email"), tipoclientes));
            }
            tablaviews.setItems(Clientes);
            codigo.setCellValueFactory(new PropertyValueFactory<Clientes, String>("codigo"));
            nameCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombre_cliente"));
            ciudad.setCellValueFactory(new PropertyValueFactory<Clientes, String>("ciudad"));
            direccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
            cedula.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("cedula"));
            telefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
            email.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
            tipocliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("tipoCliente"));

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
        tablaviews.getItems().clear();
        ListarTabla(txtbuscar.getText());
    }

    @FXML
    private void btnConfig(ActionEvent event) {
        try {

            String query = "SELECT *  "
                    + "FROM clientes cli "
                    + "INNER JOIN ciudad AS c "
                    + "ON cli.client_id_ciudad = c.id_ciudad "
                    + "WHERE client_codigo = '" + cod.getText() + "' AND cli.client_status != 0;";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                String co = rst.getString("client_codigo");
                String cc = rst.getString("client_doc");
                String nom = rst.getString("client_nombre");
                String cd = rst.getString("nombre_ciudad");
                String tipoclientee = rst.getString("tipocliente");
                setStage("/views/ModalGestionCuentas.fxml", co, cc, nom, cd, tipoclientee);
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

        if (cod.getText().equals("") || nameclient.getText().equals("") || ciud.getSelectionModel().getSelectedItem().equals("")
                || direc.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {

            String ciuda = ciud.getSelectionModel().getSelectedItem().trim();
            String[] ciu = ciuda.split("-");

            if (isnumber(ciu[0])) {
                int tipoclientes = 0;
                int idciudad = Integer.valueOf(ciu[0]);
                //validamos el tipo de cliente que se esta registrando y si esta selecionado
                if (!checkempresa.isSelected() && !checkpersona.isSelected()) {
                    noti.Info("Informes del Sistema", "Seleccione el tipo de Cliente", Pos.TOP_CENTER);
                } else {
                    if (checkempresa.isSelected()) {
                        tipoclientes = 1;//empresa
                    } else {
                        tipoclientes = 2;//persona natural
                    }
                    int rest = consultas.UPDATE_CLIENTE(cod.getText().trim(), txtcedula.getText().trim(), nameclient.getText().trim(),
                            idciudad, direc.getText().trim(), telef.getText().trim(), correo.getText().trim(), tipoclientes);
                    switch (rest) {
                        case 1:
                            noti.Confirmar("Muy Bien", "Datos  Actualizados Correctamente", Pos.TOP_CENTER);
                            tablaviews.getItems().clear();
                            cargartablaClintes();
                            btnclear(event);
                            break;
                        case 2:
                            noti.Info("informes", "ya existe este Cliente con el mismo Numero de Identificacion", Pos.TOP_CENTER);
                            tablaviews.getItems().clear();
                            cargartablaClintes();
                            break;
                        case 3:
                            noti.Info("informes", "ya existe este Cliente con el mismo Nombre", Pos.TOP_CENTER);
                            tablaviews.getItems().clear();
                            cargartablaClintes();
                            break;
                        default:
                            noti.Error("Error! Base de Datos", "Error!! Inesperado !No se Pudo Actualizar la Informacion", Pos.TOP_CENTER);
                            break;
                    }
                }
            } else {
                noti.Info("Error! Ciudad", "Error!! Inesperado !Seleccione una ciudad", Pos.TOP_CENTER);
            }
        }
    }

    private Boolean isnumber(String dato) {
        boolean isNumeric = dato.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }

    @FXML
    private void btneliminar(ActionEvent event) {
        String code = cod.getText();
        if (code.equals("")) {
            noti.Error("Codigo Invalido", "Por Favor Dar Click Derecho en EL Cliente que Deseada Eliminar", Pos.TOP_CENTER);
        } else {
            consultas.DELETE_CLIENTE(code.trim());
            noti.Confirmar("Muy Bien", "Datos  Eliminados Correctamente", Pos.TOP_CENTER);
            tablaviews.getItems().clear();
            cargartablaClintes();
            btnclear(event);
        }
    }

    @FXML
    private void btnclear(ActionEvent event) {
        cod.setText("");
        nameclient.setText("");
        ciud.getSelectionModel().select("");
        direc.setText("");
        btnAgregar.setDisable(false);
        btnActualizar.setDisable(true);
        txtcedula.setText("");
        telef.setText("");
        correo.setText("");
        correo.setStyle("-fx-background-color:rgba(255,255,255)");
        cod.setDisable(false);
        config.setDisable(true);
        checkpersona.setDisable(false);
        checkempresa.setDisable(false);
        checkempresa.setSelected(false);
        checkpersona.setSelected(false);
        num_maximo();
    }

    @FXML
    private void btnagregar(ActionEvent event) {
        if (cod.getText().equals("") || nameclient.getText().equals("") || ciud.getSelectionModel().getSelectedItem().equals("")
                || direc.getText().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            if (ValidarMail(correo.getText().trim())) {
                int tipoclientes = 0;

                int cou = Integer.valueOf(cod.getText().trim());
                String ciuda = ciud.getSelectionModel().getSelectedItem().trim();
                String[] ciu = ciuda.split("-");
                int idciudad = Integer.valueOf(ciu[0]);
                //validamos el tipo de cliente que se esta registrando y si esta selecionado radiobuton
                if (!checkempresa.isSelected() && !checkpersona.isSelected()) {
                    noti.Info("Informes del Sistema", "Seleccione el tipo de Cliente", Pos.TOP_CENTER);
                } else {
                    if (checkempresa.isSelected()) {
                        tipoclientes = 1;//empresa
                    } else {
                        tipoclientes = 2;//persona natural
                    }

                    int rest = consultas.INSERT_CLIENTE(cou, txtcedula.getText().trim(), nameclient.getText().trim(),
                            idciudad, direc.getText().trim(), telef.getText().trim(), correo.getText().trim(), tipoclientes);
                    switch (rest) {
                        case 1:
                            noti.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_CENTER);
                            tablaviews.getItems().clear();
                            cargartablaClintes();
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
            } else {
                noti.Error("Correo Incorrecto", "Por favor Verifica su Correo e Intente Ingresar Nuevamente", Pos.TOP_CENTER);
                tablaviews.getItems().clear();
                cargartablaClintes();
            }

        }
    }

    @FXML
    private void validcedula(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c) || txtcedula.getText().length() > 10) {
                event.consume();
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void validnombre(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (Character.isDigit(c) || nameclient.getText().length() > 39) {
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

    private void setStage(String fxml, String code, String doc, String nombre, String ciudad, String tipocliente) {
        try {

            //dim overlay on new stage opening
            final Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalGestionCuentasController mcc = (ModalGestionCuentasController) loader.getController();
            mcc.datosuser(code, doc, nombre, ciudad, tipocliente);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void validtelf(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || telef.getText().length() > 9) {
            event.consume();
        }
    }

    @FXML
    private void validcorreo(KeyEvent event) {
        try {
            if (ValidarMail(correo.getText().trim())) {
                correo.setStyle("-fx-background-color:rgba(35,155,86)");
            } else {
                correo.setStyle("-fx-background-color:rgba(244,67,54)");
            }
        } catch (Exception e) {

        }
    }

    /**
     *
     * @param email
     * @return
     */
    public static boolean ValidarMail(String email) {
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public void num_maximo() {
        try {
            String query = "SELECT MAX(client_codigo) FROM clientes;";

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
    private void ciudAction(ActionEvent event) {
    }

    @FXML
    private void btnasignardireccion(ActionEvent event) {
        setStageAsignardireccion("/views/ModalVistaAsignarDireccionescli.fxml");
    }

    private void setStageAsignardireccion(String fxml) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalVistaAsignarDireccionescliController mvac = (ModalVistaAsignarDireccionescliController) loader.getController();
            mvac.closeApp(getioncuentasController);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
