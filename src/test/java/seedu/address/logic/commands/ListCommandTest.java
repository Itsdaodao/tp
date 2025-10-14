package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand.SortOrder;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(SortOrder.DEFAULT), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listSortedByName_showsSortedList() {
        expectedModel.applyNameSort();
        assertCommandSuccess(
                new ListCommand(SortOrder.ALPHABETICAL),
                model,
                ListCommand.MESSAGE_SUCCESS_ALPHABETICAL_ORDER,
                expectedModel
        );
    }

    @Test
    public void execute_listSortByRecent_showsSortedList() {
        expectedModel.applyRecentSort();
        assertCommandSuccess(
                new ListCommand(SortOrder.RECENT),
                model,
                ListCommand.MESSAGE_SUCCESS_RECENT_ORDER,
                expectedModel
        );
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(SortOrder.DEFAULT), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listSortedByNameIsFiltered_showsSortedList() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        expectedModel.applyNameSort();
        assertCommandSuccess(
                new ListCommand(SortOrder.ALPHABETICAL),
                model,
                ListCommand.MESSAGE_SUCCESS_ALPHABETICAL_ORDER,
                expectedModel
        );
    }

    @Test
    public void execute_listSortByRecentIsFiltered_showsSortedList() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        expectedModel.applyRecentSort();
        assertCommandSuccess(
                new ListCommand(SortOrder.RECENT),
                model,
                ListCommand.MESSAGE_SUCCESS_RECENT_ORDER,
                expectedModel
        );
    }
}
