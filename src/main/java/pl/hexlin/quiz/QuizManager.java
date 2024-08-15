package pl.hexlin.quiz;


import java.util.ArrayList;

public class QuizManager {
    public final ArrayList<Quiz> availableQuizes;

    public QuizManager() {
        this.availableQuizes = new ArrayList<>();
    }

    public void addAvailableQuiz(Quiz quiz) {
        availableQuizes.add(quiz);
    }

    public Quiz getAvailableQuiz(String quizName) {
        return availableQuizes.stream()
                .filter(quiz -> quiz.getQuizName().equalsIgnoreCase(quizName))
                .findFirst()
                .orElse(null);
    }

    public Quiz getUserQuiz(String userId) {
        return availableQuizes.stream()
                .filter(quiz -> quiz.getUserId().equalsIgnoreCase(userId))
                .findFirst()
                .orElse(null);
    }
}
