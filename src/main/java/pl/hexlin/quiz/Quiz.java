package pl.hexlin.quiz;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import pl.hexlin.Instance;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    public final String userId;
    public final String quizName;
    public final int quizStages;
    public int currentStage;
    public ArrayList<String> yesAnswered;
    public final String full;
    public final String almostfull;
    public final String half;
    public final String almostHalf;
    public final String none;
    public List<String> questions;
    public final Instance instance;

    public Quiz(String userId, String quizName, int quizStages, List<String> questions, String full, String almostfull, String half, String almostHalf, String none, Instance instance) {
        this.userId = userId;
        this.quizName = quizName;
        this.quizStages = quizStages;
        this.questions = questions;
        this.full = full;
        this.almostfull = almostfull;
        this.half = half;
        this.almostHalf = almostHalf;
        this.none = none;
        this.instance = instance;
        this.yesAnswered = new ArrayList<>();
        this.currentStage = 0;
    }
    public int getCurrentStage() {
        return currentStage;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public int getQuizStages() {
        return quizStages;
    }

    public String getResult() {
        int numYes = yesAnswered.size();
        double percentage = Math.min((double) numYes / questions.size() * 100, 100);

        if (percentage >= 100) {
            return full;
        } else if (percentage >= 65) {
            return almostfull;
        } else if (percentage >= 40) {
            return half;
        } else if (percentage >= 25) {
            return almostHalf;
        } else {
            return none;
        }
    }

    public double getPercentage() {
        int numYes = yesAnswered.size();
        double percentage = (double) numYes / questions.size() * 100;
        return percentage;
    }

    public String getQuizName() {
        return quizName;
    }

    public void next(String messageId, TextChannel textChannel) {
        currentStage++;
        instance.api.getMessageById(messageId, textChannel).thenAccept(message -> {
            int questionsize = questions.size() - 1;
            message.createUpdater().removeAllComponents().setContent(questions.get(currentStage) + " | " + currentStage + "/" + questionsize).addComponents(ActionRow.of(Button.success("quiz-yes", "✅ Tak")), ActionRow.of(Button.danger("quiz-no", "❎ Nie"))).applyChanges();
        });
    }

    public void finish(String messageId, TextChannel textChannel) {
        currentStage++;
        instance.api.getMessageById(messageId, textChannel).join().delete().join();
    }
}
