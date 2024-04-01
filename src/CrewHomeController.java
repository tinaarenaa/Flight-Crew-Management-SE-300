import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
//import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
//import javafx.scene.input.InputMethodEvent;
//import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Node;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CrewHomeController implements Initializable{

    private fileManipulation file = new fileManipulation();
    private crewFlightController crewFlightCont = new crewFlightController(); // call functions in this class to do everything with crew and flight management
    private String currentUsername;
    
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private FlowPane calendar;

    @FXML
    private ListView<String> crewInfoList;

    @FXML
    private ListView<String> crewList;

    @FXML
    private ListView<String> flightInfoList;

    @FXML
    private ListView<String> flightList;

    @FXML
    private Text month;

    @FXML
    private ListView<String> upComingFlights;

    @FXML
    private Text userDisplay;

    @FXML
    private Text year;

    @FXML
    void addPreferences(ActionEvent event) {
    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    LinkedList<String> crewNames = crewFlightCont.getCrewNames();
    LinkedList<String> flightCodes = crewFlightCont.getFlightNumbers(); // Assuming this method exists
    dialog.setTitle("Add Preferences");
    dialog.setHeaderText("Select your unavailable days and crew member");

    // Set the button types.
    ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    // Create ComboBoxes for selecting the crew member and flight.
    ComboBox<String> crewMemberComboBox = new ComboBox<>();
    crewMemberComboBox.getItems().addAll(crewNames);
    ComboBox<String> flightComboBox = new ComboBox<>(); // New ComboBox for flights
    flightComboBox.getItems().addAll(flightCodes);

    // Create a layout and add the components.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Name:"), 0, 0);
    grid.add(crewMemberComboBox, 1, 0);
    grid.add(new Label("Select Flight:"), 0, 1); // Updated label
    grid.add(flightComboBox, 1, 1); // Added the flight ComboBox

    dialog.getDialogPane().setContent(grid);

    // Request focus on the crew member ComboBox by default.
    Platform.runLater(crewMemberComboBox::requestFocus);

    // Convert the result when the save button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == saveButtonType) {
            return new Pair<>(crewMemberComboBox.getValue(), flightComboBox.getValue());
        }
        return null;
    });

    // Show the dialog and capture the result.
    Optional<Pair<String, String>> result = dialog.showAndWait();

    result.ifPresent(nameAndFlight -> {
        // Handle the result here. Associate the crew member with the selected flight.
        file.savePreferencesToFile(nameAndFlight.getKey(), nameAndFlight.getValue());
    });
}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        updateFlightAndCrewDisplays();
        crewList.getSelectionModel().selectedItemProperty().addListener((observable, oldCrewValue, newCrewValue) -> {
          if(newCrewValue != null) {
            showCrewInfo();
          }
        });
        
        flightList.getSelectionModel().selectedItemProperty().addListener((observable, oldFlightValue, newFlightValue) -> {
          if(newFlightValue != null) {
            showFlightInfo();
          }
        });

        

    }

    private void updateFlightAndCrewDisplays() {
        
        drawCalendar();
        
        crewList.getItems().clear();
        LinkedList<String> crewNames = crewFlightCont.getCrewNames();
        for(int i = 0; i < crewNames.size(); i++) {
          crewList.getItems().add(crewNames.get(i));
        } 
        flightList.getItems().clear();
        LinkedList<String> flightNums = crewFlightCont.getFlightNumbers();
        for(int i = 0; i < flightNums.size(); i++) {
          flightList.getItems().add(flightNums.get(i));
        }
        upComingFlights.getItems().clear();
        LinkedList<flightClass> flightClassList = crewFlightCont.getFlightClassList();
        for(int i = 0; i < flightClassList.size(); i++) {
          LinkedList<String> crewAssignments = crewFlightCont.getFlightCrewAssignments(flightClassList.get(i).getFlightNumber());
          String crewAssignmentsString = "";
          for(int j = 0; j < crewAssignments.size(); j++) {
            if(j == crewAssignments.size() - 1)  {
              crewAssignmentsString = crewAssignmentsString + crewAssignments.get(j);
            } else {
              crewAssignmentsString = crewAssignmentsString + crewAssignments.get(j) + ", ";
            }
          }
          upComingFlights.getItems().add("Flight Number: " + flightClassList.get(i).getFlightNumber() + "   |   Crew Assignments: " + crewAssignmentsString);
        }

        crewList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        flightList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    
  }
  

    private String showEditAccountDialog() {
        // Create the custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Account");
        dialog.setHeaderText("Enter your new password");
    
        // Set the button types
        ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
    
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
    
        PasswordField password1 = new PasswordField();
        password1.setPromptText("New Password");
        PasswordField password2 = new PasswordField();
        password2.setPromptText("Retype New Password");
    
        grid.add(new Label("New Password:"), 0, 0);
        grid.add(password1, 1, 0);
        grid.add(new Label("Retype New Password:"), 0, 1);
        grid.add(password2, 1, 1);
    
        dialog.getDialogPane().setContent(grid);
    
        // Convert the result to a username-password pair when the submit button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                if(password1.getText().equals(password2.getText())) {
                  return password1.getText();
                }
            }
            return null;
        });
    
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    @FXML
    void editAccountButton(ActionEvent event) {
    String newPassword = showEditAccountDialog();

    if (newPassword != null) {

        // Use fileManipulation to write the new account information to the file
        file.changePass(currentUsername, newPassword);
    }
}

   

@FXML
private void saveData() {
  crewFlightCont.saveAllData();
}

    public void transfer(String username) {
        userDisplay.setText("Hello, " + username + "!");
        currentUsername = username;
      }
      //go back on the calendar
      @FXML
      void backOneMonth(ActionEvent event) {
          dateFocus = dateFocus.minusMonths(1);
          calendar.getChildren().clear();
          drawCalendar();
      }
  
      //go forward on the calendar
      @FXML
      void forwardOneMonth(ActionEvent event) {
          dateFocus = dateFocus.plusMonths(1);
          calendar.getChildren().clear();
          drawCalendar();
      }

      private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Get linkedList of flight classes using crewFlightController
        LinkedList<flightClass> flights = crewFlightCont.getFlightClassList();
        
        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
        calendar.getChildren().clear(); // Ensure the calendar is cleared before redrawing

        for (int i = 0; i < 6; i++) { // Drawing the whole thing
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

                        int flightCount = 0;
                        LinkedList<flightClass> flightsOnDate = new LinkedList<>();
                        for(int k = 0; k < flights.size(); k++) {
                          int[] flightDate = parseFlightDate(flights.get(k));
                          if(flightDate[0] == dateFocus.getMonthValue() && flightDate[1] == currentDate && flightDate[2] == dateFocus.getYear()) {
                            flightCount++;
                            flightsOnDate.add(flights.get(k));
                          }
                        }
                        
                        Text entryText = new Text(flightCount + " Flights");
                        entryText.setWrappingWidth(rectangleWidth - 10);
                        stackPane.getChildren().add(entryText);
                        if(flightsOnDate != null){
                            createCalendarActivity(flightsOnDate, entryText);
                        }
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setStrokeWidth(2);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
    
    private void createCalendarActivity(LinkedList<flightClass> flightsOnDate, Text clickText) {
        clickText.setOnMouseClicked(mouseEvent -> {
          for(int i = 0; i < flightsOnDate.size(); i++) {
            System.out.print(flightsOnDate.get(i).getFlightNumber() + ": ");
            LinkedList<String> assignments = crewFlightCont.getFlightCrewAssignments(flightsOnDate.get(i).getFlightNumber());
            if(assignments.size() == 0) {
              System.out.print("\n");
            }
            for(int j = 0; j < assignments.size(); j++) {
              if(j  == assignments.size() - 1) {
                System.out.print(assignments.get(j) + "\n");
              } else {
                System.out.print(assignments.get(j) + ", ");
              }
            }
          }

        });
    }

    private int[] parseFlightDate(flightClass flight) {
        String date = flight.getDate();
        String[] parsedDate = date.split("/");
        for(int i = 0; i < parsedDate.length; i++) {
          parsedDate[i] = parsedDate[i].trim();
        }
        int[] parseIntDate = new int[]{Integer.parseInt(parsedDate[0]), Integer.parseInt(parsedDate[1]), Integer.parseInt(parsedDate[2])};
        return parseIntDate;
      }

    private void showCrewInfo() {
    ObservableList<String> nameSelection = crewList.getSelectionModel().getSelectedItems();
    String name = "";
    if(nameSelection != null) {
      for(String m: nameSelection) {
        name += m;
      }
      crewClass crewSelection = crewFlightCont.getCrewClass(name);
      crewInfoList.getItems().clear();
      crewInfoList.getItems().addAll("Name: " + crewSelection.getName(), "Username: " + crewSelection.getUsername(), "Home Airport: " + crewSelection.getHomeAirport());
    }

  }

  private void showFlightInfo() {
    ObservableList<String> flightNumSelection = flightList.getSelectionModel().getSelectedItems();
    String flightNum = "";
    if(flightNumSelection != null) {
      for(String m: flightNumSelection) {
        flightNum += m;
      }
      flightClass flightSelection = crewFlightCont.getFlightClass(flightNum);
      flightInfoList.getItems().clear();
      flightInfoList.getItems().addAll("Flight Number: " + flightSelection.getFlightNumber(), "Depart Time: " + flightSelection.getDepartTime(), "Arrival Time: " + flightSelection.getArriveTime(), "Initial Airport: " + flightSelection.getInitAirport(), "Destination Airport: " + flightSelection.getDestAirport(), "Date: " + flightSelection.getDate());
    }

  }
}
