/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTablas;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelos.CProducto;

/**
 *
 * @author Rosario
 */
public class ModeloProductoC extends AbstractTableModel{

    private List<CProducto> producto;
    private String columnas[]={"Cantida","Nombre","Subtotal"};
    private Class[] tipos = new Class[]{java.lang.Double.class, java.lang.String.class,java.lang.Double.class};

    public ModeloProductoC(List<CProducto> producto) {
        this.producto = producto;
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
        CProducto c = this.producto.get(rowIndex);
        
        switch (columnIndex){
            case 0:
                return c.cantidad;
            case 1:
                return c.p.getProducto();
            case 2:
                return c.p.getExistencia()*c.cantidad;
                default:return null;
        }
            
    }
    @Override
    public String getColumnName(int columnIndex){
        return columnas[columnIndex];
    }
    @Override
    public Class getColumnClass(int columnIndex){
        return tipos[columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if(columnIndex>2){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex){
        
    }
    public List<CProducto> getproductos(){
        return producto;
    }
}
