package com.menachi.class3demo.Model.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.LastPurchase;
import com.menachi.class3demo.Model.LastUpdates;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;
import com.menachi.class3demo.MyApplication;

import java.util.List;

/**
 * Created by refael yehuda on 7/24/2016.
 */
public class ModelSQL {

    MyDBHelper dbHelper;
    private final static int VERSION =19;

    public ModelSQL() {dbHelper = new MyDBHelper(MyApplication.getContext());}
    public LastUpdates getLastUpdate(String table) {return LastUpdatesSQL.getLastUpdate(dbHelper.getWritableDatabase(), table);}
    public void addProduct(Product product) {ProductsSQL.addProduct(dbHelper.getWritableDatabase(),product);}
    public List<Product> getAllProducts() {return ProductsSQL.getAllProducts(dbHelper.getWritableDatabase());}
    public void updateUserByID(User updatedUser){
        UserSQL.updateUserByID(dbHelper.getReadableDatabase(),updatedUser);
    }
    public void addUser(User user) {
        UserSQL.addUser(dbHelper.getWritableDatabase(), user);
    }
    public void setLastUpdate(LastUpdates lastUpdate) {LastUpdatesSQL.setLastUpdate(dbHelper.getWritableDatabase(), lastUpdate);}
    public void setLastUpdateForSpecificRecord(String  tabelName,String date){LastUpdatesSQL.setLastUpdateForSpecificRecord(dbHelper.getWritableDatabase(),tabelName,date);}
    public List<LastPurchase>  getLastPurchasesByUserId(String uesrId){return LastPurchasesSQL.getAllLastPurchases(dbHelper.getWritableDatabase(),uesrId);}
    public void addLastPurchase(LastPurchase lastPurchase){LastPurchasesSQL.addLastPurchase(dbHelper.getWritableDatabase(),lastPurchase);}
    public List<Comment> getCommentsByProductId(String productId){return CommentSQL.getCommentByProductId(dbHelper.getWritableDatabase(),productId);}
    public void addComment(Comment comment){CommentSQL.addComment(dbHelper.getWritableDatabase(),comment);}
    class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {
            super(context, "application_DB.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UserSQL.create(db);
            ProductsSQL.create(db);
            CommentSQL.create(db);
            LastPurchasesSQL.create(db);
            LastUpdatesSQL.create(db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{UserSQL.drop(db);}catch (Exception e){e.printStackTrace();}
            try{LastUpdatesSQL.drop(db);}catch (Exception e){e.printStackTrace();}
            try{ProductsSQL.drop(db);}catch (Exception e){e.printStackTrace();}
            try{CommentSQL.drop(db);}catch (Exception e){e.printStackTrace();}
            try{LastPurchasesSQL.drop(db);}catch (Exception e){e.printStackTrace();}
            onCreate(db);
        }



    }
}
