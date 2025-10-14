package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.FLAG_ALPHABETICAL_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_RECENT_ORDER;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListCommand.SortOrder;

public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, FLAG_ALPHABETICAL_ORDER, FLAG_RECENT_ORDER
                );

        if (argMultimap.getValue(FLAG_ALPHABETICAL_ORDER).isPresent()) {
            return new ListCommand(SortOrder.ALPHABETICAL);
        } else if (argMultimap.getValue(FLAG_RECENT_ORDER).isPresent()) {
            return new ListCommand(SortOrder.RECENT);
        }

        return new ListCommand(SortOrder.DEFAULT);
    }
}
