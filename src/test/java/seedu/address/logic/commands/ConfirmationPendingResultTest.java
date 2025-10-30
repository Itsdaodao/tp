package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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

        assertEquals(1, th.numberOfCalls);
    }

    /**
     * Test helper used to check if runnable callback is
     * called multiple times. Required since primitives like
     * int/booleans are passed as values into lambda functions
     * while objects are passed by reference.
     */
    private static class TestHelper {
        private int numberOfCalls = 0;

        public void setHasCalled() {
            numberOfCalls += 1;
        }
    }
}
