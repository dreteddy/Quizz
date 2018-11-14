package deptinfo.ubfc.quizz.activities.ManageQuiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.Add_Quiz.AddQuizActivity;
import deptinfo.ubfc.quizz.activities.Main.HttpGet;
import deptinfo.ubfc.quizz.activities.Main.MainActivity;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuizs.EditQuizsActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Quiz;

public class ManageQuizActivity extends AppCompatActivity {
    // XML from server
    private HttpGet hg;
    private DatabaseHelper dbHelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_quiz);

        this.dbHelper = new DatabaseHelper(getApplicationContext());
        this.dbHelper.closeDB();
    }



    /*
        Add Quiz
     */
    public void addQuiz(View view)
    {
        Intent intent = new Intent(ManageQuizActivity.this, AddQuizActivity.class);
        startActivity(intent);
    }

    /*
        Edit Quiz
     */
    public void editQuiz(View view)
    {

        if (dbHelper.getAllQuizs().size() > 0 ){
            Intent intent = new Intent(ManageQuizActivity.this, EditQuizsActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"No Quiz available ! Download or add",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
        Download Quiz
    */
    public void downloadQuiz(View view)
    {
        // Download quizs from server and set values
        this.hg = new HttpGet(this.dbHelper);

        try {
            this.hg.tacheHttpPage.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Quiz> quizArrayList =  (ArrayList<Quiz>)dbHelper.getAllQuizs();
        Toast.makeText(getApplicationContext(),quizArrayList.size() +" Quizs downloaded from server",
                Toast.LENGTH_LONG).show();

    }

    /*
        Go back to home page
     */
    public void goBackHome(View view)
    {
        Intent intent = new Intent(ManageQuizActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
