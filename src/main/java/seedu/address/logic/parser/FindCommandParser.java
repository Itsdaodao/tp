package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_NAME, PREFIX_TAG
                );

        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG);

        int nameIndex = args.indexOf(PREFIX_NAME.getPrefix());
        int tagIndex = args.indexOf(PREFIX_TAG.getPrefix());

        boolean hasNoPrefixesPresent = nameIndex == -1 && tagIndex == -1;
        boolean hasBothPrefixes = nameIndex != -1 && tagIndex != -1;
        boolean isNameBeforeTag = hasBothPrefixes && nameIndex < tagIndex;

        if (hasNoPrefixesPresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Prefix activePrefix;
        if (hasBothPrefixes) {
            // Both prefixes exist -> pick the one that appear first
            activePrefix = isNameBeforeTag ? PREFIX_NAME : PREFIX_TAG;
        } else {
            // Only one prefix exist
            activePrefix = nameIndex != -1 ? PREFIX_NAME : PREFIX_TAG;
        }

        String trimmedArgs = argMultimap.getValue(activePrefix).orElse("").trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");
        Predicate<Person> predicate = activePrefix.equals(PREFIX_NAME)
                ? new NameContainsKeywordsPredicate(Arrays.asList(keywords))
                : new TagContainsKeywordsPredicate(Arrays.asList(keywords));

        // Show warning if both prefixes were provided (only the first one will be used)
        boolean showWarning = hasBothPrefixes;

        return new FindCommand(predicate, showWarning);
    }
}
