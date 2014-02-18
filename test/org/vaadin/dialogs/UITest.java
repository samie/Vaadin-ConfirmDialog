package org.vaadin.dialogs;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * Generic UI test automation class.
 *
 * Extend this class to create automated UI tests with Vaadin TestBench or with
 * Selenium.
 *
 * @author Sami Ekblad
 *
 */
public class UITest {
    private static final int DEFAULT_SERVER_PORT = 8888;

    private static Server server;

    private static WebDriver driver;

    private static int serverPort = DEFAULT_SERVER_PORT;

    private static Class<? extends UI> testUI;

    public UITest() {
    }

    public static void setServerPort(int serverPort) {
        UITest.serverPort = serverPort;
    }

    @AfterClass
    public static void stopBrowser() throws Exception {
        if (driver != null) {
            driver.close();
        }
        driver = null;
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
        server = null;
    }

    /**
     * Open a UI for testing.
     *
     * This instantiates a UI, runs a server and
     *
     * @param testUI
     * @param driver
     */
    protected static void openTestUI(Class<? extends UI> testUI, WebDriver driver) {
        UITest.driver = driver;

        // Start the server for testing
        try {
            server = startInEmbeddedJetty(testUI);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start embedded server", e);
        }

        // Open the WebDriver in the server URLs
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        reloadPage();
    }

    protected static void reloadPage() {
        driver.get("http://localhost:" + serverPort + "/");
    }

    private static Server startInEmbeddedJetty(Class<? extends UI> testUI)
            throws Exception {
        Server server = new Server(serverPort);
        UITest.testUI = testUI;
        ServletContextHandler handler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        handler.addServlet(Servlet.class, "/*");
        server.setHandler(handler);
        server.start();
        return server;
    }

    public static class Servlet extends VaadinServlet {

        private static final long serialVersionUID = 1L;

        private UIProvider uiprovider = new UIProvider() {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
                return testUI;
            }
        };

        @Override
        public void init(ServletConfig servletConfig) throws ServletException {
            super.init(servletConfig);
            super.getService().addSessionInitListener(
                    new SessionInitListener() {

                        private static final long serialVersionUID = 1L;

                        public void sessionInit(SessionInitEvent event)
                                throws ServiceException {
                            event.getSession().addUIProvider(uiprovider);
                        }
                    });
        }
    }

    protected static WebDriver getDriver() {
        return driver;
    }

}
