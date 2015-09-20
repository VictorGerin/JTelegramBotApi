/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.exception;

import telegram.BotApi;
import telegram.Param;

/**
 *
 * @author victor
 */
public class TelegramFileLimit extends TelegramBaseException {

    public static final long maxSendSize = 50 * 1024 * 1024;

    public TelegramFileLimit() {
    }

    public TelegramFileLimit(String msg) {
        super(msg);
    }

    public TelegramFileLimit(String message, Throwable cause) {
        super(message, cause);
    }

    public static void check(String adicionalFile, byte[] fileToUpload, String fileType, Param... params) {
        long total = calcTotalSizeToSent(adicionalFile, fileToUpload, fileType, params);
        if (total > maxSendSize) {
            long muiltpart_form_size = total - fileToUpload.length;
            throw new TelegramBaseException("The max data that can be sent to server is ("
                    + maxSendSize
                    + ") that include all muiltpart-form in this case takes (" + muiltpart_form_size
                    + ") so the file size can be only (" + (50 * 1024 * 1024 - muiltpart_form_size) + ")");
        }
    }

    public static long calcTotalSizeToSent(String adicionalFile, byte[] fileToUpload, String fileType, Param... params) {
        int two_hyphens_size = BotApi.MULTIPART_TWO_HYPHENS.length;
        int boundary_size = BotApi.MULTIPART_BOUNDARY.length;
        int new_line_size = BotApi.MULTIPART_CARRIAGE_RETURN_AND_NEWLINE.length;
        int Content_Disposition_size = BotApi.Content_Disposition.replace("<name>", "").length();

        long totalSent = 2 * two_hyphens_size + boundary_size + new_line_size;

        totalSent += two_hyphens_size * params.length;
        totalSent += boundary_size * params.length;
        totalSent += new_line_size * params.length * 4;
        totalSent += Content_Disposition_size * params.length;
        for (Param param : params) {
            totalSent += param.getName().getBytes().length + param.getValue().getBytes().length;
        }
        if (fileToUpload != null) {
            int Content_Disposition_file_size = BotApi.Content_Disposition_file
                    .replaceAll("<fieldName>", adicionalFile)
                    .replaceAll("<name>", "1234567890123456789012345").getBytes().length;
            totalSent += two_hyphens_size + boundary_size + (4 * new_line_size) + Content_Disposition_file_size + fileType.length() + fileToUpload.length;
        }
        return totalSent;
    }

}
