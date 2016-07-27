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
import entidades.Presentacion;
import entidades.Renglon;
import entidades.Lote;
import java.util.ArrayList;
import java.util.List;
import entidades.Kardex;
import entidades.Producto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
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
        if (producto.getLoteList() == null) {
            producto.setLoteList(new ArrayList<Lote>());
        }
        if (producto.getKardexList() == null) {
            producto.setKardexList(new ArrayList<Kardex>());
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
            List<Lote> attachedLoteList = new ArrayList<Lote>();
            for (Lote loteListLoteToAttach : producto.getLoteList()) {
                loteListLoteToAttach = em.getReference(loteListLoteToAttach.getClass(), loteListLoteToAttach.getIdLote());
                attachedLoteList.add(loteListLoteToAttach);
            }
            producto.setLoteList(attachedLoteList);
            List<Kardex> attachedKardexList = new ArrayList<Kardex>();
            for (Kardex kardexListKardexToAttach : producto.getKardexList()) {
                kardexListKardexToAttach = em.getReference(kardexListKardexToAttach.getClass(), kardexListKardexToAttach.getIdKardex());
                attachedKardexList.add(kardexListKardexToAttach);
            }
            producto.setKardexList(attachedKardexList);
            em.persist(producto);
            if (idPresentacion != null) {
                idPresentacion.getProductoList().add(producto);
                idPresentacion = em.merge(idPresentacion);
            }
            if (idRenglon != null) {
                idRenglon.getProductoList().add(producto);
                idRenglon = em.merge(idRenglon);
            }
            for (Lote loteListLote : producto.getLoteList()) {
                Producto oldIdProductoOfLoteListLote = loteListLote.getIdProducto();
                loteListLote.setIdProducto(producto);
                loteListLote = em.merge(loteListLote);
                if (oldIdProductoOfLoteListLote != null) {
                    oldIdProductoOfLoteListLote.getLoteList().remove(loteListLote);
                    oldIdProductoOfLoteListLote = em.merge(oldIdProductoOfLoteListLote);
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
            List<Lote> loteListOld = persistentProducto.getLoteList();
            List<Lote> loteListNew = producto.getLoteList();
            List<Kardex> kardexListOld = persistentProducto.getKardexList();
            List<Kardex> kardexListNew = producto.getKardexList();
            List<String> illegalOrphanMessages = null;
            for (Lote loteListOldLote : loteListOld) {
                if (!loteListNew.contains(loteListOldLote)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Lote " + loteListOldLote + " since its idProducto field is not nullable.");
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
            List<Lote> attachedLoteListNew = new ArrayList<Lote>();
            for (Lote loteListNewLoteToAttach : loteListNew) {
                loteListNewLoteToAttach = em.getReference(loteListNewLoteToAttach.getClass(), loteListNewLoteToAttach.getIdLote());
                attachedLoteListNew.add(loteListNewLoteToAttach);
            }
            loteListNew = attachedLoteListNew;
            producto.setLoteList(loteListNew);
            List<Kardex> attachedKardexListNew = new ArrayList<Kardex>();
            for (Kardex kardexListNewKardexToAttach : kardexListNew) {
                kardexListNewKardexToAttach = em.getReference(kardexListNewKardexToAttach.getClass(), kardexListNewKardexToAttach.getIdKardex());
                attachedKardexListNew.add(kardexListNewKardexToAttach);
            }
            kardexListNew = attachedKardexListNew;
            producto.setKardexList(kardexListNew);
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
            for (Lote loteListNewLote : loteListNew) {
                if (!loteListOld.contains(loteListNewLote)) {
                    Producto oldIdProductoOfLoteListNewLote = loteListNewLote.getIdProducto();
                    loteListNewLote.setIdProducto(producto);
                    loteListNewLote = em.merge(loteListNewLote);
                    if (oldIdProductoOfLoteListNewLote != null && !oldIdProductoOfLoteListNewLote.equals(producto)) {
                        oldIdProductoOfLoteListNewLote.getLoteList().remove(loteListNewLote);
                        oldIdProductoOfLoteListNewLote = em.merge(oldIdProductoOfLoteListNewLote);
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
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = producto.getIdProducto();
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

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<Lote> loteListOrphanCheck = producto.getLoteList();
            for (Lote loteListOrphanCheckLote : loteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Lote " + loteListOrphanCheckLote + " in its loteList field has a non-nullable idProducto field.");
            }
            List<Kardex> kardexListOrphanCheck = producto.getKardexList();
            for (Kardex kardexListOrphanCheckKardex : kardexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Kardex " + kardexListOrphanCheckKardex + " in its kardexList field has a non-nullable idProducto field.");
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

    public Producto findProducto(Long id) {
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
