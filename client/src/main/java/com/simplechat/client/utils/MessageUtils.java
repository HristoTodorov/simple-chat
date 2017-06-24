package com.simplechat.client.utils;

public class MessageUtils {
	
	/**
	 *	[<command>, <username>, <message|filename>] 
	 */
	public static String[] getMessageParts(String message, String command) {
		String[] parts = new String[3];
		parts[0] = command;
		message = message.substring(command.length() + 1);
		
		int indexOfSpace = message.indexOf(" ");
		String username = message.substring(0, indexOfSpace);
		parts[1] = username;
		String lastPart = message.substring(indexOfSpace + 1);
		parts[2] = lastPart;
		
		return parts;
	}
	
	public static String getMessage(String command, String username, String value) {
		return String.format("%s %s %s", command, username, value);
	}
	
//	public static void main(String[] args) {
//		String msg = "500 send_to user5 it is a great job!";
//		String[] messageParts = getMessageParts(msg, "500 send_to");
//		System.out.println(Arrays.toString(messageParts));
//		
//		System.out.println(getMessage("send_to", "user2", "C:\\file.txt"));
//	}
}
