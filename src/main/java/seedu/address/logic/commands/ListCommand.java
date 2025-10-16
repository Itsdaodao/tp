package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.FLAG_ALPHABETICAL_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_RECENT_ORDER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_WORD_ALPHABETICAL = COMMAND_WORD + " " + FLAG_ALPHABETICAL_ORDER;
    public static final String COMMAND_WORD_RECENT = COMMAND_WORD + " " + FLAG_RECENT_ORDER;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " (original order)\n "
            + " -a (alphabetical order)\n"
            + " -r (recent order)";
    public static final String MESSAGE_INVALID_ORDER = "Kindly choose only one sorting order.";
    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_ALPHABETICAL_ORDER = MESSAGE_SUCCESS + " in alphabetical order";
    public static final String MESSAGE_SUCCESS_RECENT_ORDER = MESSAGE_SUCCESS + " in order of most recently added";

    /**
     * Represents the sorting order for listing persons.
     */
    public enum SortOrder {
        DEFAULT,
        ALPHABETICAL,
        RECENT
    }

    private final SortOrder sortOrder;

    /**
     * Creates a ListCommand to list all persons in the address book with the specified sorting order.
     *
     * @param sortOrder The sorting order to be applied when listing persons.
     */
    public ListCommand(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        switch (sortOrder) {
        case ALPHABETICAL:
            model.applyNameSort();
            return new CommandResult(MESSAGE_SUCCESS_ALPHABETICAL_ORDER);
        case RECENT:
            model.applyRecentSort();
            return new CommandResult(MESSAGE_SUCCESS_RECENT_ORDER);
        default:
            model.resetSortOrder();
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherListCommand = (ListCommand) other;
        return otherListCommand.sortOrder == this.sortOrder;
    }

    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Lists out all contacts",
                "Example: list -a"
        );
    }
}
