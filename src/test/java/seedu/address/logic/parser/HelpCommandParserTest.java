package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Unit tests for {@link HelpCommandParser}.
 */
public class HelpCommandParserTest {

    private final HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_emptyArgs_returnsGeneralHelpCommand() {
        assertParseSuccess(parser, "", new HelpCommand());
        assertParseSuccess(parser, "   ", new HelpCommand());
    }

    @Test
    public void parse_singleWordCommand_returnsSpecificHelpCommand() {
        assertParseSuccess(parser, "add", new HelpCommand("add"));
        assertParseSuccess(parser, "list", new HelpCommand("list"));
        assertParseSuccess(parser, "delete", new HelpCommand("delete"));
    }

    @Test
    public void parse_singleWordCommandWithWhitespace_returnsSpecificHelpCommand() {
        assertParseSuccess(parser, "  add  ", new HelpCommand("add"));
        assertParseSuccess(parser, "  list  ", new HelpCommand("list"));
        assertParseSuccess(parser, "\tdelete\t", new HelpCommand("delete"));
    }

    @Test
    public void parse_commandWithLeadingDash_returnsSpecificHelpCommand() {
        assertParseSuccess(parser, "-add", new HelpCommand("add"));
        assertParseSuccess(parser, "-list", new HelpCommand("list"));
        assertParseSuccess(parser, "  -delete  ", new HelpCommand("delete"));
    }

    @Test
    public void parse_commandWithMultipleDashes_removesOnlyFirstDash() {
        assertParseSuccess(parser, "--add", new HelpCommand("-add"));
        assertParseSuccess(parser, "---list", new HelpCommand("--list"));
    }

    @Test
    public void parse_commandNameConvertedToLowerCase() {
        assertParseSuccess(parser, "ADD", new HelpCommand("add"));
        assertParseSuccess(parser, "List", new HelpCommand("list"));
        assertParseSuccess(parser, "DeLeTe", new HelpCommand("delete"));
        assertParseSuccess(parser, "-ADD", new HelpCommand("add"));
    }

    @Test
    public void parse_multipleWords_throwsParseException() {
        String expectedMessage = "Invalid command format. Please specify only one command name.\n"
                + HelpCommand.MESSAGE_USAGE;

        assertParseThrows(parser, "add command", expectedMessage);
        assertParseThrows(parser, "list all", expectedMessage);
        assertParseThrows(parser, "help me", expectedMessage);
        assertParseThrows(parser, "multiple words here", expectedMessage);
    }

    @Test
    public void parse_onlyDash_throwsParseException() {
        String expectedMessage = "Invalid command format. Please specify a command name after the dash.\n"
                + HelpCommand.MESSAGE_USAGE;

        assertParseThrows(parser, "-", expectedMessage);
        assertParseThrows(parser, "  -  ", expectedMessage);
    }

    @Test
    public void parse_dashWithSpaces_throwsParseException() {
        String expectedMessage = "Invalid command format. Please specify a command name after the dash.\n"
                + HelpCommand.MESSAGE_USAGE;

        assertParseThrows(parser, "- ", expectedMessage);
        assertParseThrows(parser, "  -   ", expectedMessage);
    }

    @Test
    public void parse_whitespaceOnlyAfterDash_throwsParseException() {
        String expectedMessage = "Invalid command format. Please specify a command name after the dash.\n"
                + HelpCommand.MESSAGE_USAGE;

        assertParseThrows(parser, "-   ", expectedMessage);
    }

    @Test
    public void parse_complexCommandNames_success() {
        // Test with command names that have numbers and mixed case
        assertParseSuccess(parser, "command123", new HelpCommand("command123"));
        assertParseSuccess(parser, "cmd-v2", new HelpCommand("cmd-v2"));
        assertParseSuccess(parser, "test_command", new HelpCommand("test_command"));
        assertParseSuccess(parser, "UPPERCASE", new HelpCommand("uppercase"));
    }

    @Test
    public void parse_edgeCaseCommandNames_success() {
        // Test with single character command names
        assertParseSuccess(parser, "a", new HelpCommand("a"));
        assertParseSuccess(parser, "-a", new HelpCommand("a"));

        // Test with command names that start with dash (after removing first dash)
        assertParseSuccess(parser, "--help", new HelpCommand("-help"));
    }

    /**
     * Helper method to assert that parsing throws a ParseException with the expected message.
     */
    private void assertParseThrows(HelpCommandParser parser, String args, String expectedMessage) {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
