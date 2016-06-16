package conexion;

import entidades.Usuario;
import java.sql.Statement;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class Conexion {

    private EntityManagerFactory emf;
    private static Conexion unicaConexion;
    private int idUsuario;

    public static Conexion getConexion(String usuario, String contrasenia) {
        if (unicaConexion == null) {
            try {
                unicaConexion = new Conexion(usuario, contrasenia);
            } catch (Exception ex) {
                unicaConexion = null;
            }
        }
        return unicaConexion;
    }

    public static Conexion getConexion(){
        return unicaConexion;
    }

    private Conexion(String usuario, String contrasenia) {
        try{
            emf = Persistence.createEntityManagerFactory("HospitalRoblesPU");
            Query q = emf.createEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasenia");
            q.setParameter("usuario", usuario);
            q.setParameter("contrasenia", contrasenia);
            try{
                Usuario verificar = (Usuario) q.getSingleResult();
                idUsuario = verificar.getIdUsuario();
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (NoResultException ex) {
                idUsuario=0;
                JOptionPane.showMessageDialog(null, "Usuario y/o contraseña inválidos.", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            emf=null;            emf = Persistence.createEntityManagerFactory("HospitalRoblesPU");

            JOptionPane.showMessageDialog(null, "Imposible establecer conexión con el servidor.\n\rRevise su conexión e intentelo de nuevo.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cerrarConexion() {
        idUsuario = 0;
        try{
            emf.close();
        } catch(NullPointerException ex){
        }
        emf = null;
        unicaConexion = null;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public EntityManagerFactory getEmf() {
        return emf;
    }


}
