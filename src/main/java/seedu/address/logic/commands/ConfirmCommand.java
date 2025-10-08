package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that confirms a pending operation.
 */
public class ConfirmCommand extends Command {
    private final String input;
    private final Runnable onComplete;
    private final ConfirmationPendingResult pendingOperation;

    public static String MESSAGE_OPERATION_CANCELLED = "Operation cancelled.";

    public static String MESSAGE_INVALID_CONFIRMATION_INPUT = "Invalid Input.\n%s";

    /**
     * Creates a ConfirmCommand to confirm a pending operation.
     * @param input the user input, any string.
     * @param onComplete a callback to run after the operation is confirmed or cancelled. Used to update state.
     * @param pendingOperation the operation that is pending confirmation.
     */
    public ConfirmCommand(String input,
                          Runnable onComplete,
                          ConfirmationPendingResult pendingOperation) {
        this.input = input;
        this.pendingOperation = pendingOperation;
        this.onComplete = onComplete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (input.equals("y")) {
            CommandResult res = pendingOperation.executeOnConfirm();
            onComplete.run();
            return res;
        } else if (input.equals("n")) {
            onComplete.run();
            return new CommandResult(MESSAGE_OPERATION_CANCELLED);
        } else {
            throw new CommandException(
                    String.format(MESSAGE_INVALID_CONFIRMATION_INPUT, pendingOperation.getFeedbackToUser())
            );
        }
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as ConfirmCommand may modify the model if the user confirms.
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("pendingOperation", pendingOperation)
                .add("userInput", input)
                .toString();
    }
}
