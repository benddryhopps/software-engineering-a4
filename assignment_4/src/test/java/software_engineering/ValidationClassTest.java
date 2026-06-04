package software_engineering;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationClassTest {

    // ===========================
    // DRIVER ID VALIDATION TESTS
    // ===========================

    @Test
    // Normal case: valid driver ID should be accepted
    public void testValidDriverId_normal() {
        assertTrue(ValidationClass.validDriverId("24@@abcdXY"));
    }

    @Test
    // Invalid input: first two digits not between 2-9
    public void testValidDriverId_invalidFirstTwoDigits() {
        assertFalse(ValidationClass.validDriverId("12@@abcdXY"));
    }

    @Test
    // Edge case: only one special character, should fail
    public void testValidDriverId_edgeOneSpecialChar() {
        assertFalse(ValidationClass.validDriverId("24@abcdeXY"));
    }

    @Test
    // Normal case: valid bus ID, all digits, length 8
    public void testValidBusId_normal() {
        assertTrue(ValidationClass.validBusIdString("12345678"));
    }

    @Test
    // Invalid input: wrong length or contains letter
    public void testValidBusId_invalid() {
        assertFalse(ValidationClass.validBusIdString("1234567A"));
    }

    @Test
    // Normal case: valid DOB format
    public void testValidDOB_normal() {
        assertTrue(ValidationClass.validDOB("01-01-2000"));
    }

    @Test
    // Invalid input: uses slashes instead of hyphens
    public void testValidDOB_invalidSlashes() {
        assertFalse(ValidationClass.validDOB("01/01/2000"));
    }

    @Test
    // Edge case: DOB too short
    public void testValidDOB_edgeShort() {
        assertFalse(ValidationClass.validDOB("1-01-2000"));
        assertFalse(ValidationClass.validDOB("01-1-2000"));
    }

    @Test
    // Normal case: valid address with 5 sections
    public void testValidAddress_normal() {
        assertTrue(ValidationClass.validAddress("12|Main Street|Melbourne|VIC|Australia"));
    }

    @Test
    // Invalid input: missing a section
    public void testValidAddress_invalidMissingSection() {
        assertFalse(ValidationClass.validAddress("12|Main Street|Melbourne|VIC"));
    }

    @Test
    // Edge case: extra information after country
    public void testValidAddress_edgeExtraInfo() {
        assertFalse(ValidationClass.validAddress("12|Main Street|Melbourne|VIC|Australia|Extra"));
    }
}
