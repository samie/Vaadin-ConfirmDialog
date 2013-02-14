package org.vaadin.dialogs;

import static org.junit.Assert.assertTrue;

import java.util.List;

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


    /** Basic test case.
     *
     * Opens
     */
    @Test
    public void basicTest() {

        // Test ui setup using Firefox driver
        openTestUI(ConfirmDialogTestUI.class, new FirefoxDriver());

        WebDriver d = getDriver();
        WebElement b = d.findElement(By.id("basic"));

        // Open confirm dialog
        b.click();

        // Find the dialog
        WebElement window = d.findElement(By.className("v-window"));
        assertTrue(window.getText().contains(ConfirmDialogTestUI.MESSAGE_1));

        // Click the cancel button
        List<WebElement> findElements = window.findElements(By
                .className("v-button"));
        WebElement cancel = findElements.get(0);
        cancel.click();

        // Find notification
        WebElement n = d.findElement(By.className("v-Notification"));
        assertTrue(n.getText().contains("false"));

    }

}
