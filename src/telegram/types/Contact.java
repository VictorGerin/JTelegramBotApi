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
public class Contact extends TelegramBaseClass {

    private String phone_number = null;
    private String first_name = null;
    private String last_name = null;
    private int user_id = 0;

    public String getPhone_number() {
        return phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void removePhone_number() {
        setPhone_number(null);
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void removeFirst_name() {
        setFirst_name(null);
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void removeLast_name() {
        setLast_name(null);
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void removeUser_id() {
        setUser_id(0);
    }

}
