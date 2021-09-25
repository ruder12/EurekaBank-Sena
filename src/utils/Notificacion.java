/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class Notificacion {

    public void Confirmar(String Titulo, String msj, Pos posision) {
        Notifications builder = Notifications.create()
                .title(Titulo)
                .text(msj)
                .graphic(new ImageView(this.getClass().getResource("img/ok.png").toString()))
                .hideAfter(Duration.seconds(5))
                .position(posision);
        builder.show();
    }

    public void Info(String Titulo, String msj, Pos posision) {
        Notifications builder = Notifications.create()
                .title(Titulo)
                .text(msj)
                .graphic(new ImageView(this.getClass().getResource("img/alert.png").toString()))
                .hideAfter(Duration.seconds(5))
                .position(posision);

        builder.show();
    }

    public void Error(String Titulo, String msj, Pos posision) {
        Notifications builder = Notifications.create()
                .title(Titulo)
                .text(msj)
                .graphic(new ImageView(this.getClass().getResource("img/error.png").toString()))
                .hideAfter(Duration.seconds(5))
                .position(posision);

        builder.show();
    }

    public void InfoAyuda(String Titulo, String msj, Pos posision) {
        Notifications builder = Notifications.create()
                .title(Titulo)
                .text(msj)
                .graphic(new ImageView(this.getClass().getResource("img/help.png").toString()))
                .hideAfter(Duration.seconds(30))
                .position(posision);

        builder.show();
    }
}
