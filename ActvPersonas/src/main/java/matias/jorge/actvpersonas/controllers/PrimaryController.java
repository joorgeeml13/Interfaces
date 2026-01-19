package matias.jorge.actvpersonas.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import matias.jorge.actvpersonas.models.Persona;

public class PrimaryController implements Initializable{
    
    private ObservableList<Persona> persnList;
    
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfEdad;
    
    @FXML private TableView<Persona> tablePersonas;
    @FXML private TableColumn<Persona, String> columnNombre;
    @FXML private TableColumn<Persona, String> columnApellido;
    @FXML private TableColumn<Persona, Integer> columnEdad;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        persnList = FXCollections.observableArrayList();
        tablePersonas.setItems(persnList);
        
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellido.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
    }
    
    @FXML
    private void agregarPersona(){
        if(validarCamposPersona()){
            Persona p = tablePersonas.getSelectionModel().getSelectedItem();
            if(p != null){
                
            }else{
                persnList.add(new Persona(tfNombre.getText(), tfApellido.getText(), Integer.parseInt(tfEdad.getText())));
            }
        }else{
            mostrarAlerta("Campos invalidos", AlertType.ERROR);
        }
    }
    
    @FXML
    private void eliminarSelecionado(){
        Persona p;
        if((p = getSelected()) != null){
            persnList.remove(p);
        }
    }
    
    @FXML
    private void modificarSelected(){
        Persona p;
        
    }
    
    private boolean validarCamposPersona(){
        return !(tfNombre.getText().isBlank() || tfApellido.getText().isBlank() || !isNumber(tfEdad.getText()));
    }
    
    private boolean isNumber(String x){
        try{
            Integer.parseInt(x);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    private void mostrarAlerta(String mensaje, AlertType tipo){
        Alert a = new Alert(tipo);
        a.setContentText(mensaje);
        a.show();
    }
    
    private Persona getSelected(){
        Persona p = tablePersonas.getSelectionModel().getSelectedItem();
        if(p == null) mostrarAlerta("Seleccione un valor", AlertType.ERROR);
        return p;
    }
}
