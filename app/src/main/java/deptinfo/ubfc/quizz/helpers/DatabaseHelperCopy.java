package deptinfo.ubfc.quizz.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperCopy extends SQLiteOpenHelper{

    private static DatabaseHelperCopy sInstance = null;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quizs";

    // Table Names
    private static final String TABLE_QUESTION = "questions";
    private static final String TABLE_ANSWER = "answers";
    private static final String TABLE_QUIZ = "quizs";

    public static DatabaseHelperCopy getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHelperCopy(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelperCopy(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(QuizTable.CREATE_TABLE_QUIZ);
        db.execSQL(QuestionTable.CREATE_TABLE_QUESTION);
        db.execSQL(AnswerTable.CREATE_TABLE_ANSWER);

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

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
