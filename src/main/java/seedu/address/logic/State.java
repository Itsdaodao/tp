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
     * Executes the pending operation.
     * Calling this method will update the state to no longer be awaiting user confirmation.
     * @return the result of previously-pending operation execution.
     */
    CommandResult executePendingOperation();

    /**
     * Cancels the pending operation.
     * Calling this method will update the state to no longer be awaiting user confirmation.
     * @return Command Result indicating that the operation was cancelled.
     */
    CommandResult cancelPendingOperation();

    /**
     * Returns the confirmation message for the pending operation.
     */
    String getConfirmationMessage();

    /**
     * Updates state to reflect that the app is waiting for user confirmation.
     * @param pendingOperation the operation that is pending confirmation.
     */
    void setAwaitingUserConfirmation(ConfirmationPendingResult pendingOperation);
}
