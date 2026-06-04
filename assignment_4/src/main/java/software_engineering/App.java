package software_engineering;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ){
	Database db = new Database();
	db.createDriverRepository();
	db.createBusRepository();
	db.seedDatabase();

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
            System.out.println("4. Count Drivers");
			System.out.println("5. List all records");
			System.out.println("6. Exit");

            int action = getValidatedChoice(scanner, 1, 6);

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
                    db.countDrivers();
					break;
				case 5:
					db.printTable("drivers");
					break;
				case 6:
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
            System.out.println("4. Count Buses.");
			System.out.println("5. List all records");
			System.out.println("6. Exit");
            int action = getValidatedChoice(scanner, 1, 6);
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
					db.countBuses();
					break;
				case 5:
					db.printTable("busRepo");
					break;
				case 6:
					return;
            }
        }
    }

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

    }

