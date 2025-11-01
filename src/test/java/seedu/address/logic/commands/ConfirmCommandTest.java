package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains unit tests for {@code ConfirmCommand}.
 */
public class ConfirmCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());

    @Test
    public void execute_confirmCommandY_performsOperation() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = "Done!";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "y", expectedMessage, "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(confirmCommand, model, "Done!", expectedModel);
    }

    @Test
    public void execute_confirmCommandN_doesNotPerformOperation() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "n", "Done!", "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());

        assertCommandSuccess(confirmCommand, model, ConfirmCommand.MESSAGE_OPERATION_CANCELLED, expectedModel);
    }


    @Test
    public void execute_confirmCommandYes_performsOperation() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = "Done!";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "yes", expectedMessage, "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(confirmCommand, model, "Done!", expectedModel);
    }

    @Test
    public void execute_confirmCommandNo_doesNotPerformOperation() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "no", "Done!", "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());

        assertCommandSuccess(confirmCommand, model, ConfirmCommand.MESSAGE_OPERATION_CANCELLED, expectedModel);
    }

    @Test
    public void execute_confirmCommandGarbageInput_promptsForInputAgain() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String feedback = "Delete user X?";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "asdfasdfasddfasdfsasdfasdf", "Done!", feedback
        );
        String expectedFeedback = String.format(ConfirmationPendingResult.CONFIRMATION_TEXT_FORMAT, feedback);
        String expected = String.format(ConfirmCommand.MESSAGE_INVALID_CONFIRMATION_INPUT, expectedFeedback);

        assertCommandFailure(confirmCommand, model, expected);
    }

    @Test
    public void execute_confirmCommandYesWithCapitalization_performsOperation() {
        Person personToDelete = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = "Done!";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
            model, personToDelete, "YES", expectedMessage, "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(confirmCommand, model, "Done!", expectedModel);
    }


    @Test
    public void toStringMethod() {
        ConfirmationPendingResult pendingOperation = new ConfirmationPendingResult(
                "Delete?", false, false, () -> {},
                new CommandResult("Deleted!")
        );
        String userInput = "n";
        ConfirmCommand confirmCommand = new ConfirmCommand(userInput, () -> {}, pendingOperation);
        String expected = ConfirmCommand.class.getCanonicalName() + "{pendingOperation=" + pendingOperation + ", "
                + "userInput=" + userInput + "}";
        assertEquals(expected, confirmCommand.toString());
    }

    private static ConfirmCommand createConfirmCommandWithDeletePending(Model model, Person person, String input,
                                                                        String result, String feedback) {
        ConfirmationPendingResult pendingOperation = new ConfirmationPendingResult(
                feedback, false, false, () -> model.deletePerson(person), new CommandResult(result)
        );
        return new ConfirmCommand(input, () -> {}, pendingOperation);
    }
}
