import java.net.*;
import java.io.*;

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

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
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
      while (!inputLine.equals("EXIT")) {
        inputLine = in.readLine();
        String[] parsedLine = inputLine.split(":");
        if(parsedLine.length > 0) {
          if(parsedLine[0].equals("LOGIN")) {
            if(parsedLine.length == 3) {
              boolean val = file.testPass(parsedLine[1], parsedLine[2]);
              if(val == true) {
                out.println("SUCCESS");
              } else {
                out.println("FAIL");
              }
            } else {
              System.out.println("LOGIN COMMAND ERROR");
            }
          } else {
            System.out.println("COMMAND ERROR");
          }
        }
      }
      out.println("TERMINATED");
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
        server.start(5555); // Replace 8080 with your desired port number
        System.out.println("Server Started.");
    }
}
