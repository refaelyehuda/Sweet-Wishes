package com.menachi.class3demo.Model;

/**
 * Created by refael yehuda on 5/18/2016.
 */
public class User {
    String userId;
    String userName;
    String email;
    String fName;
    String lName;
    String profPicture;
    String address;
    String birthDate;
    String password;


    public User(String userId, String userName, String email, String fName, String lName,String address,String password, String profPicture, String birthDate) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.password = password;
        this.profPicture = profPicture;
        this.birthDate = birthDate;
    }

    public User(String userId) {
        this.userId = userId;
        this.userName = "";
        this.email = "";
        this.fName = "";
        this.lName = "";
        this.profPicture = "";
        this.birthDate = "";
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getProfPicture() {
        return profPicture;
    }

    public void setProfPicture(String profPicture) {
        this.profPicture = profPicture;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}