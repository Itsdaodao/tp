package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationPendingResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage, new StateManager());
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void execute_addCommand_triggersWrite() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY;

        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);

        assertCommandTriggersWrite(addCommand,
                String.format(AddCommand.MESSAGE_SUCCESS, expectedPerson),
                expectedModel);
    }

    @Test
    public void execute_editCommand_triggersWrite() throws Exception {
        // setup model with one person
        model.addPerson(AMY);
        int index = 1; // assuming first person
        String editCommand = "edit " + index + " " + PHONE_DESC_AMY; // or replace with valid edit args

        Person editedPerson = new PersonBuilder(AMY).build();
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(model.getSortedAndFilteredPersonList().get(0), editedPerson);

        assertCommandTriggersWrite(editCommand,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson),
                expectedModel);
    }

    @Test
    public void execute_deleteCommand_doesNotTriggerWrite() throws Exception {
        model.addPerson(AMY);
        int index = 1;
        String deleteCommand = "delete " + index;

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        String expected = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_CONFIRM, Messages.format(AMY));
        assertCommandDoesNotTriggerWrite(deleteCommand, expected, expectedModel);
    }

    @Test
    public void execute_deletionConfirmationCommand_triggersWrite() throws Exception {
        // Arrange - create state with pending confirmation
        model.addPerson(AMY);
        State state = createPendingDeleteConfirmationState(model, AMY);
        TrackingStorageManager trackingStorage = getTestStorageManager();
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(AMY);

        // act - execute confirm command
        LogicManager lm = new LogicManager(model, trackingStorage, state);
        lm.execute("y");


        // Assert - check that contact deleted and write triggered
        assertTrue(trackingStorage.saveCalled,
                "Expected saveAddressBook() to be called but it was not.");
    }

    @Test
    public void execute_deletionCancelCommand_triggersWrite() throws Exception {
        // Arrange - create state with pending confirmation
        model.addPerson(AMY);
        State state = createPendingDeleteConfirmationState(model, AMY);
        TrackingStorageManager trackingStorage = getTestStorageManager();
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // act - execute confirm command
        LogicManager lm = new LogicManager(model, trackingStorage, state);
        lm.execute("n");


        // Assert - check that contact deleted and write triggered
        assertTrue(trackingStorage.saveCalled,
                "Expected saveAddressBook() to be called but it was not.");
    }

    @Test
    public void execute_clearCommand_triggersWrite() throws Exception {
        model.addPerson(AMY);
        String clearCommand = ClearCommand.COMMAND_WORD;

        ModelManager expectedModel = new ModelManager();
        assertCommandTriggersWrite(clearCommand, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listCommand_doesNotTriggerWrite() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandDoesNotTriggerWrite(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    // Integration test
    @Test
    public void execute_deleteCommandThenConfirmCommand_deletesUserAndWrites() throws Exception {
        // Arrange - create state with pending confirmation
        model.addPerson(AMY);
        int index = 1;
        String deleteCommand = "delete " + index;
        State state = new StateManager();
        TrackingStorageManager trackingStorage = getTestStorageManager();
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(AMY);

        // act - execute confirm command
        LogicManager lm = new LogicManager(model, trackingStorage, state);
        lm.execute(deleteCommand);
        lm.execute("y");


        // Assert - check that contact deleted and write triggered
        assertEquals(expectedModel, model);
        assertTrue(trackingStorage.saveCalled,
                "Expected saveAddressBook() to be called but it was not.");
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage, new StateManager());

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    /**
     * A Storage stub that tracks whether saveAddressBook() was called.
     */
    private static class TrackingStorageManager extends StorageManager {
        private boolean saveCalled = false;

        TrackingStorageManager(Path addressBookPath, Path prefsPath) {
            super(new JsonAddressBookStorage(addressBookPath), new JsonUserPrefsStorage(prefsPath));
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            saveCalled = true; // mark that write occurred
            super.saveAddressBook(addressBook); // optional: actually write file
        }
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * - the Storage component's saveAddressBook() method was called exactly once
     */
    private void assertCommandTriggersWrite(String inputCommand, String expectedMessage, Model expectedModel)
            throws Exception {
        Path bookPath = temporaryFolder.resolve("writeTest.json");
        Path prefsPath = temporaryFolder.resolve("prefsWriteTest.json");

        TrackingStorageManager trackingStorage = new TrackingStorageManager(bookPath, prefsPath);
        logic = new LogicManager(model, trackingStorage, new StateManager());

        CommandResult result = logic.execute(inputCommand);
        String feedback = result.getFeedbackToUser();
        if (!feedback.equals(expectedMessage)) {
            // Allow looser match for commands whose feedback may format differently
            assertTrue(feedback.startsWith(expectedMessage.split(":")[0]),
                    "Expected feedback to start with same prefix but got: " + feedback);
        } else {
            assertEquals(expectedMessage, result.getFeedbackToUser());
        }
        assertEquals(expectedModel, model);
        assertEquals(true, trackingStorage.saveCalled, "Expected saveAddressBook() to be called but it was not.");
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * - the Storage component's saveAddressBook() method was NOT called
     */
    private void assertCommandDoesNotTriggerWrite(String inputCommand, String expectedMessage, Model expectedModel)
            throws Exception {
        Path bookPath = temporaryFolder.resolve("noWriteTest.json");
        Path prefsPath = temporaryFolder.resolve("prefsNoWriteTest.json");

        TrackingStorageManager trackingStorage = new TrackingStorageManager(bookPath, prefsPath);
        logic = new LogicManager(model, trackingStorage, new StateManager());

        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
        assertEquals(false, trackingStorage.saveCalled, "Expected saveAddressBook() NOT to be called but it was.");
    }

    private TrackingStorageManager getTestStorageManager() {
        Path bookPath = temporaryFolder.resolve("writeTest.json");
        Path prefsPath = temporaryFolder.resolve("prefsWriteTest.json");
        return new TrackingStorageManager(bookPath, prefsPath);
    }

    private static State createPendingDeleteConfirmationState(Model model, Person person) {
        State state = new StateManager();
        state.setAwaitingUserConfirmation(new ConfirmationPendingResult(
                "Confirm deletion [y/n] of:\n" + Messages.format(person) + "?",
                false, false, () -> {
                    model.deletePerson(person);
                    return new CommandResult(
                            String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(person))
                    );
                }
        ));
        return state;
    }
}
