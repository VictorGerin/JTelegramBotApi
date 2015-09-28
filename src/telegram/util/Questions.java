/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegram.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import telegram.BotApi;
import telegram.Param;
import telegram.types.Message;
import telegram.types.Update;

/**
 *
 * @author victor
 */
public class Questions implements Predicate<Update> {

    private static class Question {

        public String question;
        public Consumer<Message> response;
        public Param[] params = new Param[]{};
    }

    public Questions(BotApi bot) {
        this.bot = bot;
        bot.addRedirect(this);
    }

    @Override
    protected void finalize() throws Throwable {
        bot.removeRedirect(this);
        super.finalize();
    }

    private BotApi bot;
    private Map<Integer, Integer> users = new HashMap();
    private List<Question> questions = new ArrayList(25);

    private Question onCancel = null;

    public void analizeUser(Message inchat) {
        users.put(inchat.getFrom().getId(), -1);
        test(inchat);
    }

    public void unAnalizeUser(Message inchat) {
        users.remove(inchat.getFrom().getId());
    }

    public void setOnCancel(String question, Consumer<Message> process, Param... params) {
        Question q = new Question();
        q.question = question;
        q.response = process;
        q.params = params;
        onCancel = q;
    }

    public void addQuestion(String question, Consumer<Message> process, Param... params) {
        Question _question = new Question();
        _question.question = question;
        _question.response = process;
        _question.params = params;
        questions.add(_question);
    }

    private boolean test(Message m) {
        Integer pos = users.getOrDefault(m.getFrom().getId(), Integer.MIN_VALUE);
        if (pos != Integer.MIN_VALUE) {
            if (onCancel != null) {
                if (m.getText().equals(onCancel.question)) {
                    onCancel.response.accept(m);
                    unAnalizeUser(m);
                    return true;
                }
            }
            if (pos >= questions.size()) {
                unAnalizeUser(m);
            } else if (pos == -1) {
                Question get = questions.get(0);
                bot.sendMessage(m.getChatId(), get.question, get.params);
                users.put(m.getFrom().getId(), ++pos);
                return true;
            } else {
                questions.get(pos).response.accept(m);
                if (pos != questions.size() - 1) {
                    bot.sendMessage(m.getChatId(), questions.get(pos + 1).question, questions.get(pos + 1).params);
                }
                users.put(m.getFrom().getId(), ++pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean test(Update t) {
        return test(t.getMessage());
    }

}
