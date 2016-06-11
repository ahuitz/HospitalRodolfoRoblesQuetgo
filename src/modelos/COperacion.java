/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.Date;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public abstract class COperacion {

    public Date fecha;

    public abstract double getTotal();

    public void setFecha(Date fecha) {
    }
}
