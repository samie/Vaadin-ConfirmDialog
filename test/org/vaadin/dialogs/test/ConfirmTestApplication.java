package org.vaadin.dialogs.test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.vaadin.dialogs.ConfirmDialog;

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
        handler.addServlet(VaadinServlet.class, "/*");
        server.setHandler(handler);
        server.start();
        return server;
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Example and test application");
        VerticalLayout verticalLayout = new VerticalLayout();
        Label label = new Label("Hello Vaadin user");
        verticalLayout.addComponent(label);
        setContent(verticalLayout);
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
        ((VerticalLayout)getUI().getContent()).addComponent(button);
    }

}
