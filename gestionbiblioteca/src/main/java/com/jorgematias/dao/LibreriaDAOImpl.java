package com.jorgematias.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import com.jorgematias.model.Libro;
import com.jorgematias.model.Prestamo;
import com.jorgematias.model.Reserva;
import com.jorgematias.model.Usuario;
import com.jorgematias.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class LibreriaDAOImpl implements LibreriaDAO {

    private <R> R executeTransaction(Function<EntityManager, R> action) {
        EntityTransaction transaction = null;
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
            R result = action.apply(em);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Usuario authenticate(String username, String password) {
        return executeTransaction(em -> {
            try {
                return em.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.password = :password", Usuario.class)
                        .setParameter("usuario", username)
                        .setParameter("password", password)
                        .getSingleResult();
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Override
    public void saveLibro(Libro libro) {
        executeTransaction(em -> { em.persist(libro); return null; });
    }

    @Override
    public void updateLibro(Libro libro) {
        executeTransaction(em -> { em.merge(libro); return null; });
    }

    @Override
    public void deleteLibro(Libro libro) {
        executeTransaction(em -> {
            Libro l = em.merge(libro);
            em.remove(l);
            return null;
        });
    }

    @Override
    public List<Libro> getAllLibros() {
        return executeTransaction(em -> em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList());
    }

    @Override
    public List<Libro> getAllLibrosDisponibles() {
        return executeTransaction(em -> em.createQuery("SELECT l FROM Libro l WHERE l.ejemplaresDisponibles > 0", Libro.class).getResultList());
    }

    @Override
    public List<Prestamo> getPrestamosAdmin() {
        return executeTransaction(em -> em.createQuery("SELECT p FROM Prestamo p", Prestamo.class).getResultList());
    }

    @Override
    public List<Prestamo> getPrestamosEstudiante(Long usuarioId) {
        return executeTransaction(em -> em.createQuery("SELECT p FROM Prestamo p WHERE p.usuario.id = :uid", Prestamo.class)
                .setParameter("uid", usuarioId).getResultList());
    }

    @Override
    public List<Reserva> getReservasAdmin() {
        return executeTransaction(em -> em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList());
    }

    @Override
    public boolean prestarLibro(Libro libro, Usuario usuario) {
        return executeTransaction(em -> {
            Libro l = em.find(Libro.class, libro.getId());
            if (l.getEjemplaresDisponibles() > 0) {
                l.setEjemplaresDisponibles(l.getEjemplaresDisponibles() - 1);
                Prestamo p = new Prestamo();
                p.setLibro(l);
                p.setUsuario(em.find(Usuario.class, usuario.getId()));
                p.setFechaInicio(LocalDate.now());
                p.setFechaFin(LocalDate.now().plusDays(15));
                em.persist(p);
                em.merge(l);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean reservarLibro(Libro libro, Usuario usuario) {
        return executeTransaction(em -> {
            Reserva r = new Reserva();
            r.setLibro(em.find(Libro.class, libro.getId()));
            r.setUsuario(em.find(Usuario.class, usuario.getId()));
            r.setFechaReserva(LocalDate.now());
            em.persist(r);
            return true;
        });
    }

    @Override
    public boolean devolverLibro(Prestamo prestamo) {
        return executeTransaction(em -> {
            Prestamo p = em.find(Prestamo.class, prestamo.getId());
            p.setDevuelto(true);
            Libro l = p.getLibro();
            l.setEjemplaresDisponibles(l.getEjemplaresDisponibles() + 1);
            em.merge(p);
            em.merge(l);
            return true;
        });
    }

    @Override
    public boolean prorrogarPrestamo(Prestamo prestamo) {
        return executeTransaction(em -> {
            Prestamo p = em.find(Prestamo.class, prestamo.getId());
            if (!p.isProrrogado() && !p.isDevuelto()) {
                p.setFechaFin(p.getFechaFin().plusDays(15));
                p.setProrrogado(true);
                em.merge(p);
                return true;
            }
            return false;
        });
    }
}