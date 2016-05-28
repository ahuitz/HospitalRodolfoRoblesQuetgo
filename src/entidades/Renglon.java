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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
@Entity
@Table(catalog = "hrobles", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Renglon.findAll", query = "SELECT r FROM Renglon r"),
    @NamedQuery(name = "Renglon.findByIdRenglon", query = "SELECT r FROM Renglon r WHERE r.idRenglon = :idRenglon"),
    @NamedQuery(name = "Renglon.findByNoRenglon", query = "SELECT r FROM Renglon r WHERE r.noRenglon = :noRenglon"),
    @NamedQuery(name = "Renglon.findByRenglon", query = "SELECT r FROM Renglon r WHERE r.renglon = :renglon")})
public class Renglon implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idRenglon;
    @Basic(optional = false)
    private int noRenglon;
    @Basic(optional = false)
    private String renglon;
    @JoinColumn(name = "idAlmacen", referencedColumnName = "idAlmacen")
    @ManyToOne(optional = false)
    private Almacen idAlmacen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRenglon")
    private List<Producto> productoList;

    public Renglon() {
    }

    public Renglon(Integer idRenglon) {
        this.idRenglon = idRenglon;
    }

    public Renglon(Integer idRenglon, int noRenglon, String renglon) {
        this.idRenglon = idRenglon;
        this.noRenglon = noRenglon;
        this.renglon = renglon;
    }

    public Integer getIdRenglon() {
        return idRenglon;
    }

    public void setIdRenglon(Integer idRenglon) {
        this.idRenglon = idRenglon;
    }

    public int getNoRenglon() {
        return noRenglon;
    }

    public void setNoRenglon(int noRenglon) {
        this.noRenglon = noRenglon;
    }

    public String getRenglon() {
        return renglon;
    }

    public void setRenglon(String renglon) {
        this.renglon = renglon;
    }

    public Almacen getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Almacen idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRenglon != null ? idRenglon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Renglon)) {
            return false;
        }
        Renglon other = (Renglon) object;
        if ((this.idRenglon == null && other.idRenglon != null) || (this.idRenglon != null && !this.idRenglon.equals(other.idRenglon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Renglon[ idRenglon=" + idRenglon + " ]";
    }
    
}
