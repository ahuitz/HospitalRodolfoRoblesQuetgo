/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Permiso;
import entidades.Permisousuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class PermisousuarioJpaController implements Serializable {

    public PermisousuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permisousuario permisousuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = permisousuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                permisousuario.setIdUsuario(idUsuario);
            }
            Permiso idPermiso = permisousuario.getIdPermiso();
            if (idPermiso != null) {
                idPermiso = em.getReference(idPermiso.getClass(), idPermiso.getIdPermiso());
                permisousuario.setIdPermiso(idPermiso);
            }
            em.persist(permisousuario);
            if (idUsuario != null) {
                idUsuario.getPermisousuarioList().add(permisousuario);
                idUsuario = em.merge(idUsuario);
            }
            if (idPermiso != null) {
                idPermiso.getPermisousuarioList().add(permisousuario);
                idPermiso = em.merge(idPermiso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permisousuario permisousuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permisousuario persistentPermisousuario = em.find(Permisousuario.class, permisousuario.getIdPermisoUsuario());
            Usuario idUsuarioOld = persistentPermisousuario.getIdUsuario();
            Usuario idUsuarioNew = permisousuario.getIdUsuario();
            Permiso idPermisoOld = persistentPermisousuario.getIdPermiso();
            Permiso idPermisoNew = permisousuario.getIdPermiso();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                permisousuario.setIdUsuario(idUsuarioNew);
            }
            if (idPermisoNew != null) {
                idPermisoNew = em.getReference(idPermisoNew.getClass(), idPermisoNew.getIdPermiso());
                permisousuario.setIdPermiso(idPermisoNew);
            }
            permisousuario = em.merge(permisousuario);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPermisousuarioList().remove(permisousuario);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPermisousuarioList().add(permisousuario);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idPermisoOld != null && !idPermisoOld.equals(idPermisoNew)) {
                idPermisoOld.getPermisousuarioList().remove(permisousuario);
                idPermisoOld = em.merge(idPermisoOld);
            }
            if (idPermisoNew != null && !idPermisoNew.equals(idPermisoOld)) {
                idPermisoNew.getPermisousuarioList().add(permisousuario);
                idPermisoNew = em.merge(idPermisoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permisousuario.getIdPermisoUsuario();
                if (findPermisousuario(id) == null) {
                    throw new NonexistentEntityException("The permisousuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permisousuario permisousuario;
            try {
                permisousuario = em.getReference(Permisousuario.class, id);
                permisousuario.getIdPermisoUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisousuario with id " + id + " no longer exists.", enfe);
            }
            Usuario idUsuario = permisousuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPermisousuarioList().remove(permisousuario);
                idUsuario = em.merge(idUsuario);
            }
            Permiso idPermiso = permisousuario.getIdPermiso();
            if (idPermiso != null) {
                idPermiso.getPermisousuarioList().remove(permisousuario);
                idPermiso = em.merge(idPermiso);
            }
            em.remove(permisousuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permisousuario> findPermisousuarioEntities() {
        return findPermisousuarioEntities(true, -1, -1);
    }

    public List<Permisousuario> findPermisousuarioEntities(int maxResults, int firstResult) {
        return findPermisousuarioEntities(false, maxResults, firstResult);
    }

    private List<Permisousuario> findPermisousuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permisousuario.class));
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

    public Permisousuario findPermisousuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permisousuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisousuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permisousuario> rt = cq.from(Permisousuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
