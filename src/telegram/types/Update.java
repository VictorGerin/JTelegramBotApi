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
public class Update extends TelegramBaseClass {

    private int update_id = 0;
    private Message message = null;

    /**
     * Get the value of update_id
     *
     * @return the value of update_id
     */
    public int getUpdate_id() {
        return update_id;
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public Message getMessage() {
        return message;
    }

    public void setUpdate_id(int update_id) {
        this.update_id = update_id;
    }
    
    public void removeUpdate_id() {
        setUpdate_id(0);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    public void removeMessage() {
        setMessage(null);
    }

}
