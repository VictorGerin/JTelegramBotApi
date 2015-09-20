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
public class ForceReply extends TelegramBaseClass {

    @isBoolean
    private final int force_reply = 1;
    @isBoolean
    private int selective = -1;

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
