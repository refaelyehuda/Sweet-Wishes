package com.menachi.class3demo.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Comment {
    String commentId;
    String productId;
    String userId;
    String name;
    String imageName;
    String text;
    String lastUpdate;
    String grade;
    static Integer Ids = 0;
    public Comment(){

    }


    public Comment(String commentId, String productId, String userId, String name, String imageName, String text, String lastUpdate, String grade) {
        this.commentId = commentId;
        this.productId = productId;
        this.userId = userId;
        this.name = name;
        this.imageName = imageName;
        this.text = text;
        this.lastUpdate = lastUpdate;
        this.grade = grade;
    }

    public Comment(String productId, String userId,String name,String imageName,String text, String grade){
        Ids++;
        this.productId = productId;
        this.userId = userId;
        this.name = name;
        this.imageName = imageName;
        this.text = text;
        this.grade = grade;
        this.lastUpdate = Model.Tools.getCurrentDate();
        this.commentId = Ids.toString();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
