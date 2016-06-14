/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import conexion.Conexion;
import modelos.CVenta;
import modelos.FabricaOperacion;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class PruebaConexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Conexion c = Conexion.getConexion("admin", "admin123");
        c.cerrarConexion();
        
        //Tenes que tener un objeto CVenta en el formulario de requisiciones.
        FabricaOperacion fo = new FabricaOperacion();
        CVenta nuevaVenta = (CVenta) fo.crearOperacion(0);
        nuevaVenta.agregarProducto(null/**new CProducto()**/); //obtenes los datos del producto que vas a vender y creas un objeto CProducto
    }
    
}
