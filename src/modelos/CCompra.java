/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import ModeloTablas.ModeloProducto;
import ModeloTablas.ModeloProductoC;
import conexion.Conexion;
import controladores.AlmacenJpaController;
import controladores.CompraJpaController;
import controladores.CuentaJpaController;
import controladores.PresentacionJpaController;
import controladores.ProductoJpaController;
import controladores.ProveedorJpaController;
import controladores.RenglonJpaController;
import entidades.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.swing.table.TableModel;

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
    public ArrayList<CProducto> productos;
    public Compra compra;

    public CCompra(){
        this.controladorPresentacion = new PresentacionJpaController(Conexion.getConexion().getEmf());
        this.controladorCompra = new CompraJpaController(Conexion.getConexion().getEmf());
        this.controladorProducto = new ProductoJpaController(Conexion.getConexion().getEmf());
        this.controladorProveedor =new ProveedorJpaController(Conexion.getConexion().getEmf());
        this.controladorAlmacen = new AlmacenJpaController(Conexion.getConexion().getEmf());
        this.controladorRenglon = new RenglonJpaController(Conexion.getConexion().getEmf());
        this.controladorCuenta = new CuentaJpaController(Conexion.getConexion().getEmf());
        this.productos =  new ArrayList<>();
        this.compra = new Compra();
    }
    

    public void crearProducto(Producto prducto) {
        controladorProducto.create(prducto);
        controladorProducto.findProducto(1);
       
    }

    public void crearPresentacion(Presentacion presentacion) {
    }

    public void crearProveedor() {
    }

    public void crearAlmacen() {
    }

    public void crearRenglon() {
    }

    public void crearCuenta() {
    }

    public void agregarProducto(Producto producto,int n) {
        
        productos.add(new CProducto(producto,n));
    }

    public void quitarProducto(int posicion) {
    }
    public TableModel getProductos(){
        ModeloProductoC modelo= new ModeloProductoC(productos);
        return modelo;
    }
    public TableModel getProductos(String busqueda) {
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Producto.findBybusqueda");
        try {
            Integer.parseInt(busqueda);
            q.setParameter("busqueda", Integer.parseInt(busqueda));
            q.setParameter("busquedas", null);
            
        } catch (NumberFormatException nfe) {
            q.setParameter("busqueda", null);
            q.setParameter("busquedas", "%"+busqueda+"%");

        }
        ModeloProducto modelo = new ModeloProducto(q.getResultList());
        return modelo;
    }

    public Proveedor buscarProveedor(int id){
        return controladorProveedor.findProveedor(id);
    }
    public List<Proveedor> buscarProveedor(String Nombre){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findByNombre");
        q.setParameter("Nombre", "%"+Nombre);
        return q.getResultList();
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
