package deptinfo.ubfc.quizz.activities.Play.SingleQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.Play.QuestionList.QuestionActivity;
import deptinfo.ubfc.quizz.activities.Play.SingleQuestion.adapters.SingleQuestionAdapter;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Answer;

public class SingleQuestionActivity extends AppCompatActivity {

    private RecyclerView vueAnswers;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper databaseHelper;

    private static final String TAG = "SingleQuestionActivity";
    private List<String> answersString;
    private List<Answer> answers;
    private List<String> questions;
    private int questionStart = 0;
    private int currentResult;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.play_layout);
        vueAnswers = findViewById(R.id.quiz_types);
        TextView questions_title = findViewById(R.id.play_title);

        Button backBtn = findViewById(R.id.btn_act_exit);
        backBtn.setText(R.string.result_btn_to_questions);

        answersString = new ArrayList<>();
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        //Get quiz questions from play activity
        Intent intent = getIntent();
        questions = intent.getStringArrayListExtra("questions");
        questionStart = intent.getIntExtra("questionStart" , -1);
        currentResult = intent.getIntExtra("currentResult" , 0);
        String question = questions.get(questionStart);

        String layoutTitle = getResources().getString(R.string.question_title) + " " + question;
        questions_title.setText(layoutTitle);
        answers = databaseHelper.getAllAnswersFromQuestion(question);
        int correctAnswer = databaseHelper.getCorrectAnswer(question);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueAnswers.setLayoutManager(layoutManager);

        for (Answer answer : answers) {
            String str = answer.getAnswer();
            answersString.add(str);
        }

        // define an adapter
        adapter = new SingleQuestionAdapter(this, answersString, questionStart, questions, correctAnswer, currentResult);
        vueAnswers.setAdapter(adapter);

        databaseHelper.closeDB();
    }

    /*
        Go Back To Home Page
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(SingleQuestionActivity.this, QuestionActivity.class);
        String quizType = databaseHelper.getQuizForQuestion(questions.get(questionStart));
        intent.putExtra("quizType", quizType);
        startActivity(intent);
    }
}
