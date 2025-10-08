package seedu.address.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationPendingResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * API of the State component
 */
public interface State {
    /**
     * Returns true if the app is currently waiting for user confirmation.
     */
    boolean isAwaitingUserConfirmation();

    /**
     * Returns the operation that is pending user confirmation.
     * @return the pending operation, or null if no operation is pending.
     */
    ConfirmationPendingResult getPendingOperation();

    /**
     * Clears the state to reflect that the app is no longer waiting for user confirmation.
     */
    void clearAwaitingUserConfirmation();

    /**
     * Updates state to reflect that the app is waiting for user confirmation.
     * @param pendingOperation the operation that is pending confirmation.
     */
    void setAwaitingUserConfirmation(ConfirmationPendingResult pendingOperation);
}
