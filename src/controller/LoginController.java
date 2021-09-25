/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import utils.Notificacion;
import utils.Recursos;
import utils.VariableSesion;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ConsultasMySQL;
import utils.VariableServer;

/**
 * FXML Controller class
 *
 * @author ruder palencia
 */
public class LoginController implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXTextField txtuser;

    @FXML
    private JFXPasswordField txtpass;

    @FXML
    private Label der;

    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion noti;
    @FXML
    private JFXButton btnconfig;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        muestraContenido("server.txt");
        noti = new Notificacion();
    }

    @FXML
    private void doLogin() throws IOException {
        consultas = new ConsultasMySQL();
        if (!txtuser.getText().isEmpty() || !txtpass.getText().isEmpty()) {
            String restuser = consultas.ValidarUSER(Integer.valueOf(txtuser.getText()), txtpass.getText());
            String[] datosuser = restuser.split("-");
            int idrol = Integer.parseInt(datosuser[0]);
            if (!"0".equals(restuser)) {
                String userrol = datosuser[1];
                int codemple = Integer.valueOf(datosuser[2]);
                int codsucu = Integer.valueOf(datosuser[3]);
                VariableSesion.setcodemple(codemple);
                VariableSesion.setCode_sucursal(codsucu);
                VariableSesion.setClavetemporal(txtpass.getText().trim());
                consultas.cobroAutoMes(codemple);
         
                btnLogin.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EurekaBanck.fxml"));
                Parent root = (Parent) loader.load();
                EurekaBanckController ebc = (EurekaBanckController) loader.getController();
                ebc.confiniciopermiso(idrol, userrol);
                Stage mainStage = new Stage();
                Scene scene = new Scene(root);
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/utils/img/user_menu.png")));
                mainStage.setResizable(false);
                mainStage.setScene(scene);
                mainStage.show();
                mainStage.setOnCloseRequest(e ->{ 
                    ebc.closeApp();
                });
            } else {

                noti.Error("Sesion Incorrecta", "Error al Validar Datos, o el Usuario no esta Asignado", Pos.TOP_CENTER);
                txtuser.setText("");
                txtpass.setText("");
            }

        } else {
            noti.Error("Sesion Incorrecta", "Verifique Sus Credenciales", Pos.TOP_CENTER);
        }

    }

    @FXML
    private void enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                doLogin();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void btnconfigAction(ActionEvent event) {
        setStage("/views/Confiserver.fxml");

    }

    private void setStage(String fxml) {
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

            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void muestraContenido(String archivo) {
        File file = new File(archivo);
        FileReader f = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String cadena;
            f = new FileReader(archivo);
            try (BufferedReader b = new BufferedReader(f)) {
                while ((cadena = b.readLine()) != null) {
                    String[] datosuser = cadena.split(",");
                    String h = datosuser[0];
                    String bd = datosuser[1];
                    String u = datosuser[2];
                    String p = datosuser[3];
                    if ("".equals(p)) {
                        p = "1";
                    }
                    VariableServer.setHost(h);
                    VariableServer.setBaseDatos(bd);
                    VariableServer.setUser(u);
                    VariableServer.setPass(p);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void valicedula(KeyEvent event) {
        try {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c)) {
                event.consume();
                  txtuser.setText("");
            }

        } catch (Exception e) {
        }
    }
}
