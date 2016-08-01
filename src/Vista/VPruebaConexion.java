/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import conexion.Conexion;
import entidades.Persona;
import modelos.CVenta;
import modelos.FabricaOperacion;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class VPruebaConexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Conexion c = Conexion.getConexion("yes123", "yes123");
        c.cerrarConexion();
         }
    
    
}
