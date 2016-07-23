/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import ModeloTablas.ModeloProducto;
import ModeloTablas.ModeloProductoC;
import ModeloTablas.ModeloProveedor;
import conexion.Conexion;
import controladores.AlmacenJpaController;
import controladores.CompraJpaController;
import controladores.CuentaJpaController;
import controladores.DetallecompraJpaController;
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
    private DetallecompraJpaController controladordetalle;
    public ArrayList<CProducto> productos;
    public Compra compra;
    public Proveedor proveedor;
    double Total;

    public CCompra(){
        this.controladorPresentacion = new PresentacionJpaController(Conexion.getConexion().getEmf());
        this.controladorCompra = new CompraJpaController(Conexion.getConexion().getEmf());
        this.controladorProducto = new ProductoJpaController(Conexion.getConexion().getEmf());
        this.controladorProveedor =new ProveedorJpaController(Conexion.getConexion().getEmf());
        this.controladorAlmacen = new AlmacenJpaController(Conexion.getConexion().getEmf());
        this.controladorRenglon = new RenglonJpaController(Conexion.getConexion().getEmf());
        this.controladorCuenta = new CuentaJpaController(Conexion.getConexion().getEmf());
        this.controladordetalle = new DetallecompraJpaController(Conexion.getConexion().getEmf());
        
        this.productos =  new ArrayList<>();
        this.compra = new Compra();
        proveedor=new Proveedor();
        Total=0;
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

    public void agregarProveedor(Proveedor proveedor){
        this.proveedor=proveedor;
        
    }
    public void agregarProducto(Producto producto,int n) {
        
        productos.add(new CProducto(producto,n));
        Total=Total+producto.getPrecio()*n;
    }

    public void quitarProducto(int posicion) {
        productos.remove(posicion);
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

    public TableModel buscarProveedor(){
        
        return new ModeloProveedor(controladorProveedor.findProveedorEntities());
    }
    public TableModel buscarProveedor(String Nombre){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findByNombre");
        q.setParameter("nombre", "%"+Nombre+"%");
        return new ModeloProveedor(q.getResultList());
    }
    public TableModel buscarProveedor(int nit){
        Query q = Conexion.getConexion().getEmf().createEntityManager().createNamedQuery("Proveedor.findByNit");
        q.setParameter("nit", nit);
        return new ModeloProveedor(q.getResultList());
    }

    
    public Boolean finalizarCompra(Compra compra) {
        compra.setIdUsuario(new Usuario(Conexion.getConexion().getIdUsuario()));
        compra.setIdProveedor(proveedor);
        
        controladorCompra.create(compra);
        int id=compra.getIdCompra();
        for(CProducto p:productos){
            Detallecompra d= new Detallecompra();
            d.setCantidad(p.cantidad);
            d.setIdProducto(p.p);
            d.setSubtotal(p.cantidad*p.precio);
            controladordetalle.create(d);
            
            
        }
        
        return null;
    }

    @Override
    public double getTotal() {
        return Total;
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
