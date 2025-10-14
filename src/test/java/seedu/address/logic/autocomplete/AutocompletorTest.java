package seedu.address.logic.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;

public class AutocompletorTest {
    @Test
    public void getHint_partialCommand_returnsFullCommand() {
        Autocompletor completor = new Autocompletor();
        String command = AddCommand.COMMAND_WORD
                .substring(0, AddCommand.COMMAND_WORD.length() - 1);

        String hint = completor.getHint(command);

        assertEquals(AddCommand.COMMAND_WORD, hint);
    }

    @Test
    public void getHint_emptyCommand_returnsEmpty() {
        Autocompletor completor = new Autocompletor();
        String command = "";

        String hint = completor.getHint(command);

        assertEquals("", hint);
    }

    @Test
    public void getHint_noMatchingCommand_returnsEmpty() {
        Autocompletor completor = new Autocompletor();
        String command = "cellsinterlinkedwithincellsinterlinked";

        String hint = completor.getHint(command);

        assertEquals("", hint);
    }
}
