/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class CProducto {

    public int id;
    public String nombre;
    public int cantidad;
    public double precio;

    public CProducto() {
    }

    public double getSubtotal() {
        return this.cantidad * this.precio;
    }

}
