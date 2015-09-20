/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.exception;

/**
 *
 * @author victor
 */
public class TelegramErroResponse extends TelegramBaseException {

    private int erro_code = 0;

    public TelegramErroResponse(int erro_code) {
        this.erro_code = erro_code;
    }

    public TelegramErroResponse(int erro_code, String msg) {
        super(msg);
        this.erro_code = erro_code;
    }

    public TelegramErroResponse(int erro_code, String message, Throwable cause) {
        super(message, cause);
        this.erro_code = erro_code;
    }

    @Override
    public String getMessage() {
        return "error_code[" + erro_code + "] " + super.getMessage(); //To change body of generated methods, choose Tools | Templates.
    }

}
