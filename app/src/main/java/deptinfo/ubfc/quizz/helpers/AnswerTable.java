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

public class AnswerTable {

    private static final String LOG = "Answer Table";
    private static final String KEY_ID = "id";
    private static final String KEY_ANSWER = "answer";
    private static final String TABLE_ANSWER = "answers";
    private static final String TABLE_QUESTION = "questions";
    private static final String TABLE_QUIZ = "quizs";
    private static final String KEY_ANSWER_ID = "answer_id";
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_QUESTION = "question";


    private Context mContext = null;

    public AnswerTable(Context mContext) {
        this.mContext = mContext;
    }

    protected static final String CREATE_TABLE_ANSWER = "CREATE TABLE "
            + TABLE_ANSWER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ANSWER + " TEXT ,"
            + KEY_QUESTION_ID + "INTEGER ,"
            + "FOREIGN KEY ("+ KEY_QUESTION_ID +") REFERENCES "+ TABLE_QUESTION +"("+ KEY_ID +"));";

    public long createAnswer(Answer answer, Question question) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, answer.getAnswer());
        values.put(KEY_QUESTION_ID, question.getId());

        return db.insert(TABLE_ANSWER, null, values);
    }

    /*
     * get single answer
     */
    public Answer getAnswer(long answer_id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ANSWER + " WHERE "
                + KEY_ID + " = " + answer_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Answer ans = new Answer();
        ans.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ans.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));

        return ans;
    }

    /**
     * getting answer count
     */
    public int getAnswerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ANSWER;
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * getting all answers
     * */
    public List<Answer> getAllAnswers() {
        List<Answer> answers = new ArrayList<Answer>();
        String selectQuery = "SELECT  * FROM " + TABLE_ANSWER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Answer ans = new Answer();
                ans.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                ans.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));

                // adding to answer list
                answers.add(ans);
            } while (c.moveToNext());
        }

        return answers;
    }

    /*
     * getting all answers from same question
     * */
    public List<Answer> getAllAnswersByQuestion(String question) {
        List<Answer> todos = new ArrayList<Answer>();

        String selectQuery = "SELECT  * FROM " + TABLE_ANSWER + " ans, "
                + TABLE_QUESTION + " quest, " + TABLE_QUIZ + " quiz WHERE quest."
                + KEY_QUESTION + " = '" + question + "'" + " AND quest." + KEY_ID
                + " = " + "quiz." + KEY_QUESTION_ID + " AND ans." + KEY_ID + " = "
                + "quiz." + KEY_ANSWER_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Answer ans = new Answer();
                ans.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                ans.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));

                // adding to answers list
                todos.add(ans);
            } while (c.moveToNext());
        }

        return todos;
    }

    /*
     * Updating an answer
     */
    public int updateAnswers(Answer answer) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, answer.getAnswer());

        // updating row
        return db.update(TABLE_ANSWER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(answer.getId()) });
    }

    /*
     * Deleting an answer
     */
    public void deleteAnswer(long answer_id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();
        db.delete(TABLE_ANSWER, KEY_ID + " = ?",
                new String[] { String.valueOf(answer_id) });
    }

    /*
     * Deleting aLL answers
     */
    public void deleteAllAnswers() {
        SQLiteDatabase db = DatabaseHelper.getInstance(this.mContext).getWritableDatabase();

        // now all answers
        db.delete(TABLE_ANSWER, null, null);
    }
}
