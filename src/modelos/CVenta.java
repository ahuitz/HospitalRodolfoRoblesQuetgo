/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import controladores.DetalleventaJpaController;
import controladores.PersonaJpaController;
import controladores.VentaJpaController;
import entidades.Departamento;
import entidades.Producto;
import entidades.Venta;
import java.util.ArrayList;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class CVenta extends COperacion {

    private VentaJpaController controladorVenta;
    private PersonaJpaController controladorPersona;
    private DetalleventaJpaController controladorDetalleVenta;
    private Departamento controladorDepartamento;
    public ArrayList<Producto> productos;
    public Venta venta;

    public void crearCliente() {
    }

    public void crearDepartamento() {
    }

    public void agregarProducto(CProducto producto) {
    }

    public void quitarProducto(int posicion) {
    }

    public Boolean finalizarVenta() {
        return null;
    }

    @Override
    public double getTotal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VentaJpaController getControladorVenta() {
        return controladorVenta;
    }

    public PersonaJpaController getControladorPersona() {
        return controladorPersona;
    }

    public DetalleventaJpaController getControladorDetalleVenta() {
        return controladorDetalleVenta;
    }

    public Departamento getControladorDepartamento() {
        return controladorDepartamento;
    }

}
