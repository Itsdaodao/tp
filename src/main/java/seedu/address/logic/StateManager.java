package seedu.address.logic;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationPendingResult;

/**
 * The state manager of the app.
 * Tracks whether the app is awaiting user confirmation for a previously-requested operation.
 */
public class StateManager implements State {
    private boolean awaitingUserConfirmation;
    private ConfirmationPendingResult pendingOperation;

    /**
     * Constructs {@code State}. Initializes to not awaiting user confirmation.
     */
    public StateManager() {
        this.awaitingUserConfirmation = false;
        this.pendingOperation = null;
    }

    @Override
    public boolean isAwaitingUserConfirmation(){
        return awaitingUserConfirmation;
    }

    @Override
    public void setAwaitingUserConfirmation(ConfirmationPendingResult pendingOperation) {
        this.awaitingUserConfirmation = true;
        this.pendingOperation = pendingOperation;
    }

    @Override
    public String getConfirmationMessage() {
        if (awaitingUserConfirmation && pendingOperation != null) {
            return pendingOperation.getFeedbackToUser();
        } else {
            throw new RuntimeException("No pending confirmation!");
        }
    }

    @Override
    public CommandResult executePendingOperation() {
        if (awaitingUserConfirmation && pendingOperation != null) {
            CommandResult res = pendingOperation.executeOnConfirm();
            awaitingUserConfirmation = false;
            pendingOperation = null;
            return res;
        } else {
            throw new RuntimeException("No operation to execute!");
        }
    }

    @Override
    public CommandResult cancelPendingOperation() {
        if (awaitingUserConfirmation) {
            awaitingUserConfirmation = false;
            pendingOperation = null;
            return new CommandResult("Operation cancelled.");
        } else {
            throw new RuntimeException("No operation to cancel!");
        }
    }
}
