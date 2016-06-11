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
import entidades.Persona;
import entidades.Compra;
import java.util.ArrayList;
import java.util.List;
import entidades.Venta;
import entidades.Permisousuario;
import entidades.Bitacora;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo Lopez <panlopezv@gmail.com>
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getCompraList() == null) {
            usuario.setCompraList(new ArrayList<Compra>());
        }
        if (usuario.getVentaList() == null) {
            usuario.setVentaList(new ArrayList<Venta>());
        }
        if (usuario.getPermisousuarioList() == null) {
            usuario.setPermisousuarioList(new ArrayList<Permisousuario>());
        }
        if (usuario.getBitacoraList() == null) {
            usuario.setBitacoraList(new ArrayList<Bitacora>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idPersona = usuario.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                usuario.setIdPersona(idPersona);
            }
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : usuario.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getIdCompra());
                attachedCompraList.add(compraListCompraToAttach);
            }
            usuario.setCompraList(attachedCompraList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : usuario.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getIdVenta());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            usuario.setVentaList(attachedVentaList);
            List<Permisousuario> attachedPermisousuarioList = new ArrayList<Permisousuario>();
            for (Permisousuario permisousuarioListPermisousuarioToAttach : usuario.getPermisousuarioList()) {
                permisousuarioListPermisousuarioToAttach = em.getReference(permisousuarioListPermisousuarioToAttach.getClass(), permisousuarioListPermisousuarioToAttach.getIdPermisoUsuario());
                attachedPermisousuarioList.add(permisousuarioListPermisousuarioToAttach);
            }
            usuario.setPermisousuarioList(attachedPermisousuarioList);
            List<Bitacora> attachedBitacoraList = new ArrayList<Bitacora>();
            for (Bitacora bitacoraListBitacoraToAttach : usuario.getBitacoraList()) {
                bitacoraListBitacoraToAttach = em.getReference(bitacoraListBitacoraToAttach.getClass(), bitacoraListBitacoraToAttach.getIdBitacora());
                attachedBitacoraList.add(bitacoraListBitacoraToAttach);
            }
            usuario.setBitacoraList(attachedBitacoraList);
            em.persist(usuario);
            if (idPersona != null) {
                idPersona.getUsuarioList().add(usuario);
                idPersona = em.merge(idPersona);
            }
            for (Compra compraListCompra : usuario.getCompraList()) {
                Usuario oldIdUsuarioOfCompraListCompra = compraListCompra.getIdUsuario();
                compraListCompra.setIdUsuario(usuario);
                compraListCompra = em.merge(compraListCompra);
                if (oldIdUsuarioOfCompraListCompra != null) {
                    oldIdUsuarioOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldIdUsuarioOfCompraListCompra = em.merge(oldIdUsuarioOfCompraListCompra);
                }
            }
            for (Venta ventaListVenta : usuario.getVentaList()) {
                Usuario oldIdUsuarioOfVentaListVenta = ventaListVenta.getIdUsuario();
                ventaListVenta.setIdUsuario(usuario);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldIdUsuarioOfVentaListVenta != null) {
                    oldIdUsuarioOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldIdUsuarioOfVentaListVenta = em.merge(oldIdUsuarioOfVentaListVenta);
                }
            }
            for (Permisousuario permisousuarioListPermisousuario : usuario.getPermisousuarioList()) {
                Usuario oldIdUsuarioOfPermisousuarioListPermisousuario = permisousuarioListPermisousuario.getIdUsuario();
                permisousuarioListPermisousuario.setIdUsuario(usuario);
                permisousuarioListPermisousuario = em.merge(permisousuarioListPermisousuario);
                if (oldIdUsuarioOfPermisousuarioListPermisousuario != null) {
                    oldIdUsuarioOfPermisousuarioListPermisousuario.getPermisousuarioList().remove(permisousuarioListPermisousuario);
                    oldIdUsuarioOfPermisousuarioListPermisousuario = em.merge(oldIdUsuarioOfPermisousuarioListPermisousuario);
                }
            }
            for (Bitacora bitacoraListBitacora : usuario.getBitacoraList()) {
                Usuario oldIdUsuarioOfBitacoraListBitacora = bitacoraListBitacora.getIdUsuario();
                bitacoraListBitacora.setIdUsuario(usuario);
                bitacoraListBitacora = em.merge(bitacoraListBitacora);
                if (oldIdUsuarioOfBitacoraListBitacora != null) {
                    oldIdUsuarioOfBitacoraListBitacora.getBitacoraList().remove(bitacoraListBitacora);
                    oldIdUsuarioOfBitacoraListBitacora = em.merge(oldIdUsuarioOfBitacoraListBitacora);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Persona idPersonaOld = persistentUsuario.getIdPersona();
            Persona idPersonaNew = usuario.getIdPersona();
            List<Compra> compraListOld = persistentUsuario.getCompraList();
            List<Compra> compraListNew = usuario.getCompraList();
            List<Venta> ventaListOld = persistentUsuario.getVentaList();
            List<Venta> ventaListNew = usuario.getVentaList();
            List<Permisousuario> permisousuarioListOld = persistentUsuario.getPermisousuarioList();
            List<Permisousuario> permisousuarioListNew = usuario.getPermisousuarioList();
            List<Bitacora> bitacoraListOld = persistentUsuario.getBitacoraList();
            List<Bitacora> bitacoraListNew = usuario.getBitacoraList();
            List<String> illegalOrphanMessages = null;
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its idUsuario field is not nullable.");
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its idUsuario field is not nullable.");
                }
            }
            for (Permisousuario permisousuarioListOldPermisousuario : permisousuarioListOld) {
                if (!permisousuarioListNew.contains(permisousuarioListOldPermisousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permisousuario " + permisousuarioListOldPermisousuario + " since its idUsuario field is not nullable.");
                }
            }
            for (Bitacora bitacoraListOldBitacora : bitacoraListOld) {
                if (!bitacoraListNew.contains(bitacoraListOldBitacora)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bitacora " + bitacoraListOldBitacora + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                usuario.setIdPersona(idPersonaNew);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getIdCompra());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            usuario.setCompraList(compraListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getIdVenta());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            usuario.setVentaList(ventaListNew);
            List<Permisousuario> attachedPermisousuarioListNew = new ArrayList<Permisousuario>();
            for (Permisousuario permisousuarioListNewPermisousuarioToAttach : permisousuarioListNew) {
                permisousuarioListNewPermisousuarioToAttach = em.getReference(permisousuarioListNewPermisousuarioToAttach.getClass(), permisousuarioListNewPermisousuarioToAttach.getIdPermisoUsuario());
                attachedPermisousuarioListNew.add(permisousuarioListNewPermisousuarioToAttach);
            }
            permisousuarioListNew = attachedPermisousuarioListNew;
            usuario.setPermisousuarioList(permisousuarioListNew);
            List<Bitacora> attachedBitacoraListNew = new ArrayList<Bitacora>();
            for (Bitacora bitacoraListNewBitacoraToAttach : bitacoraListNew) {
                bitacoraListNewBitacoraToAttach = em.getReference(bitacoraListNewBitacoraToAttach.getClass(), bitacoraListNewBitacoraToAttach.getIdBitacora());
                attachedBitacoraListNew.add(bitacoraListNewBitacoraToAttach);
            }
            bitacoraListNew = attachedBitacoraListNew;
            usuario.setBitacoraList(bitacoraListNew);
            usuario = em.merge(usuario);
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getUsuarioList().remove(usuario);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getUsuarioList().add(usuario);
                idPersonaNew = em.merge(idPersonaNew);
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Usuario oldIdUsuarioOfCompraListNewCompra = compraListNewCompra.getIdUsuario();
                    compraListNewCompra.setIdUsuario(usuario);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldIdUsuarioOfCompraListNewCompra != null && !oldIdUsuarioOfCompraListNewCompra.equals(usuario)) {
                        oldIdUsuarioOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldIdUsuarioOfCompraListNewCompra = em.merge(oldIdUsuarioOfCompraListNewCompra);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Usuario oldIdUsuarioOfVentaListNewVenta = ventaListNewVenta.getIdUsuario();
                    ventaListNewVenta.setIdUsuario(usuario);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldIdUsuarioOfVentaListNewVenta != null && !oldIdUsuarioOfVentaListNewVenta.equals(usuario)) {
                        oldIdUsuarioOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldIdUsuarioOfVentaListNewVenta = em.merge(oldIdUsuarioOfVentaListNewVenta);
                    }
                }
            }
            for (Permisousuario permisousuarioListNewPermisousuario : permisousuarioListNew) {
                if (!permisousuarioListOld.contains(permisousuarioListNewPermisousuario)) {
                    Usuario oldIdUsuarioOfPermisousuarioListNewPermisousuario = permisousuarioListNewPermisousuario.getIdUsuario();
                    permisousuarioListNewPermisousuario.setIdUsuario(usuario);
                    permisousuarioListNewPermisousuario = em.merge(permisousuarioListNewPermisousuario);
                    if (oldIdUsuarioOfPermisousuarioListNewPermisousuario != null && !oldIdUsuarioOfPermisousuarioListNewPermisousuario.equals(usuario)) {
                        oldIdUsuarioOfPermisousuarioListNewPermisousuario.getPermisousuarioList().remove(permisousuarioListNewPermisousuario);
                        oldIdUsuarioOfPermisousuarioListNewPermisousuario = em.merge(oldIdUsuarioOfPermisousuarioListNewPermisousuario);
                    }
                }
            }
            for (Bitacora bitacoraListNewBitacora : bitacoraListNew) {
                if (!bitacoraListOld.contains(bitacoraListNewBitacora)) {
                    Usuario oldIdUsuarioOfBitacoraListNewBitacora = bitacoraListNewBitacora.getIdUsuario();
                    bitacoraListNewBitacora.setIdUsuario(usuario);
                    bitacoraListNewBitacora = em.merge(bitacoraListNewBitacora);
                    if (oldIdUsuarioOfBitacoraListNewBitacora != null && !oldIdUsuarioOfBitacoraListNewBitacora.equals(usuario)) {
                        oldIdUsuarioOfBitacoraListNewBitacora.getBitacoraList().remove(bitacoraListNewBitacora);
                        oldIdUsuarioOfBitacoraListNewBitacora = em.merge(oldIdUsuarioOfBitacoraListNewBitacora);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = usuario.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable idUsuario field.");
            }
            List<Venta> ventaListOrphanCheck = usuario.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable idUsuario field.");
            }
            List<Permisousuario> permisousuarioListOrphanCheck = usuario.getPermisousuarioList();
            for (Permisousuario permisousuarioListOrphanCheckPermisousuario : permisousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Permisousuario " + permisousuarioListOrphanCheckPermisousuario + " in its permisousuarioList field has a non-nullable idUsuario field.");
            }
            List<Bitacora> bitacoraListOrphanCheck = usuario.getBitacoraList();
            for (Bitacora bitacoraListOrphanCheckBitacora : bitacoraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Bitacora " + bitacoraListOrphanCheckBitacora + " in its bitacoraList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idPersona = usuario.getIdPersona();
            if (idPersona != null) {
                idPersona.getUsuarioList().remove(usuario);
                idPersona = em.merge(idPersona);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
