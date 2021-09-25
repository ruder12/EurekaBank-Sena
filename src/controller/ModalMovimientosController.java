/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.historialMovimientos;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
public class ModalMovimientosController implements Initializable {

    @FXML
    private TableView<historialMovimientos> tableviewsmovi;
    @FXML
    private Label NumCuenta;
    @FXML
    private FontAwesomeIconView cerrar;
    @FXML
    private TableColumn<historialMovimientos, String> descripcion;
    @FXML
    private TableColumn<historialMovimientos, String> accion;
    @FXML
    private TableColumn<historialMovimientos, String> fecha;
    @FXML
    private TableColumn<historialMovimientos, String> importe;
    ObservableList<historialMovimientos> historialMvi = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;
    @FXML
    private TableColumn<historialMovimientos, String> cuentareferencia;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultas = new ConsultasMySQL();

    }

    /**
     *
     * @param code
     */
    public void datosuser(String code) {
        NumCuenta.setText(code);
        cargartablaCuentas(code);
    }

    /**
     *
     * @param codCliente
     */
    public void cargartablaCuentas(String codCliente) {

        try {

            String query = "SELECT tm.tipomov_descripcion,tm.tipomov_accion,m.movi_fecha,m.movi_importe,m.movi_cuentareferencia "
                    + "FROM movimiento AS m "
                    + "INNER JOIN tipomovimiento tm "
                    + "ON m.movi_tipocodigo = tm.tipomov_codigo "
                    + "WHERE m.movi_cuencodigo = '" + codCliente + "';";

            rst = consultas.SELECT(query);

          
            while (rst.next()) {
                String val = format(rst.getDouble("movi_importe"));
                historialMvi.add(new historialMovimientos(rst.getString("tipomov_descripcion"), rst.getString("tipomov_accion"),
                        rst.getString("movi_fecha"), val,rst.getString("movi_cuentareferencia")));
            }
            tableviewsmovi.setItems(historialMvi);
            descripcion.setCellValueFactory(new PropertyValueFactory<historialMovimientos, String>("despcripcion"));
            accion.setCellValueFactory(new PropertyValueFactory<historialMovimientos, String>("accion"));
            fecha.setCellValueFactory(new PropertyValueFactory<historialMovimientos, String>("fecha"));
            importe.setCellValueFactory(new PropertyValueFactory<historialMovimientos, String>("importe"));
           cuentareferencia.setCellValueFactory(new PropertyValueFactory<historialMovimientos, String>("cuentareferencia"));
        } catch (SQLException ex) {
            Logger.getLogger(SucursalesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }
 private String format(double valor) {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String valorFormateado = formato.format(valor);
        return valorFormateado;
    }
    @FXML
    private void cerrarclick(MouseEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage.hide();
    }
}
