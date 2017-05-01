package project.sudden.bookinglapang.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Lotus on 01/05/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "lapanganList";

    // Contacts table name
    private static final String TABLE_CONTACTS = "lapangan";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "jadwal";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_STATUS + " TEXT," + KEY_DATE+ " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    public void addLapangan(Lapangan lapangan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, lapangan.getNamaLapangan());
        values.put(KEY_STATUS, lapangan.getPilihanLapangan());
        values.put(KEY_DATE, lapangan.getDate());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public ArrayList<Lapangan> getAllSite() {
        ArrayList<Lapangan> contactList = new ArrayList<Lapangan>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY " + KEY_ID + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lapangan lapangan = new Lapangan();
                lapangan.setNamaLapangan(cursor.getString(1));
                lapangan.setPilihanLapangan(cursor.getString(2));
                lapangan.setDate(cursor.getString(3));
                // Adding contact to list
                contactList.add(lapangan);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
}

