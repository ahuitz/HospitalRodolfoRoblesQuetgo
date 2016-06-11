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
public class FabricaOperacion {

    public FabricaOperacion() {
    }

    public COperacion crearOperacion(int tipo) {
        switch (tipo) {
            case 0:
                return new CVenta();
            case 1:
                return new CCompra();
            default:
                return null;
        }
    }

}
