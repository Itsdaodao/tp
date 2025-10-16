package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * A factory interface for creating Command instances.
 * This class is required to circumvent Java's restriction
 * on checked exceptions in lambda expressions.
 */
@FunctionalInterface
public interface CommandFactory {
    Command create(String args) throws ParseException;
}
