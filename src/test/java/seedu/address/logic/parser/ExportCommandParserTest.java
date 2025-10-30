package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_emptyArg_returnsExportCommandWithDefaultFilename() throws ParseException {
        ExportCommand command = parser.parse("");
        assertNotNull(command);
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void parse_whitespaceArg_returnsExportCommandWithDefaultFilename() throws ParseException {
        ExportCommand command = parser.parse("   ");
        assertNotNull(command);
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void parse_validFilename_returnsExportCommand() throws ParseException {
        String filename = "mycontacts";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }


    @Test
    public void parse_validFilePath_returnsExportCommand() throws ParseException {
        String filePath = "mycontacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_filenameWithSpaces_returnsExportCommand() throws ParseException {
        String filename = "my contacts";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithLeadingAndTrailingSpaces_trimmedCorrectly() throws ParseException {
        String filename = "  contacts  ";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand("contacts"), command);
    }

    @Test
    public void parse_filenameWithUnderscores_returnsExportCommand() throws ParseException {
        String filename = "my_contacts";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithHyphens_returnsExportCommand() throws ParseException {
        String filename = "my-contacts";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithNumbers_returnsExportCommand() throws ParseException {
        String filename = "contacts2024";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithDots_returnsExportCommand() throws ParseException {
        String filename = "contacts.backup";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithCsvExtension_returnsExportCommand() throws ParseException {
        String filename = "contacts.csv";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithOtherExtension_returnsExportCommand() throws ParseException {
        // This is valid input - the extension will become part of the base name
        String filename = "contacts.pdf";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    // ==================== Invalid Input Tests ====================

    @Test
    public void parse_filenameWithForwardSlash_throwsParseException() {
        String filename = "folder/file";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("/"));
    }

    @Test
    public void parse_filenameWithBackslash_throwsParseException() {
        String filename = "folder\\file";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("\\"));
    }

    @Test
    public void parse_filenameWithColon_throwsParseException() {
        String filename = "file:name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains(":"));
    }

    @Test
    public void parse_filenameWithAsterisk_throwsParseException() {
        String filename = "file*name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("*"));
    }

    @Test
    public void parse_filenameWithQuestionMark_throwsParseException() {
        String filename = "file?name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("?"));
    }

    @Test
    public void parse_filenameWithQuotes_throwsParseException() {
        String filename = "file\"name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("\""));
    }

    @Test
    public void parse_filenameWithLessThan_throwsParseException() {
        String filename = "file<name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("<"));
    }

    @Test
    public void parse_filenameWithGreaterThan_throwsParseException() {
        String filename = "file>name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains(">"));
    }

    @Test
    public void parse_filenameWithPipe_throwsParseException() {
        String filename = "file|name";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
        assertTrue(exception.getMessage().contains("|"));
    }

    @Test
    public void parse_filenameOnlyDots_throwsParseException() {
        String filename = "...";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
    }

    @Test
    public void parse_filenameWithMultipleInvalidChars_throwsParseException() {
        String filename = "file/name*test";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(filename));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid filename"));
    }

    // ==================== Edge Cases ====================

    @Test
    public void parse_singleCharacterFilename_returnsExportCommand() throws ParseException {
        String filename = "a";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_longFilename_returnsExportCommand() throws ParseException {
        String filename = "very_long_filename_that_is_still_valid_123456789";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filenameWithMixedCase_preservesCase() throws ParseException {
        String filename = "MyContacts";
        ExportCommand command = parser.parse(filename);
        assertNotNull(command);
        assertEquals(new ExportCommand(filename), command);
    }

    @Test
    public void parse_filePathWithoutExtension_returnsExportCommand() throws ParseException {
        // Parser doesn't validate extension, just passes it through
        String filePath = "contacts";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_filePathWithDifferentExtension_returnsExportCommand() throws ParseException {
        // Parser doesn't validate extension, just passes it through
        String filePath = "contacts.txt";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }
}
