package com.example.lotus.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoManager";
    private static final String TABLE_TODO = "todos";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(String todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, todo);

        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    // code to get the single contact
    String getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO, new String[] { KEY_ID,
                        KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        /*Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));*/
        // return contact
        return cursor.getString(1);
    }

    // code to get all contacts in a list view
    public List<String> getAllContacts() {
        List<String> contactList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                /*String contact = new String();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));*/
                // Adding contact to list
                contactList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Deleting single contact
    public void deleteContact(String todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(todo) });
        db.close();
    }

}