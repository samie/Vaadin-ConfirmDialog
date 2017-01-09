package org.vaadin.dialogs;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.addonhelpers.TServer;

/**
 * Embedded Jetty test servlet container for testing a single UI class.
 *
 *
 * @author Sami Ekblad
 *
 */
public class UITestServer extends TServer {

    public static int PORT = 8080;
    public static String URL = "http://localhost/";
    
    public static void main(String[] args) {
        try {
            new TServer().startServer(PORT);
        } catch (Exception ex) {
            Logger.getLogger(UITestServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
