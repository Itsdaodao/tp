package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_MULTIPLE_PREFIXES_NOT_ALLOWED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_NAME, PREFIX_TAG
                );

        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasTagPrefix = argMultimap.getValue(PREFIX_TAG).isPresent();

        Prefix activePrefix = hasNamePrefix ? PREFIX_NAME
                : hasTagPrefix ? PREFIX_TAG
                : null;

        if (activePrefix == null) { // no prefix specified
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        } else if (hasNamePrefix && hasTagPrefix) { // Disallow using both prefixes together
            throw new ParseException(
                    String.format(MESSAGE_MULTIPLE_PREFIXES_NOT_ALLOWED, FindCommand.MESSAGE_USAGE));
        }

        String trimmedArgs = argMultimap.getValue(activePrefix).orElse("").trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");
        Predicate<Person> predicate = hasNamePrefix
                ? new NameContainsKeywordsPredicate(Arrays.asList(keywords))
                : new TagContainsKeywordsPredicate(Arrays.asList(keywords));

        return new FindCommand(predicate);
    }
}
