package deptinfo.ubfc.quizz.models;

import java.io.Serializable;

public class Answer  implements Serializable {
    private long id;
    private String answer;

    public Answer() {
    }

    public Answer(String answer) {
        this.answer = answer;
    }

    public Answer(long id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = myTrim(answer);
    }

    public String myTrim(String str){
        return str.trim();
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
