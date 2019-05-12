package gala.com.kidstv.GEYoutubeEvents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import gala.com.kidstv.GEConstants;

/**
 * Created by deepak on 01/10/17.
 */

public class GEReminderDB extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GALADB";
    private static final String TABLE_REMINDERS = "gereminders";

    public GEReminderDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_REMINDERS = "CREATE TABLE " + TABLE_REMINDERS + "("
                + GEConstants.KEY_ID + " INTEGER PRIMARY KEY," + GEConstants.KEY_VIDEO_ID + " TEXT," + GEConstants.KEY_DATETIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_REMINDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void addReminder(String videoId, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GEConstants.KEY_VIDEO_ID, videoId); // Contact Phone
        values.put(GEConstants.KEY_DATETIME, dateTime);

        // Inserting Row
        db.insert(TABLE_REMINDERS, null, values);
        db.close(); // Closing database connection
    }

    public HashMap<String, HashMap<String, String>> getAllReminders() {
        HashMap<String, HashMap<String, String>> lReminderList = new HashMap<String, HashMap<String, String>>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String lVideoId = cursor.getString(1);
                String lDateTime = cursor.getString(2);
                HashMap<String, String> lVideoInfo = new HashMap<String, String>();
                lVideoInfo.put(GEConstants.KEY_VIDEO_ID, lVideoId);
                lVideoInfo.put(GEConstants.KEY_DATETIME, lDateTime);
                lReminderList.put(lVideoId, lVideoInfo);
            } while (cursor.moveToNext());
        }

        // return contact list
        return lReminderList;
    }

    void deleteReminder(String videoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, GEConstants.KEY_VIDEO_ID + " = ?",
                new String[] { videoId });
        db.close();
    }
}
