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
public class Audio extends TelegramBaseClass {

    private String file_id = null;
    private String performer = null;
    private String title = null;
    private String mime_type = null;
    private int file_size = 0;
    private int duration = 0;

    public String getFile_id() {
        return file_id;
    }

    public int getDuration() {
        return duration;
    }

    public String getPerformer() {
        return performer;
    }

    public String getTitle() {
        return title;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void removeDuration() {
        setDuration(0);
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public void removePerformer() {
        setPerformer(null);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void removeTitle() {
        setTitle(null);
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
