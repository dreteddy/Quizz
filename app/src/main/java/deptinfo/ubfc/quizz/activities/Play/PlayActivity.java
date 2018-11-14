package deptinfo.ubfc.quizz.activities.Play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.activities.Main.MainActivity;
import deptinfo.ubfc.quizz.activities.Play.adapters.PlayAdapter;
import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Quiz;

public class PlayActivity extends AppCompatActivity {

    private RecyclerView vueTitles;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper databaseHelper;

    List<String> quizTypes;
    ArrayList<Quiz> quizs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_layout);
        vueTitles = findViewById(R.id.quiz_types);

        quizs = new ArrayList<>();
        quizTypes = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        quizs = (ArrayList<Quiz>)databaseHelper.getAllQuizs();

        for (Quiz quiz: quizs)
            quizTypes.add(quiz.getQuizType());

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueTitles.setLayoutManager(layoutManager);

        // define an adapter
        adapter = new PlayAdapter(this, quizTypes);
        vueTitles.setAdapter(adapter);

        databaseHelper.closeDB();
    }

    /*
        Go Back To Home Page
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }
}