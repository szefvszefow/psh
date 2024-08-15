package pl.hexlin.quiz;


import java.util.ArrayList;

public class PoliticalQuizManager {
    public final ArrayList<PoliticalQuiz> onGoingQuizes;

    public PoliticalQuizManager() {
        this.onGoingQuizes = new ArrayList<>();
    }

    public void addOngoingQuiz(PoliticalQuiz quiz) {
        onGoingQuizes.add(quiz);
    }

    public PoliticalQuiz getUserQuiz(String userId) {
        return onGoingQuizes.stream()
                .filter(quiz -> quiz.userId.equalsIgnoreCase(userId))
                .findFirst()
                .orElse(null);
    }
}
