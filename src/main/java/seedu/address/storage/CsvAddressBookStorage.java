package seedu.address.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * A class to handle CSV export operations for the AddressBook.
 */
public class CsvAddressBookStorage {

    private static final String CSV_HEADER = "Name,Phone,Email,Telegram,GitHub,Tags";
    private static final String CSV_DELIMITER = ",";
    private static final String EMPTY_FIELD = "";

    /**
     * Exports the given address book to a CSV file.
     *
     * @param addressBook the address book to export
     * @param filePath the path to save the CSV file
     * @throws IOException if there was any problem writing to the file
     */
    public static void exportToCsv(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        // Create parent directories if they don't exist
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write each person
            for (Person person : addressBook.getPersonList()) {
                writer.write(personToCsvRow(person));
                writer.newLine();
            }
        }
    }

    /**
     * Converts a person to a CSV row.
     */
    private static String personToCsvRow(Person person) {
        return String.join(CSV_DELIMITER,
                escapeCsvField(person.getName().fullName),
                escapeCsvField(person.getPhone().value),
                escapeCsvField(person.getEmail().value),
                escapeCsvField(person.getTelegram() != null ? person.getTelegram().value : EMPTY_FIELD),
                escapeCsvField(person.getGithub() != null ? person.getGithub().value : EMPTY_FIELD),
                escapeCsvField(person.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.joining(";")))
        );
    }

    /**
     * Escapes special characters in CSV fields.
     * Fields containing commas, quotes, or newlines are enclosed in quotes.
     * Quotes within fields are escaped by doubling them.
     */
    private static String escapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return EMPTY_FIELD;
        }

        if (field.contains(CSV_DELIMITER) || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}
