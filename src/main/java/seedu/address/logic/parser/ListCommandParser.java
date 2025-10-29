package seedu.address.logic.parser;

import static seedu.address.logic.commands.ListCommand.MESSAGE_INVALID_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_ALPHABETICAL_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_RECENT_ORDER;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListCommand.SortOrder;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, FLAG_ALPHABETICAL_ORDER, FLAG_RECENT_ORDER
                );

        boolean alphabeticalFlag = argMultimap.getValue(FLAG_ALPHABETICAL_ORDER).isPresent();
        boolean recentFlag = argMultimap.getValue(FLAG_RECENT_ORDER).isPresent();
        boolean isBothFlagPresent = alphabeticalFlag && recentFlag;

        if (isBothFlagPresent) {
            throw new ParseException(MESSAGE_INVALID_ORDER);
        }

        if (alphabeticalFlag) {
            return new ListCommand(SortOrder.ALPHABETICAL);
        } else if (recentFlag) {
            return new ListCommand(SortOrder.RECENT);
        } else {
            return new ListCommand(SortOrder.DEFAULT);
        }
    }
}
