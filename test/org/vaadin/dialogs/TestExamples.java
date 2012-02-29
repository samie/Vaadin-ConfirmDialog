package org.vaadin.dialogs;


import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vaadin.dialogs.test.ConfirmTestApplication;

/**
 * WebDriver tests against the demo/example app.
 */
public class TestExamples {

        private Server startInEmbeddedJetty;
        private FirefoxDriver driver;

        @Before
        public void startServer() throws Exception {
                startInEmbeddedJetty = ConfirmTestApplication.startInEmbeddedJetty();
                driver = new FirefoxDriver();
        }

        @After
        public void stopServer() throws Exception {
                if (startInEmbeddedJetty != null) {
                        startInEmbeddedJetty.stop();
                }
                driver.close();
        }

        @Test
        public void basicTest() {
                openTestApp();
                WebElement b = driver.findElement(By.id("basic"));
                b.click();


                WebElement window = driver.findElement(By.className("v-window"));

                assertTrue(window.getText().contains(ConfirmTestApplication.MESSAGE_1));

                List<WebElement> findElements = window.findElements(By.className("v-button"));
                WebElement cancel = findElements.get(0);
                cancel.click();

                WebElement n = driver.findElement(By.className("v-Notification"));

                assertTrue(n.getText().contains("false"));


        }

        private void openTestApp() {
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                driver.get("http://localhost:8888");
        }

}
