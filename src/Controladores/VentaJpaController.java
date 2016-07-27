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
import entidades.Persona;
import entidades.Usuario;
import entidades.Detalleventa;
import entidades.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getDetalleventaList() == null) {
            venta.setDetalleventaList(new ArrayList<Detalleventa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idJefeServicio = venta.getIdJefeServicio();
            if (idJefeServicio != null) {
                idJefeServicio = em.getReference(idJefeServicio.getClass(), idJefeServicio.getIdPersona());
                venta.setIdJefeServicio(idJefeServicio);
            }
            Persona idEntregadoPor = venta.getIdEntregadoPor();
            if (idEntregadoPor != null) {
                idEntregadoPor = em.getReference(idEntregadoPor.getClass(), idEntregadoPor.getIdPersona());
                venta.setIdEntregadoPor(idEntregadoPor);
            }
            Persona idRecibidoPor = venta.getIdRecibidoPor();
            if (idRecibidoPor != null) {
                idRecibidoPor = em.getReference(idRecibidoPor.getClass(), idRecibidoPor.getIdPersona());
                venta.setIdRecibidoPor(idRecibidoPor);
            }
            Usuario idUsuario = venta.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                venta.setIdUsuario(idUsuario);
            }
            List<Detalleventa> attachedDetalleventaList = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListDetalleventaToAttach : venta.getDetalleventaList()) {
                detalleventaListDetalleventaToAttach = em.getReference(detalleventaListDetalleventaToAttach.getClass(), detalleventaListDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaList.add(detalleventaListDetalleventaToAttach);
            }
            venta.setDetalleventaList(attachedDetalleventaList);
            em.persist(venta);
            if (idJefeServicio != null) {
                idJefeServicio.getVentaList().add(venta);
                idJefeServicio = em.merge(idJefeServicio);
            }
            if (idEntregadoPor != null) {
                idEntregadoPor.getVentaList().add(venta);
                idEntregadoPor = em.merge(idEntregadoPor);
            }
            if (idRecibidoPor != null) {
                idRecibidoPor.getVentaList().add(venta);
                idRecibidoPor = em.merge(idRecibidoPor);
            }
            if (idUsuario != null) {
                idUsuario.getVentaList().add(venta);
                idUsuario = em.merge(idUsuario);
            }
            for (Detalleventa detalleventaListDetalleventa : venta.getDetalleventaList()) {
                Venta oldIdVentaOfDetalleventaListDetalleventa = detalleventaListDetalleventa.getIdVenta();
                detalleventaListDetalleventa.setIdVenta(venta);
                detalleventaListDetalleventa = em.merge(detalleventaListDetalleventa);
                if (oldIdVentaOfDetalleventaListDetalleventa != null) {
                    oldIdVentaOfDetalleventaListDetalleventa.getDetalleventaList().remove(detalleventaListDetalleventa);
                    oldIdVentaOfDetalleventaListDetalleventa = em.merge(oldIdVentaOfDetalleventaListDetalleventa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getIdVenta());
            Persona idJefeServicioOld = persistentVenta.getIdJefeServicio();
            Persona idJefeServicioNew = venta.getIdJefeServicio();
            Persona idEntregadoPorOld = persistentVenta.getIdEntregadoPor();
            Persona idEntregadoPorNew = venta.getIdEntregadoPor();
            Persona idRecibidoPorOld = persistentVenta.getIdRecibidoPor();
            Persona idRecibidoPorNew = venta.getIdRecibidoPor();
            Usuario idUsuarioOld = persistentVenta.getIdUsuario();
            Usuario idUsuarioNew = venta.getIdUsuario();
            List<Detalleventa> detalleventaListOld = persistentVenta.getDetalleventaList();
            List<Detalleventa> detalleventaListNew = venta.getDetalleventaList();
            List<String> illegalOrphanMessages = null;
            for (Detalleventa detalleventaListOldDetalleventa : detalleventaListOld) {
                if (!detalleventaListNew.contains(detalleventaListOldDetalleventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleventa " + detalleventaListOldDetalleventa + " since its idVenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idJefeServicioNew != null) {
                idJefeServicioNew = em.getReference(idJefeServicioNew.getClass(), idJefeServicioNew.getIdPersona());
                venta.setIdJefeServicio(idJefeServicioNew);
            }
            if (idEntregadoPorNew != null) {
                idEntregadoPorNew = em.getReference(idEntregadoPorNew.getClass(), idEntregadoPorNew.getIdPersona());
                venta.setIdEntregadoPor(idEntregadoPorNew);
            }
            if (idRecibidoPorNew != null) {
                idRecibidoPorNew = em.getReference(idRecibidoPorNew.getClass(), idRecibidoPorNew.getIdPersona());
                venta.setIdRecibidoPor(idRecibidoPorNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                venta.setIdUsuario(idUsuarioNew);
            }
            List<Detalleventa> attachedDetalleventaListNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaListNewDetalleventaToAttach : detalleventaListNew) {
                detalleventaListNewDetalleventaToAttach = em.getReference(detalleventaListNewDetalleventaToAttach.getClass(), detalleventaListNewDetalleventaToAttach.getIdDetalleVenta());
                attachedDetalleventaListNew.add(detalleventaListNewDetalleventaToAttach);
            }
            detalleventaListNew = attachedDetalleventaListNew;
            venta.setDetalleventaList(detalleventaListNew);
            venta = em.merge(venta);
            if (idJefeServicioOld != null && !idJefeServicioOld.equals(idJefeServicioNew)) {
                idJefeServicioOld.getVentaList().remove(venta);
                idJefeServicioOld = em.merge(idJefeServicioOld);
            }
            if (idJefeServicioNew != null && !idJefeServicioNew.equals(idJefeServicioOld)) {
                idJefeServicioNew.getVentaList().add(venta);
                idJefeServicioNew = em.merge(idJefeServicioNew);
            }
            if (idEntregadoPorOld != null && !idEntregadoPorOld.equals(idEntregadoPorNew)) {
                idEntregadoPorOld.getVentaList().remove(venta);
                idEntregadoPorOld = em.merge(idEntregadoPorOld);
            }
            if (idEntregadoPorNew != null && !idEntregadoPorNew.equals(idEntregadoPorOld)) {
                idEntregadoPorNew.getVentaList().add(venta);
                idEntregadoPorNew = em.merge(idEntregadoPorNew);
            }
            if (idRecibidoPorOld != null && !idRecibidoPorOld.equals(idRecibidoPorNew)) {
                idRecibidoPorOld.getVentaList().remove(venta);
                idRecibidoPorOld = em.merge(idRecibidoPorOld);
            }
            if (idRecibidoPorNew != null && !idRecibidoPorNew.equals(idRecibidoPorOld)) {
                idRecibidoPorNew.getVentaList().add(venta);
                idRecibidoPorNew = em.merge(idRecibidoPorNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getVentaList().remove(venta);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getVentaList().add(venta);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Detalleventa detalleventaListNewDetalleventa : detalleventaListNew) {
                if (!detalleventaListOld.contains(detalleventaListNewDetalleventa)) {
                    Venta oldIdVentaOfDetalleventaListNewDetalleventa = detalleventaListNewDetalleventa.getIdVenta();
                    detalleventaListNewDetalleventa.setIdVenta(venta);
                    detalleventaListNewDetalleventa = em.merge(detalleventaListNewDetalleventa);
                    if (oldIdVentaOfDetalleventaListNewDetalleventa != null && !oldIdVentaOfDetalleventaListNewDetalleventa.equals(venta)) {
                        oldIdVentaOfDetalleventaListNewDetalleventa.getDetalleventaList().remove(detalleventaListNewDetalleventa);
                        oldIdVentaOfDetalleventaListNewDetalleventa = em.merge(oldIdVentaOfDetalleventaListNewDetalleventa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = venta.getIdVenta();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getIdVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleventa> detalleventaListOrphanCheck = venta.getDetalleventaList();
            for (Detalleventa detalleventaListOrphanCheckDetalleventa : detalleventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Detalleventa " + detalleventaListOrphanCheckDetalleventa + " in its detalleventaList field has a non-nullable idVenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idJefeServicio = venta.getIdJefeServicio();
            if (idJefeServicio != null) {
                idJefeServicio.getVentaList().remove(venta);
                idJefeServicio = em.merge(idJefeServicio);
            }
            Persona idEntregadoPor = venta.getIdEntregadoPor();
            if (idEntregadoPor != null) {
                idEntregadoPor.getVentaList().remove(venta);
                idEntregadoPor = em.merge(idEntregadoPor);
            }
            Persona idRecibidoPor = venta.getIdRecibidoPor();
            if (idRecibidoPor != null) {
                idRecibidoPor.getVentaList().remove(venta);
                idRecibidoPor = em.merge(idRecibidoPor);
            }
            Usuario idUsuario = venta.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getVentaList().remove(venta);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
