package com.jorgematias.controller;

import com.jorgematias.dao.LibreriaDAO;
import com.jorgematias.dao.LibreriaDAOImpl;
import com.jorgematias.model.Usuario;
import com.jorgematias.service.NavigationService;
import com.jorgematias.util.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Label lblError;


    private LibreriaDAO userDAO = new LibreriaDAOImpl(); 

    @FXML
    private void handleLogin() {
        String username = txtUser.getText();
        String password = txtPass.getText();

        // Si mandas campos vacíos, ni molestamos a la base de datos
        if(username.isEmpty() || password.isEmpty()) {
            lblError.setText("Rellena los campos, no seas vago.");
            return;
        }

        Usuario user = userDAO.authenticate(username, password);

        if (user != null) {
            SessionManager.getInstance().setUser(user);

            NavigationService.switchScene("/views/Dashboard.fxml", txtUser); 
        } else {
            lblError.setText("Error: Datos incorrectos, locotrón.");
            txtPass.clear();
        }
    }
}