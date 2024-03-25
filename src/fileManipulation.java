import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class fileManipulation {

  private String credFile = "../data/credentials.txt";
  private String crewFile = "../data/crewMembers.txt";
  private String flightFile = "../data/flightList.txt";

  // read based on line and cell
  // write based on line and cell
  private LinkedList<String> readFile(String filePath) {
    LinkedList<String> data = new LinkedList<String>();
    try {
        Scanner read = new Scanner(new File(filePath)).useDelimiter(",");
        while(read.hasNext()) {
          data.add(read.next());
        }
        read.close();
        return data;
    } catch(IOException e) {
      //showAlert("Error", "IOException while reading file.", Alert.AlertType.ERROR);
      System.out.println("ERROR.");
      // Placeholder for now until later 
      LinkedList<String> error = new LinkedList<String>();
      error.add("Error");
      return error;
    }

  }
  // Writes a string to a csv file at a given location
  private void writeString(String filePath, String message, int location) {
    LinkedList<String> data = readFile(filePath);
    data.add(location, message);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
      for(int i = 0; i < data.size(); i++) {
        if(i < data.size() - 1) {
          writer.write(data.get(i) + ",");
        } else {
          writer.write(data.get(i));
        }
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("ERROR.");
    }
  }
  // Writes a string to a csv file at the end of the file 
  private void writeString(String filePath, String message) {
    LinkedList<String> data = readFile(filePath);
    data.add(message);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
      for(int i = 0; i < data.size(); i++) {
        if(i < data.size() - 1) {
          writer.write(data.get(i) + ",");
        } else {
          writer.write(data.get(i));
        } 
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("ERROR.");
    }
  }
  
  // --------------------Username and Password Management-------------------------
  
  // Tests password and username
  public boolean testPass(String username, String password) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        return true;
      }
    }
    return false;
  }

  // Returns 0 if a crew role or 1 if an admin role. Returns -1 if username doesn't exist
  public int getRole(String username) { // 0 if crew member 1 if admin
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        if(data.get(i + 2) == "crew") {
          return 0;
        } else if(data.get(i + 2) == "admin") {
          return 1;
        }
      }
    }
    return -1;
  }

  // Returns false if a username already exists, true if successful
  public boolean newAccount(String username, String password) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        return false;
      }
    }
    writeString(credFile, username);
    writeString(credFile, password);
    writeString(credFile, "crew");
    return true;
  }
  
  // Changes the role of a certain username. Returns true if successful
  public boolean changeRole(String username, int role) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        if(role == 0) {
          writeString(credFile, "crew", i+2);
        } else if(role == 1) {
          writeString(credFile, "admin", i+2);
        }
        return true;
      }
    }
    return false;
  }
  // Changes user password, returns true if successful
  public boolean changePass(String username, String newPassword) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        writeString(credFile, newPassword, i+1);
        return true;
      }
    }
    return false;

  }

  // --------------------Crew Management-------------------------
  
  // Returns true if successful.
  public boolean newCrewMember(String name, String username, String home) {
    LinkedList<String> data = readFile(crewFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(name)) {
        return false;
      }
    }
    writeString(crewFile, name);
    writeString(crewFile, username);
    writeString(crewFile, home);
    return true;
  }
  
  // returns true if successful
  public boolean changeUser(String name, String username) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(name)) {
        writeString(credFile, username, i+1);
        return true;
      }
    }
    return false;

  }
  
  // returns true if successful
  public boolean changeHome(String name, String home) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(name)) {
        writeString(credFile, home, i+2);
        return true;
      }
    }
    return false;

  }
  // returns complete crew list as a linkedList
  public LinkedList<String> getCrewList() {
    LinkedList<String> data = readFile(crewFile);
    return data;
  }
  
  // Returns a crews home airport given the crew name
  public String getHome(String name) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(name)) {
        return data.get(i+2);
      }
    }
    return "ERROR";

  }
  
  // Returns username of a crew member given the crew name
  public String getUser(String name) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(name)) {
        return data.get(i+1);
      }
    }
   return "ERROR";
  }

  // --------------------Flight Management------------------------- 
  
  // Creates a new flight given parameters
  public boolean newFlight(String flightNum, int departTime, int arriveTime, String initialAirport, String destinationAirport, String date) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return false;
      }
    }
    writeString(flightFile, flightNum);
    writeString(flightFile, Integer.toString(departTime));
    writeString(flightFile, Integer.toString(arriveTime));
    writeString(flightFile, initialAirport);
    writeString(flightFile, destinationAirport);
    writeString(flightFile, date);
    return true;
  }
  
  // Gets a flight's depart time as an int
  public int getDepartTime(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return Integer.parseInt(data.get(i+1));
      } 
    }
    return -1;

  }
  
  // gets the fight's arrive time as an int
  public int getArriveTime(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return Integer.parseInt(data.get(i+2));
      } 
    }
    return -1;
  }
  
  // gets the flight's initial airport
  public String getInitialAirport(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return data.get(i+3);
      } 
    }
    return "ERROR";
  }
  
  // gets the flight's destination airport
  public String getDestinationAirport(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return data.get(i+4);
      } 
    }
    return "ERROR";
  }

  // gets the flight's operational date
  public String getDate(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        return data.get(i+5);
      } 
    }
    return "ERROR";
  }
  

  public boolean changeDepartTime(String flightNum, int departTime) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        writeString(flightFile, Integer.toString(departTime), i+1);
        return true;
      } 
    }
    return false; 
  }

  public boolean changeArriveTime(String flightNum, int arriveTime) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        writeString(flightFile, Integer.toString(arriveTime), i+2);
        return true;
      } 
    }
    return false; 
  }

  public boolean changeInitialAirport(String flightNum, String airport) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        writeString(flightFile, airport, i+3);
        return true;
      } 
    }
    return false; 
  }

  public boolean changeDestinationAirport(String flightNum, String airport) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        writeString(flightFile, airport, i+4);
        return true;
      } 
    }
    return false; 
  }

  public boolean changeDate(String flightNum, String date) {
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {
        writeString(flightFile, date, i+5);
        return true;
      } 
    }
    return false; 
  }




}
