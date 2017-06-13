package org.vaadin.dialogs;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.vaadin.dialogs.test.ConfirmDialogTestUI;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.WindowElement;
import org.eclipse.jetty.server.Server;
import org.vaadin.addonhelpers.TServer;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

/**
 * WebDriver tests against the demo/example app.
 */
public class ConfirmDialogIT extends TestBenchTestCase {

    // host and port configuration for the URL
    private static int PORT = 8889;
    private static String URL = "http://localhost:" + PORT + "/" + ConfirmDialogTestUI.class.getName();

    private static WebDriver commonDriver;
    private static Server server;

    @BeforeClass
    public static void beforeAllTests() throws Exception {
        // Start the server
        server = new TServer().startServer(PORT);

        // Create a single webdriver
        commonDriver = TestBench.createDriver(new ChromeDriver());
        commonDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void afterAllTests() throws Exception {
        // Stop the browser
        if (commonDriver != null) {
            commonDriver.quit();
        }

        // Stop the server
        server.stop();
    }

    @Before
    public void beforeTest() {
        if (getDriver() == null) {
            setDriver(commonDriver);
        }

        // Reload to make sure we have a clean start
        reloadPage();
    }

    /**
     * Opens dialog and presses cancel and true.
     */
    @Test
    public void testTextContent() {

        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(LabelElement.class).context(dialog)
                .id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(ConfirmDialogTestUI.MESSAGE_1_SHORT));

        // Click the cancel button in dialog
        clickButton(ConfirmDialog.CANCEL_ID, dialog);

    }

    /**
     * Test that dialog works with empty (null) content.
     */
    @Test
    public void testEmptyTextContent() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_4_NULL);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Assert message content in dialog
        String text = $(LabelElement.class).context(dialog)
                .id(ConfirmDialog.MESSAGE_ID).getText();
        assertTrue(text.equals(""));

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID, dialog);
    }

    /**
     * Opens dialog and presses ok.
     */
    @Test
    public void testOk() {

        // Open confirm dialog
        clickButton(ConfirmDialogTestUI.OPEN_BUTTON_1_SHORT);

        // Get the dialog
        WindowElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.OK_ID, dialog);

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
        WindowElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID, dialog);

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
        WindowElement dialog = findConfirmDialog();

        // Click the ok button
        clickButton(ConfirmDialog.OK_ID, dialog);

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
        WindowElement dialog = findConfirmDialog();

        // Click the cancel button
        clickButton(ConfirmDialog.CANCEL_ID, dialog);

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
        WindowElement dialog = findConfirmDialog();

        // Click the not_ok button
        clickButton(ConfirmDialog.NOT_OK_ID, dialog);

        // Assert notification value
        assertTrue(findNotification().getText().contains("Confirmed:false"));
        assertTrue(findNotification().getText().contains("Canceled:false"));
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
     * @return WindowElement for the dialog
     */
    private WindowElement findConfirmDialog() {
        return $(WindowElement.class).id(ConfirmDialog.DIALOG_ID);
    }

    /**
     * Get the last notification.
     *
     * @return
     */
    private WebElement findNotification() {
        List<WebElement> n = getDriver().findElements(
                By.className("v-Notification"));
        if (n.size() >0 ) {
            WebElement last = n.get(n.size() - 1);
            return last;            
        } else {
            return null;
        }
        
    }

    /**
     * Reloads the page. Depending on UI configuration this might re-init the UI
     * or keep the state.
     *
     * @see #restartApplication()
     */
    protected void reloadPage() {

        getDriver().get(URL);
    }

    /**
     * Restarts the Vaadin application using ?restartApplication parameter.
     *
     * @see #reloadPage()
     */
    protected void restartApplication() {
        getDriver().get(URL + "?restartApplication");
    }

}
