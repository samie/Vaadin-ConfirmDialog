package org.vaadin.dialogs;

import java.io.Serializable;

import com.vaadin.server.JsonPaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ConfirmDialog extends Window {

    private static final long serialVersionUID = -2363125714643244070L;

    public interface Factory extends Serializable {
        ConfirmDialog create(String windowCaption, String message,
                String okTitle, String cancelTitle, String notOKCaption);
    }

    /* Test IDs for elements */
    public static final String DIALOG_ID = "confirmdialog-window";
    public static final String MESSAGE_ID = "confirmdialog-message";
    public static final String OK_ID = "confirmdialog-ok-button";
    public static final String NOT_OK_ID = "confirmdialog-not-ok-button";
    public static final String CANCEL_ID = "confirmdialog-cancel-button";

    public enum ContentMode {
        TEXT_WITH_NEWLINES, TEXT, PREFORMATTED, HTML
    };

    /**
     * Listener for dialog close events. Implement and register an instance of
     * this interface to dialog to receive close events.
     *
     * @author Sami Ekblad
     *
     */
    public interface Listener extends Serializable {
        void onClose(ConfirmDialog dialog);
    }

    /**
     * Default dialog factory.
     *
     */
    private static ConfirmDialog.Factory factoryInstance;

    /**
     * Get the ConfirmDialog.Factory used to create and configure the dialog.
     *
     * By default the {@link DefaultConfirmDialogFactory} is used.
     *
     * @return
     */
    public static ConfirmDialog.Factory getFactory() {
        if (factoryInstance == null) {
            factoryInstance = new DefaultConfirmDialogFactory();
        }
        return factoryInstance;
    }

    /**
     * Set the ConfirmDialog.Factory used to create and configure the dialog.
     *
     * By default the {@link DefaultConfirmDialogFactory} is used.
     *
     * @return
     */
    public static void setFactory(final ConfirmDialog.Factory newFactory) {
        factoryInstance = newFactory;
    }

    /**
     * Show a modal ConfirmDialog in a window.
     *
     * @param parentWindow
     * @param listener
     */
    public static ConfirmDialog show(final UI ui, final Listener listener) {
        return show(ui, null, null, null, null, listener);
    }

    /**
     * Show a modal ConfirmDialog in a window.
     *
     * @param parentWindow
     * @param messageLabel
     * @param listener
     * @return
     */
    public static ConfirmDialog show(final UI ui, final String message,
            final Listener listener) {
        return show(ui, null, message, null, null, listener);
    }

    /**
     * Show a modal three way (eg. yes/no/cancel) ConfirmDialog in a window.
     * 
     * @param ui
     *            UI
     * @param windowCaption
     *            Caption for the confirmation dialog window.
     * @param message
     *            Message to display as window content.
     * @param okCaption
     *            Caption for the ok button.
     * @param cancelCaption
     *            Caption for cancel button.
     * @param notOKCaption
     *            Caption for notOK button.
     * @param listener
     *            Listener for dialog result.
     * @return
     */
    public static ConfirmDialog show(final UI ui,
            final String windowCaption, final String message,
            final String okCaption, final String cancelCaption,
            final String notOKCaption, final Listener listener) {
        ConfirmDialog d = getFactory().create(windowCaption, message,
                okCaption, cancelCaption, notOKCaption);
        d.show(ui, listener, true);
        return d;
    }

    /**
     * Show a modal ConfirmDialog in a window.
     *
     * @param parentWindow
     *            Main level window.
     * @param windowCaption
     *            Caption for the confirmation dialog window.
     * @param message
     *            Message to display as window content.
     * @param okCaption
     *            Caption for the ok button.
     * @param cancelCaption
     *            Caption for cancel button.
     * @param listener
     *            Listener for dialog result.
     * @return
     */
    public static ConfirmDialog show(final UI ui,
            final String windowCaption, final String message,
            final String okCaption, final String cancelCaption,
            final Listener listener) {
        ConfirmDialog d = getFactory().create(windowCaption, message,
                okCaption, cancelCaption, null);
        d.show(ui, listener, true);
        return d;
    }

    /**
     * Shows a modal ConfirmDialog in given window and executes Runnable if OK
     * is chosen.
     *
     * @param parentWindow
     *            Main level window.
     * @param windowCaption
     *            Caption for the confirmation dialog window.
     * @param message
     *            Message to display as window content.
     * @param okCaption
     *            Caption for the ok button.
     * @param cancelCaption
     *            Caption for cancel button.
     * @param r
     *            Runnable to be run if confirmed
     * @return
     */
    public static ConfirmDialog show(final UI ui,
            final String windowCaption, final String message,
            final String okCaption, final String cancelCaption, final Runnable r) {
        ConfirmDialog d = getFactory().create(windowCaption, message,
                okCaption, cancelCaption, null);
        d.show(ui, new Listener() {
            private static final long serialVersionUID = 1L;

            public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                    r.run();
                }
            }
        }, true);
        return d;
    }

    private Listener confirmListener = null;
    private Boolean isConfirmed = null;
    private Label messageLabel = null;
    private Button okBtn = null;
    private Button cancelBtn = null;
    private String originalMessageText;
    private ContentMode msgContentMode = ContentMode.TEXT_WITH_NEWLINES;

    /**
     * Show confirm dialog.
     *
     * @param listener
     */
    public final void show(final UI ui, final Listener listener,
            final boolean modal) {
        confirmListener = listener;
        center();
        setModal(modal);
        ui.addWindow(this);
    }

    /**
     * Did the user confirm the dialog.
     *
     * @return
     */
    public final boolean isConfirmed() {
        return isConfirmed != null && isConfirmed;
    }

    /**
     * Did the user cancel the dialog.
     * 
     * @return
     */
    public final boolean isCanceled() {
        return isConfirmed == null;
    }

    public final Listener getListener() {
        return confirmListener;
    }

    protected final void setOkButton(final Button okButton) {
        okBtn = okButton;
    }

    public final Button getOkButton() {
        return okBtn;
    }

    protected final void setCancelButton(final Button cancelButton) {
        cancelBtn = cancelButton;
    }

    public final Button getCancelButton() {
        return cancelBtn;
    }

    protected final void setMessageLabel(final Label message) {
        messageLabel = message;
    }

    public final void setMessage(final String message) {
        originalMessageText = message;
        messageLabel
                .setValue(ContentMode.TEXT_WITH_NEWLINES == msgContentMode ? formatDialogMessage(message)
                        : message);
    }

    public final String getMessage() {
        return originalMessageText;
    }

    public final ContentMode getContentMode() {
        return msgContentMode;
    }

    public final void setContentMode(final ContentMode contentMode) {
        msgContentMode = contentMode;
        com.vaadin.shared.ui.label.ContentMode labelContentMode = com.vaadin.shared.ui.label.ContentMode.TEXT;
        switch (contentMode) {
        case TEXT_WITH_NEWLINES:
        case TEXT:
            labelContentMode = com.vaadin.shared.ui.label.ContentMode.TEXT;
            break;
        case PREFORMATTED:
            labelContentMode = com.vaadin.shared.ui.label.ContentMode.PREFORMATTED;
            break;
        case HTML:
            labelContentMode = com.vaadin.shared.ui.label.ContentMode.HTML;
            break;
        }
        messageLabel
                .setContentMode(labelContentMode);
        messageLabel
                .setValue(contentMode == ContentMode.TEXT_WITH_NEWLINES ? formatDialogMessage(originalMessageText)
                        : originalMessageText);
    }

    /**
     * Format the messageLabel by maintaining text only.
     *
     * @param text
     * @return
     */
    protected final String formatDialogMessage(final String text) {
        return JsonPaintTarget.escapeXML(text).replaceAll("\n", "<br />");
    }

    /**
     * Set the isConfirmed state.
     *
     * Note: this should only be called internally by the listeners.
     *
     * @param isConfirmed
     */
    protected final void setConfirmed(final boolean confirmed) {
        isConfirmed = confirmed;
    }
}
