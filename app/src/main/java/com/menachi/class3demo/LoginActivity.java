package com.menachi.class3demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.authentication.Constants;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.User;

import org.apache.http.auth.AUTH;

import java.io.Serializable;

public class LoginActivity extends Activity {

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
        if(Model.instance().getUser() != null){
            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
            startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                TextView textView;
                username = (EditText) findViewById(R.id.user);
                password = (EditText) findViewById(R.id.password);
                // check if the user didn't fill username or password
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    toast = Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
                    textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setTextColor(Color.RED);
                    toast.show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instance().loginUser(username.getText().toString(), password.getText().toString(), new Model.LoginStatus() {
                        @Override
                        public void isLoggedIn(boolean status, User user) {
                            if(status){
                                Log.d("Tag", "The user is authenticated");
                                progressBar.setVisibility(View.GONE);
                                Model.instance().setCurrentUser(user);
                                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
                                startActivityForResult(intent, 0);
                            }else{
                                Log.d("TAG","Error with auth");
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Model.Tools.LOG_OUT) {
            setResult(Model.Tools.LOG_OUT);
            finish();
        }else if(resultCode == Model.Tools.onBackPressed){
            finish();
        }
    }

    //TODO need to imlement extra feature session timout
    private boolean isExpired(AuthData authData){
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

}
