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

    public static final String OPEN_BUTTON_1_SHORT = "confirm_1";
    public static final String OPEN_BUTTON_2_LONG = "confirm_2";
    public static final String OPEN_BUTTON_3_HTML = "confirm_3";
    public static final String OPEN_BUTTON_4_NULL = "confirm_4";
    public static final String OPEN_BUTTON_5_3WAY = "confirm_5";
    public static final String OPEN_BUTTON_6_SWAP = "confirm_6";

    public static final String MESSAGE_1_SHORT = "This is the question?";
    public static final String MESSAGE_2_LONG = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at turpis lectus. Phasellus magna nulla, fringilla et dui id, gravida elementum ligula. Aliquam dignissim tincidunt efficitur. Sed tincidunt sapien ut vehicula vehicula. Quisque faucibus vestibulum sem at facilisis. Nullam eros urna, consequat id nibh sed, suscipit eleifend metus. Ut congue dolor id nulla ultricies aliquam. Ut a lacinia orci. Donec congue sapien risus, semper consequat tortor pulvinar at. Nullam scelerisque, nibh et pretium cursus, lacus dolor bibendum eros, non volutpat magna elit quis dolor. Mauris vel tortor nulla. Fusce dapibus semper fermentum. Curabitur ac sagittis risus. In hac habitasse platea dictumst. Vestibulum iaculis fermentum orci, ultricies sodales dolor eleifend sed. Nulla accumsan, erat euismod sagittis eleifend, ex purus rutrum mi, vel accumsan quam sapien id erat." +
            "\n\n" + "Vivamus maximus tristique dapibus. Duis et congue mauris. Sed vestibulum, elit nec vehicula posuere, eros metus lobortis ex, egestas dapibus sem lorem nec ipsum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Pellentesque justo tellus, sodales nec lectus sit amet, euismod ultrices massa. Suspendisse potenti. Fusce lectus ligula, convallis et finibus ut, accumsan sit amet nisl. Donec dapibus molestie lorem. Nulla justo sem, imperdiet vitae rhoncus vel, gravida eu dui. Curabitur turpis ipsum, convallis nec placerat sed, vulputate et nisi. Duis vel dui in nunc venenatis luctus." +
            "\n\n"+"Nam venenatis dui eget erat sollicitudin, quis sagittis odio laoreet. Suspendisse ultrices dui quis lacus feugiat laoreet. Etiam rutrum cursus nibh quis porta. Donec augue mauris, pretium sed mauris vitae, facilisis blandit ipsum. Sed accumsan accumsan facilisis. Nam sed semper sapien, eu volutpat lorem. Cras venenatis ipsum velit, et hendrerit dui efficitur nec. Sed finibus vestibulum suscipit. Suspendisse mattis aliquet urna, sit amet euismod purus pulvinar vel.\n" +
            "\n";
    public static final String MESSAGE_3_HTML = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at turpis lectus. Phasellus magna nulla, fringilla et dui id, gravida elementum ligula. Aliquam dignissim tincidunt efficitur. Sed tincidunt sapien ut vehicula vehicula. Quisque faucibus vestibulum sem at facilisis. Nullam eros urna, consequat id nibh sed, suscipit eleifend metus. Ut congue dolor id nulla ultricies aliquam. Ut a lacinia orci. Donec congue sapien risus, semper consequat tortor pulvinar at. Nullam scelerisque, nibh et pretium cursus, lacus dolor bibendum eros, non volutpat magna elit quis dolor. Mauris vel tortor nulla. Fusce dapibus semper fermentum. Curabitur ac sagittis risus. In hac habitasse platea dictumst. Vestibulum iaculis fermentum orci, ultricies sodales dolor eleifend sed. Nulla accumsan, erat euismod sagittis eleifend, ex purus rutrum mi, vel accumsan quam sapien id erat.\n" +
            "</p><p>Suspendisse congue, augue at tincidunt bibendum, ex magna sodales massa, nec congue metus ipsum id ante. Morbi eu lectus lectus. Nulla sodales sed eros et lacinia. Sed ac leo at urna facilisis posuere. Donec ultrices dolor neque, nec lobortis nisl tristique quis. Nullam porttitor lacus et diam tempus, quis aliquam massa auctor. Praesent finibus suscipit nunc, nec sollicitudin risus suscipit id. Etiam et justo et ex fringilla laoreet. Fusce non nunc imperdiet, pretium dolor id, gravida diam. Sed eget aliquam mauris, ut feugiat dui. Sed eget mi maximus, cursus neque in, bibendum velit. Vestibulum et euismod ligula. Integer nec convallis neque, sit amet laoreet risus. Phasellus bibendum risus vitae quam posuere, sed dignissim eros consequat.</p>";
    public static final String MESSAGE_4_NULL = null;
    private VerticalLayout root;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Example and test application");
        setContent(root = new VerticalLayout());
        Label label = new Label("Hello Vaadin user");
        addComponent(label);
        addBasicExample();
        addLongExample();
        addHtmlExample();
        addNullMessageExample();
        addThreeWayExample();
        addButtonSwapExample();
    }

    private void addComponent(Component c) {
        root.addComponent(c);
    }

    private void addBasicExample() {
        Button button = new Button("Click " + OPEN_BUTTON_1_SHORT);
        button.setId(OPEN_BUTTON_1_SHORT);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), MESSAGE_1_SHORT,
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

    private void addLongExample() {
        Button button = new Button("Click " + OPEN_BUTTON_2_LONG);
        button.setId(OPEN_BUTTON_2_LONG);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), MESSAGE_2_LONG,
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

    private void addHtmlExample() {
        Button button = new Button("Click " + OPEN_BUTTON_3_HTML);
        button.setId(OPEN_BUTTON_3_HTML);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog d = ConfirmDialog.show(getUI(), MESSAGE_3_HTML,
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
                d.setContentMode(ConfirmDialog.ContentMode.HTML);
            }
        });
        addComponent(button);
    }

    private void addNullMessageExample() {
        Button button = new Button("Click " + OPEN_BUTTON_4_NULL);
        button.setId(OPEN_BUTTON_4_NULL);
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
        Button button = new Button("Click " + OPEN_BUTTON_5_3WAY);
        button.setId(OPEN_BUTTON_5_3WAY);
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // The quickest way to confirm
                ConfirmDialog.show(getUI(), "Title", MESSAGE_1_SHORT, "ok", "cancel",
                        "not ok", new ConfirmDialog.Listener() {

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
        Button button = new Button("Click " + OPEN_BUTTON_6_SWAP);
        button.setId(OPEN_BUTTON_6_SWAP);
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
                ConfirmDialog.show(getUI(), "Title", MESSAGE_1_SHORT, "ok", "cancel",
                        "not ok", new ConfirmDialog.Listener() {

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
