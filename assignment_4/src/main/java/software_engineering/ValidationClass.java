package software_engineering;
import java.sql.*;

public class ValidationClass{

    public static boolean validDriverId(String driverID){
        if (driverID.length() != 10){
            return false;
	}

        for (int i = 0; i < 2; i++) {
            char c = driverID.charAt(i);
            if (c < '2' || c > '9'){
                return false;
	 	}
       	}

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = driverID.charAt(i);
            if (!Character.isLetterOrDigit(c)){
                specialCount++;
	    }
        }

        if(specialCount < 2){
       		if(!Character.isUpperCase(driverID.charAt(8))){
            		return false;
		}
	}

        if (!Character.isUpperCase(driverID.charAt(9))){
            	return false;
	} else {
       		 return true;
	}
    }

    public static boolean validBusIdString(String busID){
	    if (busID.length() != 8){
	            return false;
	    }

        for (int i = 0; i < busID.length(); i++) {
            if(!Character.isDigit(busID.charAt(i))){
                return false;
       		}
	}
        return true;
    }

    public static boolean validDOB(String dob){
        if (dob.length() != 10){
            return false;
	}
        if (dob.charAt(2) != '-'){
            return false;
	}
        if (dob.charAt(5) != '-'){
            return false;
	}
        return true;
    }

    public static boolean validAddress(String address){
        int count = 0;
        for (int i = 0; i < address.length(); i++){
            if (address.charAt(i) == '|'){
                count++;
	    }
        }
        return count == 4;
    }

    public static boolean validCapacityUpdate(String busID, int newCapacity, int oldCapacity){
           if(newCapacity > oldCapacity) {
               System.out.println("Capacity cannot increase.");
               return false;
           }   
               return true;
      }

        public static boolean driverExists(Connection conn, String driverID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT driverID FROM drivers WHERE driverID=?"
            );
            stmt.setString(1, driverID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean busExists(Connection conn, String busID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT busId FROM busRepo WHERE busId=?"
            );
            stmt.setString(1, busID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean driverOlderThan50(String dob) {
        try {
            String[] parts = dob.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            java.time.LocalDate birthDate = java.time.LocalDate.of(year, month, day);
            java.time.LocalDate today = java.time.LocalDate.now();
            int age = today.getYear() - birthDate.getYear();
            if (today.getDayOfYear() < birthDate.getDayOfYear()) {
                age--;
            }
            return age > 50;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean driverHasMinExperience(int experienceYears, int minYears) {
        return experienceYears >= minYears;
    }

    public static boolean driverLicenseAllows(String licenseType) {
        return licenseType.equalsIgnoreCase("Heavy") ||
               licenseType.equalsIgnoreCase("Public Transport");
    }

}



	
