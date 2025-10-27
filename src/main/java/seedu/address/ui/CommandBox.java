package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.autocomplete.Autocompletor;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final KeyCode FILL_AUTOCOMPLETE = KeyCode.TAB;
    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Autocompletor autocompletor;

    @FXML
    private TextField commandTextField;
    @FXML
    private TextField commandHintField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor, Autocompletor autocompletor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.autocompletor = autocompletor;
        // calls handleInput whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, newText) -> handleInput(newText));
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Handles the input in the command box. Resets
     * style and updates hint text.
     */
    private void handleInput(String commandText) {
        setStyleToDefault();
        if (commandText.equals("")) {
            commandHintField.setText("");
            return;
        }
        String hint = autocompletor.getHint(commandText);
        commandHintField.setText(hint);
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(FILL_AUTOCOMPLETE)) {
            fillAutocomplete();
            event.consume();
        }
    }

    /**
     * Fills the CommandTextField with the currently-suggested
     * Autocomplete input.
     */
    private void fillAutocomplete() {
        String completion = commandHintField.getText();
        // Completion field can be blank if there is no suggestion. To prevent overwriting
        // of user input if there is no suggestion, simply return if there is no completion.
        if (completion.isBlank()) {
            return;
        }
        commandTextField.setText(completion);
        commandTextField.end();;
    }

    /**
     * Disables the command box input.
     */
    public void disableInput() {
        commandTextField.setDisable(true);
    }

    /**
     * Enables the command box input, focusing user input onto the text field.
     */
    public void enableInput() {
        commandTextField.setDisable(false);
        commandTextField.requestFocus();
        commandTextField.end();
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
