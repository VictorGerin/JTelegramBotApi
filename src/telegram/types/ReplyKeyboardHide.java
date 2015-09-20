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
public class ReplyKeyboardHide extends TelegramBaseClass {

    @isBoolean
    private int hide_keyboard = 1;
    @isBoolean
    private int selective = -1;

    /**
     * Get the value of hide_keyboard
     *
     * @return the value of hide_keyboard
     */
    public boolean isHide_keyboard() {
        return hide_keyboard != 0 && hide_keyboard != -1;
    }

    /**
     * Get the value of selective
     *
     * @return the value of selective
     */
    public boolean isSelective() {
        return selective != 0 && selective != -1;
    }

}
