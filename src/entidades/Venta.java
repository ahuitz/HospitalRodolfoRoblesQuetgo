/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.idVenta = :idVenta"),
    @NamedQuery(name = "Venta.findByNoRequisicion", query = "SELECT v FROM Venta v WHERE v.noRequisicion = :noRequisicion"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Venta.findByTotal", query = "SELECT v FROM Venta v WHERE v.total = :total"),
    @NamedQuery(name = "Venta.findByIdServicio", query = "SELECT v FROM Venta v WHERE v.idServicio = :idServicio")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idVenta;
    @Basic(optional = false)
    private int noRequisicion;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double total;
    @Basic(optional = false)
    private int idServicio;
    @JoinColumn(name = "idJefeServicio", referencedColumnName = "idPersona")
    @ManyToOne(optional = false)
    private Persona idJefeServicio;
    @JoinColumn(name = "idEntregadoPor", referencedColumnName = "idPersona")
    @ManyToOne(optional = false)
    private Persona idEntregadoPor;
    @JoinColumn(name = "idRecibidoPor", referencedColumnName = "idPersona")
    @ManyToOne(optional = false)
    private Persona idRecibidoPor;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenta")
    private List<Detalleventa> detalleventaList;

    public Venta() {
    }

    public Venta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Venta(Integer idVenta, int noRequisicion, Date fecha, int idServicio) {
        this.idVenta = idVenta;
        this.noRequisicion = noRequisicion;
        this.fecha = fecha;
        this.idServicio = idServicio;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public int getNoRequisicion() {
        return noRequisicion;
    }

    public void setNoRequisicion(int noRequisicion) {
        this.noRequisicion = noRequisicion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public Persona getIdJefeServicio() {
        return idJefeServicio;
    }

    public void setIdJefeServicio(Persona idJefeServicio) {
        this.idJefeServicio = idJefeServicio;
    }

    public Persona getIdEntregadoPor() {
        return idEntregadoPor;
    }

    public void setIdEntregadoPor(Persona idEntregadoPor) {
        this.idEntregadoPor = idEntregadoPor;
    }

    public Persona getIdRecibidoPor() {
        return idRecibidoPor;
    }

    public void setIdRecibidoPor(Persona idRecibidoPor) {
        this.idRecibidoPor = idRecibidoPor;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<Detalleventa> getDetalleventaList() {
        return detalleventaList;
    }

    public void setDetalleventaList(List<Detalleventa> detalleventaList) {
        this.detalleventaList = detalleventaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVenta != null ? idVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.idVenta == null && other.idVenta != null) || (this.idVenta != null && !this.idVenta.equals(other.idVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Venta[ idVenta=" + idVenta + " ]";
    }
    
}
