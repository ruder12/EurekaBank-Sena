/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.GestionCuentasAdmin;
import utils.Recursos;
import utils.Notificacion;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ConsultasMySQL;

/**
 * FXML Controller class
 *
 * @author Ruder Palencia (Geko)
 */
public class ModalGestionCuentasController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label labelnombre;
    @FXML
    private Label labelDoc;
    @FXML
    private Label labelciudad;
    @FXML
    private Label labelcodigo;
    @FXML
    private JFXButton btnApertura;
    @FXML
    private JFXButton btnCancelarCuenta;
    @FXML
    private JFXButton btnDeposito;
    @FXML
    private JFXButton btnRetiro;
    @FXML
    private JFXButton btnTranferencia;
    @FXML
    private JFXButton btnConsultarMovimiento;
    @FXML
    private TableView<GestionCuentasAdmin> tableviewscuentas;
    @FXML
    private Label LabelNumCuenta;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> NumCuenta;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> sucursal;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> empleado;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> saldo;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> fecha;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> estado;
    @FXML
    private TableColumn<GestionCuentasAdmin, Integer> movimientos;
    @FXML
    private TableColumn<GestionCuentasAdmin, String> moneda;
    ObservableList<GestionCuentasAdmin> cuentasAdmin = FXCollections.observableArrayList();
    ConsultasMySQL consultas;
    ResultSet rst = null;
    Recursos r;
    Notificacion noti;
    public static AnchorPane mainRootPane;
    @FXML
    private Label tipocliente;

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
        r = new Recursos();
        contenMenu();
    }

    /**
     *
     * @param code
     * @param doc
     * @param nombre
     * @param ciudad
     * @param tipocliente
     */
    public void datosuser(String code, String doc, String nombre, String ciudad, String tipocliente) {
        labelcodigo.setText(code);
        labelDoc.setText(doc);
        labelnombre.setText(nombre);
        labelciudad.setText(ciudad);
        this.tipocliente.setText(tipocliente);
        cargartablaCuentas(code);
        
    }

    /**
     *
     */
    public void contenMenu() {
        
        tableviewscuentas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    
                    GestionCuentasAdmin cli = tableviewscuentas.getSelectionModel().getSelectedItem();
                    LabelNumCuenta.setText(cli.getNumerocuenta());
                    
                }
                
            }
        });
        
    }

    /**
     *
     * @param codCliente
     */
    public void cargartablaCuentas(String codCliente) {
        
        try {
            
            String query = "SELECT cuent_codigo,cuent_monecodigo,su.sucu_nombre,cuent_empl_cuenta_creada,cuent_cuenta_saldo,cuent_fecha_creacion,cu.cuent_estado,COUNT(m.movi_cuencodigo) AS movi "
                    + "FROM cuenta AS cu  "
                    + "INNER JOIN sucursales AS su  "
                    + "ON cu.cuent_sucucodigo = su.sucu_codigo  "
                    + "INNER JOIN clientes AS cl  "
                    + "ON cu.cuent__cliente_codigo = cl.client_codigo "
                    + "INNER JOIN movimiento m "
                    + "ON m.movi_cuencodigo = cu.cuent_codigo "
                    + "WHERE cu.cuent__cliente_codigo = '" + codCliente + "' GROUP BY cu.cuent_codigo;";
            
            rst = consultas.SELECT(query);

           
            while (rst.next()) {
                int mone = rst.getInt("cuent_monecodigo");
                String moned = "USA";
                if (mone == 1) {
                    moned = "COP";
                }
                String valor = format(rst.getDouble("cuent_cuenta_saldo"));
                cuentasAdmin.add(new GestionCuentasAdmin(rst.getString("cuent_codigo"), moned, rst.getString("sucu_nombre"),
                        rst.getString("cuent_empl_cuenta_creada"), valor,
                        rst.getString("cuent_fecha_creacion"), rst.getString("cuent_estado"), rst.getInt("movi")));
            }
            tableviewscuentas.setItems(cuentasAdmin);
            NumCuenta.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("numerocuenta"));
            moneda.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("moneda"));
            sucursal.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("sucursal"));
            empleado.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("empleado"));
            saldo.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("saldo"));
            fecha.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("fecha"));
            estado.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, String>("estado"));
            movimientos.setCellValueFactory(new PropertyValueFactory<GestionCuentasAdmin, Integer>("conMvimientos"));
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
    private void btnAperturaAction(ActionEvent event) {
        if (!labelcodigo.getText().equals("")) {
             btnApertura.getScene().getWindow().hide();
            r.setStageApertura(rootPane, "/views/ModalCrearCuenta.fxml", labelcodigo.getText().trim(),tipocliente.getText().trim());
           
        } else {
            noti.Info("No Existe Codigo", "El Codigo del Cliente No Existe", Pos.TOP_LEFT);
        }
        
    }
    
    @FXML
    private void btnCancelarCuentaAction(ActionEvent event) {
        if (!LabelNumCuenta.getText().equals("")) {
             btnCancelarCuenta.getScene().getWindow().hide();
            r.setStageCancelarCuenta(rootPane, "/views/ModalCancelarCuenta.fxml", LabelNumCuenta.getText().trim(),labelcodigo.getText().trim(),tipocliente.getText().trim());
        } else {
            noti.Info("No Existe Numero de Cuenta", "!!Señor Usuario Selecione Una Cuenta Por Favor!", Pos.TOP_LEFT);
        }
    }
    
    @FXML
    private void btnDepositoAction(ActionEvent event) {
        if (!LabelNumCuenta.getText().equals("")) {
            btnDeposito.getScene().getWindow().hide();
            r.setStageDeposito(rootPane, "/views/ModalDeposito.fxml", LabelNumCuenta.getText().trim(),labelcodigo.getText().trim(),tipocliente.getText().trim());
        } else {
            noti.Info("No Existe Numero de Cuenta", "!!Señor Usuario Selecione Una Cuenta Por Favor!", Pos.TOP_LEFT);
        }
    }
    
    @FXML
    private void btnRetiroAction(ActionEvent event) {
        if (!LabelNumCuenta.getText().equals("")) {
            btnRetiro.getScene().getWindow().hide();
            r.setStageRetiro(rootPane, "/views/ModalRetiro.fxml", LabelNumCuenta.getText().trim(),labelcodigo.getText().trim(),tipocliente.getText().trim());
        } else {
            noti.Info("No Existe Numero de Cuenta", "!!Señor Usuario Selecione Una Cuenta Por Favor!", Pos.TOP_LEFT);
        }
    }
    
    @FXML
    private void btnTranferenciaAction(ActionEvent event) {
        if (!LabelNumCuenta.getText().equals("")) {
            btnTranferencia.getScene().getWindow().hide();
            r.setStageTranferencia(rootPane, "/views/ModalTranferencia.fxml", LabelNumCuenta.getText().trim(),labelcodigo.getText().trim(),tipocliente.getText().trim());
        } else {
            noti.Info("No Existe Numero de Cuenta", "!!Señor Usuario Selecione Una Cuenta Por Favor!", Pos.TOP_LEFT);
        }
    }
    
    @FXML
    private void btnConsultarMovimientoAction(ActionEvent event) {
        if (!LabelNumCuenta.getText().equals("")) {
            r.setStageMovimientos(rootPane, "/views/ModalMovimientos.fxml", LabelNumCuenta.getText().trim());
        } else {
            noti.Info("No Existe Numero de Cuenta", "!!Señor Usuario Selecione Una Cuenta Por Favor!", Pos.TOP_LEFT);
        }
    }
    
    @FXML
    private void cerrar(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
      
    }
    
    @FXML
    private void refresh(MouseEvent event) {
        resferestabla(labelcodigo.getText());
    }
    
    public void resferestabla(String codeCliente) {
        if (codeCliente != null) {
            tableviewscuentas.getItems().clear();
            cargartablaCuentas(labelcodigo.getText().trim());
        } else {
            System.out.println("no datos");
        }
        
    }

}
