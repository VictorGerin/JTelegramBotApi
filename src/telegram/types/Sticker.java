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
public class Sticker extends TelegramBaseClass {

    private String file_id = null;
    private int width = 0;
    private int height = 0;
    private PhotoSize thumb = null;
    private int file_size = 0;

    /**
     * Get the value of file_id
     *
     * @return the value of file_id
     */
    public String getFile_id() {
        return file_id;
    }

    /**
     * Get the value of width
     *
     * @return the value of width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the value of height
     *
     * @return the value of height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the value of thumb
     *
     * @return the value of thumb
     */
    public PhotoSize getThumb() {
        return thumb;
    }

    /**
     * Get the value of file_size
     *
     * @return the value of file_size
     */
    public int getFile_size() {
        return file_size;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public void removeFile_id() {
        setFile_id(null);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void removeWidth() {
        setWidth(0);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void removeHeight() {
        setHeight(0);
    }

    public void setThumb(PhotoSize thumb) {
        this.thumb = thumb;
    }

    public void removeThumb() {
        setThumb(null);
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public void removeFile_size() {
        setFile_size(0);
    }

}
