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
public class GroupChat extends TelegramBaseClass {

    private int id = 0;
    private String title = null;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void removeId() {
        setId(0);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void removeTitle() {
        setTitle(null);
    }
}
