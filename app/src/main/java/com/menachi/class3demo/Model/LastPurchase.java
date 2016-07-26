package com.menachi.class3demo.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by refael yehuda on 7/24/2016.
 */
public class LastPurchase {
    String LastPurchaseID;
    String userID;
    Product product;
    String lastUpdate;
    public LastPurchase(){

    }

    public LastPurchase(String lastPurchaseID, String userID, Product product, String lastUpdate) {
        LastPurchaseID = lastPurchaseID;
        this.userID = userID;
        this.product = product;
        this.lastUpdate = lastUpdate;
    }

    public LastPurchase(String userID, Product product){
        this.userID =userID;
        this.product = product;
        this.lastUpdate = Model.Tools.getCurrentDate();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProducts(Product product) {
        this.product = product;
        this.lastUpdate = Model.Tools.getCurrentDate();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.lastUpdate = Model.Tools.getCurrentDate();
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastPurchaseID() {
        return LastPurchaseID;
    }

    public void setLastPurchaseID(String lastPurchaseID) {
        LastPurchaseID = lastPurchaseID;
    }
}
