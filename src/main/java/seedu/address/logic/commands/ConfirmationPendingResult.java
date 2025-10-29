package seedu.address.logic.commands;

/**
 * Represents a command that requires confirmation from the user before execution.
 */
public class ConfirmationPendingResult extends CommandResult {

    /** Contains logic to execute on user confirmation.**/
    private final OnceRunnable pendingAction;
    private final CommandResult successResult;

    public static final String CONFIRMATION_TEXT_FORMAT = "%s\nProceed? " + ConfirmCommand.USER_INPUT_OPTIONS;

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

    private static class OnceRunnable{
        private boolean hasRun = false;
        private final Runnable r;

        public OnceRunnable(Runnable r) {
            this.r = r;
        }

        public void run() {
            assert !hasRun : "Pending Result should only be runned once!";
            this.r.run();
            hasRun = true;
        }
    }
}
