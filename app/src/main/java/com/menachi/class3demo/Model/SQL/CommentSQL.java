package com.menachi.class3demo.Model.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebase.client.annotations.Nullable;
import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.Product;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by refael yehuda on 7/25/2016.
 */
public class CommentSQL {
    private static final String COMMENTS_TABLE = "Comments";
    private static final String PRODUCT_ID = "productId";
    private static final String COMMENT_ID = "commentId";
    private static final String USER_ID = "userId";
    private static final String COMMENT_NAME = "name";
    private static final String COMMENT_TEXT = "text";
    private static final String COMMENT_GRADE = "grade";
    private static final String COMMENT_IMAGE_NAME = "imageName";
    private static final String COMMENT_LAST_UPDATE = "lastUpdate";

    public static void create(SQLiteDatabase db) {
        Log.d("TAG", "Creating comments table");
        db.execSQL("create table " +
                COMMENTS_TABLE      + " (" +
                PRODUCT_ID + " TEXT," +
                COMMENT_ID + " TEXT," +
                USER_ID + " TEXT," +
                COMMENT_NAME + " TEXT," +
                COMMENT_TEXT      + " TEXT," +
                COMMENT_GRADE      + " TEXT," +
                COMMENT_IMAGE_NAME      + " TEXT," +
                COMMENT_LAST_UPDATE      + " TEXT" +
                ");");
    }
    public static void addComment(SQLiteDatabase db, Comment comment) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, comment.getProductId());
        values.put(COMMENT_ID, comment.getName());
        values.put(USER_ID, comment.getUserId());
        values.put(COMMENT_NAME, comment.getName());
        values.put(COMMENT_TEXT, comment.getImageName());
        values.put(COMMENT_GRADE, comment.getGrade());
        values.put(COMMENT_IMAGE_NAME, comment.getLastUpdate());
        values.put(COMMENT_LAST_UPDATE, comment.getLastUpdate());
        db.insert(COMMENTS_TABLE, null, values);
    }

    public static void deleteComment(SQLiteDatabase db, Comment comment) {
        db.delete(COMMENTS_TABLE, COMMENT_ID + " = ?", new String[]{comment.getCommentId()});
    }

    public static void drop(SQLiteDatabase db)  {
        db.execSQL("drop table " + COMMENTS_TABLE);
    }

    public static List<Comment> getCommentByProductId(SQLiteDatabase db,String productId) {
        Cursor cursor = db.query(COMMENTS_TABLE, null, PRODUCT_ID + " = ?", new String[]{productId}, null, null, null, null);
        List<Comment> list = new LinkedList<Comment>();
        if (cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int commentIdIndex = cursor.getColumnIndex(COMMENT_ID);
            int userIdIndex = cursor.getColumnIndex(USER_ID);
            int nameIndex = cursor.getColumnIndex(COMMENT_NAME);
            int textIndex = cursor.getColumnIndex(COMMENT_TEXT);
            int gradeIndex = cursor.getColumnIndex(COMMENT_GRADE);
            int imageNameIndex = cursor.getColumnIndex(COMMENT_IMAGE_NAME);
            int lastUpdateIndex = cursor.getColumnIndex(COMMENT_LAST_UPDATE);
            do {
                String productID = cursor.getString(productIdIndex);
                String commentId = cursor.getString(commentIdIndex);
                String userId = cursor.getString(userIdIndex);
                String name = cursor.getString(nameIndex);
                String text = cursor.getString(textIndex);
                String grade = cursor.getString(gradeIndex);
                String imageName = cursor.getString(imageNameIndex);
                String lastUpdate = cursor.getString(lastUpdateIndex);
                Comment comment= new Comment(commentId,productID,userId,name,imageName,text,lastUpdate,grade);
                list.add(comment);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    @Nullable
    public static Comment getComment(SQLiteDatabase db, String commentId) {
        Cursor cursor = db.query(COMMENTS_TABLE, null, COMMENT_ID + " = ?", new String[]{commentId}, null, null, null);
        if (cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex(PRODUCT_ID);
            int commentIdIndex = cursor.getColumnIndex(COMMENT_ID);
            int userIdIndex = cursor.getColumnIndex(USER_ID);
            int nameIndex = cursor.getColumnIndex(COMMENT_NAME);
            int textIndex = cursor.getColumnIndex(COMMENT_TEXT);
            int gradeIndex = cursor.getColumnIndex(COMMENT_GRADE);
            int imageNameIndex = cursor.getColumnIndex(COMMENT_IMAGE_NAME);
            int lastUpdateIndex = cursor.getColumnIndex(COMMENT_LAST_UPDATE);

            //get the comment fields by index
            String productID = cursor.getString(productIdIndex);
            String commentID = cursor.getString(commentIdIndex);
            String userId = cursor.getString(userIdIndex);
            String name = cursor.getString(nameIndex);
            String text = cursor.getString(textIndex);
            String grade = cursor.getString(gradeIndex);
            String imageName = cursor.getString(imageNameIndex);
            String lastUpdate = cursor.getString(lastUpdateIndex);
            Comment comment= new Comment(commentID,productID,userId,name,imageName,text,lastUpdate,grade);
            return comment;
        }
        return null;
    }

    public static void updateComment(SQLiteDatabase db, Comment updatedComment) {

        //build the variable of the inser query
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, updatedComment.getProductId());
        values.put(COMMENT_ID, updatedComment.getName());
        values.put(USER_ID, updatedComment.getUserId());
        values.put(COMMENT_NAME, updatedComment.getName());
        values.put(COMMENT_TEXT, updatedComment.getImageName());
        values.put(COMMENT_GRADE, updatedComment.getGrade());
        values.put(COMMENT_IMAGE_NAME, updatedComment.getLastUpdate());
        values.put(COMMENT_LAST_UPDATE, updatedComment.getLastUpdate());
        //find the user according to comment id
        db.update(COMMENTS_TABLE,values, COMMENT_ID + " = ?", new String[]{updatedComment.getCommentId()});
    }

}
