package deptinfo.ubfc.quizz.model;

public class QuestionType {
    int id;
    String type;

    //Constructors
    public QuestionType(String type) {
        this.type = type;
    }

    public QuestionType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
