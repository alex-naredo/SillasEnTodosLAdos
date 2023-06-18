package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "booking.db";
    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_BOOKING = "booking";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_ACCESS = "access";
    private static final String COLUMN_SUNFLOWER_CARD = "sunflower_card";

    private static final String COLUMN_LOCATION = "location";

    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_BOOKING + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_ACCESS + " TEXT, " +
                COLUMN_SUNFLOWER_CARD + " INTEGER," +
                COLUMN_LOCATION + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
        onCreate(db);
    }

    public long addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, booking.getName());
        values.put(COLUMN_EMAIL, booking.getEmail());
        values.put(COLUMN_DATE, booking.getSelectedDate());
        values.put(COLUMN_TIME, booking.getSelectedTime());
        values.put(COLUMN_ACCESS, booking.getAccess());
        values.put(COLUMN_SUNFLOWER_CARD, booking.hasSunflowerCard() ? 1 : 0);
        values.put(COLUMN_LOCATION, booking.getLocation());
        db.insert(TABLE_BOOKING, null, values);
        db.close();
        return 0;
    }

    public Cursor getAllBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKING, null);
    }


    public Booking getLatestBooking() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKING,
                new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL,
                        COLUMN_DATE, COLUMN_TIME,
                        COLUMN_ACCESS, COLUMN_SUNFLOWER_CARD, COLUMN_LOCATION},
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC",
                "1");

        Booking booking = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            @SuppressLint("Range") String access = cursor.getString(cursor.getColumnIndex(COLUMN_ACCESS));
            @SuppressLint("Range") boolean sunflowerCard = cursor.getInt(cursor.getColumnIndex(COLUMN_SUNFLOWER_CARD)) == 1;
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
            booking = new Booking(name, email, date, time, access, sunflowerCard, location);
        }
        cursor.close();
        return booking;
    }
}
