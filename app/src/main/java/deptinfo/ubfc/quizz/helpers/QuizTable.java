package deptinfo.ubfc.quizz.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.models.Quiz;

public class QuizTable {
    private static final String TABLE_QUIZ = "quizs";
    private static final String KEY_ID = "id";
    private static final String KEY_QUIZ_TYPE = "quiz_type";
    private Context mContext = null;

    public QuizTable(Context mContext) {
        this.mContext = mContext;
    }

    // Quiz table create statement
    public static final String CREATE_TABLE_QUIZ = "CREATE TABLE "
            + TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUIZ_TYPE + " TEXT)";


    /*
     * Creating Quiz
     */
    public long createQuiz(Quiz quiz) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_TYPE, quiz.getQuizType());

        return db.insert(TABLE_QUIZ, null, values);
    }

    /*
     * getting all quizs
     * */
    public List<Quiz> getAllQuizs() {
        List<Quiz> quizs = new ArrayList<Quiz>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ;

        //Log.e(LOG, selectQuery);

        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Quiz quiz = new Quiz();
                quiz.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                quiz.setQuizType(c.getString(c.getColumnIndex(KEY_QUIZ_TYPE)));

                // adding to answer list
                quizs.add(quiz);
            } while (c.moveToNext());
        }

        return quizs;
    }

    /**
     * Updating a quiz
     */
    /*public int updateNoteTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }*/

    /**
     * Deleting a quiz
     */
    public void deleteQuiz(long id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();
        db.delete(TABLE_QUIZ, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting aLL quiz
     */
    public void deleteAllQuizs() {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        // now all quizs
        db.delete(TABLE_QUIZ, null, null);
    }
}
