/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author victor
 */
public class TelegramBaseClass {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    static @interface isBoolean {
    }

    protected static <T> List<List<T>> getList(T[][] list) {
        LinkedList<List<T>> lista = new LinkedList();
        for (T[] list1 : list) {
            lista.add(Arrays.asList(list1));
        }
        return lista;
    }

    @Override
    public String toString() {
        return ResponseParser.objectToJSON(this).toString(4);
    }

}
