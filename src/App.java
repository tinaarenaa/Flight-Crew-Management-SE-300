import java.io.IOException;

import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException{
        
  
    Parent root;

    try {
        root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
        Scene scene = new Scene(root); 
        primaryStage.setTitle("Flight Crew Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    } catch (IOException e) {

    }
}
 
 public static void main(String[] args) {
        launch(args);
    }
}
