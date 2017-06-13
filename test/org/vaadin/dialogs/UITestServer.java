package org.vaadin.dialogs;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.test.ConfirmDialogTestUI;

/**
 * Embedded Jetty test servlet container for testing a single UI class.
 *
 *
 * @author Sami Ekblad
 *
 */
public class UITestServer extends Server {

    public final static int DEFAULT_SERVER_PORT = 8888;

    private static Class<? extends UI> testUI;

    private static UITestServer instance;

    private UITestServer() {
    }

    private UITestServer(int serverPort) {
        super(serverPort);
    }

    public static void main(String... args) {
        UITestServer.runUIServer(ConfirmDialogTestUI.class);
    }

    @AfterClass
    public static void shutdown() {
        if (instance != null) {
            try {
                instance.stop();
            } catch (Exception letsJustIgnoreThis) {
                letsJustIgnoreThis.printStackTrace();
            }
        }
        instance = null;
    }

    /**
     * Open a UI for testing.
     *
     * This instantiates a UI, runs an UI instance in a embedded server on port
     * {@value #DEFAULT_SERVER_PORT}
     *
     * @param testUI Vaadin UI to be tested
     */
    public static void runUIServer(Class<? extends UI> testUI) {
        runUIServer(testUI, DEFAULT_SERVER_PORT);
    }

    /**
     * Open a UI for testing.
     *
     * This instantiates a UI, runs an UI instance in a embedded server.
     *
     * @param testUI Vaadin UI to be tested
     * @param serverPort HTTP port
     */
    public static void runUIServer(Class<? extends UI> testUI, int serverPort) {

        if (instance != null) {
            throw new IllegalStateException(
                    "Server already running on this JVM.");
        }

        // Start the instance for testing
        try {

            instance = new UITestServer(serverPort);
            UITestServer.testUI = testUI;
            ServletContextHandler handler = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            handler.addServlet(Servlet.class, "/*");
            instance.setHandler(handler);
            instance.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start embedded instance", e);
        }
    }

    /**
     * Generic Vaadin servlet for running the UI.
     *
     * @author Sami Ekblad
     *
     */
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

    public static String getServerUrl() {
        return "http://localhost:"
                + (instance != null ? instance.getConnectors()[0].getPort()
                : DEFAULT_SERVER_PORT);
    }

}
