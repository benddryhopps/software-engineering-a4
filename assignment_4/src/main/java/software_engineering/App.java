package software_engineering;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ){
	Database db = new Database();
	db.createDriverRepository();
	db.createBusRepository();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("Select table to manage:");
            System.out.println("1. Drivers");
            System.out.println("2. Buses");
            System.out.println("3. Exit");

            int tableChoice = getValidatedChoice(scanner, 1, 3);

            switch (tableChoice) {
                case 1:
                    manageDrivers(db, scanner);
                    break;
                case 2:
                    manageBuses(db, scanner);
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    return;
            }
        }
    }

    private static void manageDrivers(Database db, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Drivers Menu ---");
            System.out.println("1. Add Driver");
            System.out.println("2. Update Driver");
            System.out.println("3. Delete Driver");
            System.out.println("4. Back to Main Menu");

            int action = getValidatedChoice(scanner, 1, 4);

            switch (action) {
                case 1:
                    db.addDriver();
                    break;
                case 2:
                    db.updateDriver();
                    break;
                case 3:
                    db.deleteDriver();
                    break;
                case 4:
                    return;
            }
        }
    }

    private static void manageBuses(Database db, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Buses Menu ---");
            System.out.println("1. Add Bus");
            System.out.println("2. Update Bus Capacity");
            System.out.println("3. Delete Bus");
            System.out.println("4. Back to Main Menu");

            int action = getValidatedChoice(scanner, 1, 4);

            switch (action) {
                case 1:
                    db.addBus();
                    break;
                case 2:
                    db.updateBusCapacity();
                    break;
                case 3:
                    db.deleteBus();
                    break;
                case 4:
                    return;
            }
        }
    }

    /**
     * Prompts the user until a valid integer between min and max is entered.
     */
    private static int getValidatedChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            System.out.print("Enter choice: ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Choice must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

	// try{
		// Connection dbConnection = db.connect();
		// String sql = "INSERT INTO drivers (driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		// PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
		// preparedStatement.setString(1, "123456789");
		// preparedStatement.setString(2, "Benjamin Novak");
		// preparedStatement.setInt(3, 12 );
		// preparedStatement.setString(4, "exampleStreet");
		// preparedStatement.setString(5, "exampleCity");
		// preparedStatement.setString(6, "exampleState");
		// preparedStatement.setString(7, "exampleCountry");
		// preparedStatement.setString(8, "01-01-01");
		// preparedStatement.setInt(9, 1);
		// preparedStatement.setString(10, "heavy");
		// } catch(SQLException e){
			// System.out.println(e);
		// }
		// try {
			// Connection dbConnection = db.connect();
			// String sql = "SELECT * FROM drivers";
			// Statement statement = dbConnection.createStatement();
			// statement.execute(sql);
			// ResultSet resultSet = statement.getResultSet();
			// System.out.println(resultSet);
		// } catch(SQLException e){
			// System.out.println(e);
// 
		// }
	    // Driver records require driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears (int), licenseType
	    // ResultSet results = statement.execute(arbitraryStatement);
	    // System.out.println(results);
    }

