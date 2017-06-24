package com.simplechat.client.utils;

import com.simplechat.shared.messages.FileCommand;

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

	public static String getMessage(String command, String username, String fileName, String encodedFile) {
		return String.format("%s %s %s %s%s%s",
				command,
				username,
				fileName,
				FileCommand.FILE_CONTENT_HEADER,
				encodedFile,
				FileCommand.FILE_CONTENT_FOOTER);
	}

}
