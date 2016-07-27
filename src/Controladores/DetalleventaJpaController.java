/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import entidades.Detalleventa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Lote;
import entidades.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class DetalleventaJpaController implements Serializable {

    public DetalleventaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleventa detalleventa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lote idLote = detalleventa.getIdLote();
            if (idLote != null) {
                idLote = em.getReference(idLote.getClass(), idLote.getIdLote());
                detalleventa.setIdLote(idLote);
            }
            Venta idVenta = detalleventa.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getIdVenta());
                detalleventa.setIdVenta(idVenta);
            }
            em.persist(detalleventa);
            if (idLote != null) {
                idLote.getDetalleventaList().add(detalleventa);
                idLote = em.merge(idLote);
            }
            if (idVenta != null) {
                idVenta.getDetalleventaList().add(detalleventa);
                idVenta = em.merge(idVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleventa detalleventa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleventa persistentDetalleventa = em.find(Detalleventa.class, detalleventa.getIdDetalleVenta());
            Lote idLoteOld = persistentDetalleventa.getIdLote();
            Lote idLoteNew = detalleventa.getIdLote();
            Venta idVentaOld = persistentDetalleventa.getIdVenta();
            Venta idVentaNew = detalleventa.getIdVenta();
            if (idLoteNew != null) {
                idLoteNew = em.getReference(idLoteNew.getClass(), idLoteNew.getIdLote());
                detalleventa.setIdLote(idLoteNew);
            }
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getIdVenta());
                detalleventa.setIdVenta(idVentaNew);
            }
            detalleventa = em.merge(detalleventa);
            if (idLoteOld != null && !idLoteOld.equals(idLoteNew)) {
                idLoteOld.getDetalleventaList().remove(detalleventa);
                idLoteOld = em.merge(idLoteOld);
            }
            if (idLoteNew != null && !idLoteNew.equals(idLoteOld)) {
                idLoteNew.getDetalleventaList().add(detalleventa);
                idLoteNew = em.merge(idLoteNew);
            }
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getDetalleventaList().remove(detalleventa);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getDetalleventaList().add(detalleventa);
                idVentaNew = em.merge(idVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = detalleventa.getIdDetalleVenta();
                if (findDetalleventa(id) == null) {
                    throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleventa detalleventa;
            try {
                detalleventa = em.getReference(Detalleventa.class, id);
                detalleventa.getIdDetalleVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.", enfe);
            }
            Lote idLote = detalleventa.getIdLote();
            if (idLote != null) {
                idLote.getDetalleventaList().remove(detalleventa);
                idLote = em.merge(idLote);
            }
            Venta idVenta = detalleventa.getIdVenta();
            if (idVenta != null) {
                idVenta.getDetalleventaList().remove(detalleventa);
                idVenta = em.merge(idVenta);
            }
            em.remove(detalleventa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleventa> findDetalleventaEntities() {
        return findDetalleventaEntities(true, -1, -1);
    }

    public List<Detalleventa> findDetalleventaEntities(int maxResults, int firstResult) {
        return findDetalleventaEntities(false, maxResults, firstResult);
    }

    private List<Detalleventa> findDetalleventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleventa.class));
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

    public Detalleventa findDetalleventa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleventa> rt = cq.from(Detalleventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
