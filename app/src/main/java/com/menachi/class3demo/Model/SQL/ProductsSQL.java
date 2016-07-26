package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebase.client.annotations.Nullable;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by refael yehuda on 7/25/2016.
 */
public class ProductsSQL {

    private static final String PRODUCTS_TABLE = "Products";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";
    private static final String PRODUCT_TYPE = "type";
    private static final String PRODUCT_IMAGE_NAME = "imageName";
    private static final String PRODUCT_CREATED_DATE = "createDate";
    private static final String PRODUCT_LAST_UPDATE = "lastUpdate";

    public static void create(SQLiteDatabase db) {
        Log.d("TAG", "Creating products table");
        db.execSQL("create table " +
                PRODUCTS_TABLE + " (" +
                PRODUCT_ID + " TEXT," +
                PRODUCT_NAME + " TEXT," +
                PRODUCT_PRICE + " TEXT," +
                PRODUCT_TYPE + " TEXT," +
                PRODUCT_IMAGE_NAME + " TEXT," +
                PRODUCT_CREATED_DATE + " TEXT," +
                PRODUCT_LAST_UPDATE + " TEXT" +
                ");");
    }

    public static void addProduct(SQLiteDatabase db, Product product) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, product.getProductId());
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_TYPE, product.getType());
        values.put(PRODUCT_IMAGE_NAME, product.getImageName());
        values.put(PRODUCT_CREATED_DATE, product.getCreateDate());
        values.put(PRODUCT_LAST_UPDATE, product.getLastUpdate());
        db.insert(PRODUCTS_TABLE, null, values);
    }

    public static void deleteProduct(SQLiteDatabase db, Product product) {
        db.delete(PRODUCTS_TABLE, PRODUCT_ID + " = ?", new String[]{product.getProductId()});
    }

    public static void drop(SQLiteDatabase db)  {
        db.execSQL("drop table " + PRODUCTS_TABLE);
    }

    public static List<Product> getAllProducts(SQLiteDatabase db) {
        Cursor cursor = db.query(PRODUCTS_TABLE, null, null, null, null, null, null);
        List<Product> list = new LinkedList<Product>();
        if (cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int nameIndex = cursor.getColumnIndex(PRODUCT_NAME);
            int priceIndex = cursor.getColumnIndex(PRODUCT_PRICE);
            int typeIndex = cursor.getColumnIndex(PRODUCT_TYPE);
            int imageNameIndex = cursor.getColumnIndex(PRODUCT_IMAGE_NAME);
            int createDateIndex = cursor.getColumnIndex(PRODUCT_CREATED_DATE);
            int lastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATE);
            do {
                String productID = cursor.getString(productIdIndex);
                String name = cursor.getString(nameIndex);
                String price = cursor.getString(priceIndex);
                String type = cursor.getString(typeIndex);
                String imageName = cursor.getString(imageNameIndex);
                String createDate = cursor.getString(createDateIndex);
                String lastUpdate = cursor.getString(lastUpdateIndex);
                Product product = new Product(productID,name,price,type,imageName,createDate,lastUpdate);
                list.add(product);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    @Nullable
    public static Product getProduct(SQLiteDatabase db, String producId) {
        Cursor cursor = db.query(PRODUCTS_TABLE, null, PRODUCT_ID + " = ?", new String[]{producId}, null, null, null);
        if (cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int nameIndex = cursor.getColumnIndex(PRODUCT_NAME);
            int priceIndex = cursor.getColumnIndex(PRODUCT_PRICE);
            int typeIndex = cursor.getColumnIndex(PRODUCT_TYPE);
            int imageNameIndex = cursor.getColumnIndex(PRODUCT_IMAGE_NAME);
            int createDateIndex = cursor.getColumnIndex(PRODUCT_CREATED_DATE);
            int lastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATE);

            //get the product fields by index
            String productID = cursor.getString(productIdIndex);
            String name = cursor.getString(nameIndex);
            String price = cursor.getString(priceIndex);
            String type = cursor.getString(typeIndex);
            String imageName = cursor.getString(imageNameIndex);
            String createDate = cursor.getString(createDateIndex);
            String lastUpdate = cursor.getString(lastUpdateIndex);
            Product product = new Product(productID,name,price,type,imageName,createDate,lastUpdate);
            return product;
        }
        return null;
    }

    public static void updateProduct(SQLiteDatabase db, Product updatedProduct) {

        //build the variable of the inser query
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, updatedProduct.getProductId());
        values.put(PRODUCT_NAME, updatedProduct.getName());
        values.put(PRODUCT_PRICE, updatedProduct.getPrice());
        values.put(PRODUCT_TYPE, updatedProduct.getType());
        values.put(PRODUCT_IMAGE_NAME, updatedProduct.getImageName());
        values.put(PRODUCT_CREATED_DATE, updatedProduct.getCreateDate());
        values.put(PRODUCT_LAST_UPDATE, updatedProduct.getLastUpdate());
        //find the user according to product id
        db.update(PRODUCTS_TABLE,values, PRODUCT_ID + " = ?", new String[]{updatedProduct.getProductId()});
    }
}
