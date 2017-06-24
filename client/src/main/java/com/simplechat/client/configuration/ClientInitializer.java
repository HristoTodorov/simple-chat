package com.simplechat.client.configuration;

import com.simplechat.client.gui.ClientGUI;

import javax.swing.*;

/**
 * Created by lampt on 6/24/2017.
 */
public class ClientInitializer {
    public static void main(String[] args)
            throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {
        // Use system settings for UI options
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ClientGUI application = new ClientGUI();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForMessage();
    }
}
