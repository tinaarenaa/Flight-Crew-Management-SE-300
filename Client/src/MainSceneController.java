import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.util.*;

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




public class MainSceneController implements Initializable {

    private Stage primaryStage;
    private Scene scene;
    private Parent root;
    private clientComm client = new clientComm();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      client.startConnection("127.0.0.1", 5555);
    }


    @FXML
    void btnSubmitClicked(ActionEvent event) {
        
        if(tfUsername.getText() != null && tfPassword.getText() != null) {
          if(!New_Account_Checkbox.isSelected()) {
            String resp = client.sendCommand("LOGIN:" + tfUsername.getText() + ":" + tfPassword.getText());
            if(resp.equals("SUCCESS")) {
              showAlert("Login Successful", "Welcome!", Alert.AlertType.INFORMATION);
              userName = tfUsername.getText();
              specialButton.setVisible(true);
            } else {
              showAlert("Login Failed", "Username or password is incorrect. Try Again!", Alert.AlertType.INFORMATION);
            }
          } else {
            String resp = client.sendCommand("NEW_ACCT:" + tfUsername.getText() + ":" + tfPassword.getText());
            if(resp.equals("SUCCESS")) {
              showAlert("Account Successfully Created", "Welcome!", Alert.AlertType.INFORMATION);
              userName = tfUsername.getText();
              specialButton.setVisible(true);
            } else {
              showAlert("Login Failed", "Username or password is incorrect. Try Again!", Alert.AlertType.INFORMATION);
            }
          }
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
            scene2controller.transfer(userName, client);
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


