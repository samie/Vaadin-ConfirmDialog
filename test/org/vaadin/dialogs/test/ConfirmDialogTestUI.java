package org.vaadin.dialogs.test;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import java.util.List;

@SuppressWarnings("serial")
public class ConfirmDialogTestUI extends UI {

    public static final String OPEN_BUTTON_1 = "confirm_1";
    public static final String OPEN_BUTTON_2 = "confirm_2";
    public static final String OPEN_BUTTON_3 = "confirm_3";

    public static final String MESSAGE_1 = "This is the question?";
    public static final String MESSAGE_2 = null;
    private VerticalLayout root;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Example and test application");
        setContent(root = new VerticalLayout());
        Label label = new Label("Hello Vaadin user");
        addComponent(label);
        addBasicExample();
        addNullMessageExample();
        addThreeWayExample();
        addButtonSwapExample();
    }

    private void addComponent(Component c) {
        root.addComponent(c);
    }

    private void addBasicExample() {
        Button button = new Button("Click " + OPEN_BUTTON_1);
        button.setId(OPEN_BUTTON_1);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), MESSAGE_1,
                        new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    Notification.show("Confirmed:"
                                            + dialog.isConfirmed());
                                } else {
                                    Notification.show("Confirmed:"
                                            + dialog.isConfirmed());
                                }
                            }
                        });
            }
        });
        addComponent(button);
    }

    private void addNullMessageExample() {
        Button button = new Button("Click " + OPEN_BUTTON_2);
        button.setId(OPEN_BUTTON_2);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), null, new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        } else {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        }
                    }
                });
            }
        });
        addComponent(button);
    }

    private void addThreeWayExample() {
        Button button = new Button("Click " + OPEN_BUTTON_3);
        button.setId(OPEN_BUTTON_3);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), "Title", MESSAGE_1, "foo", "cancel",
                        "bar", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed()+" Canceled:"+dialog.isCanceled());
                    }
                });
            }
        });
        addComponent(button);
    }

    private void addButtonSwapExample() {
        Button button = new Button("Click " + OPEN_BUTTON_3);

        button.setId(OPEN_BUTTON_3);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {

                ConfirmDialog.Factory currentFactory = ConfirmDialog.getFactory();
                ConfirmDialog.setFactory(new DefaultConfirmDialogFactory() {

                    @Override
                    protected List<Button> orderButtons(Button cancel, Button notOk, Button ok) {
                        return super.orderButtons(cancel, ok, notOk);
                    }
                });

                // The quickest way to confirm
                ConfirmDialog.show(getUI(), "Title", MESSAGE_1, "foo", "cancel",
                        "bar", new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                Notification.show("Confirmed:"
                                        + dialog.isConfirmed()+" Canceled:"+dialog.isCanceled());
                            }
                        });

                ConfirmDialog.setFactory(currentFactory);
            }
        });
        addComponent(button);
    }
}
