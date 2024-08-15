package pl.hexlin.question;

import org.bson.Document;
import pl.hexlin.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class QuestionManager {
    public final Map<String, Question> questionMap = new HashMap<>();
    public final ArrayList<Question> questions;
    private final Instance instance;

    public QuestionManager(Instance instance) {
        this.questions = new ArrayList<>();
        this.instance = instance;
        instance.questionCollection.find().forEach((Consumer<? super Document>) document -> {
            Question question = new Question(document, instance);
            questionMap.putIfAbsent(question.getId(), question);
            questions.add(question);
        });
    }

    public void addQuestion(Question question) {
        questionMap.putIfAbsent(question.getId(), question);
        question.save();
    }

    public Question getQuestion(String questionId) {
        return questionMap.get(questionId);
    }
}
