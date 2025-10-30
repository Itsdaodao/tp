package seedu.address.model.person;

import java.time.Instant;
import java.util.Optional;

/**
 * Represents the pin status of a Person in the address book.
 */
public class PinStatus {

    public static final String PIN_DATE_MESSAGE_CONSTRAINT =
            "Invalid date time format. \n"
                    + "pinnedAt must follow the example date time format below \n"
                    + "Format: <yyyy-MM-dd>T<hr:mm:ss>Z \n"
                    + "Example: 2025-10-22T07:00:17.036469800Z";

    private final Optional<Instant> pinnedAt;

    /**
     * Constructs a new PinStatus representing an unpinned state.
     * The underlying Instant is set to Optional.empty().
     */
    public PinStatus() {
        this.pinnedAt = Optional.empty();
    }

    /**
     * Constructs a new PinStatus representing a pinned state.
     * The underlying Instant is set to the provided value, wrapped in an Optional.
     * If the provided pinnedAt is null, the resulting PinStatus will also be unpinned (Optional.empty()).
     *
     * @param pinnedAt The Instant when the person was pinned. Can be null.
     */
    public PinStatus(Instant pinnedAt) {
        this.pinnedAt = Optional.ofNullable(pinnedAt);
    }

    /**
     * Checks if the person associated with this PinStatus is currently pinned.
     *
     * @return true if an Instant is present (i.e., the person is pinned), false otherwise.
     */
    public Boolean isPinned() {
        return pinnedAt.isPresent();
    }


    public Optional<Instant> getPinnedAt() {
        return pinnedAt;
    }

    @Override
    public String toString() {
        return pinnedAt.toString();
    }

    /**
     * Returns true if both PinStatus have same Instant value.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PinStatus)) {
            return false;
        }

        PinStatus otherPinStatus = (PinStatus) other;
        return pinnedAt.equals(otherPinStatus.pinnedAt);
    }

    @Override
    public int hashCode() {
        return pinnedAt.hashCode();
    }
}
