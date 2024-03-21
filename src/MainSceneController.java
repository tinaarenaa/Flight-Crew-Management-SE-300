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
//import javafx.scene.text.Text;
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

    public void setUsername(String username) {
        user_name.setText(username);
    }


    @FXML
    void btnSubmitClicked(ActionEvent event) {
        String credentialsFilePath = "credentials.txt";
        String inputFilePath = "input_username_password.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    String fileUsername = credentials[0];
                    String filePassword = credentials[1];

                    // Checking for login
                    if (!New_Account_Checkbox.isSelected() && tfUsername.getText().equals(fileUsername) && tfPassword.getText().equals(filePassword)) {
                        showAlert("Login Successful", "Welcome!", Alert.AlertType.INFORMATION);
                        //switchToScene2(event);
                        System.out.printf("Username: %s", tfUsername.getText() );
                        
                        //user_name.setText(tfUsername.getText());
                        specialButton.setVisible(true);
                    } else if (!New_Account_Checkbox.isSelected()) {
                        showAlert("Login Failed", "Username or password is incorrect. Try Again!", Alert.AlertType.ERROR);
                    }

                    // Checking for new account creation
                    if (New_Account_Checkbox.isSelected() && !tfUsername.getText().equals(fileUsername) && !tfPassword.getText().equals(filePassword)) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath, true))) {
                            writer.write(tfUsername.getText() + "," + tfPassword.getText());
                            writer.newLine();
                            showAlert("Account Successfully Created", "Welcome!", Alert.AlertType.INFORMATION);                  
                            //switchToScene2(event);
                            
                            specialButton.setVisible(true);
                            
                        } catch (IOException e) {
                            showAlert("Error", "Could not save the entered credentials.", Alert.AlertType.ERROR);
                        }
                    } else if (New_Account_Checkbox.isSelected()) {
                        showAlert("Cannot Create Account", "Username or Password are already in use. Please change it.", Alert.AlertType.INFORMATION);
                    }
                } else {
                    showAlert("Error", "Credentials file format is incorrect.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Credentials file is empty.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("Error", "Could not read the credentials file.", Alert.AlertType.ERROR);
        }

    }

    public void switchToScene2(ActionEvent event) {
        try {
            //System.out.println("Inside switchToScene2 method");
            root = FXMLLoader.load(getClass().getResource("second_scene.fxml"));
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            primaryStage.setScene(scene);
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


