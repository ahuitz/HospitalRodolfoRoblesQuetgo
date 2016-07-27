/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Compra;
import entidades.Detallecompra;
import entidades.Lote;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class DetallecompraJpaController implements Serializable {

    public DetallecompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecompra detallecompra) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra idCompra = detallecompra.getIdCompra();
            if (idCompra != null) {
                idCompra = em.getReference(idCompra.getClass(), idCompra.getIdCompra());
                detallecompra.setIdCompra(idCompra);
            }
            Lote idLote = detallecompra.getIdLote();
            if (idLote != null) {
                idLote = em.getReference(idLote.getClass(), idLote.getIdLote());
                detallecompra.setIdLote(idLote);
            }
            em.persist(detallecompra);
            if (idCompra != null) {
                idCompra.getDetallecompraList().add(detallecompra);
                idCompra = em.merge(idCompra);
            }
            if (idLote != null) {
                idLote.getDetallecompraList().add(detallecompra);
                idLote = em.merge(idLote);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallecompra detallecompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallecompra persistentDetallecompra = em.find(Detallecompra.class, detallecompra.getIdDetalleCompra());
            Compra idCompraOld = persistentDetallecompra.getIdCompra();
            Compra idCompraNew = detallecompra.getIdCompra();
            Lote idLoteOld = persistentDetallecompra.getIdLote();
            Lote idLoteNew = detallecompra.getIdLote();
            if (idCompraNew != null) {
                idCompraNew = em.getReference(idCompraNew.getClass(), idCompraNew.getIdCompra());
                detallecompra.setIdCompra(idCompraNew);
            }
            if (idLoteNew != null) {
                idLoteNew = em.getReference(idLoteNew.getClass(), idLoteNew.getIdLote());
                detallecompra.setIdLote(idLoteNew);
            }
            detallecompra = em.merge(detallecompra);
            if (idCompraOld != null && !idCompraOld.equals(idCompraNew)) {
                idCompraOld.getDetallecompraList().remove(detallecompra);
                idCompraOld = em.merge(idCompraOld);
            }
            if (idCompraNew != null && !idCompraNew.equals(idCompraOld)) {
                idCompraNew.getDetallecompraList().add(detallecompra);
                idCompraNew = em.merge(idCompraNew);
            }
            if (idLoteOld != null && !idLoteOld.equals(idLoteNew)) {
                idLoteOld.getDetallecompraList().remove(detallecompra);
                idLoteOld = em.merge(idLoteOld);
            }
            if (idLoteNew != null && !idLoteNew.equals(idLoteOld)) {
                idLoteNew.getDetallecompraList().add(detallecompra);
                idLoteNew = em.merge(idLoteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = detallecompra.getIdDetalleCompra();
                if (findDetallecompra(id) == null) {
                    throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.");
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
            Detallecompra detallecompra;
            try {
                detallecompra = em.getReference(Detallecompra.class, id);
                detallecompra.getIdDetalleCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.", enfe);
            }
            Compra idCompra = detallecompra.getIdCompra();
            if (idCompra != null) {
                idCompra.getDetallecompraList().remove(detallecompra);
                idCompra = em.merge(idCompra);
            }
            Lote idLote = detallecompra.getIdLote();
            if (idLote != null) {
                idLote.getDetallecompraList().remove(detallecompra);
                idLote = em.merge(idLote);
            }
            em.remove(detallecompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallecompra> findDetallecompraEntities() {
        return findDetallecompraEntities(true, -1, -1);
    }

    public List<Detallecompra> findDetallecompraEntities(int maxResults, int firstResult) {
        return findDetallecompraEntities(false, maxResults, firstResult);
    }

    private List<Detallecompra> findDetallecompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecompra.class));
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

    public Detallecompra findDetallecompra(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecompra> rt = cq.from(Detallecompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
