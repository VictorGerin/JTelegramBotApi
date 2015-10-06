/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
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
    private long sessionTime = 30 * 60 * 1000;
    private Executor exec;
    private BiConsumer<Message, Map<String, Object>> execBiFunction;
    private final Map<String, Session> sesions = new HashMap();

    private class Session {

        public Map<String, Object> session = new HashMap();
        public long lastUpdate = System.currentTimeMillis();
    }

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

    public void setSessionTime(long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public long getSessionTime() {
        return sessionTime;
    }

    public void start() {
        int offset = 0;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (sessionTime >= 0) {
                    List<String> collect = (sesions)
                            .keySet()
                            .stream()
                            .filter((s) -> System.currentTimeMillis() - sesions.get(s).lastUpdate > sessionTime)
                            .collect(Collectors.toList());

                    for (String remove : collect) {
                        sesions.remove(remove);
                    }
                }
            }
        }, 0, 100);

        while (true) {

            for (Update update : bot.getUpdates(Param.get("offset", offset))) {
                new Thread(() -> {

                    int userId = update.getMessage().getFrom().getId();
                    int chatId = update.getMessage().getChatId();
                    String userId_chatId = Integer.toString(userId) + "_" + Integer.toString(chatId);

                    Session session = sesions.get(userId_chatId);
                    if (session == null) {
                        sesions.put(userId_chatId, (session = new Session()));
                    }

                    if (exec != null) {
                        exec.exec(update.getMessage(), bot, session.session);
                    } else {
                        execBiFunction.accept(update.getMessage(), session.session);
                    }

                    session.lastUpdate = System.currentTimeMillis();
                }).start();
                
                offset = update.getUpdate_id() + 1;
            }
        }
    }
}
