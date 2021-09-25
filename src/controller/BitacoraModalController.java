/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Bitacora;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ConsultasMySQL;

/**
 * FXML Controller class
 * esta controlador configura el acceso a la vista Bitacora
 * 
 * @author Ruder Palencia (Geko)
 */
public class BitacoraModalController implements Initializable {

     @FXML
    private TableView<Bitacora> tableviewsmovi;
   
    @FXML
    private FontAwesomeIconView cerrar;
    @FXML
    private TableColumn<Bitacora, String> descripcion;
    @FXML
    private TableColumn<Bitacora, String> accion;
    @FXML
    private TableColumn<Bitacora, String> fecha;
    @FXML
    private TableColumn<Bitacora, String> importe; 
    @FXML
    private TableColumn<Bitacora, String> cuenta;
    
    ObservableList<Bitacora> historialMvi = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;
  
    @FXML
    private JFXTextField Buscar;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           consultas = new ConsultasMySQL();
           cargartablaCuentas();
    }    

    /**
     * esta metodo se encarga de carga la tabla cuentas
     */
    public void cargartablaCuentas() {

        try {

            String query = "SELECT m.movi_cuencodigo,tm.tipomov_descripcion,tm.tipomov_accion,m.movi_fecha,m.movi_importe "
                    + "FROM movimiento AS m "
                    + "INNER JOIN tipomovimiento tm "
                    + "ON m.movi_tipocodigo = tm.tipomov_codigo "
                    + "ORDER BY m.movi_cuencodigo;";

            rst = consultas.SELECT(query);

          
            while (rst.next()) {
                    String valor = format(rst.getDouble("movi_importe"));
                historialMvi.add(new Bitacora(rst.getString("movi_cuencodigo"),rst.getString("tipomov_descripcion"), rst.getString("tipomov_accion"),
                        rst.getString("movi_fecha"), valor));
            }
            tableviewsmovi.setItems(historialMvi);
            cuenta.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("cuenta"));
            descripcion.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("despcripcion"));
            accion.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("accion"));
            fecha.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("fecha"));
            importe.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("importe"));
          
        } catch (SQLException ex) {
            Logger.getLogger(BitacoraModalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }

    /**
     * este medoto carga la tabla y filtra los datos
     * @param Codigo
     */
    public void cargartablaCuentaslistar(String Codigo) {

        try {

            String query = "SELECT m.movi_cuencodigo,tm.tipomov_descripcion,tm.tipomov_accion,m.movi_fecha,m.movi_importe "
                    + "FROM movimiento AS m "
                    + "INNER JOIN tipomovimiento tm "
                    + "ON m.movi_tipocodigo = tm.tipomov_codigo "
                    + "WHERE m.movi_cuencodigo LIKE '"+Codigo+"%' ORDER BY m.movi_cuencodigo;";

            rst = consultas.SELECT(query);

          
            while (rst.next()) {
                 String valor = format(rst.getDouble("movi_importe"));
               
                historialMvi.add(new Bitacora(rst.getString("movi_cuencodigo"),rst.getString("tipomov_descripcion"), rst.getString("tipomov_accion"),
                        rst.getString("movi_fecha"), valor));
            }
            tableviewsmovi.setItems(historialMvi);
            cuenta.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("cuenta"));
            descripcion.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("despcripcion"));
            accion.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("accion"));
            fecha.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("fecha"));
            importe.setCellValueFactory(new PropertyValueFactory<Bitacora, String>("importe"));
          
        } catch (SQLException ex) {
            Logger.getLogger(BitacoraModalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                consultas.getConnection().close();
            } catch (SQLException e) {
            }
        }

    }
    @FXML
    private void cerrarclick(MouseEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage.hide();
    }

    @FXML
    private void buscarTYPE(KeyEvent event) {
         tableviewsmovi.getItems().clear();
        cargartablaCuentaslistar(Buscar.getText());
    }
       private String format(double valor) {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String valorFormateado = formato.format(valor);
        return valorFormateado;
    }
}
