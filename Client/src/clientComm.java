import java.net.*;
import java.io.*;

public class clientComm {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
      try {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public String sendCommand(String msg) {
      try {
        out.println(msg);
        String resp = in.readLine();
        return resp;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      
      }
    }

    public void stopConnection() {
      try {
        in.close();
        out.close();
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}
