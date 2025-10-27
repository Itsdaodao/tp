package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.logic.autocomplete.Autocompletor;
import seedu.address.logic.commands.CommandRegistry;
import seedu.address.logic.commands.LaunchCommand;

@ExtendWith(ApplicationExtension.class)
public class CommandBoxTest {
    @TempDir
    public Path temporaryFolder;

    @Start
    private void start(Stage stage) {
        CommandRegistry.initialize();

        // Create the command box
        CommandBox commandBox = new CommandBox(
            commandText -> null,
            new Autocompletor()
        );

        Scene scene = new Scene(commandBox.getRoot());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void fillAutocomplete_withNoSuggestions_preservesUserInput(FxRobot robot) {
        String contents = "this command will have no autocomplete suggestions because its too long";
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        robot.write(contents);

        robot.push(CommandBox.FILL_AUTOCOMPLETE);

        assertEquals(contents, t.getText());
    }

    @Test
    public void fillAutocomplete_withSuggestion_updatesCommandTextFieldToSuggestionAndSetsCaretPosToEnd(FxRobot robot) {
        String expected = LaunchCommand.COMMAND_WORD;
        String contents = expected.substring(0, expected.length() - 1);
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        robot.write(contents);

        robot.push(CommandBox.FILL_AUTOCOMPLETE);

        assertEquals(expected, t.getText());
        assertEquals(expected.length(), t.getCaretPosition());
    }
}
