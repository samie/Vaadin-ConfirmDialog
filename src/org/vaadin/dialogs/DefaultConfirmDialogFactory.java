package org.vaadin.dialogs;

import java.text.NumberFormat;
import java.util.Locale;

import org.vaadin.dialogs.ConfirmDialog.Factory;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.Reindeer;

/**
 * This is the default implementation for confirmation dialog factory.
 *
 * This supports text only content and tries to approximate the the dialog size.
 *
 * TODO: Allow configuration of min and max sizes.
 *
 * @author Sami Ekblad
 *
 */
public class DefaultConfirmDialogFactory implements Factory {

    /** Generated serial UID. */
    private static final long serialVersionUID = -5412321247707480466L;

    // System wide defaults
    protected static final String DEFAULT_CAPTION = "Confirm";
    protected static final String DEFAULT_MESSAGE = "Are You sure?";
    protected static final String DEFAULT_OK_CAPTION = "Ok";
    protected static final String DEFAULT_CANCEL_CAPTION = "Cancel";

    // System wide defaults
    private static final double MIN_WIDTH = 20d;
    private static final double MAX_WIDTH = 40d;
    private static final double MIN_HEIGHT = 1d;
    private static final double MAX_HEIGHT = 30d;
    private static final double BUTTON_HEIGHT = 2.5;

    public ConfirmDialog create(final String caption, final String message,
            final String okCaption, final String cancelCaption) {

        // Create a confirm dialog
        final ConfirmDialog confirm = new ConfirmDialog();
        confirm.setCaption(caption != null ? caption : DEFAULT_CAPTION);

        // Close listener implementation
        confirm.addCloseListener(new Window.CloseListener() {

            private static final long serialVersionUID = 1971800928047045825L;

            public void windowClose(CloseEvent ce) {

                // Only process if still enabled
                if (confirm.isEnabled()) {
                    confirm.setEnabled(false); // avoid double processing
                    confirm.setConfirmed(false);
                    if (confirm.getListener() != null) {
                        confirm.getListener().onClose(confirm);
                    }
                }
            }
        });

        // Create content
        VerticalLayout c = (VerticalLayout) confirm.getContent();
        c.setSizeFull();
        c.setSpacing(true);

        // Panel for scrolling lengthy messages.
        Panel scroll = new Panel(new VerticalLayout());
        //TODO: What now Vaadin7? scroll.setScrollable(true);
        c.addComponent(scroll);
        scroll.setWidth("100%");
        scroll.setHeight("100%");
        scroll.setStyleName(Reindeer.PANEL_LIGHT);
        c.setExpandRatio(scroll, 1f);

        // Always HTML, but escape
        Label text = new Label("", com.vaadin.shared.ui.label.ContentMode.HTML);
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(text);
        scroll.setContent(layout);
        confirm.setMessageLabel(text);
        confirm.setMessage(message);

        HorizontalLayout buttons = new HorizontalLayout();
        c.addComponent(buttons);
        buttons.setSpacing(true);

        buttons.setHeight(format(BUTTON_HEIGHT) + "em");
        buttons.setWidth("100%");
        Label spacer = new Label("");
        buttons.addComponent(spacer);
        spacer.setWidth("100%");
        buttons.setExpandRatio(spacer, 1f);

        final Button cancel = new Button(cancelCaption != null ? cancelCaption
                : DEFAULT_CANCEL_CAPTION);
        cancel.setData(false);
        cancel.setClickShortcut(KeyCode.ESCAPE, null);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
        confirm.setCancelButton(cancel);

        final Button ok = new Button(okCaption != null ? okCaption
                : DEFAULT_OK_CAPTION);
        ok.setData(true);
        ok.setClickShortcut(KeyCode.ENTER, null);
        ok.setStyleName(Reindeer.BUTTON_DEFAULT);
        ok.focus();
        buttons.addComponent(ok);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        confirm.setOkButton(ok);

        // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;

            public void buttonClick(ClickEvent event) {
                // Copy the button date to window for passing through either
                // "OK" or "CANCEL". Only process id still enabled.
                if (confirm.isEnabled()) {
                    confirm.setEnabled(false); // Avoid double processing

                    confirm.setConfirmed(event.getButton() == ok);

                    // We need to cast this way, because of the backward
                    // compatibility issue in 6.4 series.
                    UI parent = confirm.getUI();
                    parent.removeWindow(confirm);

                    // This has to be invoked as the window.close
                    // event is not fired when removed.
                    if (confirm.getListener() != null) {
                        confirm.getListener().onClose(confirm);
                    }
                }

            }

        };
        cancel.addClickListener(cb);
        ok.addClickListener(cb);

        // Approximate the size of the dialog
        double[] dim = getDialogDimensions(message,
                ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES);
        confirm.setWidth(format(dim[0]) + "em");
        confirm.setHeight(format(dim[1]) + "em");
        confirm.setResizable(false);

        return confirm;
    }

    /**
     * Approximates the dialog dimensions based on its message length.
     *
     * @param message
     *            Message string
     * @return
     */
    protected double[] getDialogDimensions(String message, ConfirmDialog.ContentMode style) {

        // Based on Reindeer style:
        double chrW = 0.5d;
        double chrH = 1.5d;
        double length = chrW * message.length();
        double rows = Math.ceil(length / MAX_WIDTH);

        // Estimate extra lines
        if (style == ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES) {
            rows += count("\n", message);
        }

        // System.out.println(message.length() + " = " + length + "em");
        // System.out.println("Rows: " + (length / MAX_WIDTH) + " = " + rows);

        // Obey maximum size
        double width = Math.min(MAX_WIDTH, length);
        double height = Math.ceil(Math.min(MAX_HEIGHT, rows * chrH));

        // Obey the minimum size
        width = Math.max(width, MIN_WIDTH);
        height = Math.max(height, MIN_HEIGHT);

        // Based on Reindeer style:
        double btnHeight = 2.5d;
        double vmargin = 8d;
        double hmargin = 2d;

        double[] res = new double[] { width + hmargin,
                height + btnHeight + vmargin };
        // System.out.println(res[0] + "," + res[1]);
        return res;
    }

    /**
     * Count the number of needles within a haystack.
     *
     * @param needle
     *            The string to search for.
     * @param haystack
     *            The string to process.
     * @return
     */
    private static int count(final String needle, final String haystack) {
        int count = 0;
        int pos = -1;
        while ((pos = haystack.indexOf(needle, pos + 1)) >= 0) {
            count++;
        }
        return count;
    }

    /**
     * Format a double single fraction digit.
     *
     * @param n
     * @return
     */
    private String format(double n) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        return nf.format(n);
    }

}
