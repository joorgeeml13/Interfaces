package com.jorgematias.controller;

import com.jorgematias.dao.LibreriaDAO;
import com.jorgematias.dao.LibreriaDAOImpl;
import com.jorgematias.model.Libro;
import com.jorgematias.model.Prestamo;
import com.jorgematias.model.Rol;
import com.jorgematias.model.Usuario;
import com.jorgematias.service.NavigationService;
import com.jorgematias.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private Label lblRol;
    @FXML private VBox boxAccionesAdmin;
    @FXML private VBox boxAccionesEstudiante;
    @FXML private HBox boxBotonesPrestamoEstudiante;
    @FXML private Label lblTituloPrestamos;
    @FXML private TextField searchLibro;
    @FXML private ComboBox<String> comboGenero;
    @FXML private CheckBox checkDisponibles;
    @FXML private ImageView imgPortada;
    @FXML private Label lblDetalleTitulo;
    @FXML private Button btnPrestar;

    @FXML private TableView<Libro> tableLibros;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, String> colGenero;
    @FXML private TableColumn<Libro, Integer> colDisp;

    @FXML private TableView<Prestamo> tablePrestamos;
    @FXML private TableColumn<Prestamo, Long> colPrestamoId;
    @FXML private TableColumn<Prestamo, String> colPrestamoLibro;
    @FXML private TableColumn<Prestamo, LocalDate> colPrestamoFin;
    @FXML private TableColumn<Prestamo, Boolean> colPrestamoDevuelto;

    private LibreriaDAO dao = new LibreriaDAOImpl();
    private ObservableList<Libro> masterLibros = FXCollections.observableArrayList();
    private ObservableList<Prestamo> masterPrestamos = FXCollections.observableArrayList();
    private Usuario usuarioActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioActual = SessionManager.getInstance().getUser();
        if (usuarioActual == null) return;

        lblWelcome.setText("Hola, " + usuarioActual.getNombre());
        lblRol.setText("[" + usuarioActual.getRol().toString() + "]");

        boolean esAdmin = usuarioActual.getRol() == Rol.ADMIN;
        boxAccionesAdmin.setVisible(esAdmin);
        boxAccionesAdmin.setManaged(esAdmin);
        boxAccionesEstudiante.setVisible(!esAdmin);
        boxAccionesEstudiante.setManaged(!esAdmin);
        boxBotonesPrestamoEstudiante.setVisible(!esAdmin);
        boxBotonesPrestamoEstudiante.setManaged(!esAdmin);

        comboGenero.getItems().addAll("Todos", "Ficción", "Ciencia", "Historia", "Tecnología");

        configurarTablas();
        cargarDatos(esAdmin);

        tableLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> mostrarDetalle(newV));
    }

    private void configurarTablas() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDisp.setCellValueFactory(new PropertyValueFactory<>("ejemplaresDisponibles"));

        colPrestamoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrestamoLibro.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLibro().getTitulo()));
        colPrestamoFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colPrestamoDevuelto.setCellValueFactory(new PropertyValueFactory<>("devuelto"));
    }

    private void cargarDatos(boolean esAdmin) {
        masterLibros.setAll(dao.getAllLibros());
        FilteredList<Libro> filteredLibros = new FilteredList<>(masterLibros, p -> true);

        searchLibro.textProperty().addListener((obs, oldV, newV) -> aplicarFiltros(filteredLibros));
        comboGenero.valueProperty().addListener((obs, oldV, newV) -> aplicarFiltros(filteredLibros));
        checkDisponibles.selectedProperty().addListener((obs, oldV, newV) -> aplicarFiltros(filteredLibros));

        tableLibros.setItems(filteredLibros);

        if (esAdmin) {
            lblTituloPrestamos.setText("Todos los Préstamos");
            masterPrestamos.setAll(dao.getPrestamosAdmin());
        } else {
            lblTituloPrestamos.setText("Mis Préstamos");
            masterPrestamos.setAll(dao.getPrestamosEstudiante(usuarioActual.getId()));
        }
        tablePrestamos.setItems(masterPrestamos);
    }

    private void aplicarFiltros(FilteredList<Libro> filteredData) {
        filteredData.setPredicate(libro -> {
            String txt = searchLibro.getText();
            String gen = comboGenero.getValue();
            boolean disp = checkDisponibles.isSelected();

            boolean fTxt = txt == null || txt.isEmpty() || libro.getTitulo().toLowerCase().contains(txt.toLowerCase());
            boolean fGen = gen == null || gen.equals("Todos") || libro.getGenero().equalsIgnoreCase(gen);
            boolean fDisp = !disp || libro.getEjemplaresDisponibles() > 0;

            return fTxt && fGen && fDisp;
        });
    }

    private void mostrarDetalle(Libro libro) {
        if (libro == null) return;
        lblDetalleTitulo.setText(libro.getTitulo() + "\n" + libro.getAutor());
        if (libro.getPortada() != null) {
            imgPortada.setImage(new Image(new ByteArrayInputStream(libro.getPortada())));
        } else {
            imgPortada.setImage(null);
        }
        if (libro.getEjemplaresDisponibles() > 0) {
            btnPrestar.setText("Pedir Prestado");
            btnPrestar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        } else {
            btnPrestar.setText("Reservar (A la cola)");
            btnPrestar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        }
    }

    @FXML private void handleAccionEstudiante() {
        Libro l = tableLibros.getSelectionModel().getSelectedItem();
        if (l == null) return;
        if (l.getEjemplaresDisponibles() > 0) {
            dao.prestarLibro(l, usuarioActual);
            showAlert("Éxito", "Libro prestado correctamente.");
        } else {
            dao.reservarLibro(l, usuarioActual);
            showAlert("Éxito", "Libro reservado.");
        }
        cargarDatos(false);
    }

    @FXML private void handleDevolver() {
        Prestamo p = tablePrestamos.getSelectionModel().getSelectedItem();
        if (p != null && !p.isDevuelto()) {
            dao.devolverLibro(p);
            showAlert("Devuelto", "Gracias por devolverlo.");
            cargarDatos(false);
        }
    }

    @FXML private void handleProrrogar() {
        Prestamo p = tablePrestamos.getSelectionModel().getSelectedItem();
        if (p != null) {
            if (dao.prorrogarPrestamo(p)) showAlert("Prorrogado", "Tienes 15 días más.");
            else showAlert("Error", "No se puede prorrogar este libro.");
            cargarDatos(false);
        }
    }

    @FXML private void handleAñadirLibro() { 
        SessionManager.getInstance().setLibro(null);
        try { NavigationService.switchScene("/views/LibroForm.fxml", tableLibros); } catch (Exception e) {}
    }
    @FXML private void handleEditarLibro() {
        Libro l = tableLibros.getSelectionModel().getSelectedItem();
        if (l != null) {
            SessionManager.getInstance().setLibro(l);
            try { NavigationService.switchScene("/views/LibroForm.fxml", tableLibros); } catch (Exception e) {}
        } else {
            showAlert("Atención", "Selecciona un libro para editar.");
        }
    }
    
    @FXML private void handleEliminarLibro() {
        Libro l = tableLibros.getSelectionModel().getSelectedItem();
        if (l != null) {
            dao.deleteLibro(l);
            cargarDatos(true);
        }
    }

    @FXML private void handleLogout() {
        SessionManager.getInstance().setUser(null);
        try { NavigationService.switchScene("/views/Login.fxml", lblWelcome); } catch (Exception e) {}
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}