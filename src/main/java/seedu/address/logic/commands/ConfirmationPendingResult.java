package seedu.address.logic.commands;

/**
 * Represents a command that requires confirmation from the user before execution.
 */
public class ConfirmationPendingResult extends CommandResult {
    public static final String CONFIRMATION_TEXT_FORMAT = "%s\nProceed? " + ConfirmCommand.USER_INPUT_OPTIONS;

    /** Contains logic to execute on user confirmation.**/
    private final OnceRunnable pendingAction;
    private final CommandResult successResult;

    /**
     * Constructs a {@code ConfirmationPendingResult} with the specified fields.
     *
     * @param feedbackToUser The confirmation message to be shown to the user.
     * @param showHelp Whether help information should be shown to the user.
     * @param exit Whether the application should exit.
     * @param pendingAction The logic to execute when the user confirms to proceed with the action.
     * @param successResult The CommandResult to be returned when the user confirms to proceed with the action.
     */
    public ConfirmationPendingResult(String feedbackToUser, boolean showHelp, boolean exit,
                                     Runnable pendingAction, CommandResult successResult) {
        super(String.format(CONFIRMATION_TEXT_FORMAT, feedbackToUser), showHelp, exit);

        assert successResult != null : "successResult should be a valid CommandResult!";
        assert pendingAction != null : "pendingAction should not be null!";

        this.pendingAction = new OnceRunnable(pendingAction);
        this.successResult = successResult;
    }

    /**
     * Executes the command logic
     * @return CommandResult after execution.
     */
    public CommandResult executeOnConfirm() {
        this.pendingAction.run();
        return successResult;
    }

    /**
     * Represents a runnable callback that can only be executed once.
     */
    private static class OnceRunnable {
        private boolean hasRun = false;
        private final Runnable r;

        public OnceRunnable(Runnable r) {
            this.r = r;
        }

        /**
         * Runs the Runnable. If the inner Runnable object has already
         * been ran once, then it simply returns, doing nothing.
         */
        public void run() {
            assert !hasRun : "Pending Result should only be ran once!";

            if (hasRun || r == null) {
                return;
            }

            this.r.run();
            hasRun = true;
        }
    }
}
