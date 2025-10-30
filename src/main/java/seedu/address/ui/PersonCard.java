package seedu.address.ui;

import java.util.Comparator;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.logic.util.ApplicationLinkLauncher;
import seedu.address.model.person.Person;
import seedu.address.model.person.PreferredCommunicationMode;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String PREFERRED_SUFFIX = " (Preferred)";
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */
    public final Person person;

    private final Consumer<String> feedbackConsumer;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Hyperlink email;
    @FXML
    private Hyperlink telegram;
    @FXML
    private Hyperlink github;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView pinIcon;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     * If a contact method matches the person's preferred communication mode, a "(preferred)" suffix is appended.
     *
     * @param person            The person whose details are to be displayed.
     * @param displayedIndex    The index of the person in the list.
     * @param feedbackConsumer  The consumer to handle feedback messages.
     */
    public PersonCard(Person person, int displayedIndex, Consumer<String> feedbackConsumer) {
        super(FXML);
        this.person = person;
        this.feedbackConsumer = feedbackConsumer;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        // Display pin icon
        pinIcon.setVisible(person.isPinned());

        // Mandatory Field: Phone
        String phoneText = person.getPhone().value;
        phone.setText(phoneText);
        phone.setVisible(true);
        phone.setManaged(true);

        PreferredCommunicationMode preferredMode = person.getPreferredMode();
        boolean isPreferredPhone = preferredMode == PreferredCommunicationMode.PHONE;

        // Set preferred styling for phone field
        if (isPreferredPhone) {
            setPreferredContactField(phone, phoneText, "");
        }

        // Optional Fields
        setContactField(email, person.getEmail().value, "", PreferredCommunicationMode.EMAIL);
        setContactField(telegram, person.getTelegram().value, "Telegram: ", PreferredCommunicationMode.TELEGRAM);
        setContactField(github, person.getGithub().value, "Github: ", PreferredCommunicationMode.GITHUB);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Updates the visibility and text of a contact field label based on its value and whether it matches the
     * person's preferred communication mode.
     * <p>
     * If the value is {@code null} or blank, the label is hidden and unmanaged in the layout.
     * Otherwise, the label is shown with its text set to the provided field name and value.
     * If the field matches the person's preferred communication mode, a "(Preferred)" suffix is appended.
     *
     * @param label the JavaFX labeled to update
     * @param value the contact information to display
     * @param fieldName the field name to show before the value
     * @param modeToCheck the communication mode associated with this field
     */
    private void setContactField(Labeled label, String value, String fieldName,
                                 PreferredCommunicationMode modeToCheck) {
        boolean isEmpty = value == null || value.isBlank();
        boolean isPreferred = person.getPreferredMode() == modeToCheck;

        // Hide field if empty
        if (isEmpty) {
            label.setVisible(false);
            label.setManaged(false);
            return;
        }

        // Show valid fields
        label.setVisible(true);
        label.setManaged(true);

        // Display with preferred styling or normal text
        if (isPreferred) {
            setPreferredContactField(label, value, fieldName);
        } else {
            label.setText(fieldName + value);
            label.setGraphic(null);
        }
    }

    /**
     * Formats the given contact field as the user's preferred communication mode.
     * <p>
     * Adds a gold, bold "(Preferred)" label next to the field
     *
     * @param label the JavaFX labeled to update
     * @param value the contact information to display
     * @param fieldName the field name to show before the value
     */
    private void setPreferredContactField(Labeled label, String value, String fieldName) {
        Text mainText = new Text(fieldName + value + " ");
        Text preferredText = new Text(PREFERRED_SUFFIX);

        // Main text: inherits hyperlink color (preserves hover effect)
        mainText.fillProperty().bind(label.textFillProperty());

        // Preferred text: gold & bold
        preferredText.getStyleClass().add("preferred-tag");

        // Combine into a single flow and apply to the label
        TextFlow flow = new TextFlow(mainText, preferredText);
        label.setGraphic(flow);
        label.setText(""); // clear plain text to prevent duplication
    }

    /**
     * Launches the email application with the person's email address.
     * FeedbackConsumer will set resultDisplay based on success/failure of launch.
     */
    @FXML
    public void launchEmail() {
        feedbackConsumer.accept(ApplicationLinkLauncher.launchEmail(person.getEmail().value).getMessage());
    }

    /**
     * Launches the telegram application with the person's telegram handle.
     * FeedbackConsumer will set resultDisplay based on success/failure of launch.
     */
    @FXML
    public void launchTelegram() {
        feedbackConsumer.accept(ApplicationLinkLauncher.launchTelegram(person.getTelegram().value).getMessage());
    }

    /**
     * Launches the github page with the person's github username.
     * FeedbackConsumer will set resultDisplay based on success/failure of launch.
     */
    @FXML
    public void launchGithub() {
        feedbackConsumer.accept(ApplicationLinkLauncher.launchGithub(person.getGithub().value).getMessage());
    }

}
