/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import entidades.Permiso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Permisousuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) {
        if (permiso.getPermisousuarioList() == null) {
            permiso.setPermisousuarioList(new ArrayList<Permisousuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permisousuario> attachedPermisousuarioList = new ArrayList<Permisousuario>();
            for (Permisousuario permisousuarioListPermisousuarioToAttach : permiso.getPermisousuarioList()) {
                permisousuarioListPermisousuarioToAttach = em.getReference(permisousuarioListPermisousuarioToAttach.getClass(), permisousuarioListPermisousuarioToAttach.getIdPermisoUsuario());
                attachedPermisousuarioList.add(permisousuarioListPermisousuarioToAttach);
            }
            permiso.setPermisousuarioList(attachedPermisousuarioList);
            em.persist(permiso);
            for (Permisousuario permisousuarioListPermisousuario : permiso.getPermisousuarioList()) {
                Permiso oldIdPermisoOfPermisousuarioListPermisousuario = permisousuarioListPermisousuario.getIdPermiso();
                permisousuarioListPermisousuario.setIdPermiso(permiso);
                permisousuarioListPermisousuario = em.merge(permisousuarioListPermisousuario);
                if (oldIdPermisoOfPermisousuarioListPermisousuario != null) {
                    oldIdPermisoOfPermisousuarioListPermisousuario.getPermisousuarioList().remove(permisousuarioListPermisousuario);
                    oldIdPermisoOfPermisousuarioListPermisousuario = em.merge(oldIdPermisoOfPermisousuarioListPermisousuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getIdPermiso());
            List<Permisousuario> permisousuarioListOld = persistentPermiso.getPermisousuarioList();
            List<Permisousuario> permisousuarioListNew = permiso.getPermisousuarioList();
            List<String> illegalOrphanMessages = null;
            for (Permisousuario permisousuarioListOldPermisousuario : permisousuarioListOld) {
                if (!permisousuarioListNew.contains(permisousuarioListOldPermisousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permisousuario " + permisousuarioListOldPermisousuario + " since its idPermiso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permisousuario> attachedPermisousuarioListNew = new ArrayList<Permisousuario>();
            for (Permisousuario permisousuarioListNewPermisousuarioToAttach : permisousuarioListNew) {
                permisousuarioListNewPermisousuarioToAttach = em.getReference(permisousuarioListNewPermisousuarioToAttach.getClass(), permisousuarioListNewPermisousuarioToAttach.getIdPermisoUsuario());
                attachedPermisousuarioListNew.add(permisousuarioListNewPermisousuarioToAttach);
            }
            permisousuarioListNew = attachedPermisousuarioListNew;
            permiso.setPermisousuarioList(permisousuarioListNew);
            permiso = em.merge(permiso);
            for (Permisousuario permisousuarioListNewPermisousuario : permisousuarioListNew) {
                if (!permisousuarioListOld.contains(permisousuarioListNewPermisousuario)) {
                    Permiso oldIdPermisoOfPermisousuarioListNewPermisousuario = permisousuarioListNewPermisousuario.getIdPermiso();
                    permisousuarioListNewPermisousuario.setIdPermiso(permiso);
                    permisousuarioListNewPermisousuario = em.merge(permisousuarioListNewPermisousuario);
                    if (oldIdPermisoOfPermisousuarioListNewPermisousuario != null && !oldIdPermisoOfPermisousuarioListNewPermisousuario.equals(permiso)) {
                        oldIdPermisoOfPermisousuarioListNewPermisousuario.getPermisousuarioList().remove(permisousuarioListNewPermisousuario);
                        oldIdPermisoOfPermisousuarioListNewPermisousuario = em.merge(oldIdPermisoOfPermisousuarioListNewPermisousuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permiso.getIdPermiso();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
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
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getIdPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Permisousuario> permisousuarioListOrphanCheck = permiso.getPermisousuarioList();
            for (Permisousuario permisousuarioListOrphanCheckPermisousuario : permisousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permiso (" + permiso + ") cannot be destroyed since the Permisousuario " + permisousuarioListOrphanCheckPermisousuario + " in its permisousuarioList field has a non-nullable idPermiso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
