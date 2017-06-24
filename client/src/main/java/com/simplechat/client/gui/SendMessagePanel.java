package com.simplechat.client.gui;

import com.simplechat.shared.messages.Commands;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

/**
 * Created by lampt on 6/24/2017.
 */
public class SendMessagePanel extends JPanel {
    private JTextArea messageArea;
    private JButton sendBtn;
    private JCheckBox unicastCheckBox;
    private JTextField userNameField;
    private PrintWriter printer;

    public SendMessagePanel(PrintWriter printer) {
        super();
        this.printer = printer;
        setBorder(BorderFactory.createTitledBorder(""));
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints gridConstraints = new GridBagConstraints();
        setLayout(gridLayout);

        messageArea = new JTextArea(2, 10);
        JScrollPane scpArea0 = new JScrollPane(messageArea);
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        gridConstraints.gridwidth = 16;
        gridConstraints.gridheight = 14;
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 1;
        gridConstraints.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(scpArea0, gridConstraints);
        add(scpArea0);

        sendBtn = new JButton("Send");
        gridConstraints.gridx = 5;
        gridConstraints.gridy = 18;
        gridConstraints.gridwidth = 4;
        gridConstraints.gridheight = 1;
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 0;
        gridConstraints.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(sendBtn, gridConstraints);
        add(sendBtn);
        sendBtn.addActionListener((listener) -> {
            // Action for sendBtn
            String message = messageArea.getText();
            if (StringUtils.isBlank(message)) {
                JOptionPane.showMessageDialog(null,
                        "Message can not be null nor empty!");
            }
            if (unicastCheckBox.isSelected()) {
                // unicast message
                String recipientName = userNameField.getText();

                if (StringUtils.isNotBlank(recipientName)) {
                    printer.println(String.format("%s %s %s", Commands.SEND_TO,
                            recipientName, message));
                    printer.flush();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please, specify the user for sending message.");
                }
            } else {
                // broadcast message
                printer.println(Commands.SEND_ALL + " " + message );
                printer.flush();
            }
        });
        unicastCheckBox = new JCheckBox("Private");
        unicastCheckBox.addActionListener((listener) -> {
            userNameField.setEditable(unicastCheckBox.isSelected());
        });
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 2;
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 0;
        gridConstraints.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(unicastCheckBox, gridConstraints);
        add(unicastCheckBox);

        userNameField = new JTextField();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 2;
        gridConstraints.gridwidth = 13;
        gridConstraints.gridheight = 1;
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 0;
        gridConstraints.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(userNameField, gridConstraints);
        add(userNameField);
        userNameField.setEditable(unicastCheckBox.isSelected());
    }

}
