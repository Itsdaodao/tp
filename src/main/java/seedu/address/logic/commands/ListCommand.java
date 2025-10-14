package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String ALPHABETICAL_ORDER = " in alphabetical order";
    public static final String RECENT_ORDER = " in order of most recently added";

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
            model.sortFilteredPersonListByName();
            return new CommandResult(MESSAGE_SUCCESS + ALPHABETICAL_ORDER);
        case RECENT:
            model.sortFilteredPersonListByRecentlyAdded();
            return new CommandResult(MESSAGE_SUCCESS + RECENT_ORDER);
        default:
            model.resetSortOrder();
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
