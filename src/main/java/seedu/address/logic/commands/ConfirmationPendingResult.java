package seedu.address.logic.commands;

import java.util.function.Supplier;

/**
 * Represents a command that requires confirmation from the user before execution.
 */
public class ConfirmationPendingResult extends CommandResult{

    /** Contains logic to execute on user confirmation.**/
    private final Supplier<CommandResult> onConfirm;

    public ConfirmationPendingResult(String feedbackToUser, boolean showHelp, boolean exit,
                                     Supplier<CommandResult> onConfirm) {
        super(feedbackToUser, showHelp, exit);
        this.onConfirm = onConfirm;
    }

    /** Executes the command logic
     * @return CommandResult after execution.
     */
    public CommandResult executeOnConfirm() {
        return onConfirm.get();
    }
}
