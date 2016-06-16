/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Almacen;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Renglon;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class AlmacenJpaController implements Serializable {

    public AlmacenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Almacen almacen) {
        if (almacen.getRenglonList() == null) {
            almacen.setRenglonList(new ArrayList<Renglon>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Renglon> attachedRenglonList = new ArrayList<Renglon>();
            for (Renglon renglonListRenglonToAttach : almacen.getRenglonList()) {
                renglonListRenglonToAttach = em.getReference(renglonListRenglonToAttach.getClass(), renglonListRenglonToAttach.getIdRenglon());
                attachedRenglonList.add(renglonListRenglonToAttach);
            }
            almacen.setRenglonList(attachedRenglonList);
            em.persist(almacen);
            for (Renglon renglonListRenglon : almacen.getRenglonList()) {
                Almacen oldIdAlmacenOfRenglonListRenglon = renglonListRenglon.getIdAlmacen();
                renglonListRenglon.setIdAlmacen(almacen);
                renglonListRenglon = em.merge(renglonListRenglon);
                if (oldIdAlmacenOfRenglonListRenglon != null) {
                    oldIdAlmacenOfRenglonListRenglon.getRenglonList().remove(renglonListRenglon);
                    oldIdAlmacenOfRenglonListRenglon = em.merge(oldIdAlmacenOfRenglonListRenglon);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Almacen almacen) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen persistentAlmacen = em.find(Almacen.class, almacen.getIdAlmacen());
            List<Renglon> renglonListOld = persistentAlmacen.getRenglonList();
            List<Renglon> renglonListNew = almacen.getRenglonList();
            List<String> illegalOrphanMessages = null;
            for (Renglon renglonListOldRenglon : renglonListOld) {
                if (!renglonListNew.contains(renglonListOldRenglon)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Renglon " + renglonListOldRenglon + " since its idAlmacen field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Renglon> attachedRenglonListNew = new ArrayList<Renglon>();
            for (Renglon renglonListNewRenglonToAttach : renglonListNew) {
                renglonListNewRenglonToAttach = em.getReference(renglonListNewRenglonToAttach.getClass(), renglonListNewRenglonToAttach.getIdRenglon());
                attachedRenglonListNew.add(renglonListNewRenglonToAttach);
            }
            renglonListNew = attachedRenglonListNew;
            almacen.setRenglonList(renglonListNew);
            almacen = em.merge(almacen);
            for (Renglon renglonListNewRenglon : renglonListNew) {
                if (!renglonListOld.contains(renglonListNewRenglon)) {
                    Almacen oldIdAlmacenOfRenglonListNewRenglon = renglonListNewRenglon.getIdAlmacen();
                    renglonListNewRenglon.setIdAlmacen(almacen);
                    renglonListNewRenglon = em.merge(renglonListNewRenglon);
                    if (oldIdAlmacenOfRenglonListNewRenglon != null && !oldIdAlmacenOfRenglonListNewRenglon.equals(almacen)) {
                        oldIdAlmacenOfRenglonListNewRenglon.getRenglonList().remove(renglonListNewRenglon);
                        oldIdAlmacenOfRenglonListNewRenglon = em.merge(oldIdAlmacenOfRenglonListNewRenglon);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = almacen.getIdAlmacen();
                if (findAlmacen(id) == null) {
                    throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.");
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
            Almacen almacen;
            try {
                almacen = em.getReference(Almacen.class, id);
                almacen.getIdAlmacen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Renglon> renglonListOrphanCheck = almacen.getRenglonList();
            for (Renglon renglonListOrphanCheckRenglon : renglonListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Almacen (" + almacen + ") cannot be destroyed since the Renglon " + renglonListOrphanCheckRenglon + " in its renglonList field has a non-nullable idAlmacen field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(almacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Almacen> findAlmacenEntities() {
        return findAlmacenEntities(true, -1, -1);
    }

    public List<Almacen> findAlmacenEntities(int maxResults, int firstResult) {
        return findAlmacenEntities(false, maxResults, firstResult);
    }

    private List<Almacen> findAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacen.class));
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

    public Almacen findAlmacen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlmacenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Almacen> rt = cq.from(Almacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Almacen findAlmacen(String almacen) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, almacen);
        } finally {
            em.close();
        }
    }
}
