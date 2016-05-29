package com.menachi.class3demo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.authentication.Constants;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.User;

import org.apache.http.auth.AUTH;

public class LoginActivity extends Activity  implements ModelFirebase.UserStatus{

    EditText username;
    EditText password;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        Button login = (Button) findViewById(R.id.loginBtn);
        Model.instance().modelFirebaseSetDelegate(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.user);
                password = (EditText) findViewById(R.id.password);
                progressBar.setVisibility(View.VISIBLE);
                User user = new User("1234","rerere","rerere@gmail.com","yakir","twito","1212","123456",
                        "145522","55254");
                Model.instance().signup(user);
                //Model.instance().loginUser(username.getText().toString(), password.getText().toString());
            }
        });
    }


    private boolean isExpired(AuthData authData){
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }


    @Override
    public void isLoggedIn(boolean status) {
        if(status){
            Log.d("Tag","The user is authenticated");
            progressBar.setVisibility(View.GONE);
        }else{
            Log.d("TAG","Error with auth");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void isSignup(boolean status) {
        if(status){
            Log.d("Tag","The user is signup successfully");
            progressBar.setVisibility(View.GONE);
        }else{
            Log.d("TAG","Error with signup user");
            progressBar.setVisibility(View.GONE);
        }
    }
}
