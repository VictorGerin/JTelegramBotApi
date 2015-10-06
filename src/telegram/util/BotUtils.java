/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.util;

import java.util.function.Consumer;
import telegram.BotApi;
import telegram.Param;
import telegram.exception.TelegramBaseException;
import telegram.types.Update;

/**
 *
 * @author victor
 */
public class BotUtils {

    public static void UpdaterSynchronous(BotApi bot, Consumer<Update> run) {
        UpdaterSynchronous(bot, run, true);
    }

    public static void UpdaterSynchronous(BotApi bot, Consumer<Update> run, boolean useTelegramOffset) {
        int offset = 0;
        while (true) {
            try {
                if (useTelegramOffset) {
                    for (Update update : bot.getUpdates(Param.get("offset", offset))) {
                        offset = update.getUpdate_id() + 1;
                        run.accept(update);
                    }

                } else {
                    for (Update update : bot.getUpdates()) {
                        if (update.getUpdate_id() >= offset) {
                            offset = update.getUpdate_id() + 1;
                            run.accept(update);
                        }
                    }
                }
            } catch (TelegramBaseException ex) {

            }
        }
    }
}
