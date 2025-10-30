package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class PinStatusTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2025-10-22T07:00:17.036469800Z");

    @Test
    public void constructor_default_nothingPinned() {
        PinStatus pinStatus = new PinStatus();
        assertFalse(pinStatus.isPinned());
        assertEquals(Optional.empty(), pinStatus.getPinnedAt());
    }

    @Test
    public void constructor_withInstant_pinnedCorrectly() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertTrue(pinStatus.isPinned());
        assertEquals(Optional.of(FIXED_INSTANT), pinStatus.getPinnedAt());
    }

    @Test
    public void constructor_withNullInstant_nothingPinned() {
        PinStatus pinStatus = new PinStatus(null);
        assertFalse(pinStatus.isPinned());
        assertEquals(Optional.empty(), pinStatus.getPinnedAt());
    }

    @Test
    public void isPinned_pinned_returnsTrue() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertTrue(pinStatus.isPinned());
    }

    @Test
    public void isPinned_unpinned_returnsFalse() {
        PinStatus pinStatus = new PinStatus();
        assertFalse(pinStatus.isPinned());
    }

    @Test
    public void getPinnedAt_pinned_returnsCorrectInstant() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertEquals(Optional.of(FIXED_INSTANT), pinStatus.getPinnedAt());
    }

    @Test
    public void getPinnedAt_unpinned_returnsEmptyOptional() {
        PinStatus pinStatus = new PinStatus();
        assertEquals(Optional.empty(), pinStatus.getPinnedAt());
    }

    @Test
    public void toString_pinned_returnsInstantString() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertEquals(Optional.of(FIXED_INSTANT).toString(), pinStatus.toString());
    }

    @Test
    public void toString_unpinned_returnsEmptyOptionalString() {
        PinStatus pinStatus = new PinStatus();
        assertEquals(Optional.empty().toString(), pinStatus.toString());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertTrue(pinStatus.equals(pinStatus));
    }

    @Test
    public void equals_null_returnsFalse() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertFalse(pinStatus.equals(null));
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        PinStatus pinStatus = new PinStatus(FIXED_INSTANT);
        assertFalse(pinStatus.equals("some string"));
    }

    @Test
    public void equals_bothUnpinned_returnsTrue() {
        PinStatus pinStatus1 = new PinStatus();
        PinStatus pinStatus2 = new PinStatus();
        assertTrue(pinStatus1.equals(pinStatus2));
        assertTrue(pinStatus2.equals(pinStatus1));
    }

    @Test
    public void equals_bothPinnedWithSameInstant_returnsTrue() {
        PinStatus pinStatus1 = new PinStatus(FIXED_INSTANT);
        PinStatus pinStatus2 = new PinStatus(FIXED_INSTANT);
        assertTrue(pinStatus1.equals(pinStatus2));
        assertTrue(pinStatus2.equals(pinStatus1));
    }

    @Test
    public void equals_onePinnedOneUnpinned_returnsFalse() {
        PinStatus pinnedStatus = new PinStatus(FIXED_INSTANT);
        PinStatus unpinnedStatus = new PinStatus();
        assertFalse(pinnedStatus.equals(unpinnedStatus));
        assertFalse(unpinnedStatus.equals(pinnedStatus));
    }

    @Test
    public void equals_bothPinnedWithDifferentInstant_returnsFalse() {
        Instant instant1 = FIXED_INSTANT;
        Instant instant2 = FIXED_INSTANT.plusSeconds(1);
        PinStatus pinStatus1 = new PinStatus(instant1);
        PinStatus pinStatus2 = new PinStatus(instant2);
        assertFalse(pinStatus1.equals(pinStatus2));
        assertFalse(pinStatus2.equals(pinStatus1));
    }

    @Test
    public void hashCode_sameUnpinnedObjects_equal() {
        PinStatus pinStatus1 = new PinStatus();
        PinStatus pinStatus2 = new PinStatus();
        assertEquals(pinStatus1.hashCode(), pinStatus2.hashCode());
    }

    @Test
    public void hashCode_samePinnedObjects_equal() {
        PinStatus pinStatus1 = new PinStatus(FIXED_INSTANT);
        PinStatus pinStatus2 = new PinStatus(FIXED_INSTANT);
        assertEquals(pinStatus1.hashCode(), pinStatus2.hashCode());
    }

    @Test
    public void hashCode_differentPinnedObjects_notEqual() {
        Instant instant1 = FIXED_INSTANT;
        Instant instant2 = FIXED_INSTANT.plusSeconds(1);
        PinStatus pinStatus1 = new PinStatus(instant1);
        PinStatus pinStatus2 = new PinStatus(instant2);
        assertNotEquals(pinStatus1.hashCode(), pinStatus2.hashCode());
    }
}
