/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sesion;

import entidades.Persona;
import entidades.Usuario;
import java.io.Serializable;

/**
 *
 * @author Cristian Flores <thecris.danny@gmail.com>
 */
public class Sesion implements Serializable{
    private Usuario usuario = null;
    private Persona persona = null;
    private boolean activa;
    
    public Sesion() {
    }

    public Sesion(Usuario usuario, Persona persona) {
        this.usuario = usuario;
        this.persona = persona;
    }

    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    
}
