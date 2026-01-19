package matias.jorge.actvlogin.controllers.singUp;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author alumno
 */
public class SingUpController {
    
    @FXML
    private TextField singUpTfPassword;
    
    @FXML
    private TextField singUpTfVerfPassword;
    
    @FXML
    private PasswordField singUpPassworField;
    
    @FXML
    private PasswordField singUpVerfPassword;
    
    @FXML
    public void initialize(){
        singUpTfPassword.textProperty().bindBidirectional(singUpPassworField.textProperty());
        singUpTfVerfPassword.textProperty().bindBidirectional(singUpVerfPassword.textProperty());
        
        singUpTfPassword.managedProperty().bind(singUpTfPassword.visibleProperty());
        singUpPassworField.managedProperty().bind(singUpPassworField.visibleProperty());
        
        singUpTfVerfPassword.managedProperty().bind(singUpTfVerfPassword.visibleProperty());
        singUpVerfPassword.managedProperty().bind(singUpVerfPassword.visibleProperty());
        
        singUpTfVerfPassword.setVisible(false);
        singUpTfPassword.setVisible(false);
    }
}
