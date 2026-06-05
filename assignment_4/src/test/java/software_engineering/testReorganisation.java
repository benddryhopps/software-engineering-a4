package software_engineering;

import software_engineering.ValidationClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class testReorganisation {

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
        @Test
        void taskOneTestCaseTwo(){
            String validAddress = "12|Main Street|Melbourne|VIC|Australia";
            String invalidAddress = "12|Main Street|Melbourne|VIC";
            String edgeCaseAddress = "12|Main Street|Melbourne|VIC|amongus???";
            assertTrue(software_engineering.ValidationClass.validAddress(validAddress));
            assertFalse(software_engineering.ValidationClass.validAddress(invalidAddress));
            assertFalse(software_engineering.ValidationClass.validAddress(edgeCaseAddress));
        }

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
        void taskOneTestCaseFour(){
        }
    
    }
        
