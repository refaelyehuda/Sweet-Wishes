package com.menachi.class3demo.Model.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.menachi.class3demo.Model.User;
import com.menachi.class3demo.MyApplication;

/**
 * Created by refael yehuda on 7/24/2016.
 */
public class ModelSQL {

    MyDBHelper dbHelper;
    private final static int VERSION =1;

    public ModelSQL() {dbHelper = new MyDBHelper(MyApplication.getContext());}

    public void updateUserByID(User updatedUser){
        UserSQL.updateUserByID(dbHelper.getReadableDatabase(),updatedUser);
    }
    public void addUser(User user) {
        UserSQL.addUser(dbHelper.getWritableDatabase(), user);
    }

    class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {
            super(context, "application_DB.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UserSQL.create(db);
            LastUpdates.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                UserSQL.drop(db);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                LastUpdates.drop(db);
            }catch (
                    Exception e){e.printStackTrace();
            }
            onCreate(db);
        }

    }
}
