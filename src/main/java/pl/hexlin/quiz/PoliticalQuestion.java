package pl.hexlin.quiz;

import java.util.List;

public class PoliticalQuestion {
    public final int questionId;
    public final String questionContent;
    public final List<String> questionIdeologies;
    public final List<String> questionPositiveAxes;
    public final List<String> questionNegativeAxes;
    public final String guaranteedIdeologyIfYes;
    public final String guaranteedIdeologyIfNo;

    public PoliticalQuestion(int questionId, String questionContent, List<String> questionIdeologies, List<String> questionPositiveAxes, List<String> questionNegativeAxes, String guaranteedIdeologyIfYes, String guaranteedIdeologyIfNo) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.questionIdeologies = questionIdeologies;
        this.questionPositiveAxes = questionPositiveAxes;
        this.questionNegativeAxes = questionNegativeAxes;
        this.guaranteedIdeologyIfYes = guaranteedIdeologyIfYes;
        this.guaranteedIdeologyIfNo = guaranteedIdeologyIfNo;
    }

    public List<String> getQuestionIdeologies() {
        return questionIdeologies;
    }

    public String getQuestionContent() {
        return questionContent;
    }
}
