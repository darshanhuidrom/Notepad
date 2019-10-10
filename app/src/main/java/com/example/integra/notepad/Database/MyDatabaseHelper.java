package com.example.integra.notepad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.integra.notepad.model.User;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = MyDatabaseHelper.class.getSimpleName() + ">>>>>>>>";
    private static final String DATA_BASE_NAME = "my_table.db";
    private static final String USER_TABLE = "user_table";
    private static final int VERSION_NO = 1;
    private static final String ID = "_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String ADDRESS = "address";
    private static final String EMAIL_ID = "email_id";
    private static final String PHONE_NO = "phone_no";

    private static final String CREATE_USER_TABLE = " CREATE TABLE " + USER_TABLE + " (" + ID + " INTEGER PRIMARY KEY, " +
            "" + FIRST_NAME + " TEXT, " + LAST_NAME + " TEXT, " + ADDRESS + " TEXT, " + EMAIL_ID + " TEXT," + PHONE_NO + " TEXT )";

    public MyDatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION_NO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    /**
     * To add the user
     */
    public void addUser(User user) {
        if (userNameIsAlreadyHere(user)) {
            Log.d(TAG, "It already has a same user");
            return;
        }
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, user.getFirstName());
        values.put(LAST_NAME, user.getLastName());
        values.put(ADDRESS, user.getAddress());
        values.put(EMAIL_ID, user.getEmailId());
        values.put(PHONE_NO, user.getPhoneNo());
        Log.d(TAG, getDatabase().insert(USER_TABLE, null, values) + " rows inserted.");
        closeDb();
    }

    private void closeDb() {
        if (getDatabase().isOpen()) {
            getDatabase().close();
        }
    }

    private SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }


    private boolean userNameIsAlreadyHere(User user) {
        String[] args = {user.getFirstName().toLowerCase(), user.getLastName().toLowerCase()};
        Cursor cursor = getDatabase().rawQuery("SELECT  " + FIRST_NAME + ", " + LAST_NAME + " FROM " + USER_TABLE + " WHERE LOWER(" + FIRST_NAME + ") = ?" +
                "and LOWER(" + LAST_NAME + ") = ?", args);
        return (cursor.getCount() > 0 ? true : false);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM " + USER_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                users.add(user);

            } while (cursor.moveToNext());

            closeDb();
        }
        return users;
    }

    public User getUserById(int id) {

        String[] arg = {Integer.toString(id)};
        User user = null;
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + ID + "=?", arg);
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
        }
        return user;
    }

    public List<User> getUserByName(String name) {
        List<User> users = new ArrayList<>();
        String[] arg = {name.toLowerCase(), name.toLowerCase()};
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM "
                + USER_TABLE + " WHERE LOWER(" + FIRST_NAME + ")=? OR LOWER(" + LAST_NAME + ")=?", arg);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                users.add(user);
            } while (cursor.moveToNext());
        }
        return users;
    }

    public boolean deleteById(int id) {
        boolean status = false;
        int no = 0;
        no = getWritableDatabase().delete(USER_TABLE, ID + "=?", new String[]{Integer.toString(id)});
        if (no > 0)
            status = true;
        return status;
    }



}
