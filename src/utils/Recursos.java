/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.jfoenix.controls.JFXButton;
import controller.ModalCancelarCuentaController;
import controller.ModalCrearCuentaController;
import controller.ModalDepositoController;
import controller.ModalMovimientosController;
import controller.ModalRetiroController;
import controller.ModalTransferenciaController;
import controller.SucursalesController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class Recursos {

    private JFXButton btnCrear;

    public void setStageApertura(final AnchorPane rootPane, String fxml, String cod, String tipocliente) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalCrearCuentaController mcc = (ModalCrearCuentaController) loader.getController();
            mcc.datosuser(cod, tipocliente);
            Scene scene = new Scene(parent);
            //scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            // newStage.initModality(Modality.APPLICATION_MODAL);
            // newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            newStage.setOnCloseRequest(e -> {
                mcc.closeApp(cod, tipocliente);
            });
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStageCancelarCuenta(final AnchorPane rootPane, String fxml, String numcuenta, String cod, String tipocliente) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalCancelarCuentaController mcc = (ModalCancelarCuentaController) loader.getController();
            mcc.datosuser(numcuenta);
            Scene scene = new Scene(parent);
            //scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
            //newStage.initModality(Modality.APPLICATION_MODAL);
            //newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            newStage.setOnCloseRequest(e -> {
                mcc.closeApp(cod, tipocliente);
            });
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStageDeposito(final AnchorPane rootPane, String fxml, String numcuenta, String cod, String tipocliente) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalDepositoController mcc = (ModalDepositoController) loader.getController();
            mcc.datosuser(numcuenta);
            Scene scene = new Scene(parent);
//            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
//            newStage.initModality(Modality.APPLICATION_MODAL);
//            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            newStage.setOnCloseRequest(e -> {
                mcc.closeApp(cod, tipocliente);
            });
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStageRetiro(final AnchorPane rootPane, String fxml, String numcuenta, String cod, String tipocliente) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalRetiroController mcc = (ModalRetiroController) loader.getController();
            mcc.datosuser(numcuenta);
            Scene scene = new Scene(parent);
//            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
//            newStage.initModality(Modality.APPLICATION_MODAL);
//            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            newStage.setOnCloseRequest(e -> {
                mcc.closeApp(cod, tipocliente);
            });
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStageTranferencia(final AnchorPane rootPane, String fxml, String numcuenta, String cod, String tipocliente) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalTransferenciaController mcc = (ModalTransferenciaController) loader.getController();
            mcc.datosuser(numcuenta);
            Scene scene = new Scene(parent);
//            scene.setFill(Color.TRANSPARENT);

            newStage.setScene(scene);
//            newStage.initModality(Modality.APPLICATION_MODAL);
//            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();
            newStage.setOnCloseRequest(e -> {
                mcc.closeApp(cod, tipocliente);
            });
        } catch (IOException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStageMovimientos(final AnchorPane rootPane, String fxml, String cod) {
        try {

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = (Parent) loader.load();
            ModalMovimientosController mcc = (ModalMovimientosController) loader.getController();
            mcc.datosuser(cod);
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
