package com.menachi.class3demo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.menachi.class3demo.DateAndPickers.Date.DateEditText;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;

import java.io.Serializable;

public class SignupActivity extends Activity {

    EditText fName;
    EditText lName;
    EditText email;
    EditText address;
    DateEditText birthDate;
    EditText password;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressBar = (ProgressBar) findViewById(R.id.signupProgressBar);
        progressBar.setVisibility(View.GONE);
        Button signup = (Button) findViewById(R.id.signupBth);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                fName = (EditText) findViewById(R.id.fName);
                lName = (EditText) findViewById(R.id.lName);
                email = (EditText) findViewById(R.id.email);
                address = (EditText) findViewById(R.id.address);
                birthDate = (DateEditText) findViewById(R.id.birthDate);
                password = (EditText) findViewById(R.id.password);
                //this id is fective Id the uesr id's will generate by firebase and update the user object
                               User user = new User("1234",email.getText().toString()
                                       ,fName.getText().toString(),lName.getText().toString()
                                       ,address.getText().toString(),password.getText().toString(),
                       "145522",birthDate.getText().toString());
                Model.instance().signup(user, new Model.SignupStatus() {
                    @Override
                    public void isSignup(boolean status, User user) {
                        if(status){
                            Log.d("Tag", "The user is created");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Signup Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),ProductsActivity.class);
//                            intent.putExtra("User", (Parcelable) user);
                            startActivity(intent);
                        }else{
                            Log.d("TAG","Error with create user");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Signup Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

}
