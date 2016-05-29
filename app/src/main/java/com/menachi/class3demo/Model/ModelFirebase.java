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

/**
 * Created by refael yehuda on 5/29/2016.
 */
public class ModelFirebase {
    public interface UserStatus{
        public void isLoggedIn(boolean status);
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
        //flags to mark which action we doing now
        boolean isLogin = true , isSignup = false;
        myFirebaseRef.authWithPassword(username, password, new MyAuthResutHandler(isLogin,isSignup));
    }
    public void createUser(User user) {
        //flags to mark which action we doing now
        boolean isLogin = false , isSignup = true;

        //FIXME need to check why wee got exception on this line
        myFirebaseRef.createUser(user.getEmail(), user.getPassword(), (Firebase.ResultHandler) new MyAuthResutHandler(isLogin,isSignup));

    }



    class MyAuthResutHandler implements Firebase.AuthResultHandler{

        boolean status = false;
        boolean isLogin;
        boolean isSignup;
        public MyAuthResutHandler(boolean isLogin,boolean isSignup){
            this.isLogin = isLogin;
            this.isSignup =isSignup;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            status = true;
            if(isLogin){
                userStatus.isLoggedIn(status);
            }else{
                userStatus.isSignup(status);
            }

        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            if(isLogin){
                userStatus.isLoggedIn(status);
            }else{
                userStatus.isSignup(status);
            }

        }
    }

}

