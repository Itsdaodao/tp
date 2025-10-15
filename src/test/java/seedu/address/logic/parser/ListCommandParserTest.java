package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.FLAG_ALPHABETICAL_ORDER;
import static seedu.address.logic.parser.CliSyntax.FLAG_RECENT_ORDER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListCommand.SortOrder;

public class ListCommandParserTest {
    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noArgs_returnsDefaultOrderListCommand() {
        ListCommand expectedCommand = new ListCommand(SortOrder.DEFAULT);
        assertParseSuccess(parser, ListCommand.COMMAND_WORD, expectedCommand);
    }

    @Test
    public void parse_withNonFlagArg_returnsDefaultOrderListCommand() {
        ListCommand expectedCommand = new ListCommand(SortOrder.DEFAULT);
        assertParseSuccess(parser, ListCommand.COMMAND_WORD + NAME_DESC_AMY, expectedCommand);
    }

    @Test
    public void parse_withAlphabeticalArg_returnsAlphabeticalOrderListCommand() {
        ListCommand expectedCommand = new ListCommand(SortOrder.ALPHABETICAL);
        assertParseSuccess(parser, ListCommand.COMMAND_WORD_ALPHABETICAL, expectedCommand);
    }

    @Test
    public void parse_withRecentArg_returnsRecentOrderListCommand() {
        ListCommand expectedCommand = new ListCommand(SortOrder.RECENT);
        assertParseSuccess(parser, ListCommand.COMMAND_WORD_RECENT, expectedCommand);
    }

    @Test
    public void parse_withAlphabeticalAndRecentArgs_failure() {
        assertParseFailure(
                parser,
                ListCommand.COMMAND_WORD + " " + FLAG_ALPHABETICAL_ORDER + " " + FLAG_RECENT_ORDER,
                ListCommand.MESSAGE_INVALID_ORDER
        );
    }
}
