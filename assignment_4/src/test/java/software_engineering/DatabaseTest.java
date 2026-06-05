package software_engineering;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.*;
import org.junit.jupiter.api.*;

public class DatabaseTest {

    // TEMPORARY TESTING DB

    @BeforeEach
    void setup() {
        Database db = new Database();
        db.createDriverRepository();
        db.createBusRepository();
}
    Database db = new Database();

    private Connection getConn() throws SQLException {
        return db.connect();
    }

    private void clearTables() throws SQLException {
        try (Connection conn = getConn();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM drivers");
            stmt.execute("DELETE FROM busRepo");
        }
    }

    // DRIVER TESTS

    @Test
    void driverInsert_persistsCorrectly() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO drivers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, "24@@abcdXY");
            ps.setString(2, "John Smith");
            ps.setString(3, "12");
            ps.setString(4, "Main St");
            ps.setString(5, "Melbourne");
            ps.setString(6, "VIC");
            ps.setString(7, "Australia");
            ps.setString(8, "01-01-1990");
            ps.setInt(9, 5);
            ps.setString(10, "Heavy");
            ps.executeUpdate();
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM drivers WHERE driverID=?")) {

            ps.setString(1, "24@@abcdXY");
            ResultSet rs = ps.executeQuery();

            assertTrue(rs.next());
            assertEquals("John Smith", rs.getString("name"));
        }
    }

    @Test
    void driverDelete_removesDriver() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO drivers VALUES ('24@@abcdXY','John','12','Main','Melb','VIC','AUS','01-01-1990',5,'Heavy')");
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM drivers WHERE driverID=?")) {

            ps.setString(1, "24@@abcdXY");
            ps.executeUpdate();
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM drivers WHERE driverID=?")) {

            ps.setString(1, "24@@abcdXY");
            ResultSet rs = ps.executeQuery();

            assertFalse(rs.next());
        }
    }

    // @Test
    // void driverCount_returnsCorrectNumber() throws Exception {
    //     clearTables();

    //     try (Connection conn = getConn();
    //          Statement stmt = conn.createStatement()) {

    //         stmt.execute("INSERT INTO drivers VALUES ('D1','A','1','S','C','S','A','01-01-1990',5,'Heavy')");
    //     }

    //     try (Connection conn = getConn();
    //          Statement stmt = conn.createStatement();
    //          ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM drivers")) {

    //         assertTrue(rs.next());
    //         assertEquals(2, rs.getInt(1));
    //     }
    // }

    // =================================================
    // BUS TESTS
    // =================================================

    @Test
    void busInsert_persistsCorrectly() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO busRepo VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, "12345678");
            ps.setInt(2, 50);
            ps.setDouble(3, 80.0);
            ps.setString(4, "Diesel");
            ps.setString(5, null);

            ps.executeUpdate();
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM busRepo WHERE busId=?")) {

            ps.setString(1, "12345678");
            ResultSet rs = ps.executeQuery();

            assertTrue(rs.next());
            assertEquals(50, rs.getInt("capacity"));
        }
    }

    @Test
    void busDelete_removesBus() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             Statement stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO busRepo VALUES ('12345678',50,80,'Diesel',NULL)");
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM busRepo WHERE busId=?")) {

            ps.setString(1, "12345678");
            ps.executeUpdate();
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM busRepo WHERE busId=?")) {

            ps.setString(1, "12345678");
            ResultSet rs = ps.executeQuery();

            assertFalse(rs.next());
        }
    }

    @Test
    void busCount_returnsCorrectNumber() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             Statement stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO busRepo VALUES ('B1',50,80,'Diesel',NULL)");
            stmt.execute("INSERT INTO busRepo VALUES ('B2',30,90,'Electricity',NULL)");
        }

        try (Connection conn = getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM busRepo")) {

            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
        }
    }

    // =================================================
    // CAPACITY UPDATE RULE
    // =================================================

    @Test
    void capacity_update_decrease_allowed() throws Exception {
        assertTrue(ValidationClass.validCapacityUpdate("12345678", 40, 50));
    }

    @Test
    void capacity_update_increase_rejected() throws Exception {
        assertFalse(ValidationClass.validCapacityUpdate("12345678", 60, 50));
    }

    // =================================================
    // DRIVER ASSIGNMENT RULE (CORE LOGIC TEST)
    // =================================================

    @Test
    void assignDriver_fails_when_driver_missing() throws Exception {
        clearTables();

        try (Connection conn = getConn();
             Statement stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO busRepo VALUES ('12345678',50,80,'Diesel',NULL)");
        }

        boolean exists;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT driverID FROM drivers WHERE driverID='NOTEXIST'")) {

            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        }

        assertFalse(exists);
    }
}