/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Producto;
import entidades.Detalleventa;
import java.util.ArrayList;
import java.util.List;
import entidades.Detallecompra;
import entidades.Lote;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class LoteJpaController implements Serializable {

    public LoteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lote lote) {
        if (lote.getDetalleventaList() == null) {
            lote.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        if (lote.getDetallecompraList() == null) {
            lote.setDetallecompraList(new ArrayList<Detallecompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = lote.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                lote.setIdProducto(idProducto);
            }
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : lote.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(), detalleventaListDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            lote.setDetalleventaList(attachedDetalleventaList);
            List<Detallecompra> attachedDetallecompraList = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListDetallecompraToAttach : lote.getDetallecompraList()) {
                detallecompraListDetallecompraToAttach = em.getReference(detallecompraListDetallecompraToAttach.getClass(), detallecompraListDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraList.add(detallecompraListDetallecompraToAttach);
            }
            lote.setDetallecompraList(attachedDetallecompraList);
            em.persist(lote);
            if (idProducto != null) {
                idProducto.getLoteList().add(lote);
                idProducto = em.merge(idProducto);
            }
            for (Detalleventa detalleventaListDetalleventa : lote.getDetalleventaList()) {
                Lote oldIdLoteOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getIdLote();
                detalleventaListDetalleventa.setIdLote(lote);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldIdLoteOfDetalleventaListDetalleventa != null) {
                    oldIdLoteOfDetalleventaListDetalleventa.getDetalleventaList().remove(detalleventaListDetalleventa);
                    oldIdLoteOfDetalleventaListDetalleventa = em.merge(oldIdLoteOfDetalleventaListDetalleventa);
                }
            }
            for (Detallecompra detallecompraListDetallecompra : lote.getDetallecompraList()) {
                Lote oldIdLoteOfDetallecompraListDetallecompra = detallecompraListDetallecompra.getIdLote();
                detallecompraListDetallecompra.setIdLote(lote);
                detallecompraListDetallecompra = em.merge(detallecompraListDetallecompra);
                if (oldIdLoteOfDetallecompraListDetallecompra != null) {
                    oldIdLoteOfDetallecompraListDetallecompra.getDetallecompraList().remove(detallecompraListDetallecompra);
                    oldIdLoteOfDetallecompraListDetallecompra = em.merge(oldIdLoteOfDetallecompraListDetallecompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lote lote) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lote persistentLote = em.find(Lote.class, lote.getIdLote());
            Producto idProductoOld = persistentLote.getIdProducto();
            Producto idProductoNew = lote.getIdProducto();
            List<Detalleventa> detalleventaListOld = persistentLote.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = lote.getDetalleventaList();
            List<Detallecompra> detallecompraListOld = persistentLote.getDetallecompraList();
            List<Detallecompra> detallecompraListNew = lote.getDetallecompraList();
            List<String> illegalOrphanMessages = null;
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleventa " + detalleventaListOldDetalleventa + " since its idLote field is not nullable.");
                }
            }
            for (Detallecompra detallecompraListOldDetallecompra : detallecompraListOld) {
                if (!detallecompraListNew.contains(detallecompraListOldDetallecompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecompra " + detallecompraListOldDetallecompra + " since its idLote field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                lote.setIdProducto(idProductoNew);
            }
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(detalleventaListNewDetalleventaToAttach.getClass(), detalleventaListNewDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            lote.setDetalleventaList(detalleventaListNew);
            List<Detallecompra> attachedDetallecompraListNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraListNewDetallecompraToAttach : detallecompraListNew) {
                detallecompraListNewDetallecompraToAttach = em.getReference(detallecompraListNewDetallecompraToAttach.getClass(), detallecompraListNewDetallecompraToAttach.getIdDetalleCompra());
                attachedDetallecompraListNew.add(detallecompraListNewDetallecompraToAttach);
            }
            detallecompraListNew = attachedDetallecompraListNew;
            lote.setDetallecompraList(detallecompraListNew);
            lote = em.merge(lote);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getLoteList().remove(lote);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getLoteList().add(lote);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Lote oldIdLoteOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa.getIdLote();
                    detalleventaListNewDetalleventa.setIdLote(lote);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldIdLoteOfDetalleventaListNewDetalleventa != null && !oldIdLoteOfDetalleventaListNewDetalleventa.equals(lote)) {
                        oldIdLoteOfDetalleventaListNewDetalleventa.getDetalleventaList().remove(detalleventaListNewDetalleventa);
                        oldIdLoteOfDetalleventaListNewDetalleventa = em.merge(oldIdLoteOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            for (Detallecompra detallecompraListNewDetallecompra : detallecompraListNew) {
                if (!detallecompraListOld.contains(detallecompraListNewDetallecompra)) {
                    Lote oldIdLoteOfDetallecompraListNewDetallecompra = detallecompraListNewDetallecompra.getIdLote();
                    detallecompraListNewDetallecompra.setIdLote(lote);
                    detallecompraListNewDetallecompra = em.merge(detallecompraListNewDetallecompra);
                    if (oldIdLoteOfDetallecompraListNewDetallecompra != null && !oldIdLoteOfDetallecompraListNewDetallecompra.equals(lote)) {
                        oldIdLoteOfDetallecompraListNewDetallecompra.getDetallecompraList().remove(detallecompraListNewDetallecompra);
                        oldIdLoteOfDetallecompraListNewDetallecompra = em.merge(oldIdLoteOfDetallecompraListNewDetallecompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lote.getIdLote();
                if (findLote(id) == null) {
                    throw new NonexistentEntityException("The lote with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lote lote;
            try {
                lote = em.getReference(Lote.class, id);
                lote.getIdLote();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lote with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleventa> detalleventaListOrphanCheck = lote.getDetalleventaList();
            for (Detalleventa detalleventaListOrphanCheckDetalleventa : detalleventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lote (" + lote + ") cannot be destroyed since the Detalleventa " + detalleventaListOrphanCheckDetalleventa + " in its detalleventaList field has a non-nullable idLote field.");
            }
            List<Detallecompra> detallecompraListOrphanCheck = lote.getDetallecompraList();
            for (Detallecompra detallecompraListOrphanCheckDetallecompra : detallecompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lote (" + lote + ") cannot be destroyed since the Detallecompra " + detallecompraListOrphanCheckDetallecompra + " in its detallecompraList field has a non-nullable idLote field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Producto idProducto = lote.getIdProducto();
            if (idProducto != null) {
                idProducto.getLoteList().remove(lote);
                idProducto = em.merge(idProducto);
            }
            em.remove(lote);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lote> findLoteEntities() {
        return findLoteEntities(true, -1, -1);
    }

    public List<Lote> findLoteEntities(int maxResults, int firstResult) {
        return findLoteEntities(false, maxResults, firstResult);
    }

    private List<Lote> findLoteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lote.class));
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

    public Lote findLote(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lote.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lote> rt = cq.from(Lote.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
