package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.LogicManager;
import seedu.address.logic.LogicManagerTest;
import seedu.address.logic.State;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains unit tests for {@code ConfirmCommand}.
 */
public class ConfirmCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_confirmCommandY_performsOperation() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = "Done!";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "y", expectedMessage, "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(confirmCommand, model, "Done!", expectedModel);
    }

    @Test
    public void execute_confirmCommandN_doesNotPerformOperation() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "n", "Done!", "Delete user?"
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(confirmCommand, model, ConfirmCommand.MESSAGE_OPERATION_CANCELLED, expectedModel);
    }

    @Test
    public void execute_confirmCommandGarbageInput_promptsForInputAgain() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String feedback = "Delete user X?";
        ConfirmCommand confirmCommand = createConfirmCommandWithDeletePending(
                model, personToDelete, "asdfasdfasddfasdfsasdfasdf", "Done!", feedback
        );
        String expected = String.format(ConfirmCommand.MESSAGE_INVALID_CONFIRMATION_INPUT, feedback);

        assertCommandFailure(confirmCommand, model, expected);
    }


    @Test
    public void toStringMethod() {
        ConfirmationPendingResult pendingOperation = new ConfirmationPendingResult(
                "Delete?", false, false,
                () -> {
                    return new CommandResult("Deleted!");
                }
        );
        String userInput = "n";
        ConfirmCommand confirmCommand = new ConfirmCommand(userInput, () -> {}, pendingOperation);
        String expected = ConfirmCommand.class.getCanonicalName() + "{pendingOperation=" + pendingOperation +", "
                + "userInput=" + userInput + "}";
        assertEquals(expected, confirmCommand.toString());
    }

    private static ConfirmCommand createConfirmCommandWithDeletePending(Model model, Person person, String input,
                                                                        String result, String feedback) {
        ConfirmationPendingResult pendingOperation = new ConfirmationPendingResult(
                feedback, false, false,
                () -> {
                    model.deletePerson(person);
                    return new CommandResult(result);
                }
        );
        return new ConfirmCommand(input, () -> {}, pendingOperation);
    }
}
