package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " (original order)\n "
            + " -a (alphabetical order)\n"
            + " -r (recent order)";
    public static final String MESSAGE_INVALID_ORDER = "Kindly choose only one sorting order.";
    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_ALPHABETICAL_ORDER = MESSAGE_SUCCESS + " in alphabetical order";
    public static final String MESSAGE_SUCCESS_RECENT_ORDER = MESSAGE_SUCCESS + " in order of most recently added";

    public enum SortOrder {
        DEFAULT,
        ALPHABETICAL,
        RECENT
    }

    private final SortOrder sortOrder;

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
}
