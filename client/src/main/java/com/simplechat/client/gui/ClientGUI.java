package com.simplechat.client.gui;

import com.simplechat.client.utils.MessageUtils;
import com.simplechat.shared.CommonUtils;
import com.simplechat.shared.messages.FileCommand;
import com.simplechat.shared.messages.SimpleMessageFile;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Base64;

public class ClientGUI extends JFrame {

    private JTextField enterField;
    private JButton sendFileBtn;
    private JButton sendMessageBtn;
    private JPanel panel;
    private JEditorPane editorPane;
    private Socket clientSocket;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private boolean toClose = false;

    public ClientGUI() {
        super("Simple Chat - Client");

        enterField = new JTextField("");
        sendMessageBtn = new JButton("Send message");
        sendMessageBtn.addActionListener((event) -> {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                new SendMessageGUI(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        enterField.addActionListener((event) -> {
            try {
                String message = enterField.getText();
                enterField.setText("");
                displayMessage(message);
                if (message != null && !message.trim().equals("")) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                    if (message.startsWith(com.simplechat.shared.messages.Commands.SEND_FILE_TO)) {
                        String[] parts = MessageUtils.getMessageParts(message,
                                com.simplechat.shared.messages.Commands.SEND_FILE_TO);
                        String absoluteFilePath = parts[2];
                        File file = new File(absoluteFilePath);
                        byte[] encodedFile = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
                        String fileName = file.getName();
                        String formattedMessage = MessageUtils.getMessage(parts[0], parts[1], fileName,
                                new String(encodedFile));
                        out.println(formattedMessage);
                        out.flush();
                    } else {
                        out.println(message);
                        out.flush();
                    }
                    if (com.simplechat.shared.messages.Commands.BYE.equals(message)) {
                        toClose = true;
                    }
                }
            } catch (IOException ioException) {
                displayMessage(ioException.toString() + System.getProperty("line.separator"));
                ioException.printStackTrace();
            }
        });

        sendFileBtn = new JButton("Load a File");
        sendFileBtn.addActionListener((event) -> {
            final JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(sendFileBtn);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                appendFileName(file);
                System.out.println("Selected file: " + file.getName());
            } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.out.println("Cancelled");
            } else if (returnVal == JFileChooser.ERROR_OPTION) {
                System.out.println("Error!");
            } else {
                System.out.println("unknown...");
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(enterField);
        panel.add(Box.createHorizontalGlue());
        panel.add(sendFileBtn);
        panel.add(sendMessageBtn);
        add(panel, BorderLayout.NORTH);

        editorPane = new JEditorPane();
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        setSize(700, 500);
        setVisible(true);

        try {
            clientSocket = new Socket("localhost", 5555);
        } catch (IOException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    public void waitForMessage() {
        String filename = null;
        while (true) {
            try {
                if (toClose) {
                    clientSocket.close();
                    break;
                }
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                String message = inFromServer.readLine();
                do {
                    if (message != null) {
                        if (message.startsWith(com.simplechat.shared.messages.Commands.RECIEVED_FILE_FROM)) {
                            SimpleMessageFile messageFile = FileCommand.getFileFromCommand(message);
                            File destinationFile = Paths.get(
                                    CommonUtils.USER_HOME_DIR, messageFile.getFileName()).toFile();
                            FileUtils.touch(destinationFile);
                            Files.write(destinationFile.toPath(),
                                    Base64.getDecoder().decode(messageFile.getFileContent()),
                                    StandardOpenOption.TRUNCATE_EXISTING);
                            displayMessage(MessageFormat.format("FILE [{0}] received from [{1}].",
                                    messageFile.getFileName(), messageFile.getFrom()));
                        }
                        displayMessage(message);
                    }
                    message = inFromServer.readLine();
                } while (message != null);
            } catch (IOException exception) {
                displayMessage(exception.toString() + CommonUtils.LINE_SEPARATOR);
                exception.printStackTrace();
            }
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void displayMessage(final String messageToDisplay) {
        System.out.println(messageToDisplay);
        SwingUtilities.invokeLater(() -> {
            // TODO add text formatting
            StringBuilder sb = new StringBuilder(editorPane.getText());
            sb.append(MessageFormat.format("[{0}] : {1}", LocalDateTime.now(), messageToDisplay))
                    .append(LINE_SEPARATOR);
            editorPane.setText(sb.toString());
        });
    }

    private void appendFileName(File file) {
        String text = enterField.getText();
        enterField.setText(text.concat(file.getAbsolutePath()));
    }

}
