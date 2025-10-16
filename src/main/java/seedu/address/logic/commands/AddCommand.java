package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_TELEGRAM + "TELEGRAM "
            + PREFIX_GITHUB + "GITHUB "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_TELEGRAM + "imjohndoe23 "
            + PREFIX_GITHUB + "john-doe23 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as AddCommand modifies the address book
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

    /**
     * Registers the add command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Adds a contact to the address book",
                "Example: add n/John Doe p/98765432 e/johnd@example.com l/@johndoe g/john-doe t/friends",
                "Usage: add n/NAME p/PHONE [e/EMAIL] [l/TELEGRAM] [g/GITHUB] [t/TAG]...\n\n"
                        + "Adds a new contact to the address book with the specified details.\n\n"
                        + "Parameters:\n"
                        + "  n/NAME      - Full name of the contact (required)\n"
                        + "  p/PHONE     - Phone number (required)\n"
                        + "  e/EMAIL     - Email address (optional)\n"
                        + "  l/TELEGRAM  - Telegram username with @ prefix (optional)\n"
                        + "  g/GITHUB    - GitHub username (optional)\n"
                        + "  t/TAG       - Tag(s) to categorize the contact (optional, can have multiple)\n\n"
                        + "Notes:\n"
                        + "  - Name and phone are required fields\n"
                        + "  - You can add multiple tags by repeating t/TAG\n"
                        + "  - Duplicate contacts (same name) are not allowed"
        );
    }
}
