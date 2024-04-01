import java.util.LinkedList;

public class crewClass {

  private String name;
  private String username;
  private String homeAirport;

  public crewClass(LinkedList<String> rawCrewData) {
    if(rawCrewData.size() == 3) {
      name = rawCrewData.get(0);
      username = rawCrewData.get(1);
      homeAirport = rawCrewData.get(2);
    }
  }

  public LinkedList<String> getRawCrewData() {
    LinkedList<String> rawCrewData = new LinkedList<String>();
    rawCrewData.add(name);
    rawCrewData.add(username);
    rawCrewData.add(homeAirport);
    return rawCrewData;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getHomeAirport() {
    return homeAirport;
  }

  public void changeName(String newName) {
    name = newName;
  }

  public void changeUsername(String newUsername) {
    username = newUsername;
  }

  public void changeHomeAirport(String newHomeAirport) {
    homeAirport = newHomeAirport;
  }
}
