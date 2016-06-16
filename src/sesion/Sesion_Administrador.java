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
import javax.persistence.NoResultException;

/**
 *
 * @author kevin
 */
public class Sesion_Administrador{

    
    private Sesion sesion;
    
    public Sesion_Administrador() {
    }
    
    public void autentificacion(String usuario, String pass) throws FileNotFoundException, IOException{
        Conexion conexion = Conexion.getConexion(usuario, pass);
        Query q = conexion.getEmf().createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenia");
        q.setParameter("usuario", usuario);
        q.setParameter("contrasenia", pass);
        try{
            Usuario auxiliarUsuario = (Usuario) q.getSingleResult();
            this.sesion = new Sesion();
            this.sesion.setUsuario(auxiliarUsuario);
            this.sesion.setActiva(true);
            try{
                
                FileOutputStream archivo = new FileOutputStream("temp025.hrr");//Creamos el archivo
                ObjectOutputStream objeto = new ObjectOutputStream(archivo);//Esta clase tiene el método writeObject() que necesitamos
                objeto.writeObject(this.sesion);
                objeto.close();//Hay que cerrar siempre el archivo
              }catch(FileNotFoundException e){
                e.printStackTrace();
              }catch(IOException e){
                e.printStackTrace();
              }
        }catch(NoResultException ex){
            
        }
    }
    
    public Usuario getUsuario(){
        Usuario usuario = null;
        try{
            FileInputStream archivo = new FileInputStream("temp025.hrr");
            ObjectInputStream objeto = new ObjectInputStream(archivo);
            this.sesion = (Sesion) objeto.readObject();
            objeto.close(); archivo.close();
            usuario = sesion.getUsuario();
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return usuario;
    }
    
    public boolean isActiva(){
        boolean activa = false;
        try{
            FileInputStream archivo = new FileInputStream("temp025.hrr");
            ObjectInputStream objeto = new ObjectInputStream(archivo);
            this.sesion = (Sesion) objeto.readObject();
            objeto.close(); archivo.close();
            activa = sesion.isActiva();
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return activa;
    }
    
    public void cerrarSesion(){
        try{
            FileInputStream archivoAbrir = new FileInputStream("temp025.hrr");
            ObjectInputStream objetoAbrir = new ObjectInputStream(archivoAbrir);
            this.sesion = (Sesion) objetoAbrir.readObject();
            objetoAbrir.close(); archivoAbrir.close();
            this.sesion.setActiva(false);
            this.sesion.setUsuario(null);
            
            FileOutputStream archivo = new FileOutputStream("temp025.hrr");
            ObjectOutputStream objeto = new ObjectOutputStream(archivo);//Esta clase tiene el método writeObject() que necesitamos
            objeto.writeObject(this.sesion);
            objeto.close();//Hay que cerrar siempre el archivo
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
