package seedu.address.logic.commands;

import java.util.function.Supplier;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class ConfirmCommand extends Command {
    private final String input;
    private final Supplier<CommandResult> onConfirm;
    private final Supplier<CommandResult> onCancel;
    private final String confirmationMessage;

    public ConfirmCommand(String input,
                          Supplier<CommandResult> onConfirm,
                          Supplier<CommandResult> onCancel,
                          String confirmationMessage) {
        this.input = input;
        this.confirmationMessage = confirmationMessage;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (input.equals("y")) {
            return onConfirm.get();
        } else if (input.equals("n")) {
            return onCancel.get();
        } else {
            throw new CommandException("Invalid input.\n" + confirmationMessage);
        }
    }
}
