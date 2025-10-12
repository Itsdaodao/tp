package seedu.address.logic.commands;

import java.util.function.Supplier;

/**
 * Represents a command that requires confirmation from the user before execution.
 */
public class ConfirmationPendingResult extends CommandResult {

    /** Contains logic to execute on user confirmation.**/
    private final Supplier<CommandResult> onConfirm;

    /**
     * Constructs a {@code ConfirmationPendingResult} with the specified fields.
     *
     * @param feedbackToUser The confirmation message to be shown to the user.
     * @param showHelp Whether help information should be shown to the user.
     * @param exit Whether the application should exit.
     * @param onConfirm The logic to execute when the user confirms to proceed with the action.
     */
    public ConfirmationPendingResult(String feedbackToUser, boolean showHelp, boolean exit,
                                     Supplier<CommandResult> onConfirm) {
        super(feedbackToUser, showHelp, exit);
        this.onConfirm = onConfirm;
    }

    /**
     * Executes the command logic
     * @return CommandResult after execution.
     */
    public CommandResult executeOnConfirm() {
        return onConfirm.get();
    }
}
