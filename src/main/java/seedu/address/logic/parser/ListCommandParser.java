package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.FLAG_ALPHABETICAL_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_RECENT_ORDER;
import static seedu.address.logic.commands.ListCommand.MESSAGE_INVALID_ORDER;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListCommand.SortOrder;

public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, FLAG_ALPHABETICAL_ORDER, FLAG_RECENT_ORDER
                );

        boolean alphabeticalFlag = argMultimap.getValue(FLAG_ALPHABETICAL_ORDER).isPresent();
        boolean recentFlag = argMultimap.getValue(FLAG_RECENT_ORDER).isPresent();

        if (alphabeticalFlag && recentFlag) {
            throw new ParseException(MESSAGE_INVALID_ORDER);
        }

        if (argMultimap.getValue(FLAG_ALPHABETICAL_ORDER).isPresent()) {
            return new ListCommand(SortOrder.ALPHABETICAL);
        } else if (argMultimap.getValue(FLAG_RECENT_ORDER).isPresent()) {
            return new ListCommand(SortOrder.RECENT);
        }

        return new ListCommand(SortOrder.DEFAULT);
    }
}
