/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import controladores.AlmacenJpaController;
import controladores.CompraJpaController;
import controladores.CuentaJpaController;
import controladores.PresentacionJpaController;
import controladores.ProductoJpaController;
import controladores.ProveedorJpaController;
import controladores.RenglonJpaController;
import entidades.Compra;
import entidades.Producto;
import java.util.ArrayList;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class CCompra extends COperacion {

    public PresentacionJpaController controladorPresentacion;
    private CompraJpaController controladorCompra;
    private ProductoJpaController controladorProducto;
    private ProveedorJpaController controladorProveedor;
    private AlmacenJpaController controladorAlmacen;
    private RenglonJpaController controladorRenglon;
    private CuentaJpaController controladorCuenta;
    public ArrayList<Producto> productos;
    public Compra compra;

    public void crearProducto() {
    }

    public void crearPresentacion() {
    }

    public void crearProveedor() {
    }

    public void crearAlmacen() {
    }

    public void crearRenglon() {
    }

    public void crearCuenta() {
    }

    public void agregarProducto(CProducto producto) {
    }

    public void quitarProducto(int posicion) {
    }

    public Boolean finalizarCompra() {
        return null;
    }

    @Override
    public double getTotal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PresentacionJpaController getControladorPresentacion() {
        return controladorPresentacion;
    }

    public CompraJpaController getControladorCompra() {
        return controladorCompra;
    }

    public ProductoJpaController getControladorProducto() {
        return controladorProducto;
    }

    public ProveedorJpaController getControladorProveedor() {
        return controladorProveedor;
    }

    public AlmacenJpaController getControladorAlmacen() {
        return controladorAlmacen;
    }

    public RenglonJpaController getControladorRenglon() {
        return controladorRenglon;
    }

    public CuentaJpaController getControladorCuenta() {
        return controladorCuenta;
    }

}
