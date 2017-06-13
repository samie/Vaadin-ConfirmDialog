package org.vaadin.dialogs.test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import junit.framework.TestCase;
import org.junit.Test;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Created by se on 13/06/2017.
 */
public class ConfirmDialogUnitTest extends TestCase {


    // Mock an UI instance
    UI ui = new UI() {
        @Override
        protected void init(VaadinRequest vaadinRequest) {
        }
    };

    @Test
    public void testTwoWayDialogButtons() {

        // Create an two-way dialog
        ConfirmDialog d = ConfirmDialog.show(ui, "window caption", "message","ok","cancel",dialog -> {

        });
        assertNotNull("Dialog creation failed",d);
        assertNotNull("OK button was null",d.getOkButton());
        assertNull("Not OK button should be null",d.getNotOkButton());
        assertNotNull("Cancel button was null",d.getCancelButton());

    }

    @Test
    public void testThreeWayDialogButtons() {

        // Create an three-way dialog
        ConfirmDialog d = ConfirmDialog.show(ui, "window caption", "message","ok","cancel","not ok",dialog -> {

        });
        assertNotNull("Dialog creation failed",d);
        assertNotNull("OK button was null",d.getOkButton());
        assertNotNull("Not OK button was null",d.getNotOkButton());
        assertNotNull("Cancel button was null",d.getCancelButton());

    }

}
