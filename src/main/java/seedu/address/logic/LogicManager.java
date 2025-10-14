package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationPendingResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private final State state;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage, State state) {
        this.model = model;
        this.storage = storage;
        this.state = state;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        // Get command based on input and execute
        Command command = buildCommand(commandText);
        CommandResult commandResult = command.execute(model);

        saveIfRequired(command);

        if (commandResult instanceof ConfirmationPendingResult pendingResult) {
            // Set the state to indicate that app is waiting for confirmation
            state.setAwaitingUserConfirmation(
                pendingResult
            );
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getSortedAndFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    /**
     * Parses the command text and returns the corresponding Command object.
     * If the application is awaiting user confirmation, it creates a ConfirmCommand instead.
     *
     * @param commandText The command text input by the user.
     * @return The Command object to be executed.
     * @throws ParseException If the command text is invalid.
     */
    private Command buildCommand(String commandText) throws ParseException {
        if (state.isAwaitingUserConfirmation()) {
            return addressBookParser.parseConfirmationCommand(
                    commandText, state::clearAwaitingUserConfirmation, state.getPendingOperation()
            );
        }
        return addressBookParser.parseCommand(commandText);
    }

    /**
     * Saves the current address book to storage if the command modifies the data.
     *
     * @param command The command that was executed.
     * @throws CommandException If there was an error during saving.
     */
    private void saveIfRequired(Command command) throws CommandException {
        if (!command.requiresWrite()) {
            return;
        }
        // Save using the current model state
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }
}
