package deptinfo.ubfc.quizz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import deptinfo.ubfc.quizz.helper.QuizzDatabase;
import deptinfo.ubfc.quizz.model.Answer;
import deptinfo.ubfc.quizz.model.Question;

public class MainActivity extends AppCompatActivity {

    private HttpGet hg;
    List<String> myTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.hg = new HttpGet();

        try {
            this.myTitles = new ArrayList<String>(this.hg.tacheHttpPage.execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Database
        /*QuizzDatabase db;
        db = new QuizzDatabase(getApplicationContext());
        // Creating questions
        Question question1 = new Question("A ?" , 2);
        Question question2 = new Question("B ?", 2);

        // Inserting questions in db
        long question1_id = db.createQuestion(question1);
        long question2_id = db.createQuestion(question2);

        //Set Questions ids
        question1.setId(question1_id);
        question2.setId(question2_id);

        Log.d("Question Count", "Tag Count: " + db.getAllQuestions().size());

        // Creating Answers
        Answer answer1 = new Answer("a1");
        Answer answer2 = new Answer("a2");
        Answer answer3 = new Answer("a3");

        Answer answer4 = new Answer("b1");
        Answer answer5 = new Answer("b2");

        long answer1_id = db.createAnswer(answer1,  question1_id );
        long answer2_id = db.createAnswer(answer2, question1_id );
        long answer3_id = db.createAnswer(answer3, question1_id );


        long answer4_id = db.createAnswer(answer4,  question2_id );
        long answer5_id = db.createAnswer(answer5,  question2_id );

        //Set Answers ids
        answer1.setId(answer1_id);
        answer2.setId(answer2_id);
        answer3.setId(answer3_id);
        answer4.setId(answer4_id);
        answer5.setId(answer5_id);

        Log.e("Answer Count", "Answer count: " + db.getAnswerCount());

        // "Post new Article" - assigning this under "Important" Tag
        // Now this will have - "Androidhive" and "Important" Tags
        db.createQuiz(answer4_id, question1_id);

        // Getting all answers
        Log.d("Get Answers", "Getting All Answers");

        List<Answer> allAnswers = db.getAllAnswers();
        for (Answer answer : allAnswers) {
            Log.d("Answer", answer.getAnswer());
        }

        // Getting answers under question a1
        Log.d("Answer", "Get answers under single question");

        List<Answer> tagsWatchList = db.getAllAnswersByQuestion(question1.getQuestion());
        for (Answer answer : tagsWatchList) {
            Log.d("Answer for a1", answer.getAnswer());
        }

        // Getting all Question
        Log.d("Get Question", "Getting All Questions");

        List<Question> allQuestions = db.getAllQuestions();
        for (Question question : allQuestions) {
            Log.d("Question", Long.toString(question.getId()));
            Log.d("Question", question.getQuestion());
        }

        // Deleting an answer
        Log.d("Delete Answer", "Deleting an answer");
        Log.d("Answer Count", "Answer Count Before Deleting: " + db.getAnswerCount());

        db.deleteAnswer(answer3_id);

        Log.d("Answer Count", "Answer Count After Deleting: " + db.getAnswerCount());

        // Deleting all Answer under "a2" question
        Log.d("Question Count",
                "Question Count Before Deleting 'a2' : "
                        + db.getQuestionCount());

        db.deleteQuestion(question1, true);


        Log.d("Question Count",
                "Question Count After Deleting 'a2': "
                        + db.getQuestionCount());

//        db.deleteAllQuestions(true);
//        db.deleteAllAnswers();
//        db.deleteAllQuizs();

        // Don't forget to close database connection
        db.closeDB();*/
    }
}
