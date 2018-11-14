package deptinfo.ubfc.quizz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable{
    long id;
    private ArrayList<Question> questions;
    private String quizType;

    public Quiz() {
        this.questions = new ArrayList<Question>();
    }

    public Quiz(int id, String quizType) {
        this.id = id;
        this.quizType = quizType;
        this.questions = new ArrayList<Question>();
    }

    public Quiz(String quizType) {
        this.questions = new ArrayList<Question>();
        this.quizType = quizType;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Question> getQuestion() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "questions=" + questions +
                ", quizType='" + quizType + '\'' +
                '}';
    }
}
