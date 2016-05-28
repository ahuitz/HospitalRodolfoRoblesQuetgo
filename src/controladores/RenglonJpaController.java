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
import entidades.Almacen;
import entidades.Producto;
import entidades.Renglon;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class RenglonJpaController implements Serializable {

    public RenglonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Renglon renglon) {
        if (renglon.getProductoList() == null) {
            renglon.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen idAlmacen = renglon.getIdAlmacen();
            if (idAlmacen != null) {
                idAlmacen = em.getReference(idAlmacen.getClass(), idAlmacen.getIdAlmacen());
                renglon.setIdAlmacen(idAlmacen);
            }
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : renglon.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdProducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            renglon.setProductoList(attachedProductoList);
            em.persist(renglon);
            if (idAlmacen != null) {
                idAlmacen.getRenglonList().add(renglon);
                idAlmacen = em.merge(idAlmacen);
            }
            for (Producto productoListProducto : renglon.getProductoList()) {
                Renglon oldIdRenglonOfProductoListProducto = productoListProducto.getIdRenglon();
                productoListProducto.setIdRenglon(renglon);
                productoListProducto = em.merge(productoListProducto);
                if (oldIdRenglonOfProductoListProducto != null) {
                    oldIdRenglonOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldIdRenglonOfProductoListProducto = em.merge(oldIdRenglonOfProductoListProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Renglon renglon) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Renglon persistentRenglon = em.find(Renglon.class, renglon.getIdRenglon());
            Almacen idAlmacenOld = persistentRenglon.getIdAlmacen();
            Almacen idAlmacenNew = renglon.getIdAlmacen();
            List<Producto> productoListOld = persistentRenglon.getProductoList();
            List<Producto> productoListNew = renglon.getProductoList();
            List<String> illegalOrphanMessages = null;
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its idRenglon field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAlmacenNew != null) {
                idAlmacenNew = em.getReference(idAlmacenNew.getClass(), idAlmacenNew.getIdAlmacen());
                renglon.setIdAlmacen(idAlmacenNew);
            }
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdProducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            renglon.setProductoList(productoListNew);
            renglon = em.merge(renglon);
            if (idAlmacenOld != null && !idAlmacenOld.equals(idAlmacenNew)) {
                idAlmacenOld.getRenglonList().remove(renglon);
                idAlmacenOld = em.merge(idAlmacenOld);
            }
            if (idAlmacenNew != null && !idAlmacenNew.equals(idAlmacenOld)) {
                idAlmacenNew.getRenglonList().add(renglon);
                idAlmacenNew = em.merge(idAlmacenNew);
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Renglon oldIdRenglonOfProductoListNewProducto = productoListNewProducto.getIdRenglon();
                    productoListNewProducto.setIdRenglon(renglon);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldIdRenglonOfProductoListNewProducto != null && !oldIdRenglonOfProductoListNewProducto.equals(renglon)) {
                        oldIdRenglonOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldIdRenglonOfProductoListNewProducto = em.merge(oldIdRenglonOfProductoListNewProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = renglon.getIdRenglon();
                if (findRenglon(id) == null) {
                    throw new NonexistentEntityException("The renglon with id " + id + " no longer exists.");
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
            Renglon renglon;
            try {
                renglon = em.getReference(Renglon.class, id);
                renglon.getIdRenglon();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The renglon with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Producto> productoListOrphanCheck = renglon.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Renglon (" + renglon + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable idRenglon field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Almacen idAlmacen = renglon.getIdAlmacen();
            if (idAlmacen != null) {
                idAlmacen.getRenglonList().remove(renglon);
                idAlmacen = em.merge(idAlmacen);
            }
            em.remove(renglon);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Renglon> findRenglonEntities() {
        return findRenglonEntities(true, -1, -1);
    }

    public List<Renglon> findRenglonEntities(int maxResults, int firstResult) {
        return findRenglonEntities(false, maxResults, firstResult);
    }

    private List<Renglon> findRenglonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Renglon.class));
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

    public Renglon findRenglon(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Renglon.class, id);
        } finally {
            em.close();
        }
    }

    public int getRenglonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Renglon> rt = cq.from(Renglon.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
