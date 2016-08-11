package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.menachi.class3demo.Model.LastUpdates;


public class LastUpdatesSQL {
    final static String LAST_UPDATE_TABLE = "LastUpdates";
    final static String LAST_UPDATE_TABLE_NAME = "table_name";
    final static String LAST_UPDATE_TABLE_DATE = "last_update_date";
    final static String NUM_OF_RECORDS_IN_TABLE = "num_of_records";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " +
                LAST_UPDATE_TABLE + " (" +
                LAST_UPDATE_TABLE_NAME + " TEXT PRIMARY KEY," +
                LAST_UPDATE_TABLE_DATE + " TEXT," +
                NUM_OF_RECORDS_IN_TABLE      + " INTEGER" +
                ");" );
    }
    public static LastUpdates getLastUpdate(SQLiteDatabase db, String tableName) {
        String[] args = {tableName};
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, LAST_UPDATE_TABLE_NAME + " = ?",args , null, null, null);

        if (cursor.moveToFirst()) {
            int tabelNameIndex = cursor.getColumnIndex(LAST_UPDATE_TABLE_NAME);
            int lastUpdateIndex = cursor.getColumnIndex(LAST_UPDATE_TABLE_DATE);
            int numOfRecordsIndex = cursor.getColumnIndex(NUM_OF_RECORDS_IN_TABLE);
            //get the product fields by index
            String tabelName = cursor.getString(tabelNameIndex);
            String lastUpdate = cursor.getString(lastUpdateIndex);
            Integer numOfRecords = Integer.valueOf(cursor.getString(numOfRecordsIndex));
            LastUpdates lastUpdatesToReturn = new LastUpdates(tabelName,numOfRecords,lastUpdate);
            return lastUpdatesToReturn;
        }
        return null;
    }
    public static void setLastUpdate(SQLiteDatabase db,LastUpdates lastUpdates) {
        ContentValues values = new ContentValues();
        values.put(LAST_UPDATE_TABLE_NAME, lastUpdates.getTabelName());
        values.put(LAST_UPDATE_TABLE_DATE, lastUpdates.getLastUpdate());
        values.put(NUM_OF_RECORDS_IN_TABLE, lastUpdates.getCountOfRecords());

        db.insertWithOnConflict(LAST_UPDATE_TABLE,LAST_UPDATE_TABLE_NAME,values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    public static void setLastUpdateForSpecificRecord(SQLiteDatabase db,String  tabelName,String date) {
        String[] args = {tabelName};
        LastUpdates updateLastUpdates ;
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, LAST_UPDATE_TABLE_NAME + " = ?",args , null, null, null);
        if (cursor.moveToFirst()) {
            int tabelNameIndex = cursor.getColumnIndex(LAST_UPDATE_TABLE_NAME);
            int lastUpdateIndex = cursor.getColumnIndex(LAST_UPDATE_TABLE_DATE);
            int numOfRecordsIndex = cursor.getColumnIndex(NUM_OF_RECORDS_IN_TABLE);
            //get the product fields by index
            tabelName = cursor.getString(tabelNameIndex);
            String lastUpdate = cursor.getString(lastUpdateIndex);
            Integer numOfRecords = Integer.valueOf(cursor.getString(numOfRecordsIndex));
            updateLastUpdates = new LastUpdates(tabelName,numOfRecords,lastUpdate);
            updateLastUpdates.setLastUpdate(date);
            updateLastUpdates.setCountOfRecords(numOfRecords+1);
            ContentValues values = new ContentValues();
            values.put(LAST_UPDATE_TABLE_NAME, updateLastUpdates.getTabelName());
            values.put(LAST_UPDATE_TABLE_DATE, updateLastUpdates.getLastUpdate());
            values.put(NUM_OF_RECORDS_IN_TABLE, updateLastUpdates.getCountOfRecords());

            db.insertWithOnConflict(LAST_UPDATE_TABLE, LAST_UPDATE_TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
        }else{
            Log.e("TAG","setLastUpdateForSpecificRecord - ERROR");
        }
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + LAST_UPDATE_TABLE + ";");
    }
}
