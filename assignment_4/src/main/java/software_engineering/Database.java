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
		String sql="create table if not exists busRepo ( busId text primary key, capacity int, fuelLevel number, fuelType text)";
		this.executeStatement(sql);

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
}

