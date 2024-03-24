import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
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
        calendarActivityBox.setStyle("-fx-background-color:LIGHTBLUE");
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

    }





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
    departTimeTextField.setPromptText("Enter Depart Time");
    
    TextField arriveTimeTextField = new TextField();
    arriveTimeTextField.setPromptText("Enter Arrival Time");
    
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

        crewList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        flightList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    
  }
  
  @FXML
  private void saveData() {
    crewFlightCont.saveAllData();
  }

}
