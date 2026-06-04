package software_engineering;

import java.util.Scanner;
import java.sql.*;


public class Database {

	public Database(){
		System.out.println("Initialising Database instance...");
		System.out.println(this + " has been initialised succesfully!");
	}

	private static final String URL="jdbc:sqlite:app.db";

	public Connection connect() throws SQLException {
		return DriverManager.getConnection(URL);
	}

	public void createDriverRepository(){ // address is stored across multiple elements, and streetNumber is stored as a string in case of driver residence being a unit / flat.
		String sql="create table if not exists drivers( driverID text not null primary key, name text not null, streetNumber text not null, streetName text not null, city text not null, state text not null, country text not null, dob text not null, experienceYears int, licenseType text not null)";
		this.executeStatement(sql);
	}

	public void createBusRepository(){
		String sql="create table if not exists busRepo ( busId text primary key, capacity int, fuelLevel number, fuelType text, driverID text default null)";
		this.executeStatement(sql);

	}

	public void seedDatabase() {
    seedDrivers();
    seedBuses();
	}

	private void seedDrivers() {
		String sql = "INSERT OR IGNORE INTO drivers " +
				"(driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "DR12345678");
			pstmt.setString(2, "John Smith");
			pstmt.setString(3, "12");
			pstmt.setString(4, "Queen St");
			pstmt.setString(5, "Melbourne");
			pstmt.setString(6, "VIC");
			pstmt.setString(7, "Australia");
			pstmt.setString(8, "10-05-1985");
			pstmt.setInt(9, 12);
			pstmt.setString(10, "Heavy");
			pstmt.executeUpdate();
			pstmt.setString(1, "DR87654321");
			pstmt.setString(2, "Emily Brown");
			pstmt.setString(3, "45");
			pstmt.setString(4, "Collins St");
			pstmt.setString(5, "Melbourne");
			pstmt.setString(6, "VIC");
			pstmt.setString(7, "Australia");
			pstmt.setString(8, "22-11-1990");
			pstmt.setInt(9, 5);
			pstmt.setString(10, "Light");

			pstmt.executeUpdate();

			System.out.println("Sample drivers inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void seedBuses() {
		String sql = "INSERT OR IGNORE INTO busRepo (busId, capacity, fuelLevel, fuelType) " +
				"VALUES (?, ?, ?, ?)";

		try (Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, "BUS10001");
			pstmt.setInt(2, 45);
			pstmt.setDouble(3, 75.5);
			pstmt.setString(4, "Diesel");
			pstmt.executeUpdate();

			pstmt.setString(1, "BUS10002");
			pstmt.setInt(2, 30);
			pstmt.setDouble(3, 90.0);
			pstmt.setString(4, "Electricity");
			pstmt.executeUpdate();

			System.out.println("Sample buses inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void executeStatement(String sql){
		try{
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		} catch(SQLException e){
		e.printStackTrace();
		}
	}

	public void addDriver() {
    Scanner scanner = new Scanner(System.in);

    String driverID;
    while (true) {
        System.out.print("Driver ID (or type 'exit' to cancel): ");
        driverID = scanner.nextLine();
        if (driverID.equalsIgnoreCase("exit")) return;
        if (ValidationClass.validDriverId(driverID)) break;
        System.out.println("Invalid Driver ID. Must be 10 chars, first two digits 2-9, etc.");
    }

    System.out.print("Name: ");
    String name = scanner.nextLine();
    if (name.equalsIgnoreCase("exit")) return;

    System.out.print("Street Number: ");
    String streetNumber = scanner.nextLine();
    if (streetNumber.equalsIgnoreCase("exit")) return;

    System.out.print("Street Name: ");
    String streetName = scanner.nextLine();
    if (streetName.equalsIgnoreCase("exit")) return;

    System.out.print("City: ");
    String city = scanner.nextLine();
    if (city.equalsIgnoreCase("exit")) return;

    System.out.print("State: ");
    String state = scanner.nextLine();
    if (state.equalsIgnoreCase("exit")) return;

    System.out.print("Country: ");
    String country = scanner.nextLine();
    if (country.equalsIgnoreCase("exit")) return;

    String dob;
    while (true) {
        System.out.print("DOB (dd-mm-yyyy, or type 'exit' to cancel): ");
        dob = scanner.nextLine();
        if (dob.equalsIgnoreCase("exit")) return;
        if (ValidationClass.validDOB(dob)) break;
        System.out.println("Invalid DOB format. Must be dd-mm-yyyy.");
    }

    int experienceYears = 0;
    while (true) {
        System.out.print("Experience Years: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("exit")) return;
        try {
            experienceYears = Integer.parseInt(input);
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please enter an integer.");
        }
    }

    System.out.print("License Type: ");
    String licenseType = scanner.nextLine();
    if (licenseType.equalsIgnoreCase("exit")) return;

    String sql = "INSERT INTO drivers(driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, driverID);
        pstmt.setString(2, name);
        pstmt.setString(3, streetNumber);
        pstmt.setString(4, streetName);
        pstmt.setString(5, city);
        pstmt.setString(6, state);
        pstmt.setString(7, country);
        pstmt.setString(8, dob);
        pstmt.setInt(9, experienceYears);
        pstmt.setString(10, licenseType);

        pstmt.executeUpdate();
        System.out.println("Driver added successfully.");

    } catch (SQLException e) {
        e.printStackTrace();
	    }
	}

	public void updateDriver() {
    Scanner scanner = new Scanner(System.in);
	try{
		Connection connection = this.connect();

		String driverID;
		while (true) {
			System.out.print("Driver ID to update (or type 'exit'): ");
			driverID = scanner.nextLine();
			if (driverID.equalsIgnoreCase("exit")) return;
			if (ValidationClass.driverExists(connection, driverID)) break;
			System.out.println("Driver not found.");
		}

    try (Connection conn = connect()) {
        PreparedStatement selectStmt = conn.prepareStatement(
                "SELECT * FROM drivers WHERE driverID=?");
        selectStmt.setString(1, driverID);
        ResultSet rs = selectStmt.executeQuery();
        if (!rs.next()) return;

        int experienceYears = rs.getInt("experienceYears");
        String currentLicense = rs.getString("licenseType");

        // Address input
        String address;
        String[] addressParts;
        while (true) {
            System.out.print("New Address (StreetNumber|StreetName|City|State|Country): ");
            address = scanner.nextLine();
            if (address.equalsIgnoreCase("exit")) return;
            if (ValidationClass.validAddress(address)) {
                addressParts = address.split("\\|");
                break;
            }
            System.out.println("Invalid address format.");
        }

        // DOB input
        String dob;
        while (true) {
            System.out.print("New DOB (DD-MM-YYYY): ");
            dob = scanner.nextLine();
            if (dob.equalsIgnoreCase("exit")) return;
            if (ValidationClass.validDOB(dob)) break;
            System.out.println("Invalid DOB format.");
        }

        // License update restriction
        String licenseType = currentLicense;
        if (experienceYears <= 10) {
            System.out.print("New License Type (leave blank to keep current): ");
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("exit") && !input.isEmpty()) {
                licenseType = input;
            }
        } else {
            System.out.println("License cannot be changed for drivers with >10 years experience.");
        }

        PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE drivers SET streetNumber=?, streetName=?, city=?, state=?, country=?, dob=?, licenseType=? WHERE driverID=?");
        updateStmt.setString(1, addressParts[0]);
        updateStmt.setString(2, addressParts[1]);
        updateStmt.setString(3, addressParts[2]);
        updateStmt.setString(4, addressParts[3]);
        updateStmt.setString(5, addressParts[4]);
        updateStmt.setString(6, dob);
        updateStmt.setString(7, licenseType);
        updateStmt.setString(8, driverID);
        updateStmt.executeUpdate();
        System.out.println("Driver updated successfully.");

    } catch (SQLException e) {
        e.printStackTrace();
    }
} catch(SQLException e){
	System.out.println(e);
	}
	}

	public void deleteDriver() {
    Scanner scanner = new Scanner(System.in);

    String driverID;
	try{Connection connection = this.connect();
			while (true) {
				System.out.print("Driver ID to delete (or type 'exit'): ");
				driverID = scanner.nextLine();
				if (driverID.equalsIgnoreCase("exit")) return;
				if (ValidationClass.driverExists(connection, driverID)) break;
				System.out.println("Driver not found.");
			}

			try (Connection conn = connect()) {
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM drivers WHERE driverID=?");
				stmt.setString(1, driverID);
				stmt.executeUpdate();
				System.out.println("Driver deleted successfully.");
			} catch (SQLException e) {
				e.printStackTrace();
				}
		} catch(SQLException e){
			System.out.println(e);
		}

	}


	public void addBus() {
		Scanner scanner = new Scanner(System.in);

			String busID;
			try{Connection connection  = this.connect();
				while (true) {
					System.out.print("Bus ID (8 digits, unique, or type 'exit'): ");
					busID = scanner.nextLine();
					if (busID.equalsIgnoreCase("exit")) return;
					if (!ValidationClass.validBusIdString(busID)) {
						System.out.println("Invalid Bus ID format.");
						continue;
					}
					if (ValidationClass.busExists(connection, busID)) {
						System.out.println("Bus ID already exists.");
						continue;
					}
					break;
				}

			int capacity = 0;
			while (true) {
				System.out.print("Capacity: ");
				String input = scanner.nextLine();
				if (input.equalsIgnoreCase("exit")) return;
				try {
					capacity = Integer.parseInt(input);
					if (capacity > 0) break;
					System.out.println("Capacity must be positive.");
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid integer.");
				}
			}

			double fuelLevel = 0;
			while (true) {
				System.out.print("Fuel Level: ");
				String input = scanner.nextLine();
				if (input.equalsIgnoreCase("exit")) return;
				try {
					fuelLevel = Double.parseDouble(input);
					if (fuelLevel >= 0) break;
					System.out.println("Fuel Level cannot be negative.");
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}
			}

			String fuelType;
			while (true) {
				System.out.print("Fuel Type (Gasoline/Diesel/Electricity/Hybrid): ");
				fuelType = scanner.nextLine();
				if (fuelType.equalsIgnoreCase("exit")) return;
				if (!fuelType.isEmpty()) break;
			}

			try (Connection conn = connect()) {
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO busRepo(busId, capacity, fuelLevel, fuelType) VALUES (?, ?, ?, ?)");
				stmt.setString(1, busID);
				stmt.setInt(2, capacity);
				stmt.setDouble(3, fuelLevel);
				stmt.setString(4, fuelType);
				stmt.executeUpdate();
				System.out.println("Bus added successfully.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			} catch(SQLException e){
				System.out.println(e);
			}
		}

	public void updateBusCapacity() {
    	Scanner scanner = new Scanner(System.in);

		try{Connection connection = this.connect();
			String busID;
			while (true) {
				System.out.print("Bus ID to update (or type 'exit'): ");
				busID = scanner.nextLine();
				if (busID.equalsIgnoreCase("exit")) return;
				if (ValidationClass.busExists(connection, busID)) break;
				System.out.println("Bus not found.");
			}

			int newCapacity = 0;
			int oldCapacity = 0;

			try (Connection conn = connect()) {
				PreparedStatement selectStmt = conn.prepareStatement("SELECT capacity FROM busRepo WHERE busId=?");
				selectStmt.setString(1, busID);
				ResultSet rs = selectStmt.executeQuery();
				if (rs.next()) {
					oldCapacity = rs.getInt("capacity");
				}

				while (true) {
					System.out.print("New Capacity: ");
					String input = scanner.nextLine();
					if (input.equalsIgnoreCase("exit")) return;
					try {
						newCapacity = Integer.parseInt(input);
						if (ValidationClass.validCapacityUpdate(busID, newCapacity, oldCapacity)) break;
					} catch (NumberFormatException e) {
						System.out.println("Enter a valid integer.");
					}
				}

				PreparedStatement updateStmt = conn.prepareStatement("UPDATE busRepo SET capacity=? WHERE busId=?");
				updateStmt.setInt(1, newCapacity);
				updateStmt.setString(2, busID);
				updateStmt.executeUpdate();
				System.out.println("Bus capacity updated successfully.");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			} catch(SQLException e){
				e.printStackTrace();
			}
	}


	public void deleteBus() {
	
		Scanner scanner = new Scanner(System.in);
		try{Connection connection = this.connect();
			String busID;
			while (true) {
				System.out.print("Bus ID to delete (or type 'exit'): ");
				busID = scanner.nextLine();
				if (busID.equalsIgnoreCase("exit")) return;
				if (ValidationClass.busExists(connection, busID)) break;
				System.out.println("Bus not found.");
			}

			try (Connection conn = connect()) {
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM busRepo WHERE busId=?");
				stmt.setString(1, busID);
				stmt.executeUpdate();
				System.out.println("Bus deleted successfully.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void countDrivers() {
    String sql = "SELECT COUNT(*) FROM drivers";
    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            System.out.println("Total drivers: " + rs.getInt(1));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
 }

	public void countBuses() {
	    String sql = "SELECT COUNT(*) FROM busRepo";
	    try (Connection conn = connect();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        if (rs.next()) {
	            System.out.println("Total buses: " + rs.getInt(1));
	        }

	    } catch (SQLException e) {
        e.printStackTrace();
    }
	}

	public ResultSet selectAll(String tableName) {
	    String sql = "SELECT * FROM " + tableName;
	    try {
	        Connection conn = connect();
	        Statement stmt = conn.createStatement();
	        return stmt.executeQuery(sql);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    return null;
	}


		public void printTable(String tableName) {
		try {
			ResultSet rs = selectAll(tableName);
			if (rs == null) return;
			ResultSetMetaData meta = rs.getMetaData();
			int columns = meta.getColumnCount();
			for (int i = 1; i <= columns; i++) {
				System.out.print(meta.getColumnName(i) + "\t\t");
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i <= columns; i++) {
					System.out.print(rs.getString(i) + "\t\t");
				}
				System.out.println();
			}
			rs.getStatement().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void assignDriverToBus() {
    Scanner scanner = new Scanner(System.in);

    try (Connection conn = connect()) {
        String busID;
        while (true) {
            System.out.print("Enter Bus ID (or 'exit'): \n");
            busID = scanner.nextLine();
            if (busID.equalsIgnoreCase("exit")) return;
            if (ValidationClass.busExists(conn, busID)) break;
            System.out.println("Bus not found.");
        }

        String driverID;
        while (true) {
            System.out.print("Enter Driver ID to assign (or 'exit'): \n");
            driverID = scanner.nextLine();
            if (driverID.equalsIgnoreCase("exit")) return;
            if (ValidationClass.driverExists(conn, driverID)) break;
            System.out.println("Driver not found.");
        }

        PreparedStatement busStmt = conn.prepareStatement("SELECT capacity, fuelType FROM busRepo WHERE busId=?");
        busStmt.setString(1, busID);
        ResultSet busRS = busStmt.executeQuery();
        if (!busRS.next()) return;
        int busCapacity = busRS.getInt("capacity");
        String fuelType = busRS.getString("fuelType");

        PreparedStatement driverStmt = conn.prepareStatement(
            "SELECT dob, experienceYears, licenseType FROM drivers WHERE driverID=?"
        );
        driverStmt.setString(1, driverID);
        ResultSet driverRS = driverStmt.executeQuery();
        if (!driverRS.next()) return;
        String dob = driverRS.getString("dob");
        int experience = driverRS.getInt("experienceYears");
        String license = driverRS.getString("licenseType");

        String[] dobParts = dob.split("-");
        int birthYear = Integer.parseInt(dobParts[2]);
        int currentYear = java.time.LocalDate.now().getYear();
        int age = currentYear - birthYear;

        if (age > 50 && busCapacity >= 50) {
            System.out.println("Assignment failed: Driver is over 50 and cannot drive a bus with capacity >= 50.");
            return;
        }

        if (fuelType.equalsIgnoreCase("Electricity") && experience < 5) {
            System.out.println("Assignment failed: Driver must have at least 5 years experience for electric buses.");
            return;
        }

        if ((fuelType.equalsIgnoreCase("Electricity") || fuelType.equalsIgnoreCase("Hybrid"))
                && !(license.equalsIgnoreCase("Heavy") || license.equalsIgnoreCase("Public Transport"))) {
            System.out.println("Assignment failed: Driver must have Heavy or Public Transport license.");
            return;
        }

        PreparedStatement assignStmt = conn.prepareStatement(
            "UPDATE busRepo SET driverID=? WHERE busId=?"
        );
        assignStmt.setString(1, driverID);
        assignStmt.setString(2, busID);
        assignStmt.executeUpdate();

        System.out.println("Driver " + driverID + " successfully assigned to bus " + busID + ".");

    } catch (SQLException e) {
        e.printStackTrace();
    }
	}

	// Adding in a busExists and driverExists method to this class instead of validation class
	// even though I am keeping in validationClass's method for it so that I don't have to rewrite (not enough time)
	// its just gonna be a massive pain in the ass for me to keep wrapping those crazy nested try-catch blocks.

	public boolean driverExists(String driverID) {
    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(
                 "SELECT 1 FROM drivers WHERE driverID=?")) {
        stmt.setString(1, driverID);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
    }

	public boolean busExists(String busID) {
    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(
                 "SELECT 1 FROM busRepo WHERE busId=?")) {
        stmt.setString(1, busID);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
	 }


}

