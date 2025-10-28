package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.FLAG_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.FLAG_RENAME_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RENAMED_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.TagOperation;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {
    private final TagCommandParser parser = new TagCommandParser();

    // <--- Valid cases --->
    @Test
    public void parse_validRename_success() {
        // e.g. tag -r t/CS1101 s/CS2100
        String userInput = " " + FLAG_RENAME_TAG + " "
                + PREFIX_TARGET_TAG + "CS1101 "
                + PREFIX_RENAMED_TAG + "CS2100";

        Set<Tag> targetTags = Set.of(new Tag("CS1101"));
        Set<Tag> renamedTags = Set.of(new Tag("CS2100"));
        TagCommand expectedCommand = new TagCommand(targetTags, renamedTags, TagOperation.RENAME);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validRenameWithExtraSpaces_success() {
        // Handles random spacing correctly
        String userInput = "   " + FLAG_RENAME_TAG
                + "       " + PREFIX_TARGET_TAG + "  friend  "
                + "    " + PREFIX_RENAMED_TAG + " bestie  ";

        Set<Tag> targetTags = Set.of(new Tag("friend"));
        Set<Tag> renamedTags = Set.of(new Tag("bestie"));
        TagCommand expectedCommand = new TagCommand(targetTags, renamedTags, TagOperation.RENAME);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validDelete_success() {
        // e.g. tag -d t/CS1101
        String userInput = " " + FLAG_DELETE_TAG + " "
                + PREFIX_TARGET_TAG + "CS1101";
        Set<Tag> targetTags = Set.of(new Tag("CS1101"));
        Set<Tag> emptyTags = new HashSet<>();
        TagCommand expectedCommand = new TagCommand(targetTags, emptyTags, TagOperation.DELETE);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validDeleteWithExtraSpaces_success() {
        // Handles random spacing correctly
        String userInput = "   " + FLAG_DELETE_TAG
                + "       " + PREFIX_TARGET_TAG + "  friend  ";

        Set<Tag> targetTags = Set.of(new Tag("friend"));
        Set<Tag> renamedTags = new HashSet<>();
        TagCommand expectedCommand = new TagCommand(targetTags, renamedTags, TagOperation.DELETE);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validDeleteWithMultipleTargetTags_success() {
        // e.g. tag -d t/CS1101 t/CS2100
        String userInput = " " + FLAG_DELETE_TAG + " "
                + PREFIX_TARGET_TAG + "CS1101 "
                + PREFIX_TARGET_TAG + "CS2100 ";

        Set<Tag> targetTags = Set.of(new Tag("CS1101"), new Tag("CS2100"));
        Set<Tag> emptyTags = new HashSet<>();
        TagCommand expectedCommand = new TagCommand(targetTags, emptyTags, TagOperation.DELETE);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // <--- Invalid flag cases --->
    @Test
    public void parse_missingFlag_failure() {
        // Missing -r
        String userInput = " " + PREFIX_TARGET_TAG + "CS1101"
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_NO_FLAG_PROVIDED);
    }

    @Test
    public void parse_invalidFlag_failure() {
        // Wrong flag (e.g. -x)
        String userInput = " -x "
                + " " + PREFIX_TARGET_TAG + "CS1101"
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_NO_FLAG_PROVIDED);
    }

    @Test
    public void parse_multipleFlag_failure() {
        // Multiple flags (e.g. -d -r)
        String userInput = " " + FLAG_DELETE_TAG + " " + FLAG_RENAME_TAG
                + " " + PREFIX_TARGET_TAG + "CS1101"
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_MULTIPLE_FLAGS_INVALID);
    }

    // <--- Invalid tag count cases --->
    @Test
    public void parse_missingRenameTargetTag_failure() {
        // Missing target tag
        String userInput = " " + FLAG_RENAME_TAG
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_RENAMED_EXACTLY_ONE);
    }

    @Test
    public void parse_missingDeleteTargetTag_failure() {
        // Missing target tag
        String userInput = " " + FLAG_DELETE_TAG + " ";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_NO_TARGET_TAG_PROVIDED);
    }

    @Test
    public void parse_missingRenamedTag_failure() {
        // Missing renamed tag
        String userInput = " " + FLAG_RENAME_TAG
                + " " + PREFIX_TARGET_TAG + "CS1101";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_RENAMED_EXACTLY_ONE);
    }

    @Test
    public void parse_deleteWithRenamedTagPresent_failure() {
        // Unsuitable use of renamed tag for delete operation
        // e.g. tag -d t/CS1101 r/CS2100
        String userInput = " " + FLAG_DELETE_TAG + " "
                + PREFIX_TARGET_TAG + "CS1101 "
                + PREFIX_RENAMED_TAG + "CS2100 ";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_RENAMED_TAG_FOUND_UNSUITABLE);
    }

    @Test
    public void parse_multipleRenameTargetTags_failure() {
        // Two target tags, one renamed tag
        String userInput = " " + FLAG_RENAME_TAG
                + " " + PREFIX_TARGET_TAG + "CS1101"
                + " " + PREFIX_TARGET_TAG + "CS1231"
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_RENAMED_EXACTLY_ONE);
    }

    @Test
    public void parse_multipleRenamedTags_failure() {
        // One target tag, two renamed tags
        String userInput = " " + FLAG_RENAME_TAG
                + " " + PREFIX_TARGET_TAG + "CS1101 "
                + " " + PREFIX_RENAMED_TAG + "CS2100 "
                + " " + PREFIX_RENAMED_TAG + "CS2101";

        assertParseFailure(parser, userInput, TagCommand.MESSAGE_RENAMED_EXACTLY_ONE);
    }

    @Test
    public void parse_invalidRenameTagFormat_failure() {
        // invalid tag containing special characters
        String userInput = " " + FLAG_RENAME_TAG
                + " " + PREFIX_TARGET_TAG + "CS@1101"
                + " " + PREFIX_RENAMED_TAG + "CS2100";

        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDeleteTagFormat_failure() {
        // invalid tag containing special characters
        String userInput = " " + FLAG_DELETE_TAG
                + " " + PREFIX_TARGET_TAG + "CS@1101";

        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }
}
