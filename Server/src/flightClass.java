import java.util.LinkedList;


public class flightClass {

  private String flightNumber;
  private int departTime;
  private int arriveTime;
  private String initAirport;
  private String destAirport;
  private String date;

  public flightClass(LinkedList<String> rawFlightData) { // linkedList rawFlightData 
    if(rawFlightData.size() == 6) {
      flightNumber = rawFlightData.get(0);
      departTime = Integer.parseInt(rawFlightData.get(1));
      arriveTime = Integer.parseInt(rawFlightData.get(2));
      initAirport = rawFlightData.get(3);
      destAirport = rawFlightData.get(4);
      date = rawFlightData.get(5);
    }
  }


  public LinkedList<String> getRawFlightData() {
    LinkedList<String> rawFlightData = new LinkedList<String>();
    rawFlightData.add(flightNumber);
    rawFlightData.add(Integer.toString(departTime));
    rawFlightData.add(Integer.toString(arriveTime));
    rawFlightData.add(initAirport);
    rawFlightData.add(destAirport);
    rawFlightData.add(date);
    return rawFlightData;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public int getDepartTime() {
    return departTime;
  }

  public int getArriveTime() {
    return arriveTime;
  }

  public String getInitAirport() {
    return initAirport;
  }

  public String getDestAirport() {
    return destAirport;
  }

  public String getDate() {
    return date;
  }

  public void changeFlightNumber(String newFlightNum) {
    flightNumber = newFlightNum;
  }

  public void changeDepartTime(int newDepartTime) {
    departTime = newDepartTime;
  }

  public void changeArriveTime(int newArriveTime) {
    arriveTime = newArriveTime;
  }

  public void changeInitAirport(String newInitAirport) {
    initAirport = newInitAirport;
  }

  public void changeDestAirport(String newDestAirport) {
    destAirport = newDestAirport;
  }

  public void changeDate(String newDate) {
    date = newDate;
  }





}
