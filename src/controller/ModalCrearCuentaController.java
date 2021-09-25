/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import models.ConsultasMySQL;
import utils.Recursos;
import utils.VariableSesion;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalCrearCuentaController implements Initializable {

    @FXML
    private JFXTextField txtnumCuenta;
    @FXML
    private JFXComboBox<String> comboMoneda;

    @FXML
    private JFXTextField txtsaldoinicial;
    @FXML
    private JFXButton btnCrear;
    @FXML
    private JFXPasswordField password;
    ConsultasMySQL consultas;
    ResultSet rst = null;
    private String codeCliente;

    Notificacion notif;
    Recursos r;
    Notificacion noti;
    public static AnchorPane mainRootPane;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label tipoempleado;

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
        notif = new Notificacion();
        codigos(txtnumCuenta);
        moneda();

    }

    private void moneda() {
        comboMoneda.getItems().add(0, "");
        comboMoneda.getItems().add(1, "COP");
        comboMoneda.getItems().add(2, "USA");
    }

    /**
     *
     * @param codCliente
     * @param tipocliente
     */
    public void datosuser(String codCliente, String tipocliente) {
        this.codeCliente = codCliente;
        tipoempleado.setText(tipocliente);
    }

    @FXML
    private void validtxtnumCuenta(KeyEvent event) {
    }

    private void validtxtsaldoinicial(KeyEvent event) {
        try {

            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c)) {
                event.consume();
            }

        } catch (NumberFormatException e) {

        }
    }

    @FXML
    private void btnCrearAction(ActionEvent event) {
        if ((txtnumCuenta.getText().equals("")) || (comboMoneda.getSelectionModel().equals(""))) {
            notif.Error("Error Campos vacios", "Por Favor Ingresar Los datos Correctos", Pos.CENTER_RIGHT);

        } else {

            try {
                String sald = txtsaldoinicial.getText().trim();

                String punto = sald.replace(".", "");
                String coma = punto.replace(",", "");

                Double saldo = Double.parseDouble(coma);
                int sucursal = VariableSesion.getCode_sucursal();
                int mone = comboMoneda.getSelectionModel().getSelectedIndex();
                if (mone == 0) {
                    notif.Error("Error Campos vacios", "Por Favor la Moneda es Obligatoria", Pos.CENTER_RIGHT);
                    return;
                }
                //AQUI VERIFICAMOS QUE EXISTA UNA CUENTA O SI ESTA CANCELADA
                String query = "SELECT cuent_codigo,cuent__cliente_codigo FROM cuenta WHERE cuent__cliente_codigo =" + this.codeCliente.trim() + " AND cuent_estado != 'CANCELADO';";

                rst = consultas.SELECT(query);
                int tipocli = Integer.valueOf(tipoempleado.getText().trim());
                if (rst.next()) {
                    //si existen los datos validamos que tipo de cliente es.
                    if (tipocli == 2)//persona natural
                    {
                        int numcuent = Integer.valueOf(txtnumCuenta.getText().trim());
                        int rest = consultas.INSERT_CREAR_CUENTAS(numcuent, mone, sucursal,
                                this.codeCliente.trim(), saldo, password.getText().trim());
                        switch (rest) {
                            case 1:
                                notif.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_RIGHT);
                                clear();
                         
                                break;
                            default:
                                notif.Error("Error Base de Datos", "Error Inesperado !No se Pudo Ingresar la Informacion", Pos.TOP_CENTER);
                            
                                break;
                        }
                    } else {// este usuario ya tiene cuenta y pertenecese a empresa
                        notif.Info("Info", "Esta Cliente es una Empresa y ya tiene cuenta asignadas", Pos.TOP_RIGHT);
                        password.setDisable(false);
                    }
                } else {
                    int numcuent = Integer.valueOf(txtnumCuenta.getText().trim());
                    int rest = consultas.INSERT_CREAR_CUENTAS(numcuent, mone, sucursal,
                            this.codeCliente.trim(), saldo, password.getText().trim());
                    switch (rest) {
                        case 1:
                            notif.Confirmar("Muy Bien", "Datos  Ingresados Correctamente", Pos.TOP_RIGHT);
                            clear();
                          
                            break;
                        default:
                            notif.Error("Error Base de Datos", "Error Inesperado !No se Pudo Ingresar la Informacion", Pos.TOP_CENTER);
                           
                            break;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModalCrearCuentaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void clear() {
        password.setText("");
        txtsaldoinicial.setText("");
        comboMoneda.getSelectionModel().select("");
        codigos(txtnumCuenta);
    }

    /**
     *
     * @param txtserie
     * @return
     */
    public String codigos(JFXTextField txtserie) {

        try {

            int j;

            int c = 0;
            String query = "SELECT MAX(cuent_codigo) FROM cuenta;";

            rst = consultas.SELECT(query);
            while (rst.next()) {
                c = rst.getInt(1);
            }
            if (c == 0) {
                txtserie.setText("1");
            } else {
                int numcuenta = c + 1;
                txtserie.setText(String.valueOf(numcuenta));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "ERROR CONEXION SQL BD", "Eureka", JOptionPane.WARNING_MESSAGE);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }
        return null;

    }

    @FXML
    private void validpass(KeyEvent event) {
        try {

            if (password.getText().length() > 3) {
                event.consume();
            }

        } catch (Exception e) {

        }
    }

    private String format(double valor) {
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorFormateado = formato.format(valor);
        return valorFormateado;
    }

    public void closeApp(String cod, String tipocliente) {

        try {
            String query = "SELECT *  "
                    + "FROM clientes cli "
                    + "INNER JOIN ciudad AS c "
                    + "ON cli.client_id_ciudad = c.id_ciudad "
                    + "WHERE client_codigo = '" + cod + "'";

            rst = consultas.SELECT(query);

            if (rst.next()) {
                String co = rst.getString("client_codigo");
                String cc = rst.getString("client_doc");
                String nom = rst.getString("client_nombre");
                String cd = rst.getString("nombre_ciudad");

                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModalGestionCuentas.fxml"));
                Parent parent = (Parent) loader.load();
                ModalGestionCuentasController mgc = (ModalGestionCuentasController) loader.getController();
                mgc.datosuser(co, cc, nom, cd, tipocliente);
                Scene scene = new Scene(parent);
                scene.setFill(Color.TRANSPARENT);

                newStage.setScene(scene);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.TRANSPARENT);

                newStage.centerOnScreen();
                newStage.show();

            }

        } catch (IOException | SQLException ex) {
            Logger.getLogger(ModalCrearCuentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void mouseexit(MouseEvent event) {
        try{
            if (!txtsaldoinicial.getText().equals("")) {
           int val= Integer.parseInt(txtsaldoinicial.getText());
            txtsaldoinicial.setText(format(val));
        }
        }catch(NumberFormatException e){
            
        }
  
    }

 

}
