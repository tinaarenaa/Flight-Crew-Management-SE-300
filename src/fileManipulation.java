import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class fileManipulation {

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

  private void writeString(String filePath, String message, int location) {
    LinkedList<String> data = readFile(filePath);
    data.add(location, message);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
      for(int i = 0; i < data.size(); i++) {
        writer.write(data.get(i) + ",");
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("ERROR.");
    }
  }
  
  private void writeString(String filePath, String message) {
    LinkedList<String> data = readFile(filePath);
    data.add(message);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
      for(int i = 0; i < data.size(); i++) {
        writer.write(data.get(i) + ",");
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("ERROR.");
    }
  }
  





}
