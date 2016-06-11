/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Presentacion;
import entidades.Renglon;
import entidades.Cuenta;
import entidades.Detalleventa;
import java.util.ArrayList;
import java.util.List;
import entidades.Kardex;
import entidades.Detallecompra;
import entidades.Producto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getDetalleventaList() == null) {
            producto.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        if (producto.getKardexList() == null) {
            producto.setKardexList(new ArrayList<Kardex>());
        }
        if (producto.getDetallecompraList() == null) {
            producto.setDetallecompraList(new ArrayList<Detallecompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Presentacion idPresentacion = producto.getIdPresentacion();
            if (idPresentacion != null) {
                idPresentacion = em.getReference(idPresentacion.getClass(), idPresentacion.getIdPresentacion());
                producto.setIdPresentacion(idPresentacion);
            }
            Renglon idRenglon = producto.getIdRenglon();
            if (idRenglon != null) {
                idRenglon = em.getReference(idRenglon.getClass(), idRenglon.getIdRenglon());
                producto.setIdRenglon(idRenglon);
            }
            Cuenta idCuenta = producto.getIdCuenta();
            if (idCuenta != null) {
                idCuenta = em.getReference(idCuenta.getClass(), idCuenta.getIdCuenta());
                producto.setIdCuenta(idCuenta);
            }
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : producto.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(), detalleventaListDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            producto.setDetalleventaList(attachedDetalleventaList);
            List<Kardex> attachedKardexList = new ArrayList<Kardex>();
            for (Kardex kardexListKardexToAttach : producto.getKardexList()) {
                kardexListKardexToAttach = em.getReference(kardexListKardexToAttach.getClass(), kardexListKardexToAttach.getIdKardex());
                attachedKardexList.add(kardexListKardexToAttach);
            }
            producto.setKardexList(attachedKardexList);
            List<Detallecompra> attachedDetallecompraList = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListDetallecompraToAttach : producto.getDetallecompraList()) {
                detallecompraListDetallecompraToAttach = em.getReference(detallecompraListDetallecompraToAttach.getClass(), detallecompraListDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraList.add(detallecompraListDetallecompraToAttach);
            }
            producto.setDetallecompraList(attachedDetallecompraList);
            em.persist(producto);
            if (idPresentacion != null) {
                idPresentacion.getProductoList().add(producto);
                idPresentacion = em.merge(idPresentacion);
            }
            if (idRenglon != null) {
                idRenglon.getProductoList().add(producto);
                idRenglon = em.merge(idRenglon);
            }
            if (idCuenta != null) {
                idCuenta.getProductoList().add(producto);
                idCuenta = em.merge(idCuenta);
            }
            for (Detalleventa detalleventaListDetalleventa : producto.getDetalleventaList()) {
                Producto oldIdProductoOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getIdProducto();
                detalleventaListDetalleventa.setIdProducto(producto);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldIdProductoOfDetalleventaListDetalleventa != null) {
                    oldIdProductoOfDetalleventaListDetalleventa.getDetalleventaList().remove(detalleventaListDetalleventa);
                    oldIdProductoOfDetalleventaListDetalleventa = em.merge(oldIdProductoOfDetalleventaListDetalleventa);
                }
            }
            for (Kardex kardexListKardex : producto.getKardexList()) {
                Producto oldIdProductoOfKardexListKardex = kardexListKardex.getIdProducto();
                kardexListKardex.setIdProducto(producto);
                kardexListKardex = em.merge(kardexListKardex);
                if (oldIdProductoOfKardexListKardex != null) {
                    oldIdProductoOfKardexListKardex.getKardexList().remove(kardexListKardex);
                    oldIdProductoOfKardexListKardex = em.merge(oldIdProductoOfKardexListKardex);
                }
            }
            for (Detallecompra detallecompraListDetallecompra : producto.getDetallecompraList()) {
                Producto oldIdProductoOfDetallecompraListDetallecompra = detallecompraListDetallecompra.getIdProducto();
                detallecompraListDetallecompra.setIdProducto(producto);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
                if (oldIdProductoOfDetallecompraListDetallecompra != null) {
                    oldIdProductoOfDetallecompraListDetallecompra.getDetallecompraList().remove(detallecompraListDetallecompra);
                    oldIdProductoOfDetallecompraListDetallecompra = em.merge(oldIdProductoOfDetallecompraListDetallecompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Presentacion idPresentacionOld = persistentProducto.getIdPresentacion();
            Presentacion idPresentacionNew = producto.getIdPresentacion();
            Renglon idRenglonOld = persistentProducto.getIdRenglon();
            Renglon idRenglonNew = producto.getIdRenglon();
            Cuenta idCuentaOld = persistentProducto.getIdCuenta();
            Cuenta idCuentaNew = producto.getIdCuenta();
            List<Detalleventa> detalleventaListOld = persistentProducto.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = producto.getDetalleventaList();
            List<Kardex> kardexListOld = persistentProducto.getKardexList();
            List<Kardex> kardexListNew = producto.getKardexList();
            List<Detallecompra> detallecompraListOld = persistentProducto.getDetallecompraList();
            List<Detallecompra> detallecompraListNew = producto.getDetallecompraList();
            List<String> illegalOrphanMessages = null;
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleventa " + detalleventaListOldDetalleventa + " since its idProducto field is not nullable.");
                }
            }
            for (Kardex kardexListOldKardex : kardexListOld) {
                if (!kardexListNew.contains(kardexListOldKardex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Kardex " + kardexListOldKardex + " since its idProducto field is not nullable.");
                }
            }
            for (Detallecompra detallecompraListOldDetallecompra : detallecompraListOld) {
                if (!detallecompraListNew.contains(detallecompraListOldDetallecompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecompra " + detallecompraListOldDetallecompra + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPresentacionNew != null) {
                idPresentacionNew = em.getReference(idPresentacionNew.getClass(), idPresentacionNew.getIdPresentacion());
                producto.setIdPresentacion(idPresentacionNew);
            }
            if (idRenglonNew != null) {
                idRenglonNew = em.getReference(idRenglonNew.getClass(), idRenglonNew.getIdRenglon());
                producto.setIdRenglon(idRenglonNew);
            }
            if (idCuentaNew != null) {
                idCuentaNew = em.getReference(idCuentaNew.getClass(), idCuentaNew.getIdCuenta());
                producto.setIdCuenta(idCuentaNew);
            }
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(detalleventaListNewDetalleventaToAttach.getClass(), detalleventaListNewDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            producto.setDetalleventaList(detalleventaListNew);
            List<Kardex> attachedKardexListNew = new ArrayList<Kardex>();
            for (Kardex kardexListNewKardexToAttach : kardexListNew) {
                kardexListNewKardexToAttach = em.getReference(kardexListNewKardexToAttach.getClass(), kardexListNewKardexToAttach.getIdKardex());
                attachedKardexListNew.add(kardexListNewKardexToAttach);
            }
            kardexListNew = attachedKardexListNew;
            producto.setKardexList(kardexListNew);
            List<Detallecompra> attachedDetallecompraListNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListNewDetallecompraToAttach : detallecompraListNew) {
                detallecompraListNewDetallecompraToAttach = em.getReference(detallecompraListNewDetallecompraToAttach.getClass(), detallecompraListNewDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraListNew.add(detallecompraListNewDetallecompraToAttach);
            }
            detallecompraListNew = attachedDetallecompraListNew;
            producto.setDetallecompraList(detallecompraListNew);
            producto = em.merge(producto);
            if (idPresentacionOld != null && !idPresentacionOld.equals(idPresentacionNew)) {
                idPresentacionOld.getProductoList().remove(producto);
                idPresentacionOld = em.merge(idPresentacionOld);
            }
            if (idPresentacionNew != null && !idPresentacionNew.equals(idPresentacionOld)) {
                idPresentacionNew.getProductoList().add(producto);
                idPresentacionNew = em.merge(idPresentacionNew);
            }
            if (idRenglonOld != null && !idRenglonOld.equals(idRenglonNew)) {
                idRenglonOld.getProductoList().remove(producto);
                idRenglonOld = em.merge(idRenglonOld);
            }
            if (idRenglonNew != null && !idRenglonNew.equals(idRenglonOld)) {
                idRenglonNew.getProductoList().add(producto);
                idRenglonNew = em.merge(idRenglonNew);
            }
            if (idCuentaOld != null && !idCuentaOld.equals(idCuentaNew)) {
                idCuentaOld.getProductoList().remove(producto);
                idCuentaOld = em.merge(idCuentaOld);
            }
            if (idCuentaNew != null && !idCuentaNew.equals(idCuentaOld)) {
                idCuentaNew.getProductoList().add(producto);
                idCuentaNew = em.merge(idCuentaNew);
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Producto oldIdProductoOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa.getIdProducto();
                    detalleventaListNewDetalleventa.setIdProducto(producto);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldIdProductoOfDetalleventaListNewDetalleventa != null && !oldIdProductoOfDetalleventaListNewDetalleventa.equals(producto)) {
                        oldIdProductoOfDetalleventaListNewDetalleventa.getDetalleventaList().remove(detalleventaListNewDetalleventa);
                        oldIdProductoOfDetalleventaListNewDetalleventa = em.merge(oldIdProductoOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            for (Kardex kardexListNewKardex : kardexListNew) {
                if (!kardexListOld.contains(kardexListNewKardex)) {
                    Producto oldIdProductoOfKardexListNewKardex = kardexListNewKardex.getIdProducto();
                    kardexListNewKardex.setIdProducto(producto);
                    kardexListNewKardex = em.merge(kardexListNewKardex);
                    if (oldIdProductoOfKardexListNewKardex != null && !oldIdProductoOfKardexListNewKardex.equals(producto)) {
                        oldIdProductoOfKardexListNewKardex.getKardexList().remove(kardexListNewKardex);
                        oldIdProductoOfKardexListNewKardex = em.merge(oldIdProductoOfKardexListNewKardex);
                    }
                }
            }
            for (Detallecompra detallecompraListNewDetallecompra : detallecompraListNew) {
                if (!detallecompraListOld.contains(detallecompraListNewDetallecompra)) {
                    Producto oldIdProductoOfDetallecompraListNewDetallecompra = detallecompraListNewDetallecompra.getIdProducto();
                    detallecompraListNewDetallecompra.setIdProducto(producto);
                    detallecompraListNewDetallecompra = em.merge(detallecompraListNewDetallecompra);
                    if (oldIdProductoOfDetallecompraListNewDetallecompra != null && !oldIdProductoOfDetallecompraListNewDetallecompra.equals(producto)) {
                        oldIdProductoOfDetallecompraListNewDetallecompra.getDetallecompraList().remove(detallecompraListNewDetallecompra);
                        oldIdProductoOfDetallecompraListNewDetallecompra = em.merge(oldIdProductoOfDetallecompraListNewDetallecompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleventa> detalleventaListOrphanCheck = producto.getDetalleventaList();
            for (Detalleventa detalleventaListOrphanCheckDetalleventa : detalleventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Detalleventa " + detalleventaListOrphanCheckDetalleventa + " in its detalleventaList field has a non-nullable idProducto field.");
            }
            List<Kardex> kardexListOrphanCheck = producto.getKardexList();
            for (Kardex kardexListOrphanCheckKardex : kardexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Kardex " + kardexListOrphanCheckKardex + " in its kardexList field has a non-nullable idProducto field.");
            }
            List<Detallecompra> detallecompraListOrphanCheck = producto.getDetallecompraList();
            for (Detallecompra detallecompraListOrphanCheckDetallecompra : detallecompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Detallecompra " + detallecompraListOrphanCheckDetallecompra + " in its detallecompraList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Presentacion idPresentacion = producto.getIdPresentacion();
            if (idPresentacion != null) {
                idPresentacion.getProductoList().remove(producto);
                idPresentacion = em.merge(idPresentacion);
            }
            Renglon idRenglon = producto.getIdRenglon();
            if (idRenglon != null) {
                idRenglon.getProductoList().remove(producto);
                idRenglon = em.merge(idRenglon);
            }
            Cuenta idCuenta = producto.getIdCuenta();
            if (idCuenta != null) {
                idCuenta.getProductoList().remove(producto);
                idCuenta = em.merge(idCuenta);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
