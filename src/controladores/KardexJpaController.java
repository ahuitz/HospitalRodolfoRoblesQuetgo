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
import entidades.Producto;
import entidades.Departamento;
import entidades.Kardex;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class KardexJpaController implements Serializable {

    public KardexJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kardex kardex) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = kardex.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                kardex.setIdProducto(idProducto);
            }
            Departamento idDepartamento = kardex.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                kardex.setIdDepartamento(idDepartamento);
            }
            em.persist(kardex);
            if (idProducto != null) {
                idProducto.getKardexList().add(kardex);
                idProducto = em.merge(idProducto);
            }
            if (idDepartamento != null) {
                idDepartamento.getKardexList().add(kardex);
                idDepartamento = em.merge(idDepartamento);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kardex kardex) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kardex persistentKardex = em.find(Kardex.class, kardex.getIdKardex());
            Producto idProductoOld = persistentKardex.getIdProducto();
            Producto idProductoNew = kardex.getIdProducto();
            Departamento idDepartamentoOld = persistentKardex.getIdDepartamento();
            Departamento idDepartamentoNew = kardex.getIdDepartamento();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                kardex.setIdProducto(idProductoNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                kardex.setIdDepartamento(idDepartamentoNew);
            }
            kardex = em.merge(kardex);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getKardexList().remove(kardex);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getKardexList().add(kardex);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getKardexList().remove(kardex);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getKardexList().add(kardex);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = kardex.getIdKardex();
                if (findKardex(id) == null) {
                    throw new NonexistentEntityException("The kardex with id " + id + " no longer exists.");
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
            Kardex kardex;
            try {
                kardex = em.getReference(Kardex.class, id);
                kardex.getIdKardex();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kardex with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = kardex.getIdProducto();
            if (idProducto != null) {
                idProducto.getKardexList().remove(kardex);
                idProducto = em.merge(idProducto);
            }
            Departamento idDepartamento = kardex.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getKardexList().remove(kardex);
                idDepartamento = em.merge(idDepartamento);
            }
            em.remove(kardex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kardex> findKardexEntities() {
        return findKardexEntities(true, -1, -1);
    }

    public List<Kardex> findKardexEntities(int maxResults, int firstResult) {
        return findKardexEntities(false, maxResults, firstResult);
    }

    private List<Kardex> findKardexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kardex.class));
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

    public Kardex findKardex(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kardex.class, id);
        } finally {
            em.close();
        }
    }

    public int getKardexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kardex> rt = cq.from(Kardex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
