package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code ConfirmationPendingResultTest}.
 */
public class ConfirmationPendingResultTest {
    @Test
    public void executeOnConfirm_validSet_returnsResultAndRunsCallback() {
        TestHelper th = new TestHelper();
        CommandResult expected = new CommandResult("done");
        ConfirmationPendingResult cpr = new ConfirmationPendingResult(
                "", false, false, th::setHasCalled,
                expected
        );

        CommandResult actual = cpr.executeOnConfirm();

        assertEquals(actual, expected);
        assertEquals(th.numberOfCalls, 1);
    }

    @Test
    public void executeOnConfirm_validSet_multipleRunsOnlyExecutesRunnableOnce() {
        TestHelper th = new TestHelper();
        ConfirmationPendingResult cpr = new ConfirmationPendingResult(
                "", false, false, th::setHasCalled, new CommandResult("hi")
        );

        cpr.executeOnConfirm();
        cpr.executeOnConfirm();

        assertEquals( 1, th.numberOfCalls);
    }

    private class TestHelper {
        public int numberOfCalls = 0;

        public void setHasCalled() {
            numberOfCalls += 1;
        }
    }
}
