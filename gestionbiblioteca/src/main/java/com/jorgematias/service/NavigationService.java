package com.jorgematias.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class NavigationService {

    /**
     * Cambia la escena actual por una nueva.
     * @param fxmlPath Ruta del archivo FXML (ej: "/views/AdminMain.fxml")
     * @param node Cualquier nodo de la escena actual para obtener el Stage
     */
    public static void switchScene(String fxmlPath, Node node) {
        try {
            // Cargar el archivo FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(NavigationService.class.getResource(fxmlPath)));
            
            // Obtener el Stage (ventana) actual desde el nodo proporcionado
            Stage stage = (Stage) node.getScene().getWindow();
            
            // Crear la nueva escena y asignarla al escenario
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("No se encontró el archivo FXML: " + fxmlPath);
        }
    }
}
