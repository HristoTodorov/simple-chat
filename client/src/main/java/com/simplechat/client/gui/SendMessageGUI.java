package com.simplechat.client.gui;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.io.PrintWriter;


public class SendMessageGUI extends JFrame {

    private SendMessagePanel sendMessagePanel;

    public SendMessageGUI(PrintWriter printer) {
        super("Simple chat - Client - Send message");
        sendMessagePanel = new SendMessagePanel(printer);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(sendMessagePanel);
        setContentPane(scrollPane);
        pack();
        setVisible(true);
    }
}
