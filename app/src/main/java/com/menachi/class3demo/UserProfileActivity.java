package com.menachi.class3demo;

import android.os.Bundle;
import android.app.Activity;

import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;

public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User currentUser = Model.instance().getUser();
    }

}
