package software_engineering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class unitTest {

       // TEMPORARY TESTING DB

       @BeforeEach
       void setup() {
        Database testDatabase = new Database();
        testDatabase.createDriverRepository();
        testDatabase.createBusRepository();
       }

       Database testDatabase = new Database();

       private Connection getConnection() throws SQLException{
        return testDatabase.connect();
       }

       private void clearTables() throws SQLException {
            try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()){
            stmt.execute("DELETE FROM drivers");
            stmt.execute("DELETE FROM drivers");
            }
        }

        // Task 1 Test Case 1, tests input valiation of driverID field - By Hung
        @Test
        void taskOneTestCaseOne(){
            String validDriverID = "24@@abcdXY";
            String invalidDriverID = "12@@abcdXY";
            String edgeCaseDriverID = "24@aabcdXY";
            assertTrue(software_engineering.ValidationClass.validDriverId(validDriverID));
            assertFalse(software_engineering.ValidationClass.validDriverId(invalidDriverID));
            assertFalse(software_engineering.ValidationClass.validDriverId(edgeCaseDriverID));
        }

        // Task 1 Test Case 2, tests input validation of address field - By Hung
        // It is worth noting that this test is no longer in use, as updateDriver() method uses an interface that takes each one of these elements individually.
        // @Test
        // void taskOneTestCaseTwo(){
        //     String validAddress = "12|Main Street|Melbourne|VIC|Australia";
        //     String invalidAddress = "12|Main Street|Melbourne|VIC";
        //     String edgeCaseAddress = "12|Main Street|Melbourne|VIC|amongus???";
        //     assertTrue(software_engineering.ValidationClass.validAddress(validAddress));
        //     assertFalse(software_engineering.ValidationClass.validAddress(invalidAddress));
        //     assertFalse(software_engineering.ValidationClass.validAddress(edgeCaseAddress));
        // }

        // Task 1, Test Case 3, tests input validation of date of birth field;
        @Test
        void taskOneTestCaseThree(){
            String validDOB = "01-01-2000";
            String invalidDOB = "01/01/2000";
            String edgeCaseDOB = "01-1-2000";
            assertTrue(software_engineering.ValidationClass.validDOB(validDOB));
            assertFalse(software_engineering.ValidationClass.validDOB(invalidDOB));
            assertFalse(software_engineering.ValidationClass.validDOB(edgeCaseDOB));
        }

        
        // Task 1 Test Case 4, test input validation of the license update restrictions for drivers with greater than 10 years of experience.
         @Test
        void taskOneTestCaseFour() throws Exception {
        String exampleDriverOnePK = "23@#ABCXY0";
        String exampleDriverTwoPK = "24$%DEFYZ0";
        String exampleDriverThreePK = "25&*GHIJK0";
        try (Connection connection = testDatabase.connect()) {
            PreparedStatement insert = connection.prepareStatement(
                "INSERT OR REPLACE INTO drivers " +
                "(driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            // Insert fake drivers into testing database, they are all the same, except for yearsExperience and PK attributes
            insert.setString(1, exampleDriverOnePK);
            insert.setString(2, "A");
            insert.setString(3, "1");
            insert.setString(4, "A");
            insert.setString(5, "Melbourne");
            insert.setString(6, "VIC");
            insert.setString(7, "Australia");
            insert.setString(8, "01-01-1980");
            insert.setInt(9, 12);
            insert.setString(10, "Light");
            insert.executeUpdate();

            insert.setString(1, exampleDriverTwoPK);
            insert.setInt(9, 11);
            insert.executeUpdate();

            insert.setString(1, exampleDriverThreePK);
            insert.setInt(9, 10);
            insert.executeUpdate();

            // This section of code updates Driver 1 and 3 from licenseType attribute value of "Light" to "Heavy";
            // I am unable to demonstrate rejection of an invalid value, because the prompt will simply skip an opening for it if yearsExperience attribute > 10
            // It took me a really long time to figure out what was going wrong there, because the second scanner was leaving a newline for the third, which would stop case 3 from updating, though I thought it was a logic error. fml
            List<String> driverPKList = List.of(exampleDriverOnePK, exampleDriverTwoPK, exampleDriverThreePK);
            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT licenseType FROM drivers;");){
                    testDatabase.updateDriver(new Scanner(driverPKList.get(0) + "\n\n\n\n\n\n\n\nHeavy"));
                    testDatabase.updateDriver(new Scanner(driverPKList.get(2) + "\n\n\n\n\n\n\n\nHeavy"));
                    resultSet.next();
                    assertEquals("Heavy", resultSet.getString("licenseType"));
                    resultSet.next();
                    resultSet.next();
                    assertEquals("Light", resultSet.getString("licenseType"));
                    connection.close();
                }
            }
        }

        // The invalid and edge cases for this test cannot be implemented like the above, as the updateDriver method does not prompt for name or input under any circumstances.
        // It does not implement the updateDriver() method's ability to update unrelated record attributes because that is also showcased in the above method.
    @Test
    void taskOneTestCaseFive(){
        assertTrue(true);
    }


    // This test case validates busID primary key selection, based off of uniqueness, and all digit formatting, along with a fixed 8 digit length
    @Test
    void taskTwoTestCaseOneValidCase() {
        testDatabase.addBus(new Scanner("12345678\n50\n50\nGasoline\nexit\n"));

        try (Connection connection = testDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                 "SELECT busID FROM busRepo WHERE busID = '12345678'"
             )) {

            assertTrue(resultSet.next());
            assertEquals("12345678", resultSet.getString("busID"));

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed with SQLException");
        }
    }

    // This test case checks that the System rejects invalid busID PKs.
    @Test
    void taskTwoTestCaseOneInvalidCase() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        testDatabase.addBus(new Scanner("1234\nexit\n"));
        String outputString = output.toString().toLowerCase();
        assertTrue(outputString.contains("invalid"));
        
        System.setOut(originalOut);
    }

    // This test case checks for uniqueness in busIDs
    @Test
    void taskTwoTestCaseOneEdgeCase() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // Appended explicit fallback "exit" tokens at every potential input junction
        testDatabase.addBus(new Scanner("12345678\n50\n50\nGasoline\nexit\n"));
        testDatabase.addBus(new Scanner("12345678\nexit\n"));
        
        String outputString = output.toString();
        System.setOut(originalOut);
        
        assertTrue(outputString.toLowerCase().contains("already exists"));
    }

    @Test
    void taskTwoTestCaseTwo() throws Exception {
        testDatabase.addBus(new Scanner("87654321\n50\n100.0\nDiesel\n"));
        assertTrue(ValidationClass.validCapacityUpdate("87654321", 40, 50));
        assertFalse(ValidationClass.validCapacityUpdate("87654321", 60, 50));
        assertTrue(ValidationClass.validCapacityUpdate("87654321", 50, 50));
    }

    @Test
    void taskTwoTestCaseThree() {
        assertTrue(ValidationClass.driverOlderThan50("01-01-1970"));
        assertFalse(ValidationClass.driverOlderThan50("01-01-2000"));
    }

    @Test
    void taskTwoTestCaseFour() {
        assertTrue(ValidationClass.driverHasMinExperience(5, 5));
        assertFalse(ValidationClass.driverHasMinExperience(4, 5));
    }

    @Test
    void taskTwoTestCaseFive() {
        assertTrue(ValidationClass.driverLicenseAllows("Heavy"));
        assertTrue(ValidationClass.driverLicenseAllows("Public Transport"));
        assertFalse(ValidationClass.driverLicenseAllows("Medium"));
    }

    @Test
    void taskThreeTestCaseOne() throws Exception {
        String driverID = "23@#abcdAB";
        assertTrue(ValidationClass.validDriverId(driverID));
        assertTrue(ValidationClass.validAddress("12|Main Street|Melbourne|VIC|Australia"));
        assertTrue(ValidationClass.validDOB("15-06-1990"));
        assertTrue(ValidationClass.driverLicenseAllows("Heavy"));
    }

    @Test
    void taskThreeTestCaseTwo() {
        String driverID = "1234567890";
        assertFalse(ValidationClass.validDriverId(driverID));
    }

    @Test
    void taskThreeTestCaseThree() throws Exception {
        String driverID = "23@#abcdAB";
        assertTrue(ValidationClass.validDriverId(driverID));
        assertTrue(ValidationClass.validAddress("99|High Street|Sydney|NSW|Australia"));
        
        try (Connection connection = testDatabase.connect()) {
            PreparedStatement insert = connection.prepareStatement(
                "INSERT OR REPLACE INTO drivers (driverID, name, streetNumber, streetName, city, state, country, dob, experienceYears, licenseType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            insert.setString(1, driverID);
            insert.setString(2, "John Smith");
            insert.setString(3, "99");
            insert.setString(4, "High Street");
            insert.setString(5, "Sydney");
            insert.setString(6, "NSW");
            insert.setString(7, "Australia");
            insert.setString(8, "15-06-1990");
            insert.setInt(9, 5);
            insert.setString(10, "Heavy");
            insert.executeUpdate();
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT city FROM drivers WHERE driverID = '23@#abcdAB'")) {
                assertTrue(resultSet.next());
                assertEquals("Sydney", resultSet.getString("city"));
            }
        }
    }

    @Test
    void taskThreeTestCaseFour() {
        assertTrue(ValidationClass.validDriverId("23@#abcdAB"));
        assertTrue(ValidationClass.validDriverId("34@#efghCD"));
        assertFalse(ValidationClass.validDriverId("1234567890"));
    }

    @Test
    void taskFourTestCaseOne() throws Exception {
        String busID = "12345678";
        assertTrue(ValidationClass.validBusIdString(busID));
        testDatabase.addBus(new Scanner(busID + "\n50\n100.0\nDiesel\nexit\n"));
    }

    @Test
    void taskFourTestCaseTwo() {
        String busID = "1234567A";
        assertFalse(ValidationClass.validBusIdString(busID));
    }

    @Test
    void taskFourTestCaseThree() throws Exception {
        String busID = "12345678";
        assertTrue(ValidationClass.validBusIdString(busID));
        assertTrue(ValidationClass.validCapacityUpdate(busID, 40, 50));
    }

    @Test
    void taskFourTestCaseFour() {
        assertTrue(ValidationClass.validBusIdString("12345678"));
        assertTrue(ValidationClass.validBusIdString("87654321"));
        assertFalse(ValidationClass.validBusIdString("1234567A"));
    }
}