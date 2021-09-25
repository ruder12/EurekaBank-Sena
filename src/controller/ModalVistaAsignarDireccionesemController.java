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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import utils.Notificacion;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalVistaAsignarDireccionesemController implements Initializable {

    @FXML
    private JFXTextField txtvias;
    @FXML
    private JFXTextField numeroprincipal;
    @FXML
    private JFXTextField txtnumerosegundario;
    @FXML
    private JFXTextField txtbarrio_localida;
    @FXML
    private JFXTextField txtopciones;
    @FXML
    private Label labeltipovivineda;
    @FXML
    private JFXTextField txtnumerovivienda;
    @FXML
    private Label br;
    @FXML
    private JFXButton diagonal;
    @FXML
    private JFXButton bulevar;
    @FXML
    private JFXButton esquina;
    @FXML
    private JFXButton calleavenida;
    @FXML
    private JFXButton autopista;
    @FXML
    private JFXButton calle;
    @FXML
    private JFXButton avenidaCarerra;
    @FXML
    private JFXButton carrera;
    @FXML
    private JFXButton transversal;
    @FXML
    private JFXButton avenida;
    @FXML
    private JFXButton casa;
    @FXML
    private JFXButton apt;
    @FXML
    private JFXButton finca;
    @FXML
    private JFXButton parcela;
    @FXML
    private JFXButton conjunto;
    @FXML
    private JFXButton btnenviar;

    EmpleadosController empleadoController;
    Notificacion noti;
    @FXML
    private FontAwesomeIconView info;
    @FXML
    private JFXButton btncerrar;
    @FXML
    private JFXTextField txtnumerosegundario1;
    @FXML
    private JFXTextField txtotrasopciones;
    @FXML
    private JFXButton conjunto1;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        noti = new Notificacion();
    }

    public void closeApp(EmpleadosController controllerempleado) {
        empleadoController = controllerempleado;
    }

    @FXML
    private void numeroprincipal(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || numeroprincipal.getText().length() > 3) {
            event.consume();
        }
    }

    @FXML
    private void txtnumerosegundario(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || txtnumerosegundario.getText().length() > 5) {
            event.consume();
        }
    }
    private void txtnumerosegundario1(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || txtnumerosegundario1.getText().length() > 5) {
            event.consume();
        }
    }

    @FXML
    private void txtopciones(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (Character.isDigit(c) || txtopciones.getText().length() > 1) {
            event.consume();
        }
    }

    @FXML
    private void txtnumerovivienda(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || txtnumerovivienda.getText().length() > 3) {
            event.consume();
        }
    }

    @FXML
    private void diagonalAction(ActionEvent event) {
        txtvias.setText("Diagonal");
    }

    @FXML
    private void bulevarAction(ActionEvent event) {
        txtvias.setText("Bulevar");
    }

    @FXML
    private void esquinaAction(ActionEvent event) {
        txtvias.setText("Esquina");
    }

    @FXML
    private void calleavenidaAction(ActionEvent event) {
        txtvias.setText("Calle Avenida");
    }

    @FXML
    private void autopistaAction(ActionEvent event) {
        txtvias.setText("Autopista");
    }

    @FXML
    private void calleAction(ActionEvent event) {
        txtvias.setText("Calle");
    }

    @FXML
    private void avenidaCarerraAction(ActionEvent event) {
        txtvias.setText("Avenida Carrera");
    }

    @FXML
    private void carreraAction(ActionEvent event) {
        txtvias.setText("Carrera");
    }

    @FXML
    private void tranversalAction(ActionEvent event) {
        txtvias.setText("Transversal");
    }

    @FXML
    private void avenidaAction(ActionEvent event) {
        txtvias.setText("Avenida");
    }

    @FXML
    private void casaAction(ActionEvent event) {
        labeltipovivineda.setText("Casa");
    }

    @FXML
    private void aptAction(ActionEvent event) {
        labeltipovivineda.setText("Apt");
    }

    @FXML
    private void fincaAction(ActionEvent event) {
        labeltipovivineda.setText("Finca");
    }

    @FXML
    private void parcela(ActionEvent event) {
        labeltipovivineda.setText("Parcela");
    }

    @FXML
    private void conjuntoAction(ActionEvent event) {
        labeltipovivineda.setText("Conjunto");
    }

    @FXML
    private void btnenviar(ActionEvent event) {

        String direccion = "";
        if (txtvias.getText().equals("") || numeroprincipal.getText().equals("") || txtbarrio_localida.getText().equals("")) {
            noti.Info("Campos Vacios", "Los dos primeros campos son Obligatorios y el Barrio", Pos.TOP_CENTER);
        } else {
            btnenviar.getScene().getWindow().hide();
             direccion = txtvias.getText().trim() + " " + numeroprincipal.getText().trim() + "" + txtopciones.getText()
                    + " #" + txtnumerosegundario.getText() + "-" +txtnumerosegundario1.getText() + " " + labeltipovivineda.getText().trim() + " " + txtnumerovivienda.getText()
                    + "-Br. " + txtbarrio_localida.getText().trim()+ " " + txtotrasopciones.getText().trim();
            empleadoController.asignardireccion(direccion);
        }

    }

      @FXML
    private void info(MouseEvent event) {
        noti.InfoAyuda("Registrar Direccion", "1. Seleccione la via o clic en el boton (Calle). \n"
                + "2. Ingrese el numero de la via en la primera casilla con el simbolo #. Max 4 Digitos \n"
                + "3. La tercera opcion es Opcional, si el numero de la via es (Bis) puede agregar \n"
                + " la letra que le sigue. ej: calle 45A.  Max 2 Carateres\n"
                + "4. Numero de la casa. Max 6 Digitos \n"
                + "5. Selecione el tipo de Vivienda dando clic en el (Boton Casa) en la parte inferior del recuadro.\n"
                + "6. el numero de la vivienda es Opcional, ej: apt 420. Max 4 Digitos \n"
                + "7. ingrese el nombre de su barrio o localidad. Max 30 Carateres", Pos.CENTER);
    }

    @FXML
    private void validbarrio(KeyEvent event) {

        if (txtopciones.getText().length() > 29) {
            event.consume();
        }
    }

    @FXML
    private void btncerrarAction(ActionEvent event) {
        btnenviar.getScene().getWindow().hide();
    }

    @FXML
    private void conjunto1(ActionEvent event) {   
        txtvias.setText("Conjunto");
        
    }
}
