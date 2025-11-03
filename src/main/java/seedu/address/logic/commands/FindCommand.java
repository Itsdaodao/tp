package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name or tag contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    public static final String EXAMPLE_MESSAGE =
            "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "alice bob charlie\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "family";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names or tags contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: "
            + PREFIX_NAME + "KEYWORD [MORE_KEYWORDS]... (to search by name)\n"
            + "            "
            + PREFIX_TAG + "KEYWORD [MORE_KEYWORDS]... (to search by tag)\n"
            + "Note: Only one prefix (" + PREFIX_NAME + " or " + PREFIX_TAG + ") can be used at a time\n"
            + EXAMPLE_MESSAGE;

    public static final String MESSAGE_MULTIPLE_PREFIXES_NOT_ALLOWED =
            "WARNING: Both prefixes provided. Only the first prefix is used.\n";

    private final Predicate<Person> predicate;
    private final boolean showWarning;

    /**
     * Constructs a FindCommand with the specified search criteria and warning flag
     *
     * @param predicate A condition to check which person to find
     * @param showWarning whether to show warning if no person match
     */
    public FindCommand(Predicate<Person> predicate, Boolean showWarning) {
        requireNonNull(predicate);
        requireNonNull(showWarning);
        this.predicate = predicate;
        this.showWarning = showWarning;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        assert predicate != null : "Predicate must not be null before filtering.";

        model.updateFilteredPersonList(predicate);
        String combinedMessage = showWarning
                ? MESSAGE_MULTIPLE_PREFIXES_NOT_ALLOWED + Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                : Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;

        return new CommandResult(
                String.format(combinedMessage, model.getSortedAndFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("showWarning", showWarning)
                .toString();
    }

    /**
     * Registers the find command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        String detailedUsage = String.format(
                "Usage: find %sKEYWORD [MORE_KEYWORDS]... find %sKEYWORD [MORE_KEYWORDS]...\n"
                        + "\n"
                        + "Finds all contacts whose names or tags contain any of the specified keywords "
                        + "(case-insensitive).\n"
                        + "\n"
                        + "Parameters:\n"
                        + "  PREFIX - Either '%s' to search by name or '%s' to search by tag\n"
                        + "  KEYWORD - One or more keywords to search for (required)\n"
                        + "\n"
                        + "Notes:\n"
                        + "  - Keywords must match the start of the word (e.g. 'De' matches 'Derek')"
                        + "  - Only one prefix can be used at a time\n"
                        + "  - The search is case-insensitive (e.g., 'alice' matches 'Alice')\n"
                        + "  - Only the name field or tag field is searched\n"
                        + "  - Contacts matching at least one keyword will be displayed\n"
                        + "  - If both prefixes are provided. Only the first prefix is used.\n",
                PREFIX_NAME,
                PREFIX_TAG,
                PREFIX_NAME,
                PREFIX_TAG
        );

        CommandRegistry.register(
                COMMAND_WORD,
                "Finds contacts whose names or tags contain any of the given keywords",
                EXAMPLE_MESSAGE,
                detailedUsage
        );
    }
}
