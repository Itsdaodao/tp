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

    public static final String MESSAGE_USAGE = COMMAND_WORD + " (original order)\n"
            + "  " + FLAG_ALPHABETICAL_ORDER + " (alphabetical order)\n"
            + "  " + FLAG_RECENT_ORDER + " (recent order)";

    public static final String MESSAGE_INVALID_ORDER = "Kindly choose only one sorting order.";
    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_ALPHABETICAL_ORDER = MESSAGE_SUCCESS + " in alphabetical order";
    public static final String MESSAGE_SUCCESS_RECENT_ORDER = MESSAGE_SUCCESS + " in order of most recently added";

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

        sortOrder.applySort(model);
        return new CommandResult(sortOrder.getMessage());
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

    /**
     * Registers the list command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Lists all contacts in the address book with optional sorting",
                "Example: list\n"
                        + "            Example: list -a\n"
                        + "            Example: list -r",
                "Usage: list [-a | -r]\n\n"
                        + "Lists all contacts in the address book with optional sorting.\n\n"
                        + "Parameters:\n"
                        + "  -a (optional) - Sort contacts in alphabetical order by name\n"
                        + "  -r (optional) - Sort contacts by most recently added\n\n"
                        + "Notes:\n"
                        + "  - Without flags, contacts are listed in their default order\n"
                        + "  - Only one sorting flag can be used at a time"
        );
    }


    /**
     * Represents the available sorting orders for listing persons in the address book.
     * Each enum constant encapsulates the specific logic for applying the sort
     * to the {@code Model} and retrieving the corresponding success message.
     */
    public enum SortOrder {
        /**
         * Represents the default listing order (earliest to latest, as per internalList).
         */
        DEFAULT {
            @Override
            public String getMessage() { return MESSAGE_SUCCESS; }
            @Override
            public void applySort(Model model) { model.resetSortOrder(); }
        },

        /**
         * Represents sorting the persons in alphabetical order by name.
         */
        ALPHABETICAL {
            @Override
            public String getMessage() { return MESSAGE_SUCCESS_ALPHABETICAL_ORDER; }
            @Override
            public void applySort(Model model) { model.applyNameSort(); }
        },

        /**
         * Represents sorting the persons in order of most recently added.
         */
        RECENT {
            @Override
            public String getMessage() { return MESSAGE_SUCCESS_RECENT_ORDER; }
            @Override
            public void applySort(Model model) { model.applyRecentSort(); }
        };

        /**
         * Retrieves the success message specific to this sorting order.
         *
         * @return The formatted success message string.
         */
        public abstract String getMessage();

        /**
         * Applies the required sorting mechanism to the model's person list.
         *
         * @param model The {@code Model} to which the sorting action will be applied.
         */
        public abstract void applySort(Model model);
    }
}
