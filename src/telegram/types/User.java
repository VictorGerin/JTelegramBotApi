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
public class User extends TelegramBaseClass {

    private int id = 0;
    private String first_name = null;
    private String last_name = null;
    private String username = null;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of first_name
     *
     * @return the value of first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Get the value of last_name
     *
     * @return the value of last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void removeId() {
        setId(0);
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

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void removeUsername() {
        setUsername(null);
    }

}
