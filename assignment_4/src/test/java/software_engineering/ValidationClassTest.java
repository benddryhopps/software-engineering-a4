package software_engineering;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ValidationClassTest {

    // Driver ID tests

    @Test
    void validDriverId_acceptsValidId() {
        String driverID = "24@@abcdXY";
        assertTrue(ValidationClass.validDriverId(driverID));
    }

    @Test
    void validDriverId_rejectsShortId() {
        String driverID = "24@abcXY";
        assertFalse(ValidationClass.validDriverId(driverID));
    }

    @Test
    void validDriverId_rejectsLongId() {
        String driverID = "24@@abcdXYZ";
        assertFalse(ValidationClass.validDriverId(driverID));
    }

    @Test
    void validDriverId_rejectsInvalidEndUppercase() {
        String driverID = "24@@abcdXy";
        assertFalse(ValidationClass.validDriverId(driverID));
    }

    // BusID tests.

    @Test
    void validBusId_acceptsValid8DigitId() {
        String busID = "12345678";
        assertTrue(ValidationClass.validBusIdString(busID));
    }

    @Test
    void validBusId_rejectsNonDigitId() {
        String busID = "1234A678";
        assertFalse(ValidationClass.validBusIdString(busID));
    }

    @Test
    void validBusId_rejectsShortId() {
        String busID = "1234567";
        assertFalse(ValidationClass.validBusIdString(busID));
    }

    @Test
    void validBusId_rejectsLongId() {
        String busID = "123456789";
        assertFalse(ValidationClass.validBusIdString(busID));
    }

    // Date of birth input validation tests

    @Test
    void validDOB_acceptsCorrectFormat() {
        String dob = "01-01-2000";
        assertTrue(ValidationClass.validDOB(dob));
    }

    @Test
    void validDOB_rejectsWrongSeparator() {
        String dob = "01/01/2000";
        assertFalse(ValidationClass.validDOB(dob));
    }

    @Test
    void validDOB_rejectsShortDate() {
        String dob = "1-01-2000";
        assertFalse(ValidationClass.validDOB(dob));
    }

    @Test
    void validDOB_rejectsLongDate() {
        String dob = "01-01-20000";
        assertFalse(ValidationClass.validDOB(dob));
    }

    // Address input validation tests

    @Test
    void validAddress_acceptsCorrectFormat() {
        String address = "12|Main Street|Melbourne|VIC|Australia";
        assertTrue(ValidationClass.validAddress(address));
    }

    @Test
    void validAddress_rejectsTooFewFields() {
        String address = "12|Main Street|Melbourne|VIC";
        assertFalse(ValidationClass.validAddress(address));
    }

    @Test
    void validAddress_rejectsTooManyFields() {
        String address = "12|Main Street|Melbourne|VIC|Australia|Extra";
        assertFalse(ValidationClass.validAddress(address));
    }

    // capacity update tests.

    @Test
    void capacityUpdate_allowsDecrease() {
        assertTrue(ValidationClass.validCapacityUpdate("12345678", 40, 50));
    }

    @Test
    void capacityUpdate_allowsSame() {
        assertTrue(ValidationClass.validCapacityUpdate("12345678", 50, 50));
    }

    @Test
    void capacityUpdate_rejectsIncrease() {
        assertFalse(ValidationClass.validCapacityUpdate("12345678", 60, 50));
    }

    // driver age tests - through date of birth

    @Test
    void driverOlderThan50_rejectsBusCapacity50OrMore() {
        assertTrue(ValidationClass.driverOlderThan50("01-01-1970")); // depends on current year
    }

    @Test
    void driverOlderThan50_acceptsYounger() {
        assertFalse(ValidationClass.driverOlderThan50("01-01-2000"));
    }

    // tests done on yearsExperience

    @Test
    void driverHasMinExperience_acceptsMinYears() {
        assertTrue(ValidationClass.driverHasMinExperience(5, 5));
    }

    @Test
    void driverHasMinExperience_rejectsBelowMin() {
        assertFalse(ValidationClass.driverHasMinExperience(4, 5));
    }

    // Licensing type tests

    @Test
    void driverLicenseAllows_acceptsHeavy() {
        assertTrue(ValidationClass.driverLicenseAllows("Heavy"));
    }

    @Test
    void driverLicenseAllows_acceptsPublicTransport() {
        assertTrue(ValidationClass.driverLicenseAllows("Public Transport"));
    }

    @Test
    void driverLicenseAllows_rejectsOther() {
        assertFalse(ValidationClass.driverLicenseAllows("Medium"));
    }

}