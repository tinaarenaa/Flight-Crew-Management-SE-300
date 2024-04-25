import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.layout.Priority;



public class CrewHomeController implements Initializable {

    private String currentUsername;
    int user_daily;
    int user_monthly;
    int user_weekly;
    private clientComm client;
    
    
    //**************************************FXML elements*********************************************

    @FXML
    private HBox title;

    @FXML
    private Button backward_month;

    @FXML
    private Button forward_month;

    @FXML
    private HBox days;

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
    private ListView<String> upComingFlights;

    @FXML
    private Button weekly_view;

    @FXML
    private Button monthly_view;
    
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void transfer(String username, clientComm passedClient) {
      userDisplay.setText("Hello, " + username + "!");
      currentUsername = username;
      client = passedClient;
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        updateFlightAndCrewDisplays();


    }

    @FXML
    void change_to_monthly_view(ActionEvent event) {
      backward_month.setVisible(true);
      forward_month.setVisible(true);
      
      user_daily=0;
      user_monthly=1;
      user_weekly=0;

      drawCalendar();
    }

    @FXML
    void swich_to_daily_view(ActionEvent event) {
      drawDailyCalendar();

      user_daily=1;
      user_monthly=0;
      user_weekly=0;
    }


    // Method to navigate to the previous day
    @FXML
    void backOneDay(ActionEvent event) {
        dateFocus = dateFocus.minusDays(1);
        drawDailyCalendar();
    }

// Method to navigate to the next day
    @FXML
    void forwardOneDay(ActionEvent event) {
        dateFocus = dateFocus.plusDays(1);
        drawDailyCalendar();
    }

    // Method to draw the daily calendar view
    private void drawDailyCalendar() {
    // Clear the existing view
    calendar.getChildren().clear();
    days.setVisible(false); // Hide days of the week if they are visible
    backward_month.setVisible(false); // Hide month navigation if visible
    forward_month.setVisible(false); // Hide month navigation if visible
    
    // Create navigation buttons for daily view
    Button btnPreviousDay = new Button("< Previous Day");
    btnPreviousDay.setOnAction(this::backOneDay);
    
    Button btnNextDay = new Button("Next Day >");
    btnNextDay.setOnAction(this::forwardOneDay);

    // Create the day title
    Label dayTitle = new Label(dateFocus.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    dayTitle.setFont(new Font(16));

    // Add the buttons and title to the title HBox
    title.getChildren().clear();
    title.getChildren().addAll(btnPreviousDay, dayTitle, btnNextDay);
    title.setAlignment(Pos.CENTER);

    // Create a ListView for the day's events or flights
    ListView<String> dayEvents = new ListView<>();
    dayEvents.setPrefSize(600, 600); // Set the preferred size for the ListView

    // Add the ListView to a VBox with padding
    VBox dayView = new VBox(dayEvents);
    dayView.setPadding(new Insets(10));

    // Add the VBox to the calendar
    calendar.getChildren().add(dayView);
    
    // Load the events or flights into the ListView    
    String resp = client.sendCommand("GET_NAMES_FLIGHTS_ON_DATE:" + Integer.toString(dateFocus.getMonthValue()) + ":" + Integer.toString(dateFocus.getDayOfMonth()) + ":" + Integer.toString(dateFocus.getYear()));
    LinkedList<String> flightsOnDate = parseRecievedData(resp);
    
    for(int i = 0; i < flightsOnDate.size(); i++) {
    	String resp1 = client.sendCommand("GET_RAW_FLIGHT_DATA:" + flightsOnDate.get(i));
      	LinkedList<String> rawFlightData = parseRecievedData(resp1);
      	if(rawFlightData.size() == 6) {
      	String flightDetails = "Flight Number: " + rawFlightData.get(0) +
              ", Depart: " + rawFlightData.get(1) +
              ", Arrive: " + rawFlightData.get(2) +
              ", From: " + rawFlightData.get(3) +
              ", To: " + rawFlightData.get(4);
      	dayEvents.getItems().add(flightDetails);
    }}
    
}


    @FXML
    void change_to_weekly_view(ActionEvent event) {
      user_daily=0;
      user_monthly=0;
      user_weekly=1;
      drawWeeklyCalendar();
}

    
// Weekly view navigation
@FXML
void backOneWeek(ActionEvent event) {
    dateFocus = dateFocus.minusWeeks(1);
    drawWeeklyCalendar();
}

@FXML
void forwardOneWeek(ActionEvent event) {
    dateFocus = dateFocus.plusWeeks(1);
    drawWeeklyCalendar();
}

private void drawWeeklyCalendar() {
  // Clear the existing view
  calendar.getChildren().clear();
  days.setVisible(false);
  backward_month.setVisible(false);
  forward_month.setVisible(false);
  //Create navigation buttons
  Button btnPreviousWeek = new Button("< Previous Week");
  btnPreviousWeek.setOnAction(this::backOneWeek);
  
  Button btnNextWeek = new Button("Next Week >");
  btnNextWeek.setOnAction(this::forwardOneWeek);

  // Create the week title
  LocalDate startOfWeek = dateFocus.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  LocalDate endOfWeek = startOfWeek.plusDays(6);
  Label weekTitle = new Label("Week of " + startOfWeek.getDayOfMonth() + " - " + endOfWeek.getDayOfMonth() + " " +
                              startOfWeek.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + 
                              startOfWeek.getYear());
  weekTitle.setFont(new Font(16));

  // Clear the title HBox and add new components
  title.getChildren().clear();
  title.getChildren().addAll(btnPreviousWeek, weekTitle, btnNextWeek);

  // Create an HBox for the days of the week with ListViews
  HBox daysOfWeek = new HBox();
  daysOfWeek.setFillHeight(true); // Make sure children fill the height
  daysOfWeek.setSpacing(0); // Set spacing to 0 if you want the days to be right next to each other

  // Stretch the day columns to fill the space
  for (int i = 0; i < 7; i++) {
      LocalDate date = startOfWeek.plusDays(i);
      VBox dayColumn = new VBox();
      dayColumn.setAlignment(Pos.TOP_CENTER);
      dayColumn.setFillWidth(true); // Make VBox fill the width

      Label lblDayOfWeek = new Label(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
      lblDayOfWeek.setFont(Font.font(16));

      ListView<String> listView = new ListView<>();
      listView.setPrefHeight(600); // Adjust as necessary
      
      String resp = client.sendCommand("GET_NAMES_FLIGHTS_ON_DATE:" + Integer.toString(date.getMonthValue()) + ":" + Integer.toString(date.getDayOfMonth()) + ":" + Integer.toString(date.getYear()));
    LinkedList<String> flightsOnDate = parseRecievedData(resp);
    if(!flightsOnDate.get(0).equals("")) {
    for(int j = 0; j < flightsOnDate.size(); j++) {
      	String flightDetails = ("Flight: " + flightsOnDate.get(j));
      	listView.getItems().add(flightDetails);
    }
      }
      dayColumn.getChildren().addAll(lblDayOfWeek, listView);
      HBox.setHgrow(dayColumn, Priority.ALWAYS); // Make day columns grow equally
      daysOfWeek.getChildren().add(dayColumn);
  }

  // Make daysOfWeek fill the width of the calendar
  daysOfWeek.prefWidthProperty().bind(calendar.widthProperty());

  // Add the days of the week to the calendar view
  calendar.getChildren().add(daysOfWeek);

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
      String resp = client.sendCommand("REQ_USER_LIST");
      LinkedList<String> names = parseRecievedData(resp);

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
              client.sendCommand("CHANGE_ROLE:" + usernameComboBox.getValue() + ":ADMIN");
            } else {
              client.sendCommand("CHANGE_ROLE:" + usernameComboBox.getValue() + ":CREW");
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
        client.sendCommand("CHANGE_PASS:" + currentUsername + ":" + newPassword);
    }
}

  // ********************************* CALENDAR ******************************************


    private void drawCalendar(){

        // Clear any existing content from the title HBox
        title.getChildren().clear();
        title.getChildren().add(backward_month);
        title.getChildren().add(year);
        title.getChildren().add(month);
        title.getChildren().add(forward_month);
        
        // Center the title HBox content
        title.setAlignment(Pos.CENTER);
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
       
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

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

                        String resp = client.sendCommand("GET_NAMES_FLIGHTS_ON_DATE:" + Integer.toString(dateFocus.getMonthValue()) + ":" + Integer.toString(currentDate) + ":" + Integer.toString(dateFocus.getYear()));
                        LinkedList<String> flightsOnDate = parseRecievedData(resp);
                        int flightCount = 0;
                        if(!flightsOnDate.get(0).equals("")) {
                          flightCount = flightsOnDate.size();
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
    
    private void createCalendarActivity(LinkedList<String> flightsOnDate, Text clickText) { 
        clickText.setOnMouseClicked(mouseEvent -> {
          for(int i = 0; i < flightsOnDate.size(); i++) {
            System.out.print(flightsOnDate.get(i) + ": ");
            String resp = client.sendCommand("GET_FLIGHT_CREW_ASSIGNMENTS:" + flightsOnDate.get(i));
            LinkedList<String> assignments = parseRecievedData(resp);
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
      String name = input.get(0);
      String flightCode = input.get(1);
      
      String resp = client.sendCommand("ASSIGN_CREW_TO_FLIGHT:" + input.get(0) + ":" + input.get(1));
      updateFlightAndCrewDisplays();
      
  }

}

@FXML
    void addPreferences(ActionEvent event) {
    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    String resp = client.sendCommand("GET_NAMES_FLIGHTS");
    LinkedList<String> flightCodes = parseRecievedData(resp);
    resp = client.sendCommand("GET_NAMES_CREW");
    LinkedList<String> crewNames = parseRecievedData(resp);

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
        //file.savePreferencesToFile(nameAndFlight.getKey(), nameAndFlight.getValue());
    });
}


    // This will be how to assign crews to flights
    private LinkedList<String> assignCrewFlightDialog() {
        String resp = client.sendCommand("GET_NAMES_FLIGHTS");
        LinkedList<String> flightNums = parseRecievedData(resp);
        resp = client.sendCommand("GET_NAMES_CREW");
        LinkedList<String> crewNames = parseRecievedData(resp);
        
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


  // ************************************ CREW AND FLIGHT TABS **********************************************8


  @FXML
  private void addCrewMemberPressed() {
    LinkedList<String> rawCrewData = addCrewDialog();
    if(rawCrewData != null) {
      client.sendCommand("ADD_CREW:" + rawCrewData.get(0) + ":" + rawCrewData.get(1) + ":" + rawCrewData.get(2));
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

 

// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- //

  @FXML
  private void addFlightPressed() {
    LinkedList<String> rawFlightData = addFlightDialog();
    if(rawFlightData != null) {
      client.sendCommand("ADD_FLIGHT:" + rawFlightData.get(0) + ":" + rawFlightData.get(1) + ":" + rawFlightData.get(2) + ":" + rawFlightData.get(3) + ":" + rawFlightData.get(4) + ":" + rawFlightData.get(5));
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
    departTimeTextField.setPromptText("XX:XX");
    
    TextField arriveTimeTextField = new TextField();
    arriveTimeTextField.setPromptText("XX:XX");
    
    TextField initAirportTextField = new TextField();
    initAirportTextField.setPromptText("Enter Inital Airport");
    
    TextField destAirportTextField = new TextField();
    destAirportTextField.setPromptText("Enter Destination Airport");
    
    TextField dateTextField = new TextField();
    dateTextField.setPromptText("MM/DD/YYYY");
    

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



  

  private void updateFlightAndCrewDisplays() {
  
  		client.sendCommand("REFRESH");
        
        if(user_monthly==1){
          drawCalendar();
        }
        if(user_weekly==1){
          drawWeeklyCalendar();
        }
        if(user_daily==1){
          drawDailyCalendar();
        }
        
        
 
        String resp = client.sendCommand("GET_NAMES_CREW");
        LinkedList<String> crewNames = parseRecievedData(resp);



        resp = client.sendCommand("GET_NAMES_FLIGHTS");
        LinkedList<String> flightNums = parseRecievedData(resp);

        upComingFlights.getItems().clear();
		
        for(int i = 0; i < flightNums.size(); i++) {
          resp = client.sendCommand("GET_FLIGHT_CREW_ASSIGNMENTS:" + flightNums.get(i));
          LinkedList<String> crewAssignments = parseRecievedData(resp);

          String crewAssignmentsString = "";
          for(int j = 0; j < crewAssignments.size(); j++) {
            if(j == crewAssignments.size() - 1)  {
              crewAssignmentsString = crewAssignmentsString + crewAssignments.get(j);
            } else {
              crewAssignmentsString = crewAssignmentsString + crewAssignments.get(j) + ", ";
            }
          }
          upComingFlights.getItems().add("Flight Number: " + flightNums.get(i) + "   |   Crew Assignments: " + crewAssignmentsString);

        }


    
  }
  
  @FXML
  private void saveData() {
    updateFlightAndCrewDisplays();
  }

  private LinkedList<String> parseRecievedData(String data) {
    if(data != null) {
      String[] parsedLine = data.split(":");
      LinkedList<String> dataList = new LinkedList<>();
      for(int i = 0; i < parsedLine.length; i++) {
        dataList.add(parsedLine[i].trim());
      }
      return dataList;
    } else {
      return new LinkedList<String>();
    }

  }

}
