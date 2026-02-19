package com.jorgematias.controller;

import com.jorgematias.dao.LibreriaDAO;
import com.jorgematias.dao.LibreriaDAOImpl;
import com.jorgematias.model.Libro;
import com.jorgematias.service.NavigationService;
import com.jorgematias.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LibroFormController implements Initializable {

    @FXML private Label lblTituloForm;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private ComboBox<String> comboGenero;
    @FXML private DatePicker dpFecha;
    @FXML private Spinner<Integer> spinEjemplares;
    @FXML private Label lblRutaImagen;
    @FXML private ImageView imgPreview;

    private LibreriaDAO dao = new LibreriaDAOImpl();
    private Libro libroActual;
    private byte[] imagenBytes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboGenero.getItems().addAll("Ficción", "Ciencia", "Historia", "Tecnología", "Fantasía", "Otros");
        spinEjemplares.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));

        libroActual = SessionManager.getInstance().getLibro();

        if (libroActual != null) {
            lblTituloForm.setText("Editar Libro");
            txtIsbn.setText(libroActual.getIsbn());
            txtIsbn.setDisable(true);
            txtTitulo.setText(libroActual.getTitulo());
            txtAutor.setText(libroActual.getAutor());
            comboGenero.setValue(libroActual.getGenero());
            dpFecha.setValue(libroActual.getFechaPublicacion());
            spinEjemplares.getValueFactory().setValue(libroActual.getEjemplaresTotales());
            imagenBytes = libroActual.getPortada();

            if (imagenBytes != null) {
                imgPreview.setImage(new Image(new ByteArrayInputStream(imagenBytes)));
                lblRutaImagen.setText("Imagen cargada");
            }
        } else {
            dpFecha.setValue(LocalDate.now());
        }
    }

    @FXML
    private void handleSeleccionarPortada() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        File file = fc.showOpenDialog(txtTitulo.getScene().getWindow());
        
        if (file != null) {
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                imgPreview.setImage(new Image(new ByteArrayInputStream(imagenBytes)));
                lblRutaImagen.setText(file.getName());
            } catch (IOException e) {
                showAlert("Error", "No se pudo leer la imagen.");
            }
        }
    }

    @FXML
    private void handleGuardar() {
        if (txtIsbn.getText().isEmpty() || txtTitulo.getText().isEmpty() || txtAutor.getText().isEmpty() || comboGenero.getValue() == null) {
            showAlert("Error", "Rellena los campos, no seas vago.");
            return;
        }

        boolean esNuevo = (libroActual == null);
        if (esNuevo) libroActual = new Libro();

        libroActual.setIsbn(txtIsbn.getText());
        libroActual.setTitulo(txtTitulo.getText());
        libroActual.setAutor(txtAutor.getText());
        libroActual.setGenero(comboGenero.getValue());
        libroActual.setFechaPublicacion(dpFecha.getValue());
        
        int nuevosTotales = spinEjemplares.getValue();
        if (!esNuevo) {
            int diferencia = nuevosTotales - libroActual.getEjemplaresTotales();
            libroActual.setEjemplaresDisponibles(libroActual.getEjemplaresDisponibles() + diferencia);
        } else {
            libroActual.setEjemplaresDisponibles(nuevosTotales);
        }
        libroActual.setEjemplaresTotales(nuevosTotales);
        libroActual.setPortada(imagenBytes);

        try {
            if (esNuevo) dao.saveLibro(libroActual);
            else dao.updateLibro(libroActual);
            volverAlDashboard();
        } catch (Exception e) {
            showAlert("Error", "Ese ISBN ya existe o hay un problema con la base de datos.");
        }
    }

    @FXML
    private void handleCancelar() {
        volverAlDashboard();
    }

    private void volverAlDashboard() {
        SessionManager.getInstance().setLibro(libroActual);
        try { NavigationService.switchScene("/views/Dashboard.fxml", txtTitulo); } 
        catch (Exception e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}