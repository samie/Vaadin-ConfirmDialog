package org.vaadin.dialogs;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
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

    private Server server;

    private WebDriver driver;

    private static Class<? extends UI> testUI;

    private int serverPort;

    public UITest() {
        setServerPort(DEFAULT_SERVER_PORT);
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @After
    public void stopBrowser() throws Exception {
        if (driver != null) {
            driver.close();
        }
        driver = null;
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    /**
     * Open a UI for testing.
     *
     * This instantiates a UI, runs a server and
     *
     * @param testUI
     * @param driver
     */
    protected void openTestUI(Class<? extends UI> testUI, WebDriver driver) {
        this.driver = driver;

        // Start the server for testing
        try {
            startInEmbeddedJetty(testUI);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start embedded server", e);
        }

        // Open the WebDriver in the server URLs
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get("http://localhost:" + serverPort + "/");
    }

    private Server startInEmbeddedJetty(Class<? extends UI> testUI)
            throws Exception {
        Server server = new Server(serverPort);
        this.testUI = testUI;
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

    protected WebDriver getDriver() {
        return driver;
    }

}
