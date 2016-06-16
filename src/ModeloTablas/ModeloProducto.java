/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTablas;

import conexion.Conexion;
import entidades.Cuenta;
import entidades.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 * @author Miguel Diaz <modm.1993.12@gmail.com>
 */
public class ModeloProducto extends AbstractTableModel {

    private List<Producto> producto; // Listado de cuentas que irian en ingreso
    private String columnas[] = {"Codigo", "Nombre", "Precio", "Existencia"};   // Datos de cada cuenta.
    private Class[] tipos = new Class[]{java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class};    // Tipo de dato de cada columna.
    
    public ModeloProducto(List<Producto> producto) {
        this.producto = producto;
        
    }

    /**
     *
     * @return numero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return producto.size();
    }

    /**
     *
     * @return numero de columnas de la tabla.
     */
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return valor de la fila, columna.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Producto c = this.producto.get(rowIndex);
        
        
        
        switch (columnIndex) {
            //evalua si pone el signo o no
            case 0:
                return c.getCodigo();
                
            case 1:
                return c.getProducto();
            case 2:
                return c.getPrecio();
            case 3:
                return c.getExistencia();
                
            default:return null;
        }
    }

    /**
     *
     * @param columnIndex
     * @return nombre de la columna.
     */
    @Override
    public String getColumnName(int columnIndex){
        return columnas[columnIndex];
    }
    
    /**
     * 
     * @param columnIndex
     * @return tipo de dato de la columna
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return tipos [columnIndex];
    }
    
    /**
     * Permite editar las columnas de giro, venta, mercaderia y costo.
     * @param rowIndex
     * @param columnIndex
     * @return columna editable o no.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex>2){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * @param aValue
     * @param rowIndex
     * @param columnIndex 
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
       
    }
    
    /**
     * 
     * @return cuentas del estado financiero.
     */
    public List<Producto> getCuentas(){
        return producto;
    }
    
 
   
}
