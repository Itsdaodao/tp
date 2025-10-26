package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class CommandHistoryManagerTest {
    @Test
    public void getPrevious_noHistory_returnsNull() {
        CommandHistoryManager chm = new CommandHistoryManager();
        assertNull(chm.getPreviousCommandFromHistory(""));
    }

    @Test
    public void getNext_noHistory_returnsNull() {
        CommandHistoryManager chm = new CommandHistoryManager();
        assertNull(chm.getPreviousCommandFromHistory(""));
    }

    @Test
    public void getPrevious_withHistory_returnsCommandsInOrder() {
        CommandHistoryManager chm = new CommandHistoryManager();
        chm.addCommandToHistory("first command");
        chm.addCommandToHistory("second command");
        chm.addCommandToHistory("third command");

        String command = chm.getPreviousCommandFromHistory("");
        assert command.equals("third command");

        command = chm.getPreviousCommandFromHistory("");
        assert command.equals("second command");

        command = chm.getPreviousCommandFromHistory("");
        assert command.equals("first command");
    }

    @Test
    public void getNext_withHistory_returnsCommandsInOrder() {
        CommandHistoryManager chm = new CommandHistoryManager();
        chm.addCommandToHistory("first command");
        chm.addCommandToHistory("second command");
        chm.addCommandToHistory("third command");

        // Navigate to the oldest command
        chm.getPreviousCommandFromHistory("");
        chm.getPreviousCommandFromHistory("");
        chm.getPreviousCommandFromHistory("");

        String command = chm.getNextCommandFromHistory();
        assert command.equals("second command");

        command = chm.getNextCommandFromHistory();
        assert command.equals("third command");

        command = chm.getNextCommandFromHistory();
        assert command.isEmpty();
    }

    @Test
    public void addCommand_exceedCapacity_oldestCommandRemoved() {
        CommandHistoryManager chm = new CommandHistoryManager();
        for (int i = 1; i <= 16; i++) {
            chm.addCommandToHistory("command " + i);
        }
        // The oldest command "command 1" should be removed
        for (int i = 16; i >= 2; i--) {
            String command = chm.getPreviousCommandFromHistory("");
            assert command.equals("command " + i);
        }
    }
}
