package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.logic.LogicManager;
import seedu.address.logic.StateManager;
import seedu.address.logic.commands.CommandRegistry;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

@ExtendWith(ApplicationExtension.class)
public class MainWindowTest {
    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private MainWindow mainWindow;

    @Start
    private void start(Stage stage) {
        // Initialize temporary storage for prefs and address
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        // Set initial settings
        CommandRegistry.initialize();
        model.setAddressBook(getTypicalAddressBook());

        // Create the MainWindow
        mainWindow = new MainWindow(stage, new LogicManager(model, storage, new StateManager()));
        mainWindow.show();
        mainWindow.fillInnerParts();
    }

    @Test
    public void insertMode_noSelectedPerson_noInput() {
        assertNull(mainWindow.getPersonListPanel().getSelectedPerson());
    }

    @Test
    public void insertMode_writeValues_canFillInput(FxRobot robot) {
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);

        robot.clickOn("#commandTextField");
        robot.write("select 1");

        assertEquals("select 1", t.getText());
    }

    @Test
    public void insertMode_writeValues_worksAfterTogglingMode(FxRobot robot) {
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);
        robot.push(MainWindow.ENTER_SCROLL_MODE);
        robot.push(MainWindow.ENTER_INPUT_MODE);

        robot.clickOn("#commandTextField");
        robot.write("select 1");

        assertEquals("select 1", t.getText());
    }

    @Test
    public void scrollMode_writeValues_doesNotFillInput(FxRobot robot) {
        TextField t = robot.lookup("#commandTextField").queryAs(TextField.class);

        robot.push(MainWindow.ENTER_SCROLL_MODE);

        robot.clickOn("#commandTextField");
        robot.write("select 1");

        assertEquals("", t.getText());
    }

    @Test
    public void scrollMode_noSelectedPerson_noInput(FxRobot robot) {
        robot.push(MainWindow.ENTER_SCROLL_MODE);

        assertNull(mainWindow.getPersonListPanel().getSelectedPerson());
    }

    @Test
    public void scrollMode_selectsFirstPerson_onScrollNext(FxRobot robot) {
        Person selected = model.getAddressBook().getPersonList().get(0);

        robot.push(MainWindow.ENTER_SCROLL_MODE);
        robot.push(MainWindow.SCROLL_MODE_NEXT);

        assertEquals(selected, mainWindow.getPersonListPanel().getSelectedPerson());
    }

    @Test
    public void scrollMode_selectsLastPerson_onManyRepeatedScrollNextInput(FxRobot robot) {
        List<Person> list = model.getAddressBook().getPersonList();
        Person selected = list.get(list.size() - 1);

        robot.push(MainWindow.ENTER_SCROLL_MODE);
        for (int i = 0; i < list.size() + 5; i++) {
            robot.push(MainWindow.SCROLL_MODE_NEXT);
        }

        assertEquals(selected, mainWindow.getPersonListPanel().getSelectedPerson());
    }

    @Test
    public void scrollMode_selectsFirstPerson_onScrollPreviousInput(FxRobot robot) {
        // Arrange - select second person by pressing SCROLL_NEXT twice
        Person firstPerson = model.getAddressBook().getPersonList().get(0);
        Person secondPerson = model.getAddressBook().getPersonList().get(1);
        robot.push(MainWindow.ENTER_SCROLL_MODE);
        robot.push(MainWindow.SCROLL_MODE_NEXT);
        robot.push(MainWindow.SCROLL_MODE_NEXT);
        // Ensure second person is selected
        assertEquals(secondPerson, mainWindow.getPersonListPanel().getSelectedPerson());

        // ACT - Press SCROLL_PREVIOUS to go back to first person
        robot.push(MainWindow.SCROLL_MODE_PREVIOUS);

        assertEquals(firstPerson, mainWindow.getPersonListPanel().getSelectedPerson());
    }

}
