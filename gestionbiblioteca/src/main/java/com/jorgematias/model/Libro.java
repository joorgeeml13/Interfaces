package com.jorgematias.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] portada;

    @Column(name = "ruta_resumen_pdf")
    private String resumenPdf;

    @Column(nullable = false)
    private int ejemplaresTotales;

    @Column(nullable = false)
    private int ejemplaresDisponibles;
}