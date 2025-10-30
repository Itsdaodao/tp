package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, VALID_NAME_POPPY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothPrefixesNoDescription_throwsParseException() {
        assertParseFailure(parser, PREFIX_NAME + " " + PREFIX_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        // Invalid prefix (like p/)
        String userInput = " p'\'12345 " + NAME_DESC_AMY + TAG_DESC_FRIEND;
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        // Non-empty preamble before prefix
        String userInput = " someText " + NAME_DESC_AMY;
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyKeywordList_throwsParseException() {
        String input = " n'\' "; // empty name prefix argument
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void extractKeywords_emptyKeyword_throwsParseException() {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize("n/   ", CliSyntax.PREFIX_NAME);
        String args = "n/   ";

        assertThrows(ParseException.class, () ->
                parser.extractKeywords(argMultimap, PREFIX_NAME, args)
        );
    }

    @Test
    public void buildPredicate_namePrefix_returnsNamePredicate() {
        String[] keywords = {"Alice", "Bob"};
        Predicate<Person> predicate = parser.buildPredicate(PREFIX_NAME, keywords);
        assertTrue(predicate instanceof NameContainsKeywordsPredicate);
    }

    @Test
    public void buildPredicate_tagPrefix_returnsTagPredicate() {
        String[] keywords = {"friends", "colleagues"};
        Predicate<Person> predicate = parser.buildPredicate(PREFIX_TAG, keywords);
        assertTrue(predicate instanceof TagContainsKeywordsPredicate);
    }

    @Test
    public void buildPredicate_unknownPrefix_returnsNull() {
        Prefix unknownPrefix = new Prefix("x/");
        String[] keywords = {"irrelevant"};
        Predicate<Person> predicate = parser.buildPredicate(unknownPrefix, keywords);
        assertNull(predicate);
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
        assertParseSuccess(parser, " \n " + TAG_DESC_FRIEND + "\n \t"
                + VALID_TAG_HUSBAND + "\t", expectedFindCommand);
    }

    @Test
    public void parse_nameBeforeTag_successWithWarning() {
        FindCommand expectedCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_POPPY)), true);
        assertParseSuccess(parser, NAME_DESC_POPPY + TAG_DESC_FRIEND, expectedCommand);
    }

    @Test
    public void parse_tagBeforeName_successWithWarning() {
        FindCommand expectedCommand = new FindCommand(
                new TagContainsKeywordsPredicate(Arrays.asList(VALID_TAG_FRIEND)), true);
        assertParseSuccess(parser, TAG_DESC_FRIEND + NAME_DESC_POPPY, expectedCommand);
    }
}
