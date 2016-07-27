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
@Table(name = "detalleventa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleventa.findAll", query = "SELECT d FROM Detalleventa d"),
    @NamedQuery(name = "Detalleventa.findByIdDetalleVenta", query = "SELECT d FROM Detalleventa d WHERE d.idDetalleVenta = :idDetalleVenta"),
    @NamedQuery(name = "Detalleventa.findByCantidadSolicitada", query = "SELECT d FROM Detalleventa d WHERE d.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "Detalleventa.findByCantidadEntregada", query = "SELECT d FROM Detalleventa d WHERE d.cantidadEntregada = :cantidadEntregada"),
    @NamedQuery(name = "Detalleventa.findByPrecio", query = "SELECT d FROM Detalleventa d WHERE d.precio = :precio"),
    @NamedQuery(name = "Detalleventa.findBySubtotal", query = "SELECT d FROM Detalleventa d WHERE d.subtotal = :subtotal")})
public class Detalleventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleVenta")
    private Long idDetalleVenta;
    @Basic(optional = false)
    @Column(name = "CantidadSolicitada")
    private int cantidadSolicitada;
    @Basic(optional = false)
    @Column(name = "CantidadEntregada")
    private int cantidadEntregada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Precio")
    private Double precio;
    @Column(name = "Subtotal")
    private Double subtotal;
    @JoinColumn(name = "idLote", referencedColumnName = "idLote")
    @ManyToOne(optional = false)
    private Lote idLote;
    @JoinColumn(name = "idVenta", referencedColumnName = "idVenta")
    @ManyToOne(optional = false)
    private Venta idVenta;

    public Detalleventa() {
    }

    public Detalleventa(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Detalleventa(Long idDetalleVenta, int cantidadSolicitada, int cantidadEntregada) {
        this.idDetalleVenta = idDetalleVenta;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadEntregada = cantidadEntregada;
    }

    public Long getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(int cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Lote getIdLote() {
        return idLote;
    }

    public void setIdLote(Lote idLote) {
        this.idLote = idLote;
    }

    public Venta getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Venta idVenta) {
        this.idVenta = idVenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleVenta != null ? idDetalleVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleventa)) {
            return false;
        }
        Detalleventa other = (Detalleventa) object;
        if ((this.idDetalleVenta == null && other.idDetalleVenta != null) || (this.idDetalleVenta != null && !this.idDetalleVenta.equals(other.idDetalleVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Detalleventa[ idDetalleVenta=" + idDetalleVenta + " ]";
    }
    
}
