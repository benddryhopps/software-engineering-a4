package software_engineering;
import java.sql.*;

public class App 
{
    public static void main( String[] args ){
	Database db = new Database();
	db.createDriverRepository();
	db.createBusRepository();
	try{
		Connection dbConnection = db.connect();
		String sql = "INSERT INTO drivers (driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
		preparedStatement.setString(1, "123456789");
		preparedStatement.setString(2, "Benjamin Novak");
		preparedStatement.setInt(3, 12 );
		preparedStatement.setString(4, "exampleStreet");
		preparedStatement.setString(5, "exampleCity");
		preparedStatement.setString(6, "exampleState");
		preparedStatement.setString(7, "exampleCountry");
		preparedStatement.setString(8, "01-01-01");
		preparedStatement.setInt(9, 1);
		preparedStatement.setString(10, "heavy");
		} catch(SQLException e){
			System.out.println(e);
		}
		try {
			Connection dbConnection = db.connect();
			String sql = "SELECT * FROM driver";
			Statement statement = dbConnection.createStatement();
			statement.execute(sql);
			ResultSet resultSet = statement.getResultSet();
			System.out.println(resultSet);
		} catch(SQLException e){
			System.out.println(e);

		}
	    // Driver records require driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears (int), licenseType
	    // ResultSet results = statement.execute(arbitraryStatement);
	    // System.out.println(results);
    }
}
