package anjali.learning.practical14;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 2; // Increment version if you modify the schema

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE quiz_attempts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER, " +
                "attempted_count INTEGER, " +
                "date TEXT, " +   // Optional, add date if needed
                "user_id INTEGER);"; // Optional, add user_id if needed
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS quiz_attempts");
            onCreate(db);
        }
    }

    // Method to insert score and attempted count
    public void insertScore(int score, int attemptedCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("attempted_count", attemptedCount);
        values.put("date", "2024-11-23"); // Example for storing date
        db.insert("quiz_attempts", null, values);
        db.close();
    }

    // Method to get all attempts from the database
    public List<QuizAttempt> getAllAttempts() {
        List<QuizAttempt> attempts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM quiz_attempts", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int score = cursor.getInt(cursor.getColumnIndex("score"));
                    int attemptedCount = cursor.getInt(cursor.getColumnIndex("attempted_count"));
                    attempts.add(new QuizAttempt(score, attemptedCount));
                }
            }
        } catch (Exception e) {
            Log.e("QuizDatabaseHelper", "Error fetching attempts", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return attempts;
    }

    // Method to get highest score
    public int getHighestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT MAX(score) FROM quiz_attempts", null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("QuizDatabaseHelper", "Error fetching highest score", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }

    // Method to get attempted count
    public int getAttemptedCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM quiz_attempts", null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("QuizDatabaseHelper", "Error fetching attempted count", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
}
