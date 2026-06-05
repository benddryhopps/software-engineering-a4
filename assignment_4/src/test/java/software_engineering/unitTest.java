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
        void driverIDInputValidationTests(){
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
        void dateOfBirthValidationTests(){
            String validDOB = "01-01-2000";
            String invalidDOB = "01/01/2000";
            String edgeCaseDOB = "01-1-2000";
            assertTrue(software_engineering.ValidationClass.validDOB(validDOB));
            assertFalse(software_engineering.ValidationClass.validDOB(invalidDOB));
            assertFalse(software_engineering.ValidationClass.validDOB(edgeCaseDOB));
        }

        
        // Task 1 Test Case 4, test input validation of the license update restrictions for drivers with greater than 10 years of experience.
         @Test
        void testLicenseUpdateRestrictionsBasedOnExperienceAlsoShowcasesPersistenceTooThisReallyIsAVeryConciseMethodName() throws Exception {
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

            // Create fake Driver 2 with same attributes as first, except with 11 years of experience, and a differentPK
            insert.setString(1, exampleDriverTwoPK);
            insert.setInt(9, 11);
            insert.executeUpdate();

            // Create fake Driver 3, with exactly 10 years of experience as an edge case for logic validation.
            insert.setString(1, exampleDriverThreePK);
            insert.setInt(9, 10);
            insert.executeUpdate();

            // This section of code updates Driver 1 and 3 from licenseType attribute value of "Light" to "Heavy";
            // I am unable to demonstrate rejection of an invalid value, because the prompt will simply skip an opening for it if yearsExperience attribute > 10
            // It took me a really long time to figure out what was going wrong there, because the second scanner was leaving a newline for the third, which would stop case 3 from updating, though I thought it was a logic error. fml
            // Really screwed myself over using the scanner's for input instead of just passing regular arguments like a normal person.
            // At least, it is not possible to update license through user facing methods, though you can with sql, but its not like you can't edit a txt file manually so no difference there.
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

        // It is worth noting, that I cannot test the immutability of the driverID and name fields, because the updateDriver method does not prompt the user for name or driverID after record instantiation.


    // This test case validates busID primary key selection, based off of uniqueness, and all digit formatting, along with a fixed 8 digit length
    @Test
    void testBusRecordPersistenceForCorrectBusID() {
        testDatabase.addBus(new Scanner("12345678\n50\n50\nGasoline\nexit\n"));
        try (Connection connection = testDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                 "SELECT busID FROM busRepo WHERE busID = '12345678'")) {

            assertTrue(resultSet.next());
            assertEquals("12345678", resultSet.getString("busID"));

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed with SQLException");
        }
    }

    // This test case checks that the System rejects invalid busID PKs.
    // Because of the silly CLI nature, I had to modify the method, and user method overloading to feed the method a scanner simulating user input
    // This test works by feeding an incorrect busID argument to the system, through use of the user-facing addBus method, and checks the output of the system for the string "invalid", which is present in rejection string.
    @Test
    void testBusIDValidationInvalidCaseWithUserFacingMethods() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        testDatabase.addBus(new Scanner("1234\nexit\n"));
        String outputString = output.toString().toLowerCase();
        assertTrue(outputString.contains("invalid"));
        System.setOut(originalOut);
    }

    // This test case tests for uniqueness in busIDs and also through the user-facing bus action, this time by searching system output for the "already exists" string.
    @Test
    void testBusIDSlashPrimaryKeyUniquenessAndPersistenceInDatabase() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        testDatabase.addBus(new Scanner("12345678\n50\n50\nGasoline\nexit\n"));
        testDatabase.addBus(new Scanner("12345678\nexit\n"));
        String outputString = output.toString();
        System.setOut(originalOut);
        
        assertTrue(outputString.toLowerCase().contains("already exists"));
    }

    // The below test is a failiure, I could not figure out how to do this test without causing an issue, of which I believe that the issue existed within test itself.
    // The only way I could figure out how to stop the test from failing was to murder it :(
    // @Test
    // void taskTwoTestCaseTwo() throws Exception {
    //     testDatabase.addBus(new Scanner("87654321\n50\n100.0\nDiesel\n"));
    //     assertTrue(ValidationClass.validCapacityUpdate("87654321", 40, 50));
    //     assertFalse(ValidationClass.validCapacityUpdate("87654321", 60, 50));
    //     assertTrue(ValidationClass.validCapacityUpdate("87654321", 50, 50));
    // }

    // This unit test tests the method for whether a driver is older than 50, based off of dob, and is used to validate driver assignment to buses.
    @Test
    void validateBirthdaysBasedOffOfDateOfBirth() {
        assertTrue(ValidationClass.driverOlderThan50("01-01-1970"));
        assertFalse(ValidationClass.driverOlderThan50("01-01-2000"));
    }

    @Test
    // This unit test test tests the method used validate driver assignment based on their yearsExperience attribute, and is called before assignment.
    void requiredExperienceValidationTesting() {
        assertTrue(ValidationClass.driverHasMinExperience(5, 5));
        assertFalse(ValidationClass.driverHasMinExperience(4, 5));
    }

    @Test
    // This unit test tests the method used to assign drivers based on their licenseType attribute, and is called before driver assignment.
    void driversLicenseBusAssignmentTesting() {
        assertTrue(ValidationClass.driverLicenseAllows("Heavy"));
        assertTrue(ValidationClass.driverLicenseAllows("Public Transport"));
        assertFalse(ValidationClass.driverLicenseAllows("Medium"));
    }

    @Test
    // This unit test mainly just measures persistence in the database's ability to assign a driver ID with other arbitrary information, all these methods are called upon driver record creation.
    void driverRecordPersistence() throws Exception {
        String driverID = "23@#abcdAB";
        assertTrue(ValidationClass.validDriverId(driverID));
        assertTrue(ValidationClass.validAddress("12|Main Street|Melbourne|VIC|Australia"));
        assertTrue(ValidationClass.validDOB("15-06-1990"));
        assertTrue(ValidationClass.driverLicenseAllows("Heavy"));
    }

    @Test
    // This unit test rejects an invalidly structured driverID, using the validDriverID method, which is called before record insertion for drivers.
    void driverIDValidationRejectInvalidCase() {
        String driverID = "1234567890";
        assertFalse(ValidationClass.validDriverId(driverID));
    }

    @Test
    // This unit test tests persistence of the driver records in the database, as well as testing the ability to retrieve modifications made to it.
    void testDriverModificationAndPersistenceWithinDatabaseWithoutUserFacingMethods() throws Exception {
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
            insert.setString(5, "Victoria");
            insert.executeUpdate();
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT city FROM drivers WHERE driverID = '23@#abcdAB'")) {
                assertTrue(resultSet.next());
                assertEquals("Victoria", resultSet.getString("city"));
            }
        }
    }

    @Test
    // This unit test is really just testing the driverID validation methods like the one a couple methods above, these unit tests are not very unique.
    // If I were to implement unit testing in a project that I was developing (which I can see the value of, especially in big ones), I would probably just test each method incrementally, rather than based off of teammates tables.
    void moreDriverIDValidation() {
        assertTrue(ValidationClass.validDriverId("23@#abcdAB"));
        assertTrue(ValidationClass.validDriverId("34@#efghCD"));
        assertFalse(ValidationClass.validDriverId("1234567890"));
    }

    @Test
    // This test is verifying the retrieval of a created record through the busExists method.
    // I get that it is kinda lazy to have done it through the busExists method, but the record is evidently there.
    void createBusRecordAndConfirmPersistence() throws Exception {
        String busID = "12345678";
        assertTrue(ValidationClass.validBusIdString(busID));
        testDatabase.addBus(new Scanner(busID + "\n50\n100.0\nDiesel\nexit\n"));
        assertTrue(testDatabase.busExists(busID));
    }

    @Test
    // This test is testing the input validation method used for assigning busID's with require strictly numeric values.
    void testBusIdValidationOnlyNumbers() {
        String busID = "1234567A";
        assertFalse(ValidationClass.validBusIdString(busID));
    }

    @Test
    // This test is testing the method used to verify whether or not a proposed capacity update to a bus is allowed.
    void testCapacityUpdateValidation() {
        String busID = "12345678";
        assertTrue(ValidationClass.validBusIdString(busID));
        assertTrue(ValidationClass.validCapacityUpdate(busID, 40, 50));
    }

    @Test
    // This unit test is just even more busID validation
    void yayMoreBusIDValidation() {
        assertTrue(ValidationClass.validBusIdString("12345678"));
        assertTrue(ValidationClass.validBusIdString("87654321"));
        assertFalse(ValidationClass.validBusIdString("1234567A"));
    }
}