/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.scene.control.CheckBox;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class permisos {

    private String modulos;
    private CheckBox v;
    private CheckBox c;
    private CheckBox e;
    private CheckBox a;

    public permisos(String modulos, boolean ve,boolean cr,boolean el,boolean ac) {
        this.modulos = modulos;
        this.v = new CheckBox();
        this.v.setSelected(ve);
        this.c = new CheckBox();
        this.c.setSelected(cr);
        this.e = new CheckBox();
        this.e.setSelected(el);
        this.a = new CheckBox();
        this.a.setSelected(ac);
    }

    public String getModulos() {
        return modulos;
    }

    public void setModulos(String modulos) {
        this.modulos = modulos;
    }

    public CheckBox isV() {
        return v;
    }

    public void setV(CheckBox v) {
        this.v = v;
    }

    public CheckBox isC() {
        return c;
    }

    public void setC(CheckBox c) {
        this.c = c;
    }

    public CheckBox isE() {
        return e;
    }

    public void setE(CheckBox e) {
        this.e = e;
    }

    public CheckBox isA() {
        return a;
    }

    public void setA(CheckBox a) {
        this.a = a;
    }
    
}
