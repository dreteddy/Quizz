package deptinfo.ubfc.quizz.model;

public class User {
    int id;
    int nbQuestionAnswered;
    int nbCorrectAnswer;
    int rank;

    public User(int id, int nbQuestionAnswered, int nbCorrectAnswer, int rank) {
        this.id = id;
        this.nbQuestionAnswered = nbQuestionAnswered;
        this.nbCorrectAnswer = nbCorrectAnswer;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbQuestionAnswered() {
        return nbQuestionAnswered;
    }

    public void setNbQuestionAnswered(int nbQuestionAnswered) {
        this.nbQuestionAnswered = nbQuestionAnswered;
    }

    public int getNbCorrectAnswer() {
        return nbCorrectAnswer;
    }

    public void setNbCorrectAnswer(int nbCorrectAnswer) {
        this.nbCorrectAnswer = nbCorrectAnswer;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
