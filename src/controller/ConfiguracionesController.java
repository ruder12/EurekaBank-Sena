package controller;

import utils.VariableSesion;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ConsultasMySQL;

/**
 * FXML Controller class controlador de la vista configuraciones
 *
 * @author Ruder Palencia (Geko)
 */
public class ConfiguracionesController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXComboBox<String> comboUsuario;

    @FXML
    private JFXTextField interesmensules;
    @FXML
    private JFXTextField cargomante;
    @FXML
    private JFXTextField costoMante;
    @FXML
    private JFXTextField interesTransac;

    ConsultasMySQL consultas;
    ResultSet rst = null;

    Notificacion noti;
    @FXML
    private JFXComboBox<String> Roles;
    @FXML
    private JFXButton btnguardar;
    @FXML
    private JFXButton btnVerPermisos;
    @FXML
    private Label idrols;
    @FXML
    private JFXComboBox<String> modulos;
    @FXML
    private CheckBox ver;
    @FXML
    private CheckBox crear;
    @FXML
    private CheckBox eliminar;
    @FXML
    private CheckBox actualizar;
    @FXML
    private JFXButton btnpermisos;
    VariableSesion pr;
    private JFXButton btninsteres;
    @FXML
    private JFXButton verBitacora;
    @FXML
    private JFXTextField interesmensules1;
    @FXML
    private JFXTextField costoMante1;
    @FXML
    private JFXTextField cargomante1;
    public static AnchorPane mainRootPane;
    @FXML
    private JFXButton VerCiudades;
    @FXML
    private JFXButton btnasignaciones;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainRootPane = rootPane;
        consultas = new ConsultasMySQL();
        noti = new Notificacion();
        setcomboUsuario();
        setcomboRoless();
        setcomboModulos();
    }

    /**
     * este metodo recibe como parametro id del rol para consultar permisos del
     * usuario
     *
     * @param idtrol
     */
    public void confiniciopermiso(int idtrol) {

        try {
            String query = "SELECT v,c,e,a FROM permisos WHERE idRol = " + idtrol + " AND id_modulo=4";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                int v = rst.getInt("v");
                int c = rst.getInt("c");
                int e = rst.getInt("e");
                int a = rst.getInt("a");
                if (v == 1) {
                    btnVerPermisos.setDisable(false);
                    VerCiudades.setDisable(false);
                    btnguardar.setDisable(false);
                    //btnpermisos.setDisable(false);
                    verBitacora.setDisable(false);
                    btnasignaciones.setDisable(false);

                } else {

                    VerCiudades.setDisable(true);
                    btnVerPermisos.setDisable(true);
                    btnguardar.setDisable(true);
                   // btnpermisos.setDisable(true);
                    verBitacora.setDisable(true);
                    btnasignaciones.setDisable(true);
                }
                if (c == 1) {

                } else {

                }
                if (e == 1) {

                } else {

                }
                if (a == 1) {
                    btnVerPermisos.setDisable(false);
                    VerCiudades.setDisable(false);
                    btnguardar.setDisable(false);
                    //btnpermisos.setDisable(false);
                    verBitacora.setDisable(false);
                    btnasignaciones.setDisable(false);

                } else {
                    VerCiudades.setDisable(true);
                    btnVerPermisos.setDisable(true);
                    btnguardar.setDisable(true);
                    //btnpermisos.setDisable(true);
                    verBitacora.setDisable(true);
                    btnasignaciones.setDisable(true);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * se cargan los empleados con su repectivo usuario de acceso al sistema
     */
    private void setcomboUsuario() {
        try {
            String query = "SELECT empl_codigo,empl_nombre FROM empleado WHERE empl_status !=0 && empl_codigo!='1'";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                this.comboUsuario.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));

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
     * se cargan los roles con su repectivo usuario de acceso al sistema
     */
    private void setcomboRoless() {
        try {
            String query = "SELECT idrol,nombre_rol FROM rol WHERE idrol != 1";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                this.Roles.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));

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
     * se cargan los modulos con su repectivo usuario de acceso al sistema
     */
    private void setcomboModulos() {
        try {
            String query = "SELECT idmod,nombre_Modulo FROM modulos;";
            rst = consultas.SELECT(query);

            while (rst.next()) {
                this.modulos.getItems().addAll(rst.getString(1) + "-" + rst.getString(2));
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
    private void guardarAction(ActionEvent event) {
        if (comboUsuario.getSelectionModel().getSelectedItem().equals("") || Roles.getSelectionModel().getSelectedItem().equals("")) {
            noti.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.TOP_CENTER);

        } else {
            String codeempl = comboUsuario.getSelectionModel().getSelectedItem();
            String[] coddatos = codeempl.split("-");
            String cod = coddatos[0];
            String idrol = Roles.getSelectionModel().getSelectedItem();
            String[] roldatos = idrol.split("-");
            String rol = roldatos[0];
            int r = Integer.parseInt(rol);
            int rest = consultas.UPDATE_USUARIO(cod, r);
            switch (rest) {
                case 1:
                    noti.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_CENTER);
                    Roles.getSelectionModel().select("");
                    comboUsuario.getSelectionModel().select("");
                    break;
                default:
                    noti.Error("Error Base de Datos", "Error Inesperado !No se Pudo Ingresar la Informacion", Pos.TOP_CENTER);
                    break;
            }

        }
    }

    @FXML
    private void VerPermisosAction(ActionEvent event) {
        if (idrols.getText().equals("")) {
            noti.Info("Ningun Usuario", "Por Favor Selecione Un Usuario Para Continuar", Pos.TOP_CENTER);
        } else {
            int idrol = Integer.parseInt(idrols.getText());
            setStage("/views/ModalvistaPermisos.fxml", idrol);
        }

    }

    private void setStage(String fxml, int user) {
        try {

            //dim overlay on new stage opening
            final Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalvistaPermisosController mcc = (ModalvistaPermisosController) loader.getController();
            mcc.datosuser(user);
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

    @FXML
    private void selecuser(ActionEvent event) {
        String selet = comboUsuario.getSelectionModel().getSelectedItem();
        if (selet.equals("")) {
            idrols.setText("0");
            modulos.setDisable(true);
            ver.setDisable(true);
            crear.setDisable(true);
            eliminar.setDisable(true);
            actualizar.setDisable(true);
            btnpermisos.setDisable(true);
        } else {
            String[] datos = selet.split("-");
            String cod = datos[0];
            try {
                String query = "SELECT rol FROM empleado WHERE empl_codigo='" + cod + "';";
                rst = consultas.SELECT(query);

                if (rst.next()) {
                    idrols.setText(rst.getString(1));
                    modulos.setDisable(false);
                    ver.setDisable(false);
                    crear.setDisable(false);
                    eliminar.setDisable(false);
                    actualizar.setDisable(false);
                    btnpermisos.setDisable(false);
                    
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

    }

    @FXML
    private void btnpermisosAction(ActionEvent event) {
        if (comboUsuario.getSelectionModel().getSelectedItem().equals("") || idrols.getText().equals("0")
                || modulos.getSelectionModel().getSelectedItem().equals("")) {
            noti.Info("Ningun Usuario", "Por Favor Selecione Un Usuario Para Continuar", Pos.TOP_CENTER);
        } else {
            int v = 0, c = 0, e = 0, a = 0;
            String idmodulos = modulos.getSelectionModel().getSelectedItem();
            String[] moddatos = idmodulos.split("-");
            String idmod = moddatos[0];
            int idm = Integer.parseInt(idmod);
            String rol = idrols.getText();
            int r = Integer.parseInt(rol);
            if (r != 4) {
                if (ver.isSelected()) {
                    v = 1;
                }
                if (crear.isSelected()) {
                    c = 1;
                }
                if (eliminar.isSelected()) {
                    e = 1;
                }
                if (actualizar.isSelected()) {
                    a = 1;
                }
                int rest = consultas.INSERT_PERMISOS(idm, r, v, c, e, a);
                switch (rest) {
                    case 1:
                        noti.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_CENTER);
                        limpiarpermisos();
                        break;
                    case 2:
                        noti.Confirmar("Muy Bien", "Permisos Actualizados Correctamente", Pos.TOP_CENTER);
                        limpiarpermisos();
                        break;
                    default:
                        noti.Error("Error Base de Datos", "Error Inesperado !No se Pudo Ingresar la Informacion", Pos.TOP_CENTER);
                        break;
                }
            } else {
                noti.Error("Asigne un rol", "Este Rol no es Valido.. Contacte al Adminitrador", Pos.TOP_CENTER);
            }
        }
    }

    /**
     *
     */
    public void limpiarpermisos() {
        modulos.getSelectionModel().select("");
        ver.setSelected(false);
        crear.setSelected(false);
        eliminar.setSelected(false);
        actualizar.setSelected(false);

    }

    @FXML
    private void verBitacoraAction(ActionEvent event) {
        setStageBitacora("/views/BItacora.fxml");
    }

    @FXML
    private void VerCiudadesAction(ActionEvent event) {
        setStageBitacora("/views/ModalCiudades.fxml");
    }

    @FXML
    private void btnasignaciones(ActionEvent event) {
        setStageBitacora("/views/BItacora_asignaciones.fxml");
    }

    private void setStageBitacora(String fxml) {
        try {

            //dim overlay on new stage opening
            final Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource(fxml));
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

}
