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
                String csvRow = formatPersonAsCsv(person);
                writer.write(csvRow);
                writer.newLine();
            }
        }
    }

    /**
     * Formats a person as a CSV row with proper escaping.
     */
    private static String formatPersonAsCsv(Person person) {
        String[] fields = new String[6];

        fields[0] = escapeCsvField(person.getName().fullName);
        fields[1] = escapeCsvField(person.getPhone().value);
        fields[2] = escapeCsvField(person.getEmail().value);
        fields[3] = escapeCsvField(person.getTelegram() != null ? person.getTelegram().value : EMPTY_FIELD);
        fields[4] = escapeCsvField(person.getGithub() != null ? person.getGithub().value : EMPTY_FIELD);
        fields[5] = escapeCsvField(
                person.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.joining(";"))
        );

        return String.join(CSV_DELIMITER, fields);
    }

    /**
     * Escapes special characters in CSV fields.
     */
    private static String escapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return EMPTY_FIELD;
        }

        // Check if field contains any characters that require quoting
        boolean needsQuotes = field.contains(CSV_DELIMITER)
                || field.contains("\"")
                || field.contains("\n")
                || field.contains("\r");

        if (needsQuotes) {
            // Replace each quote with two quotes and wrap in quotes
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}
