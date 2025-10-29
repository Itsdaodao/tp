package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private final Logger logger = Logger.getLogger(FindCommandParser.class.getName());
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        logger.fine("Starting FindCommand parse with args: " + args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);

        validatePreamble(argMultimap, args);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG);

        // Determine which prefix is active (if both exist, pick the first)
        Prefix activePrefix = determineActivePrefix(argMultimap, args);

        String[] keywords = extractKeywords(argMultimap, activePrefix, args);

        // Create the appropriate predicate (name-based or tag-based)
        Predicate<Person> predicate = buildPredicate(activePrefix, keywords);

        assert predicate != null : "Predicate should not be null after parsing.";

        // Show warning if both prefixes were provided (only the first one will be used)
        boolean showWarning = bothPrefixesPresent(argMultimap);

        logger.fine("FindCommand successfully parsed. Active prefix: " + activePrefix
                + ", showWarning=" + showWarning
                + ", keywords=" + Arrays.toString(keywords));

        return new FindCommand(predicate, showWarning);
    }

    /**
     * Checks that the command does not have any text before the first prefix
     */
    private void validatePreamble(ArgumentMultimap argMultimap, String args) throws ParseException {
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            logger.warning("Invalid command format: non-empty preamble detected. Args: " + args);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Determines which prefix (name or tag) is active based on their order of appearance.
     */
    private Prefix determineActivePrefix(ArgumentMultimap argMultimap, String args) throws ParseException {
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasTagPrefix = argMultimap.getValue(PREFIX_TAG).isPresent();
        boolean hasNoPrefixesPresent = !hasNamePrefix && !hasTagPrefix;
        boolean hasBothPrefixes = hasNamePrefix && hasTagPrefix;

        if (hasNoPrefixesPresent) {
            logger.warning("No prefixes found in FindCommand input: " + args);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Both prefixes exist: Choose the one that appear first
        if (hasBothPrefixes) {
            int nameIndex = args.indexOf(PREFIX_NAME.getPrefix());
            int tagIndex = args.indexOf(PREFIX_TAG.getPrefix());
            boolean isNameBeforeTag = nameIndex < tagIndex;
            return isNameBeforeTag ? PREFIX_NAME : PREFIX_TAG;
        }

        // Only one prefix exist
        return hasNamePrefix ? PREFIX_NAME : PREFIX_TAG;
    }

    /**
     * Extracts and validates keywords for the given prefix.
     */
    // package private for testing
    String[] extractKeywords(ArgumentMultimap argMultimap, Prefix prefix, String args) throws ParseException {
        String trimmedArgs = argMultimap.getValue(prefix).orElse("").trim();

        if (trimmedArgs.isEmpty()) {
            logger.warning("Empty keyword list detected for prefix: " + prefix + ". Args: " + args);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return trimmedArgs.split("\\s+");
    }

    /**
     * Builds the correct predicate depending on the prefix.
     */
    // Package private for testing
    Predicate<Person> buildPredicate(Prefix prefix, String[] keywords) {
        if (prefix.equals(PREFIX_NAME)) {
            return new NameContainsKeywordsPredicate(Arrays.asList(keywords));
        } else if (prefix.equals(PREFIX_TAG)) {
            return new TagContainsKeywordsPredicate(Arrays.asList(keywords));
        } else {
            logger.severe("Unexpected prefix encountered in buildPredicate: " + prefix);
            return null;
        }
    }

    /**
     * Checks whether both prefixes were present (used to show warning).
     */
    private boolean bothPrefixesPresent(ArgumentMultimap argMultimap) {
        return argMultimap.getValue(PREFIX_NAME).isPresent()
                && argMultimap.getValue(PREFIX_TAG).isPresent();
    }
}
