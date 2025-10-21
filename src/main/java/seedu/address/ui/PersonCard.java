package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.PreferredCommunicationMode;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String PREFERRED_SUFFIX = " (preferred)";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label telegram;
    @FXML
    private Label github;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     * <p>
     * Displays the person's name, phone number, email, Telegram handle, GitHub username, and tags.
     * If a contact method matches the person's preferred communication mode, a "(preferred)" suffix is appended.
     *
     * @param person the person whose details are to be displayed
     * @param displayedIndex the index number shown beside the person's name in the list
     *
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        PreferredCommunicationMode preferredMode = person.getPreferredMode();

        // Mandatory Field: Phone
        String phoneText = person.getPhone().value;
        if (preferredMode == PreferredCommunicationMode.PHONE) {
            phoneText += PREFERRED_SUFFIX;
        }
        phone.setText(phoneText);
        phone.setVisible(true);
        phone.setManaged(true);

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
     * If the value is {@code null} or blank, the label is hidden and unmanaged in the layout.
     * Otherwise, the label is shown with its text set to the provided field name and value.
     * If the field matches the person's preferred communication mode, a "(preferred)" suffix is appended.
     *
     * @param label the JavaFX label to udpate
     * @param value the contact information to display
     * @param fieldName the field name to show before the value
     * @param modeToCheck the communication mode associated with this field
     */
    private void setContactField(Label label, String value, String fieldName, PreferredCommunicationMode modeToCheck) {
        boolean isEmpty = value == null || value.isBlank();
        boolean isPreferred = person.getPreferredMode() == modeToCheck;

        if (isEmpty) {
            label.setVisible(false);
            label.setManaged(false);
        } else {
            label.setVisible(true);
            label.setManaged(true);
            String text = fieldName + value + (isPreferred ? PREFERRED_SUFFIX : "");
            label.setText(text);
        }
    }

}
