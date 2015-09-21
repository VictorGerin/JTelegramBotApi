/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram;

/**
 *
 * @author victor
 */
public class Param {

    private String name;
    private String value;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public static Param get(String name, String value) {
        return new Param(name, value);
    }
    public static Param get(String name, telegram.types.TelegramBaseClass value) {
        return new Param(name, telegram.types.ResponseParser.objectToJSON(value).toString());
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
