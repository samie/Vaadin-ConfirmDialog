package org.vaadin.dialogs.test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import java.util.List;

@Route("")
public class ConfirmDialogTestUI extends VerticalLayout {

    public static final String OPEN_BUTTON_1_SHORT = "confirm_1";
    public static final String OPEN_BUTTON_2_LONG = "confirm_2";
    public static final String OPEN_BUTTON_3_HTML = "confirm_3";
    public static final String OPEN_BUTTON_4_NULL = "confirm_4";
    public static final String OPEN_BUTTON_5_3WAY = "confirm_5";
    public static final String OPEN_BUTTON_6_SWAP = "confirm_6";
    public static final String OPEN_BUTTON_7_CUSTOM = "confirm_7";

    public static final String OPEN_BUTTON_9_LICENSE = "confirm_9";

    public static final String MESSAGE_1_SHORT = "This is the question?";
    public static final String MESSAGE_2_LONG = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at turpis lectus. Phasellus magna nulla, fringilla et dui id, gravida elementum ligula. Aliquam dignissim tincidunt efficitur. Sed tincidunt sapien ut vehicula vehicula. Quisque faucibus vestibulum sem at facilisis. Nullam eros urna, consequat id nibh sed, suscipit eleifend metus. Ut congue dolor id nulla ultricies aliquam. Ut a lacinia orci. Donec congue sapien risus, semper consequat tortor pulvinar at. Nullam scelerisque, nibh et pretium cursus, lacus dolor bibendum eros, non volutpat magna elit quis dolor. Mauris vel tortor nulla. Fusce dapibus semper fermentum. Curabitur ac sagittis risus. In hac habitasse platea dictumst. Vestibulum iaculis fermentum orci, ultricies sodales dolor eleifend sed. Nulla accumsan, erat euismod sagittis eleifend, ex purus rutrum mi, vel accumsan quam sapien id erat." +
            "\n\n" + "Vivamus maximus tristique dapibus. Duis et congue mauris. Sed vestibulum, elit nec vehicula posuere, eros metus lobortis ex, egestas dapibus sem lorem nec ipsum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Pellentesque justo tellus, sodales nec lectus sit amet, euismod ultrices massa. Suspendisse potenti. Fusce lectus ligula, convallis et finibus ut, accumsan sit amet nisl. Donec dapibus molestie lorem. Nulla justo sem, imperdiet vitae rhoncus vel, gravida eu dui. Curabitur turpis ipsum, convallis nec placerat sed, vulputate et nisi. Duis vel dui in nunc venenatis luctus." +
            "\n\n"+"Nam venenatis dui eget erat sollicitudin, quis sagittis odio laoreet. Suspendisse ultrices dui quis lacus feugiat laoreet. Etiam rutrum cursus nibh quis porta. Donec augue mauris, pretium sed mauris vitae, facilisis blandit ipsum. Sed accumsan accumsan facilisis. Nam sed semper sapien, eu volutpat lorem. Cras venenatis ipsum velit, et hendrerit dui efficitur nec. Sed finibus vestibulum suscipit. Suspendisse mattis aliquet urna, sit amet euismod purus pulvinar vel.\n" +
            "\n";
    public static final String MESSAGE_3_HTML = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at turpis lectus. Phasellus magna nulla, fringilla et dui id, gravida elementum ligula. Aliquam dignissim tincidunt efficitur. Sed tincidunt sapien ut vehicula vehicula. Quisque faucibus vestibulum sem at facilisis. Nullam eros urna, consequat id nibh sed, suscipit eleifend metus. Ut congue dolor id nulla ultricies aliquam. Ut a lacinia orci. Donec congue sapien risus, semper consequat tortor pulvinar at. Nullam scelerisque, nibh et pretium cursus, lacus dolor bibendum eros, non volutpat magna elit quis dolor. Mauris vel tortor nulla. Fusce dapibus semper fermentum. Curabitur ac sagittis risus. In hac habitasse platea dictumst. Vestibulum iaculis fermentum orci, ultricies sodales dolor eleifend sed. Nulla accumsan, erat euismod sagittis eleifend, ex purus rutrum mi, vel accumsan quam sapien id erat.\n" +
            "</p><p>Suspendisse congue, augue at tincidunt bibendum, ex magna sodales massa, nec congue metus ipsum id ante. Morbi eu lectus lectus. Nulla sodales sed eros et lacinia. Sed ac leo at urna facilisis posuere. Donec ultrices dolor neque, nec lobortis nisl tristique quis. Nullam porttitor lacus et diam tempus, quis aliquam massa auctor. Praesent finibus suscipit nunc, nec sollicitudin risus suscipit id. Etiam et justo et ex fringilla laoreet. Fusce non nunc imperdiet, pretium dolor id, gravida diam. Sed eget aliquam mauris, ut feugiat dui. Sed eget mi maximus, cursus neque in, bibendum velit. Vestibulum et euismod ligula. Integer nec convallis neque, sit amet laoreet risus. Phasellus bibendum risus vitae quam posuere, sed dignissim eros consequat.</p>";
    public static final String MESSAGE_4_NULL = null;

    public static final String MESSAGE_5_10ROWS = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n";

    public static final String MESSAGE_6_TWOLINES = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at turpis lectus.";
    private VerticalLayout root;

    public ConfirmDialogTestUI() {
        UI.getCurrent().getPage().setTitle("Example and test application");
        add(root = new VerticalLayout());
        Span label = new Span("Hello Vaadin user");
        add(label);
        addBasicExample();
        addLongExample();
        addHtmlExample();
        addNullMessageExample();
        addThreeWayExample();
        addButtonSwapExample();
        addCustomButtonExample();
        addLicenseButtonExample();
    }


    private void addComponent(Component c) {
        root.add(c);
    }

    private void addBasicExample() {
        Button button = new Button("Click " + OPEN_BUTTON_1_SHORT);
        button.setId(OPEN_BUTTON_1_SHORT);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(), MESSAGE_1_SHORT,
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        } else {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        }
                    });
        });
        add(button);
    }

    private void addLongExample() {
        Button button = new Button("Click " + OPEN_BUTTON_2_LONG);
        button.setId(OPEN_BUTTON_2_LONG);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(), MESSAGE_2_LONG,
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        } else {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        }
                    });
        });
        add(button);
    }

    private void addHtmlExample() {
        Button button = new Button("Click " + OPEN_BUTTON_3_HTML);
        button.setId(OPEN_BUTTON_3_HTML);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog d = ConfirmDialog.show(UI.getCurrent(), MESSAGE_3_HTML,
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        } else {
                            Notification.show("Confirmed:"
                                    + dialog.isConfirmed());
                        }
                    });
            d.setContentMode(ConfirmDialog.ContentMode.HTML);
        });
        add(button);
    }

    private void addNullMessageExample() {
        Button button = new Button("Click " + OPEN_BUTTON_4_NULL);
        button.setId(OPEN_BUTTON_4_NULL);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(), MESSAGE_4_NULL, (ConfirmDialog.Listener) dialog -> {
                if (dialog.isConfirmed()) {
                    Notification.show("Confirmed:"
                            + dialog.isConfirmed());
                } else {
                    Notification.show("Confirmed:"
                            + dialog.isConfirmed());
                }
            });
        });
        add(button);
    }

    private void addThreeWayExample() {
        Button button = new Button("Click " + OPEN_BUTTON_5_3WAY);
        button.setId(OPEN_BUTTON_5_3WAY);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(), "Title", MESSAGE_5_10ROWS, "ok", "cancel",
                    "not ok", (ConfirmDialog.Listener) dialog -> Notification.show("Confirmed:"
                            + dialog.isConfirmed()+" Canceled:"+dialog.isCanceled()));
        });
        add(button);
    }

    private void addButtonSwapExample() {
        Button button = new Button("Click " + OPEN_BUTTON_6_SWAP);
        button.setId(OPEN_BUTTON_6_SWAP);
        button.addClickListener((e) -> {
            ConfirmDialog.Factory currentFactory = ConfirmDialog.getFactory();
            ConfirmDialog.setFactory(new DefaultConfirmDialogFactory() {

                @Override
                protected List<Button> orderButtons(Button cancel, Button notOk, Button ok) {
                    return super.orderButtons(cancel, ok, notOk);
                }
            });

            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(), "Title", MESSAGE_6_TWOLINES, "ok", "cancel",
                    "not ok", (ConfirmDialog.Listener) dialog -> Notification.show("Confirmed:"
                            + dialog.isConfirmed()+" Canceled:"+dialog.isCanceled()));

            // Restore the factory
            ConfirmDialog.setFactory(currentFactory);
        });


        add(button);
    }

    private void addCustomButtonExample() {
        Button button = new Button("Click " + OPEN_BUTTON_7_CUSTOM);
        button.setId(OPEN_BUTTON_7_CUSTOM);
        button.addClickListener((e) -> {
                    ConfirmDialog.Factory currentFactory = ConfirmDialog.getFactory();
                    ConfirmDialog.setFactory(new DefaultConfirmDialogFactory() {

                        @Override
                        protected Button buildCancelButton(String cancelCaption) {
                            Button b = super.buildCancelButton(cancelCaption);
                            b.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                            return b;
                        }

                        @Override
                        protected Button buildNotOkButton(String notOkCaption) {
                            Button b = super.buildNotOkButton(notOkCaption);
                            b.addThemeVariants(ButtonVariant.LUMO_ERROR);
                            return b;
                        }
                    });
                    ConfirmDialog.show(UI.getCurrent(), "Deleting file", "Delete 'file.txt'?", "Keep", "Cancel",
                            "Delete", (ConfirmDialog.Listener) dialog -> Notification.show("Delete:"
                                    + (!dialog.isConfirmed() && !dialog.isCanceled())));


            // Restore the factory
            ConfirmDialog.setFactory(currentFactory);

        });
        add(button);
    }

    private void addLicenseButtonExample() {
        Button button = new Button("Click " + OPEN_BUTTON_9_LICENSE);
        button.setId(OPEN_BUTTON_4_NULL);
        button.addClickListener((e) -> {
            // The quickest way to confirm
            ConfirmDialog.show(UI.getCurrent(),
                    "Read and Accept License Agreement",
                    ApacheLicense.get(),
                    "Accept",
                    "Decline",
                    dialog -> {
                if (dialog.isConfirmed()) {
                    Notification.show("Thank you.");
                } else {
                    Notification.show("License not accepted.");
                }
            });
        });
        add(button);
    }

}
