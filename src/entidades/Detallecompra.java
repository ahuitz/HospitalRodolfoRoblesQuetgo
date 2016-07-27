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
@Table(name = "detallecompra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d"),
    @NamedQuery(name = "Detallecompra.findByIdDetalleCompra", query = "SELECT d FROM Detallecompra d WHERE d.idDetalleCompra = :idDetalleCompra"),
    @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallecompra.findBySubtotal", query = "SELECT d FROM Detallecompra d WHERE d.subtotal = :subtotal"),
    @NamedQuery(name = "Detallecompra.findByFolioAlmacen", query = "SELECT d FROM Detallecompra d WHERE d.folioAlmacen = :folioAlmacen")})
public class Detallecompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleCompra")
    private Long idDetalleCompra;
    @Basic(optional = false)
    @Column(name = "Cantidad")
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Subtotal")
    private Double subtotal;
    @Column(name = "FolioAlmacen")
    private String folioAlmacen;
    @JoinColumn(name = "idCompra", referencedColumnName = "idCompra")
    @ManyToOne(optional = false)
    private Compra idCompra;
    @JoinColumn(name = "idLote", referencedColumnName = "idLote")
    @ManyToOne(optional = false)
    private Lote idLote;

    public Detallecompra() {
    }

    public Detallecompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Detallecompra(Long idDetalleCompra, int cantidad) {
        this.idDetalleCompra = idDetalleCompra;
        this.cantidad = cantidad;
    }

    public Long getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getFolioAlmacen() {
        return folioAlmacen;
    }

    public void setFolioAlmacen(String folioAlmacen) {
        this.folioAlmacen = folioAlmacen;
    }

    public Compra getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Compra idCompra) {
        this.idCompra = idCompra;
    }

    public Lote getIdLote() {
        return idLote;
    }

    public void setIdLote(Lote idLote) {
        this.idLote = idLote;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleCompra != null ? idDetalleCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallecompra)) {
            return false;
        }
        Detallecompra other = (Detallecompra) object;
        if ((this.idDetalleCompra == null && other.idDetalleCompra != null) || (this.idDetalleCompra != null && !this.idDetalleCompra.equals(other.idDetalleCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detallecompra[ idDetalleCompra=" + idDetalleCompra + " ]";
    }
    
}
