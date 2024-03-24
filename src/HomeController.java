import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
<<<<<<< Updated upstream
import javafx.scene.control.ButtonBar.ButtonData;
=======
import javafx.geometry.Insets;
>>>>>>> Stashed changes
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
//import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.Node;

public class HomeController implements Initializable {

    private LinkedList<flightClass> flightList = new LinkedList<flightClass>();
    private LinkedList<crewClass> crewList = new LinkedList<crewClass>();
    private fileManipulation file = new fileManipulation();
    private crewFlightController crewFlightCont = new crewFlightController(); // call functions in this class to do everything with crew and flight management

    @FXML
    private FlowPane calendar;

    @FXML
    private Text month;

    @FXML
    private TextField user_name;

    @FXML
    private Text year;

    @FXML
    private Text userDisplay;

    @FXML
    private VBox container;

    ZonedDateTime dateFocus;
    ZonedDateTime today;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        
    }

    public void transfer(String username) {
      userDisplay.setText("Hello, " + username + "!");
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }


    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
<<<<<<< Updated upstream
private VBox container;

    @FXML
void addCrewMember(ActionEvent event) {
=======
    void addCrewMember(ActionEvent event) {
>>>>>>> Stashed changes
    // Create the custom dialog.
    Dialog<List<String>> dialog = new Dialog<>();
    dialog.setTitle("Add Crew Member");
    dialog.setHeaderText("Enter crew member details");

    // Set the button types.
    ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

    // Create the layout and inputs
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    TextField ageField = new TextField();
    ageField.setPromptText("Age");
    ComboBox<String> genderComboBox = new ComboBox<>();
    genderComboBox.getItems().addAll("Male", "Female", "Other");
    TextField homeAirportField = new TextField();
    homeAirportField.setPromptText("Home Airport");

    grid.add(new Label("Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Age:"), 0, 1);
    grid.add(ageField, 1, 1);
    grid.add(new Label("Gender:"), 0, 2);
    grid.add(genderComboBox, 1, 2);
    grid.add(new Label("Home Airport:"), 0, 3);
    grid.add(homeAirportField, 1, 3);

    dialog.getDialogPane().setContent(grid);

    // Request focus on the name field by default.
    Platform.runLater(nameField::requestFocus);

    // Convert the result when the submit button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == submitButtonType) {
            List<String> details = new ArrayList<>();
            details.add(nameField.getText());
            details.add(ageField.getText());
            details.add(genderComboBox.getValue());
            details.add(homeAirportField.getText());
            return details;
        }
        return null;
    });

    Optional<List<String>> result = dialog.showAndWait();

<<<<<<< Updated upstream
    result.ifPresent(crewMemberDetails -> {
    String name = crewMemberDetails.get(0); // Assuming the first entry is the name
    Label nameLabel = new Label(name);
    nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #040a3a; -fx-font-size: 16px;");
    Platform.runLater(() -> container.getChildren().add(nameLabel));


    // Add additional logic as needed for handling the crew member details
});

=======

    result.ifPresent(crewMemberDetails -> {
        
        String name = crewMemberDetails.get(0); // Assuming the first entry is the name
        Label nameLabel = new Label(name);
        VBox.setMargin(nameLabel, new Insets(5, 0, 5, 0)); // Add 5 units of padding above and below the label
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #040a3a; -fx-font-size: 9px;");
        Platform.runLater(() -> container.getChildren().add(nameLabel));

    });
    
>>>>>>> Stashed changes
}


    @FXML
<<<<<<< Updated upstream
void editAccountSpecifications(ActionEvent event) {
    // Prepare the list of names
    List<String> names = Arrays.asList(
        "Alex Johnson,28,Male",
        "Maria Lee,34,Female",
        "James Williams,45,Male",
        "Patricia Brown,26,Female",
        "John Davis,31,Male",
        "Linda Martinez,37,Female",
        "Robert Miller,52,Male",
        "Elizabeth Moore,24,Female",
        "Michael Taylor,43,Male",
        "Barbara Wilson,39,Female"
    ).stream().map(s -> s.split(",")[0]).collect(Collectors.toList());

    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Edit Account Specifications");
    dialog.setHeaderText("Select the new account type and name");

    // Set the button types.
    ButtonType applyButtonType = new ButtonType("Apply", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    // Name ComboBox
    ComboBox<String> nameComboBox = new ComboBox<>();
    nameComboBox.getItems().addAll(names);
    nameComboBox.getSelectionModel().selectFirst(); // Default to first item

    // Account Type ComboBox
    ComboBox<String> accountTypeComboBox = new ComboBox<>();
    accountTypeComboBox.getItems().addAll("admin", "crew");
    accountTypeComboBox.getSelectionModel().selectFirst(); // Default to first item

    grid.add(new Label("Name:"), 0, 0);
    grid.add(nameComboBox, 1, 0);
    grid.add(new Label("Account Type:"), 0, 1);
    grid.add(accountTypeComboBox, 1, 1);

    dialog.getDialogPane().setContent(grid);

    // Convert the result to a pair of name and account type when the apply button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == applyButtonType) {
            return new Pair<>(nameComboBox.getValue(), accountTypeComboBox.getValue());
        }
        return null;
    });

    Optional<Pair<String, String>> result = dialog.showAndWait();

    result.ifPresent(nameAccountType -> {
        String selectedName = nameAccountType.getKey();
        String selectedAccountType = nameAccountType.getValue();
        System.out.println("Selected Name: " + selectedName + ", Selected Account Type: " + selectedAccountType);
        // Here, handle the selected name and account type update. 
        // For example, you can update the role in your database or application context based on the selected values.
    });
}


    private Pair<String, String> showEditAccountDialog() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Account");
        dialog.setHeaderText("Enter your new username and password");
    
        // Set the button types.
        ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
    
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
    
        TextField username = new TextField();
        username.setPromptText("New Username");
        PasswordField password = new PasswordField();
        password.setPromptText("New Password");
    
        grid.add(new Label("New Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(password, 1, 1);
    
        dialog.getDialogPane().setContent(grid);
    
        // Convert the result to a username-password pair when the submit button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
    
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result.orElse(null);
    }

    
    @FXML
void editAccountButton(ActionEvent event) {
=======
    void changeAccountSpecifications(ActionEvent event) {

            // Prepare the list of names
            List<String> names = Arrays.asList(
                "Alex Johnson,28,Male",
                "Maria Lee,34,Female",
                "James Williams,45,Male",
                "Patricia Brown,26,Female",
                "John Davis,31,Male",
                "Linda Martinez,37,Female",
                "Robert Miller,52,Male",
                "Elizabeth Moore,24,Female",
                "Michael Taylor,43,Male",
                "Barbara Wilson,39,Female"
            ).stream().map(s -> s.split(",")[0]).collect(Collectors.toList());
        
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Edit Account Specifications");
            dialog.setHeaderText("Select the new account type and name");
        
            // Set the button types.
            ButtonType applyButtonType = new ButtonType("Apply", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);
        
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
        
            // Name ComboBox
            ComboBox<String> nameComboBox = new ComboBox<>();
            nameComboBox.getItems().addAll(names);
            nameComboBox.getSelectionModel().selectFirst(); // Default to first item
        
            // Account Type ComboBox
            ComboBox<String> accountTypeComboBox = new ComboBox<>();
            accountTypeComboBox.getItems().addAll("admin", "crew");
            accountTypeComboBox.getSelectionModel().selectFirst(); // Default to first item
        
            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameComboBox, 1, 0);
            grid.add(new Label("Account Type:"), 0, 1);
            grid.add(accountTypeComboBox, 1, 1);
        
            dialog.getDialogPane().setContent(grid);
        
            // Convert the result to a pair of name and account type when the apply button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == applyButtonType) {
                    return new Pair<>(nameComboBox.getValue(), accountTypeComboBox.getValue());
                }
                return null;
            });
        
            Optional<Pair<String, String>> result = dialog.showAndWait();
        
            result.ifPresent(nameAccountType -> {
                String selectedName = nameAccountType.getKey();
                String selectedAccountType = nameAccountType.getValue();
                System.out.println("Selected Name: " + selectedName + ", Selected Account Type: " + selectedAccountType);
                // Here, handle the selected name and account type update. 
                // For example, you can update the role in your database or application context based on the selected values.
            });
        }
        
        
    @FXML
    void editAccountButton(ActionEvent event) {
>>>>>>> Stashed changes
    Pair<String, String> accountInfo = showEditAccountDialog();

    if (accountInfo != null) {
        String newUsername = accountInfo.getKey();
        String newPassword = accountInfo.getValue();

        // Use fileManipulation to write the new account information to the file
        boolean accountCreated = file.newAccount(newUsername, newPassword);

        if (accountCreated) {
            System.out.println("New account created successfully with username: " + newUsername);
<<<<<<< Updated upstream
            // Update the userDisplay Text to show the new username
            userDisplay.setText("Hello, " + newUsername + "!");

            // You can update your UI or model as needed here
=======
            // You can update your UI or model as needed here
            // Update the userDisplay Text to show the new username
            userDisplay.setText("Hello, " + newUsername + "!");
>>>>>>> Stashed changes
        } else {
            System.out.println("Account creation failed. Username might already exist.");
            // Handle the failure, possibly by alerting the user through the UI
        }
    } else {
        // Handle case where dialog was canceled or closed without input
        System.out.println("Account update dialog was canceled or closed without input.");
    }
}


<<<<<<< Updated upstream
=======
private Pair<String, String> showEditAccountDialog() {
    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Edit Account");
    dialog.setHeaderText("Enter your new username and password");

    // Set the button types.
    ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField username = new TextField();
    username.setPromptText("New Username");
    PasswordField password = new PasswordField();
    password.setPromptText("New Password");

    grid.add(new Label("New Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new Label("New Password:"), 0, 1);
    grid.add(password, 1, 1);

    dialog.getDialogPane().setContent(grid);

    // Convert the result to a username-password pair when the submit button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == submitButtonType) {
            return new Pair<>(username.getText(), password.getText());
        }
        return null;
    });

    Optional<Pair<String, String>> result = dialog.showAndWait();
    return result.orElse(null);
}
>>>>>>> Stashed changes


    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Read the entries from the file
        Map<LocalDate, List<String>> calendarEntries = readCalendarEntries();

        //List of activities for a given month
        Map<Integer, List<Calendar_Activity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
        calendar.getChildren().clear(); // Ensure the calendar is cleared before redrawing

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text dateText = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        dateText.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(dateText);

                        // Determine the date for the current cell
                        LocalDate cellDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);
                        // Check if there are entries for this date and add them to the calendar
                        if (calendarEntries.containsKey(cellDate)) {
                            List<String> dayEntries = calendarEntries.get(cellDate);
                            for (String entry : dayEntries) {
                                Text entryText = new Text(entry);
                                entryText.setWrappingWidth(rectangleWidth - 10); // Adjust text wrapping width
                                stackPane.getChildren().add(entryText);
                            }
                        }

                        List<Calendar_Activity> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<Calendar_Activity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:NAVY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<Calendar_Activity>> createCalendarMap(List<Calendar_Activity> calendarActivities) {
        Map<Integer, List<Calendar_Activity>> calendarActivityMap = new HashMap<>();

        for (Calendar_Activity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<Calendar_Activity> OldListByDate = calendarActivityMap.get(activityDate);

                List<Calendar_Activity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    private Map<Integer, List<Calendar_Activity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<Calendar_Activity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        
        

        return createCalendarMap(calendarActivities);
    }

    @FXML
    void editCalendar(ActionEvent event) {
    // Call the custom dialog to get crew member name and assignment date
    Pair<String, LocalDate> input = showCustomDialogWithNamesAndFlights();

    if (input != null) {
        String crewMemberName = input.getKey();
        LocalDate assignmentDate = input.getValue();
        
        // Here you can handle the inputs, for example, print them out
        System.out.println("Crew Member Name: " + crewMemberName + ", Assignment Date: " + assignmentDate);
        // Further processing can be done here, such as updating a data model or UI
        // Write to file
        try(FileWriter fw = new FileWriter("calendar_entries.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println("Crew Member Name: " + crewMemberName + ", Assignment Date: " + assignmentDate);
          
            // After successfully writing the new entry, immediately update the calendar view
            Platform.runLater(() -> {
            calendar.getChildren().clear(); // Clear the current view
            drawCalendar(); // Redraw the calendar with updated data
            });
        } catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }
    } else {
        // Handle case where dialog was canceled or no input was provided
        System.out.println("Dialog was canceled or closed without input.");
    }
}

    private Map<LocalDate, List<String>> readCalendarEntries() {
    Map<LocalDate, List<String>> entriesMap = new HashMap<>();
    try (Scanner scanner = new Scanner(new File("calendar_entries.txt"))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Assuming the line format is "Crew Member Name: [name] - [flightCode], Assignment Date: [date]"
            // You might need to adjust the parsing logic based on your actual file format
            String[] parts = line.split(", Assignment Date: ");
            //String infoPart = parts[0]; // "Crew Member Name: [name] - [flightCode]"
            //LocalDate date = LocalDate.parse(parts[1]);

            // entriesMap.computeIfAbsent(date, k -> new ArrayList<>()).add(infoPart);
            String nameAndFlight = parts[0].substring("Crew Member Name: ".length()); // Extracting "[name] - [flightCode]"
            LocalDate date = LocalDate.parse(parts[1]); // Parsing the date
            
            //use the name and flight code
            String formattedEntry = nameAndFlight; // using "[name] - [flightCode]"

            entriesMap.computeIfAbsent(date, k -> new ArrayList<>()).add(formattedEntry);
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return entriesMap;
}

    private Pair<String, LocalDate> showCustomDialogWithNamesAndFlights() {
        // Crew member names
        List<String> names = Arrays.asList(
            "Alex Johnson,28,Male",
            "Maria Lee,34,Female",
            "James Williams,45,Male",
            "Patricia Brown,26,Female",
            "John Davis,31,Male",
            "Linda Martinez,37,Female",
            "Robert Miller,52,Male",
            "Elizabeth Moore,24,Female",
            "Michael Taylor,43,Male",
            "Barbara Wilson,39,Female"
        ).stream().map(s -> s.split(",")[0]).collect(Collectors.toList());
    
        // Flight codes
        List<String> flights = Arrays.asList(
            "FL123,New York,5.5,2024-03-20,15:00",
            "FL456,Los Angeles,6,2024-03-21,16:30",
            "FL789,Chicago,2,2024-03-22,14:00",
            "FL101,Paris,8,2024-03-23,09:00",
            "FL102,Tokyo,12,2024-03-24,17:45",
            "FL103,Sydney,15,2024-03-25,19:00",
            "FL104,Dubai,7,2024-03-26,22:00",
            "FL105,Singapore,10,2024-03-27,13:00",
            "FL106,Berlin,9,2024-03-28,08:30",
            "FL107,London,7.5,2024-03-29,11:15"
        ).stream().map(s -> s.split(",")[0]).collect(Collectors.toList()); // Extracting only the flight codes
    
        // Create the custom dialog.
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Edit Calendar");
    
        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        // Create the UI components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
    
        ComboBox<String> nameComboBox = new ComboBox<>();
        nameComboBox.getItems().addAll(names);
    
        ComboBox<String> flightComboBox = new ComboBox<>();
        flightComboBox.getItems().addAll(flights);
    
        DatePicker datePicker = new DatePicker();

       // Time entry TextField
        TextField timeTextField = new TextField();
        timeTextField.setPromptText("HH:mm");

        grid.add(new Label("Crew Member Name:"), 0, 0);
        grid.add(nameComboBox, 1, 0);
        grid.add(new Label("Flight Code:"), 0, 1);
        grid.add(flightComboBox, 1, 1);
        grid.add(new Label("Assignment Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Time (HH:mm):"), 0, 3);
        grid.add(timeTextField, 1, 3);
    
        dialog.getDialogPane().setContent(grid);
    
        // Convert the result to a pair when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(nameComboBox.getValue() + " - " + flightComboBox.getValue(), datePicker.getValue());
            }
            return null;
        });
    
        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        return result.orElseGet(() -> {
            Random rand = new Random();
            String name = names.get(rand.nextInt(names.size()));
            String flight = flights.get(rand.nextInt(flights.size()));
            LocalDate date = LocalDate.now();
            return new Pair<>(name + " - " + flight, date);
        });

        //return result.get();
        
        /*int flag=1;
        while(flag==1){
            if (result.isPresent()) {
                flag = 0;
            }
        }
        return result.get();*/
    }

}