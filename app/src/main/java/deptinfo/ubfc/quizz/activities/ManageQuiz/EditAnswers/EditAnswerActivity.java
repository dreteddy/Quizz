package deptinfo.ubfc.quizz.activities.ManageQuiz.EditAnswers;

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
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditAnswers.adapters.EditAnswersAdapter;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuestions.EditQuestionActivity;
import deptinfo.ubfc.quizz.activities.Play.SingleQuestion.adapters.SingleQuestionAdapter;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Answer;

public class EditAnswerActivity extends AppCompatActivity {

    private RecyclerView vueAnswers;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper databaseHelper;

    private static final String TAG = "SingleQuestionActivity";
    private List<String> answersString;
    private List<Answer> answers;
    private String question;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.play_layout);
        vueAnswers = findViewById(R.id.quiz_types);
        TextView questions_title = findViewById(R.id.play_title);

        answersString = new ArrayList<>();
        answers = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        //Get quiz questions from play activity
        Intent intent = getIntent();
        question = intent.getStringExtra("Question");

        //Set text for button to go back
        Button backBtn = findViewById(R.id.btn_act_exit);
        backBtn.setText(R.string.btn_back);

        questions_title.setText(question);
        answers = databaseHelper.getAllAnswersFromQuestion(question);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueAnswers.setLayoutManager(layoutManager);

        for (Answer answer : answers) {
            String str = answer.getAnswer();
            answersString.add(str);
        }

        // define an adapter
        adapter = new EditAnswersAdapter(this, answersString, question);
        vueAnswers.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        databaseHelper.deleteAnswer(answersString.get(viewHolder.getAdapterPosition()) , question);
                        answersString.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(vueAnswers);

        databaseHelper.closeDB();
    }

    /*
        Go Back
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(EditAnswerActivity.this, EditQuestionActivity.class);
        String quizType = databaseHelper.getQuizForQuestion(question);
        intent.putExtra("quizType", quizType);
        startActivity(intent);
    }
}
