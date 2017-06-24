package com.simplechat.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.simplechat.client.utils.Commands;
import com.simplechat.client.utils.FileUtils;
import com.simplechat.client.utils.MessageUtils;

public class GUIClient extends JFrame {
	
	private static GUIClient application;
	private JTextField enterField;
	private JButton button;
	private JPanel panel;
	private JTextArea displayArea;
	private Socket clientSocket;
	
	private boolean toClose = false;

	public GUIClient() {
		super("Client");

		enterField = new JTextField("");
		enterField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try
				{
					String message = event.getActionCommand();
					enterField.setText("");
					
					if (message != null && !message.trim().equals("")) {
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
						
						if (message.startsWith(Commands.SEND_FILE_TO)) {
							String[] parts = MessageUtils.getMessageParts(message, Commands.SEND_FILE_TO);
							String filename = parts[2];
							File file = new File(filename);
							
							String formattedMessage = MessageUtils.getMessage(parts[0], parts[1], file.getName());
							out.println(formattedMessage);
							out.flush();
							
							String encodedFile = FileUtils.encodeFile(file);
							out.println(Commands.FILE_DATA + encodedFile);
							out.flush();
						} else {
							
						}

						displayArea
						.setCaretPosition(displayArea.getText().length());
						
						if (Commands.BYE.equals(message)) {
							toClose = true;
						}
					}
				} catch (IOException ioException) {
					displayMessage(ioException.toString() + System.getProperty("line.separator"));
					ioException.printStackTrace();
				}
			}
		});

		button = new JButton("Load a File");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
	            int returnVal = fileChooser.showOpenDialog(button);
	            if(returnVal == JFileChooser.APPROVE_OPTION){
	                File file = fileChooser.getSelectedFile();
	                appendFileName(file);
	                System.out.println("Selected file: " + file.getName());
	            }else if(returnVal == JFileChooser.CANCEL_OPTION){
	                System.out.println("Cancelled");
	            }else if(returnVal == JFileChooser.ERROR_OPTION){
	                System.out.println("Error!");
	            }else{
	                System.out.println("unknown...");
	            }
				
			}
		});
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(enterField);
		panel.add(Box.createHorizontalGlue());
		panel.add(button);		
		add(panel, BorderLayout.NORTH);

		displayArea = new JTextArea();
		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		setSize(400, 300);
		setVisible(true);

		try
		{
			clientSocket = new Socket("localhost", 5555);
		} catch (IOException socketException) {
			socketException.printStackTrace();
			System.exit(1);
		}
	}
	
	public void waitForMessage() {
		String filename = null;
		while (true) {
			try
			{
				if (toClose) {
					clientSocket.close();
					break;
				}
				
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				String message = inFromServer.readLine();
				do {
					if (message != null) {
						if (message.startsWith(Commands.FILE_FROM)) {
							String[] parts = MessageUtils.getMessageParts(message, Commands.FILE_FROM);
							filename = parts[2];
						}
						if (message.startsWith(Commands.FILE_DATA)) {
							filename = FileUtils.getDownloadedFileAbsoluteName(filename);
							String encodedFile = message.substring(Commands.FILE_DATA.length());
							File decodeFile = FileUtils.decodeFile(encodedFile, filename);
							
							PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
							String fileTransferStatusMessage = Commands.FILE_ACCEPTED_SUCESSFULLY;
							if (decodeFile == null) {
								fileTransferStatusMessage = Commands.CLIENT_TRANSFER_ERROR;
							}
							out.println(fileTransferStatusMessage);
							out.flush();
							
							message = Commands.FILE_DATA + filename + Commands.FILE_DATA;
							filename = null;
						}
						displayMessage(message);
					}
					message = inFromServer.readLine();
				} while (message != null);
			} catch (IOException exception) {
				displayMessage(exception.toString() + System.getProperty("line.separator"));
				exception.printStackTrace();
			}
		}
		application.dispatchEvent(new WindowEvent(application, WindowEvent.WINDOW_CLOSING));
	}

	private void displayMessage(final String messageToDisplay) {
		System.out.println(messageToDisplay);
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				displayArea.append(messageToDisplay);
				displayArea.append(System.getProperty("line.separator"));
			}
		});
	}
	
	private void appendFileName(File file) {
		String text = enterField.getText();
		enterField.setText(text.concat(file.getAbsolutePath()));
	}

	public static void main(String args[]) {
		application = new GUIClient();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.waitForMessage();
	}
}
