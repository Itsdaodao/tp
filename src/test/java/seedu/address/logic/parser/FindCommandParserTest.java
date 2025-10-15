package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_POPPY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_POPPY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_THOMAS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, VALID_NAME_POPPY, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothPrefixesNoDescription_throwsParseException() {
        assertParseFailure(parser, PREFIX_NAME + " " + PREFIX_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        // Invalid prefix (like p/)
        String userInput = " p/12345 " + NAME_DESC_AMY + TAG_DESC_FRIEND;
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        // Non-empty preamble before prefix
        String userInput = " someText " + NAME_DESC_AMY;
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameSingleArg_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_POPPY)), false);
        assertParseSuccess(parser, NAME_DESC_POPPY, expectedFindCommand);

        // with leading/trailing/multiple whitespace
        assertParseSuccess(parser, "   \n  " + NAME_DESC_POPPY + "\t ", expectedFindCommand);
    }

    @Test
    public void parse_validTagSingleArg_returnsFindCommand() {
        // single keyword: friend
        FindCommand expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList(VALID_TAG_FRIEND)), false);
        assertParseSuccess(parser, TAG_DESC_FRIEND, expectedFindCommand);

        // with leading/trailing/multiple whitespace
        assertParseSuccess(parser, "  \n  " + TAG_DESC_FRIEND + "   \t ", expectedFindCommand);
    }


    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // no leading/trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(
                        Arrays.asList(VALID_NAME_POPPY, VALID_NAME_THOMAS)), false);

        assertParseSuccess(parser, NAME_DESC_POPPY + " " + VALID_NAME_THOMAS, expectedFindCommand);

        // multiple whitespaces and newlines between keywords
        assertParseSuccess(parser, " \n" + NAME_DESC_POPPY + "\n \t" + VALID_NAME_THOMAS, expectedFindCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand = new FindCommand(
                new TagContainsKeywordsPredicate(Arrays.asList(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)), false);
        assertParseSuccess(parser, TAG_DESC_FRIEND + " " + VALID_TAG_HUSBAND, expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n " + TAG_DESC_FRIEND + "\n \t" + VALID_TAG_HUSBAND+ "\t",expectedFindCommand);
    }

    @Test
    public void parse_bothPrefixes_nameBeforeTag_successWithWarning() {
        FindCommand expectedCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_POPPY)), true);
        assertParseSuccess(parser, NAME_DESC_POPPY + TAG_DESC_FRIEND, expectedCommand);
    }

    @Test
    public void parse_bothPrefixes_tagBeforeName_successWithWarning() {
        FindCommand expectedCommand = new FindCommand(
                new TagContainsKeywordsPredicate(Arrays.asList(VALID_TAG_FRIEND)), true);
        assertParseSuccess(parser, TAG_DESC_FRIEND + NAME_DESC_POPPY, expectedCommand);
    }
}
