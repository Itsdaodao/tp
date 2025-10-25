package seedu.address.logic.parser;

import static seedu.address.logic.commands.TagCommand.MESSAGE_NO_FLAG_PROVIDED;
import static seedu.address.logic.commands.TagCommand.MESSAGE_RENAMED_EXACTLY_ONE;
import static seedu.address.logic.parser.CliSyntax.FLAG_RENAME_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RENAMED_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET_TAG;

import java.util.Set;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class TagCommandParser implements Parser<TagCommand> {

    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, FLAG_RENAME_TAG, PREFIX_TARGET_TAG, PREFIX_RENAMED_TAG);

        boolean isRename = argMultimap.getValue(FLAG_RENAME_TAG).isPresent();

        if (!isRename) {
            // invalid flag
            throw new ParseException(MESSAGE_NO_FLAG_PROVIDED);
        }

        return parseRename(argMultimap);
    }

    public TagCommand parseRename(ArgumentMultimap argMultimap) throws ParseException {

        Set<Tag> targetTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TARGET_TAG));
        Set<Tag> renamedTags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_RENAMED_TAG));

        boolean isOneTagEach = targetTags.size() == 1 && renamedTags.size() == 1;
        if (!isOneTagEach) {
            // MUST HAVE EXACTLY ONE TAG EACH
            throw new ParseException(MESSAGE_RENAMED_EXACTLY_ONE);
        }

        return new TagCommand(targetTags, renamedTags);
    }
}
