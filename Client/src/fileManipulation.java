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
  private String assignmentFile = "../data/assignments.txt";

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

  private void writeListToFile(String filePath, LinkedList<String> data) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
      for(int i = 0; i < data.size(); i++) {
        if(i < data.size() - 1) {
          writer.write(data.get(i) + ',');
        } else {
          writer.write(data.get(i));
        }
      }
      writer.close();
    
    } catch (IOException e) {
      System.out.println("ERROR");
    }
  }
  
  // --------------------Username and Password Management-------------------------
  
  // Tests password and username
  public boolean testPass(String username, String password) {
    LinkedList<String> data = readFile(credFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(username)) {
        if(data.get(i + 1).equals(password)) {
          return true;
        }
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
  
  public LinkedList<String> getUsernames() {
  	LinkedList<String> data = readFile(credFile);
  	LinkedList<String> usernames = new LinkedList<>();
  	for(int i = 0; i < data.size(); i = i + 3) {
  		usernames.add(data.get(i));
  	}
  	return usernames;
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
  
  public LinkedList<String> getCrewNames() {
    LinkedList<String> crewNames = new LinkedList<String>();
    LinkedList<String> data = readFile(crewFile);
    for(int i = 0; i < data.size(); i = i + 3) {
      crewNames.add(data.get(i));
    }
    return crewNames;
  }

  public void saveRawCrewData(LinkedList<String> rawCompleteCrewData) {
    writeListToFile(crewFile, rawCompleteCrewData);
  }

  public LinkedList<String> loadRawCrewData(String crewName) {
    LinkedList<String> data = readFile(crewFile);
    LinkedList<String> crewData = new LinkedList<String>();
    for(int i = 0; i < data.size(); i = i + 3) {
      if(data.get(i).equals(crewName)) {

        crewData.add(data.get(i));
        crewData.add(data.get(i + 1));
        crewData.add(data.get(i + 2));
        return crewData;
      }
    }
    return crewData;
  }


  // --------------------Flight Management------------------------- 
  
  public LinkedList<String> getFlightNumbers() {
    LinkedList<String> flightNums = new LinkedList<String>();
    LinkedList<String> data = readFile(flightFile);
    for(int i = 0; i < data.size(); i = i + 6) {
      flightNums.add(data.get(i));
    }
    return flightNums;
  }

  public void saveRawFlightData(LinkedList<String> rawCompleteFlightData) {
    writeListToFile(flightFile, rawCompleteFlightData);
  }


  public LinkedList<String> loadRawFlightData(String flightNum) {
    LinkedList<String> data = readFile(flightFile);
    LinkedList<String> flightData = new LinkedList<String>();
    for(int i = 0; i < data.size(); i = i + 6) {
      if(data.get(i).equals(flightNum)) {

        flightData.add(data.get(i));
        flightData.add(data.get(i + 1));
        flightData.add(data.get(i + 2));
        flightData.add(data.get(i + 3));
        flightData.add(data.get(i + 4));
        flightData.add(data.get(i + 5));
        return flightData;
      } 
      
    }
    return flightData;
  }

  // --------------------- Assignment Management ----------------------
  
  public void saveRawAssignmentData(LinkedList<String[]> rawCompleteAssignmentData) {
    LinkedList<String> data = new LinkedList<String>();
    for(int i = 0; i < rawCompleteAssignmentData.size(); i++) {
      data.add(rawCompleteAssignmentData.get(i)[0]);
      data.add(rawCompleteAssignmentData.get(i)[1]);
    }
    writeListToFile(assignmentFile, data);
  }

  public LinkedList<String[]> loadRawAssignmentData() {
    LinkedList<String> data = readFile(assignmentFile);
    LinkedList<String[]> assignmentData = new LinkedList<String[]>();
    for(int i = 0; i < data.size(); i = i + 2) {
      assignmentData.add(new String[]{data.get(i), data.get(i+1)});
    }
    return assignmentData;
  }


}
