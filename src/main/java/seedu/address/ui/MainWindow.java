package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.autocomplete.Autocompletor;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyCommandHistory;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {
    public static final KeyCode ENTER_SCROLL_MODE = KeyCode.ESCAPE;
    public static final KeyCode SCROLL_MODE_NEXT = KeyCode.K;
    public static final KeyCode SCROLL_MODE_PREVIOUS = KeyCode.L;
    public static final KeyCode ENTER_INPUT_MODE = KeyCode.I;

    private static final String ENTER_INPUT_MODE_FEEDBACK = "Entered insert mode.\nPress "
            + ENTER_SCROLL_MODE + " to return to scroll mode.";
    private static final String ENTER_SCROLL_MODE_FEEDBACK = "Entered scroll mode.\nPress "
            + ENTER_INPUT_MODE + " to return to input mode.\n Use [" + SCROLL_MODE_NEXT + "/"
            + SCROLL_MODE_PREVIOUS + "] to navigate.";
    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    private boolean isCommandMode;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.isCommandMode = false;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        setModeListeners();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList(),
                message -> resultDisplay.setFeedbackToUser(message));
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
    }

    void createCommandBox(Autocompletor autocompletor, ReadOnlyCommandHistory commandHistory) {
        commandBox = new CommandBox(this::executeCommand, autocompletor, commandHistory);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets up the key listeners for scroll and insert modes and keybindings associated to them.
     */
    void setModeListeners() {
        // Listener for entering input mode - this needs to be a KEY_TYPED event to
        // prevent 'i' input from leaking into the command box.
        String toMatch = ENTER_INPUT_MODE.toString().toLowerCase();
        getRoot().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String eventInput = event.getCharacter().toLowerCase();
            if (isCommandMode && eventInput.equals(toMatch)) {
                handleEnterInputMode();
            }
        });

        // Listener for other key presses - for switching modes and navigation.
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

    /**
     * Manages global key presses for toggling modes and navigation.
     * @param event The key event to handle.
     */
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(ENTER_SCROLL_MODE)) {
            handleEnterScrollMode();
        } else if (event.getCode().equals(SCROLL_MODE_NEXT)) {
            handleNavigateNext();
        } else if (event.getCode().equals(SCROLL_MODE_PREVIOUS)) {
            handleNavigatePrevious();
        }
    }

    /**
     * Handles the switch to input mode.
     */
    private void handleEnterInputMode() {
        commandBox.enableInput();
        isCommandMode = false;
        resultDisplay.setFeedbackToUser(ENTER_INPUT_MODE_FEEDBACK);
    }

    /**
     * Handles the switch to scroll mode.
     */
    private void handleEnterScrollMode() {
        commandBox.disableInput();
        isCommandMode = true;
        resultDisplay.setFeedbackToUser(ENTER_SCROLL_MODE_FEEDBACK);
    }

    /**
     * Handles navigation to the next person in scroll mode.
     */
    private void handleNavigateNext() {
        if (isCommandMode) {
            personListPanel.goToNextPerson();
        }
    }

    /**
     * Handles navigation to the previous person in scroll mode.
     */
    private void handleNavigatePrevious() {
        if (isCommandMode) {
            personListPanel.goToPreviousPerson();
        }
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
