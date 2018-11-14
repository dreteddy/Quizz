package deptinfo.ubfc.quizz.activities.ManageQuiz.AddAnswer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.AddQuestion.AddQuestionActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Answer;
import deptinfo.ubfc.quizz.models.Question;

public class AddAnswerActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper ;
    private String question;
    private String answerString;
    private int answerNB;

    private static final String TAG = "AddAnswerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        question = intent.getStringExtra("question");
        answerNB = intent.getIntExtra("answerNB", -1);

        EditText txtAnswer = findViewById(R.id.txt_answer);
        txtAnswer.addTextChangedListener(new TextWatcher() {
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
    public void saveAnswer(View view)
    {
        EditText txtAnswer = findViewById(R.id.txt_answer);

        answerString = txtAnswer.getText().toString();

        Answer answer = new Answer(answerString);
        String quizType = this.databaseHelper.getQuizForQuestion(question);
        long question_id = this.databaseHelper.getIdForQuestion(question, quizType);

        Question quest = new Question();
        quest.setQuestion(question);
        quest.setId(question_id);

        //Check if this answer already exist for parent question
        boolean questionAlreadyExists = this.databaseHelper.checkAnswerAlreadyExist(answerString, question);
        if(! questionAlreadyExists) {
            this.databaseHelper.createAnswer(answer, quest);

            //If this is correct answer then MAJ question table on DB
            RadioButton rdo_is_correct_ans = findViewById(R.id.rbtn_correct_answer);
            if (rdo_is_correct_ans.isChecked()){
                Log.i(TAG , "Correct answer nb :" + answerNB);
                this.databaseHelper.updateCorrectAnswerForQuestion(question, answerNB);
            }



            Intent intent = new Intent(AddAnswerActivity.this, AddAnswerActivity.class);
            intent.putExtra("question", question);
            intent.putExtra("answerNB", (answerNB + 1));
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Question : " + question + " already has Answer : " + answer.getAnswer(),
                    Toast.LENGTH_LONG).show();
    }

    /*
        Go back to quiz page
     */
    public void goBackToQuestion(View view)
    {
        Intent intent = new Intent(AddAnswerActivity.this, AddQuestionActivity.class);
        String quiz = databaseHelper.getQuizForQuestion(question);
        intent.putExtra("quizType", quiz);
        startActivity(intent);
    }
}
