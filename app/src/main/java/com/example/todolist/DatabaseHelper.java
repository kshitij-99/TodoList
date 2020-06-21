package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "ToDo_Table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Date";
    private static final String COL4 = "Time";
    private static final String COL5 = "Che";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT, "
                + COL3 + " DATE, "
                + COL4 + " TIME, "
                + COL5 + " TEXT" + ");";
        Log.d(TAG, "Creating table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert data into database
    public boolean insertData(String item, String date, String time, String check) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, date);
        contentValues.put(COL4, time);
        contentValues.put(COL5, check);
        Log.d(TAG, "insertData: Inserting " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    //Delete data from database
    public void deleteData(String name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                    COL2 + "= '" + name + "'" +
                    " AND " + COL3 + "= '" + date + "'" +
                    " AND " + COL4 + "= '" + time + "'";

            db.execSQL(query);
            Log.d(TAG, "deleteItem: " + query);
            Log.d(TAG, "deleteData: Deleted " + name + " from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    public void update(String name, String date, String time, String check)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "UPDATE "+TABLE_NAME +" SET " + COL5+ " = '"+check+"' WHERE " +
                    COL2 + "= '" + name + "'" +
                    " AND " + COL3 + "= '" + date + "'" +
                    " AND " + COL4 + "= '" + time + "'";

            db.execSQL(query);
            Log.d(TAG, "update: " + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    //Fetch all data from database
    public ArrayList<DataModel> getAllData() {
        ArrayList<DataModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String check = cursor.getString(4);
            DataModel dataModel = new DataModel(title, date, time,check);
            arrayList.add(dataModel);
        }
        db.close();
        return arrayList;
    }

}
