import java.util.LinkedList;

public class crewFlightController {
    
  private LinkedList<flightClass> flightList = new LinkedList<flightClass>();
  private LinkedList<crewClass> crewList = new LinkedList<crewClass>();
  private LinkedList<String[]> flightCrewAssignments = new LinkedList<>();
  private fileManipulation file = new fileManipulation();

  public crewFlightController() {
    LinkedList<String> flightNames = file.getFlightNumbers();
    LinkedList<String> crewNames = file.getCrewNames();

    for(int i = 0; i < flightNames.size(); i++) {
      flightList.add(new flightClass(file.loadRawFlightData(flightNames.get(i))));
    }
    for(int i = 0; i < crewNames.size(); i++) {
      crewList.add(new crewClass(file.loadRawCrewData(crewNames.get(i))));
    }
  }

  public void saveAllData() {
    LinkedList<String> rawFlightData = new LinkedList<String>();
    LinkedList<String> rawCrewData = new LinkedList<String>();
    file.saveRawAssignmentData(flightCrewAssignments);
    for(int i = 0; i < flightList.size(); i++) {
      rawFlightData.addAll(flightList.get(i).getRawFlightData());
    }
    for(int i = 0; i < crewList.size(); i++) {
      rawCrewData.addAll(crewList.get(i).getRawCrewData());
    }

    file.saveRawFlightData(rawFlightData);
    file.saveRawCrewData(rawCrewData);
  }

  public LinkedList<flightClass> getFlightClassList() {
    return flightList;
  }

  public LinkedList<crewClass> getCrewClassList() {
    return crewList;
  }

  public flightClass getFlightClass(String flightNumber) throws NotFoundException {
    for(int i = 0; i < flightList.size(); i++) {
      if(flightList.get(i).getFlightNumber().equals(flightNumber)) {
        return flightList.get(i);
      }
    }
    throw new NotFoundException("Flight Not Found: " + flightNumber);
  }

  public crewClass getCrewClass(String name) throws NotFoundException {
    for(int i = 0; i < crewList.size(); i++) {
      if(crewList.get(i).getName().equals(name)) {
        return crewList.get(i);
      }
    }
    throw new NotFoundException("Crew Member Not Found: " + name);
  }

  public LinkedList<String> getFlightNumbers() {
    LinkedList<String> flightNums = new LinkedList<String>();
    for(int i = 0; i < flightList.size(); i++) {
      flightNums.add(flightList.get(i).getFlightNumber());
    }
    return flightNums;
  }
  
  public LinkedList<String> getCrewNames() {
    LinkedList<String> crewNames = new LinkedList<String>();
    for(int i = 0; i < crewList.size(); i++) {
      crewNames.add(crewList.get(i).getName());
    }
    return crewNames;
  }

  public boolean removeFlight(String flightName) {
    for(int i = 0; i < flightCrewAssignments.size(); i++) {
      if(flightCrewAssignments.get(i)[0].equals(flightName)) {
        flightCrewAssignments.remove(i);
      }
    }
    for(int i = 0; i < flightList.size(); i++) {
      if(flightList.get(i).getFlightNumber().equals(flightName)) {
        flightList.remove(i);
        return true;
      }
    }
    return false;
  }

  public boolean removeCrew(String crewName) {
    for(int i = 0; i < flightCrewAssignments.size(); i++) {
      if(flightCrewAssignments.get(i)[1].equals(crewName)) {
        flightCrewAssignments.remove(i);
      }
    }
    for(int i = 0; i < crewList.size(); i++) {
      if(crewList.get(i).getName().equals(crewName)) {
        crewList.remove(i);
        return true;
      }
    }
    return false;
  }

  public boolean addFlight(String flightName, int departTime, int arriveTime, String initAirport, String destAirport, String date) {
    for(int i = 0; i < flightList.size(); i++) {
      if(flightList.get(i).getFlightNumber().equals(flightName)) {
        return false;
      }
    }
    LinkedList<String> rawFlightData = new LinkedList<>();
    rawFlightData.add(flightName);
    rawFlightData.add(Integer.toString(departTime));
    rawFlightData.add(Integer.toString(arriveTime));
    rawFlightData.add(initAirport);
    rawFlightData.add(destAirport);
    rawFlightData.add(date);
    flightList.add(new flightClass(rawFlightData));
    return true;
  }

  public boolean addCrew(String name, String username, String homeAirport) {
    for(int i = 0; i < crewList.size(); i++) {
      if(crewList.get(i).getName().equals(name)) {
        return false;
      }
    }
    LinkedList<String> rawCrewData = new LinkedList<>();
    rawCrewData.add(name);
    rawCrewData.add(username);
    rawCrewData.add(homeAirport);
    crewList.add(new crewClass(rawCrewData));
    return true;
  }

  public boolean assignCrewToFlight(String crewName, String flightNumber) {
    boolean flightExist = false;
    boolean crewExist = false;
    for(int i = 0; i < flightCrewAssignments.size(); i++) {
      if(flightCrewAssignments.get(i)[0].equals(flightNumber) && flightCrewAssignments.get(i)[1].equals(crewName)) {
        return false;
      }
    }
    for(int i = 0; i < flightList.size(); i++) {
      if(flightList.get(i).getFlightNumber().equals(flightNumber)) {
        flightExist = true;
        break;
      }
    }
    for(int i = 0; i < crewList.size(); i++) {
      if(crewList.get(i).getName().equals(crewName)) {
        crewExist = true;
        break;
      }
    }
    if(crewExist == true && flightExist == true) {
      flightCrewAssignments.add(new String[]{flightNumber,crewName});
      return true;
    }
    return false;
  }

  public LinkedList<String> getCrewFlightAssignments(String crewName) {
    LinkedList<String> flightAssignments = new LinkedList<>();
    for(int i = 0; i < flightCrewAssignments.size(); i++) {
      if(flightCrewAssignments.get(i)[1].equals(crewName)) {
        flightAssignments.add(flightCrewAssignments.get(i)[0]);
      }
    }
    return flightAssignments;
  }

  public LinkedList<String> getFlightCrewAssignments(String flightNumber) {
    LinkedList<String> crewAssignments = new LinkedList<>();
    for(int i = 0; i < flightCrewAssignments.size(); i++) {
      if(flightCrewAssignments.get(i)[0].equals(flightNumber)) {
        crewAssignments.add(flightCrewAssignments.get(i)[1]);
      }
    }
    return crewAssignments;

  }
}



class NotFoundException extends Exception {
  public NotFoundException(String message) {
    super(message);
  }
}
