package com.menachi.class3demo.Model;
import android.util.Log;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by refael yehuda on 6/1/2016.
 */
public class Product {
    String productId;
    String name;
    String price;
    String imageName;
    Date createDate;
    Date lastUpdate;
    //key = userId , value = {comment,grade}
    JSONObject comments;
    //iterate over JSONObject
//    Iterator<?> keys = jObject.keys();
//
//    while( keys.hasNext() ) {
//        String key = (String)keys.next();
//        Log.d("Tag", key + " : " + jObject.get(key));
//    }
//    String key = (String)keys.next();
//    if ( jObject.get(key) instanceof JSONObject ) {
//
//    }

    Product(){

    }

    public Product(String name, String price, String imageName){
        this.name = name;
        this.price = price;
        this.imageName = imageName;
        this.productId = productId;
        this.createDate = new Date();
        this.lastUpdate = new Date();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
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

    public JSONObject getComments() {
        return comments;
    }

    public void setComments(JSONObject comments) {
        this.comments = comments;
    }

}
