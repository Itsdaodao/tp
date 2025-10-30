package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class PreferredCommunicationModeTest {

    @Test
    public void isValidMode_nullOrBlank_returnsTrue() {
        Set<PreferredCommunicationMode> allModes = EnumSet.allOf(PreferredCommunicationMode.class);
        assertTrue(PreferredCommunicationMode.isValidMode(null, allModes, false));
        assertTrue(PreferredCommunicationMode.isValidMode("", allModes, false));
        assertTrue(PreferredCommunicationMode.isValidMode("   ", allModes, false));
    }

    @Test
    public void isValidMode_allModesAvailable_validModesAccepted() {
        Set<PreferredCommunicationMode> allModes = EnumSet.allOf(PreferredCommunicationMode.class);

        assertTrue(PreferredCommunicationMode.isValidMode("PHONE", allModes, false));
        assertTrue(PreferredCommunicationMode.isValidMode("email", allModes, false));
        assertTrue(PreferredCommunicationMode.isValidMode("TeLeGrAm", allModes, false));
    }

    @Test
    public void isValidMode_invalidModes_returnsFalse() {
        Set<PreferredCommunicationMode> allModes = EnumSet.allOf(PreferredCommunicationMode.class);
        assertFalse(PreferredCommunicationMode.isValidMode("whatsapp", allModes, false));
        assertFalse(PreferredCommunicationMode.isValidMode("123", allModes, false));
        assertFalse(PreferredCommunicationMode.isValidMode("phone@", allModes, false));
    }

    @Test
    public void isValidMode_limitedAvailableModes_onlyThoseValid() {
        Set<PreferredCommunicationMode> available = EnumSet.of(
                PreferredCommunicationMode.EMAIL, PreferredCommunicationMode.TELEGRAM
        );

        assertTrue(PreferredCommunicationMode.isValidMode("EMAIL", available, false));
        assertTrue(PreferredCommunicationMode.isValidMode("telegram", available, false));
        assertFalse(PreferredCommunicationMode.isValidMode("PHONE", available, false));
    }

    @Test
    public void isValidMode_allowNone_true() {
        Set<PreferredCommunicationMode> available = EnumSet.of(PreferredCommunicationMode.EMAIL);
        assertTrue(PreferredCommunicationMode.isValidMode("NONE", available, true));
    }

    @Test
    public void isValidMode_allowNone_false() {
        Set<PreferredCommunicationMode> available = EnumSet.of(PreferredCommunicationMode.EMAIL);
        assertFalse(PreferredCommunicationMode.isValidMode("NONE", available, false));
    }

    @Test
    public void isValidMode_availableModesNull_allEnumsValid() {
        assertTrue(PreferredCommunicationMode.isValidMode("PHONE", null, false));
        assertTrue(PreferredCommunicationMode.isValidMode("email", null, false));
        assertFalse(PreferredCommunicationMode.isValidMode("slack", null, false));
    }

    @Test
    public void of_nullOrBlank_returnsNone() {
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of(null));
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of(""));
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of("   "));
    }

    @Test
    public void of_validInputs_returnsCorrectEnum() {
        assertEquals(PreferredCommunicationMode.PHONE, PreferredCommunicationMode.of("phone"));
        assertEquals(PreferredCommunicationMode.EMAIL, PreferredCommunicationMode.of("EMAIL"));
        assertEquals(PreferredCommunicationMode.TELEGRAM, PreferredCommunicationMode.of("telegram"));
    }

    @Test
    public void of_invalidInput_returnsNone() {
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of("slack"));
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of("123"));
    }

    @Test
    public void of_explicitNone_returnsNone() {
        assertEquals(PreferredCommunicationMode.NONE, PreferredCommunicationMode.of("NONE"));
    }

    @Test
    public void isEmpty_checksCorrectly() {
        assertTrue(PreferredCommunicationMode.NONE.isEmpty());
        assertFalse(PreferredCommunicationMode.EMAIL.isEmpty());
    }

    @Test
    public void toString_returnsExpectedFormat() {
        assertEquals("phone", PreferredCommunicationMode.PHONE.toString());
        assertEquals("email", PreferredCommunicationMode.EMAIL.toString());
        assertEquals("", PreferredCommunicationMode.NONE.toString());
    }

    @Test
    public void roundTrip_ofToString_returnsSameMode() {
        for (PreferredCommunicationMode mode : PreferredCommunicationMode.values()) {
            if (mode != PreferredCommunicationMode.NONE) {
                String modeString = mode.toString();
                assertEquals(mode, PreferredCommunicationMode.of(modeString));
            }
        }
    }
}
