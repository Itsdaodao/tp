package seedu.address.logic;

import seedu.address.logic.commands.ConfirmationPendingResult;

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
