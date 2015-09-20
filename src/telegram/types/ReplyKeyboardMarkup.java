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
public class ReplyKeyboardMarkup extends TelegramBaseClass {

    private List<List<String>> keyboard = null;
    
    @isBoolean
    private int resize_keyboard = -1;
    @isBoolean
    private int one_time_keyboard = -1;
    @isBoolean
    private int selective = -1;

    /**
     * Get the value of keyboard
     *
     * @return the value of keyboard
     */
    public List<List<String>> getKeyboard() {
        return keyboard;
    }
    
    public void setKeyboard(String[][] keyboard) {
        setKeyboard(getList(keyboard));
    }

    public void setKeyboard(List<List<String>> keyboard) {
        this.keyboard = keyboard;
    }

    public void removeKeyboard() {
        this.keyboard = null;
    }

    /**
     * Get the value of resize_keyboard
     *
     * @return the value of resize_keyboard
     */
    public boolean isResize_keyboard() {
        return resize_keyboard != 0 && resize_keyboard != -1;
    }

    public void setResize_keyboard(boolean resize_keyboard) {
        this.resize_keyboard = resize_keyboard ? 1 : 0;
    }

    public void removeResize_keyboard() {
        this.resize_keyboard = -1;
    }

    /**
     * Get the value of one_time_keyboard
     *
     * @return the value of one_time_keyboard
     */
    public boolean isOne_time_keyboard() {
        return one_time_keyboard != 0 && one_time_keyboard != -1;
    }

    public void setOne_time_keyboard(boolean one_time_keyboard) {
        this.one_time_keyboard = one_time_keyboard ? 1 : 0;
    }

    public void removeOne_time_keyboard() {
        this.one_time_keyboard = -1;
    }

    /**
     * Get the value of selective
     *
     * @return the value of selective
     */
    public boolean isSelective() {
        return selective != 0 && selective != -1;
    }

    public void setSelective(boolean selective) {
        this.selective = selective ? 1 : 0;
    }

    public void removeSelective() {
        this.selective = -1;
    }

}
