package matias.jorge.actvlogin.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class MainController {
    
    @FXML
    private StackPane formPane;
    
    @FXML
    private Button buttonToLogIn;
    
    @FXML
    private Button buttonToSingUp;
    
    @FXML
    public void initialize(){
        cargarLogIn();
        

    }
    
    public void cargarLogIn(){
        cargarForm("/matias/jorge/actvlogin/logIn/LogIn.fxml");
    }
    
    public void cargarRegister(){
        cargarForm("/matias/jorge/actvlogin/singUp/singUp.fxml");
    }
    
    private void cargarForm(String ruta){
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource(ruta));
            Parent p = loader.load();
            
            formPane.getChildren().setAll(p);
        }catch (IOException e) {
            System.err.println("Error al cargar la vista: " + ruta);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("No se encontró el archivo FXML. Revisa la ruta: " + ruta);
        }
    }
}