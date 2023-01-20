package org.vaadin.dialogs;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog.Factory;

/**
 * This is the default implementation for confirmation dialog factory.
 * This supports text only content and tries to approximate the the dialog size.
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

    // System-wide defaults of text space
    private static final double MIN_WIDTH = 28d; // ch
    private static final double MAX_WIDTH_LONG = 80d; // ch
    private static final double MAX_WIDTH_SHORT = 40d; // ch
    private static final double MIN_HEIGHT = 1d; // em
    private static final double MAX_HEIGHT = 40d; // em
    private static final int LONG_MESSAGE_LIMIT = (int) Math.ceil(MAX_WIDTH_SHORT * 5);


    public ConfirmDialog create(final String caption, final String message,
            final String okCaption, final String cancelCaption,
            final String notOkCaption) {

        final boolean threeWay = notOkCaption != null;
        // Create a confirm dialog
        final ConfirmDialog confirm = new ConfirmDialog();
        confirm.setCloseOnEsc(true);
        confirm.setCloseOnOutsideClick(false);
        confirm.setId(ConfirmDialog.DIALOG_ID);

        // Close listener implementation
        confirm.addDialogCloseActionListener((e) -> {

                // Only process if still enabled
                if (confirm.isEnabled()) {
                    confirm.setEnabled(false); // avoid double processing
                    confirm.close();
                    if (!threeWay) {
                        confirm.setConfirmed(false);
                    }
                    if (confirm.getListener() != null) {
                        confirm.getListener().onClose(confirm);
                    }
                }
        });

        // Create content
        VerticalLayout c = new VerticalLayout();
        c.setMargin(false);
        c.setPadding(false);
        c.setSizeFull();
        confirm.add(c);

        // Caption
        H2 captionHeader = new H2(caption);
        captionHeader.getStyle().set("font-size", "1.5em").set("font-weight", "bold");
        c.add(captionHeader);


        // Scroller for scrolling lengthy messages.
        Div scrollContent = new Div();
        Scroller scroller = new Scroller(scrollContent);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.setSizeFull();
        scroller.getStyle().set("margin","0");
        c.add(scroller);

        // Always HTML, but escape
        ConfirmDialog.Label captionLabel = new ConfirmDialog.Label("");
        captionLabel.setWidthFull();
        ConfirmDialog.Label textLabel = new ConfirmDialog.Label("");
        textLabel.setWidthFull();
        textLabel.setId(ConfirmDialog.MESSAGE_ID);
        scrollContent.add(textLabel);

        confirm.setCaptionLabel(captionLabel);
        confirm.setMessageLabel(textLabel);

        confirm.setCaption(caption != null ? caption : DEFAULT_CAPTION);
        confirm.setMessage(message != null ? message : "");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setSpacing(true);
        Footer footer = new Footer(buttons);
        footer.setWidth("100%");
        c.add(footer);

        final Button cancel = buildCancelButton(cancelCaption);
        confirm.setCancelButton(cancel);

        Button notOk = null;
        if (threeWay) {
            notOk = buildNotOkButton(notOkCaption);
            confirm.setNotOkButton(notOk);
        }

        final Button ok = buildOkButton(okCaption);
        confirm.setOkButton(ok);

        // Create a listener for buttons
        ComponentEventListener<ClickEvent<Button>> cb = new ComponentEventListener<ClickEvent<Button>>() {

            private static final long serialVersionUID = 3525060915814334881L;

            @Override
            public void onComponentEvent(ClickEvent<Button> event) {

                // Copy the button date to window for passing through either
                // "OK" or "CANCEL". Only process id still enabled.
                if (confirm.isEnabled()) {
                    confirm.setEnabled(false); // Avoid double processing

                    Button b = event.getSource();
                    if (b != cancel)
                        confirm.setConfirmed(b == ok);

                    confirm.getUI().ifPresent(ui -> {
                        ui.remove(confirm);
                    });

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
        if (notOk != null)
            notOk.addClickListener(cb);

        for (Button button : orderButtons(cancel, notOk, ok)) {
            if (button != null) {
                buttons.add(button);
            }
        }

        // Approximate the size of the dialog
        double[] dim = getDialogDimensions(caption, message,
                ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES);
        confirm.setWidth(format(dim[0]) + "ch");
        confirm.setHeight(format(dim[1]) + "em");
        confirm.setResizable(false);

        return confirm;
    }

    /**
     * This method allows overwriting the button order. It's provided with all
     * three buttons of the dialog (out of which some can be null) and it
     * returns a list of buttons in wanted order. This method can be overwritten to change the order of the buttons.
     * 
     * @param cancel Button for canceling the dialog
     * @param notOk Button for "not ok"
     * @param ok Button for "ok"
     * @return List of buttons in defined order.
     */
    protected List<Button> orderButtons(Button cancel, Button notOk,
            Button ok) {
        return Arrays.asList(cancel, notOk, ok);
    }

    /**
     * Builds new cancel button. This method can be used to overwrite the button
     * building.
     * 
     * @param cancelCaption Caption for the dialog cancel button
     * @return Button for cancel
     */
    protected Button buildCancelButton(String cancelCaption) {
        final Button cancel = new Button(
                cancelCaption != null ? cancelCaption : DEFAULT_CANCEL_CAPTION);
        cancel.setId(ConfirmDialog.CANCEL_ID);

        return cancel;
    }

    /**
     * Builds new "Not ok" button. This method can be used to overwrite the
     * button building.
     * 
     * @param notOkCaption Caption for the "not Ok" button
     * @return Button for "Not ok"
     */
    protected Button buildNotOkButton(String notOkCaption) {
        final Button notOk = new Button(notOkCaption);;
        notOk.setId(ConfirmDialog.NOT_OK_ID);
        return notOk;
    }

    /**
     * Builds new Ok button. This method can be used to overwrite the button
     * building.
     * 
     * @param okCaption Caption for the Ok button
     * @return Button for Ok
     */
    protected Button buildOkButton(String okCaption) {
        final Button ok = new Button(
                okCaption != null ? okCaption : DEFAULT_OK_CAPTION);
        ok.setId(ConfirmDialog.OK_ID);
        ok.addClickShortcut(Key.ENTER);
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        ok.setAutofocus(true);
        ok.focus();

        return ok;
    }

    /**
     * Approximates the dialog dimensions based on its message length.
     *
     * @param message
     *            the message string
     * @param style
     *            the content mode
     * @return approximate size for the dialog with given message
     */
    protected double[] getDialogDimensions(String title, String message,
            ConfirmDialog.ContentMode style) {

        double maxWidth = message != null && message.length() > LONG_MESSAGE_LIMIT ? MAX_WIDTH_LONG :MAX_WIDTH_SHORT;

        // We use 'ch' and 'em':
        double chrW = 1;
        double chrH = 1.6;
        double length = message != null ? chrW * message.length() : 0;
        double rows = Math.ceil(length / (maxWidth*1.40)); // line fits safely 40% more that ch says

        // Estimate extra lines
        if (style == ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES) {
            rows += message != null ? count("\n", message) : 0;
            rows += message != null ? count("</p>", message) : 0;
        }

        System.out.println("ROWS: "+rows);
            // Obey maximum size
        double width = Math.min(maxWidth, length);
        double height = Math.ceil(Math.min(MAX_HEIGHT, rows * chrH));

        // Obey the minimum size
        width = Math.max(width, MIN_WIDTH);
        height = Math.max(height, MIN_HEIGHT);

        // Handle title
        height += title != null && !title.isEmpty()? 2 : 0;

        // Based on Lumo style:
        double btnHeight = 5d; // em
        double vmargin = 3d; // em
        double hmargin = 4d; // ch

        double[] res = new double[] { width + hmargin,
                height + btnHeight + vmargin };
        return res;
    }

    /**
     * Count the number of needles within a haystack.
     *
     * @param needle
     *            The string to search for.
     * @param haystack
     *            The string to process.
     * @return count of needles within a haystack
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
