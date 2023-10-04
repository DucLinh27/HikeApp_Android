package com.example.hikeapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hikeapplication.Hike.Hike;

import java.util.ArrayList;
import java.util.List;

public class ConnectDb extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "manager.db";

    private static final String TABLE_HIKES = "hikes";


    public ConnectDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String hikes_table = "CREATE TABLE " + TABLE_HIKES + "(hike_id INTEGER primary key autoincrement, name TEXT, location TEXT, date TEXT, parking TEXT, length INTEGER, level TEXT,description TEXT);";
        sqLiteDatabase.execSQL(hikes_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(sqLiteDatabase);
    }

    public void addHike(String name, String location, String date, String parking, String length, String level, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put("name", name);
        value.put("location", location);
        value.put("date", date);
        value.put("parking", parking);
        value.put("length", length);
        value.put("level", level);
        value.put("description", description);

        long result = db.insert(TABLE_HIKES, null, value);
        if (result == -1) {
            Toast.makeText(context, "Adding new hike failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Adding new hike successful", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("Recycle")
    public List<Hike> getHike() {
        List<Hike> list = new ArrayList<>();
        String queryDB = "SELECT hike_id, name, location, date, parking, length, level, description FROM " + TABLE_HIKES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(queryDB, null);

            while ((cursor != null) && (cursor.moveToNext())) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String location = cursor.getString(2);
                String date = cursor.getString(3);
                String parking = cursor.getString(4);
                String length = cursor.getString(5);
                String level = cursor.getString(6);
                String description = cursor.getString(7);

                list.add(new Hike(id, name, location, date, parking, length, level, description));
            }
        }
        return list;
    }

    public void editHike(String row_id, String name, String location, String date, String parking, String length, String level, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put("name", name);
        value.put("location", location);
        value.put("date", date);
        value.put("parking", parking);
        value.put("length", length);
        value.put("level", level);
        value.put("description", description);

        long result = db.update(TABLE_HIKES, value, "hike_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Edit hike to failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Edit hike successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteHike(String HikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultHike = db.delete(TABLE_HIKES, "hike_id=?", new String[]{HikeId});

        if (resultHike == -1 ) {
            Toast.makeText(context, "Delete hike to failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete hike successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllHike() {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultHike = db.delete(TABLE_HIKES, null, null);

        if (resultHike == -1 ) {
            Toast.makeText(context, "Delete all hike to failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete all hike successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}

