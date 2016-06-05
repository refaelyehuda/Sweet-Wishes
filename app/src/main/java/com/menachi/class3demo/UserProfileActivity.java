package com.menachi.class3demo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.menachi.class3demo.Fragments.PersonalInfo;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;

public class UserProfileActivity extends Activity implements PersonalInfo.Delegate {
    String currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User currentUser = Model.instance().getUser();

    }

    @Override
    public void onReturnToList() {
        Log.d("TAG", "Returning to List");
        Intent back = new Intent(this, ProductsActivity.class);
        startActivity(back);
        this.finish();
    }
}
