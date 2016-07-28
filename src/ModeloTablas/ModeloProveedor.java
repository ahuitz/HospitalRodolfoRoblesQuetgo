/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTablas;

import entidades.Proveedor;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rosario
 */
public class ModeloProveedor extends AbstractTableModel{

    private List<Proveedor> proveedor;
    private String columnas[] = {"Nit","Nombre","Telefono","Correo"};
    private Class[] tipos = new Class[]{java.lang.Integer.class, java.lang.String.class,java.lang.Integer.class,java.lang.String.class};

    public ModeloProveedor(List<Proveedor> proveedor) {
        this.proveedor = proveedor;
    }

    
    @Override
    public int getRowCount() {
        return proveedor.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Proveedor c = this.proveedor.get(rowIndex);
        switch (columnIndex){
            case 0:
                return c.getNit();
            case 1:
                return c.getNombre();
            case 2:
                return c.getTelefono();
            case 3:
                return c.getCorreo();
            default:return null;
        }
    }
    @Override
    public Class getColumnClass(int columnIndex){
        return tipos [columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if (columnIndex>2){
            return true;
           
        }else{
            return false;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex){
        
    }
    public List<Proveedor>getProductos(){
        return proveedor;
    }
}
