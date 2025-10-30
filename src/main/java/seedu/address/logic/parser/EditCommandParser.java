package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PREFERRED_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PreferredCommunicationMode;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {
    private static final boolean IS_ACCEPT_EMPTY_STRING = true;
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_NAME,
                        PREFIX_PHONE,
                        PREFIX_EMAIL,
                        PREFIX_TELEGRAM,
                        PREFIX_GITHUB,
                        PREFIX_PREFERRED_MODE,
                        PREFIX_TAG,
                        PREFIX_REMOVE_TAG
                );

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM, PREFIX_PREFERRED_MODE, PREFIX_GITHUB);

        EditPersonDescriptor editPersonDescriptor = createEditPersonDescriptor(argMultimap);
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Helper method used to help parse and extract all values for present parameters
     *
     * @param argMultimap
     * @return {@code EditPersonDescriptor} used for EditCommand
     * @throws ParseException
     */
    private EditPersonDescriptor createEditPersonDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil
                    .parseEmail(argMultimap.getValue(PREFIX_EMAIL).get(), IS_ACCEPT_EMPTY_STRING));
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            editPersonDescriptor.setTelegram(ParserUtil
                    .parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get(), IS_ACCEPT_EMPTY_STRING));
        }
        if (argMultimap.getValue(PREFIX_GITHUB).isPresent()) {
            editPersonDescriptor.setGithub(ParserUtil
                    .parseGithub(argMultimap.getValue(PREFIX_GITHUB).get(), IS_ACCEPT_EMPTY_STRING));
        }
        if (argMultimap.getValue(PREFIX_PREFERRED_MODE).isPresent()) {
            Set<PreferredCommunicationMode> availableModes = null;
            editPersonDescriptor.setPreferredMode(ParserUtil.parsePreferredMode(argMultimap
                    .getValue(PREFIX_PREFERRED_MODE).get(), availableModes, IS_ACCEPT_EMPTY_STRING));
        }
        // Finds the tags to add if any
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        // Finds the tags to remove if any
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_REMOVE_TAG)).ifPresent(editPersonDescriptor::setRemovedTags);

        return editPersonDescriptor;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

}
