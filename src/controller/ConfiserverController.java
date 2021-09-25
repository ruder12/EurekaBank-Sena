/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import utils.Notificacion;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ConsultasMySQL;
import utils.VariableServer;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ConfiserverController implements Initializable {

    ConsultasMySQL consultas;

    Notificacion noti;
    @FXML
    private JFXTextField namebd;
    @FXML
    private JFXTextField host;
    @FXML
    private JFXTextField BDuser;
   
    @FXML
    private FontAwesomeIconView cerrar;

    @FXML
    private JFXButton guardarcredenciales;
    ResultSet rst = null;
    @FXML
    private JFXPasswordField Password;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        noti = new Notificacion();
        host.setText(VariableServer.getHost());
        BDuser.setText(VariableServer.getUser());
    }

    @FXML
    private void cerrar(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void guardarcredencialesAction(ActionEvent event) {
        try {
            String pass = "";
            if (Password.getText().trim().equals("")) {
                pass = "1";
            } else {
                pass = Password.getText().trim();
            }
            String ruta = "server.txt";
            String contenido = host.getText().trim() + "," + namebd.getText().trim() + "," + BDuser.getText().trim() + "," + pass;
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            try (BufferedWriter bw = new BufferedWriter(fw)) {

                if (!file.exists()) {
                    noti.Info("Archivo no Encontrado", "vuelve a ingresar los datos", Pos.CENTER);
                } else {
                    bw.write(contenido);
                    VariableServer.setHost(host.getText().trim());
                    VariableServer.setBaseDatos(namebd.getText().trim());
                    VariableServer.setUser(BDuser.getText().trim());
                    VariableServer.setPass(Password.getText().trim());
                    noti.Confirmar("Muy Bien", "Datos guardados", Pos.CENTER);
                }
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
        } catch (IOException ex) {
            Logger.getLogger(ConfiserverController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
