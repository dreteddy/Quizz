package deptinfo.ubfc.quizz.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.common.DbBitmapUtility;
import deptinfo.ubfc.quizz.models.Answer;
import deptinfo.ubfc.quizz.models.Question;
import deptinfo.ubfc.quizz.models.Quiz;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper sInstance = null;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quizs";

    // Table Names
    private static final String TABLE_QUESTION = "questions";
    private static final String TABLE_ANSWER = "answers";
    private static final String TABLE_QUIZ = "quizs";
    private static final String TABLE_RESULT = "results";

    //Common column name
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";

    //Quiz column names
    private static final String KEY_QUIZ_TYPE = "quiz_type";

    //Question column names
    private static final String KEY_QUESTION = "question";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_QUIZ = "quiz";

    //Answer column names
    private static final String KEY_ANSWER = "answer";

    //Result column names
    private static final String KEY_RESULT = "result";

    // Quiz table create statement
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE "
            + TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUIZ_TYPE + " TEXT,"
            + KEY_IMAGE + " BLOB DEFAULT NULL)";

    // Question table create statement
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUESTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUESTION + " TEXT,"
            + KEY_CORRECT_ANSWER + " INTEGER,"
            + KEY_QUIZ + " INTEGER ,"
            + "FOREIGN KEY ("+ KEY_QUIZ +") REFERENCES "+ TABLE_QUESTION +"("+ KEY_ID +"));";

    // Answer table create statement
    private static final String CREATE_TABLE_ANSWER = "CREATE TABLE "
            + TABLE_ANSWER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ANSWER + " TEXT ,"
            + KEY_QUESTION + " INTEGER ,"
            + "FOREIGN KEY ("+ KEY_QUESTION +") REFERENCES "+ TABLE_QUESTION +"("+ KEY_ID +"));";

    // Result table create statement
    private static final String CREATE_TABLE_RESULT = "CREATE TABLE "
            + TABLE_RESULT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUIZ + " INTEGER ,"
            + KEY_RESULT + " INTEGER)";

    public static DatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //////////////////////////////////////////////////////////////
    ///////////////////////////QUIZ///////////////////////////////
    //////////////////////////////////////////////////////////////
    /*
     * Creating Quiz
     */
    public long createQuiz(Quiz quiz) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_TYPE, quiz.getQuizType());


        return db.insert(TABLE_QUIZ, null, values);
    }

    /*
     * getting id for a quiz
     * */
    public int getIdForQuiz(String quiz) {
        quiz = DatabaseUtils.sqlEscapeString(quiz);
        int result = -1;
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_QUIZ +
        " WHERE " + TABLE_QUIZ + "." + KEY_QUIZ_TYPE +
                " = " + quiz ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_ID));
            } while (c.moveToNext());
        }

        c.close();
        return result;
    }

    /*
     * getting image for a quiz
     * */
    public byte[] getImgForQuiz(String quiz) {
        quiz = DatabaseUtils.sqlEscapeString(quiz);
        byte[] result = null;
        String selectQuery = "SELECT "+KEY_IMAGE+" FROM " + TABLE_QUIZ +
                " WHERE " + TABLE_QUIZ + "." + KEY_QUIZ_TYPE +
                " = " + quiz ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                result = c.getBlob(1);
            } while (c.moveToNext());
        }

        c.close();
        return result;
    }

    /*
        Check if quiz already exist
     */
    public boolean checkQuizAlreadyExist(String quiz)
    {
        quiz = DatabaseUtils.sqlEscapeString(quiz);
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_QUIZ +
                " WHERE " + TABLE_QUIZ + "." + KEY_QUIZ_TYPE +
                " = " + quiz ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    /*
        Update quiz type
     */
    public void updateQuizType(long id , String newQuizType){

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_TYPE, newQuizType);

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_QUIZ, values,KEY_ID + " = " + id, null);
    }
    /*
        Update quiz image
     */
    public void updateQuizImg(String quiz, byte[] image){
        quiz = DatabaseUtils.sqlEscapeString(quiz);
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, image);

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_QUIZ, values,KEY_QUIZ + " = " + quiz, null);

    }

    /*
     * getting all quizs
     * */
    public List<Quiz> getAllQuizs() {
        List<Quiz> quizs = new ArrayList<Quiz>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Quiz quiz = new Quiz();
                quiz.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                quiz.setQuizType(c.getString(c.getColumnIndex(KEY_QUIZ_TYPE)));

                // adding to answer list
                quizs.add(quiz);
            } while (c.moveToNext());
        }
        c.close();
        return quizs;
    }

    /**
     * Deleting a quiz
     */
    public void deleteQuiz(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all quiz
     */
    public void deleteAllQuizs() {
        SQLiteDatabase db = this.getWritableDatabase();
        // now all quizs
        db.delete(TABLE_QUIZ, null, null);
    }

    /**
     * getting all Questions in a QUIZ
     * */
    public String getQuizForQuestion(String question) {
        String quizType = "";
        question = DatabaseUtils.sqlEscapeString(question);
        String selectQuery = "SELECT * FROM " + TABLE_QUIZ
                + " WHERE "+ TABLE_QUIZ + "." + KEY_ID
                + " = (SELECT " + TABLE_QUESTION + "." + KEY_QUIZ +
                " FROM " + TABLE_QUESTION +
                " WHERE " + TABLE_QUESTION + "." + KEY_QUESTION +
                " = " + question + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                quizType = c.getString(c.getColumnIndex(KEY_QUIZ_TYPE));
            } while (c.moveToNext());
        }
        return quizType;
    }

    /*
        Delete Quiz
     */
    public void deleteQuiz(String quizType){

        ArrayList<Question> questions = (ArrayList<Question>)getAllQuestionsFromQuiz(quizType);
        quizType = DatabaseUtils.sqlEscapeString(quizType);

        //Delete all Questions and answers for quiz
        for( Question question : questions)
            deleteQuestion(question.getQuestion());

        //delete question
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = KEY_QUIZ_TYPE + " = " + quizType;
        db.delete(TABLE_QUIZ ,whereClause, null);
    }

    //////////////////////////////////////////////////////////////
    ///////////////////////////QUESTION///////////////////////////
    //////////////////////////////////////////////////////////////

    public long createQuestion(Question question, Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion());
        values.put(KEY_CORRECT_ANSWER, Integer.toString(question.getCorrect_answer()));
        values.put(KEY_QUIZ, quiz.getId());

        return db.insert(TABLE_QUESTION, null, values);
    }

    /*
     * getting id for a question
     * */
    public long getIdForQuestion(String question, String quiz) {
        question = DatabaseUtils.sqlEscapeString(question);
        long quiz_id = getIdForQuiz(quiz);
        long result = -1;
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_QUESTION +
                " WHERE " + KEY_QUESTION + " = " + question +
                " AND " + KEY_QUIZ + " = " + quiz_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_ID));
            } while (c.moveToNext());
        }

        c.close();
        return result;
    }

    /*
        Check if question already exist
     */
    public boolean checkQuestionAlreadyExist(String question, String quiz)
    {
        question = DatabaseUtils.sqlEscapeString(question);
        quiz = DatabaseUtils.sqlEscapeString(quiz);
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_QUESTION +
                " WHERE " + KEY_QUESTION + " = " + question +
                " AND " + KEY_QUIZ + " = " + quiz;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }

    }

    /**
     * getting all Questions in a QUIZ
     * */
    public List<Question> getAllQuestionsFromQuiz(String quizType) {
        List<Question> questions = new ArrayList<Question>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION
                + " WHERE "+ TABLE_QUESTION + "." + KEY_QUIZ
                + " = (SELECT " + TABLE_QUIZ + "." + KEY_ID +
                " FROM " + TABLE_QUIZ +
                " WHERE " + TABLE_QUIZ + "." + KEY_QUIZ_TYPE +
                " = " + "'" + quizType + "')";

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
                questions.add(q);
            } while (c.moveToNext());
        }
        return questions;
    }

    /**
     * getting all Questions in a QUIZ
     * */
    public int getCorrectAnswer(String question) {
        List<Question> questions = new ArrayList<Question>();
        question = DatabaseUtils.sqlEscapeString(question);
        int correctAnswer = -1;
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION
                + " WHERE "+ TABLE_QUESTION + "." + KEY_QUESTION + " = " +question;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                correctAnswer = c.getInt(c.getColumnIndex(KEY_CORRECT_ANSWER));
            } while (c.moveToNext());
        }

        if(correctAnswer < 1){
            Log.i("dbHelper" , "Correct answer not correct");
        }
        return correctAnswer;
    }

    /*

     */
    public void updateCorrectAnswerForQuestion(String question, int correctAnswer){
        question = DatabaseUtils.sqlEscapeString(question);

        ContentValues values = new ContentValues();
        values.put(KEY_CORRECT_ANSWER, correctAnswer);

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_QUESTION, values,KEY_QUESTION + " = " + question, null);

    }

    /*
        Update question text
     */
    public void updateQuestion(Long id ,String newQuestion, long quizId){

        Log.d("TestEditQuestion DBH" , "id : " + id + " : " + newQuestion + ": " + quizId);
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, newQuestion);
        String whereClause = KEY_ID + " = " + id + " AND " + KEY_QUIZ + " = " + quizId;

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_QUESTION, values,whereClause, null);

    }

    /**
     * getting number Questions in a QUIZ
     * */
    public int getNbQuestionsFromQuiz(String quizType) {
        return getAllQuestionsFromQuiz(quizType).size();
    }

    /*
     * Deleting aLL questions
     */
    public void deleteAllQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, null, null);
    }

    /*
        Delete question
     */
    public void deleteQuestion(String question){
        String quiz = getQuizForQuestion(question);
        question = DatabaseUtils.sqlEscapeString(question);

        long questionId = getIdForQuestion(question ,  quiz);

        //Delete all answers for question
        String whereClause = KEY_QUESTION + " = " + questionId;
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ANSWER ,whereClause, null);

        //delete question
        whereClause = KEY_ID + " = " + questionId;
        db.delete(TABLE_QUESTION ,whereClause, null);
    }

    //////////////////////////////////////////////////////////////
    ///////////////////////////ANSWER/////////////////////////////
    //////////////////////////////////////////////////////////////
    public long createAnswer(Answer answer, Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, answer.getAnswer());
        values.put(KEY_QUESTION, question.getId());

        return db.insert(TABLE_ANSWER, null, values);
    }

    /*
     * getting id for a answer
     * */
    public long getIdForAnswer(String answer, String question) {
        String quiz = getQuizForQuestion(question);
        long question_id = getIdForQuestion(question, quiz);
        answer = DatabaseUtils.sqlEscapeString(answer);


        long result = -1;
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_ANSWER +
                " WHERE " + KEY_ANSWER + " = " + answer +
                " AND " + KEY_QUESTION + " = " + question_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_ID));
            } while (c.moveToNext());
        }

        return result;
    }

    /*
        Check if answer already exist
     */
    public boolean checkAnswerAlreadyExist(String answer, String question)
    {
        String quiz = getQuizForQuestion(question);
        long question_id = getIdForQuestion(question, quiz);

        answer = DatabaseUtils.sqlEscapeString(answer);
        question = DatabaseUtils.sqlEscapeString(question);

        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_ANSWER +
                " WHERE " + KEY_ANSWER + " = " + answer +
                " AND " + KEY_QUESTION + " = " + question_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }

    /*
        Update question text
     */
    public void updateAnswer(Long id ,String answer){

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, answer);
        String whereClause = KEY_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_ANSWER, values,whereClause, null);

    }

    /*
        Delete question text
     */
    public void deleteAnswer(String answer, String question){

        answer = DatabaseUtils.sqlEscapeString(answer);
        String quiz = getQuizForQuestion(question);
        Long questionId = getIdForQuestion(question , quiz);
        String whereClause = KEY_ANSWER + " = " + answer + " AND " + KEY_QUESTION + " = " + questionId;

        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ANSWER ,whereClause, null);

    }

    /**
     * getting all Answers in a Question
     * */
    public List<Answer> getAllAnswersFromQuestion(String question) {
        List<Answer> answers = new ArrayList<Answer>();

        question = DatabaseUtils.sqlEscapeString(question);
        String selectQuery = "SELECT * FROM " + TABLE_ANSWER
                + " WHERE "+ TABLE_ANSWER + "." + KEY_QUESTION
                + " = (SELECT " + TABLE_QUESTION + "." + KEY_ID +
                " FROM " + TABLE_QUESTION +
                " WHERE " + TABLE_QUESTION + "." + KEY_QUESTION +
                " = " + question + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Answer ans = new Answer();
                ans.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                ans.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));

                // adding to tags list
                answers.add(ans);
            } while (c.moveToNext());
        }
        return answers;
    }

    /**
     * getting number Questions in a QUIZ
     * */
    public int getNbAnswersFromQuestion(String question) {
        return getAllAnswersFromQuestion(question).size();
    }

    /*
     * Deleting aLL questions
     */
    public void deleteAllAnswers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANSWER, null, null);
    }

    //////////////////////////////////////////////////////////////
    ///////////////////////RESULT/////////////////////////////////
    //////////////////////////////////////////////////////////////
    /*
     * Creating Result
     */
    public long createResult(String quiz, int result) {
        SQLiteDatabase db = getWritableDatabase();
        int quiz_id = getIdForQuiz(quiz);

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ, quiz_id);
        values.put(KEY_RESULT, result);

        return db.insert(TABLE_RESULT, null, values);
    }


    /*
     * Get Highest Result for quiz
     */
    public int getHighestResult(String quiz) {
        int result = -1;
        int quiz_id = getIdForQuiz(quiz);
        String selectQuery = "SELECT MAX(" +KEY_RESULT+") FROM " + TABLE_RESULT +
                " WHERE " + KEY_QUIZ + " = " + quiz_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.i("TEST DBHELPER" , selectQuery);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex("MAX(" + KEY_RESULT + ")"));
            } while (c.moveToNext());
        }

        Log.i("TEST DBHELPER" , selectQuery);

        if(result < 0)
            Log.e("dbHelper error" , "getHighestResult is negative");

        return result;
    }

    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_ANSWER);
        db.execSQL(CREATE_TABLE_RESULT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);

        // create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
