package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationPendingResult;

public class StateManagerTest {
    @Test
    public void getPendingOperation_notAwaitingConfirmation_returnsNull() {
        StateManager stateManager = new StateManager();

        assertEquals(null, stateManager.getPendingOperation());
    }

    @Test
    public void getPendingOperation_awaitingConfirmation_returnsPendingOperation() {
        StateManager stateManager = new StateManager();
        ConfirmationPendingResult mockPendingOperation = new ConfirmationPendingResult(
                "Confirm?", false, false, () -> {}, new CommandResult("Done!")
        );
        stateManager.setAwaitingUserConfirmation(mockPendingOperation);

        assertEquals(mockPendingOperation, stateManager.getPendingOperation());
    }

    @Test
    public void isAwaitingUserConfirmation_initialState_returnsFalse() {
        StateManager stateManager = new StateManager();
        assertFalse(stateManager.isAwaitingUserConfirmation());
    }

    @Test
    public void isAwaitingUserConfirmation_afterSettingPendingOperation_returnsTrue() {
        StateManager stateManager = new StateManager();
        ConfirmationPendingResult mockPendingOperation = new ConfirmationPendingResult(
                "Confirm?", false, false, () -> {}, new CommandResult("Done!")
        );
        stateManager.setAwaitingUserConfirmation(mockPendingOperation);
        assertTrue(stateManager.isAwaitingUserConfirmation());
    }

    @Test
    public void clearAwaitingUserConfirmation_afterSettingPendingOperation_resetsState() {
        StateManager stateManager = new StateManager();
        ConfirmationPendingResult mockPendingOperation = new ConfirmationPendingResult(
                "Confirm?", false, false, () -> {}, new CommandResult("Done!")
        );
        stateManager.setAwaitingUserConfirmation(mockPendingOperation);
        stateManager.clearAwaitingUserConfirmation();

        assertFalse(stateManager.isAwaitingUserConfirmation());
    }
}
