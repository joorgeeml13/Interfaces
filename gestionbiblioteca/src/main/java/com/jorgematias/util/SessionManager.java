package com.jorgematias.util;

import com.jorgematias.model.Libro;
import com.jorgematias.model.Usuario;

public class SessionManager {
    private static SessionManager instance;
    private Usuario user;
    private Libro libro;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public static void setInstance(SessionManager instance) {
        SessionManager.instance = instance;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
