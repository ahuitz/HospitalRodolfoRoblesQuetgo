/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import conexion.Conexion;

/**
 *
 * @author modm_
 */
public class VPrueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Conexion c = Conexion.getConexion("root", "root");
        c.cerrarConexion();
    }
    
}
