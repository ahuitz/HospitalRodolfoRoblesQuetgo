/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTablas;

import entidades.Producto;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rosario
 */
public class ModeloProducto extends AbstractTableModel{

    private List<Producto> producto;
    private String columnas[]={"Codigo","Nombre","Existencia"};
    private Class[] tipos = new Class[]{java.lang.Integer.class, java.lang.String.class, java.lang.Double.class};
    
    public ModeloProducto(List<Producto> producto){
        this.producto=producto; 
    }
    
    @Override
    public int getRowCount() {
        return producto.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Producto c = this.producto.get(rowIndex);
        
        switch(columnIndex){
            case 0:
                return c.getCodigo();
            case 1:
                return c.getProducto();
            case 2:
                return c.getExistencia();
            default:return null;
        }
    }
    @Override
    public String getColumnName(int columnIndex){
        return columnas[columnIndex];
    }
    
    
    @Override
    public Class getColumnClass(int columnIndex) {
    return tipos [columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if(columnIndex>2){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex){
        
    }
    public List<Producto> getLote(){
        return producto;
    }
    
}
