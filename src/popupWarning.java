import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class popupWarning {

    public static void displayWarning(String title, String message) {
        // Ensure this runs on the JavaFX thread
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait();
        });
    }
}

