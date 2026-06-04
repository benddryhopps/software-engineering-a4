package software_engineering;

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
}

