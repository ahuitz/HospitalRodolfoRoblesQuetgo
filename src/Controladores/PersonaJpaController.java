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
import entidades.Departamento;
import entidades.Persona;
import entidades.Venta;
import java.util.ArrayList;
import java.util.List;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rosario
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getVentaList() == null) {
            persona.setVentaList(new ArrayList<Venta>());
        }
        if (persona.getVentaList1() == null) {
            persona.setVentaList1(new ArrayList<Venta>());
        }
        if (persona.getVentaList2() == null) {
            persona.setVentaList2(new ArrayList<Venta>());
        }
        if (persona.getUsuarioList() == null) {
            persona.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDepartamento = persona.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                persona.setIdDepartamento(idDepartamento);
            }
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : persona.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getIdVenta());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            persona.setVentaList(attachedVentaList);
            List<Venta> attachedVentaList1 = new ArrayList<Venta>();
            for (Venta ventaList1VentaToAttach : persona.getVentaList1()) {
                ventaList1VentaToAttach = em.getReference(ventaList1VentaToAttach.getClass(), ventaList1VentaToAttach.getIdVenta());
                attachedVentaList1.add(ventaList1VentaToAttach);
            }
            persona.setVentaList1(attachedVentaList1);
            List<Venta> attachedVentaList2 = new ArrayList<Venta>();
            for (Venta ventaList2VentaToAttach : persona.getVentaList2()) {
                ventaList2VentaToAttach = em.getReference(ventaList2VentaToAttach.getClass(), ventaList2VentaToAttach.getIdVenta());
                attachedVentaList2.add(ventaList2VentaToAttach);
            }
            persona.setVentaList2(attachedVentaList2);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : persona.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            persona.setUsuarioList(attachedUsuarioList);
            em.persist(persona);
            if (idDepartamento != null) {
                idDepartamento.getPersonaList().add(persona);
                idDepartamento = em.merge(idDepartamento);
            }
            for (Venta ventaListVenta : persona.getVentaList()) {
                Persona oldIdJefeServicioOfVentaListVenta = ventaListVenta.getIdJefeServicio();
                ventaListVenta.setIdJefeServicio(persona);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldIdJefeServicioOfVentaListVenta != null) {
                    oldIdJefeServicioOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldIdJefeServicioOfVentaListVenta = em.merge(oldIdJefeServicioOfVentaListVenta);
                }
            }
            for (Venta ventaList1Venta : persona.getVentaList1()) {
                Persona oldIdEntregadoPorOfVentaList1Venta = ventaList1Venta.getIdEntregadoPor();
                ventaList1Venta.setIdEntregadoPor(persona);
                ventaList1Venta = em.merge(ventaList1Venta);
                if (oldIdEntregadoPorOfVentaList1Venta != null) {
                    oldIdEntregadoPorOfVentaList1Venta.getVentaList1().remove(ventaList1Venta);
                    oldIdEntregadoPorOfVentaList1Venta = em.merge(oldIdEntregadoPorOfVentaList1Venta);
                }
            }
            for (Venta ventaList2Venta : persona.getVentaList2()) {
                Persona oldIdRecibidoPorOfVentaList2Venta = ventaList2Venta.getIdRecibidoPor();
                ventaList2Venta.setIdRecibidoPor(persona);
                ventaList2Venta = em.merge(ventaList2Venta);
                if (oldIdRecibidoPorOfVentaList2Venta != null) {
                    oldIdRecibidoPorOfVentaList2Venta.getVentaList2().remove(ventaList2Venta);
                    oldIdRecibidoPorOfVentaList2Venta = em.merge(oldIdRecibidoPorOfVentaList2Venta);
                }
            }
            for (Usuario usuarioListUsuario : persona.getUsuarioList()) {
                Persona oldIdPersonaOfUsuarioListUsuario = usuarioListUsuario.getIdPersona();
                usuarioListUsuario.setIdPersona(persona);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdPersonaOfUsuarioListUsuario != null) {
                    oldIdPersonaOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdPersonaOfUsuarioListUsuario = em.merge(oldIdPersonaOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            Departamento idDepartamentoOld = persistentPersona.getIdDepartamento();
            Departamento idDepartamentoNew = persona.getIdDepartamento();
            List<Venta> ventaListOld = persistentPersona.getVentaList();
            List<Venta> ventaListNew = persona.getVentaList();
            List<Venta> ventaList1Old = persistentPersona.getVentaList1();
            List<Venta> ventaList1New = persona.getVentaList1();
            List<Venta> ventaList2Old = persistentPersona.getVentaList2();
            List<Venta> ventaList2New = persona.getVentaList2();
            List<Usuario> usuarioListOld = persistentPersona.getUsuarioList();
            List<Usuario> usuarioListNew = persona.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its idJefeServicio field is not nullable.");
                }
            }
            for (Venta ventaList2OldVenta : ventaList2Old) {
                if (!ventaList2New.contains(ventaList2OldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaList2OldVenta + " since its idRecibidoPor field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idPersona field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                persona.setIdDepartamento(idDepartamentoNew);
            }
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getIdVenta());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            persona.setVentaList(ventaListNew);
            List<Venta> attachedVentaList1New = new ArrayList<Venta>();
            for (Venta ventaList1NewVentaToAttach : ventaList1New) {
                ventaList1NewVentaToAttach = em.getReference(ventaList1NewVentaToAttach.getClass(), ventaList1NewVentaToAttach.getIdVenta());
                attachedVentaList1New.add(ventaList1NewVentaToAttach);
            }
            ventaList1New = attachedVentaList1New;
            persona.setVentaList1(ventaList1New);
            List<Venta> attachedVentaList2New = new ArrayList<Venta>();
            for (Venta ventaList2NewVentaToAttach : ventaList2New) {
                ventaList2NewVentaToAttach = em.getReference(ventaList2NewVentaToAttach.getClass(), ventaList2NewVentaToAttach.getIdVenta());
                attachedVentaList2New.add(ventaList2NewVentaToAttach);
            }
            ventaList2New = attachedVentaList2New;
            persona.setVentaList2(ventaList2New);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            persona.setUsuarioList(usuarioListNew);
            persona = em.merge(persona);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getPersonaList().remove(persona);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getPersonaList().add(persona);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Persona oldIdJefeServicioOfVentaListNewVenta = ventaListNewVenta.getIdJefeServicio();
                    ventaListNewVenta.setIdJefeServicio(persona);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldIdJefeServicioOfVentaListNewVenta != null && !oldIdJefeServicioOfVentaListNewVenta.equals(persona)) {
                        oldIdJefeServicioOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldIdJefeServicioOfVentaListNewVenta = em.merge(oldIdJefeServicioOfVentaListNewVenta);
                    }
                }
            }
            for (Venta ventaList1OldVenta : ventaList1Old) {
                if (!ventaList1New.contains(ventaList1OldVenta)) {
                    ventaList1OldVenta.setIdEntregadoPor(null);
                    ventaList1OldVenta = em.merge(ventaList1OldVenta);
                }
            }
            for (Venta ventaList1NewVenta : ventaList1New) {
                if (!ventaList1Old.contains(ventaList1NewVenta)) {
                    Persona oldIdEntregadoPorOfVentaList1NewVenta = ventaList1NewVenta.getIdEntregadoPor();
                    ventaList1NewVenta.setIdEntregadoPor(persona);
                    ventaList1NewVenta = em.merge(ventaList1NewVenta);
                    if (oldIdEntregadoPorOfVentaList1NewVenta != null && !oldIdEntregadoPorOfVentaList1NewVenta.equals(persona)) {
                        oldIdEntregadoPorOfVentaList1NewVenta.getVentaList1().remove(ventaList1NewVenta);
                        oldIdEntregadoPorOfVentaList1NewVenta = em.merge(oldIdEntregadoPorOfVentaList1NewVenta);
                    }
                }
            }
            for (Venta ventaList2NewVenta : ventaList2New) {
                if (!ventaList2Old.contains(ventaList2NewVenta)) {
                    Persona oldIdRecibidoPorOfVentaList2NewVenta = ventaList2NewVenta.getIdRecibidoPor();
                    ventaList2NewVenta.setIdRecibidoPor(persona);
                    ventaList2NewVenta = em.merge(ventaList2NewVenta);
                    if (oldIdRecibidoPorOfVentaList2NewVenta != null && !oldIdRecibidoPorOfVentaList2NewVenta.equals(persona)) {
                        oldIdRecibidoPorOfVentaList2NewVenta.getVentaList2().remove(ventaList2NewVenta);
                        oldIdRecibidoPorOfVentaList2NewVenta = em.merge(oldIdRecibidoPorOfVentaList2NewVenta);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Persona oldIdPersonaOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdPersona();
                    usuarioListNewUsuario.setIdPersona(persona);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdPersonaOfUsuarioListNewUsuario != null && !oldIdPersonaOfUsuarioListNewUsuario.equals(persona)) {
                        oldIdPersonaOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdPersonaOfUsuarioListNewUsuario = em.merge(oldIdPersonaOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Venta> ventaListOrphanCheck = persona.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable idJefeServicio field.");
            }
            List<Venta> ventaList2OrphanCheck = persona.getVentaList2();
            for (Venta ventaList2OrphanCheckVenta : ventaList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Venta " + ventaList2OrphanCheckVenta + " in its ventaList2 field has a non-nullable idRecibidoPor field.");
            }
            List<Usuario> usuarioListOrphanCheck = persona.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idPersona field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento idDepartamento = persona.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getPersonaList().remove(persona);
                idDepartamento = em.merge(idDepartamento);
            }
            List<Venta> ventaList1 = persona.getVentaList1();
            for (Venta ventaList1Venta : ventaList1) {
                ventaList1Venta.setIdEntregadoPor(null);
                ventaList1Venta = em.merge(ventaList1Venta);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
