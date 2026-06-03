package software_engineering;

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
}
