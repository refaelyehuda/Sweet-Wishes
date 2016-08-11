package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebase.client.annotations.Nullable;
import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.LastPurchase;
import com.menachi.class3demo.Model.Product;

import java.util.LinkedList;
import java.util.List;

public class LastPurchasesSQL {

    private static final String LAST_PURCHASES_TABLE = "LastPurchase";
    private static final String LAST_PURCHASE_ID = "LastPurchaseId";
    private static final String USER_ID = "userId";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_PRICE = "productPrice";
    private static final String PRODUCT_TYPE = "productType";
    private static final String PRODUCT_IMAGE_NAME = "productImageName";
    private static final String PRODUCT_CREATED_DATE = "productCreateDate";
    private static final String PRODUCT_LAST_UPDATE = "productLastUpdate";
    private static final String LAST_UPDATE = "lastUpdate";

    public static void create(SQLiteDatabase db) {
        Log.d("TAG", "Creating comments table");
        db.execSQL("create table " +
                LAST_PURCHASES_TABLE      + " (" +
                LAST_PURCHASE_ID + " TEXT," +
                USER_ID + " TEXT," +
                PRODUCT_ID + " TEXT," +
                PRODUCT_NAME + " TEXT," +
                PRODUCT_PRICE + " TEXT," +
                PRODUCT_TYPE      + " TEXT," +
                PRODUCT_IMAGE_NAME      + " TEXT," +
                PRODUCT_CREATED_DATE      + " TEXT," +
                PRODUCT_LAST_UPDATE      + " TEXT," +
                LAST_UPDATE      + " TEXT" +
                ");");
    }
    public static void addLastPurchase(SQLiteDatabase db, LastPurchase lastPurchase) {
        ContentValues values = new ContentValues();
        values.put(LAST_PURCHASE_ID, lastPurchase.getLastPurchaseID());
        values.put(USER_ID, lastPurchase.getUserID());
        values.put(PRODUCT_ID, lastPurchase.getProduct().getProductId());
        values.put(PRODUCT_NAME, lastPurchase.getProduct().getName());
        values.put(PRODUCT_PRICE, lastPurchase.getProduct().getPrice());
        values.put(PRODUCT_TYPE, lastPurchase.getProduct().getType());
        values.put(PRODUCT_IMAGE_NAME, lastPurchase.getProduct().getImageName());
        values.put(PRODUCT_CREATED_DATE, lastPurchase.getProduct().getCreateDate());
        values.put(PRODUCT_LAST_UPDATE, lastPurchase.getProduct().getLastUpdate());
        values.put(LAST_UPDATE, lastPurchase.getLastUpdate());
        db.insert(LAST_PURCHASES_TABLE, null, values);
    }

    public static void deleteLastPurchase(SQLiteDatabase db, LastPurchase lastPurchase) {
        db.delete(LAST_PURCHASES_TABLE, LAST_PURCHASE_ID + " = ?", new String[]{lastPurchase.getLastPurchaseID()});
    }

    public static void drop(SQLiteDatabase db)  {
        db.execSQL("drop table " + LAST_PURCHASES_TABLE);
    }

    public static List<LastPurchase> getAllLastPurchases(SQLiteDatabase db,String uesrId) {
        Cursor cursor = db.query(LAST_PURCHASES_TABLE, null, USER_ID + " = ?", new String[]{uesrId}, null, null, null);
        List<LastPurchase> list = new LinkedList<LastPurchase>();
        if (cursor.moveToFirst()) {
            int lastPurchaseIdIndex = cursor.getColumnIndex(LAST_PURCHASE_ID);
            int userIdIndex = cursor.getColumnIndex(USER_ID);
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int productNameIndex = cursor.getColumnIndex(PRODUCT_NAME);
            int productPriceIndex = cursor.getColumnIndex(PRODUCT_PRICE);
            int productTypeIndex = cursor.getColumnIndex(PRODUCT_TYPE);
            int productImageNameIndex = cursor.getColumnIndex(PRODUCT_IMAGE_NAME);
            int productCreatedDateIndex = cursor.getColumnIndex(PRODUCT_CREATED_DATE);
            int productLastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATE);
            int lastUpdateIndex = cursor.getColumnIndex(LAST_UPDATE);
            do {
                String lastPurchaseId = cursor.getString(lastPurchaseIdIndex);
                String userId = cursor.getString(userIdIndex);
                String productID = cursor.getString(productIdIndex);
                String productName = cursor.getString(productNameIndex);
                String productPrice = cursor.getString(productPriceIndex);
                String productType = cursor.getString(productTypeIndex);
                String productImageName = cursor.getString(productImageNameIndex);
                String productCreatedDate = cursor.getString(productCreatedDateIndex);
                String productLastUpdate = cursor.getString(productLastUpdateIndex);
                String lastUpdate = cursor.getString(lastUpdateIndex);
                Product product = new Product(productID,productName,productPrice,productType,productImageName,productCreatedDate,productLastUpdate);
                LastPurchase lastPurchase = new LastPurchase(lastPurchaseId,userId,product,lastUpdate);
                list.add(lastPurchase);
            }
            while (cursor.moveToNext());
        }
        return list;
    }


    @Nullable
    public static LastPurchase getLastPurchase(SQLiteDatabase db, String lastPurchaseId) {
        Cursor cursor = db.query(LAST_PURCHASES_TABLE, null, LAST_PURCHASE_ID + " = ?", new String[]{lastPurchaseId}, null, null, null);
        if (cursor.moveToFirst()) {
            int lastPurchaseIdIndex = cursor.getColumnIndex(LAST_PURCHASE_ID);
            int userIdIndex = cursor.getColumnIndex(USER_ID);
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int productNameIndex = cursor.getColumnIndex(PRODUCT_NAME);
            int productPriceIndex = cursor.getColumnIndex(PRODUCT_PRICE);
            int productTypeIndex = cursor.getColumnIndex(PRODUCT_TYPE);
            int productImageNameIndex = cursor.getColumnIndex(PRODUCT_IMAGE_NAME);
            int productCreatedDateIndex = cursor.getColumnIndex(PRODUCT_CREATED_DATE);
            int productLastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATE);
            int lastUpdateIndex = cursor.getColumnIndex(LAST_UPDATE);

            //get the lastPurchase fields by index
            String lastPurchaseID = cursor.getString(lastPurchaseIdIndex);
            String userId = cursor.getString(userIdIndex);
            String productID = cursor.getString(productIdIndex);
            String productName = cursor.getString(productNameIndex);
            String productPrice = cursor.getString(productPriceIndex);
            String productType = cursor.getString(productTypeIndex);
            String productImageName = cursor.getString(productImageNameIndex);
            String productCreatedDate = cursor.getString(productCreatedDateIndex);
            String productLastUpdate = cursor.getString(productLastUpdateIndex);
            String lastUpdate = cursor.getString(lastUpdateIndex);
            Product product = new Product(productID,productName,productPrice,productType,productImageName,productCreatedDate,productLastUpdate);
            LastPurchase lastPurchase = new LastPurchase(lastPurchaseId,userId,product,lastUpdate);
            return lastPurchase;
        }
        return null;
    }

    public static void updateLastPurchase(SQLiteDatabase db, LastPurchase updatedLastPurchase) {

        //build the variable of the inser query
        ContentValues values = new ContentValues();
        values.put(LAST_PURCHASE_ID, updatedLastPurchase.getLastPurchaseID());
        values.put(USER_ID, updatedLastPurchase.getUserID());
        values.put(PRODUCT_ID, updatedLastPurchase.getProduct().getProductId());
        values.put(PRODUCT_NAME, updatedLastPurchase.getProduct().getName());
        values.put(PRODUCT_PRICE, updatedLastPurchase.getProduct().getPrice());
        values.put(PRODUCT_TYPE, updatedLastPurchase.getProduct().getType());
        values.put(PRODUCT_IMAGE_NAME, updatedLastPurchase.getProduct().getImageName());
        values.put(PRODUCT_CREATED_DATE, updatedLastPurchase.getProduct().getCreateDate());
        values.put(PRODUCT_LAST_UPDATE, updatedLastPurchase.getProduct().getLastUpdate());
        values.put(LAST_UPDATE, updatedLastPurchase.getLastUpdate());
        //find the user according to comment id
        db.update(LAST_PURCHASES_TABLE,values, LAST_PURCHASE_ID + " = ?", new String[]{updatedLastPurchase.getLastPurchaseID()});
    }
}
