package matias.jorge.actvlogin.controllers.logIn;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author alumno
 */
public class LogInController {
    @FXML
    private CheckBox togglePassword;
    
    @FXML
    private PasswordField logInpasswordField;
    
    @FXML
    private TextField logInTfPassword;
    
    @FXML
    public void initialize(){
        logInTfPassword.textProperty().bindBidirectional(logInpasswordField.textProperty());
        
        logInTfPassword.managedProperty().bind(logInTfPassword.visibleProperty());
        logInpasswordField.managedProperty().bind(logInpasswordField.visibleProperty());
        
        logInTfPassword.setVisible(false);
    }
    
   @FXML
   private void togglePassword(){
        logInTfPassword.setVisible(togglePassword.isSelected());
        logInpasswordField.setVisible(!togglePassword.isSelected());
   }
}
