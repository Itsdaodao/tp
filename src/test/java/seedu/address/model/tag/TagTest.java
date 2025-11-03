package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("tag-name"));
        assertFalse(Tag.isValidTagName("tag_name"));
        assertFalse(Tag.isValidTagName("tag name"));
        assertFalse(Tag.isValidTagName("tag@name"));
        assertFalse(Tag.isValidTagName("tag.name"));
        assertFalse(Tag.isValidTagName("tag#name"));
        assertFalse(Tag.isValidTagName("tag!"));
        assertFalse(Tag.isValidTagName("A".repeat(129))); // boundary case

        // valid tag names
        assertTrue(Tag.isValidTagName("tag"));
        assertTrue(Tag.isValidTagName("MyNewTag"));
        assertTrue(Tag.isValidTagName("tag123"));
        assertTrue(Tag.isValidTagName("TAG"));
        assertTrue(Tag.isValidTagName("A".repeat(128))); // boundary case
    }

}
