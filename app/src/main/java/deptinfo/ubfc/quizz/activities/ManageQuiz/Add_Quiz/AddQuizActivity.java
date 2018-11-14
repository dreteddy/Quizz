package deptinfo.ubfc.quizz.activities.ManageQuiz.Add_Quiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.AddQuestion.AddQuestionActivity;

import deptinfo.ubfc.quizz.activities.ManageQuiz.ManageQuizActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Quiz;

public class AddQuizActivity extends AppCompatActivity {
    final static int GET_FROM_GALLERY = 1;
    private DatabaseHelper databaseHelper ;
    private long quiz_id;
    private String quizType;
    private boolean containsImg = false;
    Bitmap bitmapImg = null;

    private static final String TAG = "AddQuizActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        databaseHelper = new DatabaseHelper(this);
        EditText txtQuizType = findViewById(R.id.txt_quiz_type_real);

        txtQuizType.addTextChangedListener(new TextWatcher() {
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
        Upload Image
     */
    public void uploadImage(View view)
    {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            containsImg = true;

            try {
                //Get Image
                bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);



            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
        Save Quiz
     */
    public void saveQuiz(View view)
    {
        EditText txtQuizType = (EditText)findViewById(R.id.txt_quiz_type_real);

        quizType = txtQuizType.getText().toString();
        Quiz quiz = new Quiz();
        quiz.setQuizType(quizType);

        boolean quizAlreadyExists = this.databaseHelper.checkQuizAlreadyExist(quiz.getQuizType());

        //Check if quiz already exists
        if(! quizAlreadyExists) {
            this.quiz_id = this.databaseHelper.createQuiz(quiz);
            byte[] imageBytes = null;
            if(containsImg){
                //Convert bitmap to bytes
                //TODO
//                imageBytes = DbBitmapUtility.getBytes(bitmapImg);
//                databaseHelper.updateQuizImg( quizType , imageBytes);
            }

            Intent intent = new Intent(AddQuizActivity.this, AddQuestionActivity.class);
            intent.putExtra("quizType", quiz.getQuizType());
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Quiz : " + quiz.getQuizType() + " already exists",
                    Toast.LENGTH_LONG).show();
    }

    /*
        Go back to home page
     */
    public void goBackHome(View view)
    {
        Intent intent = new Intent(AddQuizActivity.this, ManageQuizActivity.class);
        startActivity(intent);
    }


}
