import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

//import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import javafx.scene.input.InputMethodEvent;
//import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.Node;




public class MainSceneController {

    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    private fileManipulation file = new fileManipulation();
    private String userName;

    @FXML
    private CheckBox New_Account_Checkbox;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    private Button specialButton; 

    @FXML
    void btn_edit_account(ActionEvent event) {

    }

    @FXML
    void btn_edit_crew_roster(ActionEvent event) {

    }

    @FXML
    private TextField user_name;


    @FXML
    void btnSubmitClicked(ActionEvent event) {

        if(!New_Account_Checkbox.isSelected() && file.testPass(tfUsername.getText(),tfPassword.getText())) {
          showAlert("Login Successful", "Welcome!", Alert.AlertType.INFORMATION);
          userName = tfUsername.getText();
          specialButton.setVisible(true);
        } else if(!New_Account_Checkbox.isSelected()) {
          showAlert("Login Failed", "Username or password is incorrect. Try Again!", Alert.AlertType.INFORMATION);
        }
        

        if(New_Account_Checkbox.isSelected() && (tfUsername.getText() != null) && (tfPassword.getText() != null)) {
          file.newAccount(tfUsername.getText(), tfPassword.getText());
          showAlert("Account Successfully Created", "Welcome!", Alert.AlertType.INFORMATION);
          userName = tfUsername.getText();
          specialButton.setVisible(true);
        }
    }
    
    @FXML
    public void switchToScene2(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("second_scene.fxml"));
            root = loader.load();
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            HomeController scene2controller = loader.getController();
            scene2controller.transfer(userName);
            primaryStage.show();
            

        } catch (Exception e) { // Catching Exception here to catch any type of error
            e.printStackTrace(); // This will print the stack trace to the console
            showAlert("Error", "Failed to load the Home Screen: " , Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}


