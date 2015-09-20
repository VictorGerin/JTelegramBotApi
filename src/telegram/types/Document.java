/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.types;

/**
 *
 * @author victor
 */
public class Document extends TelegramBaseClass {

    private String file_id = null;
    private PhotoSize thumb = null;
    private String file_name = null;
    private String mime_type = null;
    private int file_size = 0;

    public String getFile_id() {
        return file_id;
    }

    public PhotoSize getThumb() {
        return thumb;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getMime_type() {
        return mime_type;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public void removeFile_id() {
        setFile_id(null);
    }

    public void setThumb(PhotoSize thumb) {
        this.thumb = thumb;
    }

    public void removeThumb() {
        setThumb(null);
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void removeFile_name() {
        setFile_name(null);
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public void removeMime_type() {
        setMime_type(null);
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public void removeFile_size() {
        setFile_size(0);
    }
}
