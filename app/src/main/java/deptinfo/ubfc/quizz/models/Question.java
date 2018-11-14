package deptinfo.ubfc.quizz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Question  implements Serializable {
    private long id;
    private int correct_answer;
    private String question;
    private ArrayList<Answer> answers;

    public Question() {
        answers = new ArrayList<Answer>();
    }

    public Question(String question, int correct_answer) {
        this.correct_answer = correct_answer;
        this.question = question;
        answers = new ArrayList<Answer>();
    }

    public Question(long id, String question, int correct_answer) {
        this.id = id;
        this.correct_answer = correct_answer;
        this.question = question;
        answers = new ArrayList<Answer>();
    }

    public long getId() {
        return id;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public int getCorrect_answer() {
        return correct_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = myTrim(question);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCorrect_answer(int valid_answer) {
            this.correct_answer = valid_answer;
    }

    public String myTrim(String str){
        return str.trim();
    }

    @Override
    public String toString() {
        return "Question{" +
                "correct_answer=" + correct_answer +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                '}';
    }
}
