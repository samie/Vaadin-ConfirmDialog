package org.vaadin.dialogs;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vaadin.dialogs.test.ConfirmDialogTestUI;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.WindowElement;

/**
 * WebDriver tests against the demo/example app.
 */
public class TestConfirmDialog extends TestBenchTestCase {

    private static WebDriver commonDriver;

    @BeforeClass
    public static void beforeAllTests() {
        // Start the server
        UITestServer.runUIServer(ConfirmDialogTestUI.class);

        // Create a single webdriver
        commonDriver = TestBench.createDriver(new FirefoxDriver());
        commonDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void afterAllTests() {
        // Stop the browser
        if (commonDriver != null) {
            commonDriver.quit();
        }

        // Stop the server
        UITestServer.shutdown();
    }

    @Before
    public void beforeTest() {
        if (getDriver() == null) {
            setDriver(commonDriver);
        }

        // Reload to make sure we have a clean start
        reloadPage();
    }

    /** Opens dialog and presses cancel and true. */
    @Test
    public void testTextContent() {

        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(LabelElement.class).context(dialog)
                .id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(ConfirmDialogTestUI.MESSAGE_1));

        // Click the cancel button in dialog
        clickButton(ConfirmDialog.CANCEL_ID, dialog);

    }

    /** Test that dialog works with empty (null) content. */
    @Test
    public void testEmptyTextContent() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_2);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(LabelElement.class).context(dialog)
                .id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(""));

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID, dialog);
    }

    /** Opens dialog and presses ok. */
    @Test
    public void testOk() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.OK_ID, dialog);

        // Assert notification value
        assertTrue(findNotification().getText().contains("true"));

    }

    /** Opens dialog and presses cancel. */
    @Test
    public void testCancel() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID, dialog);

        // Assert notification value
        assertTrue(findNotification().getText().contains("false"));

    }

    private void clickButton(String id, WebElement inContext) {
        $(ButtonElement.class).context(inContext).id(id).click();
    }

    private void clickButton(String id) {
        $(ButtonElement.class).id(id).click();
    }

    /**
     * Find the the confirmation dialog.
     *
     * @param d
     * @return WindowElement for the dialog
     */
    private WindowElement findConfirmDialog() {
        return $(WindowElement.class).id(ConfirmDialog.DIALOG_ID);
    }

    /**
     * Get the last notification.
     *
     * @param d
     * @return
     */
    private WebElement findNotification() {
        List<WebElement> n = getDriver().findElements(
                By.className("v-Notification"));
        WebElement last = n.get(n.size() - 1);
        return last;
    }

    /**
     * Reloads the page. Depending on UI configuration this might re-init the UI
     * or keep the state.
     *
     * @see #restartApplication()
     */
    protected void reloadPage() {
        getDriver().get(UITestServer.getServerUrl());
    }

    /**
     * Restarts the Vaadin application using ?restartApplication parameter.
     *
     * @see #reloadPage()
     */
    protected void restartApplication() {
        getDriver().get(UITestServer.getServerUrl() + "?restartApplication");
    }

}
