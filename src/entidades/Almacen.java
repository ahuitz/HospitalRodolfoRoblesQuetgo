/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rosario
 */
@Entity
@Table(name = "almacen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a"),
    @NamedQuery(name = "Almacen.findByIdAlmacen", query = "SELECT a FROM Almacen a WHERE a.idAlmacen = :idAlmacen"),
    @NamedQuery(name = "Almacen.findByNombre", query = "SELECT a FROM Almacen a WHERE a.nombre = :nombre")})
public class Almacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAlmacen")
    private Integer idAlmacen;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAlmacen")
    private List<Renglon> renglonList;

    public Almacen() {
    }

    public Almacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Almacen(Integer idAlmacen, String nombre) {
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
    }

    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Renglon> getRenglonList() {
        return renglonList;
    }

    public void setRenglonList(List<Renglon> renglonList) {
        this.renglonList = renglonList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlmacen != null ? idAlmacen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.idAlmacen == null && other.idAlmacen != null) || (this.idAlmacen != null && !this.idAlmacen.equals(other.idAlmacen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Almacen[ idAlmacen=" + idAlmacen + " ]";
    }
    
}
