package org.vaadin.dialogs.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;
import org.vaadin.sami.demoapp.AbstractDemoApp;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

public class ConfirmDialogApplication extends AbstractDemoApp {

    private static final long serialVersionUID = 3621197493208576958L;
    private static final String MESSAGE_1 = "Are you really sure?";
    private Select r;

    @Override
    public void init() {

        init("Confirmation Dialog Sample Application");
        title("Welcome to ConfirmDialog Demo");

        googleAnalytics("UA-10273223-1");

        description(
                "Note: ConfirmDialog automatically calculates the right size for the dialog if using text only. Test with different message lengths.");

        r = new Select("Message length:");
        root.addComponent(r);
        for (int i = 0; i < 100; i++) {
            r.addItem(i);
            r.setItemCaption(i, (MESSAGE_1.length()*i)+" chars");
        }
        r.setValue(1);
        r.setNullSelectionAllowed(false);

        GridLayout gl = new GridLayout(2, 6);
        gl.setSpacing(true);
        root.addComponent(gl);

        gl.addComponent(new Button(
                "Default everything (select message length above)",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        confirm1();
                    }
                }));
        gl.addComponent(linkToCodeSample(null,"1"));

        gl.addComponent(new Button(
                "Confirmation with custom captions for everything",
                new Button.ClickListener() {

                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        confirm2();
                    }
                }));
        gl.addComponent(linkToCodeSample(null,"2"));

        gl.addComponent(new Button(
                "HTML is escaped, but newlines are preserved in input texts",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        confirm3();
                    }
                }));
        gl.addComponent(linkToCodeSample(null,"3"));

        gl.addComponent(new Button(
                "Present a lengthy License Agreement from Apache",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        confirm5();
                    }
                }));
        gl.addComponent(linkToCodeSample(null,"5"));

        gl.addComponent(new Button(
                "Custom color, custom cancel button, HTML content",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        confirm6();
                    }
                }));
        gl.addComponent(linkToCodeSample(null,"6"));

        gl
                .addComponent(new Button(
                        "Custom ConfirmDialog.Factory (light, resizeable, custom captions)",
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            public void buttonClick(ClickEvent event) {
                                confirm4();
                            }
                        }));
        gl.addComponent(linkToCodeSample(null,"4"));

    }

    public void confirm1() {

        ConfirmDialog.setFactory(new DefaultConfirmDialogFactory());
        // # 1
        // The quickest way to confirm
        ConfirmDialog.show(getMainWindow(), repeat(MESSAGE_1),
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
        // #
    }

    private String repeat(String string) {
        return repeat(string, (Integer) r.getValue());
    }

    public void confirm2() {
        ConfirmDialog.setFactory(new DefaultConfirmDialogFactory());
        // # 2
        // Using custom captions.
        ConfirmDialog.show(getMainWindow(), "Please Confirm:", "Are you really sure?",
                "I am", "Not quite", new ConfirmDialog.Listener() {

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
        // #
    }

    public void confirm3() {
        ConfirmDialog.setFactory(new DefaultConfirmDialogFactory());
        // # 3
        // Support text with newlines.
        ConfirmDialog
                .show(
                        getMainWindow(),
                        "Please confirm:",
                        "Are you really sure?\n\n<b>This has some nasty side effects</b>, you know.",
                        "I am sure", "No, wait!", new ConfirmDialog.Listener() {

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
        // #
    }

    public void confirm4() {

        // # 4
        // Customization via the factory. This makes all subsequent dialog
        // calls to use this application-wide.
        ConfirmDialog.Factory df = new DefaultConfirmDialogFactory() {
            String MY_CAPTION = "Sanos ny";
            String MY_MESSAGE = "Et sunka sä nii meinannu?";
            String MY_OK_CAPTION = "Kyl maar";
            String MY_CANCEL_CAPTION = "Juuei";

            // We change the default language to "turku" and also
            // allow dialog to resize.
            @Override
            public ConfirmDialog create(String caption, String message,
                    String okCaption, String cancelCaption) {
                ConfirmDialog d;
                d = super.create(caption == null ? MY_CAPTION : caption,
                        message == null ? MY_MESSAGE : message,
                        okCaption == null ? MY_OK_CAPTION : okCaption,
                        cancelCaption == null ? MY_CANCEL_CAPTION
                                : cancelCaption);
                d.setResizable(true);
                d.setStyleName(Reindeer.WINDOW_LIGHT);
                return d;
            }

        };
        ConfirmDialog.setFactory(df);

        // Now we can confirm the standard way
        ConfirmDialog.show(getMainWindow(), new ConfirmDialog.Listener() {

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
        // #

    }

    public void confirm5() {

        ConfirmDialog.setFactory(new DefaultConfirmDialogFactory());

        // # 5
        // License confirmation.
        ConfirmDialog.show(getMainWindow(), "Please Accept the Licence",
                resourceToString("Apache-License-2.0.txt"), "Accept", "Cancel",
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
        // #
    }

    public void confirm6() {
        ConfirmDialog.setFactory(new DefaultConfirmDialogFactory());

        // # 6
        // Customization: a black, HTML confirmation dialog.
        ConfirmDialog d = ConfirmDialog
                .show(
                        getMainWindow(),
                        "Please agree with me",
                        "Actually you <b>have to</b> agree with me.<ul><li>One</li><li>Two</li></ul>",
                        "OK", "No, I dont agree", new ConfirmDialog.Listener() {

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
        d.setStyleName(Reindeer.WINDOW_BLACK);
        d.setContentMode(ConfirmDialog.CONTENT_HTML);
        d.setHeight("16em");
        d.getCancelButton().setStyleName(Reindeer.BUTTON_LINK);
        // #
    }

    protected void feedback(boolean confirmed) {
        getMainWindow().showNotification("ConfirmDialog onClose: " + confirmed);

    }

    private String resourceToString(String name) {
        InputStream in = this.getClass().getResourceAsStream(name);
        Reader r = new InputStreamReader(in);
        int i = 0;
        char[] cbuf = new char[1024];
        StringBuffer res = new StringBuffer();
        try {
            while ((i = r.read(cbuf)) > 0) {
                res.append(cbuf, 0, i);
            }

            return res.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String repeat(String string, int count) {
        String res = "";
        for (int i = 0; i < count; i++) {
            res += (i % 2 != 0 ? " and" : "") + " " + string;
        }
        return res + " (" + (string.length() * count) + "chars)";
    }

}
