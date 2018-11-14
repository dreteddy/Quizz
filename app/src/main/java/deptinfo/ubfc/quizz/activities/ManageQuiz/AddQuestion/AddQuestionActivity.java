package deptinfo.ubfc.quizz.activities.ManageQuiz.AddQuestion;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.AddAnswer.AddAnswerActivity;
import deptinfo.ubfc.quizz.activities.ManageQuiz.Add_Quiz.AddQuizActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Question;
import deptinfo.ubfc.quizz.models.Quiz;

public class AddQuestionActivity extends AppCompatActivity {

    private static final String TAG = "AddQuestionActivity";
    private DatabaseHelper databaseHelper ;
    private String quizType;
    private String questionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        databaseHelper = new DatabaseHelper(this);
        EditText txtQuestion = findViewById(R.id.txt_question);

        Intent intent = getIntent();
        quizType = intent.getStringExtra("quizType");

        txtQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(count != 0) {
                    Button btn_save = findViewById(R.id.btn_quiz_save);
                    btn_save.setEnabled(true);
                    btn_save.setBackgroundColor(Color.WHITE);
                }
                else {
                    Button btn_save = findViewById(R.id.btn_quiz_save);
                    btn_save.setEnabled(false);
                    btn_save.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() != 0) {
                    Button btn_save = findViewById(R.id.btn_quiz_save);
                    btn_save.setEnabled(true);
                    btn_save.setBackgroundColor(Color.WHITE);
                }
                else {
                    Button btn_save = findViewById(R.id.btn_quiz_save);
                    btn_save.setEnabled(false);
                    btn_save.setBackgroundColor(Color.GRAY);
                }
            }
        });

        databaseHelper.closeDB();
    }

    /*
        Save Quiz
     */
    public void saveQuestion(View view)
    {
        EditText txtQuestion = findViewById(R.id.txt_question);

        questionString = txtQuestion.getText().toString();
        Question question = new Question();
        question.setQuestion(questionString);

        Quiz quiz = new Quiz(quizType);
        quiz.setId(this.databaseHelper.getIdForQuiz(quizType));

        //Check if this question already exists for quiz
        boolean questionAlreadyExists = this.databaseHelper.checkQuestionAlreadyExist(question.getQuestion(), quizType);

        if(! questionAlreadyExists) {
            this.databaseHelper.createQuestion(question , quiz);

            Intent intent = new Intent(AddQuestionActivity.this, AddAnswerActivity.class);
            intent.putExtra("question", questionString);
            intent.putExtra("answerNB", 1);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Question : " + question.getQuestion() + " already exists for Quiz : " + quizType,
                    Toast.LENGTH_LONG).show();

    }

    /*
        Go back to home page
     */
    public void goBackToQuiz(View view)
    {
        Intent intent = new Intent(AddQuestionActivity.this, AddQuizActivity.class);
        startActivity(intent);
    }
}
