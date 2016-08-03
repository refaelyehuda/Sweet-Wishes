package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebase.client.annotations.Nullable;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by refael yehuda on 7/24/2016.
 */
public class UserSQL {

    private static final String USERS_TABLE = "Users";
    private static final String USERS_ID = "userId";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_FNAME = "fName";
    private static final String USERS_LNAME = "lName";
    private static final String USERS_IS_ADMIN = "isAdmin";
    private static final String USERS_BDATE = "birth_date";
    private static final String USERS_PASSWORD = "password";
    private static final String USERS_ADDRESS = "address";
    private static final String USERS_PROFILE_PICTURE = "profPicture";
    private static final String USERS_BILLING_NAME = "billingName";
    private static final String USERS_CARD_EXPIRY_DATE = "user_card_expiry_date";
    private static final String USERS_CREDIT_CARD = "user_credit_card";
    private static final String USERS_LAST_UPDATED = "last_update";

    public static void create(SQLiteDatabase db) {
        Log.d("TAG", "Creating users table");
        db.execSQL("create table " +
                USERS_TABLE      + " (" +
                USERS_ID + " TEXT," +
                USERS_IS_ADMIN + " TEXT," +
                USERS_ADDRESS + " TEXT," +
                USERS_LAST_UPDATED + " TEXT," +
                USERS_EMAIL      + " TEXT," +
                USERS_FNAME      + " TEXT," +
                USERS_LNAME      + " TEXT," +
                USERS_BDATE      + " TEXT," +
                USERS_BILLING_NAME + " TEXT," +
                USERS_CARD_EXPIRY_DATE + " TEXT," +
                USERS_CREDIT_CARD + " TEXT," +
                USERS_PASSWORD   + " TEXT," +
                USERS_PROFILE_PICTURE + " TEXT" +
                ");");
    }

    public static void addUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();
        values.put(USERS_ID, user.getUserId());
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_IS_ADMIN, user.getIsAdmin().toString());
        values.put(USERS_ADDRESS, user.getAddress());
        values.put(USERS_BILLING_NAME, user.getBillingInfo().get("billingName"));
        values.put(USERS_CARD_EXPIRY_DATE, user.getBillingInfo().get("userCardExpiryDate"));
        values.put(USERS_CREDIT_CARD, user.getBillingInfo().get("userCreditCard"));
        values.put(USERS_FNAME, user.getfName());
        values.put(USERS_LNAME, user.getlName());
        values.put(USERS_BDATE, user.getBirthDate());
        values.put(USERS_PASSWORD, user.getPassword());
        values.put(USERS_PROFILE_PICTURE, user.getProfPicture());
        values.put(USERS_LAST_UPDATED, user.getLastUpdate());
        db.insert(USERS_TABLE, null, values);
    }

    public static void delete(SQLiteDatabase db, User u) {
        db.delete(USERS_TABLE, USERS_EMAIL + " = ?", new String[]{u.getUserId()});
    }

    public static void drop(SQLiteDatabase db)  {
        db.execSQL("drop table " + USERS_TABLE);
    }

    public static List<User> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query(USERS_TABLE, null, null, null, null, null, null);
        List<User> list = new LinkedList<User>();
        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(USERS_ID);
            int emailIndex = cursor.getColumnIndex(USERS_EMAIL);
            int isAdminIndex = cursor.getColumnIndex(USERS_IS_ADMIN);
            int addressIndex = cursor.getColumnIndex(USERS_ADDRESS);
            int billingNameIndex = cursor.getColumnIndex(USERS_BILLING_NAME);
            int cardExpiryDateIndex = cursor.getColumnIndex(USERS_CARD_EXPIRY_DATE);
            int creditCardIndex = cursor.getColumnIndex(USERS_CREDIT_CARD);
            int fnameIndex = cursor.getColumnIndex(USERS_FNAME);
            int lnameIndex = cursor.getColumnIndex(USERS_LNAME);
            int bDateIndex = cursor.getColumnIndex(USERS_BDATE);
            int passwdIndex = cursor.getColumnIndex(USERS_PASSWORD);
            int profilePictureIndex = cursor.getColumnIndex(USERS_PROFILE_PICTURE);
            int lastUpdatedIndex = cursor.getColumnIndex(USERS_LAST_UPDATED);
            do {
                String userID = cursor.getString(userIdIndex);
                String email = cursor.getString(emailIndex);
                String isAdmin = cursor.getString(isAdminIndex);
                Boolean isAdminFlag = false;
                if(isAdmin.equals("true")){
                    isAdminFlag = true;
                }
                String address = cursor.getString(addressIndex);
                String billingName = cursor.getString(billingNameIndex);
                String cardExpiryDate = cursor.getString(cardExpiryDateIndex);
                String creditCard = cursor.getString(creditCardIndex);
                String fname = cursor.getString(fnameIndex);
                String lname = cursor.getString(lnameIndex);
                String bDate = cursor.getString(bDateIndex);
                String password = cursor.getString(passwdIndex);
                String profilePicture = cursor.getString(profilePictureIndex);
                String lastUpdated = cursor.getString(lastUpdatedIndex);
                User user = new User(userID,email,fname,lname,address,password,profilePicture,bDate,isAdminFlag);
                Map<String, String> billingInfo = new HashMap<String, String>();
                if(billingName != "" || cardExpiryDate != "" || creditCard != ""){
                    billingInfo.put("billingName", billingName);
                    billingInfo.put("userCreditCard",creditCard);
                    billingInfo.put("userCardExpiryDate",cardExpiryDate);
                    user.setBillingInfo(billingInfo);
                }
                list.add(user);
            }
            while (cursor.moveToNext());

        }
        return list;
    }

    @Nullable
    public static User getUser(SQLiteDatabase db, String userID) {
        Cursor cursor = db.query(USERS_TABLE, null, USERS_ID + " = ?", new String[]{userID}, null, null, null);
        if (cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex(USERS_EMAIL);
            int isAdminIndex = cursor.getColumnIndex(USERS_IS_ADMIN);
            int addressIndex = cursor.getColumnIndex(USERS_ADDRESS);
            int billingNameIndex = cursor.getColumnIndex(USERS_BILLING_NAME);
            int cardExpiryDateIndex = cursor.getColumnIndex(USERS_CARD_EXPIRY_DATE);
            int creditCardIndex = cursor.getColumnIndex(USERS_CREDIT_CARD);
            int fnameIndex = cursor.getColumnIndex(USERS_FNAME);
            int lnameIndex = cursor.getColumnIndex(USERS_LNAME);
            int bDateIndex = cursor.getColumnIndex(USERS_BDATE);
            int passwdIndex = cursor.getColumnIndex(USERS_PASSWORD);
            int profilePictureIndex = cursor.getColumnIndex(USERS_PROFILE_PICTURE);
            int lastUpdatedIndex = cursor.getColumnIndex(USERS_LAST_UPDATED);
            String email = cursor.getString(emailIndex);
            String isAdmin = cursor.getString(isAdminIndex);
            Boolean isAdminFlag = false;
            if(isAdmin.equals("true")){
                isAdminFlag = true;
            }
            String address = cursor.getString(addressIndex);
            String billingName = cursor.getString(billingNameIndex);
            String cardExpiryDate = cursor.getString(cardExpiryDateIndex);
            String creditCard = cursor.getString(creditCardIndex);
            String fname = cursor.getString(fnameIndex);
            String lname = cursor.getString(lnameIndex);
            String bDate = cursor.getString(bDateIndex);
            String password = cursor.getString(passwdIndex);
            String profilePicture = cursor.getString(profilePictureIndex);
            String lastUpdated = cursor.getString(lastUpdatedIndex);
            User user = new User(userID,email,fname,lname,address,password,profilePicture,bDate,isAdminFlag);
            Map<String, String> billingInfo = new HashMap<String, String>();
            if(billingName != "" || cardExpiryDate != "" || creditCard != ""){
                billingInfo.put("billingName", billingName);
                billingInfo.put("userCreditCard",creditCard);
                billingInfo.put("userCardExpiryDate",cardExpiryDate);
                user.setBillingInfo(billingInfo);
            }
            return user;
        }
        return null;
    }

    public static void updateUserByID(SQLiteDatabase db, User updatedUser) {

        //build the variable of the inser query
        ContentValues values = new ContentValues();
        values.put(USERS_ID, updatedUser.getUserId());
        values.put(USERS_EMAIL, updatedUser.getEmail());
        values.put(USERS_IS_ADMIN, updatedUser.getIsAdmin().toString());
        values.put(USERS_ADDRESS, updatedUser.getAddress());
        if(updatedUser.getBillingInfo() != null){
            values.put(USERS_BILLING_NAME, updatedUser.getBillingInfo().get("billingName"));
            values.put(USERS_CARD_EXPIRY_DATE, updatedUser.getBillingInfo().get("userCardExpiryDate"));
            values.put(USERS_CREDIT_CARD, updatedUser.getBillingInfo().get("userCreditCard"));
        }
        values.put(USERS_FNAME, updatedUser.getfName());
        values.put(USERS_LNAME, updatedUser.getlName());
        values.put(USERS_BDATE, updatedUser.getBirthDate());
        values.put(USERS_PASSWORD, updatedUser.getPassword());
        values.put(USERS_PROFILE_PICTURE, updatedUser.getProfPicture());
        values.put(USERS_LAST_UPDATED, updatedUser.getLastUpdate());
        //find the user according to user id
        db.update(USERS_TABLE,values, USERS_ID + " = ?", new String[]{updatedUser.getUserId()});
    }

    public static User userAuthenticate(SQLiteDatabase db, String email, String password) {
        Cursor cursor = db.query(USERS_TABLE, null, USERS_EMAIL + " = ?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(USERS_ID);
            int emailIndex = cursor.getColumnIndex(USERS_EMAIL);
            int isAdminIndex = cursor.getColumnIndex(USERS_IS_ADMIN);
            int addressIndex = cursor.getColumnIndex(USERS_ADDRESS);
            int billingNameIndex = cursor.getColumnIndex(USERS_BILLING_NAME);
            int cardExpiryDateIndex = cursor.getColumnIndex(USERS_CARD_EXPIRY_DATE);
            int creditCardIndex = cursor.getColumnIndex(USERS_CREDIT_CARD);
            int fnameIndex = cursor.getColumnIndex(USERS_FNAME);
            int lnameIndex = cursor.getColumnIndex(USERS_LNAME);
            int bDateIndex = cursor.getColumnIndex(USERS_BDATE);
            int passwdIndex = cursor.getColumnIndex(USERS_PASSWORD);
            int profilePictureIndex = cursor.getColumnIndex(USERS_PROFILE_PICTURE);
            int lastUpdatedIndex = cursor.getColumnIndex(USERS_LAST_UPDATED);
            String userEmail = cursor.getString(emailIndex);
            String userpassword = cursor.getString(passwdIndex);
            if(email.equals(userEmail) && password.equals(userpassword)) {
                String userID = cursor.getString(userIdIndex);
                String isAdmin = cursor.getString(isAdminIndex);
                Boolean isAdminFlag = false;
                if(isAdmin.equals("true")){
                    isAdminFlag = true;
                }
                String address = cursor.getString(addressIndex);
                String billingName = cursor.getString(billingNameIndex);
                String cardExpiryDate = cursor.getString(cardExpiryDateIndex);
                String creditCard = cursor.getString(creditCardIndex);
                String fname = cursor.getString(fnameIndex);
                String lname = cursor.getString(lnameIndex);
                String bDate = cursor.getString(bDateIndex);
                String profilePicture = cursor.getString(profilePictureIndex);
                String lastUpdated = cursor.getString(lastUpdatedIndex);
                User user = new User(userID,email,fname,lname,address,password,profilePicture,bDate,isAdminFlag);
                Map<String, String> billingInfo = new HashMap<String, String>();
                if(billingName != "" || cardExpiryDate != "" || creditCard != ""){
                    billingInfo.put("billingName", billingName);
                    billingInfo.put("userCreditCard",creditCard);
                    billingInfo.put("userCardExpiryDate",cardExpiryDate);
                    user.setBillingInfo(billingInfo);
                }
                return user;
            }
        }
        return null;
    }
}
