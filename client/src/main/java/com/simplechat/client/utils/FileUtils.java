package com.simplechat.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtils {
	
	public static String encodeFile(File file) {
		try {			
			// Reading a file from file system
			FileInputStream dataInFile = new FileInputStream(file);
			byte fileData[] = new byte[(int) file.length()];
			dataInFile.read(fileData);

			// Converting File byte array into Base64 String
			String fileDataString = encodeFile(fileData);
			System.out.println(fileDataString);

			dataInFile.close();

			System.out.println("File Successfully Manipulated!");
			return fileDataString;
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the File " + ioe);
		}
		return null;
	}
	
	public static String encodeFile(byte[] byteArray) {
		return Base64.getEncoder().encodeToString(byteArray);
	}

	public static File decodeFile(String dataString, String filename) {
		try {			
			// Converting a Base64 String into File byte array
			byte[] fileByteArray = decodeFile(dataString);

			// Write a file byte array into file system
			File file = new File(filename);
			FileOutputStream dataOutFile = new FileOutputStream(file);
			dataOutFile.write(fileByteArray);

			dataOutFile.close();

			System.out.println("File Successfully Manipulated!");
			return file;
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the File " + ioe);
		}
		return null;
	}
	
	public static byte[] decodeFile(String dataString) {
		return Base64.getDecoder().decode(dataString);
	}
	
	public static String getDownloadedFileAbsoluteName(String filename) {
		return String.format("%s%sdata%s%s", System.getProperty("user.dir"), File.separator, File.separator, filename);
	}
	
//	public static void main(String[] args) {
//		System.out.println(getDownloadedFileAbsoluteName("textfile.txt"));
//	}
}
