/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Departamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Persona;
import java.util.ArrayList;
import java.util.List;
import entidades.Kardex;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getPersonaList() == null) {
            departamento.setPersonaList(new ArrayList<Persona>());
        }
        if (departamento.getKardexList() == null) {
            departamento.setKardexList(new ArrayList<Kardex>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Persona> attachedPersonaList = new ArrayList<Persona>();
            for (Persona personaListPersonaToAttach : departamento.getPersonaList()) {
                personaListPersonaToAttach = em.getReference(personaListPersonaToAttach.getClass(), personaListPersonaToAttach.getIdPersona());
                attachedPersonaList.add(personaListPersonaToAttach);
            }
            departamento.setPersonaList(attachedPersonaList);
            List<Kardex> attachedKardexList = new ArrayList<Kardex>();
            for (Kardex kardexListKardexToAttach : departamento.getKardexList()) {
                kardexListKardexToAttach = em.getReference(kardexListKardexToAttach.getClass(), kardexListKardexToAttach.getIdKardex());
                attachedKardexList.add(kardexListKardexToAttach);
            }
            departamento.setKardexList(attachedKardexList);
            em.persist(departamento);
            for (Persona personaListPersona : departamento.getPersonaList()) {
                Departamento oldIdDepartamentoOfPersonaListPersona = personaListPersona.getIdDepartamento();
                personaListPersona.setIdDepartamento(departamento);
                personaListPersona = em.merge(personaListPersona);
                if (oldIdDepartamentoOfPersonaListPersona != null) {
                    oldIdDepartamentoOfPersonaListPersona.getPersonaList().remove(personaListPersona);
                    oldIdDepartamentoOfPersonaListPersona = em.merge(oldIdDepartamentoOfPersonaListPersona);
                }
            }
            for (Kardex kardexListKardex : departamento.getKardexList()) {
                Departamento oldIdDepartamentoOfKardexListKardex = kardexListKardex.getIdDepartamento();
                kardexListKardex.setIdDepartamento(departamento);
                kardexListKardex = em.merge(kardexListKardex);
                if (oldIdDepartamentoOfKardexListKardex != null) {
                    oldIdDepartamentoOfKardexListKardex.getKardexList().remove(kardexListKardex);
                    oldIdDepartamentoOfKardexListKardex = em.merge(oldIdDepartamentoOfKardexListKardex);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepartamento());
            List<Persona> personaListOld = persistentDepartamento.getPersonaList();
            List<Persona> personaListNew = departamento.getPersonaList();
            List<Kardex> kardexListOld = persistentDepartamento.getKardexList();
            List<Kardex> kardexListNew = departamento.getKardexList();
            List<String> illegalOrphanMessages = null;
            for (Persona personaListOldPersona : personaListOld) {
                if (!personaListNew.contains(personaListOldPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Persona " + personaListOldPersona + " since its idDepartamento field is not nullable.");
                }
            }
            for (Kardex kardexListOldKardex : kardexListOld) {
                if (!kardexListNew.contains(kardexListOldKardex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Kardex " + kardexListOldKardex + " since its idDepartamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Persona> attachedPersonaListNew = new ArrayList<Persona>();
            for (Persona personaListNewPersonaToAttach : personaListNew) {
                personaListNewPersonaToAttach = em.getReference(personaListNewPersonaToAttach.getClass(), personaListNewPersonaToAttach.getIdPersona());
                attachedPersonaListNew.add(personaListNewPersonaToAttach);
            }
            personaListNew = attachedPersonaListNew;
            departamento.setPersonaList(personaListNew);
            List<Kardex> attachedKardexListNew = new ArrayList<Kardex>();
            for (Kardex kardexListNewKardexToAttach : kardexListNew) {
                kardexListNewKardexToAttach = em.getReference(kardexListNewKardexToAttach.getClass(), kardexListNewKardexToAttach.getIdKardex());
                attachedKardexListNew.add(kardexListNewKardexToAttach);
            }
            kardexListNew = attachedKardexListNew;
            departamento.setKardexList(kardexListNew);
            departamento = em.merge(departamento);
            for (Persona personaListNewPersona : personaListNew) {
                if (!personaListOld.contains(personaListNewPersona)) {
                    Departamento oldIdDepartamentoOfPersonaListNewPersona = personaListNewPersona.getIdDepartamento();
                    personaListNewPersona.setIdDepartamento(departamento);
                    personaListNewPersona = em.merge(personaListNewPersona);
                    if (oldIdDepartamentoOfPersonaListNewPersona != null && !oldIdDepartamentoOfPersonaListNewPersona.equals(departamento)) {
                        oldIdDepartamentoOfPersonaListNewPersona.getPersonaList().remove(personaListNewPersona);
                        oldIdDepartamentoOfPersonaListNewPersona = em.merge(oldIdDepartamentoOfPersonaListNewPersona);
                    }
                }
            }
            for (Kardex kardexListNewKardex : kardexListNew) {
                if (!kardexListOld.contains(kardexListNewKardex)) {
                    Departamento oldIdDepartamentoOfKardexListNewKardex = kardexListNewKardex.getIdDepartamento();
                    kardexListNewKardex.setIdDepartamento(departamento);
                    kardexListNewKardex = em.merge(kardexListNewKardex);
                    if (oldIdDepartamentoOfKardexListNewKardex != null && !oldIdDepartamentoOfKardexListNewKardex.equals(departamento)) {
                        oldIdDepartamentoOfKardexListNewKardex.getKardexList().remove(kardexListNewKardex);
                        oldIdDepartamentoOfKardexListNewKardex = em.merge(oldIdDepartamentoOfKardexListNewKardex);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIdDepartamento();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Persona> personaListOrphanCheck = departamento.getPersonaList();
            for (Persona personaListOrphanCheckPersona : personaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Persona " + personaListOrphanCheckPersona + " in its personaList field has a non-nullable idDepartamento field.");
            }
            List<Kardex> kardexListOrphanCheck = departamento.getKardexList();
            for (Kardex kardexListOrphanCheckKardex : kardexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Kardex " + kardexListOrphanCheckKardex + " in its kardexList field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
