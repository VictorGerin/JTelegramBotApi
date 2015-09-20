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
public class TelegramBaseException extends RuntimeException {

    /**
     * Creates a new instance of <code>telegramBaseException</code> without
     * detail message.
     */
    public TelegramBaseException() {
    }

    /**
     * Constructs an instance of <code>telegramBaseException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TelegramBaseException(String msg) {
        super(msg);
    }

    public TelegramBaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
