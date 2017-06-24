package com.simplechat.shared.messages;

/**
 * Created by lampt on 6/24/2017.
 */
public class SimpleMessageFile {
    private String fileName;

    private String from;

    private String fileContent;

    public SimpleMessageFile(String fileName, String from, String fileContent) {
        this.fileName = fileName;
        this.from = from;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFrom() {
        return from;
    }

    public String getFileContent() {
        return fileContent;
    }
}
