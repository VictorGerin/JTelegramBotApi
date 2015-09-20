/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.types;

import java.util.List;

/**
 *
 * @author victor
 */
public class UserProfilePhotos extends TelegramBaseClass {

    private int total_count = 0;
    private List<List<PhotoSize>> photos = null;

    public int getTotal_count() {
        return total_count;
    }

    public List<List<PhotoSize>> getPhotos() {
        return photos;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void removeTotal_count() {
        setTotal_count(0);
    }

    public void setPhotos(PhotoSize[][] photos) {
        setPhotos(getList(photos));
    }
    public void setPhotos(List<List<PhotoSize>> photos) {
        this.photos = photos;
    }

    public void removePhotos() {
        setPhotos((List<List<PhotoSize>>) null);
    }

}
