package seedu.address.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public interface ReadOnlyCommandHistory {

    String getNextCommandFromHistory();

    String getPreviousCommandFromHistory(String curr);

    List<String> getHistory();
}
