package org.vaadin.dialogs;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vaadin.dialogs.test.ConfirmDialogTestUI;

/**
 * WebDriver tests against the demo/example app.
 */
public class TestConfirmDialog extends UITest {


    @BeforeClass
    public static void setup() {
        // Test ui setup using Firefox driver
        openTestUI(ConfirmDialogTestUI.class, new FirefoxDriver());
    }

    /**
     * Basic test case.
     *
     * Opens dialog and presses cancel and true
     */
    @Test
    public void testCancel() {

        reloadPage();
        WebDriver d = getDriver();

        // Open confirm dialog
        WebElement b = d.findElement(By.id(ConfirmDialogTestUI.BUTTON_1));
        b.click();

        WebElement window = findConfirmDialog(d);
        assertTrue(window.getText().contains(ConfirmDialogTestUI.MESSAGE_1));

        // Click the cancel button
        WebElement cancel = window.findElement(By.id(ConfirmDialog.CANCEL_ID));
        cancel.click();

        assertTrue(findNotification(d).getText().contains("false"));
    }

    /**
     * Basic test case.
     *
     * Opens dialog and presses cancel and true
     */
    @Test
    public void testOk() {

        reloadPage();
        WebDriver d = getDriver();

        // Open confirm dialog
        WebElement b = d.findElement(By.id(ConfirmDialogTestUI.BUTTON_1));
        b.click();

        WebElement window = findConfirmDialog(d);
        assertTrue(window.getText().contains(ConfirmDialogTestUI.MESSAGE_1));

        // Click the cancel button
        WebElement ok = window.findElement(By.id(ConfirmDialog.OK_ID));
        ok.click();

        assertTrue(findNotification(d).getText().contains("true"));

    }

    /**
     * Basic test case.
     *
     */
    @Test
    public void testEmptyMessage() {

        reloadPage();
        WebDriver d = getDriver();

        // Open confirm dialog
        WebElement b = d.findElement(By.id(ConfirmDialogTestUI.BUTTON_2));
        b.click();

        // Find the dialog
        WebElement window = findConfirmDialog(d);

        // Click the cancel button
        WebElement ok = window.findElement(By.id(ConfirmDialog.OK_ID));
        ok.click();

    }

    /** Get the ConfirmDialog sub-window element.
     *
     * @param d
     * @return
     */
    private WebElement findConfirmDialog(WebDriver d) {
        WebElement window = d.findElement(By.id(ConfirmDialog.DIALOG_ID));
        return window;
    }

    /** Get the last notification.
     *
     * @param d
     * @return
     */
    private static WebElement findNotification(WebDriver d) {
        List<WebElement> n = d.findElements(By.className("v-Notification"));
        WebElement last = n.get(n.size()-1);
        return last;
    }

}
