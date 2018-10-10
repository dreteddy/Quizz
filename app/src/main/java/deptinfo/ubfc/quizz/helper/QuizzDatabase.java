package deptinfo.ubfc.quizz.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.model.Answer;
import deptinfo.ubfc.quizz.model.Question;
import deptinfo.ubfc.quizz.model.QuestionType;

public class QuizzDatabase extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = "QuizzDatabase";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quizzs";

    // Table Names
    private static final String TABLE_QUESTION = "questions";
    private static final String TABLE_ANSWER = "answers";
    private static final String TABLE_QUIZ = "quizs";

    // Common column names
    private static final String KEY_ID = "id";

    // QUESTIONS Table - column names
    private static final String KEY_QUESTION = "question";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";

    // ANSWERS Table - column names
    private static final String KEY_ANSWER = "answer";

    // QUIZ Table - column names
    private static final String KEY_ANSWER_ID = "answer_id";
    private static final String KEY_QUESTION_ID = "question_id";

    // Table Create Statements
    // Question table create statement
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUESTION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_QUESTION
            + " TEXT," + KEY_CORRECT_ANSWER + " INTEGER )";

    // Answer table create statement
    private static final String CREATE_TABLE_ANSWER = "CREATE TABLE " + TABLE_ANSWER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ANSWER + " TEXT )";

    // Quiz table create statement
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE "
            + TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUESTION_ID + " INTEGER," + KEY_ANSWER_ID + " INTEGER" + ")";


    public QuizzDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_ANSWER);
        db.execSQL(CREATE_TABLE_QUIZ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);

        // create new tables
        onCreate(db);
    }


    ////////////////////////////////////////////////////////////////////////
    /////////////////////ANSWER///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /*
     * Creating an answer
     */
    public long createAnswer(Answer answer, /*long[] questions_ids*/ long question_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, answer.getAnswer());

        // insert row
        long answer_id = db.insert(TABLE_ANSWER, null, values);

        // assigning question to answer
        //for (long question_id : questions_ids) {
            createQuiz(answer_id, question_id);
        //}

        return answer_id;
    }

    /*
     * get single answer
     */
    public Answer getAnswer(long answer_id) {
        SQLiteDatabase db = this.getReadableDatabase();

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
        SQLiteDatabase db = this.getReadableDatabase();
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

        SQLiteDatabase db = this.getReadableDatabase();
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

        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();

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
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANSWER, KEY_ID + " = ?",
                new String[] { String.valueOf(answer_id) });
    }

    /*
     * Deleting aLL answers
     */
    public void deleteAllAnswers() {
        SQLiteDatabase db = this.getWritableDatabase();

        // now all answers
        db.delete(TABLE_ANSWER, null, null);
    }


    ////////////////////////////////////////////////////////////////////////
    /////////////////////QUESTION///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /*
     * Creating tag
     */
    public long createQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion());
        values.put(KEY_CORRECT_ANSWER, question.getCorrect_answer());

        // insert row
        long quest_id = db.insert(TABLE_QUESTION, null, values);

        return quest_id;
    }

    /**
     * getting answer count
     */
    public int getQuestionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting all tags
     * */
    public List<Question> getAllQuestions() {
        List<Question> tags = new ArrayList<Question>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Question q = new Question();
                q.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                q.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));
                q.setCorrect_answer(c.getInt(c.getColumnIndex(KEY_CORRECT_ANSWER)));

                // adding to tags list
                tags.add(q);
            } while (c.moveToNext());
        }
        return tags;
    }

    /*
     * Updating a Question
     */
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

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
    public void deleteQuestion(Question question, boolean should_delete_all_question_answers) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting question
        // check if answers under this tag should also be deleted
        if (should_delete_all_question_answers) {
            // get all answers under this tag
            List<Answer> allQuestionAnswers = getAllAnswersByQuestion(question.getQuestion());

            // delete all answers
            for (Answer answer : allQuestionAnswers) {
                // delete answers
                deleteAnswer(answer.getId());
            }
        }

        // now delete the tag
        db.delete(TABLE_QUESTION, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getId()) });
    }

    /*
     * Deleting a question
     */
    public void deleteAllQuestions( boolean should_delete_all_question_answers) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting question
        // check if answers under this tag should also be deleted
        /*if (should_delete_all_question_answers) {
            // get all answers under this tag
            List<Answer> allQuestionAnswers = getAllAnswersByQuestion(question.getQuestion());

            // delete all answers
            for (Answer answer : allQuestionAnswers) {
                // delete answers
                deleteAnswer(answer.getId());
            }
        }*/

        // now delete the tag
        db.delete(TABLE_QUESTION, null, null);
    }


    ////////////////////////////////////////////////////////////////////////
    /////////////////////QUIZ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /*
     * Creating Quiz
     */
    public long createQuiz(long question_id, long answer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, question_id);
        values.put(KEY_ANSWER_ID, answer_id);

        long id = db.insert(TABLE_QUIZ, null, values);

        return id;
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting aLL quiz
     */
    public void deleteAllQuizs() {
        SQLiteDatabase db = this.getWritableDatabase();

        // now all quizs
        db.delete(TABLE_QUIZ, null, null);
    }

    ////////////////////////////////////////////////////////////////////////
    /////////////////////CLOSE DATABASE///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
