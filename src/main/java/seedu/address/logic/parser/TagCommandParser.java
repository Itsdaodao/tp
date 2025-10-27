package seedu.address.logic.parser;

import static seedu.address.logic.commands.TagCommand.MESSAGE_MULTIPLE_FLAGS_INVALID;
import static seedu.address.logic.commands.TagCommand.MESSAGE_NO_FLAG_PROVIDED;
import static seedu.address.logic.commands.TagCommand.MESSAGE_NO_TARGET_TAG_PROVIDED;
import static seedu.address.logic.commands.TagCommand.MESSAGE_RENAMED_EXACTLY_ONE;
import static seedu.address.logic.commands.TagCommand.MESSAGE_RENAMED_TAG_FOUND_UNSUITABLE;
import static seedu.address.logic.parser.CliSyntax.FLAG_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.FLAG_RENAME_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RENAMED_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.TagOperation;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns an TagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, FLAG_RENAME_TAG, FLAG_DELETE_TAG, PREFIX_TARGET_TAG, PREFIX_RENAMED_TAG);

        boolean isRename = argMultimap.getValue(FLAG_RENAME_TAG).isPresent();
        boolean isDelete = argMultimap.getValue(FLAG_DELETE_TAG).isPresent();

        if (!isRename && !isDelete) {
            // No flags provided
            throw new ParseException(MESSAGE_NO_FLAG_PROVIDED);
        }

        if (isRename && isDelete) {
            // Multiple flags provided
            throw new ParseException(MESSAGE_MULTIPLE_FLAGS_INVALID);
        }

        if (isRename) {
            return parseRename(argMultimap);
        } else if (isDelete) {
            return parseDelete(argMultimap);
        } else {
            throw new ParseException(MESSAGE_NO_FLAG_PROVIDED);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand given the rename flag {@code -r}
     * and returns an TagCommand object for rename tag operation execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    private TagCommand parseRename(ArgumentMultimap argMultimap) throws ParseException {

        Set<Tag> targetTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TARGET_TAG));
        Set<Tag> renamedTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_RENAMED_TAG));

        boolean isOneTagEach = targetTags.size() == 1 && renamedTags.size() == 1;
        if (!isOneTagEach) {
            // MUST HAVE EXACTLY ONE TAG EACH
            throw new ParseException(MESSAGE_RENAMED_EXACTLY_ONE);
        }

        return new TagCommand(targetTags, renamedTags, TagOperation.RENAME);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand given the delete flag {@code -d}
     * and returns an TagCommand object for delete tag operation execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    private TagCommand parseDelete(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_RENAMED_TAG).isPresent()) {
            throw new ParseException(MESSAGE_RENAMED_TAG_FOUND_UNSUITABLE);
        }

        Set<Tag> targetTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TARGET_TAG));
        if (targetTags.isEmpty()) {
            throw new ParseException(MESSAGE_NO_TARGET_TAG_PROVIDED);
        }

        Set<Tag> emptyTags = new HashSet<>();
        return new TagCommand(targetTags, emptyTags, TagOperation.DELETE);
    }
}
