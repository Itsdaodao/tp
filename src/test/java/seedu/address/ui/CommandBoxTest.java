package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
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
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

@ExtendWith(ApplicationExtension.class)
public class CommandBoxTest {
    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();

    CommandHistoryManager chm = new CommandHistoryManager();
    CommandBox cb;

    @Start
    private void start(Stage stage) {
        CommandRegistry.initialize();

        // Create the MainWindow
        cb = new CommandBox(
                new CommandExecutorStub(),
                new Autocompletor(),
                chm
        );

        Scene scene = new Scene(cb.getRoot());
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        chm.clearHistory();
    }

    @Test
    public void goPreviousCommand_onEmptyHistory_doesNotChangeCommandBoxContents(FxRobot robot) {
        String contents = "some command that i'm currently typing...";
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        t.setText(contents);

        robot.clickOn("#commandTextField");
        robot.push(CommandBox.GO_PREVIOUS_COMMAND);

        assertEquals(contents, t.getText());
    }

    @Test
    public void goNextCommand_onEmptyHistory_doesNotChangeCommandBoxContents(FxRobot robot) {
        String contents = "some command that i'm currently typing...";
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        t.setText(contents);

        robot.clickOn("#commandTextField");
        robot.push(CommandBox.GO_NEXT_COMMAND);

        assertEquals(contents, t.getText());
    }

    @Test
    public void goPreviousCommand_onNonEmptyHistory_updatesCommandBoxContents(FxRobot robot) {
        String firstCommand = "first command";
        chm.addCommandToHistory(firstCommand);
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);

        robot.clickOn("#commandTextField");
        robot.push(CommandBox.GO_PREVIOUS_COMMAND);

        assertEquals(firstCommand, t.getText());
    }

    @Test
    public void goPreviousCommandMultipleTimes_onNonEmptyHistory_updatesCommandBoxContents(FxRobot robot) {
        String secondCommand = "second command";
        String firstCommand = "first command";
        chm.addCommandToHistory(firstCommand);
        chm.addCommandToHistory(secondCommand);
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        robot.clickOn("#commandTextField");

        for (int i = 0; i < 5; i++){
            robot.push(CommandBox.GO_PREVIOUS_COMMAND);
        }

        assertEquals(firstCommand, t.getText());
    }

    @Test
    public void goNextCommand_onNonEmptyHistory_updatesCommandBoxContents(FxRobot robot) {
        String secondCommand = "second command";
        String firstCommand = "first command";
        chm.addCommandToHistory(firstCommand);
        chm.addCommandToHistory(secondCommand);
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        robot.clickOn("#commandTextField");
        robot.push(CommandBox.GO_PREVIOUS_COMMAND);
        robot.push(CommandBox.GO_PREVIOUS_COMMAND);
        // Ensure command box is set to the oldest command in the history
        assertEquals(firstCommand, t.getText());

        robot.push(CommandBox.GO_NEXT_COMMAND);

        assertEquals(secondCommand, t.getText());
    }

    @Test
    public void goNextCommand_onNonEmptyHistory_preservesUserInput(FxRobot robot) {
        String userInput = "I was typing this command before I accidentally pressed the up arrow!";
        String firstCommand = "first command";
        chm.addCommandToHistory(firstCommand);
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        t.setText(userInput);
        robot.clickOn("#commandTextField");
        robot.push(CommandBox.GO_PREVIOUS_COMMAND);
        // Ensure command box is set to the old command
        assertEquals(firstCommand, t.getText());

        robot.push(CommandBox.GO_NEXT_COMMAND);

        assertEquals(userInput, t.getText());
    }

    private class CommandExecutorStub implements CommandBox.CommandExecutor {
        @Override
        public CommandResult execute(String commandText) throws CommandException  {
            if (commandText.equals("invalid command")){
                throw new CommandException("This command is invalid");
            }
            return new CommandResult(commandText);
        }
    }
}