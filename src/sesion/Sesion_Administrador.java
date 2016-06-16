/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sesion;

import entidades.Persona;
import entidades.Usuario;
import javax.persistence.Query;
import conexion.Conexion;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;

/**
 *
 * @author Cristian Flores <thecris.danny@gmail.com>
 */
public class Sesion_Administrador{

    
    private Sesion sesion;
    
    public Sesion_Administrador() {
    }
    
    public void autentificacion(String usuario, String pass) throws FileNotFoundException, IOException{
        Conexion conexion = Conexion.getConexion(usuario, pass);
        this.sesion = new Sesion();
        Query q = conexion.getEmf().createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenia");
        q.setParameter("usuario", usuario);
        q.setParameter("contrasenia", pass);
        try{
            Usuario auxiliarUsuario = (Usuario) q.getSingleResult();
            this.sesion.setUsuario(auxiliarUsuario);
            this.sesion.setActiva(true);
            escribitSesion(this.sesion);
        }catch(NoResultException ex){
            this.sesion.setActiva(false);
            escribitSesion(this.sesion);
        }
    }
    
    public Usuario getUsuario(){
        leerSesion();
        return this.sesion.getUsuario();
    }
    
    public boolean isActiva(){
        leerSesion();
        return this.sesion.isActiva();
    }
    
    public void cerrarSesion(){
        this.sesion = new Sesion();
        this.sesion.setActiva(false);
        this.sesion.setUsuario(null);
        escribitSesion(this.sesion);
    }
    
    private void escribitSesion(Sesion sesion){
        FileOutputStream archivo = null;
        try {
            archivo = new FileOutputStream("temp025.hrr");
            try (ObjectOutputStream objeto = new ObjectOutputStream(archivo)) {
                objeto.writeObject(sesion);
                //Hay que cerrar siempre el archivo
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void leerSesion(){
        ObjectInputStream objeto = null;
        try {
            FileInputStream archivo = new FileInputStream("temp025.hrr");
            objeto = new ObjectInputStream(archivo);
            this.sesion = (Sesion) objeto.readObject();
            objeto.close();
            archivo.close();
        } catch (IOException ex) {
            Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objeto.close();
            } catch (IOException ex) {
                Logger.getLogger(Sesion_Administrador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
