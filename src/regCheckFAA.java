// import fileManipulation.Crew;

public class regCheckFAA {

    private static final int MAX_DUTY_HOURS = 14;
    private static final int MAX_FLIGHT_HOURS_SINGLE_PILOT = 8;
    private static final int MAX_FLIGHT_HOURS_TWO_PILOTS = 10;
    private static final int REQUIRED_REST_HOURS = 9;

    /**
     * Checks if a flight assignment for a crew violates FAA regulations.
     * 
     * @param crew The crew object containing crew information.
     * @return true if the assignment violates FAA regulations, false otherwise.
     */
    public static boolean checkForViolations(Crew crew) {
        int dutyHours = crew.getDutyHours();
        int flightHours = crew.getFlightHours();
        int pilotsCount = crew.getPilotsCount();
        int restHoursBeforeDuty = crew.getRestHoursBeforeDuty();

        // Check for maximum duty hours violation
        if (dutyHours > MAX_DUTY_HOURS) {
            popupWarning.displayWarning("FAA Regulation Violation", "Duty period exceeds the maximum allowed 14 hours.");
            return true;
        }

        // Check for flight hours violation based on the number of pilots
        if ((pilotsCount == 1 && flightHours > MAX_FLIGHT_HOURS_SINGLE_PILOT) || (pilotsCount == 2 && flightHours > MAX_FLIGHT_HOURS_TWO_PILOTS)) {
            popupWarning.displayWarning("FAA Regulation Violation", String.format("Flight hours exceed the maximum allowed for %d pilot(s).", pilotsCount));
            return true;
        }

        // Check for required rest hours violation
        if (restHoursBeforeDuty < REQUIRED_REST_HOURS) {
            popupWarning.displayWarning("FAA Regulation Violation", "Insufficient rest hours before the next duty period.");
            return true;
        }

        return false; // No violations detected
    }

       //crew class I wrote before, leaving as a comment in case its useful 
        public class Crew {
        private int dutyHours;
        private int flightHours;
        private int pilotsCount;
        private int restHoursBeforeDuty;
    
        // Constructor
        public Crew(int dutyHours, int flightHours, int pilotsCount, int restHoursBeforeDuty) {
            this.dutyHours = dutyHours;
            this.flightHours = flightHours;
            this.pilotsCount = pilotsCount;
            this.restHoursBeforeDuty = restHoursBeforeDuty;
        }
    
        // Getter methods
        public int getDutyHours() {
            return dutyHours;
        }
    
        public int getFlightHours() {
            return flightHours;
        }
    
        public int getPilotsCount() {
            return pilotsCount;
        }
    
        public int getRestHoursBeforeDuty() {
            return restHoursBeforeDuty;
        }
    } 
}
