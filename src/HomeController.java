import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.collections.ObservableList;
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

public class HomeController implements Initializable {

    private fileManipulation file = new fileManipulation();
    private crewFlightController crewFlightCont = new crewFlightController(); // call functions in this class to do everything with crew and flight management
    private String currentUsername;
    
    //**************************************FXML elements*********************************************

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
    private ListView<String> crewList;

    @FXML
    private ListView<String> flightList;

    @FXML
    private ListView<String> crewInfoList;

    @FXML
    private ListView<String> flightInfoList;

    @FXML
    private ListView<String> upComingFlights;

    ZonedDateTime dateFocus;
    ZonedDateTime today;

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

    //method to implement button to edic account specifications
    @FXML
    void editAccountSpecifications(ActionEvent event) {

    	LinkedList<String> names = file.getUsernames();

    	// custom dialog
    	Dialog<LinkedList<String>> dialog = new Dialog<>();
    	dialog.setTitle("Edit Account Specifications");
    	dialog.setHeaderText("Select the new account type and name");

    	// Set the button types
    	ButtonType applyButtonType = new ButtonType("Apply", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);

    	// Name ComboBox
    	ComboBox<String> usernameComboBox = new ComboBox<>();
    	usernameComboBox.getItems().addAll(names);
    	usernameComboBox.getSelectionModel().selectFirst(); // Default to first item

    	// Account Type ComboBox
    	ComboBox<String> accountTypeComboBox = new ComboBox<>();
    	accountTypeComboBox.getItems().addAll("admin", "crew");
    	accountTypeComboBox.getSelectionModel().selectFirst(); // Default to first item

    	grid.add(new Label("Username:"), 0, 0);
    	grid.add(usernameComboBox, 1, 0);
    	grid.add(new Label("Account Type:"), 0, 1);
    	grid.add(accountTypeComboBox, 1, 1);

    	dialog.getDialogPane().setContent(grid);

    	// Convert the result to a pair of name and account type when the apply button is clicked.
    	dialog.setResultConverter(dialogButton -> {
        if (dialogButton == applyButtonType) {
            if(accountTypeComboBox.getValue().equals("admin")) {
              file.changeRole(usernameComboBox.getValue(), 1);
            } else {
              file.changeRole(usernameComboBox.getValue(), 0);
            }
            return new LinkedList<String>(Arrays.asList(usernameComboBox.getValue(), accountTypeComboBox.getValue()));
        }
        return null;
    	});

    	Optional<LinkedList<String>> result = dialog.showAndWait();

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

  // ********************************* CALENDAR ******************************************


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

    @FXML 
    void assignCrewMember(ActionEvent event) {
    // Call the custom dialog to get crew member name and assignment date
    LinkedList<String> input = assignCrewFlightDialog();

    if (input != null) {
      crewFlightCont.assignCrewToFlight(input.get(0), input.get(1));
      updateFlightAndCrewDisplays();
    } 
  }




    // This will be how to assign crews to flights
    private LinkedList<String> assignCrewFlightDialog() {
        LinkedList<String> flightNums = crewFlightCont.getFlightNumbers();
        LinkedList<String> crewNames = crewFlightCont.getCrewNames();


        // Create the custom dialog.
        Dialog<LinkedList<String>> dialog = new Dialog<>();
        dialog.setTitle("Assign Crew Member to Flight");
    
        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        // Create the UI components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
    
        ComboBox<String> nameComboBox = new ComboBox<>();
        nameComboBox.getItems().addAll(crewNames);
    
        ComboBox<String> flightComboBox = new ComboBox<>();
        flightComboBox.getItems().addAll(flightNums);

        grid.add(new Label("Crew Member Name:"), 0, 0);
        grid.add(nameComboBox, 1, 0);
        grid.add(new Label("Flight Code:"), 0, 1);
        grid.add(flightComboBox, 1, 1);
    
        dialog.getDialogPane().setContent(grid);
    
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
              if(nameComboBox.getValue() != null && flightComboBox != null) {
                return new LinkedList<String>(Arrays.asList(nameComboBox.getValue(), flightComboBox.getValue()));
              }
            }
            return null;
        });
    
        Optional<LinkedList<String>> result = dialog.showAndWait();
        return result.orElse(null);
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


  // ************************************ CREW AND FLIGHT TABS **********************************************8


  @FXML
  private void addCrewMemberPressed() {
    LinkedList<String> rawCrewData = addCrewDialog();
    if(rawCrewData != null) {
      crewFlightCont.addCrew(rawCrewData.get(0), rawCrewData.get(1), rawCrewData.get(2));
      updateFlightAndCrewDisplays();
    }
  }

  private LinkedList<String> addCrewDialog() {
    Dialog<LinkedList<String>> crewDialog = new Dialog<>();
    crewDialog.setTitle("Add Crew Member");

    crewDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    TextField nameTextField = new TextField();
    nameTextField.setPromptText("Enter Name");
    
    TextField usernameTextField = new TextField();
    usernameTextField.setPromptText("Enter Username");

    TextField homeAirportTextField = new TextField();
    homeAirportTextField.setPromptText("Enter Airport");

    grid.add(new Label("Crew Member Name:"), 0, 0);
    grid.add(nameTextField, 1, 0);
    grid.add(new Label("Crew Member Username:"), 0, 1);
    grid.add(usernameTextField, 1, 1);
    grid.add(new Label("Crew Member Home Airport:"), 0, 2);
    grid.add(homeAirportTextField, 1, 2);

    crewDialog.getDialogPane().setContent(grid);
    
    
    crewDialog.setResultConverter(dialogButton -> {
      if(dialogButton == ButtonType.OK) {
        if(nameTextField.getText() != null && usernameTextField.getText() != null && homeAirportTextField.getText() != null) {
          return new LinkedList<String>(Arrays.asList(nameTextField.getText(), usernameTextField.getText(), homeAirportTextField.getText()));
        }
      } else if(dialogButton == ButtonType.CANCEL) {
        return null;
      }

      return null;

    });

    Optional<LinkedList<String>> result = crewDialog.showAndWait();
    return result.orElse(null);
    
  }

  @FXML
  private void removeCrewMemberPressed() {
    ObservableList<String> nameSelection = crewList.getSelectionModel().getSelectedItems();
    String name = "";
    if(nameSelection != null) {
      for(String m: nameSelection) {
        name += m;
      }
      crewFlightCont.removeCrew(name);
    }
    updateFlightAndCrewDisplays();
  }


  @FXML
  private void editCrewMemberPressed() {
    ObservableList<String> nameSelection = crewList.getSelectionModel().getSelectedItems();
    String name = "";
    if(nameSelection != null) {
      for(String m: nameSelection) {
        name += m;
      }
      LinkedList<String> rawCrewMemberData = crewFlightCont.getCrewClass(name).getRawCrewData();

      LinkedList<String> newCrewData = editCrewDialog(rawCrewMemberData);
      crewFlightCont.editCrew(name, newCrewData.get(1), newCrewData.get(2));
      updateFlightAndCrewDisplays();
    }
  }

  private LinkedList<String> editCrewDialog(LinkedList<String> rawCrewData) {
    Dialog<LinkedList<String>> crewDialog = new Dialog<>();

    crewDialog.setTitle("Edit Crew Member: " + rawCrewData.get(0));

    crewDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    
    TextField usernameTextField = new TextField();
    usernameTextField.setPromptText("Enter Username");
    usernameTextField.setText(rawCrewData.get(1));

    TextField homeAirportTextField = new TextField();
    homeAirportTextField.setPromptText("Enter Airport");
    homeAirportTextField.setText(rawCrewData.get(2));

    grid.add(new Label("Crew Member Username:"), 0, 0);
    grid.add(usernameTextField, 1, 0);
    grid.add(new Label("Crew Member Home Airport:"), 0, 1);
    grid.add(homeAirportTextField, 1, 1);

    crewDialog.getDialogPane().setContent(grid);
    
    
    crewDialog.setResultConverter(dialogButton -> {
      if(dialogButton == ButtonType.OK) {
        if(usernameTextField.getText() != null && homeAirportTextField.getText() != null) {
          return new LinkedList<String>(Arrays.asList(rawCrewData.get(0), usernameTextField.getText(), homeAirportTextField.getText()));
        }
      } else if(dialogButton == ButtonType.CANCEL) {
        return null;
      }

      return null;

    });

    Optional<LinkedList<String>> result = crewDialog.showAndWait();
    return result.orElse(null);
    
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

// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- //

  @FXML
  private void addFlightPressed() {
    LinkedList<String> rawFlightData = addFlightDialog();
    if(rawFlightData != null) {
      crewFlightCont.addFlight(rawFlightData.get(0), Integer.parseInt(rawFlightData.get(1)), Integer.parseInt(rawFlightData.get(2)), rawFlightData.get(3), rawFlightData.get(4), rawFlightData.get(5));
      updateFlightAndCrewDisplays();
    }
  }

  private LinkedList<String> addFlightDialog() {
    Dialog<LinkedList<String>> flightDialog = new Dialog<>();
    flightDialog.setTitle("Add Flight");

    flightDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    TextField flightNumTextField = new TextField();
    flightNumTextField.setPromptText("Enter Flight Number");
    
    TextField departTimeTextField = new TextField();
    departTimeTextField.setPromptText("MM/DD/YYYY");
    
    TextField arriveTimeTextField = new TextField();
    arriveTimeTextField.setPromptText("MM/DD/YYYY");
    
    TextField initAirportTextField = new TextField();
    initAirportTextField.setPromptText("Enter Inital Airport");
    
    TextField destAirportTextField = new TextField();
    destAirportTextField.setPromptText("Enter Destination Airport");
    
    TextField dateTextField = new TextField();
    dateTextField.setPromptText("Enter Date");
    

    grid.add(new Label("Flight Number:"), 0, 0);
    grid.add(flightNumTextField, 1, 0);
    
    grid.add(new Label("Depart Time:"), 0, 1);
    grid.add(departTimeTextField, 1, 1);
    
    grid.add(new Label("Arrival Time:"), 0, 2);
    grid.add(arriveTimeTextField, 1, 2);
    
    grid.add(new Label("Initial Airport:"), 0, 3);
    grid.add(initAirportTextField, 1, 3);
    
    grid.add(new Label("Destination Airport:"), 0, 4);
    grid.add(destAirportTextField, 1, 4);
    
    grid.add(new Label("Date:"), 0, 5);
    grid.add(dateTextField, 1, 5);
   

    flightDialog.getDialogPane().setContent(grid);
    
    
    flightDialog.setResultConverter(dialogButton -> {
      if(dialogButton == ButtonType.OK) {
        if(flightNumTextField.getText() != null && departTimeTextField.getText() != null && arriveTimeTextField.getText() != null && initAirportTextField.getText() != null && destAirportTextField.getText() != null && dateTextField.getText() != null) {
          return new LinkedList<String>(Arrays.asList(flightNumTextField.getText(), departTimeTextField.getText(), arriveTimeTextField.getText(), initAirportTextField.getText(), destAirportTextField.getText(), dateTextField.getText()));
        }
      } else if(dialogButton == ButtonType.CANCEL) {
        return null;
      }

      return null;

    });

    Optional<LinkedList<String>> result = flightDialog.showAndWait();
    return result.orElse(null);
    
  }

  @FXML
  private void removeFlightPressed() {
    ObservableList<String> flightNumSelection = flightList.getSelectionModel().getSelectedItems();
    String flightNum = "";
    if(flightNumSelection != null) {
      for(String m: flightNumSelection) {
        flightNum += m;
      }
      crewFlightCont.removeFlight(flightNum);
    }
    updateFlightAndCrewDisplays();
  }


  @FXML
  private void editFlightPressed() {
    ObservableList<String> flightNumSelection = flightList.getSelectionModel().getSelectedItems();
    String flightNum = "";
    if(flightNumSelection != null) {
      for(String m: flightNumSelection) {
        flightNum += m;
      }
      LinkedList<String> rawFlightData = crewFlightCont.getFlightClass(flightNum).getRawFlightData();

      LinkedList<String> newFlightData = editFlightDialog(rawFlightData);
      crewFlightCont.editFlight(flightNum, Integer.parseInt(newFlightData.get(1)), Integer.parseInt(newFlightData.get(2)), newFlightData.get(3), newFlightData.get(4), newFlightData.get(5));
      updateFlightAndCrewDisplays();
    }
  }

  private LinkedList<String> editFlightDialog(LinkedList<String> rawFlightData) {
    Dialog<LinkedList<String>> flightDialog = new Dialog<>();

    flightDialog.setTitle("Edit Flight: " + rawFlightData.get(0));

    flightDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    TextField departTimeTextField = new TextField();
    departTimeTextField.setPromptText("Enter Depart Time");
    departTimeTextField.setText(rawFlightData.get(1));
    
    TextField arriveTimeTextField = new TextField();
    arriveTimeTextField.setPromptText("Enter Arrival Time");
    arriveTimeTextField.setText(rawFlightData.get(2));
    
    TextField initAirportTextField = new TextField();
    initAirportTextField.setPromptText("Enter Inital Airport");
    initAirportTextField.setText(rawFlightData.get(3));
    
    TextField destAirportTextField = new TextField();
    destAirportTextField.setPromptText("Enter Destination Airport");
    destAirportTextField.setText(rawFlightData.get(4));
    
    TextField dateTextField = new TextField();
    dateTextField.setPromptText("Enter Date");
    dateTextField.setText(rawFlightData.get(5));
  
    
    grid.add(new Label("Depart Time:"), 0, 0);
    grid.add(departTimeTextField, 1, 0);

    grid.add(new Label("Arrival Time:"), 0, 1);
    grid.add(arriveTimeTextField, 1, 1);
    
    grid.add(new Label("Initial Airport:"), 0, 2);
    grid.add(initAirportTextField, 1, 2);
    
    grid.add(new Label("Destination Airport:"), 0, 3);
    grid.add(destAirportTextField, 1, 3);
    
    grid.add(new Label("Date:"), 0, 4);
    grid.add(dateTextField, 1, 4);
    
    
    flightDialog.getDialogPane().setContent(grid);
    
    
    flightDialog.setResultConverter(dialogButton -> {
      if(dialogButton == ButtonType.OK) {
        if(departTimeTextField.getText() != null && arriveTimeTextField.getText() != null && initAirportTextField.getText() != null && destAirportTextField.getText() != null && dateTextField.getText() != null) {
          return new LinkedList<String>(Arrays.asList(rawFlightData.get(0), departTimeTextField.getText(), arriveTimeTextField.getText(), initAirportTextField.getText(), destAirportTextField.getText(), dateTextField.getText()));
        }
      } else if(dialogButton == ButtonType.CANCEL) {
        return null;
      }

      return null;

    });

    Optional<LinkedList<String>> result = flightDialog.showAndWait();
    return result.orElse(null);
    
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
  
  @FXML
  private void saveData() {
    crewFlightCont.saveAllData();
  }

}
