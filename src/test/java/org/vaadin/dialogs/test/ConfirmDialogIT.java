package org.vaadin.dialogs.test;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.LabelElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * WebDriver tests against the demo/example app.
 */
public class ConfirmDialogIT extends AbstractViewTest {

    /**
     * Opens dialog and presses cancel and true.
     */
    @Test
    public void testTextContent() {

        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(DivElement.class).id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(ConfirmDialogTestUI.MESSAGE_1_SHORT));

        // Click the cancel button in dialog
        clickButton(ConfirmDialog.CANCEL_ID);

    }

    /**
     * Test that dialog works with empty (null) content.
     */
    @Test
    public void testEmptyTextContent() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_4_NULL);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(DivElement.class).id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(""));

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID);
    }

    /**
     * Opens dialog and presses ok.
     */
    @Test
    public void testOk() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.OK_ID);

        // Assert notification value
        assertTrue(findNotification().getText().contains("true"));

    }

    /**
     * Opens dialog and presses enter key.
     */
    @Test
    public void testEnterKey() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);


        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Press enter key
        getDriver().switchTo().activeElement().sendKeys(Keys.ENTER);

        // Assert notification value
        assertTrue(findNotification().getText().contains("true"));

    }

    /**
     * Opens dialog and presses escape key.
     */
    @Test
    public void testEscapeKey() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Press escape key
        getDriver().switchTo().activeElement().sendKeys(Keys.ESCAPE);

        // Assert notification value
        assertTrue(findNotification().getText().contains("false"));

    }

    /**
     * Opens dialog and presses cancel.
     */
    @Test
    public void testCancel() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID);

        // Assert notification value
        assertTrue(findNotification().getText().contains("false"));

    }



    /**
     * Opens dialog and presses cancel.
     */
    @Test
    public void testThreeWayOK() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_5_3WAY);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Click the ok button
        clickButton(ConfirmDialog.OK_ID);

        // Assert notification value
        assertTrue(findNotification().getText().contains("Confirmed:true"));
        assertTrue(findNotification().getText().contains("Canceled:false"));
    }

    /**
     * Opens dialog and presses cancel.
     */
    @Test
    public void testThreeWayCancel() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_5_3WAY);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID);

        // Assert notification value
        assertTrue(findNotification().getText().contains("Confirmed:false"));
        assertTrue(findNotification().getText().contains("Canceled:true"));
    }

    /**
     * Opens dialog and presses cancel.
     */
    @Test
    public void testThreeWayNotOK() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_5_3WAY);

        // Get the dialog
        DialogElement dialog = findConfirmDialog();

        // Click the not_ok button
        clickButton(ConfirmDialog.NOT_OK_ID);

        // Assert notification value
        assertTrue(findNotification().getText().contains("Confirmed:false"));
        assertTrue(findNotification().getText().contains("Canceled:false"));
    }

    private void clickButton(String id) {
        $(ButtonElement.class).id(id).click();
    }

    /**
     * Find the the confirmation dialog.
     *
     * @return WindowElement for the dialog
     */
    private DialogElement findConfirmDialog() {
        DialogElement d = $(DialogElement.class).id(ConfirmDialog.DIALOG_ID);
        assertNotNull("Could not find the dialog",d);
        return d;
    }

    /**
     * Get the last notification.
     *
     * @return
     */
    private NotificationElement findNotification() {
        NotificationElement n = $(NotificationElement.class).first();
        assertNotNull("Could not find the notification",n);
        return n;
    }
}
