package com.simplechat.shared.messages;

import com.simplechat.shared.CommonUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lampt on 6/24/2017.
 */
public class FileCommand {
    public static final String FILE_CONTENT_HEADER = "===FILE_STARTS_HERE===";
    public static final String FILE_CONTENT_FOOTER = "===FILE_ENDS_HERE===";

    public static SimpleMessageFile getFileFromCommand(String message) {
        String[] msgArgs = message.split(" ");
        String from = msgArgs[1];
        String fileName = msgArgs[2];
        String rawMessage = msgArgs[3];
        String fileContentBase64Encoded = StringUtils
                .removeStart(rawMessage, FILE_CONTENT_HEADER);
        fileContentBase64Encoded = StringUtils.removeEnd(fileContentBase64Encoded, FILE_CONTENT_FOOTER);
        return new SimpleMessageFile(fileName, from, fileContentBase64Encoded);
    }

}
