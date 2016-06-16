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
 * @author kevin
 */
public class Sesion implements Serializable{
    private Usuario usuario;
    private Persona persona;
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
