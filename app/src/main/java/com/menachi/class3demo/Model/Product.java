package com.menachi.class3demo.Model;
import android.util.Log;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by refael yehuda on 6/1/2016.
 */
public class Product {
    String productId;
    String name;
    String price;
    String type;
    String imageName;
    String createDate;
    String lastUpdate;
    static Integer Ids = 0;

    Product(){
        Ids++;
    }

    public Product(String name, String price,String type, String imageName){
        //this is a static variable that grow in each call to the constructor
        Ids++;
        this.name = name;
        this.price = price;
        this.type = type;
        this.imageName = imageName;
        this.createDate = Model.Tools.getCurrentDate();
        this.lastUpdate = Model.Tools.getCurrentDate();
        this.productId = Ids.toString();
    }

    public Product(String productId,String name, String price,String type, String imageName,String createDate,String lastUpdate){
        Ids++;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.imageName = imageName;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
