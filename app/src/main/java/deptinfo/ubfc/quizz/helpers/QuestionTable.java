package deptinfo.ubfc.quizz.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.models.Answer;
import deptinfo.ubfc.quizz.models.Question;
import deptinfo.ubfc.quizz.models.Quiz;

public class QuestionTable {

    private static final String LOG = "Question Table";
    private static final String KEY_ID = "id";
    private static final String TABLE_QUESTION = "questions";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_QUIZ_ID = "quiz_id";

    private Context mContext = null;

    public QuestionTable(Context mContext) {
        this.mContext = mContext;
    }

    /**
     *
     */
    protected static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUESTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUESTION + " TEXT,"
            + KEY_CORRECT_ANSWER + " INTEGER,"
            + KEY_QUIZ_ID + " INTEGER ,"
            + "FOREIGN KEY ("+ KEY_QUIZ_ID +") REFERENCES "+ TABLE_QUESTION +"("+ KEY_ID +"));";


    public long createQuestion(Question question, Quiz quiz) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion());
        values.put(KEY_CORRECT_ANSWER, question.getCorrect_answer());
        values.put(KEY_QUIZ_ID, quiz.getId());

        return db.insert(TABLE_QUESTION, null, values);
    }

    /**
     * getting answer count
     */
    public int getQuestionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * getting all Questions
     * */
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<Question>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Question q = new Question();
                q.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                q.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));
                q.setCorrect_answer(c.getInt(c.getColumnIndex(KEY_CORRECT_ANSWER)));

                // adding to tags list
                questions.add(q);
            } while (c.moveToNext());
        }
        return questions;
    }

    /*
     * Updating a Question
     */
    public int updateQuestion(Question question) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion());
        values.put(KEY_CORRECT_ANSWER, question.getCorrect_answer());

        // updating row
        return db.update(TABLE_QUESTION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getId()) });
    }

    /*
     * Deleting a question
     */
    public void deleteQuestion(Question question) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

            AnswerTable answerTable = new AnswerTable(mContext);
            List<Answer> allQuestionAnswers = answerTable.getAllAnswersByQuestion(question.getQuestion());

            // delete all answers
            for (Answer answer : allQuestionAnswers) {
                // delete answers
                answerTable.deleteAnswer(answer.getId());
            }

        // now delete the tag
        db.delete(TABLE_QUESTION, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getId()) });
    }

    /*
     * Deleting a question
     */
    public void deleteAllQuestions() {

        List<Question> questions = this.getAllQuestions();
        for (Question question : questions ) {
            deleteQuestion(question);
        }
    }

}