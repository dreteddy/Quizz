package deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuizs;

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
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuizs.adapters.EditQuizsAdapter;
import deptinfo.ubfc.quizz.activities.ManageQuiz.ManageQuizActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Quiz;

public class EditQuizsActivity extends AppCompatActivity {

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

        TextView title = findViewById(R.id.play_title);
        title.setText("Edit Quizs");

        quizs = new ArrayList<>();
        quizTypes = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        quizs = (ArrayList<Quiz>)databaseHelper.getAllQuizs();

        for (Quiz quiz: quizs)
            quizTypes.add(quiz.getQuizType());

        //Set text for button to go back
        Button backBtn = findViewById(R.id.btn_act_exit);
        backBtn.setText(R.string.result_btn_manage);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        vueTitles.setLayoutManager(layoutManager);

        // define an adapter
        adapter = new EditQuizsAdapter(this, quizTypes);
        vueTitles.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        databaseHelper.deleteQuiz(quizTypes.get(viewHolder.getAdapterPosition()));
                        quizTypes.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(vueTitles);

        databaseHelper.closeDB();
    }

    /*
        Go Back
     */
    public void goBack(View view)
    {
        Intent intent = new Intent(EditQuizsActivity.this, ManageQuizActivity.class);
        startActivity(intent);
    }
}