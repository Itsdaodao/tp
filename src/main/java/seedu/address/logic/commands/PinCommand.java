package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Updates the status of existing person as pinned
 */
public class PinCommand extends Command {

    public static final String COMMAND_WORD = "pin";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Pins the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_PIN_PERSON_SUCCESS = "Pinned: %1$s";

    public static final String MESSAGE_PERSON_ALREADY_PINNED = "Person is already pinned.";

    private final Index targetIndex;

    public PinCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedAndFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToPin = lastShownList.get(targetIndex.getZeroBased());
        Person pinnedPerson = personToPin.pin();

        assert pinnedPerson.isPinned() : "Person is not pinned";
        assert pinnedPerson.getPinnedAt().isPresent() : "PinnedAt time is not found";

        if (personToPin.equals(pinnedPerson)) {
            // No changes, person already pinned
            throw new CommandException(MESSAGE_PERSON_ALREADY_PINNED);
        }

        model.setPerson(personToPin, pinnedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
                String.format(MESSAGE_PIN_PERSON_SUCCESS, Messages.format(pinnedPerson))
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PinCommand)) {
            return false;
        }

        PinCommand otherPinCommand = (PinCommand) other;
        return targetIndex.equals(otherPinCommand.targetIndex);
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as PinCommand modifies the address book
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    /**
     * Registers the pin command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Pins a contact by index number",
                "Example: pin 1",
                "Usage: pin INDEX\n\n"
                        + "Pins the contact at the specified index from the address book.\n\n"
                        + "Parameters:\n"
                        + "  INDEX - The index number shown in the displayed contact list (required)\n\n"
                        + "Notes:\n"
                        + "  - The index must be a positive integer (1, 2, 3, ...)\n"
                        + "  - The index refers to the contact's position in the currently displayed list"
        );
    }
}
