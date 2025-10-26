package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.CommandHistory;
import seedu.address.model.ReadOnlyCommandHistory;

/**
 * A class to access CommandHistory stored in the hard disk
 */
public class NewlineDelimitedCommandHistoryStorage implements CommandHistoryStorage {
    private Path filePath;

    /**
     * Constructs an object with the provided filePath
     * @param filePath The location of the file
     */
    public NewlineDelimitedCommandHistoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getCommandHistoryFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<CommandHistory> readCommandHistory() throws DataLoadingException {
        if (filePath == null) {
            throw new NullPointerException();
        }
        if (!FileUtil.isFileExists(filePath)) {
            return Optional.empty();
        }

        String data;
        try {
            data = FileUtil.readFromFile(filePath);
        } catch (IOException e) {
            throw new DataLoadingException(e);
        }

        CommandHistory history = new CommandHistory();
        for (String line : data.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            // Process each line as a command
            history.addCommandToHistory(line);
        }

        return Optional.of(history);
    }

    @Override
    public void saveCommandHistory(ReadOnlyCommandHistory history) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> historyList = history.getHistory();

        for (int i = historyList.size() - 1; i >= 0; i--) {
            sb.append(historyList.get(i)).append("\n");
        }

        FileUtil.writeToFile(filePath, sb.toString());
    }
}
