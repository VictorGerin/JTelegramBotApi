/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import telegram.BotApi;
import telegram.Param;
import telegram.types.Message;
import telegram.types.Update;

/**
 *
 * @author victor
 */
public class Server {

    private final BotApi bot;
    private Executor exec;
    private BiConsumer<Message, Map<String, Object>> execBiFunction;
    private final Map<String, Map<String, Object>> sesions = new HashMap();

    public interface Executor {

        public abstract void exec(Message message, BotApi bot, Map<String, Object> session);
    }

    public Server(BotApi bot, Executor exec) {
        this.bot = bot;
        this.exec = exec;
    }

    public Server(BotApi bot, BiConsumer<Message, Map<String, Object>> execBiFunction) {
        this.bot = bot;
        this.execBiFunction = execBiFunction;
    }

    public void start() {
        int offset = 0;
        while (true) {
            for (Update update : bot.getUpdates(Param.get("offset", offset))) {
                int userId = update.getMessage().getFrom().getId();
                int chatId = update.getMessage().getChatId();
                String userId_chatId = Integer.toString(userId) + "_" + Integer.toString(chatId);
                final Map<String, Object> session = sesions.get(userId_chatId);
                if (session == null) {
                    final Map<String, Object> _session = new HashMap();
                    new Thread(() -> {
                        if (exec != null) {
                            exec.exec(update.getMessage(), bot, _session);
                        } else {
                            execBiFunction.accept(update.getMessage(), _session);
                        }
                    }).start();
                    sesions.put(userId_chatId, _session);
                } else {
                    new Thread(() -> {
                        if (exec != null) {
                            exec.exec(update.getMessage(), bot, session);
                        } else {
                            execBiFunction.accept(update.getMessage(), session);
                        }
                    }).start();
                }
                offset = update.getUpdate_id() + 1;
            }
        }
    }
}
