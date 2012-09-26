package org.vaadin.dialogs.test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ConfirmTestApplication extends UI {

    public static final String MESSAGE_1 = "This is the question?";

    public static void main(String[] args) throws Exception {
        startInEmbeddedJetty();
    }

    public static Server startInEmbeddedJetty() throws Exception {
        Server server = new Server(8888);
        ServletContextHandler handler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        handler.addServlet(Servlet.class, "/*");
        server.setHandler(handler);
        server.start();
        return server;
    }

    public static class Servlet extends VaadinServlet {
        @Override
        public void init(ServletConfig servletConfig) throws ServletException {

            super.init(servletConfig);

            super.getService().addSessionInitListener(
                    new SessionInitListener() {
                        public void sessionInit(
                                SessionInitEvent event)
                                throws ServiceException {
                            UIProvider uiprovider = new UIProvider() {

                                @Override
                                public Class<? extends UI> getUIClass(
                                        UIClassSelectionEvent event) {
                                    return ConfirmTestApplication.class;
                                }
                            };
                            getService().addUIProvider(event.getSession(),
                                    uiprovider);
                        }
                    });

        }
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Example and test application");
        Label label = new Label("Hello Vaadin user");
        addComponent(label);
        addBasicExample();

    }

    private void addBasicExample() {
        Button button = new Button("Basic");
        button.setId("basic");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), MESSAGE_1,
                        new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    // Confirmed to continue
                                    Notification.show("Confirmed:"
                                            + dialog.isConfirmed());
                                } else {
                                    // User did not confirm
                                    Notification.show("Confirmed:"
                                            + dialog.isConfirmed());
                                }
                            }
                        });
            }
        });
        getUI().addComponent(button);
    }

}
