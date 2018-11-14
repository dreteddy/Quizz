package deptinfo.ubfc.quizz.activities.Play.Result;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.Play.PlayActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    private int currentResult;
    private int maxResult;
    private String quizType;
    private String lastQuestion;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        databaseHelper = new DatabaseHelper(this);
        //Get quiz questions from play activity
        Intent intent = getIntent();
        currentResult = intent.getIntExtra("currentResult" , -1);
        lastQuestion = intent.getStringExtra("lastQuestion");

        Log.i("TEST " + TAG , String.valueOf(currentResult));
        quizType = databaseHelper.getQuizForQuestion(lastQuestion);

        //Get highest result before adding new result
        maxResult = databaseHelper.getHighestResult(quizType);
        databaseHelper.createResult(quizType , currentResult );

        TextView result_title = findViewById(R.id.result_title);
        TextView highest_score = findViewById(R.id.result_highest_score);
        TextView current_score = findViewById(R.id.result_current_score);

        result_title.append(quizType);
        highest_score.append(String.valueOf(maxResult));
        current_score.append(String.valueOf(currentResult));

        databaseHelper.closeDB();
    }

    /*
        Go Back to Quizs: when button is clicked
     */
    public void goBackToQuizs(View view)
    {
        Intent intent = new Intent(this, PlayActivity.class);
        this.startActivity(intent);
    }

    /*
        Exit Quiz: when button exit is clicked
     */
    public void exitQuiz(View view)
    {
        databaseHelper.deleteAllAnswers();
        databaseHelper.deleteAllQuestions();
        databaseHelper.deleteAllQuizs();

        finish();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
