package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class GithubTest {

    @Test
    public void constructor_invalidGithub_throwsIllegalArgumentException() {
        String invalidUsername = "";
        assertThrows(IllegalArgumentException.class, () -> new Github(invalidUsername));
    }

    @Test
    public void isValidGithub() {
        // blank GitHub
        assertFalse(Github.isValidGithub("")); // empty string
        assertFalse(Github.isValidGithub(" ")); // spaces only

        // invalid length
        assertFalse(Github.isValidGithub("a".repeat(40))); // too long (40 chars)

        // invalid characters
        assertFalse(Github.isValidGithub("user name")); // space not allowed
        assertFalse(Github.isValidGithub("user_name")); // underscore not allowed
        assertFalse(Github.isValidGithub("user.name")); // period not allowed
        assertFalse(Github.isValidGithub("user+name")); // plus not allowed
        assertFalse(Github.isValidGithub("user@name")); // at symbol not allowed
        assertFalse(Github.isValidGithub("user/name")); // slash not allowed
        assertFalse(Github.isValidGithub("username!")); // special char not allowed
        assertFalse(Github.isValidGithub("-username")); // cannot start with hyphen
        assertFalse(Github.isValidGithub("username-")); // cannot end with hyphen
        assertFalse(Github.isValidGithub("user--name")); // cannot have consecutive hyphens

        // valid GitHub
        assertTrue(Github.isValidGithub("a")); // min length (1)
        assertTrue(Github.isValidGithub("abcde")); // letters only
        assertTrue(Github.isValidGithub("abcde1")); // letters + numbers
        assertTrue(Github.isValidGithub("user-name")); // single hyphen allowed
        assertTrue(Github.isValidGithub("UserName")); // uppercase allowed
        assertTrue(Github.isValidGithub("user123")); // alphanumeric
        assertTrue(Github.isValidGithub("a".repeat(39))); // max length (39)
        assertTrue(Github.isValidGithub("test-123-abc")); // valid combo

    }

    @Test
    public void isEmpty() {
        Github github = new Github();
        assertTrue(github.isEmpty());
    }

    @Test
    public void equals() {
        Github github = new Github("valid-username");
        Github emptyGithub = new Github();

        // same values -> returns true
        assertTrue(github.equals(new Github("valid-username")));

        // same null values -> returns true
        assertTrue(emptyGithub.equals(new Github(null)));

        // same object -> returns true
        assertTrue(github.equals(github));

        // null -> returns false
        assertFalse(github.equals(null));

        // different types -> returns false
        assertFalse(github.equals(5.0f));

        // different values -> returns false
        assertFalse(github.equals(new Github("other-username")));
    }

    @Test
    public void toString_null_returnsEmptyString() {
        Github github = new Github();
        assert(Objects.equals(github.toString(), ""));
    }

    @Test
    public void hashCode_null_returnsZero() {
        Github github = new Github();
        assert(Objects.equals(github.hashCode(), 0));
    }
}
