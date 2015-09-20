/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.types;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author victor
 */
public class ResponseParser {

    public static JSONObject objectToJSON(TelegramBaseClass teleObj) {
        JSONObject obj = new JSONObject();
        Field[] declaredFields = teleObj.getClass().getDeclaredFields();

        try {
            for (Field declaredField : declaredFields) {
                boolean accessible = declaredField.isAccessible();
                declaredField.setAccessible(true);
                if (declaredField.get(teleObj) == null) {
                    continue;
                }
                if (declaredField.getAnnotation(TelegramBaseClass.isBoolean.class) != null && ((Integer) declaredField.get(teleObj)) == -1) {
                    continue;
                }
                if (declaredField.getAnnotation(TelegramBaseClass.isBoolean.class) != null && ((Integer) declaredField.get(teleObj)) != -1) {
                    Integer val = (Integer) declaredField.get(teleObj);
                    obj.put(declaredField.getName(), val == 1);
                    continue;
                }
                
                if(TelegramBaseClass.class.isAssignableFrom(declaredField.getType())) {
                    obj.put(declaredField.getName(), objectToJSON((TelegramBaseClass) declaredField.get(teleObj)));
                    continue;
                }

                if (!declaredField.get(teleObj).equals(0)) {
                    obj.put(declaredField.getName(), declaredField.get(teleObj));
                }
                declaredField.setAccessible(accessible);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ResponseParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return obj;
    }

    public static void fieldArrayParser(Field declaredField, Object dest, JSONArray arrs) {
        try {
            Type genericType = declaredField.getGenericType();

            Pattern p = Pattern.compile("<(?<subtipo>.+)>");
            Matcher matcher = p.matcher(genericType.getTypeName());
            matcher.find();

            Class<?> tipoDaLista;
            Matcher matcher1 = p.matcher(matcher.group("subtipo"));
            String subTipo = null;
            
            if (matcher1.find()) {
                subTipo = matcher1.group("subtipo");
                tipoDaLista = Class.forName(matcher1.replaceAll(""));
            } else {
                tipoDaLista = Class.forName(matcher.group("subtipo"));
            }

            List lista = new LinkedList();

            boolean temp = declaredField.isAccessible();
            declaredField.setAccessible(true);

            for (Object arr : arrs) {
                if (!tipoDaLista.isPrimitive()
                        && !String.class.isAssignableFrom(tipoDaLista)
                        && TelegramBaseClass.class.isAssignableFrom(tipoDaLista)) {
                    lista.add(objectParser(tipoDaLista, (JSONObject) arr));
                } else if (List.class.isAssignableFrom(tipoDaLista)) {
                    lista.add(arrayParser(Class.forName(subTipo), (JSONArray) arr));
                } else {
                    lista.add(arr);
                }
            }

            declaredField.set(dest, lista);
            declaredField.setAccessible(temp);
        } catch (SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ResponseParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static <A> List<A> arrayParser(Class<A> type, JSONArray arrs) {

        List<A> list = new LinkedList();
        for (Object arr : arrs) {
            if (!List.class.isAssignableFrom(type)) {
                if (!type.isPrimitive()
                        && !String.class.isAssignableFrom(type)
                        && TelegramBaseClass.class.isAssignableFrom(type)) {
                    list.add(objectParser(type, (org.json.JSONObject) arr));
                } else {
                    list.add((A) arr);
                }
            } else {
                list.add((A) arrayParser(type.getComponentType(), new JSONArray(arr)));
            }
        }
        return list;
    }

    public static <A> A objectParser(Class<A> type, JSONObject obj) {
        Field[] declaredFields = type.getDeclaredFields();
        try {
            A newInstance = type.newInstance();
            for (Field declaredField : declaredFields) {

                Class<?> typeDoCampo = declaredField.getType();

                if (obj.has(declaredField.getName())) {
                    boolean k = declaredField.isAccessible();
                    declaredField.setAccessible(true);
                    if (List.class.isAssignableFrom(typeDoCampo)) {
                        fieldArrayParser(declaredField, newInstance, obj.getJSONArray(declaredField.getName()));
                    } else if (!typeDoCampo.isPrimitive()
                            && !typeDoCampo.getName().equals("java.lang.String")
                            && TelegramBaseClass.class.isAssignableFrom(typeDoCampo)) {
                        declaredField.set(newInstance, objectParser(typeDoCampo, obj.getJSONObject(declaredField.getName())));
                    } else {
                        Object get = obj.get(declaredField.getName());
                        if (get instanceof JSONObject && String.class.isAssignableFrom(typeDoCampo)) {
                            declaredField.set(newInstance, get.toString());
                        } else if (get instanceof Boolean) {
                            declaredField.set(newInstance, ((Boolean) get) ? 1 : 0);
                        } else {
                            declaredField.set(newInstance, get);
                        }

                    }
                    declaredField.setAccessible(k);

                }

            }
            return newInstance;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ResponseParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
