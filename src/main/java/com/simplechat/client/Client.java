package com.simplechat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {

	private static Client application;
	private JTextField enterField;
	private JTextArea displayArea;
	private Socket clientSocket;
	
	private boolean toClose = false;

	public Client() {
		super("Client");

		enterField = new JTextField("Type message here");
		enterField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try
				{
					String message = event.getActionCommand();
					
//					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//					outToServer.writeBytes(message + '\n');
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
					out.println(message);
					out.flush();

					displayArea
					.setCaretPosition(displayArea.getText().length());
					
					if (message.equals("quit") || message.equals("end")) {
						toClose = true;
					}
				} catch (IOException ioException) {
					displayMessage(ioException.toString() + "\n");
					ioException.printStackTrace();
				}
			}
		});

		add(enterField, BorderLayout.NORTH);

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
					displayMessage(message);
					message = inFromServer.readLine();
					System.out.println(message + (message == null));
				} while (message != null);
			} catch (IOException exception) {
				displayMessage(exception.toString() + "\n");
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
				displayArea.append("\n");
			}
		});
	}

	public static void main(String args[]) {
		application = new Client();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.waitForMessage();
	}
}
