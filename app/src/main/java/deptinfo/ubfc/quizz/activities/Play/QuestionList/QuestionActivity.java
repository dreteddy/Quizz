package deptinfo.ubfc.quizz.activities.Play.QuestionList;

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
import deptinfo.ubfc.quizz.activities.Play.PlayActivity;
import deptinfo.ubfc.quizz.activities.Play.QuestionList.adapters.QuestionListAdapter;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Question;

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView vueQuestions;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper databaseHelper;

    private static final String TAG = "QuestionActivity";
    List<String> questionsString;
    List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_layout);
        vueQuestions = findViewById(R.id.quiz_types);
        TextView questions_list_title = findViewById(R.id.play_title);

        Button backBtn = findViewById(R.id.btn_act_exit);
        backBtn.setText(R.string.result_btn_to_quizs);

        questionsString = new ArrayList<>();
        questions = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        //Get quiz type from play activity
        Intent intent = getIntent();
        String quizType = intent.getStringExtra("quizType");

        String layoutTitle = getResources().getString(R.string.quiz_title) + " " + quizType;
        questions_list_title.setText(layoutTitle);
        questions = databaseHelper.getAllQuestionsFromQuiz(quizType);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueQuestions.setLayoutManager(layoutManager);

        for (Question question : questions) {
            String str = question.getQuestion();
            questionsString.add(str);
        }

        // define an adapter
        adapter = new QuestionListAdapter(this, questionsString);
        vueQuestions.setAdapter(adapter);

        databaseHelper.closeDB();
    }

    /*
        Go Back To Home Page
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(QuestionActivity.this, PlayActivity.class);
        startActivity(intent);
    }
}
