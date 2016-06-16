/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import conexion.Conexion;
import controladores.DetalleventaJpaController;
import controladores.PersonaJpaController;
import controladores.VentaJpaController;
import entidades.Departamento;
import entidades.Persona;
import entidades.Producto;
import static entidades.Producto_.producto;
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
    public ArrayList<CProducto> productos;
    public Venta venta;
    public Persona persona;
   public CVenta() {
       
           controladorVenta = new VentaJpaController(Conexion.getConexion().getEmf());
           venta = new Venta();
        
    }
    
    public void crearPersona() {
        //Recibe Persona/datos de persona y la almacena en base de datos.
        controladorPersona.create(persona);
    }

    public void crearDepartamento() {
        
    }

    public void agregarProducto(CProducto producto) {
        productos.add(producto);
    }

    public void quitarProducto(int posicion) {
        productos.remove(posicion);
    }

    public Boolean finalizarVenta() {
        //Guardar venta en la base de datos
        productos = new ArrayList<>();
        controladorVenta.create(venta);
        //Recorrer lista de productos y guardar cada objeto como DetalleVenta en base de datos.
        //Retornar verdadero/falso si la venta se hizo exitosamente o no.
        for (CProducto nomProd:productos){
            
            
           }
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
