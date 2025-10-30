package seedu.address.ui;

/**
 * Handles dispatching information to the StatusBar in the application.
 */
public class StatusBarNotificationDispatcher {
    private static StatusBarNotificationDispatcher instance;
    private static String pendingMessages;
    private static StatusBarFooter footer;

    private StatusBarNotificationDispatcher() {

    }

    /**
     * Returns the Singleton dispatcher instance
     */
    public static StatusBarNotificationDispatcher getInstance() {
        if (instance == null) {
            instance = new StatusBarNotificationDispatcher();
        }
        return instance;
    }

    /**
     * Sets the StatusBarFooter to be used for dispatching messages.
     *
     * @param status The StatusBarFooter to be setting the messages on.
     */
    public void setStatusBar(StatusBarFooter status) {
        footer = status;
        if (pendingMessages != null) {
            footer.setText(pendingMessages);
            pendingMessages = null;
        }
    }

    /**
     * Dispatch a message to the StatusBar. If the StatusBar
     * is not yet available, it enqueues the message to be shown later.
     *
     * @param text The text to be shown
     */
    public void notify(String text) {
        if (footer == null) {
            enqueueMessage(text);
            return;
        }
        footer.setText(text);
    }

    private void enqueueMessage(String message) {
        if (pendingMessages == null) {
            pendingMessages = message;
        } else {
            pendingMessages = pendingMessages + "\n" + message;
        }
    }

}
