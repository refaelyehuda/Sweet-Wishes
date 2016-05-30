package com.menachi.class3demo.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by refael yehuda on 5/29/2016.
 */
public class ModelFirebase {
    public interface UserStatus{
        /**
         * return true if user is loggedIn and false otherwise
         * @param status
         */
        public void isLoggedIn(boolean status);

        /**
         * return true if user is created and false otherwise
         * @param status
         */
        public void isSignup (boolean status);
    }
    UserStatus userStatus;
    Firebase myFirebaseRef;


    ModelFirebase(Context context){
        Firebase.setAndroidContext(context);
        myFirebaseRef = new Firebase("https://dessers-project.firebaseio.com/");
    }

    public void setUserStatus(UserStatus userStatus){
        this.userStatus = userStatus;
    }

    public void Login(String username , String password){
        myFirebaseRef.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            boolean status = false;

            @Override
            public void onAuthenticated(AuthData authData) {
                status = true;
                userStatus.isLoggedIn(status);

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                userStatus.isLoggedIn(status);

            }
        });
    }
    public void createUser(User user) {
        //create a new user in firebase regitration
        myFirebaseRef.createUser(user.getEmail(), user.getPassword(), new HandleUserCreatetion(user));

    }
    /**
     * this class handle in the user creation response
     */
    class HandleUserCreatetion implements  Firebase.ValueResultHandler<Map<String, Object>>{
        boolean status;
        User user;
        //get the user to create in the DB after the registration
        public HandleUserCreatetion(User user){
            this.user = user;
        }
        @Override
        public void onSuccess(Map<String, Object> stringObjectMap) {
            status = true;
            String userId = (String) stringObjectMap.get("uid");
            user.setUserId(userId);
            Firebase userRef = myFirebaseRef.child("Users").child(userId);
            userRef.setValue(user);
            userStatus.isSignup(status);
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            userStatus.isSignup(status);
        }
    }

    public String getUserId(){
        AuthData authData = myFirebaseRef.getAuth();
        if (authData != null) {
            return authData.getUid();
        }
        return null;
    }

}

