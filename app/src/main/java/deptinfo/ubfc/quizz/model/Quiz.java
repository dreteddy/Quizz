package deptinfo.ubfc.quizz.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Quiz {
    int id;
    private ArrayList<Question> question;
    private String quizType;

    public Quiz() {

    }

    public Quiz(int id, String quizType) {
        this.id = id;
        this.quizType = quizType;
    }

    public Quiz(String quizType) {
        question = new ArrayList<Question>();
        this.quizType = quizType;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Question> getQuestion() {
        return question;
    }

    public void addQuestion(Question question) {
        this.question.add(question);
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }
}
