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
    public void clearAwaitingUserConfirmation() {
        this.awaitingUserConfirmation = false;
        this.pendingOperation = null;
    }

    @Override
    public ConfirmationPendingResult getPendingOperation() {
        if (!this.awaitingUserConfirmation){
            return null;
        }
        return pendingOperation;
    }
}
