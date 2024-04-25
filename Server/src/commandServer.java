import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class commandServer {
  private ServerSocket serverSocket;

  public void start(int port) {
    try {
      serverSocket = new ServerSocket(port);
        while (true) {
          new ClientHandler(serverSocket.accept()).start();
        }
    } catch (IOException e) {
            e.printStackTrace();
    }
  }

  public void stop() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private fileManipulation file = new fileManipulation();
    private crewFlightController crewFlightCont = new crewFlightController();
    private String userName = "null";

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
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
        

    // ****** LIST OF CLIENT -> SERVER COMMANDS *******
    // LOGIN {USERNAME, PASSWORD} -> RESPONSE: {SUCCESS, FAIL}
    // REQ_USER -> RESPONSE: {USERNAME}
    // NEW_ACCT {USERNAME, PASSWORD} -> RESPONSE: {SUCCESS, FAIL}
    // REQ_USER_LIST -> RESPONSE: {LIST OF USERNAMES}
    // REQ_USER_TYPE {USERNAME} -> RESPONSE: {USER TYPE}
    // CHANGE_ROLE {USERNAME, ROLE} -> RESPONSE: {SUCCESS, FAIL} 
    // CHANGE_PASS {USERNAME, PASSWORD} -> RESPONSE: {SUCCESS, FAIL}
    // **************************************************
    // GET_NUM_FLIGHTS {} -> RESPONSE: {LIST OF FLIGHT NUMS}
    // GET_NAMES_CREW {} -> RESPONSE: {LIST OF CREW NAMES}
    // GET_NUM_FLIGHTS_ON_DATE {MONTH, DAY, YEAR} -> RESPONSE: {NUMBER OF FLIGHTS}
    // GET_NAME_FLIGHTS_ON_DATE {MONTH, DAY, YEAR} -> RESPONSE: {LIST OF FLIGHT NUMS}
    // GET_FLIGHT_CREW_ASSIGNMENTS {FLIGHT NAME} -> RESPONSE: {LIST OF NAMES}
    // GET_CREW_FLIGHT_ASSIGNMENTS {CREW NAME} -> RESPONSE: {LIST OF FLIGHT NUMS}
    // ASSIGN_CREW_TO_FLIGHT {CREW NAME, FLIGHT NAME} -> RESPONSE: {SUCCESS, FAIL}
    // GET_FLIGHT_DATE {FLIGHT NUMBER} -> RESPONSE: {FLIGHT DATE}
    // ADD_CREW {}
    // ADD_FLIGHT {}
    // REMOVE_CREW {}
    // REMOVE_FLIGHT {}
    // GET_RAW_CREW_DATA {CREW NAME}
    // GET_RAW_FLIGHT_DATA {FLIGHT NUMBER}
    // EDIT_CREW {}
    // EDIT_FLIGHT {}
    //
    // COMMAND STRUCTURE FROM CLIENT:
    // COMMAND:PARAM1:PARAM2:PARAM3
   
  public void run() {
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      String inputLine = "";
      System.out.println("Client Connected.");
      while (!inputLine.equals("EXIT")) {
        inputLine = in.readLine();
        String[] parsedLine = inputLine.split(":");
        if(parsedLine.length > 0) {
          boolean val = false;
          String valStringList = "";
          for(int i = 0; i < parsedLine.length; i++) {
            parsedLine[i] = parsedLine[i].trim();
          }
          switch (parsedLine[0]) {

              case "LOGIN":
                System.out.println("Login Command Recieved");
                if(parsedLine.length == 3) {
                  val = file.testPass(parsedLine[1], parsedLine[2]);
                  if(val == true) {
                    out.println("SUCCESS");
                    userName = parsedLine[1];
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "REQ_USER":
                System.out.println("Request User Command Recieved");
                out.println(userName);
                break;

              case "NEW_ACCT":
                System.out.println("New Account Command Recieved");
                if(parsedLine.length == 3) {
                  val = file.newAccount(parsedLine[1], parsedLine[2]);
                  if(val == true) {
                    out.println("SUCCESS");
                    userName = parsedLine[1];
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "REQ_USER_LIST":
                System.out.println("Request Username List Recieved");
                LinkedList<String> userList = file.getUsernames();
                valStringList = "";
                for(int i = 0; i < userList.size(); i++) {
                  if(i == userList.size() - 1) {
                    valStringList = valStringList + userList.get(i);
                  } else {
                    valStringList = valStringList + userList.get(i) + ":";
                  }
                }
                out.println(valStringList);
                break;

              case "REQ_USER_TYPE":
                System.out.println("Request User Type Recieved");
                if(parsedLine.length == 2) {
                  int valInt = file.getRole(parsedLine[1]);
                  if(valInt == 0) {
                    out.println("CREW");
                  } else if (valInt == 1) {
                    out.println("ADMIN");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "CHANGE_ROLE":
                System.out.println("Change Role Request Recieved");
                if(parsedLine.length == 3) {
                  val = false;
                  if(parsedLine[2].equals("CREW")) {
                    val = file.changeRole(parsedLine[1], 0);
                  } else if(parsedLine[2].equals("ADMIN")) {
                    val = file.changeRole(parsedLine[1], 1);
                  }
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "CHANGE_PASS":
                System.out.println("Change Password Request Recieved");
                if(parsedLine.length == 3) {
                  val = file.changePass(parsedLine[1], parsedLine[2]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "GET_NAMES_FLIGHTS":
                System.out.println("Request Flight Names Recieved");
                LinkedList<String> flightsList = crewFlightCont.getFlightNumbers();
                valStringList = "";
                for(int i = 0; i < flightsList.size(); i++) {
                  if(i == flightsList.size() - 1) {
                    valStringList = valStringList + flightsList.get(i);
                  } else {
                    valStringList = valStringList + flightsList.get(i) + ":";
                  }
                }
                out.println(valStringList);
                break;

              case "GET_NAMES_CREW":
                System.out.println("Request Crew Names Recieved");
                LinkedList<String> crewList = crewFlightCont.getCrewNames();
                valStringList = "";
                for(int i = 0; i < crewList.size(); i++) {
                  if(i == crewList.size() - 1) {
                    valStringList = valStringList + crewList.get(i);
                  } else {
                    valStringList = valStringList + crewList.get(i) + ":";
                  }
                }
                out.println(valStringList);
                break;

              case "GET_NAMES_FLIGHTS_ON_DATE":// GET_NAMES_FLIGHTS_ON_DATE:MM:DD:YYYY
                System.out.println("Request Flights on Date Recieved");
                valStringList = "";
                if(parsedLine.length == 4) {
                  LinkedList<String> flightsOnDate = new LinkedList<>();
                  LinkedList<flightClass> flights = crewFlightCont.getFlightClassList();
                  for(int i = 0; i < flights.size(); i++) {
                    int[] flightDate = parseFlightDate(flights.get(i));
                    if(flightDate[0] == Integer.parseInt(parsedLine[1]) && flightDate[1] == Integer.parseInt(parsedLine[2]) && flightDate[2] == Integer.parseInt(parsedLine[3])) {
                      flightsOnDate.add(flights.get(i).getFlightNumber());
                    }
                  }
                  for(int i = 0; i < flightsOnDate.size(); i++) {
                    if(i == flightsOnDate.size() - 1) {
                      valStringList = valStringList + flightsOnDate.get(i);
                    } else {
                      valStringList = valStringList + flightsOnDate.get(i) + ":";
                    }
                  }
                }
                out.println(valStringList);
                break;

              case "GET_FLIGHT_CREW_ASSIGNMENTS":
                System.out.println("Reguest Flight Crew Assignments Recieved");
                valStringList = "";
                if(parsedLine.length == 2) {
                  LinkedList<String> flightCrewAssignments = crewFlightCont.getFlightCrewAssignments(parsedLine[1]);
                  for(int i = 0; i < flightCrewAssignments.size(); i++) {
                    if(i == flightCrewAssignments.size() - 1) {
                      valStringList = valStringList + flightCrewAssignments.get(i);
                    } else {
                      valStringList = valStringList + flightCrewAssignments.get(i) + ":";
                    }
                  }
                }
                out.println(valStringList);
                break;

              case "GET_CREW_FLIGHT_ASSIGNMENTS":
                System.out.println("Reguest Crew Flight Assignments Recieved");
                valStringList = "";
                if(parsedLine.length == 2) {
                  LinkedList<String> crewFlightAssignments = crewFlightCont.getCrewFlightAssignments(parsedLine[2]);
                  for(int i = 0; i < crewFlightAssignments.size(); i++) {
                    if(i == crewFlightAssignments.size() - 1) {
                      valStringList = valStringList + crewFlightAssignments.get(i);
                    } else {
                      valStringList = valStringList + crewFlightAssignments.get(i) + ":";
                    }
                  }
                }
                out.println(valStringList);
                break;

              case "ASSIGN_CREW_TO_FLIGHT":
                System.out.println("Reguest Assign Crew Member to Flight Recieved");
                if(parsedLine.length == 3) {
                  val = crewFlightCont.assignCrewToFlight(parsedLine[1], parsedLine[2]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "GET_FLIGHT_DATE":
                System.out.println("Request Flight Date Recieved");
                valStringList = "";
                if(parsedLine.length == 2) {
                  flightClass flightForDate = crewFlightCont.getFlightClass(parsedLine[1]);
                  String date;
                  if(flightForDate != null) {
                    date = flightForDate.getDate();
                    out.println(date);
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "ADD_CREW":
                System.out.println("Add Crew Member Recieved");
                if(parsedLine.length == 4) {
                  val = crewFlightCont.addCrew(parsedLine[1], parsedLine[2], parsedLine[3]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "ADD_FLIGHT":
                System.out.println("Add Flight Recieved");
                if(parsedLine.length == 7) {
                  val = crewFlightCont.addFlight(parsedLine[1], Integer.parseInt(parsedLine[2]), Integer.parseInt(parsedLine[3]), parsedLine[4], parsedLine[5], parsedLine[6]);
                  if(val == true) {
                    out.println("SUCCESS");
                    System.out.println("Success");
                  } else {
                    out.println("FAIL");
                    System.out.println("Fail1");
                  }
                } else {
                  out.println("FAIL");
                    System.out.println("Fail2");
                }
                break;

              case "REMOVE_CREW":
                System.out.println("Remove Crew Member Recieved");
                if(parsedLine.length == 2) {
                  val = crewFlightCont.removeCrew(parsedLine[1]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "REMOVE_FLIGHT":
                System.out.println("Remove Flight Recieved");
                if(parsedLine.length == 2) {
                  val = crewFlightCont.removeFlight(parsedLine[1]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "GET_RAW_CREW_DATA":
                System.out.println("Get Raw Crew Data Recieved");
                valStringList = "";
                if(parsedLine.length == 2) {
                  crewClass rawCrewClass = crewFlightCont.getCrewClass(parsedLine[1]);
                  if(rawCrewClass != null) {
                    LinkedList<String> rawCrewData = rawCrewClass.getRawCrewData();
                    for(int i = 0; i < rawCrewData.size(); i++) {
                      if(i == rawCrewData.size() - 1) {
                        valStringList = valStringList + rawCrewData.get(i);
                      } else {
                        valStringList = valStringList + rawCrewData.get(i) + ":";
                      }
                    }
                    System.out.println(valStringList);
                    out.println(valStringList);
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "GET_RAW_FLIGHT_DATA":
                System.out.println("Get Raw Flight Data Recieved");
                valStringList = "";
                if(parsedLine.length == 2) {
                  flightClass rawFlightClass = crewFlightCont.getFlightClass(parsedLine[1]);
                  if(rawFlightClass != null) {
                    LinkedList<String> rawFlightData = rawFlightClass.getRawFlightData();
                    for(int i = 0; i < rawFlightData.size(); i++) {
                      if(i == rawFlightData.size() - 1) {
                        valStringList = valStringList + rawFlightData.get(i);
                      } else {
                        valStringList = valStringList + rawFlightData.get(i) + ":";
                      }
                    }
                    out.println(valStringList);
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "EDIT_CREW":
                System.out.println("Edit Crew Member Recieved");
                if(parsedLine.length == 4) {
                  val = crewFlightCont.editCrew(parsedLine[1], parsedLine[2], parsedLine[3]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                }
                break;

              case "EDIT_FLIGHT":
                System.out.println("Edit Flight Recieved");
                if(parsedLine.length == 7) {
                  val = crewFlightCont.editFlight(parsedLine[1], Integer.parseInt(parsedLine[2]), Integer.parseInt(parsedLine[3]), parsedLine[4], parsedLine[5], parsedLine[6]);
                  if(val == true) {
                    out.println("SUCCESS");
                  } else {
                    out.println("FAIL");
                  }
                } else {
                  out.println("FAIL");
                  
                }
                break;
                
              case "REFRESH":
              	System.out.println("Refresh Database Recieved");
              	crewFlightCont = new crewFlightController();
				out.println("SUCCESS");
				break;


           
          }
            crewFlightCont.saveAllData();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  }

    public static void main(String[] args) {
        System.out.println("Starting Server...");
        commandServer server = new commandServer();
        System.out.println("Server started at port 5555");
        server.start(5555);
    }

}
