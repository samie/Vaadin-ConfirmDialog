package org.vaadin.dialogs.test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.Application;
import com.vaadin.RootRequiresMoreInformationException;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;

@SuppressWarnings("serial")
public class ConfirmTestApplication extends Root {

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

	public static class Servlet extends AbstractApplicationServlet {
		@Override
		protected Application getNewApplication(HttpServletRequest request)
				throws ServletException {
			return new DummyApplication();
		}

		@Override
		protected Class<? extends Application> getApplicationClass()
				throws ClassNotFoundException {
			return DummyApplication.class;
		}
	}

	 public static class DummyApplication extends Application {
	        private static final long serialVersionUID = -1099516579868329607L;

	        @Override
	        protected Root getRoot(WrappedRequest request) throws RootRequiresMoreInformationException {
	            return new ConfirmTestApplication();
	        }
	 }


	@Override
	protected void init(WrappedRequest request) {
		getPage().setTitle("Example and test application");
		Label label = new Label("Hello Vaadin user");
		addComponent(label);
		addBasicExample();

	}

	private void addBasicExample() {
		Button button = new Button("Basic");
		button.setDebugId("basic");
		button.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				// The quickest way to confirm
				ConfirmDialog.show(getRoot(), MESSAGE_1,
				        new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                    // Confirmed to continue
				                    feedback(dialog.isConfirmed());
				                } else {
				                    // User did not confirm
				                    feedback(dialog.isConfirmed());
				                }
				            }
				        });
			}
		});
		getRoot().addComponent(button);
	}

	private void feedback(boolean confirmed) {
		getRoot().showNotification("Confirmed:" + confirmed);
	}


}
