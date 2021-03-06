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
public class Location extends TelegramBaseClass {

    private double longitude = 0.0f;
    private double latitude = 0.0f;

    /**
     * Get the value of longitude
     *
     * @return the value of longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the value of latitude
     *
     * @return the value of latitude
     */
    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void removeLongitude() {
        setLongitude(0.0f);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void removeLatitude() {
        setLatitude(0.0f);
    }

}
