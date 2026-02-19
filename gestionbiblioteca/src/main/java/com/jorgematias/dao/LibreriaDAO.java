package com.jorgematias.dao;

import java.util.List;
import com.jorgematias.model.Libro;
import com.jorgematias.model.Prestamo;
import com.jorgematias.model.Reserva;
import com.jorgematias.model.Usuario;

public interface LibreriaDAO {
    void saveLibro(Libro libro);
    void updateLibro(Libro libro);
    void deleteLibro(Libro libro);
    List<Libro> getAllLibros();
    List<Libro> getAllLibrosDisponibles();
    List<Prestamo> getPrestamosAdmin();
    List<Prestamo> getPrestamosEstudiante(Long usuarioId);
    List<Reserva> getReservasAdmin();
    Usuario authenticate(String email, String password);
    boolean prestarLibro(Libro libro, Usuario usuario);
    boolean reservarLibro(Libro libro, Usuario usuario);
    boolean devolverLibro(Prestamo prestamo);
    boolean prorrogarPrestamo(Prestamo prestamo);
}