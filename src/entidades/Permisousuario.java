/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rosario
 */
@Entity
@Table(name = "permisousuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisousuario.findAll", query = "SELECT p FROM Permisousuario p"),
    @NamedQuery(name = "Permisousuario.findByIdPermisoUsuario", query = "SELECT p FROM Permisousuario p WHERE p.idPermisoUsuario = :idPermisoUsuario")})
public class Permisousuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPermisoUsuario")
    private Integer idPermisoUsuario;
    @JoinColumn(name = "idPermiso", referencedColumnName = "idPermiso")
    @ManyToOne(optional = false)
    private Permiso idPermiso;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Permisousuario() {
    }

    public Permisousuario(Integer idPermisoUsuario) {
        this.idPermisoUsuario = idPermisoUsuario;
    }

    public Integer getIdPermisoUsuario() {
        return idPermisoUsuario;
    }

    public void setIdPermisoUsuario(Integer idPermisoUsuario) {
        this.idPermisoUsuario = idPermisoUsuario;
    }

    public Permiso getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Permiso idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermisoUsuario != null ? idPermisoUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisousuario)) {
            return false;
        }
        Permisousuario other = (Permisousuario) object;
        if ((this.idPermisoUsuario == null && other.idPermisoUsuario != null) || (this.idPermisoUsuario != null && !this.idPermisoUsuario.equals(other.idPermisoUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Permisousuario[ idPermisoUsuario=" + idPermisoUsuario + " ]";
    }
    
}
