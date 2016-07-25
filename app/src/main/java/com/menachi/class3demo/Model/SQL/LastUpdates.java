package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by refael yehuda on 7/24/2016.
 */
public class LastUpdates {
    final static String LAST_UPDATE_TABLE = "last_update";
    final static String LAST_UPDATE_TABLE_NAME = "table_name";
    final static String LAST_UPDATE_TABLE_DATE = "last_update_date";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + LAST_UPDATE_TABLE + " (" + LAST_UPDATE_TABLE_NAME + " TEXT PRIMARY KEY," +
                LAST_UPDATE_TABLE_DATE + " TEXT);" );
    }
    public static String getLastUpdate(SQLiteDatabase db, String tableName) {
        String[] args = {tableName};
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, LAST_UPDATE_TABLE_NAME + " = ?",args , null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(LAST_UPDATE_TABLE_DATE));
        }
        return null;
    }
    public static void setLastUpdate(SQLiteDatabase db, String table, String date) {
        ContentValues values = new ContentValues();
        values.put(LAST_UPDATE_TABLE_NAME, table);
        values.put(LAST_UPDATE_TABLE_DATE, date);

        db.insertWithOnConflict(LAST_UPDATE_TABLE,LAST_UPDATE_TABLE_NAME,values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + LAST_UPDATE_TABLE + ";");
    }
}
