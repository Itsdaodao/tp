package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_emptyArg_returnsExportCommandWithDefaultPath() throws ParseException {
        ExportCommand command = parser.parse("");
        assertNotNull(command);
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void parse_whitespaceArg_returnsExportCommandWithDefaultPath() throws ParseException {
        ExportCommand command = parser.parse("   ");
        assertNotNull(command);
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void parse_validFilePath_returnsExportCommand() throws ParseException {
        String filePath = "mycontacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_filePathWithSpaces_returnsExportCommand() throws ParseException {
        String filePath = "my contacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_filePathWithLeadingAndTrailingSpaces_trimmedCorrectly() throws ParseException {
        String filePath = "  contacts.csv  ";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand("contacts.csv"), command);
    }

    @Test
    public void parse_absolutePath_returnsExportCommand() throws ParseException {
        String filePath = "/home/user/contacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_windowsPath_returnsExportCommand() throws ParseException {
        String filePath = "C:\\Users\\user\\contacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
    }

    @Test
    public void parse_relativePath_returnsExportCommand() throws ParseException {
        String filePath = "data/exports/contacts.csv";
        ExportCommand command = parser.parse(filePath);
        assertNotNull(command);
        assertEquals(new ExportCommand(filePath), command);
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
