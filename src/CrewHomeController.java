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

public class CrewHomeController implements Initializable{

    private fileManipulation file = new fileManipulation();
    private crewFlightController crewFlightCont = new crewFlightController(); // call functions in this class to do everything with crew and flight management
    private String currentUsername;
    int user_daily;
    int user_monthly;
    int user_weekly;
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private FlowPane calendar;

    @FXML
    private HBox days;
    
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
    private Button monthly_view;

    @FXML
    private Button weekly_view;

    @FXML
    private Button backward_month;

    @FXML
    private Button forward_month;

    @FXML
    private HBox title;


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
      loadEventsForDay(dateFocus.toLocalDate(), dayEvents);
  }
  
  private void loadEventsForDay(LocalDate date, ListView<String> dayEvents) {
    String formattedDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
  
    // Get all flights and then filter for the selected date
    LinkedList<flightClass> allFlights = crewFlightCont.getFlightClassList();
    LinkedList<flightClass> flightsOnDate = allFlights.stream()
            .filter(flight -> flight.getDate().equals(formattedDate))
            .collect(Collectors.toCollection(LinkedList::new));
  
    // Add each flight to the ListView
    for (flightClass flight : flightsOnDate) {
        String flightDetails = "Flight Number: " + flight.getFlightNumber() +
                ", Depart: " + flight.getDepartTime() +
                ", Arrive: " + flight.getArriveTime() +
                ", From: " + flight.getInitAirport() +
                ", To: " + flight.getDestAirport();
        dayEvents.getItems().add(flightDetails);
    }
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
        loadFlightsForDay(date, listView); 
        
        dayColumn.getChildren().addAll(lblDayOfWeek, listView);
        HBox.setHgrow(dayColumn, Priority.ALWAYS); // Make day columns grow equally
        daysOfWeek.getChildren().add(dayColumn);
    }
  
    // Make daysOfWeek fill the width of the calendar
    daysOfWeek.prefWidthProperty().bind(calendar.widthProperty());
  
    // Add the days of the week to the calendar view
    calendar.getChildren().add(daysOfWeek);
  
  }
  
  private void loadFlightsForDay(LocalDate date, ListView<String> dayEvents) {
    String formattedDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LinkedList<flightClass> allFlights = crewFlightCont.getFlightClassList();
    LinkedList<flightClass> flightsOnDate = allFlights.stream()
            .filter(flight -> flight.getDate().equals(formattedDate))
            .collect(Collectors.toCollection(LinkedList::new));
  
    for (flightClass flight : flightsOnDate) {
        // Using existing methods to gather flight details
        String flightDetails = String.format("Flight: %s",
                flight.getFlightNumber());
  
        // Add the detailed string to the ListView for the day
        dayEvents.getItems().add(flightDetails);
    }
  }
  
  
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
        
      if(user_monthly==1){
        drawCalendar();
      }
      if(user_weekly==1){
        drawWeeklyCalendar();
      }
      if(user_daily==1){
        drawDailyCalendar();
      }
        
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

        // Clear any existing content from the title HBox
        title.getChildren().clear();
        title.getChildren().add(backward_month);
        title.getChildren().add(year);
        title.getChildren().add(month);
        title.getChildren().add(forward_month);
        title.setAlignment(Pos.CENTER);
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
