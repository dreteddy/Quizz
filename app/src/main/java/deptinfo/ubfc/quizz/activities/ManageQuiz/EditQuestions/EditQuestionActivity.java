package deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuestions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuestions.adapters.EditQuestionsAdapter;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuizs.EditQuizsActivity;
import deptinfo.ubfc.quizz.activities.Play.QuestionList.adapters.QuestionListAdapter;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Question;

public class EditQuestionActivity extends AppCompatActivity {

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

        questionsString = new ArrayList<>();
        questions = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        //Get quiz type from play activity
        Intent intent = getIntent();
        String quizType = intent.getStringExtra("quizType");

        //Set text for button to go back
        Button backBtn = findViewById(R.id.btn_act_exit);
        backBtn.setText(R.string.btn_back);

        questions_list_title.setText(quizType);
        questions = databaseHelper.getAllQuestionsFromQuiz(quizType);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueQuestions.setLayoutManager(layoutManager);

        for (Question question : questions) {
            String str = question.getQuestion();
            questionsString.add(str);
        }

        // define an adapter
        adapter = new EditQuestionsAdapter(this, questionsString);
        vueQuestions.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        databaseHelper.deleteQuestion(questionsString.get(viewHolder.getAdapterPosition()));
                        questionsString.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(vueQuestions);

        databaseHelper.closeDB();
    }

    /*
        Go Back
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(EditQuestionActivity.this, EditQuizsActivity.class);
        startActivity(intent);
    }
}
