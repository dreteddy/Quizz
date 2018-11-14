package deptinfo.ubfc.quizz.activities.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.ManageQuizActivity;
import deptinfo.ubfc.quizz.activities.Play.PlayActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        this.dbHelper = new DatabaseHelper(getApplicationContext());
        this.dbHelper.closeDB();
    }

    /*
        Play Quiz: when button play is clicked
     */
    public void playQuiz(View view)
    {
        if (dbHelper.getAllQuizs().size() > 0 ){

            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"No Quiz available ! Download or add",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
        Add Quiz: when button add is clicked
     */
    public void manageQuiz(View view)
    {
        Intent intent = new Intent(MainActivity.this, ManageQuizActivity.class);
        startActivity(intent);
    }

    /*
        Exit Quiz: when button exit is clicked
     */
    public void exitQuiz(View view)
    {
        dbHelper.deleteAllAnswers();
        dbHelper.deleteAllQuestions();
        dbHelper.deleteAllQuizs();
        finish();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
